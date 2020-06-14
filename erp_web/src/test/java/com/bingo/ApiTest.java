package com.bingo;

import com.bingo.erp.base.enums.MaterialColorEnums;
import com.bingo.erp.commons.entity.OrderInfo;
import com.bingo.erp.commons.entity.StoreSummaryInfo;
import com.bingo.erp.web.WebApplication;
import com.bingo.erp.xo.order.global.ExcelConf;
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

    @Resource
    private OrderService orderService;

    @Resource
    private StoreSummaryInfoMapper storeSummaryInfoMapper;


    @Test
    public void test1() {

        List<IndexOrderVO> vos = orderService.getIndexOrderInfo();

        for (IndexOrderVO vo : vos) {
            System.out.println(vo.toString());
        }

    }

    @Test
    public void test2() throws Exception {

        File file = new File(ExcelConf.NEW_FILE_DICT + "store.xls");

        POIFSFileSystem poifsFileSystem = new POIFSFileSystem(file);

        HSSFWorkbook workbook = new HSSFWorkbook(poifsFileSystem);

        HSSFSheet sheet = workbook.getSheetAt(0);


        for (int i = 0; i < sheet.getLastRowNum(); i++) {

            StoreSummaryInfo storeSummaryInfo = new StoreSummaryInfo();

            HSSFRow row = sheet.getRow(i);

            for (int j = 0; j < row.getLastCellNum(); j++) {

                if (j == 0) {
                    storeSummaryInfo.setMaterialName(row.getCell(j).getStringCellValue());
                }

                if (j == 1) {
                    String name = row.getCell(j).getStringCellValue();
                    storeSummaryInfo.setMaterialColor(MaterialColorEnums.getByName(name));
                }
                if (j == 2) {

                    storeSummaryInfo.setSpecification(row.getCell(j).getStringCellValue());
                }
                if (j == 3) {

                    storeSummaryInfo.setUnit(row.getCell(j).getStringCellValue());

                }
                if (j == 4) {

                    storeSummaryInfo.setPrice(new BigDecimal(row.getCell(j).getNumericCellValue()).setScale(2,BigDecimal.ROUND_HALF_UP));
                }
                if (j == 5) {

                    storeSummaryInfo.setWeight(new BigDecimal(row.getCell(j).getNumericCellValue()).setScale(2,BigDecimal.ROUND_HALF_UP));

                }
                if (j == 6) {
                    storeSummaryInfo.setMaterialNum(new BigDecimal(row.getCell(j).getNumericCellValue()+""));
                }
                if (j == 7) {
                    storeSummaryInfo.setTotalWeight(new BigDecimal(row.getCell(j).getNumericCellValue()).setScale(2,BigDecimal.ROUND_HALF_UP));
                }
                if (j == 8) {

                    storeSummaryInfo.setTotalPrice(new BigDecimal(row.getCell(j).getNumericCellValue()).setScale(2,BigDecimal.ROUND_HALF_UP));

                    storeSummaryInfoMapper.insert(storeSummaryInfo);

                    log.info("=========第" + i + "条数据插入成功===============");
                }


            }

        }


    }


}
