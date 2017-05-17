/*
 * Copyright (C) 2017 Fekete András Demeter
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package com.schedulerapp.ProcessClasses;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.util.CellUtil;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Timestamp;
import org.apache.poi.ss.usermodel.Cell;
/**
 *
 * @author Fekete András Demeter 
 */
public class ResultSetToXLS {
  private HSSFWorkbook workbook;
  private HSSFSheet sheet;
  private HSSFFont boldFont;
  private HSSFDataFormat format;
  private ResultSet resultSet;
  private FormatType[] formatTypes;

    /**
     *
     * @param resultSet query resultSet
     * @param formatTypes format types all String
     * @param sheetName workbook sheet name
     */
    public ResultSetToXLS(ResultSet resultSet, FormatType[] formatTypes, String sheetName) {
    workbook = new HSSFWorkbook();
    this.resultSet = resultSet;
    sheet = workbook.createSheet(sheetName);
    boldFont = workbook.createFont();
    boldFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
    format = workbook.createDataFormat();
    this.formatTypes = formatTypes;
  }

    /**
     *
     * @param resultSet query resultSet
     * @param sheetName workbook sheet name
     */
    public ResultSetToXLS(ResultSet resultSet, String sheetName) {
    this(resultSet, null, sheetName);
  }

  private FormatType getFormatType(Class _class) {
    if (_class == Integer.class || _class == Long.class) {
      return FormatType.INTEGER;
    } else if (_class == Float.class || _class == Double.class) {
      return FormatType.FLOAT;
    } else if (_class == Timestamp.class || _class == java.sql.Date.class) {
      return FormatType.DATE;
    } else {
      return FormatType.TEXT;
    }
  }

    /**
     *
     * @param outputStream datastream
     * @throws Exception Exception
     */
    public void generate(OutputStream outputStream) throws Exception {
    try {
      ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
      
      /*
      if (formatTypes != null && formatTypes.length != resultSetMetaData.getColumnCount()) {
        throw new IllegalStateException("Number of types is not identical to number of resultset columns. " +
            "Number of types: " + formatTypes.length + ". Number of columns: " + resultSetMetaData.getColumnCount());
      }
      */
      
      int currentRow = 0;
      HSSFRow row = sheet.createRow(currentRow);
      int numCols = resultSetMetaData.getColumnCount();
      boolean isAutoDecideFormatTypes;
      if (isAutoDecideFormatTypes = (formatTypes == null)) {
        formatTypes = new FormatType[numCols];
      }

      for (int i = 0; i < numCols; i++) {
        String title = resultSetMetaData.getColumnName(i + 1);
        writeCell(row, i, title, FormatType.TEXT, boldFont);
        if (isAutoDecideFormatTypes) {
          Class _class = Class.forName(resultSetMetaData.getColumnClassName(i + 1));
          formatTypes[i] = getFormatType(_class);
        }
      }

      currentRow++;

      // Write report rows
      while (resultSet.next()) {
        row = sheet.createRow(currentRow++);
        for (int i = 0; i < numCols; i++) {
          Object value = resultSet.getObject(i + 1);
          writeCell(row, i, value, FormatType.TEXT); //formatTypes[i])
        }
      }
    
      workbook.write(outputStream);
    }
    finally {
      outputStream.close();
    }
  }

    /**
     *
     * @param file output file
     * @throws Exception Exception
     */
    public void generate(File file) throws Exception {
    generate(new FileOutputStream(file));
  }

  private void writeCell(HSSFRow row, int col, Object value, FormatType formatType) throws Exception {
    writeCell(row, col, value, formatType, null, null);
  }

  private void writeCell(HSSFRow row, int col, Object value, FormatType formatType, HSSFFont font) throws Exception {
    writeCell(row, col, value, formatType, null, font);
  }

  private void writeCell(HSSFRow row, int col, Object value, FormatType formatType,
                         Short bgColor, HSSFFont font) throws Exception {
    Cell cell = CellUtil.createCell(row, col, null);
    if (value == null) {
      return;
    }

    if (font != null) {
      HSSFCellStyle style = workbook.createCellStyle();
      style.setFont(font);
      cell.setCellStyle(style);
    }

    switch (formatType) {
      case TEXT:
        cell.setCellValue(value.toString());
        break;
      case INTEGER:
        cell.setCellValue(((Number) value).intValue());
        CellUtil.setCellStyleProperty(cell, workbook, CellUtil.DATA_FORMAT,
            HSSFDataFormat.getBuiltinFormat(("#,##0")));
        break;
      case FLOAT:
        cell.setCellValue(((Number) value).doubleValue());
        CellUtil.setCellStyleProperty(cell, workbook, CellUtil.DATA_FORMAT,
            HSSFDataFormat.getBuiltinFormat(("#,##0.00")));
        break;
      case DATE:
        cell.setCellValue((Timestamp) value);
        CellUtil.setCellStyleProperty(cell, workbook, CellUtil.DATA_FORMAT,
            HSSFDataFormat.getBuiltinFormat(("m/d/yy")));
        break;
      case MONEY:
        cell.setCellValue(((Number) value).intValue());
        CellUtil.setCellStyleProperty(cell, workbook,
            CellUtil.DATA_FORMAT, format.getFormat("($#,##0.00);($#,##0.00)"));
        break;
      case PERCENTAGE:
        cell.setCellValue(((Number) value).doubleValue());
        CellUtil.setCellStyleProperty(cell, workbook,
            CellUtil.DATA_FORMAT, HSSFDataFormat.getBuiltinFormat("0.00%"));
    }

    if (bgColor != null) {
      CellUtil.setCellStyleProperty(cell, workbook, CellUtil.FILL_FOREGROUND_COLOR, bgColor);
      CellUtil.setCellStyleProperty(cell, workbook, CellUtil.FILL_PATTERN, HSSFCellStyle.SOLID_FOREGROUND);
    }
  }

    /**
     *
     */
    public enum FormatType {
    TEXT,
    INTEGER,
    FLOAT,
    DATE,
    MONEY,
    PERCENTAGE
  }
}
