package com.temp.myself.until;

import com.alibaba.fastjson.JSONArray;
import org.apache.poi.ss.usermodel.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExcelUtils {

    public static List<Object> excelToShopIdList(InputStream inputStream) {
        List<Object> list = new ArrayList<>();

        Workbook workbook = null;
        try {
            workbook = WorkbookFactory.create(inputStream);
            inputStream.close();
            //工作表对象
            Sheet sheet = workbook.getSheetAt(0);
            //总行数
            int rowLength = sheet.getLastRowNum()+1;

            System.out.println("总行数有多少行"+rowLength);
            //工作表的列
            Row row = sheet.getRow(0);

            //总列数
            int colLength = row.getLastCellNum();
            System.out.println("总列数有多少列"+colLength);
            //得到指定的单元格
            Cell cell = row.getCell(0);
            for (int i = 0; i < rowLength; i++) {
                String[] con = new String[colLength];
                row = sheet.getRow(i);
                for (int j = 0; j < colLength; j++) {
                    cell = row.getCell(j);
                    // System.out.println(cell);
                    if (cell != null) {
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        String data = cell.getStringCellValue();
                        data = data.trim();
                        con[j] = data;
                        //System.out.print(data);
                    }
                }
                list.add(con);
                //System.out.println("+");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list ;
    }



}
