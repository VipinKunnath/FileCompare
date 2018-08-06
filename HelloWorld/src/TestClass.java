import java.io.IOException;
import java.util.HashMap;

public class TestClass {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		
		ExcelUtils excel=new ExcelUtils();
		HashMap<Integer, HashMap> fileMap=excel.readCSVFile("C:\\Users\\vipk5\\OneDrive\\Desktop\\BaseFile.csv");
		
		
		
		
		
		
		

	}

}
