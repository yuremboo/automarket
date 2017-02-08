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
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class WorkWithExcel {

	public static List<GoodsDTO> readFromExcell(File file) {
		List<GoodsDTO> result = new ArrayList<>();
		try {
			FileInputStream inputStream = new FileInputStream(file);
			String ext = FilenameUtils.getExtension(file.getName());
			Iterator<Row> rowIterator = null;
			if(ext.equals("xls")) {
				HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
				HSSFSheet sheet = workbook.getSheetAt(0);
				rowIterator = sheet.iterator();
			} else if(ext.equals("xlsx")) {
				XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
				XSSFSheet sheet = workbook.getSheetAt(0);
				rowIterator = sheet.iterator();
			}
			if(rowIterator != null) {
				while(rowIterator.hasNext()) {
					Row row = rowIterator.next();
					GoodsDTO goodsDTO = new GoodsDTO();
					DataFormatter dataFormatter = new DataFormatter();
					for(Cell cell : row) {
						int columnIndex = cell.getColumnIndex();
						switch(columnIndex) {
							case 0:
								goodsDTO.setName(dataFormatter.formatCellValue(cell));
								break;
							case 1:
								goodsDTO.setStore(dataFormatter.formatCellValue(cell));
								break;
							case 2:
								goodsDTO.setCount(Integer.valueOf(dataFormatter.formatCellValue(cell)));
								break;
							case 3:
								goodsDTO.setDescription(dataFormatter.formatCellValue(cell));
							default:
								break;
						}
					}
					result.add(goodsDTO);
				}
			}
			inputStream.close();
		} catch(Exception e) {
			System.err.println(e);
		}
		return result;
	}

	private static Object getCellValue(Cell cell) {
		Object object;
		int i1 = cell.getCellType();
		if(i1 == Cell.CELL_TYPE_BOOLEAN) {
			object = cell.getBooleanCellValue();
		} else if(i1 == Cell.CELL_TYPE_NUMERIC) {
			object = cell.getNumericCellValue();
		} else if(i1 == Cell.CELL_TYPE_STRING) {
			object = cell.getStringCellValue();
		} else {
			object = null;
		}
		return object;
	}

	public static boolean writeToExcell(Map<Integer, ArrayList<Object>> data) {
		boolean result = true;
		XSSFWorkbook workbook = new XSSFWorkbook();
		CellStyle cellStyle = workbook.createCellStyle();
		DataFormat dateFormat = workbook.createDataFormat();
		cellStyle.setDataFormat(dateFormat.getFormat("[$-809]DD/MM/YY HH:MM;@"));
		XSSFSheet sheet = workbook.createSheet("Report");
		Set<Integer> keyset = data.keySet();
		int rownum = 0;
		for(Integer key : keyset) {
			Row row = sheet.createRow(rownum++);
			ArrayList<Object> objects = data.get(key);
			int cellNum = 0;
			for(Object obj : objects) {
				Cell cell = row.createCell(cellNum++);
				if(obj instanceof String) {
					cell.setCellValue((String) obj);
				} else if(obj instanceof Integer) {
					cell.setCellValue((Integer) obj);
				} else if(obj instanceof Date) {
					cell.setCellStyle(cellStyle);
					cell.setCellValue((Date) obj);
				}
			}
		}
		try {
			FileOutputStream outputStream = new FileOutputStream(new File(String.valueOf((new Date()).getTime()) + "report.xlsx"));
			workbook.write(outputStream);
			outputStream.close();

		} catch(FileNotFoundException e) {
			result = false;
			e.printStackTrace();
		} catch(IOException e) {
			result = false;
			e.printStackTrace();
		}
		return result;
	}
}
