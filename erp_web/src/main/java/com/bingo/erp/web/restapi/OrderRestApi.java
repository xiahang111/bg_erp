package com.bingo.erp.web.restapi;

import cn.hutool.core.util.RandomUtil;
import com.bingo.erp.commons.entity.Admin;
import com.bingo.erp.utils.ExportExecUtil;
import com.bingo.erp.utils.ResultUtil;
import com.bingo.erp.web.global.SysConf;
import com.bingo.erp.xo.global.ExcelConf;
import com.bingo.erp.xo.service.AdminService;
import com.bingo.erp.xo.service.OrderService;
import com.bingo.erp.xo.vo.MaterialVO;
import com.bingo.erp.xo.vo.OrderRecordPageVO;
import com.bingo.erp.xo.vo.ProductRecordPageVO;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
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

    @Autowired
    private AdminService adminService;

    @PostMapping("commitOrder")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
    public String commitOrder(@RequestBody(required = true) MaterialVO materialVO) {

        try {
            List<String> fileNames = orderService.saveOrder(materialVO);

            return ResultUtil.result(SysConf.SUCCESS, fileNames);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.result(SysConf.Fail, e.getMessage());
        }

    }

    @PostMapping("getByUser")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
    public String getByUser(HttpServletRequest request, @RequestBody OrderRecordPageVO orderRecordPageVO) {

        if (request.getAttribute(SysConf.ADMIN_UID) == null) {
            return ResultUtil.result(SysConf.ERROR, "token用户过期");
        }

        Admin admin = adminService.getById(request.getAttribute(SysConf.ADMIN_UID).toString());


        return ResultUtil.result(SysConf.SUCCESS, orderService.getMaterialVOByUser(admin, orderRecordPageVO));
    }

    @GetMapping("getMaterialVOById")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
    public String getMaterialVOByUid(HttpServletRequest request, @RequestParam("orderUid") String orderUid) {

        if (request.getAttribute(SysConf.ADMIN_UID) == null) {
            return ResultUtil.result(SysConf.ERROR, "token用户过期");
        }

        log.info("orderId = " + orderUid);
        return ResultUtil.result(SysConf.SUCCESS, orderService.getMaterialVOByUid(orderUid));
    }


    @GetMapping("excelDownload")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
    public String excelDownload(HttpServletResponse response, @RequestParam(value = "fileNames") List<String> fileNames) {

        try {

            String outputFileName = "订单生产单" + RandomUtil.randomNumbers(6) + ".zip";
            // 设置response参数
            response.reset();
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename=" + new String((outputFileName).getBytes(), "iso-8859-1"));
            ServletOutputStream out = response.getOutputStream();

            ZipArchiveOutputStream zous = new ZipArchiveOutputStream(out);

            for (String fileName : fileNames) {
                File file = new File(ExcelConf.NEW_FILE_DICT + fileName);

                InputStream inputStream = new FileInputStream(file);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len;
                while ((len = inputStream.read(buffer)) != -1) {
                    baos.write(buffer, 0, len);
                }
                if (baos != null) {
                    baos.flush();
                }
                byte[] bytes = baos.toByteArray();

                //设置文件名
                ArchiveEntry entry = new ZipArchiveEntry(fileName);
                zous.putArchiveEntry(entry);
                zous.write(bytes);
                zous.closeArchiveEntry();
                if (baos != null) {
                    baos.close();
                }

            }

            if (zous != null) {
                zous.close();
            }

            log.info("文件下载成功 :   " + outputFileName);
            return ResultUtil.result(SysConf.SUCCESS, "文件下载成功");

        } catch (Exception e) {
            return ResultUtil.result(SysConf.Fail, "文件下载出现异常");
        }

    }


}
