package org.ssh.pm.common.utils.view;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.web.servlet.view.document.AbstractExcelView;

public class ExcelView extends AbstractExcelView {

    @Override
    protected void buildExcelDocument(Map<String, Object> model,
            HSSFWorkbook workbook, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        HSSFSheet sheet;
        HSSFCell cell;
        sheet = workbook.createSheet("Spring");
        sheet.setDefaultColumnWidth((short) 12);
        // write a text at A1
        cell = getCell(sheet, 0, 0);
        setText(cell, "Spring-Excel 测试");
    }

}
