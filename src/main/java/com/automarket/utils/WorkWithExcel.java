package com.automarket.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
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
}
