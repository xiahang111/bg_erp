package com.bingo.erp.web.restapi;

import com.bingo.erp.utils.ResultUtil;
import com.bingo.erp.web.global.SysConf;
import com.bingo.erp.xo.order.service.StoreOutsideService;
import com.bingo.erp.xo.order.vo.StoreOutsidePageVO;
import com.bingo.erp.xo.order.vo.StoreOutsideVO;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api-web/store-outside")
@Api(value = "在外材料相关api")
@Slf4j
public class StoreOutsideRestApi {

    @Resource
    private StoreOutsideService storeOutsideService;

    /**
     * 获取在外材料库存列表
     * @param request
     * @param storeOutsidePageVO
     * @return
     */
    @PostMapping("getStoreOutsideList")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
    public String getStoreOutsideList(HttpServletRequest request, @RequestBody StoreOutsidePageVO storeOutsidePageVO) {

        return ResultUtil.result(SysConf.SUCCESS, storeOutsideService.getList(storeOutsidePageVO));

    }

    /**
     * 保存在外材料修改数据
     * @param request
     * @param storeOutsideVO
     * @return
     */
    @PostMapping("saveStoreOutSideRecord")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
    public String saveStoreOutSideRecord(HttpServletRequest request, @RequestBody StoreOutsideVO storeOutsideVO){
        try {
            storeOutsideService.    updateStoreOutsideByUid(storeOutsideVO);
            return ResultUtil.result(SysConf.SUCCESS, "修改成功");
        }catch (Exception e){
            return ResultUtil.result(SysConf.Fail, "修改失败,原因:"+e.getMessage());
        }

    }

    /**
     * 保存在外材料修改数据
     * @param request
     * @param storeOutsideVO
     * @return
     */
    @PostMapping("updateStoreOutSideData")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
    public String updateStoreOutSideData(HttpServletRequest request, @RequestBody StoreOutsideVO storeOutsideVO){
        try {
            storeOutsideService.updateStoreOutsideData(storeOutsideVO);
            return ResultUtil.result(SysConf.SUCCESS, "修改成功");
        }catch (Exception e){
            return ResultUtil.result(SysConf.Fail, "修改失败,原因:"+e.getMessage());
        }

    }
}
