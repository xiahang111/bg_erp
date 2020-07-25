package com.bingo;

import com.bingo.erp.base.enums.MaterialColorEnums;
import com.bingo.erp.commons.entity.OrderInfo;
import com.bingo.erp.commons.entity.StoreOriginalInfo;
import com.bingo.erp.commons.entity.StoreSummaryInfo;
import com.bingo.erp.web.WebApplication;
import com.bingo.erp.xo.order.global.ExcelConf;
import com.bingo.erp.xo.order.mapper.StoreOriginalInfoMapper;
import com.bingo.erp.xo.order.mapper.StoreSummaryInfoMapper;
import com.bingo.erp.xo.order.service.OrderService;
import com.bingo.erp.xo.order.vo.IndexOrderVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = WebApplication.class)
@Slf4j
public class ApiTest {

    @Value(value = "${srcFileUrl}")
    private String SRC_FILE_URL;

    @Value(value = "${newFileDict}")
    private String NEW_FILE_DICT;

    @Resource
    private OrderService orderService;

    @Resource
    private StoreSummaryInfoMapper storeSummaryInfoMapper;

    @Resource
    private StoreOriginalInfoMapper storeOriginalInfoMapper;


    @Test
    public void test1() {

        List<IndexOrderVO> vos = orderService.getIndexOrderInfo();

        for (IndexOrderVO vo : vos) {
            System.out.println(vo.toString());
        }

    }

    @Test
    public void test2() throws Exception {

        File file = new File(NEW_FILE_DICT + "origin.xls");

        POIFSFileSystem poifsFileSystem = new POIFSFileSystem(file);

        HSSFWorkbook workbook = new HSSFWorkbook(poifsFileSystem);

        HSSFSheet sheet = workbook.getSheetAt(0);


        for (int i = 0; i < sheet.getLastRowNum(); i++) {

            StoreOriginalInfo storeOriginalInfo = new StoreOriginalInfo();

            HSSFRow row = sheet.getRow(i);

            for (int j = 0; j < row.getLastCellNum(); j++) {

                if (j == 0) {
                    storeOriginalInfo.setMaterialName(row.getCell(j).getStringCellValue());
                }

                if (j == 1) {

                    storeOriginalInfo.setSpecification(row.getCell(j).getStringCellValue());
                }
                if (j == 2) {

                    storeOriginalInfo.setUnit(row.getCell(j).getStringCellValue());

                }
                if (j == 4) {

                    storeOriginalInfo.setPrice(new BigDecimal(row.getCell(j).getNumericCellValue()).setScale(2,BigDecimal.ROUND_HALF_UP));
                }
                if (j == 3) {

                    storeOriginalInfo.setWeight(new BigDecimal(row.getCell(j).getNumericCellValue()).setScale(2,BigDecimal.ROUND_HALF_UP));

                }
                if (j == 5) {
                    storeOriginalInfo.setMaterialNum(new BigDecimal(row.getCell(j).getNumericCellValue()+""));
                }
                if (j == 6) {
                    storeOriginalInfo.setTotalWeight(new BigDecimal(row.getCell(j).getNumericCellValue()).setScale(2,BigDecimal.ROUND_HALF_UP));
                }
                if (j == 7) {

                    storeOriginalInfo.setTotalPrice(new BigDecimal(row.getCell(j).getNumericCellValue()).setScale(2,BigDecimal.ROUND_HALF_UP));

                    storeOriginalInfoMapper.insert(storeOriginalInfo);

                    log.info("=========第" + i + "条数据插入成功===============");
                }


            }

        }


    }


}
