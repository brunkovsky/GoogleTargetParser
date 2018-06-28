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
    private int i = 1;

    @Override
    public void init(String filename) {
        this.filename = filename;
        book = new XSSFWorkbook();
        sheet = book.createSheet();
        makeHeader();
    }

    @Override
    public void write(String path, String className, String description) {
        Row row = sheet.createRow(i++);
        row.createCell(0).setCellValue(path);
        row.createCell(1).setCellValue(className);
        row.createCell(2).setCellValue(description);
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
        row.createCell(1).setCellValue("ClassName");
        row.createCell(2).setCellValue("Description");
    }

    private void setColumnsWidth() {
        sheet.setColumnWidth(0, 25600);
        sheet.setColumnWidth(1, 25600);
        sheet.setColumnWidth(2, 25600);
    }
}
