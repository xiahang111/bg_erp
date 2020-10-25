package com.bingo.erp.web.restapi;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bingo.erp.commons.entity.Admin;
import com.bingo.erp.commons.entity.Role;
import com.bingo.erp.config.jwt.Audience;
import com.bingo.erp.config.jwt.JwtHelper;
import com.bingo.erp.utils.*;
import com.bingo.erp.web.global.SysConf;
import com.bingo.erp.xo.order.global.MessageConf;
import com.bingo.erp.xo.order.global.RedisConf;
import com.bingo.erp.xo.order.global.SQLConf;
import com.bingo.erp.xo.order.service.AdminService;
import com.bingo.erp.xo.order.service.RoleService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api-web/auth")
@Slf4j
public class LoginRestApi {

    @Autowired
    private RedisUtil redisUtil;

    @Resource
    private AdminService adminService;

    @Autowired
    private JwtHelper jwtHelper;

    @Resource
    private RoleService roleService;

    @Value(value = "${isRememberMeExpiresSecond}")
    private int longExpiresSecond;

    @Value(value = "${tokenHead}")
    private String tokenHead;

    @Autowired
    private Audience audience;

    @ApiOperation(value = "用户登录", notes = "用户登录")
    @PostMapping("/login")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
    public String login(HttpServletRequest request,
                        @ApiParam(name = "username", value = "用户名或邮箱或手机号", required = false) @RequestParam(name = "username", required = false) String username,
                        @ApiParam(name = "password", value = "密码", required = false) @RequestParam(name = "password", required = false) String password,
                        @ApiParam(name = "isRememberMe", value = "是否记住账号密码", required = false) @RequestParam(name = "isRememberMe", required = false, defaultValue = "0") int isRememberMe) {

        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            return ResultUtil.result(SysConf.ERROR, "账号或密码不能为空");
        }

        String ip = IpUtils.getIpAddr(request);
        String limitCount = redisUtil.get(RedisConf.LOGIN_LIMIT + RedisConf.SEGMENTATION + ip);
        if (StringUtils.isNotEmpty(limitCount)) {
            Integer tempLimitCount = Integer.valueOf(limitCount);
            if (tempLimitCount >= 5) {
                return ResultUtil.result(SysConf.ERROR, "密码输错次数过多,已被锁定30分钟");
            }
        }
        Boolean isEmail = CheckUtils.checkEmail(username);
        Boolean isMobile = CheckUtils.checkMobileNumber(username);
        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
        if (isEmail) {
            queryWrapper.eq(SQLConf.EMAIL, username);
        } else if (isMobile) {
            queryWrapper.eq(SQLConf.MOBILE, username);
        } else {
            queryWrapper.eq(SQLConf.USER_NAME, username);
        }
        Admin admin = adminService.getOne(queryWrapper);
        if (admin == null) {
            // 设置错误登录次数
            return ResultUtil.result(SysConf.ERROR, String.format(MessageConf.LOGIN_ERROR, setLoginCommit(request)));
        }
        //验证密码
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        boolean isPassword = encoder.matches(password, admin.getPassWord());
        if (!isPassword) {
            //密码错误，返回提示
            return ResultUtil.result(SysConf.ERROR, String.format(MessageConf.LOGIN_ERROR, setLoginCommit(request)));
        }

        List<String> roleUids = new ArrayList<>();
        roleUids.add(admin.getRoleUid());
        List<Role> roles = (List<Role>) roleService.listByIds(roleUids);

        if (roles.size() <= 0) {
            return ResultUtil.result(SysConf.ERROR, MessageConf.NO_ROLE);
        }
        String roleNames = null;
        for (Role role : roles) {
            roleNames += (role.getRoleName() + ",");
        }
        String roleName = roleNames.substring(0, roleNames.length() - 2);
        long expiration = isRememberMe == 1 ? longExpiresSecond : audience.getExpiresSecond();
        String jwtToken = jwtHelper.createJWT(admin.getUserName(),
                admin.getUid(),
                roleName.toString(),
                audience.getClientId(),
                audience.getName(),
                expiration * 1000,
                audience.getBase64Secret());
        String token = tokenHead + jwtToken;
        Map<String, Object> result = new HashMap<>();
        result.put(SysConf.TOKEN, token);

        //进行登录相关操作
        Integer count = admin.getLoginCount() + 1;
        admin.setLoginCount(count);
        admin.setLastLoginIp(IpUtils.getIpAddr(request));
        admin.setLastLoginTime(new Date());
        admin.updateById();

        return ResultUtil.result(SysConf.SUCCESS, result);
    }


    /**
     * 设置登录限制，返回剩余次数
     * 密码错误五次，将会锁定10分钟
     *
     * @param request
     */
    private Integer setLoginCommit(HttpServletRequest request) {
        String ip = IpUtils.getIpAddr(request);
        String count = redisUtil.get(RedisConf.LOGIN_LIMIT + RedisConf.SEGMENTATION + ip);
        Integer surplusCount = 5;
        if (StringUtils.isNotEmpty(count)) {
            Integer countTemp = Integer.valueOf(count) + 1;
            surplusCount = surplusCount - countTemp;
            redisUtil.setEx(RedisConf.LOGIN_LIMIT + RedisConf.SEGMENTATION + ip, String.valueOf(countTemp), 10, TimeUnit.MINUTES);
        } else {
            surplusCount = surplusCount - 1;
            redisUtil.setEx(RedisConf.LOGIN_LIMIT + RedisConf.SEGMENTATION + ip, "1", 30, TimeUnit.MINUTES);
        }

        return surplusCount;
    }

    @ApiOperation(value = "用户信息", notes = "用户信息", response = String.class)
    @GetMapping(value = "/info")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
    public String info(HttpServletRequest request,
                       @ApiParam(name = "token", value = "token令牌", required = false) @RequestParam(name = "token", required = false) String token) {

        Map<String, Object> map = new HashMap<>();

        if (request.getAttribute(SysConf.ADMIN_UID) == null) {
            return ResultUtil.result(SysConf.ERROR, "token用户过期");
        }
        Admin admin = adminService.getById(request.getAttribute(SysConf.ADMIN_UID).toString());
        map.put(SysConf.TOKEN, token);
        //获取图片
        /*if (StringUtils.isNotEmpty(admin.getAvatar())) {
            map.put(SysConf.AVATAR, "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        }*/
        map.put(SysConf.AVATAR, "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");

        //加载这些角色所能访问的菜单页面列表
        //1)获取该管理员所有角色
        //List<String> roleUid = new ArrayList<>();
        //roleUid.add(admin.getRoleUid());
        //Collection<Role> roleList = roleService.listByIds(roleUid);
        String roleUid = admin.getRoleUid();
        Role role = roleService.getById(roleUid);
        map.put(SysConf.ROLES, role.getRoleName());
        map.put(SysConf.NAME, admin.getUserName());
        map.put(SysConf.REALNAME,admin.getNickName());
        return ResultUtil.result(SysConf.SUCCESS, map);
    }

    @ApiOperation(value = "退出登录", notes = "退出登录", response = String.class)
    @PostMapping(value = "/logout")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
    public String logout(@ApiParam(name = "token", value = "token令牌", required = false) @RequestParam(name = "token", required = false) String token) {
        String destroyToken = null;
        return ResultUtil.result(SysConf.SUCCESS, destroyToken);
    }

    /* ===================================================================================================*/

}
