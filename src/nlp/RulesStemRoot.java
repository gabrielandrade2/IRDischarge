package nlp;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import activerecord.Regra;
import activerecord.Termo;
import br.usp.pcs.lta.cogroo.entity.Token;

public class RulesStemRoot {
	
	
	/**
	 * Obtém quantidade de palavras de uma regra
	 * @param arr
	 * @return
	 */
	//public static int getRuleSize(String[] arr){
	public static int getRuleSize(Regra rule){
		return rule.termos.size();
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
	 * @param words
	 * @return
	 */
	public static boolean ruleApplies(String[] tags, Regra rule, List<String> words){
		
		for(int i = 0; i < getRuleSize(rule); i++){

			Termo t = rule.termos.get(i);
			String termo = t.termo;
			
			System.out.print(tags[i] + " = " + termo + "_");
			System.out.println();
			
			//Se for regra de STEMMING
			if(isStemRule(t)){
				//System.out.println(TaggerStemRoot.getStem(words.get(i)));
				//System.out.println(termo.replace("*", ""));
				//System.out.println("-----");
				if(!TaggerStemRoot.getStem(words.get(i)).equalsIgnoreCase(termo.replace("*", "")))
					return false;
			}
			else{
				//TODO: Retirar underlines na obtenção do POS-tagging
				if(!tags[i].equalsIgnoreCase(termo + "_"))
					return false;
			}
		
		}
		
		return true;
		
	}
	
	/**
	 * Verifica é regra de STEMMING
	 * @param rule
	 * @return
	 */
	public static boolean isStemRule(Termo t){
		
		//if(rule.startsWith("*"))
		//	return true;
		if(t.tipotermo_id == 2)
			return true;
		
		return false;
		
		
	}
	
	/**
	 * Verifica é regra de POS-TAG
	 * @param rule
	 * @return
	 */
	public static boolean isPOSRule(Termo t){
		
		//if(!rule.startsWith("*"))
		//	return true;
		if(t.tipotermo_id == 1)
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
	
	public static List<Regra> getRules(boolean fromDb){
		
		//if(fromDb)
			return getRulesDB();
		//else
		//	return getRulesExcel();
		
		
	}
	
	public static List<Regra> getRulesDB(){
		
		return new Regra().findByElement(8, 1, true);
		
	}
	
	
	//TODO: Ajustar função para popular lista de regras - List<Regra>
	public static List<ArrayList<String>> getRulesExcel(){
		
		List<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
		
		/*
		StringTokenizer tokens = null;
		File inputWorkbook = new File("files\\Regras_encaminhamento_02_10_12.xls");
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
						
						//Se é regra de STEMMING não coloca UNDERLINE
						if(isStemRule(aux))
							listSingle.add(aux.replace("[", "").replace("]", ""));
						else
							listSingle.add(aux.replace("[", "").replace("]", "") + "_");
					}
					
					list.add(i, listSingle);
				}
				
			}
		}
		catch(Exception e){
			
		}
		*/
		return list;
		
		
	}
	
	

}
