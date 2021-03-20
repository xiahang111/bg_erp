package com.bingo.erp.web.restapi;

import com.bingo.erp.utils.ResultUtil;
import com.bingo.erp.web.global.SysConf;
import com.bingo.erp.xo.order.service.OrderProcessService;
import com.bingo.erp.xo.order.vo.OrderProcessAnalyzePageVO;
import com.bingo.erp.xo.order.vo.OrderProcessPageVO;
import com.bingo.erp.xo.order.vo.OrderProcessVO;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api-web/order-process")
@Api(value = "订单流程相关api")
@Slf4j
public class OrderProcessRestApi {

    @Resource
    private OrderProcessService orderProcessService;

    @PostMapping("getProcessRecord")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
    public String getByUser(HttpServletRequest request, @RequestBody OrderProcessPageVO orderProcessPageVO) {

        if (request.getAttribute(SysConf.ADMIN_UID) == null) {
            return ResultUtil.result(SysConf.ERROR, "token用户过期");
        }


        return ResultUtil.result(SysConf.SUCCESS, orderProcessService.getProcessRecord(orderProcessPageVO));
    }

    @PostMapping("updateProcessRecord")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
    public String updateProcessRecord(HttpServletResponse response, @RequestBody OrderProcessVO orderProcessVO) {

        try {
            orderProcessService.updateProcessRecord(orderProcessVO);
            return ResultUtil.result(SysConf.SUCCESS, "删除成功");
        } catch (Exception e) {
            return ResultUtil.result(SysConf.Fail, "删除失败！原因：" + e.getMessage());
        }

    }

    @GetMapping("deleteByUid")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
    public String deleteByOrderUid(HttpServletResponse response, @RequestParam(value = "processUid") String processUid) {

        try {
            orderProcessService.deleteByUid(processUid);
            return ResultUtil.result(SysConf.SUCCESS, "删除成功");
        } catch (Exception e) {
            return ResultUtil.result(SysConf.Fail, "删除失败！原因：" + e.getMessage());
        }

    }

    @PostMapping("upload")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
    public String uploadFile(HttpServletRequest request, @RequestParam(value = "file", required = false) MultipartFile file, HttpServletResponse response) {

        try {
            String adminUid = (String) request.getAttribute(SysConf.ADMIN_UID);
            if (adminUid == null) {
                return ResultUtil.result(SysConf.ERROR, "token用户过期");
            }
            orderProcessService.fileAnalyze(file);
            return ResultUtil.result(SysConf.SUCCESS, "导入成功!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.result(SysConf.Fail, e.getMessage());
        }

    }

    /**
     * 获取流程表解析数据列表
     *
     * @param request
     * @param
     * @return
     */
    @PostMapping("getProcessHistoryPage")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
    public String getProcessHistoryPage(HttpServletRequest request, @RequestBody OrderProcessAnalyzePageVO orderProcessAnalyzePageVO) {

        String adminUid = (String) request.getAttribute(SysConf.ADMIN_UID);
        if (adminUid == null) {
            return ResultUtil.result(SysConf.ERROR, "token用户过期");
        }
        try {

            return ResultUtil.result(SysConf.SUCCESS, orderProcessService.getProcessRecordAnalyze(orderProcessAnalyzePageVO));
        } catch (Exception e) {
            return ResultUtil.result(SysConf.Fail, "获取失败!原因:" + e.getMessage());
        }

    }

}













