package nlp;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import org.tartarus.snowball.SnowballStemmer;
import org.tartarus.snowball.ext.portugueseStemmer;

import util.ArrayHandle;
import util.ReadWriteTextFile;
import activerecord.Regra;
import activerecord.Termo;
import br.gpri.view.Interface;
import br.usp.pcs.lta.cogroo.entity.Token;
import br.usp.pcs.lta.cogroo.entity.impl.runtime.SentenceCogroo;
import br.usp.pcs.lta.cogroo.util.viewer.CogrooWrapper;

/**
 * Regras com Stemming sendo tratadas como regras comuns
 * @author Lucas Oliveira
 *
 */
public class TaggerStemSub {
	
	public CogrooWrapper cogroo;
	
	public TaggerStemSub(){
		
		cogroo = new CogrooWrapper();
		
	}
	
	public String preProccessText(String text){

		//Expande as datas	
		text = expandirData(text);
		//Retira erros recorrentes nos sumários
		text = retiraErrosRecorrentes(text);
		//Retira pontuação e caracteres especiais
		text = retiraPontuacao(text);
		//Espaça ponto
		text = espacaPontuacao(text);
		//Expande Acrônimos
		text = expandirAcronimos(text);
		//Coloca em minúsculas
		text = text.toLowerCase();
		//Retira Stopwords
		text = retiraStopWords(text);
		
		return text;
		
	}
	

	//Tagger para Interface gráfica
	public String TaggerInterface(String text_sumario, String text_selecionado, boolean isWeb){
		
		//Executa operações de PRÉ-PROCESSAMENTO
		text_sumario = preProccessText(text_sumario);
		text_selecionado = preProccessText(text_selecionado);
		
		//Separa texto selecionado em palavras
		SentenceCogroo selecionado = new SentenceCogroo(text_selecionado);
		cogroo.tokenizer(selecionado);
		cogroo.nameFinder(selecionado);
		cogroo.preTagger(selecionado);
		List<Token> text_separado = selecionado.getTokens();
						
		String res = "";
		String res1 = "";

		//Separa texto em sentenças
		String[] sentencas = cogroo.sentDetect(text_sumario);
		for (String sentenca : sentencas) {
			
			//Tokeniza sentença
			SentenceCogroo sc = new SentenceCogroo(sentenca);
			List<Token> tokens = null;
			cogroo.tokenizer(sc);

			//Aplica o NAMEFINDER
			cogroo.nameFinder(sc);
			
			//Expansão de preposições
			cogroo.preTagger(sc);
			
			//Realiza POS_tagging
			cogroo.tagger(sc);
			tokens = sc.getTokens();
			
			//Procura onde estão os termos selecionados
			//Compara um termo com o primeiro do vetor separado, caso encontre, ve se os termos
			//seguintes também são os esperados
			boolean igual = false;
			String tags = null;
			String palavras = null;
			
			for(int i=0; i < tokens.size(); i++){
				for(int j=0; j < text_separado.size(); j++){
					if(text_separado.get(j).getLexeme().equals(tokens.get(i+j).getLexeme()))
						igual = true;
					else{
						igual = false;
						break;}}
				
				//Cria a string a ser retornada,
				//a partir do indice do primeiro termo esperado encontrado. 
				if(igual){
					for(int k=0; k < text_separado.size(); k++){
						Token token = tokens.get(k+i);	
						res += token.getLexeme() + "_" + token.getMorphologicalTag() + " ";
						
						//Inserts
						//Termos Regras
						String morphologicaltag = tokens.get(k+i).getMorphologicalTag().toString();
						morphologicaltag = morphologicaltag.substring(0, morphologicaltag.length()-1);
						Interface.termosregras[k] = ("insert into termosregras(id, regra_id, tipotermo_id, ordem, termo) select max(termosregras.id) +1, max(regras.id), 1," + (k+1) + ", '" + morphologicaltag + "' from regras, termosregras");
						
						//Regras
						tags += ("[" + morphologicaltag + "] ");
						palavras += (tokens.get(k+i).getLexeme() + " ");
					}
					break;}
				}
			//Regras
			Interface.regras = ("insert into regras(id, conjunto_id, elemento_id, ordem, previa, observacao) select max(id)+1 ,1,8,1,' " + tags + "','" + palavras + "' from regras");
		}
			
		System.out.println(res);
		return (isWeb) ? res.replace("\n", "<br>") : res; 
				
	}

	
	/**
	 * Executa etiquetação de texto recebido com o Cogroo
	 * @param text
	 * @return
	 */
	public String tagTextCogroo(String text, boolean isWeb){
		
		//Executa operações de PRÉ-PROCESSAMENTO
		text = preProccessText(text);
		
		String res = "";
		String res1 = "";

		//Separa texto em sentenças
		String[] sentencas = cogroo.sentDetect(text);
		for (String sentenca : sentencas) {
			
			//Tokeniza sentença
			SentenceCogroo sc = new SentenceCogroo(sentenca);
			List<Token> tokens = null;
			cogroo.tokenizer(sc);

			//Aplica o NAMEFINDER
			cogroo.nameFinder(sc);
			
			//Expansão de preposições
			cogroo.preTagger(sc);
			
			//Realiza POS_tagging
			cogroo.tagger(sc);
			tokens = sc.getTokens();

			for (Token token : tokens){
				if(!isWeb)
				{
					res += token.getLexeme() + "_" + token.getMorphologicalTag() + " ";
    				res1 += token.getLexeme() + "_" + token.getMorphologicalTag() + " ";
				}
				else
				{
					res += token.getLexeme() + "_<b>" + token.getMorphologicalTag() + "</b> ";
    				res1 += token.getLexeme() + "_<b>" + token.getMorphologicalTag() + "</b> ";
				}
			}
				
		}
		System.out.println(res);
		System.out.println(res1);
		return (isWeb) ? res.replace("\n", "<br>") : res; 
				
	}
	
	
	
	
	public String testTagRules(String text, List<Regra> rules, boolean isWeb){
		
		//Executa operações de PRÉ-PROCESSAMENTO
		text = preProccessText(text);
		
		System.out.println(text);
		
		//Quantidade de palavras por regra
		int qtdePalavrasRegra = 0;
		
		//Armazena saída do resultado
		String res = "";
		
		//Lista de TOKENS
		List<Token> sent = new ArrayList<Token>();
		
		//Separa as sentenças e efetua pos-tagging
		String[] sentencas = cogroo.sentDetect(text);
		
		//INVERTE ORDEM DAS SENTENÇAS - Melhor aproveitamento para CONTINUIDADE/ENCAMINHAMENTO
		ArrayHandle.reverse(sentencas);

		//Percorre as sentencas
		for (String sentenca : sentencas) {
			
			SentenceCogroo sc = new SentenceCogroo(sentenca);
			
			cogroo.tokenizer(sc);
			
			cogroo.nameFinder(sc);
			
			cogroo.preTagger(sc);
			
			cogroo.tagger(sc);
			
			sent.addAll(sc.getTokens());
/*			System.out.println("parada para mostrar a frase taggeada");
			 System.out.println(cogroo.toString());
			Scanner scan = new Scanner(System.in);
		    scan.next();
*/		    
			
		}
		
		//Percorre regras
		String wordsEtags = "";
		for (Regra rule : rules) {
			
			//Regras a serem percorridas
    		List<Regra> rulesToMatch = new ArrayList<Regra>();
    		
    		/*
    		 * VERIFICAR SE REGRA TEM SUBREGRAS, SE SIM, PERCORRÊ-LAS ANTES DE BUSCAR REGRA ATUAL
    		 * - Se não encontrar nenhuma das sub-regras, buscar regra-base
    		 * 
    		 * Quanto mais específica(MAIOR QTDE DE TERMOS) uma regra tiver, MENOR A CHANCE DE OBTENÇÃO DE FALSO POSITIVO.
    		 * Portanto, se as regras mais específicas forem analisadas anteriormente, o algoritmo estaria privilegiando regras
    		 * com maior chance de VERDADEIRO POSITIVO
    		 * 
    		 * OBS: Será que apenas uma inversão de ordem das regras no algoritmo SEM SUB-REGRAS já não resolveria?
    		 */
    		
    		//Se existem SUB-REGRAS na regra atual, adiciona-as na Lista
    		if(rule.subregras.size() > 0){
    			
    			//Percorre sub-regras
    			for(int i = 0; i < rule.subregras.size(); i++){
    				//Adiciona na lista de regras a serem verificadas
    				rulesToMatch.add(rule.subregras.get(i));
    			}
    			
    		}
    		
    		//Adiciona regra atual
    		rulesToMatch.add(rule);
    		for(int l = 0; l < rulesToMatch.size(); l++){
    		
    			Regra r = rulesToMatch.get(l);
    			
				//Obtém quantidade de palavras da regra
		    	qtdePalavrasRegra = this.getRuleSize(r);
		        
		    	//System.out.println("Regra Atual: " + Rules.getPrintableWords(r) + "\n\n");
		        //System.out.println("Qtde palavras: " + sent.size());
		        //System.out.println("Qtde palavras regras: " + qtdePalavrasRegra);
		    	
		        //Percorre palavras
		        for(int i = 0; i < sent.size(); i++){
		        
		        	//Obtém as palavras correspondentes a regra definida
		        	List<String> words = this.getWords(i, qtdePalavrasRegra, sent);
		        	wordsEtags = wordsEtags + "[" + i + "]" + "[" + sent.get(i).toString() + "]";
		        	wordsEtags = wordsEtags + "[" + sent.get(i).getMorphologicalTag() + "]";
//					System.out.println("words[i] " + i + " " + sent.get(i).getMorphologicalTag() + "");

		        	try{
		        		
			        	//Se não faltaram palavras para completar a regra
		        		if(words.size() == qtdePalavrasRegra){
			        	
				        	//Obtém as TAGS das palavras correspondentes a regra definida
			        		String tags[] = this.getTags(i, qtdePalavrasRegra, sent);
				        	//Se foi encontrada ocorrência da regra no trecho atual
//			        		System.out.println("tags 1 " + tags[1]);
			        		if(this.ruleApplies(tags, r, words)){
				        		
			        			res += "TRECHO ENCONTRADO: " + i + Rules.getPrintableWords(words) + "\r\nRegra: " + r.previa + "\n";
				        		//return "TRECHO ENCONTRADO: " + Rules.getPrintableWords(words) + "\nRegra: " + rule.toString() + "\n";
				        		
				        	}
			        	
			        	}
		        	}
		        	catch(Exception e){
		        		
		        	}
		        	
				}
		        
    		}
	        
	      
		}
        System.out.println(wordsEtags);
		System.out.println("parada encontrado");
		System.out.println("indice: ");
	    Scanner scan1 = new Scanner(System.in);
	    String inputString = scan1.nextLine();
	    int indice = Integer.parseInt(inputString);
		System.out.println("quantidade Termos: ");
		scan1 = new Scanner(System.in);
		inputString = scan1.nextLine();
	    int qtdTermos = Integer.parseInt(inputString);
	    
	    System.out.println("valor de indice = " + indice);
	    System.out.println("valor de qtd termos= " + qtdTermos);

	    String queryRegras = "insert into regras(id, conjunto_id, elemento_id, ordem, previa, observacao) select max(id)+1 ,1,8,1,";
	    String queryTermosRegras = "insert into termosregras(id, regra_id, tipotermo_id, ordem, termo) select max(termosregras.id) +1, max(regras.id), 1, ";
        String previa = "";
        String observacao = "";
        String termo = "";
	    for(int ii = indice; ii < indice+qtdTermos; ii++){
        	previa = previa + "[" + sent.get(ii).getMorphologicalTag() + "]";
        	observacao = observacao + sent.get(ii).toString() + " ";
        	termo = termo + sent.get(ii).getMorphologicalTag();
        	termo = termo.substring(0, termo.length()-1);
        	System.out.println(queryTermosRegras + (ii - indice + 1) + "," + "'" + termo + "'"+ " from regras , termosregras");
        	termo = "";
        	}
        System.out.println(queryRegras + "'" + previa +"'" + "," + "'" + observacao + "'" + " from regras");
        observacao = "";
	    
		if(res.equals(""))
		{
			System.out.println("parada não encontrado");
		    Scanner scan = new Scanner(System.in);
		    scan.next();
			
		    return "Não encontrado!";
		}
		else
			return res;
	        
      /*  if(isWeb){
        	res = res.replace("#", "<hr>");
        	res = res.replace("\n", "<br>");
        }
        
        
		return res;*/
	        
				
	}
	
	/**
	 * Obtém o STEM da palavra
	 * @param word
	 * @return
	 */
	public static String getStem(String word){
		
        SnowballStemmer stemmer = new portugueseStemmer();
    	stemmer.setCurrent(word);
    	stemmer.stem();
    	
    	return stemmer.getCurrent();
		
	}
	
	public void writeFile(String path, String name, String content){
		
		try{
			  // Create file 
			  //FileWriter fstream = new FileWriter(pathData + "sumarios/" + name + ".txt");
			  //BufferedWriter out = new BufferedWriter(fstream);
			  File file = new File(path +  name + ".txt");
			  BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),"UTF8"));
			  out.write(content);
			  out.close();
			  
		  }catch (Exception e){//Catch exception if any
			  System.err.println("Error: " + e.getMessage());
		  }
		
	}
	
	public String getRulesFile(String type){
		return ReadWriteTextFile.getContents(new File("rules/rules_" + type + ".txt"));
		
	}
	
	public void setRulesFile(String type, String text){
		
		try {
			ReadWriteTextFile.setContents(new File("rules/rules_" + type + ".txt"), text);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public String retiraErrosRecorrentes(String text){
		
		text = text.replaceFirst("#", "Paciente");

		return text;

		
	}
	
	public String retiraPontuacao(String text){
		
		return text.replaceAll("[-!?><=%;/#,@*]", " ");
		
	}
	
	public String espacaPontuacao(String text){
		
		return text.replace(".", " .");
	}
	
	public String expandirData(String text){
		
		
		 if (Pattern.compile("[\\d]+[\\d]+[/]+[\\d]+[\\d]+[/]+[\\d]+[\\d]").matcher(text).find()
			    	|| Pattern.compile("[\\d]+[\\d]+[/]+[\\d]+[\\d]+[/]+[\\d]+[\\d]+[\\d]+[\\d]").matcher(text).find()
			    	|| Pattern.compile("[\\d]+[\\d]+[/]+[a-zA-Z]+[a-zA-Z]+[a-zA-Z][/]+[\\d]+[\\d]+[\\d]+[\\d]").matcher(text).find()
			    	|| Pattern.compile("[\\d]+[\\d]+[/]+[a-zA-Z]+[a-zA-Z]+[a-zA-Z][/]+[\\d]+[\\d]").matcher(text).find()){
			
			text = text.replace("01/","1/");
			text = text.replace("02/","2/");
			text = text.replace("03/","3/");
			text = text.replace("04/","4/");
			text = text.replace("05/","5/");
			text = text.replace("06/","6/");
			text = text.replace("07/","7/");
			text = text.replace("08/","8/");
			text = text.replace("09/","9/");
			 
			text = text.replace("/jan/"," de janeiro de ");
	    	text = text.replace("/fev/"," de fevereiro de ");
	    	text = text.replace("/mar/"," de março de ");
	    	text = text.replace("/abr/"," de abril de ");
	    	text = text.replace("/mai/"," de maio de ");
	    	text = text.replace("/jun/"," de junho de ");
	    	text = text.replace("/jul/"," de julho de ");
	    	text = text.replace("/ago/"," de agosto de ");
	    	text = text.replace("/set/"," de setembro de ");
	    	text = text.replace("/out/"," de outubro de ");
	    	text = text.replace("/nov/"," de novembro de ");
	    	text = text.replace("/dez/"," de dezembro de ");
			  
	    	text = text.replace("/01/"," de janeiro de ");
	    	text = text.replace("/02/"," de fevereiro de ");
	    	text = text.replace("/03/"," de março de ");
	    	text = text.replace("/04/"," de abril de ");
	    	text = text.replace("/05/"," de maio de ");
	    	text = text.replace("/06/"," de junho de ");
	    	text = text.replace("/07/"," de julho de ");
	    	text = text.replace("/08/"," de agosto de ");
	    	text = text.replace("/09/"," de setembro de ");
	    	text = text.replace("/10/"," de outubro de ");
	    	text = text.replace("/11/"," de novembro de ");
	    	text = text.replace("/12/"," de dezembro de ");
	    	
	    }
		
	    if (Pattern.compile("[\\d]+[\\d]+[/]+[\\d]+[\\d]").matcher(text).find()
	    	|| Pattern.compile("[\\d]+[\\d]+[/]+[a-zA-Z]+[a-zA-Z]+[a-zA-Z]").matcher(text).find()){
	    	
	    	text = text.replace("01/","1/");
			text = text.replace("02/","2/");
			text = text.replace("03/","3/");
			text = text.replace("04/","4/");
			text = text.replace("05/","5/");
			text = text.replace("06/","6/");
			text = text.replace("07/","7/");
			text = text.replace("08/","8/");
			text = text.replace("09/","9/");
	    	
	    	text = text.replace("/jan"," de janeiro");
	    	text = text.replace("/fev"," de fevereiro");
	    	text = text.replace("/mar"," de março");
	    	text = text.replace("/abr"," de abril");
	    	text = text.replace("/mai"," de maio");
	    	text = text.replace("/jun"," de junho");
	    	text = text.replace("/jul"," de julho");
	    	text = text.replace("/ago"," de agosto");
	    	text = text.replace("/set"," de setembro");
	    	text = text.replace("/out"," de outubro");
	    	text = text.replace("/nov"," de novembro");
	    	text = text.replace("/dez"," de dezembro");
	    	
	        text = text.replace("/01"," de janeiro");
	    	text = text.replace("/02"," de fevereiro");
	    	text = text.replace("/03"," de março");
	    	text = text.replace("/04"," de abril");
	    	text = text.replace("/05"," de maio");
	    	text = text.replace("/06"," de junho");
	    	text = text.replace("/07"," de julho");
	    	text = text.replace("/08"," de agosto");
	    	text = text.replace("/09"," de setembro");
	    	text = text.replace("/10"," de outubro");
	    	text = text.replace("/11"," de novembro");
	    	text = text.replace("/12"," de dezembro");}
	    
	       
	    return text;
	}
	
	
	public String retiraStopWords(String text){
		
		text = text.replace(" o ", " ");
		text = text.replace(" a ", " ");
		text = text.replace(" os ", " ");
		text = text.replace(" as ", " ");
		text = text.replace(" um ", " ");
		text = text.replace(" uma ", " ");
		text = text.replace(" uns ", " ");
		text = text.replace(" umas ", " ");
		
		
		return text;
		
	}
	
	public String expandirAcronimos(String text){
		
		
		text = text.replace(" AAS "," ácido acetil salicílico ");
		text = text.replace(" ACTP "," angioplastia coronária transluminal percutânea ");
		text = text.replace(" HCPA "," Hospital de Clínicas de Porto Alegre ");
		text = text.replace(" HAS "," hipertensão arterial sistêmica ");
		text = text.replace(" VO "," via oral ");
		text = text.replace(" ICC "," insuficiência cardíaca congestiva ");
		text = text.replace(" FE "," fração de ejeção ");
		text = text.replace(" ECG "," eletrocardiograma ");
		text = text.replace(" VE "," ventrículo esquerdo ");
		text = text.replace(" IAM "," infarto agudo do miocárdio ");
		text = text.replace(" CD "," coronária direita ");
		text = text.replace(" DAE "," dimensão do átrio esquerdo ");
		text = text.replace(" DA "," artéria descendente anterior ");
		text = text.replace(" TC "," tomografia computadorizada ");
		text = text.replace(" CAT "," avaliação crítica tópica ");
		text = text.replace(" mg/d "," miligrama/decilitro ");
		text = text.replace(" CTI "," Centro de Terapia Intensiva ");
		text = text.replace(" DM "," diabetes melitus ");
		text = text.replace(" CRM "," cardio-ressonância magnética ");
		text = text.replace(" AE "," átrio esquerdo ");
		text = text.replace(" RN "," recém-nato ");
		text = text.replace(" STENT "," stent ");
		text = text.replace(" IG "," imunoglobulina ");
		text = text.replace(" IRC "," insuficiência renal crônica ");
		text = text.replace(" TIMI "," medida do fluxo coronário e microvascular ");
		text = text.replace(" APGAR "," Apgar ");
		text = text.replace(" NR "," não-reagente ");
		text = text.replace(" AVC "," acidente vascular cerebral ");
		text = text.replace(" ACD "," artéria coronária direita ");
		text = text.replace(" CX "," artéria circunflexa ");
		text = text.replace(" RX "," raio X ");
		text = text.replace(" DPOC "," doença pulmonar obstrutiva crônica ");
		text = text.replace(" PSAP "," pressão sistólica da artéria pulmonar ");
		text = text.replace(" PCR "," parada cárdio-respiratória ");
		text = text.replace(" ECO "," ecocardiograma ");
		text = text.replace(" VD "," ventrículo direito ");
		text = text.replace(" IC "," insuficiência cardíaca ");
		text = text.replace(" EV "," endovenosa ");
		text = text.replace(" O2 "," oxigênio ");
		text = text.replace(" FC "," função cardíaca ");
		text = text.replace(" NPH "," protamina neutra Hagedorn ");
		text = text.replace(" HCTZ "," hidroclortiazida ");
		text = text.replace(" FO "," ferida operatória ");
		text = text.replace(" INR "," razão normalizada internacional ");
		text = text.replace(" BID "," duas vezes ao dia ");
		text = text.replace(" PA "," pressão arterial ");
		text = text.replace(" SCA "," síndrome coronariana aguda ");
		text = text.replace(" VDRL "," laboratório de pesquisa de doenças venéreas ");
		text = text.replace(" ADA "," artéria descendente anterior ");
		text = text.replace(" ACFA "," fibrilação atrial crônica ");
		text = text.replace(" SL "," sublingual ");
		text = text.replace(" CI "," cardiopatia isquêmica ");
		text = text.replace(" MP "," marca-passo ");
		text = text.replace(" ITU "," infecção do trato urinário ");
		text = text.replace(" BCP "," broncopneumonia ");
		text = text.replace(" DM2 "," diabetes melitus tipo 2 ");
		text = text.replace(" UI "," unidades internacionais ");
		text = text.replace(" ATB "," antibiótico ");
		text = text.replace(" TS "," tipo sangüíneo ");
		text = text.replace(" TID "," dilatação isquêmica transitória ");
		text = text.replace(" FA "," fibrilação atrial ");
		text = text.replace(" TCE "," tronco de coronária esquerda ");
		text = text.replace(" II "," 2 ");
		text = text.replace(" IM "," insuficiência mitral ");
		text = text.replace(" PS "," pronto socorro ");
		text = text.replace(" HIV "," vírus da imunodeficiência humana ");
		text = text.replace(" mmHg "," milímetro de mercúrio ");
		//text = text.replace(" NA "," sódio ");//Retirado 05/09/2012
		text = text.replace(" HCV "," vírus da hepatite C ");
		text = text.replace(" AP "," ausculta pulmonar ");
		text = text.replace(" IgG "," imunoglobulina G ");
		text = text.replace(" MIE "," membro inferior esquerdo ");
		text = text.replace(" PP "," parto prematuro ");
		text = text.replace(" TP "," trabalho de parto ");
		text = text.replace(" AC "," ausculta cardíaca ");
		text = text.replace(" HMG "," hemograma ");
		text = text.replace(" ACX "," artéria coronariana circunflexa ");
		text = text.replace(" EQU "," exame qualitativo de urina ");
		text = text.replace(" UTI "," Unidade de Terapia Intensiva ");
		text = text.replace(" IR "," insuficiência renal ");
		text = text.replace(" SV "," supraventricular ");
		text = text.replace(" DP "," doença periapical ");
		text = text.replace(" III "," 3 ");
		text = text.replace(" IV "," 4 ");
		text = text.replace(" MID "," membro inferior direito ");
		text = text.replace(" RNM "," ressonância nuclear magnética ");
		text = text.replace(" RN1 "," recém-nato 1 ");
		text = text.replace(" TSH "," hormônio tíreo-estimulante ");
		text = text.replace(" CDI "," cardioversor-desfibrilador implantável ");
		text = text.replace(" VM "," ventilação mecânica ");
		text = text.replace(" BRE "," bloqueio de ramo esquerdo ");
		text = text.replace(" IGG "," imunoglobulina G ");
		text = text.replace(" EDA "," endoscopia digestiva alta ");
		text = text.replace(" IT "," transição interna ");
		text = text.replace(" BAV "," bloqueio atrioventricular ");
		text = text.replace(" CHAD "," concentrado de hemácias adulto ");
		text = text.replace(" IgM "," imunoglobulina M ");
		text = text.replace(" DD "," diâmetro diastólico ");
		text = text.replace(" DS "," diâmetro sistólico ");
		text = text.replace(" HPS "," Hospital de Pronto Socorro ");
		text = text.replace(" CK "," creatinoquinase ");
		text = text.replace(" MEI "," medicina interna ");
		text = text.replace(" PC "," parada cardíaca ");
		text = text.replace(" MG "," miligrama ");
		text = text.replace(" TOXO "," toxoplamose ");
		text = text.replace(" ADAE "," átrio direito átrio esquerdo ");
		text = text.replace(" IGM "," imunoglobulina M ");
		text = text.replace(" SC "," subcutâneo ");
		text = text.replace(" CA "," câncer ");
		text = text.replace(" AV "," átrio ventricular ");
		text = text.replace(" BAAR "," bacilo álcool ácido resistente ");
		text = text.replace(" BT "," bilirrubina total ");
		text = text.replace(" EGD "," esofagogastroduodenoscopia ");
		text = text.replace(" TV "," taquicardias ventriculares ");
		text = text.replace(" QT "," quimioterapia ");
		text = text.replace(" TEP "," tromboembolismo pulmonar ");
		text = text.replace(" TVP "," trombose venosa profunda ");
		text = text.replace(" TA "," artéria transversa ");
		text = text.replace(" CO "," monóxido de carbono ");
		text = text.replace(" CP "," comprimido ");
		text = text.replace(" EHCPA "," serviço de emergências do Hospital de Clínicas de Porto Alegre ");
		text = text.replace(" EPED "," especialidades pediátricas ");
		text = text.replace(" HBsAg "," antígeno de superfície do vírus da hepatite B ");
		text = text.replace(" BD "," morte cerebral ");
		text = text.replace(" SSST "," sem supradesnível do segmento ST ");
		text = text.replace(" DDVE "," diâmetro diastólico do ventrículo esquerdo ");
		text = text.replace(" DCE "," depuração de creatinina endógena ");
		text = text.replace(" MsIs "," membros inferiores ");
		text = text.replace(" SF "," soro fisiológico ");
		text = text.replace(" VP "," vasopressina ");
		text = text.replace(" CPAP "," pressão positiva contínua em vias aéreas ");
		text = text.replace(" HD "," hemodiálise ");
		text = text.replace(" ITB "," índice tornozelo/braço ");
		text = text.replace(" IRA "," insuficiência renal aguda ");
		text = text.replace(" TTO "," tratamento ");
		text = text.replace(" DSVE "," diâmetro sistólico do ventrículo esquerdo ");
		text = text.replace(" IECA "," inibidor da enzima conversora de angiotensina ");
		text = text.replace(" VEF1 "," volume expiratório forçado 1 ");
		text = text.replace(" BAVT "," bloqueio atrioventricular total ");
		text = text.replace(" CF "," colo femoral ");
		text = text.replace(" DVO "," distúrbio ventilatório obstrutivo ");
		text = text.replace(" RTU "," ressecção transuretral ");
		text = text.replace(" EAP "," edema agudo de pulmão ");
		text = text.replace(" ICT "," índice cardiotorácico ");
		text = text.replace(" MSE "," membro superior esquerdo ");
		text = text.replace(" ACx "," artéria circunflexa A ");
		text = text.replace(" AVE "," acidente vascular encefálico ");
		text = text.replace(" HMC "," hemocultura ");
		text = text.replace(" TB "," tuberculose ");
		text = text.replace(" B2 "," ausculta cardíaca ");
		text = text.replace(" FR "," freqüência respiratória ");
		text = text.replace(" TE "," teste ergométrico ");
		text = text.replace(" ATC "," angioplastia transluminal coronária ");
		text = text.replace(" IGO "," índice do grau de obesidade ");
		text = text.replace(" NPT "," nutrição parenteral total ");
		text = text.replace(" CD4 "," grupo de diferenciação 4 ");
		text = text.replace(" CMV "," citomegalovírus ");
		text = text.replace(" EEG "," eletroencefalograma ");
		text = text.replace(" FV "," fibrilação ventricular ");
		text = text.replace(" RV "," remodelamento ventricular ");
		text = text.replace(" VI "," 6 ");
		text = text.replace(" AJ "," antes do jantar ");
		text = text.replace(" HbsAg "," antígeno de superfície da hepatite B ");
		text = text.replace(" PL "," punção lombar ");
		text = text.replace(" PSA "," antígeno específico da próstata ");
		text = text.replace(" RHZ "," rifampicina, isoniazida e pirazinamida ");
		text = text.replace(" TT "," transtorácico ");
		text = text.replace(" CK-MB "," fração MB da creatinofosfoquinase ");
		text = text.replace(" MSD "," membro superior direito ");
		text = text.replace(" T4 "," tiroxina ");
		text = text.replace(" UTIP "," Unidade de Terapia Intensiva Pediátrica ");
		text = text.replace(" AA "," após almoço ");
		text = text.replace(" ACO "," anticoagulante oral ");
		text = text.replace(" LDH "," desidrogenase láctica ");
		text = text.replace(" MM "," mieloma múltiplo ");
		text = text.replace(" NPO "," nada por via oral ");
		text = text.replace(" TGO "," transaminase glutâmica oxalacética ");
		text = text.replace(" ECGs "," eletrocardiogramas ");
		text = text.replace(" HVE "," hipertrofia ventricular esquerda ");
		text = text.replace(" METS "," equivalente metabólico ");
		text = text.replace(" PBF "," perfil biofísico fetal ");
		text = text.replace(" SNE "," sonda nasoenteral ");
		text = text.replace(" CVF "," capacidade vital forçada ");
		text = text.replace(" DDD "," DDD ");
		text = text.replace(" HGT "," hemoglicoteste ");
		text = text.replace(" ILA "," índice de líquido amniótico ");
		text = text.replace(" OK "," ok ");
		text = text.replace(" SVD "," sobrecarga ventricular direita ");
		text = text.replace(" AIT "," ataque isquêmico transitório ");
		text = text.replace(" HGTs "," hemoglicotestes ");
		text = text.replace(" MMII "," membros inferiores ");
		text = text.replace(" MgCx "," ramo marginal da artéria circunflexa ");
		text = text.replace(" PAM "," pressão arterial média ");
		text = text.replace(" PO "," pós-operatório ");
		text = text.replace(" TET "," tubo endotraqueal ");
		text = text.replace(" HAP "," hipertensão arterial pulmonar ");
		text = text.replace(" MTX "," metotrexato ");
		text = text.replace(" PMT "," prematuro ");
		text = text.replace(" PPL "," pressão pleural ");
		text = text.replace(" PUC "," Pontifícia Universidade Católica ");
		text = text.replace(" RDT "," radioterapia ");
		text = text.replace(" SIDA "," síndrome da imunodeficiência adquirida ");
		text = text.replace(" SN "," sistema nervoso ");
		text = text.replace(" VSG "," velocidade de sedimentação globular ");
		text = text.replace(" VSR "," vírus sincicial respiratório ");
		text = text.replace(" BR "," bloqueio de ramo ");
		text = text.replace(" CE "," coronária esquerda ");
		text = text.replace(" DN "," data do nascimento ");
		text = text.replace(" ECA "," enzima conversora de angotensina ");
		text = text.replace(" EEF "," escala de expressões faciais ");
		text = text.replace(" FNB "," fenobarbital ");
		text = text.replace(" NS "," não significativo ");
		text = text.replace(" NTG "," nitroglicerina ");
		text = text.replace(" PROX "," próximo ");
		text = text.replace(" SST "," supradesnível do segmento ST ");
		text = text.replace(" TGP "," transaminase glutâmico-pirúvica ");
		text = text.replace(" TIG "," imunoglobulina antitetânica humana ");
		text = text.replace(" AINE "," antiinflamatório não-esteroidal ");
		text = text.replace(" CN "," catéter nasal ");
		text = text.replace(" CPRE "," colangiopancreatografia retrógrada endoscópica ");
		text = text.replace(" HDA "," hemorragia digestiva alta ");
		text = text.replace(" LCR "," licor ");
		text = text.replace(" Mg-Cx "," ramo marginal da artéria circunflexa ");
		text = text.replace(" PBE "," peritonite bacteriana espontânea ");
		text = text.replace(" PN "," pneumonia ");
		text = text.replace(" BCF "," batimentos cardíacos fetais ");
		text = text.replace(" CV "," carga viral ");
		text = text.replace(" HB "," hemoglobina ");
		text = text.replace(" IAo "," insuficiência aórtica ");
		text = text.replace(" LSD "," lobo superior direito ");
		text = text.replace(" RxTx "," raio X de tórax ");
		text = text.replace(" SAMU "," Serviço de Atendimento Médico de Urgência ");
		text = text.replace(" x/dia "," vezes ao dia ");
		text = text.replace(" AD "," átrio direito ");
		text = text.replace(" ARV "," anti-retro viral ");
		text = text.replace(" AVCs "," acidentes vasculares cerebrais ");
		text = text.replace(" CCA "," cardiopatias congênitas em adultos ");
		text = text.replace(" CEA "," antígeno carcinoembrionário ");
		text = text.replace(" CIV "," comunicação interventricular ");
		text = text.replace(" DRGE "," doença do refluxo gastresofágico ");
		text = text.replace(" GGT "," gama GT ");
		text = text.replace(" HBSAG "," antígeno de superfície para hepatite B ");
		text = text.replace(" HF "," história familiar ");
		text = text.replace(" I-ECA "," inibidor da enzima conversora de angiotensina ");
		text = text.replace(" IAMs "," infarto agudo do miocárdio ");
		text = text.replace(" IVC "," insuficiência venosa crônica ");
		text = text.replace(" MgCX "," ramo moarginal da artéria circunflexa ");
		text = text.replace(" SNG "," sonda nasogástrica ");
		text = text.replace(" TBC "," tuberculose ");
		text = text.replace(" VO2 "," volume de oxigênio ");
		text = text.replace(" B12 "," B12 ");
		text = text.replace(" BRD "," bloqueio de ramo direito ");
		text = text.replace(" EA "," emergência ambulatorial ");
		text = text.replace(" FAN "," fator anti-nuclear ");
		text = text.replace(" FAV "," favorável ");
		text = text.replace(" HNSC "," Hospital Nossa Senhora da Conceição ");
		text = text.replace(" MAP "," pressão média de vias aéreas ");
		text = text.replace(" NEG "," negativo ");
		text = text.replace(" OMA "," otite média aguda ");
		text = text.replace(" QMT "," quimioterapia ");
		text = text.replace(" S-PP "," sistólico - parede posterior ");
		text = text.replace(" TX "," tórax ");
		text = text.replace(" VCI "," veia cava inferior ");
		text = text.replace(" VCM "," volume corpuscular médio ");
		text = text.replace(" C3 "," complemento 3 ");
		text = text.replace(" CIG "," cigarro ");
		text = text.replace(" CPER "," colangiopancreatografia endoscópica retrógrada ");
		text = text.replace(" DIPI "," dipiridamol ");
		text = text.replace(" EX "," ex ");
		text = text.replace(" HPB "," hiperplasia prostática benigna ");
		text = text.replace(" IVUS "," ultra-sonografia intravascular ");
		text = text.replace(" NBZ "," nebulização ");
		text = text.replace(" PIG "," pequeno para idade gestacional ");
		text = text.replace(" RM "," ressonância magnética ");
		text = text.replace(" TCC "," tomografia computadorizada cardiovascular ");
		text = text.replace(" TSM "," tipo sangüíneo da mãe ");
		text = text.replace(" VLP "," videolaparoscopia ");
		text = text.replace(" AIG "," adequado para a idade gestacional ");
		text = text.replace(" AINES "," antiinflamatórios não-esteroidais ");
		text = text.replace(" CAPS "," Centro de Atendimento Psicoprofissionalizante ");
		text = text.replace(" CEC "," circulação extracorpórea ");
		text = text.replace(" CIPED "," Centro de Investigação de Doenças Pediátricas ");
		text = text.replace(" CT "," centro de tratamento ");
		text = text.replace(" DG "," ramo diagonal ").replace(" Dg "," ramo diagonal ");//Adicionado 05/09/2012
		text = text.replace(" DPCD "," diálise peritoneal continuada ");
		text = text.replace(" ESV "," extra-sístoles ventriculares ");
		text = text.replace(" IMi "," infarto do miocárdio ");
		text = text.replace(" IVAS "," infecções das vias aéreas superiores ");
		text = text.replace(" PEG "," pré-eclâmpsia grave ");
		text = text.replace(" R1 "," residente 1 ");
		text = text.replace(" RG "," regime geral ");
		text = text.replace(" RxT "," raio X de tórax ");
		text = text.replace(" VD-AD "," ventrículo direito - átrio direito ");
		text = text.replace(" VEF "," volume expiratório forçado ");
		text = text.replace(" mL "," mililitro ");
		text = text.replace(" ABD "," detecção automática de fronteira ");
		text = text.replace(" ACTH "," hormônio adrenocorticotrófico ");
		text = text.replace(" AMO "," alteração de medula óssea ");
		text = text.replace(" BB "," beta-bloqueador ");
		text = text.replace(" BI "," duas ");
		text = text.replace(" BIA "," balão intra-aórtico ");
		text = text.replace(" BX "," biópsia ");
		text = text.replace(" C4 "," complemento 4 ");
		text = text.replace(" CEN "," catéter endonasal ");
		text = text.replace(" CKMB "," fração MB da creatinofosfoquinase ");
		text = text.replace(" DI "," dois ");
		text = text.replace(" DIP "," dipiridamol ");
		text = text.replace(" HP "," Helicobacter pylori ");
		text = text.replace(" LID "," lobo inferior direito ");
		text = text.replace(" MV "," murmúrio vesicular ");
		text = text.replace(" REED "," radiograma de esôfago, estômago e duodeno ");
		text = text.replace(" RNI "," razão normalizada internacional ");
		text = text.replace(" STK "," streptoquinase ");
		text = text.replace(" T2 "," tempo 2 ");
		text = text.replace(" TPP "," trabalho de parto prematuro ");
		text = text.replace(" VPA "," valproato de sódio ");
		text = text.replace(" mCi "," milicurie ");
		text = text.replace(" mg/dl "," miligrama/decilitro ");
		text = text.replace(" AIH "," Autorização de Internação Hospitalar ");
		text = text.replace(" BEG "," bom estado geral ");
		text = text.replace(" CBZ "," carbamazepina ");
		text = text.replace(" CHILD "," Child ");
		text = text.replace(" CIA "," comunicação interatrial ");
		text = text.replace(" D3 "," dia 3 ");
		text = text.replace(" DNA "," ácido desoxirribonucléico ");
		text = text.replace(" ENMG "," eletroneuromiografia ");
		text = text.replace(" ESQ "," esquerdo ");
		text = text.replace(" ESSV "," extra-sístoles supraventriculares ");
		text = text.replace(" FBC "," freqüência do batimento ciliar ");
		text = text.replace(" FS "," freqüência sinusal ");
		text = text.replace(" HT "," hematócrito ");
		text = text.replace(" ITUs "," infecções do trato urinário ");
		text = text.replace(" ITr "," insuficiência tricúspide ");
		text = text.replace(" LT "," leucograma total ");
		text = text.replace(" PTU "," propiltiouracil ");
		text = text.replace(" QD "," quantidade diária ");
		text = text.replace(" QN "," quando necessário ");
		text = text.replace(" R2 "," residente 2 ");
		text = text.replace(" RS "," ritmo sinusal ");
		text = text.replace(" RXT "," raio X de tórax ");
		text = text.replace(" SNC "," sistema nervoso central ");
		text = text.replace(" SO "," sala de observação ");
		text = text.replace(" SVE "," sobrecarga ventricular esquerda ");
		text = text.replace(" VD/AD "," ventrículo direito / átrio direito ");
		text = text.replace(" VPP "," ventilação com pressão positiva ");
		text = text.replace(" s/pp "," sistólico / parede posterior ");
		text = text.replace(" AAA "," aneurisma da aorta abdominal ");
		text = text.replace(" AB "," AB ");
		text = text.replace(" AITs "," ataques isquêmicos transitórios ");
		text = text.replace(" AMBU "," ambulatório ");
		text = text.replace(" BQLT "," bronquiolite ");
		text = text.replace(" CO2 "," dióxido de carbono ");
		text = text.replace(" CTC "," corticóide ");
		text = text.replace(" DPN "," dispnéia paroxística noturna ");
		text = text.replace(" DR "," doutor ");
		text = text.replace(" GH "," hormônio do crescimento ");
		text = text.replace(" hdl "," lipoproteínas de alta densidade ");
		text = text.replace(" ICE "," insuficiência congestiva esquerda ");
		text = text.replace(" IFI "," imunofluorescência indireta ");
		text = text.replace(" KG "," quilograma ");
		text = text.replace(" LBA "," lavado brônquico alveolar ");
		text = text.replace(" LDL "," lipoproteínas de baixa densidade ");
		text = text.replace(" M0 "," medula óssea ");
		text = text.replace(" PAS "," pressão arterial sistêmica ");
		text = text.replace(" PFH "," prova de função hepática ");
		text = text.replace(" PSP "," punção suprapúbica ");
		text = text.replace(" PT "," perímetro ");
		text = text.replace(" QI "," quociente de inteligência ");
		text = text.replace(" RMN "," ressonância magnética ");
		text = text.replace(" SG "," assobrevida global ");
		text = text.replace(" SVs "," sinais vitais ");
		text = text.replace(" T1 "," tempo 1 ");
		text = text.replace(" i-ECA "," inibidores da enzima conversora de angiotensina ");
		text = text.replace(" iECA "," inibidores da enzima conversora de angiotensina ");
		text = text.replace(" mcg/d "," microgramas/decilitro ");
		text = text.replace(" ACM "," artéria cerebral média ");
		text = text.replace(" ACTPs "," angioplastias coronarianas transluminais percutâneas ");
		text = text.replace(" AESP "," atividade elétrica sem pulso ");
		text = text.replace(" AIRV "," alterações inespecíficas da repolarização ventricular ");
		text = text.replace(" ANTI "," anti ");
		text = text.replace(" AVCi "," acidente vascular cerebral isquêmico ");
		text = text.replace(" AVEi "," acidente vascular encefálico isquêmico ");
		text = text.replace(" AZT "," zidovudina ");
		text = text.replace(" B6 "," B6 ");
		text = text.replace(" BQTE "," bronquite ");
		text = text.replace(" CCT "," carcinoma de células transicionais ");
		text = text.replace(" DM1 "," diabetes melitus tipo I ");
		text = text.replace(" EBV "," Epstein-Barr vírus ");
		text = text.replace(" EGDA "," anastomose esofagogastroduodenal ");
		text = text.replace(" FAF "," arma de fogo ");
		text = text.replace(" HBV "," vírus da hepatite B ");
		text = text.replace(" HCG "," hormônio da gonadotrofina coriônica ");
		text = text.replace(" HMV "," Hospital Moinhos de Vento ");
		text = text.replace(" HSL "," Hospital São Lucas ");
		text = text.replace(" IMC "," índice de massa corporal ");
		text = text.replace(" LH "," hormônio luteinizante ");
		text = text.replace(" LIE "," lobo inferior esquerdo ");
		text = text.replace(" MAM "," artéria mamária ");
		text = text.replace(" MED "," médio ");
		text = text.replace(" METs "," equivalentes metabólicos ");
		text = text.replace(" MGCX "," ramo marginal da artéria circunflexa ");
		text = text.replace(" OAA "," obstrução arterial aguda ");
		text = text.replace(" P/C "," polifenol/carboidrato ");
		text = text.replace(" PAAF "," punção aspirativa por agulha fina ");
		text = text.replace(" PCRs "," paradas cardio-respiratórias ");
		text = text.replace(" PNA "," pielonefrite aguda ");
		text = text.replace(" R-x "," raio X ");
		text = text.replace(" SIADH "," síndrome de secreção inapropriada de hormônio anti-diurético ");
		text = text.replace(" SK "," estreptoquinase ");
		text = text.replace(" SOP "," síndrome do ovário policístico ");
		text = text.replace(" SUS "," Sistema Único de Saúde ");
		text = text.replace(" TGI "," trato gastrointestinal ");
		text = text.replace(" TMO "," transplante de medula óssea ");
		text = text.replace(" TPO "," tireoperoxidase ");
		text = text.replace(" TVNS "," taquicardia ventricular não sustentada ");
		text = text.replace(" VD>AD "," ventrículo direito > átrio direito ");
		text = text.replace(" cm2 "," centímetro quadrado ");
		text = text.replace(" o2 "," oxigênio ");
		text = text.replace(" ACV "," aparelho cardiovascular ");
		text = text.replace(" AMPI "," ampicilina ");
		text = text.replace(" BMO "," biópsia de medula óssea ");
		text = text.replace(" CIE "," carótida interna esquerda ");
		text = text.replace(" CM "," centímetro ");
		text = text.replace(" CPAPn "," pressão positiva contínua em vias aéreas nasal ");
		text = text.replace(" CR "," creatinina ");
		text = text.replace(" DMG "," diabetes melitus gestacional ");
		text = text.replace(" DMSA "," ácido dimercaptosuccínico ");
		text = text.replace(" DRA "," doutora ");
		text = text.replace(" DV "," densitovolumetria ");
		text = text.replace(" DVP "," derivação ventrículo peritoneal ");
		text = text.replace(" EMG "," emergência ");
		text = text.replace(" FEVE "," fração de ejeção do ventrículo esquerdo ");
		text = text.replace(" FK "," trakolimus ");
		text = text.replace(" FiO2 "," fração de oxigênio inspirado ");
		text = text.replace(" GA "," gasometria ");
		text = text.replace(" HCO3 "," bicarbonato ");
		text = text.replace(" HPIV "," hemorragia peri-intraventricular ");
		text = text.replace(" HS "," horas ");
		text = text.replace(" HTLV "," vírus T-linfotrópico humano ");
		text = text.replace(" ISQ "," isquêmico ");
		text = text.replace(" KTTP "," tempo de tromboplastina parcialmente ativada ");
		text = text.replace(" LMA "," leucemia mielóide aguda ");
		text = text.replace(" LOC "," lúcido, orientado e consciente ");
		text = text.replace(" LSE "," lobo superior esquerdo ");
		text = text.replace(" MA "," meningites assépticas ");
		text = text.replace(" MAC "," ambulatório de cardiologia ");
		text = text.replace(" MM-DA "," mamária - descendente anterior ");
		text = text.replace(" MMIIs "," membros inferiores ");
		text = text.replace(" MO "," medula óssea ");
		text = text.replace(" MT "," metiltestosterona ");
		text = text.replace(" MTD "," dose máxima tolerada ");
		text = text.replace(" NEO "," neonatal ");
		text = text.replace(" NEURO "," neurologia ");
		text = text.replace(" NPS "," nitroprussiato de sódio ");
		text = text.replace(" ORL "," otorrinolaringologia ");
		text = text.replace(" PLAQ "," plaquetas ");
		text = text.replace(" PPNL "," propranolol ");
		text = text.replace(" PTH "," paratormônio ");
		text = text.replace(" PVC "," pressão venosa central ");
		text = text.replace(" PaO2 "," pressão arterial de oxigênio ");
		text = text.replace(" RA "," rêmora atrial ");
		text = text.replace(" RDW "," amplitude de distribuição eritrocitária ");
		text = text.replace(" RH "," rifampicina e isoniazida ");
		text = text.replace(" RxABD "," raio X abdominal ");
		text = text.replace(" SFA "," sofrimento fetal agudo ");
		text = text.replace(" SM "," silagem de milho ");
		text = text.replace(" SPA "," Serviço de Pronto Atendimento ");
		text = text.replace(" SpO2 "," saturação percutânea de oxigênio ");
		text = text.replace(" TAB "," transtorno afetivo bipolar ");
		text = text.replace(" TAP "," tempo de atividade de protrombina ");
		text = text.replace(" TARV "," terapia antiretroviral ");
		text = text.replace(" TG "," triglicerídeos ");
		text = text.replace(" TILT "," teste de inclinação ortostática ");
		text = text.replace(" UCA "," cultura de urina ");
		text = text.replace(" UCI "," unidade de cuidados intermediários ");
		text = text.replace(" URC "," urocultura ");
		text = text.replace(" US "," ultra-sonografia ");
		text = text.replace(" UTIN "," Unidade de Terapia Intensiva Neonatal ");
		text = text.replace(" VD28 "," ventrículo direito 28 ");
		text = text.replace(" pH "," potencial hidrogeniônico ");
		text = text.replace(" A2 "," amostra 2 ");
		text = text.replace(" ADC "," coeficiente de difusão aparente ");
		text = text.replace(" AMgCx "," ramo marginal da artéria circunflexa ");
		text = text.replace(" ANCA "," anticorpo anticitoplasma de neutrófilo ");
		text = text.replace(" ARV´s "," antiretrovirais ");
		text = text.replace(" BFM "," Berlin-Frankfurt-Münster ");
		text = text.replace(" BQLTE "," bronquiolite ");
		text = text.replace(" CAPD "," diálise peritoneal ambulatorial crônica ");
		text = text.replace(" CC "," cardiopatias congênitas ");
		text = text.replace(" CHC "," carcinoma hepatocelular ");
		text = text.replace(" CPT "," capacidade pulmonar total ");
		text = text.replace(" CREAT "," creatinina ");
		text = text.replace(" CTICC "," Centro de Tratamento Intensivo Clínico-cirúrgico ");
		text = text.replace(" DAOP "," doença arterial obstrutiva periférica ");
		text = text.replace(" DBPOC "," doença broncopulmonar obstrutiva crônica ");
		text = text.replace(" DC "," doença celíaca ");
		text = text.replace(" DDI "," DDI ");
		text = text.replace(" DMO "," densidade mineral óssea ");
		text = text.replace(" DRC "," doença renal crônica ");
		text = text.replace(" DX "," diagnóstico ");
		text = text.replace(" EF "," exame físico ");
		text = text.replace(" EFZ "," efavirenz ");
		text = text.replace(" ELLA "," endoprótese arterial de perna esquerda ");
		text = text.replace(" EPF "," exame parasitológico de fezes ");
		text = text.replace(" EPO "," eritropoetina ");
		text = text.replace(" EQ "," esquema quimioterápico ");
		text = text.replace(" ESBL "," produtoras de beta-lactamase com espectro estendido ");
		text = text.replace(" FOP "," forame oval patente ");
		text = text.replace(" FSH "," hormônio folículo estimulante ");
		text = text.replace(" G3 "," gestação 3 ");
		text = text.replace(" HELLP "," anemia hemolítica, níveis elevados de enzimas hepáticas e contagem baixa de plaquetas ");
		text = text.replace(" HMP "," história médica pregressa ");
		text = text.replace(" IAO "," insuficiência aórtica ");
		text = text.replace(" ICP "," intervenção coronária percutânea ");
		text = text.replace(" IGP "," idade gestacional no parto ");
		text = text.replace(" IN "," intranasais ");
		text = text.replace(" JUP "," junção uretero-piélica ");
		text = text.replace(" L1 "," lombar 1 ");
		text = text.replace(" LM "," lobo médio ");
		text = text.replace(" LV "," leite de vaca ");
		text = text.replace(" MI "," membro inferior ");
		text = text.replace(" MIBI "," metoxi-isobutil-isonitrila ");
		text = text.replace(" MI´s "," membros inferiores ");
		text = text.replace(" MRSA "," Staphylococcus aureus resistente à meticilina ");
		text = text.replace(" MTZ "," mirtazapina ");
		text = text.replace(" MsIS "," membros inferiores ");
		text = text.replace(" NC "," nervo craniano ");
		text = text.replace(" OBS "," observação ");
		text = text.replace(" OD "," olho direito ");
		text = text.replace(" OE "," olho esquerdo ");
		text = text.replace(" PCO2 "," pressão de dióxido de carbono ");
		text = text.replace(" PCP "," pressão capilar pulmonar ");
		text = text.replace(" PCT "," paciente ");
		text = text.replace(" PMAP "," pressão média da artéria pulmonar ");
		text = text.replace(" PNE "," portador de necessidades especiais ");
		text = text.replace(" PNTx "," pneumotórax ");
		text = text.replace(" POA "," Porto Alegre ");
		text = text.replace(" PPD "," derivado protéico purificado ");
		text = text.replace(" PV "," parto vaginal ");
		text = text.replace(" QID "," quadrante inferior direito ");
		text = text.replace(" R-X "," raio X ");
		text = text.replace(" RBV "," ribavirina ");
		text = text.replace(" RC "," risco cardiovascular ");
		text = text.replace(" RCP "," reanimação cardiopulmonar ");
		text = text.replace(" RD "," retinopatia diabética ");
		text = text.replace(" REAG "," reagente ");
		text = text.replace(" RGE "," refluxo gastresofágico ");
		text = text.replace(" RHA "," ruídos hidroaéreos ");
		text = text.replace(" RN2 "," recém-nato 2 ");
		text = text.replace(" RPD "," retinopatia diabética proliferativa ");
		text = text.replace(" RXTX "," raio X de tórax ");
		text = text.replace(" S/N "," se necessário ");
		text = text.replace(" SMC "," serviço médico de cirurgia ");
		text = text.replace(" SMO "," serviço médico de oncologia ");
		text = text.replace(" SR "," senhor ");
		text = text.replace(" SVA "," sonda uretral plástica ");
		text = text.replace(" TCEC "," tempo de circulação extracorpórea ");
		text = text.replace(" TIFF "," Tiffeneau ");
		text = text.replace(" TOT "," tubo orotraqueal ");
		text = text.replace(" TSRN "," tipo sangüíneo do recém-nato ");
		text = text.replace(" UBS "," Unidade Básica de Saúde ");
		text = text.replace(" VA "," vias aéreas ");
		text = text.replace(" VAD "," vincristina, adriblastina e dexametasona ");
		text = text.replace(" VB "," vesícula biliar ");
		text = text.replace(" VCR "," vincristina ");
		text = text.replace(" VED "," diâmetro diastólico ");
		text = text.replace(" VEDF "," ventrículo esquerdo diástole final ");
		text = text.replace(" VES "," diâmetro sistólico ");
		text = text.replace(" VESF "," ventrículo esquerdo sístole final ");
		text = text.replace(" VSVE "," via de saída do ventrículo esquerdo ");
		text = text.replace(" h/h "," de hora em hora ");
		text = text.replace(" A2RV "," alteração de repolarização ventricular ");
		text = text.replace(" AA2 "," aminoácidos ");
		text = text.replace(" ACE "," artéria coronária esquerda ");
		text = text.replace(" ADS "," amniocentese descompressiva seriada ");
		text = text.replace(" AI "," angina instável ");
		text = text.replace(" AINEs "," antiinflamatórios não-esteroidais ");
		text = text.replace(" ANGIO "," angiografia ");
		text = text.replace(" ARA "," antagonistas dos receptores da angiotensina ");
		text = text.replace(" ART "," artéria ");
		text = text.replace(" AVEs "," acidente vascular encefálico ");
		text = text.replace(" AVF "," ante-verso-flexão ");
		text = text.replace(" B1 "," B1 ");
		text = text.replace(" B3 "," terceira bulha ");
		text = text.replace(" B4 "," quarta bulha ");
		text = text.replace(" BBloq "," beta-bloqueadores ");
		text = text.replace(" BC "," bloco cirúrgico ");
		text = text.replace(" BCG "," bacilo de Calmette-Guérin ");
		text = text.replace(" BCPs "," broncopneumonias ");
		text = text.replace(" BDAS "," bloqueios divisionais ântero-superiores ");
		text = text.replace(" BIPAP "," pressão positiva em vias aéreas com dois níveis ");
		text = text.replace(" BNF "," bulhas normofonéticos ");
		text = text.replace(" BT:41 "," bilirrubina total ");
		text = text.replace(" BZD "," benzodiazepínicos ");
		text = text.replace(" BiPAP "," pressão positiva em vias aéreas com dois níveis ");
		text = text.replace(" C1 "," cesariana 1 ");
		text = text.replace(" CAt "," avaliação crítica tópica ");
		text = text.replace(" CD34 "," grupo de diferenciação 34 ");
		text = text.replace(" CHAd "," concentrado de hemácias adulto ");
		text = text.replace(" CIC "," cirurgia cardíaca ");
		text = text.replace(" CID "," Classificação Internacional de Doenças ");
		text = text.replace(" CIP "," carcinoma incidental da próstata ");
		text = text.replace(" CTi "," centro de terapia intensiva ");
		text = text.replace(" CVE "," cardioversão elétrica ");
		text = text.replace(" CVM "," contração voluntária máxima ");
		text = text.replace(" Ca2 "," cálcio ");
		text = text.replace(" CaT "," avaliação crítica tópica ");
		text = text.replace(" D14 "," dia 14 ");
		text = text.replace(" D4 "," dia 4 ");
		text = text.replace(" DA/Dg "," descendente anterior / primeira diagonal ");
		text = text.replace(" DBP "," diâmetro biparietal ");
		text = text.replace(" DHEA "," deidroepiandrosterona ");
		text = text.replace(" DIU "," dispositivo intra-uterino ");
		text = text.replace(" DM-2 "," diabete melitus tipo 2 ");
		text = text.replace(" DMII "," diabete melitus tipo 2 ");
		text = text.replace(" DP-CD "," diagonal posterior - coronária direita ");
		text = text.replace(" DPP "," descolamento prematuro de placenta ");
		text = text.replace(" DPT "," espessamento peritoneal difuso ");
		text = text.replace(" DTS "," dose total semanal ");
		text = text.replace(" DVR "," distúrbio ventilatório restritivo ");
		text = text.replace(" Dg-DA "," primeira diagonal / descendente anterior ");
		text = text.replace(" Dg1 "," primeira diagonal 1 ");
		text = text.replace(" Dm2 "," diabete melitus tipo 2 ");
		text = text.replace(" E-D "," esquerda-direita ");
		text = text.replace(" ECG´s "," ecografias ");
		text = text.replace(" ECT "," eletroconvulsoterapia ");
		text = text.replace(" EDSS "," escala ampliada do estado de incapacidade ");
		text = text.replace(" EFV "," efavirenz ");
		text = text.replace(" EME "," emergência ");
		text = text.replace(" EMEP "," emergência pediátrica ");
		text = text.replace(" EP "," epitélio pigmentado ");
		text = text.replace(" FCmáx "," função cardíaca máxima ");
		text = text.replace(" FEJ "," fração de ejeção ");
		text = text.replace(" FEM "," feminino ");
		text = text.replace(" FEV "," fevereiro ");
		text = text.replace(" FEj "," fração de ejeção ");
		text = text.replace(" FH "," formação do hipocampo ");
		text = text.replace(" FID "," fossa ilíaca direita ");
		text = text.replace(" FSV "," fundo de saco vaginal ");
		text = text.replace(" G4 "," gestação 4 ");
		text = text.replace(" G6PD "," glicose-6-fosfato dehidrogenase ");
		text = text.replace(" GI "," gastro intestinal ");
		text = text.replace(" GNMP "," glomerulonefrite membrano-proliferativa ");
		text = text.replace(" GRAFT "," Graft ");
		text = text.replace(" GRAM "," gram ");
		text = text.replace(" GT "," glutariltransferase ");
		text = text.replace(" GTS "," gotas ");
		text = text.replace(" HAS,e "," hipertensão arterial sistêmica ");
		text = text.replace(" HBAE "," hemibloqueio anterior esquerdo ");
		text = text.replace(" HBP "," hiperplasia benigna da próstata ");
		text = text.replace(" HBs "," vírus da hepatite B ");
		text = text.replace(" HCSA "," Hospital da Criança Santo Antônio ");
		text = text.replace(" HIC "," hemorragia intracraniana ");
		text = text.replace(" HM "," hipertermia maligna ");
		text = text.replace(" HMCs "," hemoculturas ");
		text = text.replace(" HNF "," heparina não-fracionada ");
		text = text.replace(" HTC "," hematócrito ");
		text = text.replace(" HX "," histórico ");
		text = text.replace(" Ht/Hb "," hematócrito/hemoglobina ");
		text = text.replace(" IA "," infarto agudo ");
		text = text.replace(" IAM's "," infartos agudos do miocárdio ");
		text = text.replace(" IF "," forma indeterminada ");
		text = text.replace(" IFN "," interferon alfa-recombinante ");
		text = text.replace(" IMT "," calibre intermediário da carótida ");
		text = text.replace(" IOT "," intubação orotraqueal ");
		text = text.replace(" IPC "," índice de potencial de contaminação ");
		text = text.replace(" ISHAK "," Ishak ");
		text = text.replace(" ISRS "," inibidor seletivo da recaptação de serotonina ");
		text = text.replace(" ITC "," insuficiência tricúspide ");
		text = text.replace(" IVa "," veia interventricular anterior ");
		text = text.replace(" IX "," 9 ");
		text = text.replace(" K2 "," potássio ");
		text = text.replace(" L2-L3 "," lombar 2 - lombar 3 ");
		text = text.replace(" L3 "," lombar 3 ");
		text = text.replace(" L4 "," lombar 4 ");
		text = text.replace(" L4-L5 "," lombar 4 - lombar 5 ");
		text = text.replace(" L5-S1 "," lombar 5 - sacro 1 ");
		text = text.replace(" LE "," laparotomia exploradora ");
		text = text.replace(" LFN "," linfonodos ");
		text = text.replace(" LI "," liquido intersticial ");
		text = text.replace(" LLA "," leucemia linfocítica aguda ");
		text = text.replace(" LLC "," leucemia linfocítica crônica ");
		text = text.replace(" LMC "," leucemia mielóide crônica ");
		text = text.replace(" LN "," linfonodo ");
		text = text.replace(" LNH "," linfoma não-Hodgkin ");
		text = text.replace(" LQR "," líquor ");
		text = text.replace(" LUTS "," sintomas do trato urinário inferior ");
		text = text.replace(" MELD "," modelo para doença hepática terminal ");
		text = text.replace(" MF "," microorganismos filamentosos ");
		text = text.replace(" MGCx "," ramo marginal da artéria circunflexa ");
		text = text.replace(" MGLIS "," artéria coronária marginal localizada intrastent ");
		text = text.replace(" MMG "," mamografia ");
		text = text.replace(" MMSS "," membros superiores ");
		text = text.replace(" MSS "," membros superiores ");
		text = text.replace(" MSSA "," Staphylococcus aureus sensível à meticilin ");
		text = text.replace(" MSs "," membros superiores ");
		text = text.replace(" Mg1 "," primeira marginal ");
		text = text.replace(" MsSs "," membros superiores ");
		text = text.replace(" NAC "," neuropatia autonômica cardiovascular ");
		text = text.replace(" NAN "," leite NAN ");
		text = text.replace(" ND "," nefropatia diabética ");
		text = text.replace(" NOV "," novembro ");
		text = text.replace(" NYHA "," Associação do Coração de Nova York ");
		text = text.replace(" Na3 "," potássio ");
		text = text.replace(" OEA "," otoemisiones acústicas ");
		text = text.replace(" OFT "," oftalmologia ");
		text = text.replace(" OUT "," outubro ");
		text = text.replace(" P4 "," parto 4 ");
		text = text.replace(" PAAf "," punção aspirativa por agulha fina ");
		text = text.replace(" PAC "," pneumonia adquirida na comunidade ");
		text = text.replace(" PASP "," pressão sistólica da artéria pulmonar ");
		text = text.replace(" PAVM "," pneumonia associada à ventilação mecânica ");
		text = text.replace(" PFE "," peso fetal estimado ");
		text = text.replace(" PICC "," catéter central de inserção periférica ");
		text = text.replace(" PNI "," psiconeuroimunologia ");
		text = text.replace(" PNM "," pneumonia ");
		text = text.replace(" PNP "," polineuropatia ");
		text = text.replace(" PO2 "," pressão parcial do oxigênio ");
		text = text.replace(" POP "," procedimento operacional padrão ");
		text = text.replace(" PS-MG "," Pronto Socorro de Minas Gerais ");
		text = text.replace(" PSG "," polissonografia ");
		text = text.replace(" PUCRS "," Pontifícia Universidade Católica do Rio Grande do Sul ");
		text = text.replace(" PVM "," prolapso da valva mitral ");
		text = text.replace(" R3 "," residente 3 ");
		text = text.replace(" RCR "," ressuscitação cardio-respiratória ");
		text = text.replace(" RCT "," rastreamento corporal total com radioiodo ");
		text = text.replace(" RDNPM "," retardo do desenvolvimento neuropsicomotor ");
		text = text.replace(" RDP "," retinopatia diabética proliferativa ");
		text = text.replace(" RE "," retículo esquerdo ");
		text = text.replace(" RHJ "," refluxo hepato-jugular ");
		text = text.replace(" RR "," risco relativo ");
		text = text.replace(" RT "," radioterapia ");
		text = text.replace(" RTX "," neurotoxina resiniferatoxina ");
		text = text.replace(" RUB "," rubéola ");
		text = text.replace(" S1 "," sacro 1 ");
		text = text.replace(" SAD "," sobrecarga atrial direita ");
		text = text.replace(" SAE "," sobrecarga atrial esquerda ");
		text = text.replace(" SCD "," seio coronário distal ");
		text = text.replace(" SMD "," síndrome mielodisplásica ");
		text = text.replace(" SOG "," sonda orogástrica ");
		text = text.replace(" SP "," sala de politraumatizados ");
		text = text.replace(" SPECT "," spect cardíaco ");
		text = text.replace(" SULFA "," sulfametoxazol ");
		text = text.replace(" T12 "," torácica 12 ");
		text = text.replace(" T4L "," tetraiodotironina ");
		text = text.replace(" T:0,7 "," troponina 0 ");
		text = text.replace(" T:2,1 "," troponina 2 ");
		text = text.replace(" THB "," transtorno de humor bipolar ");
		text = text.replace(" TIG5 "," taxa de infusão de glicose ");
		text = text.replace(" TISQ "," tempo de isquemia ");
		text = text.replace(" TJV "," transfusão intravascular ");
		text = text.replace(" TMP "," trimetoprim ");
		text = text.replace(" TRAM "," retalho miocutâneo transverso abdominal ");
		text = text.replace(" TSC "," tiragem subcostal ");
		text = text.replace(" TSE "," spin-eco turbo ");
		text = text.replace(" TSH:5 "," hormônio tíreo-estimulante 5 ");
		text = text.replace(" TTG "," teste de tolerância a glicose ");
		text = text.replace(" TU "," trato urinário ");
		text = text.replace(" TVS "," taquicardias ventriculares monomórficas sustentadas ");
		text = text.replace(" TnT "," troponina ");
		text = text.replace(" TxH "," transplante hepático ");
		text = text.replace(" UCC "," Unidade de Cardiopatias Congênitas ");
		text = text.replace(" UFC "," unidades formadoras de colônia ");
		text = text.replace(" UR "," uréia ");
		text = text.replace(" URO "," urocultura ");
		text = text.replace(" VA-AD "," valvula anterior do átrio direito ");
		text = text.replace(" VANCO "," vancomicina ");
		text = text.replace(" VAS "," vias aéreas superiores ");
		text = text.replace(" VD17 "," ventrículo direito 17 ");
		text = text.replace(" VEd "," diástole do ventrículo esquerdo ");
		text = text.replace(" VEs "," sístole do ventrículo esquerdo ");
		text = text.replace(" VMA "," ácido vanilmandélico ");
		text = text.replace(" VR "," via retal ");
		text = text.replace(" Vo2 "," volume de oxigênio ");
		text = text.replace(" XII "," 12 ");
		text = text.replace(" b2 "," beta 2 ");
		text = text.replace(" g/dia "," grama/dia ");
		text = text.replace(" mVE "," massa ventricular esquerda ");
		text = text.replace(" mnmHg "," milímetros de mercúrio ");
		text = text.replace(" pCO2 "," pressão de dióxido de carbono ");
		text = text.replace(" r-x "," raio X ");
		text = text.replace(" s/n "," se necessário ");
		text = text.replace(" satO2 "," saturação de oxigênio ");
		text = text.replace(" vO "," via oral ");
		
		return text;
		
	}
	
	
	
	
	
	/**
	 * ANTIGOS MÉTODOS DA CLASSE RULES
	 */
	
	
	
	
	
	
	
	/**
	 * Obtém quantidade de palavras de uma regra
	 * @param arr
	 * @return
	 */
	//public static int getRuleSize(String[] arr){
	public int getRuleSize(Regra rule){
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
	public List<String> getWords(int pos, int ruleSize, List<Token> tokens){
		
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
	public String[] getTags(int pos, int ruleSize, List<Token> tokens){
		
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
	 * @param rule
	 * @param words
	 * @return
	 */
	public boolean ruleApplies(String[] tags, Regra rule, List<String> words){
		
		//Percorre palavras e regras
		//Ex: Isso é uma frase - [V_INF][N_M_S][PREP][N_F_P]
		
		for(int i = 0; i < getRuleSize(rule); i++){

			Termo t = rule.termos.get(i);
			String termo = t.termo;
			
/*			System.out.print(tags[i] + " = " + termTaggerStemSub o + "_");
			System.out.println();
*/			
			//Se for regra de STEMMING
			if(isStemRule(t)){
				//System.out.println(TaggerStemRoot.getStem(words.get(i)));
				//System.out.println(termo.replace("*", ""));
				//System.out.println("-----");
				if(!TaggerStemRoot.getStem(words.get(i)).equalsIgnoreCase(termo.replace("*", "")))
				{
					return false;
				}
			}
			else{
				//TODO: Retirar underlines na obtenção do POS-tagging
				if(!tags[i].equalsIgnoreCase(termo + "_"))
				{
					return false;
				}
			}
		
		}

		return true;
		
	}
	
	/**
	 * Verifica é regra de STEMMING
	 * @param rule
	 * @return
	 */
	public boolean isStemRule(Termo t){
		
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
	public boolean isPOSRule(Termo t){
		
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
	public String getPrintableWords(List<String> list){
		
		String res = "";
		
		try{
			for(String str: list){
				
				res += str + " ";
				
			}
			
		}
		catch (Exception e) {
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

// original		return new Regra().findByElement(8, 2, true);

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
