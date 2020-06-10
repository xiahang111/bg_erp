package com.bingo.erp.web.restapi;

import com.bingo.erp.utils.ResultUtil;
import com.bingo.erp.web.global.SysConf;
import com.bingo.erp.xo.service.MaterialCalculateRecordService;
import com.bingo.erp.xo.service.ProductCalculateRecordService;
import com.bingo.erp.xo.service.ProductService;
import com.bingo.erp.xo.vo.GlassCalculateVO;
import com.bingo.erp.xo.vo.GlassInfoPageVO;
import com.bingo.erp.xo.vo.ProductRecordPageVO;
import com.bingo.erp.xo.vo.MaterialCalculateVO;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/product")
@Api(value = "产品相关api")
@Slf4j
public class ProductRestApi {


    @Autowired
    private ProductService productService;

    @Autowired
    private ProductCalculateRecordService productCalculateRecordService;

    @Autowired
    private MaterialCalculateRecordService materialCalculateRecordService;

    @GetMapping("getAllProduct")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
    public String getAllProduct(HttpServletRequest request, @RequestParam(name = "productType", required = false) Integer productType) {

        if (null == productType) {
            productType = 1;
        }

        return ResultUtil.result(SysConf.SUCCESS, productService.getAllProducts(productType));
    }


    @PostMapping("glassCalculate")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
    public String glassCalculate(HttpServletRequest request, @RequestBody(required = true) GlassCalculateVO glassCalculateVO) {

        return ResultUtil.result(SysConf.SUCCESS, productService.glassCalculate(glassCalculateVO));
    }


    @GetMapping("getAllGlassCalculate")
    public String getAllGlassCalculate() {
        return ResultUtil.result(SysConf.SUCCESS, productCalculateRecordService.getAllGlassCalculateRecord());
    }

    @PostMapping("getGlassRecordByQuery")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
    public String getGlassRecordByQuery(@RequestBody ProductRecordPageVO productRecordPageVO) {

        return ResultUtil.result(SysConf.SUCCESS, productCalculateRecordService.getGlassRecordPage(productRecordPageVO));

    }

    @PostMapping("materialCalculate")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
    public String materialCalculate(HttpServletRequest request, @RequestBody(required = true) MaterialCalculateVO materialCalculateVO) {

        try {
            return ResultUtil.result(SysConf.SUCCESS, productService.materialCalculate(materialCalculateVO));
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.result(SysConf.Fail, e.getMessage());
        }

    }

    @PostMapping("getMaterialRecordByQuery")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
    public String getMaterialRecordByQuery(@RequestBody ProductRecordPageVO productRecordPageVO) {

        return ResultUtil.result(SysConf.SUCCESS, materialCalculateRecordService.getMaterialRecordPage(productRecordPageVO));

    }

    @PostMapping("getGlassInfo")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
    public String getGlassInfo(@RequestBody GlassInfoPageVO glassInfoPageVO) {

        return ResultUtil.result(SysConf.SUCCESS, productCalculateRecordService.getGlassInfo(glassInfoPageVO));

    }

}
