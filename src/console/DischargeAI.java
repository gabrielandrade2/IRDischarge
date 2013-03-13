package console;


import java.io.File;
import java.io.IOException;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import nlp.Tagger;

/**
 * MANTER CHARSET DESSA CLASSE COMO ISO-8859
 * @author Lucas Oliveira
 *
 */
public class DischargeAI {
	
	private static WritableWorkbook workbook;
	private static WritableSheet sheetW;
	private static Tagger tagger;

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
		tagger = new Tagger();
		
		File inputWorkbook = new File("files\\ia.xls");
		Workbook w;
		try {
			
			w = Workbook.getWorkbook(inputWorkbook);
			
			Sheet sheet = w.getSheet(0);
			String texto = "";

			int size = sheet.getRows();
	
			createXLS();
			
			for (int i = 0; i < size; i++) {

				Cell celln = sheet.getCell(0, i);

				if(!celln.getContents().isEmpty()){
				
					texto = tagger.separateWords(celln.getContents());
					
					addCell(0, i, texto);
					
					System.out.println(texto);
					System.out.println("0/" + i);
				
				}
				//else{
				//	break;
				//}

				
			}
			
			closeXLS();

		} catch (BiffException e) {
			e.printStackTrace();
		}

	}
	
	public static void createXLS(){
		
		try {
			workbook = Workbook.createWorkbook(new File("files\\output_ia.xls"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		sheetW = workbook.createSheet("Resultados", 0);
		
	}
	
	public static void addCell(int coluna, int linha, String text){
	
		Label label = new Label(coluna, linha, text); 
		try {
			sheetW.addCell(label);
		} catch (RowsExceededException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	
	}
	
	public static void closeXLS(){
		
		try {
			workbook.write();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		try {
			workbook.close();
		} catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	

}
