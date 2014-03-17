package com.automarket.utils;

import java.io.*;
import java.lang.reflect.Array;
import java.text.DateFormat;
import java.util.*;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class WorkWithExcel {
	public static Map<Integer, List<Object>> readFromExcell(File file) {
		Map<Integer, List<Object>> result = new HashMap<Integer, List<Object>>();
		List<Object> list = new ArrayList<>();
		Object object = new Object();
		try {
			FileInputStream inputStream = new FileInputStream(file);
			String ext = FilenameUtils.getExtension(file.getName());
			Iterator<Row> rowIterator = null;
			if (ext.equals("xls")) {
				HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
				HSSFSheet sheet = workbook.getSheetAt(0);
				rowIterator = sheet.iterator();
			} else if (ext.equals("xlsx")) {
				XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
				XSSFSheet sheet = workbook.getSheetAt(0);
				rowIterator = sheet.iterator();
			}
			int i = 0;
			while(rowIterator.hasNext()) {
				Row row = rowIterator.next();
				Iterator<Cell> cellIterator = row.cellIterator();
				while(cellIterator.hasNext()) {
					Cell cell = cellIterator.next();
					switch (cell.getCellType()) {
					case Cell.CELL_TYPE_BOOLEAN:
						System.out.print(cell.getBooleanCellValue() + "\t\t");
						object = cell.getBooleanCellValue();
	                    break;
	                case Cell.CELL_TYPE_NUMERIC:
	                    System.out.print(cell.getNumericCellValue() + "\t\t");
	                    object = cell.getNumericCellValue();
	                    break;
	                case Cell.CELL_TYPE_STRING:
	                    System.out.print(cell.getStringCellValue() + "\t\t");
	                    object = cell.getStringCellValue();
	                    break;
					default:
						object = null;
						break;
					}
					list.add(object);
				}
				result.put(i, new ArrayList<>(list));
				list.clear();
				i++;
				System.out.println("");
			}
			inputStream.close();
		} catch (IOException exception) {
			exception.printStackTrace();
		}
		return result;
	}

    public static boolean writeToExcell(Map<Integer,ArrayList<Object>> data) {
        boolean result = true;
        XSSFWorkbook workbook = new XSSFWorkbook();
        CellStyle cellStyle = workbook.createCellStyle();
        DataFormat dateFormat = workbook.createDataFormat();
        cellStyle.setDataFormat(dateFormat.getFormat("[$-809]DD/MM/YY HH:MM;@"));
        XSSFSheet sheet = workbook.createSheet("Report");
        Set<Integer> keyset = data.keySet();
        int rownum = 0;
        for (Integer key : keyset) {
            Row row = sheet.createRow(rownum++);
            ArrayList<Object> objects = data.get(key);
            int cellNum = 0;
            for (Object obj:objects) {
                Cell cell = row.createCell(cellNum++);
                if (obj instanceof String) {
                    cell.setCellValue((String) obj);
                } else if (obj instanceof Integer) {
                    cell.setCellValue((Integer) obj);
                } else if (obj instanceof Date) {
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue((Date) obj);
                }
            }
        }
        try {
            FileOutputStream outputStream = new FileOutputStream(new File(String.valueOf((new Date()).getTime())
                    + "report.xlsx"));
            workbook.write(outputStream);
            outputStream.close();

        } catch (FileNotFoundException e) {
            result = false;
            e.printStackTrace();
        } catch (IOException e) {
            result = false;
            e.printStackTrace();
        }
        return result;
    }
}
