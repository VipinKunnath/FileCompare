import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import javax.swing.JProgressBar;

public class NewFileCompare {

	private JFrame frame;
	private JTextField textField;
	private JTextField textField_1;
	private JProgressBar progressBar;
	private ExcelDataManager baseFileExcel;
	private ExcelDataManager newFileExcel;
	private ExcelDataManager diffFileExcel;
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					NewFileCompare window = new NewFileCompare();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public NewFileCompare() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				
				
			}
		});
		frame.getContentPane().setBackground(new Color(250, 250, 210));
		frame.setBounds(100, 100, 537, 330);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblBaseFilePath = new JLabel("Base File Path");
		lblBaseFilePath.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblBaseFilePath.setHorizontalAlignment(SwingConstants.CENTER);
		lblBaseFilePath.setForeground(Color.BLUE);
		lblBaseFilePath.setBounds(27, 43, 130, 33);
		frame.getContentPane().add(lblBaseFilePath);
		
		JLabel lblNewFilePath = new JLabel("New File Path");
		lblNewFilePath.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewFilePath.setForeground(Color.BLUE);
		lblNewFilePath.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNewFilePath.setBounds(27, 82, 130, 33);
		frame.getContentPane().add(lblNewFilePath);
		
		textField = new JTextField();
		
		textField.setBounds(180, 51, 209, 20);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(180, 90, 209, 20);
		frame.getContentPane().add(textField_1);
		
		JLabel lblSelectKeyTo = new JLabel("Select Key To Compare");
		lblSelectKeyTo.setHorizontalAlignment(SwingConstants.CENTER);
		lblSelectKeyTo.setForeground(Color.BLUE);
		lblSelectKeyTo.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblSelectKeyTo.setBounds(27, 126, 192, 33);
		frame.getContentPane().add(lblSelectKeyTo);
		
		JComboBox comboBox = new JComboBox();
		
		comboBox.setBounds(277, 134, 89, 20);
		frame.getContentPane().add(comboBox);
		
		JButton btnCompare = new JButton("Compare");
		btnCompare.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				String value=(String)comboBox.getSelectedItem();
				try {
					createDifferenceFile(newFileExcel);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				performFileComparison(value,baseFileExcel,newFileExcel,diffFileExcel);
				
				JOptionPane.showMessageDialog(frame,"Comparison is completed and difference file is found at "+diffFileExcel.getCompleteFilePath());
			}

			@SuppressWarnings("unchecked")
			private void createDifferenceFile(ExcelDataManager newFileExcel) throws IOException {
				// TODO Auto-generated method stub
				diffFileExcel=new ExcelDataManager();
				FileOutputStream diffFile=null;
				HashMap<Integer, HashMap> newFileMap=newFileExcel.getCompleteFileMap();
				File newFile=new File(newFileExcel.getCompleteFilePath());
				String absolutPath=newFile.getAbsolutePath();
				String diffFilePath=absolutPath.substring(0,absolutPath.lastIndexOf(File.separator));
				Workbook workbook=new XSSFWorkbook();
				Sheet sheet=workbook.createSheet("Diference");
				Row row=null;
				Cell cell=null;
				HashMap<Integer, String> rowMap=null;
				for(Integer key:newFileMap.keySet()) {
					
					 row=sheet.createRow(key);
					 rowMap=newFileMap.get(key);
					 for(Integer key1:rowMap.keySet()) {
					 cell=row.createCell(key1);
					 cell.setCellValue(rowMap.get(key1));
					 }
				}
				
				try {
					String diffFileName=diffFilePath+"\\DiffFile_"+new Random().nextInt(100)+".xlsx";
					diffFileExcel.setCompleteFilePath(diffFileName);
					diffFile=new FileOutputStream(diffFileName);
					workbook.write(diffFile);
					diffFile.close();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				finally {
					if(diffFile!=null)diffFile.close();
				}
				
				
				
			}

			private void performFileComparison(String value, ExcelDataManager baseFileExcel,
					ExcelDataManager newFileExcel, ExcelDataManager diffFileExcel) {
				
				int columnKey=findColumnNumOfKey(value,baseFileExcel);
				HashMap<Integer, HashMap> baseFileMap=baseFileExcel.getCompleteFileMap();
				
				int rowSize=baseFileMap.keySet().size();
				progressBar.setVisible(true);
				progressBar.setValue(0);
				
				for(Integer baseFileKey:baseFileMap.keySet()) {
					
					
					HashMap<Integer, String> rowMap=baseFileMap.get(baseFileKey);
					String valueToCompare=rowMap.get(columnKey);
					boolean diffYes=compareWithNewFile(baseFileKey,columnKey,valueToCompare);
					monitorFileCompareProgress(baseFileKey,rowSize);
				//	String valueToCompare=
					
					
					
					
				}
				
				//System.out.println(columnKey);
				
				
				
				
				
				
			}

			private void monitorFileCompareProgress(int baseFileKey, int rowSize) {
				// TODO Auto-generated method stub
				int progress=((baseFileKey+1)/rowSize)*100;
				progressBar.setValue(progress);
				
				
				
				
			}

			private boolean compareWithNewFile(int rowKey,int columnKey,String valueToCompare) {
				// TODO Auto-generated method stub
				int rowNewFile=checkIfKeyPresentInNewFile(columnKey,valueToCompare);
				if(rowNewFile>=0) {
					
					@SuppressWarnings("unchecked")
					HashMap<Integer,String> rowMapBase=baseFileExcel.getCompleteFileMap().get(rowKey);
					@SuppressWarnings("unchecked")
					HashMap<Integer,String> rowMapNew=newFileExcel.getCompleteFileMap().get(rowNewFile);
					
						
					for(Integer key1:rowMapBase.keySet()) {
					String value1=(String) rowMapBase.get(key1);
					String value2=(String) rowMapNew.get(key1);
					if(value1.equals(value2)) {
						
						
						
						//System.out.println("Cell "+rowKey+" "+key1+" is correct");
						
			
					}
					
					else {
						
						try {
							ExcelUtils.highlightDifference(diffFileExcel, rowNewFile, key1);
						} catch (InvalidFormatException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				//		System.out.println("Cell "+rowKey+" "+key1+" is incorrect");
						
					}
					
					
					
					}
					
					
				}
				
				else {
					//System.out.println("Primary key not found in new file");
				}
				
				
				
				
				
					
					
				
				return false;
				
				
				
				
				
			}

			private int checkIfKeyPresentInNewFile(int columnKey, String valueToCompare) {
				// TODO Auto-generated method stub
				HashMap<Integer, HashMap> newFileMap=newFileExcel.getCompleteFileMap();
				HashMap<Integer,String> rowMap;
				
				for(Integer newFileKey1:newFileMap.keySet()){
					rowMap=newFileMap.get(newFileKey1);
					
					String checkValue=rowMap.get(columnKey);
					if(valueToCompare.equals(checkValue)) {
						
						return newFileKey1;
						
						
					}
				
			}
				
				return -1;
				
			}

			private int findColumnNumOfKey(String value, ExcelDataManager baseFileExcel) {
				// TODO Auto-generated method stub
				
				HashMap<Integer,String> columnMap=(HashMap)baseFileExcel.getCompleteFileMap().get(0);
				
				for(Integer key:columnMap.keySet()) {
					
					if(columnMap.get(key).equals(value)) {
						return key;
					}
					
					
				}
				
				return 0;
				
				
				
			}
			
		});
		btnCompare.setBounds(202, 186, 89, 23);
		frame.getContentPane().add(btnCompare);
		
		JButton btnBrowse = new JButton("Browse");
		btnBrowse.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				
				JFileChooser fileChoose=new JFileChooser();
				int retVal=fileChoose.showOpenDialog(fileChoose);
				
				if (retVal == JFileChooser.APPROVE_OPTION) {
                    //... The user selected a file, get it, use it.
                    File file = fileChoose.getSelectedFile();
                    textField.setText(file.getPath());
                    try {
                    	
						HashMap<Integer,HashMap> baseFileMap=ExcelUtils.readCSVFile(file.getPath());
						baseFileExcel=new ExcelDataManager();
						baseFileExcel.setCompleteFilePath(file.getPath());
						baseFileExcel.setCompleteFileMap(baseFileMap);
						setKeyComboBoxValues(baseFileMap,comboBox);
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                }
				
				
			}
		});
		btnBrowse.setBounds(399, 50, 89, 23);
		frame.getContentPane().add(btnBrowse);
		
		JButton button = new JButton("Browse");
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				JFileChooser fileChoose=new JFileChooser();
				int retVal=fileChoose.showOpenDialog(fileChoose);
				
				if (retVal == JFileChooser.APPROVE_OPTION) {
                    //... The user selected a file, get it, use it.
                    File file = fileChoose.getSelectedFile();
                    textField_1.setText(file.getPath());
                    
                    try {
						HashMap<Integer,HashMap> newFileMap=ExcelUtils.readCSVFile(file.getPath());
						newFileExcel=new ExcelDataManager();
						newFileExcel.setCompleteFilePath(file.getPath());
						newFileExcel.setCompleteFileMap(newFileMap);
						
						
						
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
                    
                }
				
				
				
				
			}
		});
		button.setBounds(399, 89, 89, 23);
		frame.getContentPane().add(button);
		
		progressBar = new JProgressBar();
		progressBar.setStringPainted(true);
		progressBar.setBounds(46, 230, 442, 14);
		frame.getContentPane().add(progressBar);
		progressBar.setVisible(false);
	}
	
	@SuppressWarnings("unchecked")
	private static void setKeyComboBoxValues(HashMap<Integer, HashMap> fileMap, JComboBox comboBox) {
		
		@SuppressWarnings("rawtypes")
		DefaultComboBoxModel model=(DefaultComboBoxModel)comboBox.getModel();
		model.removeAllElements();
		HashMap<Integer,String> columnMap=(HashMap)fileMap.get(0);
		for(Integer key:columnMap.keySet()) {
			
			model.addElement(columnMap.get(key));
			
		}
		comboBox.setModel(model);
		
		
		
		
	}
}
