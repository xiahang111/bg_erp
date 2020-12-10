package com.bingo.erp.web.restapi;

import com.bingo.erp.utils.ResultUtil;
import com.bingo.erp.utils.StringUtils;
import com.bingo.erp.web.annotion.AuthorityVerify.AuthorityVerify;
import com.bingo.erp.web.global.SysConf;
import com.bingo.erp.xo.order.service.StoreOriginalInfoService;
import com.bingo.erp.xo.order.service.StoreOriginalRecordInfoService;
import com.bingo.erp.xo.order.service.StoreRecordInfoService;
import com.bingo.erp.xo.order.service.StoreSummaryInfoService;
import com.bingo.erp.xo.order.vo.*;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api-web/store")
@Api(value = "库存相关api")
@Slf4j
public class StoreRestApi {

    @Resource
    private StoreSummaryInfoService storeSummaryInfoService;

    @Resource
    private StoreRecordInfoService storeRecordInfoService;

    @Resource
    private StoreOriginalInfoService storeOriginalInfoService;

    @Resource
    private StoreOriginalRecordInfoService storeOriginalRecordInfoService;


    /**
     * 获取库存数据
     * @param request
     * @param storeSummaryPageVO
     * @return
     */
    @PostMapping("getStoreSummary")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
    public String getStoreSummary(HttpServletRequest request, @RequestBody StoreSummaryPageVO storeSummaryPageVO) {

        return ResultUtil.result(SysConf.SUCCESS, storeSummaryInfoService.getStoreSummaryByPage(storeSummaryPageVO));

    }

    /**
     * 获取坯料数据
     * @param request
     * @param storeOriginalPageVO
     * @return
     */
    @PostMapping("getStoreOrigin")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
    public String getStoreOrigin(HttpServletRequest request, @RequestBody StoreOriginalPageVO storeOriginalPageVO) {

        return ResultUtil.result(SysConf.SUCCESS, storeOriginalInfoService.getStoreOriginal(storeOriginalPageVO));

    }


    /**
     * 新增仓库种类信息
     * @param request
     * @param storeSummaryVO
     * @return
     */
    @PostMapping("saveStoreSummary")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
    public String saveStoreSummary(HttpServletRequest request, @RequestBody StoreSummaryVO storeSummaryVO) {

        try {
            storeSummaryInfoService.saveStoreSummary(storeSummaryVO);
            return ResultUtil.result(SysConf.SUCCESS, "");
        } catch (Exception e) {
            return ResultUtil.result(SysConf.Fail, e.getMessage());
        }


    }

    /**
     * 新增坯料种类信息
     * @param request
     * @param storeOriginVO
     * @return
     */
    @PostMapping("saveStoreOrigin")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
    public String saveStoreOrigin(HttpServletRequest request, @RequestBody StoreOriginVO storeOriginVO) {

        try {
            storeOriginalInfoService.saveStoreOrigin(storeOriginVO);
            return ResultUtil.result(SysConf.SUCCESS, "");
        } catch (Exception e) {
            return ResultUtil.result(SysConf.Fail, e.getMessage());
        }


    }

    /**
     * 删除仓库种类信息
     * @param request
     * @param storeSummaryVO
     * @return
     */
    @PostMapping("deleteStoreSummary")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
    @AuthorityVerify
    public String deleteStoreSummary(HttpServletRequest request, @RequestBody StoreSummaryVO storeSummaryVO) {

        try {
            storeSummaryInfoService.deleteStoreSummary(storeSummaryVO);
            return ResultUtil.result(SysConf.SUCCESS, "");
        } catch (Exception e) {
            return ResultUtil.result(SysConf.Fail, e.getMessage());
        }


    }

    /**
     * 删除坯料种类信息
     * @param request
     * @param storeOriginVO
     * @return
     */
    @PostMapping("deleteStoreOrigin")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
    @AuthorityVerify
    public String deleteStoreOrigin(HttpServletRequest request, @RequestBody StoreOriginVO storeOriginVO) {

        try {
            storeOriginalInfoService.deleteStoreOrigin(storeOriginVO);
            return ResultUtil.result(SysConf.SUCCESS, "");
        } catch (Exception e) {
            return ResultUtil.result(SysConf.Fail, e.getMessage());
        }


    }

    @PostMapping("getStoreRecord")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
    public String getStoreRecord(HttpServletRequest request, @RequestBody StoreRecordPageVO storeRecordPageVO) {

        return ResultUtil.result(SysConf.SUCCESS, storeRecordInfoService.getStoreRecord(storeRecordPageVO));

    }

    @PostMapping("getStoreOriginRecord")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
    public String getStoreOriginRecord(HttpServletRequest request, @RequestBody StoreRecordPageVO storeRecordPageVO) {
        return ResultUtil.result(SysConf.SUCCESS, storeOriginalInfoService.getStoreOriginalRecord(storeRecordPageVO));
    }

    /**
     * 获取仓库库存报表
     * @param request
     * @param
     * @return
     */
    @PostMapping("getStoreStatement")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
    public String getStoreStatement(HttpServletRequest request, @RequestBody StoreStatementVO storeStatementVO) {
        try {
            return ResultUtil.result(SysConf.SUCCESS, storeRecordInfoService.getStoreStatement(storeStatementVO));
        }catch (Exception e){
            return ResultUtil.result(SysConf.Fail, e.getMessage());
        }

    }

    @PostMapping("saveStoreRecord")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
    public String saveStoreRecord(HttpServletRequest request, @RequestBody StoreRecordVO storeRecordPageVO) {

        try {
            storeRecordInfoService.saveStoreRecord(storeRecordPageVO);
            return ResultUtil.result(SysConf.SUCCESS, "");
        } catch (Exception e) {
            return ResultUtil.result(SysConf.Fail, e.getMessage());
        }


    }

    @PostMapping("saveStoreOriginalRecord")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
    public String saveStoreOriginalRecord(HttpServletRequest request, @RequestBody StoreOriginRecordVO storeOriginRecordVO) {
        try {
            storeOriginalInfoService.saveStoreOriginalRecord(storeOriginRecordVO);
            return ResultUtil.result(SysConf.SUCCESS, "");
        } catch (Exception e) {
            return ResultUtil.result(SysConf.Fail, e.getMessage());
        }
    }

    @GetMapping("callbackStoreRecord")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
    public String callbackStoreRecord(@RequestParam("recordUid") String recordUid,@RequestParam(value = "type",defaultValue = "",required = false) String type) {

        try {
            if(StringUtils.isNotBlank(type) && type.equals("origin")){
                storeOriginalRecordInfoService.callbackStoreRecord(recordUid);
            }else {
                storeRecordInfoService.callbackStoreRecord(recordUid);
            }
            return ResultUtil.result(SysConf.SUCCESS, "删除成功");
        } catch (Exception e) {
            return ResultUtil.result(SysConf.Fail, e.getMessage());
        }
    }

    @GetMapping("getStoreNameList")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
    public String getStoreNameList(HttpServletRequest request){
        try {
            return ResultUtil.result(SysConf.SUCCESS, storeRecordInfoService.getStoreNameList());
        } catch (Exception e) {
            return ResultUtil.result(SysConf.Fail, e.getMessage());
        }
    }

    @GetMapping("getOriginNameList")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
    public String getOriginNameList(HttpServletRequest request){
        try {
            return ResultUtil.result(SysConf.SUCCESS, storeOriginalInfoService.getOriginNameList());
        } catch (Exception e) {
            return ResultUtil.result(SysConf.Fail, e.getMessage());
        }
    }
}
