package br.gov.rj.rio.iplanrio.aceitacao.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;



/**
 * Classe de utilizario para ler dados de uma planilha em Excel. Para que
 * funcione e necessario a utilizacao de uma biblioteca java chamada Apache POI
 * http://poi.apache.org/
 * 
 * @author Elias Nogueira <elias.nogueira@gmail.com>
 * 
 */
public class SpreadsheetData {

	

	public static List<Map<String, String>> readExcelDataList(String sheetName, String filePath, String tableName) {
		
		 boolean testaEsseRegistro = false;
		List<Map<String, String>> listaDeDados = new ArrayList<Map<String,String>>();
		try {
			HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(
					filePath));
			HSSFSheet sheet = workbook.getSheet(sheetName); 			
			HSSFCell[] boundaryCells = findCell(sheet, tableName);
			HSSFCell startCell = boundaryCells[0];
			HSSFCell endCell = boundaryCells[1];
			int startRow = startCell.getRowIndex() + 1;
			int endRow = endCell.getRowIndex() - 1;
			int startCol = startCell.getColumnIndex() + 1;
			int endCol = endCell.getColumnIndex() - 1;

			for (int i = startRow; i < endRow + 1; i++) {
				Map<String, String> dado = new HashMap<String, String>();
				for (int j = startCol; j < endCol + 1; j++) {
					if(sheet.getRow(i).getCell(j) == null || sheet.getRow(i).getCell(j).equals("")){
						dado.put(sheet.getRow(startRow-1).getCell(j).getStringCellValue(), "");
						
					}else {
						
						dado.put(sheet.getRow(startRow-1).getCell(j).getStringCellValue(), sheet.getRow(i).getCell(j).getStringCellValue());
						//ct - para testar somente o registro da planilha que tenha "testaEsse" na coluna selecionaRegistroPraLer
						if (j==1) {
							if (dado.put(sheet.getRow(startRow-1).getCell(j).getStringCellValue(), sheet.getRow(i).getCell(j).getStringCellValue()).equals("testaEsse")){
								testaEsseRegistro = true;
							//	j = endCol;  //força não ler todas as colunas
						}
					}
					}
				}
				
				if (testaEsseRegistro == true){
					testaEsseRegistro = false;
					listaDeDados.add(dado);
					
					
				};
			}

		} catch (FileNotFoundException e) {
			System.out.println("Nao pode carregar a planilha Excel");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Nao pode carregar a planilha Excel");
			e.printStackTrace();
		}

		return listaDeDados;

	}

	public static String[][] readExcelData(String sheetName, String filePath,
			String tableName) {
		String[][] testData = null;

		try {
			HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(
					filePath));
			HSSFSheet sheet = workbook.getSheet(sheetName);
			HSSFCell[] boundaryCells = findCell(sheet, tableName);
			HSSFCell startCell = boundaryCells[0];
			HSSFCell endCell = boundaryCells[1];
			int startRow = startCell.getRowIndex() + 1;
			int endRow = endCell.getRowIndex() - 1;
			int startCol = startCell.getColumnIndex() + 1;
			int endCol = endCell.getColumnIndex() - 1;

			testData = new String[endRow - startRow + 1][endCol - startCol + 1];

			for (int i = startRow; i < endRow + 1; i++) {
				for (int j = startCol; j < endCol + 1; j++) {
					testData[i - startRow][j - startCol] = sheet.getRow(i)
							.getCell(j).getStringCellValue();
				}
			}

		} catch (FileNotFoundException e) {
			System.out.println("Nao pode carregar a planilha Excel");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Nao pode carregar a planilha Excel");
			e.printStackTrace();
		}

		return testData;

	}

	public static HSSFCell[] findCell(HSSFSheet sheet, String text) {

		String pos = "start";

		HSSFCell[] cells = new HSSFCell[2];

		for (Row row : sheet) {
			for (Cell cell : row) {
				//FIXME Permitir uso de valores numericos
				
				
				try {
					
					if (text.equals(cell.getStringCellValue())) {
						if (pos.equalsIgnoreCase("start")) {
							cells[0] = (HSSFCell) cell;
							pos = "end";
						} else {
							cells[1] = (HSSFCell) cell;
						}
					}
					
				} catch (IllegalStateException e) {
					System.out.println("erro na linha " + cell.getRowIndex() + " e na coluna " + cell.getColumnIndex()  );
					e.printStackTrace();
					
					throw e;
				}

			}
		}
		return cells;
	}
}