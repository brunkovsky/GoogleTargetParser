package org.antalis;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;

public class ExcelWriter implements Writer {
    private Sheet sheet;
    private Workbook book;
    private String filename;
    private int count = 1;

    ExcelWriter(String filename) {
        this.filename = filename;
        book = new XSSFWorkbook();
        sheet = book.createSheet();
        makeHeader();
    }

    @Override
    public void write(String path, String title, String className, String description) {
        Row row = sheet.createRow(count++);
        row.createCell(0).setCellValue(path);
        row.createCell(1).setCellValue(title);
        row.createCell(2).setCellValue(className);
        row.createCell(3).setCellValue(description);
    }

    @Override
    public void close() throws IOException {
        setColumnsWidth();
        book.write(new FileOutputStream(filename));
        book.close();
    }

    private void makeHeader() {
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("Path");
        row.createCell(1).setCellValue("Title");
        row.createCell(2).setCellValue("ClassName");
        row.createCell(3).setCellValue("Description");
    }

    private void setColumnsWidth() {
        sheet.setColumnWidth(0, 25000);
        sheet.setColumnWidth(1, 18000);
        sheet.setColumnWidth(2, 22000);
        sheet.setColumnWidth(3, 22000);
    }
}
