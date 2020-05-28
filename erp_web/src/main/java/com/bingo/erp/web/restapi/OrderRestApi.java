package com.bingo.erp.web.restapi;

import com.bingo.erp.utils.ExportExecUtil;
import com.bingo.erp.utils.ResultUtil;
import com.bingo.erp.web.global.SysConf;
import com.bingo.erp.xo.global.ExcelConf;
import com.bingo.erp.xo.service.OrderService;
import com.bingo.erp.xo.vo.MaterialVO;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

@RestController
@RequestMapping("/order")
@Api(value = "订单相关api")
@Slf4j
public class OrderRestApi {

    @Resource
    private OrderService orderService;

    @PostMapping("commitOrder")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
    public String commitOrder( @RequestBody(required = true) MaterialVO materialVO) {

        try {
            List<String> fileNames = orderService.saveOrder(materialVO);

            return ResultUtil.result(SysConf.SUCCESS, fileNames);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.result(SysConf.Fail, e.getMessage());
        }

    }

    @GetMapping("excelDownload")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
    public String excelDownload(HttpServletResponse response,@RequestParam(value = "fileName")String fileName){

        try {

            File file = new File(ExcelConf.NEW_FILE_DICT + fileName);

            ExportExecUtil.showExec(file,fileName,response);
            log.info("文件下载成功 :   " + fileName);
            return ResultUtil.result(SysConf.SUCCESS, "文件下载成功");

        }catch (Exception e){
            return ResultUtil.result(SysConf.Fail, "文件下载出现异常");
        }

    }
}
