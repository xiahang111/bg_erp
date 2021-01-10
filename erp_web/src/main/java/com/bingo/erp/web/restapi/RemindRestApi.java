package com.bingo.erp.web.restapi;

import com.bingo.erp.utils.ResultUtil;
import com.bingo.erp.web.global.SysConf;
import com.bingo.erp.xo.order.service.MaterialRemindConfigService;
import com.bingo.erp.xo.order.service.MessageRemindInfoService;
import com.bingo.erp.xo.order.vo.MessageRemindPageVO;
import com.bingo.erp.xo.order.vo.RemindConfigPageVO;
import com.bingo.erp.xo.order.vo.RemindConfigVO;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api-web/store-remind")
@Api(value = "在外材料相关api")
@Slf4j
public class RemindRestApi {

    @Resource
    private MaterialRemindConfigService materialRemindConfigService;

    @Resource
    private MessageRemindInfoService messageRemindInfoService;

    /**
     * 获取在外材料库存列表
     * @param request
     * @param remindConfigVO
     * @return
     */
    @PostMapping("saveRemindConfig")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
    public String getStoreOutsideList(HttpServletRequest request, @RequestBody RemindConfigVO remindConfigVO) {

        try {
            materialRemindConfigService.saveRemindConfig(remindConfigVO);
            return ResultUtil.result(SysConf.SUCCESS,"保存成功!");
        }catch (Exception e){
            return ResultUtil.result(SysConf.Fail,"保存失败!原因:"+e.getMessage());
        }
    }

    /**
     * 获取在外材料库存列表
     * @param request
     * @param
     * @return
     */
    @PostMapping("getRemindConfigList")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
    public String getStoreOutsideList(HttpServletRequest request, @RequestBody RemindConfigPageVO remindConfigPageVO) {

        try {
            return ResultUtil.result(SysConf.SUCCESS,materialRemindConfigService.getRemindConfigList(remindConfigPageVO));
        }catch (Exception e){
            return ResultUtil.result(SysConf.Fail,"保存失败!原因:"+e.getMessage());
        }
    }

    /**
     * 获取在外材料库存列表
     * @param request
     * @param
     * @return
     */
    @GetMapping("deleteRemindConfigByUid")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
    public String deleteRemindConfigByUid(HttpServletRequest request, @RequestParam String materialUid) {

        try {
            materialRemindConfigService.deleteRemindConfig(materialUid);
            return ResultUtil.result(SysConf.SUCCESS,"删除成功");
        }catch (Exception e){
            return ResultUtil.result(SysConf.Fail,"删除失败!原因:"+e.getMessage());
        }
    }


    /**
     * 获取未读的告警信息列表
     * @param request
     * @param
     * @return
     */
    @PostMapping("getMessageRemindList")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
    public String getMessageRemindList(HttpServletRequest request){

        String adminUid = (String) request.getAttribute(SysConf.ADMIN_UID);
        if (adminUid == null) {
            return ResultUtil.result(SysConf.ERROR, "token用户过期");
        }
        try {
            return ResultUtil.result(SysConf.SUCCESS,messageRemindInfoService.getMessageRemindList(adminUid));
        }catch (Exception e){
            return ResultUtil.result(SysConf.Fail,"获取失败!原因:"+e.getMessage());
        }

    }

    /**
     * 获取未读的告警信息列表
     * @param request
     * @param
     * @return
     */
    @PostMapping("getMessageRemindPage")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
    public String getMessageRemindPage(HttpServletRequest request, @RequestBody MessageRemindPageVO messageRemindPageVO){

        String adminUid = (String) request.getAttribute(SysConf.ADMIN_UID);
        if (adminUid == null) {
            return ResultUtil.result(SysConf.ERROR, "token用户过期");
        }
        try {
            messageRemindPageVO.setAdminUid(adminUid);
            return ResultUtil.result(SysConf.SUCCESS,messageRemindInfoService.getMessageRemindPage(messageRemindPageVO));
        }catch (Exception e){
            return ResultUtil.result(SysConf.Fail,"获取失败!原因:"+e.getMessage());
        }

    }


}
