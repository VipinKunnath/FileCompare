import java.util.HashMap;

public class ExcelDataManager {
	
	private String completeFilePath;
	public String getCompleteFilePath() {
		return completeFilePath;
	}


	public void setCompleteFilePath(String completeFilePath) {
		this.completeFilePath = completeFilePath;
	}


	@SuppressWarnings("rawtypes")
	private HashMap<Integer, HashMap> completeFileMap;
	
	@SuppressWarnings("rawtypes")
	public HashMap<Integer, HashMap> getCompleteFileMap() {
		return completeFileMap;
	}
	
	
	public void setCompleteFileMap(HashMap<Integer, HashMap> completeFileMap) {
		this.completeFileMap = completeFileMap;
	}
	
	
	
	
	

}
