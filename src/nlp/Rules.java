package nlp;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import br.usp.pcs.lta.cogroo.entity.Token;

public class Rules {
	
	
	/**
	 * Obtém quantidade de palavras de uma regra
	 * @param arr
	 * @return
	 */
	//public static int getRuleSize(String[] arr){
	public static int getRuleSize(List<String> list){
		return list.size();
	}
	
	/**
	 * Obtém conjunto de palavras de acordo com a quantidade e posição definidas
	 * @param pos
	 * @param qtde
	 * @param tokens
	 * @return
	 */
	//public static String[] getWords(int pos, int ruleSize, String[] tokens){
	//public static String[] getWords(int pos, int ruleSize, List<Token> tokens){
	public static List<String> getWords(int pos, int ruleSize, List<Token> tokens){
		
		//String[] words = new String[ruleSize]; 
		List<String> words = new ArrayList<String>(); 
		
		for(int i = 0; i < ruleSize; i++){
			
			try{
				//if(tokens[pos + i] == null)
				if(tokens.get(pos + i) == null)
					return null;
			}
			catch(Exception e){
				return null;
			}
			
			//words[i] = tokens[pos + i];
			//words[i] = tokens.get(pos + i).getLexeme();
			words.add(i, tokens.get(pos + i).getLexeme());
		
		}
		
		return words;
		
	}
	
	/**
	 * Obtém conjunto de regras de acordo com a quantidade e posição definidas
	 * @param pos
	 * @param qtde
	 * @param tokens
	 * @return
	 */
	//public static String[] getTags(int pos, int ruleSize, String[] tokens){
	public static String[] getTags(int pos, int ruleSize, List<Token> tokens){
		
		String[] words = new String[ruleSize]; 
		
		for(int i = 0; i < ruleSize; i++){
			
			try{
				//if(tokens[pos + i] == null)
				if(tokens.get(pos + i) == null)
					return null;
			}
			catch(Exception e){
				return null;
			}
			
			words[i] = tokens.get(pos + i).getMorphologicalTag() + "";
		
		}
		
		return words;
		
	}
	
	/**
	 * Verifica se conjunto de tags encontradas se aplica a regra
	 * @param tags
	 * @param rules
	 * @return
	 */
	public static boolean ruleApplies(String[] tags, List<String> rule){
		
		for(int i = 0; i < rule.size(); i++){
			
			if(!tags[i].equalsIgnoreCase(rule.get(i)))
				return false;
		
		}
		
		return true;
		
	}
	
	/**
	 * Verifica é regra de STEMMING
	 * @param rule
	 * @return
	 */
	public static boolean isStemRule(String rule){
		
		if(rule.startsWith("*"))
			return true;
		
		return false;
		
		
	}
	
	/**
	 * Verifica é regra de POS-TAG
	 * @param rule
	 * @return
	 */
	public static boolean isPOSRule(String rule){
		
		if(!rule.startsWith("*"))
			return true;
		
		return false;
		
		
	}
	
	/**
	 * Obtém versão para impressão dos conteúdos de um array
	 * @param arr
	 * @return
	 */
	//public static String getPrintableWords(String[] arr){
	public static String getPrintableWords(List<String> list){
		
		String res = "";
		
		try{
			//for(int i = 0; i < arr.length; i++){
			for(String str: list){
				
				res += str + " ";
				
			}
			//}
			
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		
		return res;
	}
	
	public static List<ArrayList<String>> getRules(){
		
		List<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
		
		StringTokenizer tokens = null;
		File inputWorkbook = new File("files\\Regras_encaminhamento_12_09_12.xls");
		Workbook w;
		String aux = "";
		try {
			
			w = Workbook.getWorkbook(inputWorkbook);
			
			Sheet sheet = w.getSheet(0);

			int size = sheet.getRows();

			for (int i = 0; i < size; i++) {

				Cell celln = sheet.getCell(1, i);

				if(!celln.getContents().isEmpty()){
					
					tokens = new StringTokenizer(celln.getContents(), "][");
		
					ArrayList<String> listSingle = new ArrayList<String>();
					
					while (tokens.hasMoreTokens()) {
						aux = tokens.nextToken();
						
						listSingle.add(aux.replace("[", "").replace("]", "") + "_");
					}
					
					list.add(i, listSingle);
				}
				
			}
		}
		catch(Exception e){
			
		}
		
		return list;
		
	}
	
	

}
