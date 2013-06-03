package console;


import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import br.gpri.view.Interface;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import nlp.RulesStemSub;
import nlp.TaggerStemSub;
import activerecord.Regra;

/**
 * MANTER CHARSET DESSA CLASSE COMO ISO-8859
 * @author Lucas Oliveira
 *
 */
public class RulesXLS {
	
	private static WritableWorkbook workbook;
	private static WritableSheet sheetW;
	//private static Tagger tagger;

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
		Scanner s = new Scanner(System.in);
		
		//Selecionar qual programa utilizar
		System.out.println("1 - Interface Gráfica");
		System.out.println("2 - IRDischarge");
		System.out.print("Digite a opção selecionada: ");
		
		if (s.nextInt() == 1)
			Interface.ExecutaInterface();
		
		else{
		//Tagger antes do Stemming
		//Tagger tagger = new Tagger();
		
		//Tagger com Stemming como regra-base
		//TaggerStemRoot tagger = new TaggerStemRoot();
		
		//Tagger com Stemming como sub-regra
		TaggerStemSub tagger = new TaggerStemSub();
		
		//List<Regra> rules = RulesStemRoot.getRules(true);
		//List<Regra> rules = RulesStemSub.getRules(true);
		List<Regra> rules = TaggerStemSub.getRules(true);
		
		File inputWorkbook = new File("files/Sumarios_encaminhamento_v2.xls");
		Workbook w;
		try {
			
			w = Workbook.getWorkbook(inputWorkbook);
			
			Sheet sheet = w.getSheet(0);

			int size = sheet.getRows();
	
			createXLS();
			
			//Itera sumários de alta presentes no EXCEL
			for (int i = 0; i < size; i++) {

				Cell celln = sheet.getCell(0, i);

				if(!celln.getContents().isEmpty()){
					
					String resultado = tagger.testTagRules(celln.getContents(), rules, false);
					
					addCell(0, i, celln.getContents());
					addCell(1, i, resultado);
					
					System.out.println("Sumario num: " + i);
					System.out.println(celln.getContents());
					System.out.println(resultado);
					System.out.println("-----------------------------------");
				
				}
				else{
					break;
				}

				
			}

		} catch (BiffException e) {
			e.printStackTrace();
		}
		
		closeXLS();

	}
	}
	public static void createXLS(){
		
		try {
			workbook = Workbook.createWorkbook(new File("files\\output.xls"));
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
