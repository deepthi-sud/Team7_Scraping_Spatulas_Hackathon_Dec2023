package com.recipe.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtility {
	
	public FileInputStream fi;
	public FileOutputStream fo;
	public XSSFWorkbook workbook;
	public XSSFSheet sheet;
	public XSSFRow row;
	public XSSFCell cell;
	public CellStyle style;
	
	String path;
		
	public ExcelUtility(String path) {
		this.path=path;
	}

	public int getRowCount(String SheetName) throws IOException
	{
		fi = new FileInputStream(path);
		workbook = new XSSFWorkbook(fi);
		sheet = workbook.getSheet(SheetName);
		int rowcount = sheet.getLastRowNum();
		workbook.close();
		fi.close();
		return rowcount;
	}
	
	public int getColCount(String SheetName, int rownum) throws IOException
	{
		fi = new FileInputStream(path);
		workbook = new XSSFWorkbook(fi);
		sheet = workbook.getSheet(SheetName);
		row = sheet.getRow(rownum);
		int colcount = row.getLastCellNum();
		workbook.close();
		fi.close();
		return colcount;
	}
	
	public String getCellData(String SheetName, int rownum, int colcount) throws IOException
	{
		fi = new FileInputStream(path);
		workbook = new XSSFWorkbook(fi);
		sheet = workbook.getSheet(SheetName);
		row = sheet.getRow(rownum);
		cell = row.getCell(colcount);
		
		DataFormatter formatter = new DataFormatter();
		String data;
		try {
			data = formatter.formatCellValue(cell); // Returns the formatted value of a cell as a String regardless of the 
		}
		catch (Exception e) {
			data = "";			
		}
		workbook.close();
		fi.close();
		return data;		
	}
	
	public void setCellData (String SheetName, int rownum, int colcount, String data) throws IOException
	{
	
		File xlfile = new File(path);
		if(!xlfile.exists())// If file not exists then create new file
		{
			workbook = new XSSFWorkbook();
			fo = new FileOutputStream(path);
			workbook.write(fo);
		}
				
		fi = new FileInputStream(path);
		workbook = new XSSFWorkbook(fi);
		
		if(workbook.getSheetIndex(SheetName)== -1) // If sheet not exists then create new sheet
			workbook.createSheet(SheetName);
		sheet = workbook.getSheet(SheetName);
		
		
		if (sheet.getRow(rownum) == null) // If row not exists, then create new row
			sheet.createRow(rownum);
		row = sheet.getRow(rownum);
		
		cell = row.createCell(colcount);
		cell.setCellValue(data);
		
		fo = new FileOutputStream(path);
		workbook.write(fo);
		workbook.close();
		fi.close();
		fo.close();
		
	}
		
	
//Read data from Excel
	static String path1 = ".\\TestData\\Ingredientsandcomorbidities.xlsx";

	public static ArrayList<String> readDataFromSheet(int sheetIndex, int colNum) throws IOException {
	    ArrayList<String> dataList = new ArrayList<>();

	    try (FileInputStream inputStream = new FileInputStream(new File(path1));
	         XSSFWorkbook workbook = new XSSFWorkbook(inputStream)) {

	        XSSFSheet sheet = workbook.getSheetAt(sheetIndex);
	        int rowIndex = 0;

	        for (Row row : sheet) {
	            if (rowIndex >= 2) {
	                Cell cell = row.getCell(colNum);
	                if (cell != null && cell.getCellType() == CellType.STRING) {
	                    dataList.add(cell.getStringCellValue());
	                }
	            }
	            rowIndex++;
	        }
	    }

	    System.out.println(dataList);
	    return dataList;
	}
	}




