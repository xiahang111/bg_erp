package com.bingo.erp.web.restapi;

import com.bingo.erp.utils.ResultUtil;
import com.bingo.erp.web.annotion.AuthorityVerify.AuthorityVerify;
import com.bingo.erp.web.global.SysConf;
import com.bingo.erp.xo.order.service.StoreRecordInfoService;
import com.bingo.erp.xo.order.service.StoreSummaryInfoService;
import com.bingo.erp.xo.order.vo.StoreRecordPageVO;
import com.bingo.erp.xo.order.vo.StoreRecordVO;
import com.bingo.erp.xo.order.vo.StoreSummaryPageVO;
import com.bingo.erp.xo.order.vo.StoreSummaryVO;
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


    @PostMapping("getStoreSummary")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
    public String getStoreSummary(HttpServletRequest request, @RequestBody StoreSummaryPageVO storeSummaryPageVO) {

        return ResultUtil.result(SysConf.SUCCESS, storeSummaryInfoService.getStoreSummaryByPage(storeSummaryPageVO));

    }

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

    @PostMapping("getStoreRecord")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
    public String getStoreRecord(HttpServletRequest request, @RequestBody StoreRecordPageVO storeRecordPageVO) {

        return ResultUtil.result(SysConf.SUCCESS, storeRecordInfoService.getStoreRecord(storeRecordPageVO));

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

    @GetMapping("callbackStoreRecord")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
    public String callbackStoreRecord(@RequestParam("recordUid") String recordUid) {

        try {
            storeRecordInfoService.callbackStoreRecord(recordUid);
            return ResultUtil.result(SysConf.SUCCESS, "删除成功");
        } catch (Exception e) {
            return ResultUtil.result(SysConf.Fail, e.getMessage());
        }
    }
}
