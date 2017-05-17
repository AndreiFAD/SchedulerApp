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

import java.io.File;
import java.io.FileOutputStream;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Timestamp;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

/**
 *
 * @author Fekete András Demeter 
 */

public class ResultSetToXLSX {
  private SXSSFWorkbook workbook;
  private SXSSFSheet sheet;
  private Font boldFont;
  private DataFormat format;
  private ResultSet resultSet;
  private FormatType[] formatTypes;

    /**
     *
     * @param resultSet query resultset
     * @param formatTypes format types
     * @param sheetName workbook sheet Name
     * @throws Exception Exception
     */
    public ResultSetToXLSX(ResultSet resultSet, FormatType[] formatTypes, String sheetName) throws Exception {
    workbook = new SXSSFWorkbook(1000);
    
    this.resultSet = resultSet;
    sheet = workbook.createSheet(sheetName);
    boldFont = workbook.createFont();
    boldFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
    format = workbook.createDataFormat();
    this.formatTypes = formatTypes;
  }

    /**
     *
     * @param resultSet query resultset
     * @param sheetName workbook sheet Name
     * @throws Exception Exception
     */
    public ResultSetToXLSX(ResultSet resultSet, String sheetName )throws Exception {
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
    public void generate(FileOutputStream outputStream) throws Exception {
    try {
      ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
      /*
      if (formatTypes != null && formatTypes.length != resultSetMetaData.getColumnCount()) {
        throw new IllegalStateException("Number of types is not identical to number of resultset columns. " +
            "Number of types: " + formatTypes.length + ". Number of columns: " + resultSetMetaData.getColumnCount());
      }
     */

      int currentRow = 0;
      Row row =  sheet.createRow(currentRow);
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
        SXSSFRow rowe = (SXSSFRow) sheet.createRow(currentRow++);
        for (int i = 0; i < numCols; i++) {
          Object value = resultSet.getObject(i + 1);
          writeCell(rowe, i, value, FormatType.TEXT);
        }
      }
      
      /*
      
      // Autosize columns
      for (int i = 0; i < numCols; i++) {
        sheet.autoSizeColumn((short) i);
      }
      
      */
      
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

  private void writeCell(Row row, int col, Object value, FormatType formatType) throws Exception {
    writeCell(row, col, value, formatType, null, null);
  }

  private void writeCell(Row row, int col, Object value, FormatType formatType, Font font) throws Exception {
    writeCell(row, col, value, formatType, null, font);
  }

  private void writeCell(Row rowe, int col, Object value, FormatType formatType,
                         Short bgColor, Font font) throws Exception {
    SXSSFCell cell = (SXSSFCell) rowe.createCell(col);

    if (value == null) {
      return ;
    }

    if (font != null) {
      CellStyle style = workbook.createCellStyle();
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
      CellUtil.setCellStyleProperty(cell, workbook, CellUtil.FILL_PATTERN, CellStyle.SOLID_FOREGROUND);
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
