import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtils {
	
	
	public static void readExcelFile(String filePath) {
		
		
		
		
	}
	
	
	
	@SuppressWarnings("rawtypes")
	public static  HashMap<Integer, HashMap> readCSVFile(String filePath) throws IOException {
		
		File file=new File(filePath);
		FileReader fr=new FileReader(file);
		BufferedReader br=new BufferedReader(fr);
		String line="";
		int i=0;
  		HashMap<Integer, HashMap> fileMap = new HashMap<>();
		//HashMap<Integer,String> rowMap = new HashMap<>();
		String tempArr[];
		while((line=br.readLine())!=null) {
			//System.out.println(line);
			tempArr=line.split(",");
			int length=tempArr.length;
			int j=0;
			HashMap<Integer,String> rowMap = new HashMap<>();
			while(length>0) {
				
				rowMap.put(j, tempArr[j]);
				
				length--;
				j++;
			}
			fileMap.put(i, rowMap);
			i++;
			
		}
		
		br.close();
		
		
		return fileMap;
		
		
		
		
		
	}
	
	
	public static void highlightDifference(ExcelDataManager diffExcel,int rowNum,int colNum) throws InvalidFormatException, IOException {
		
		File diffFile=new File(diffExcel.getCompleteFilePath());
		FileInputStream fis=new FileInputStream(diffFile);
		
		XSSFWorkbook wb=new XSSFWorkbook(fis);
		Sheet sheet=wb.getSheetAt(0);
		CellStyle style = wb.createCellStyle();
	    style.setFillForegroundColor(IndexedColors.RED.getIndex());
	    style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	    Row row=sheet.getRow(rowNum);
	    Cell cell=row.getCell(colNum);
	    cell.setCellStyle(style);
	    fis.close();
	    FileOutputStream fos=new FileOutputStream(diffFile);
	    wb.write(fos);
	    fos.close();
	   
	    
	    
	    
	    
		
		
		
		
		
		
		
		
	}
	
	
	
	

}
