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
		//Retira erros recorrentes nos sum�rios
		text = retiraErrosRecorrentes(text);
		//Retira pontua��o e caracteres especiais
		text = retiraPontuacao(text);
		//Espa�a ponto
		text = espacaPontuacao(text);
		//Expande Acr�nimos
		text = expandirAcronimos(text);
		//Coloca em min�sculas
		text = text.toLowerCase();
		//Retira Stopwords
		text = retiraStopWords(text);
		
		return text;
		
	}
	

	//Tagger para Interface gr�fica
	public String TaggerInterface(String text_sumario, String text_selecionado, boolean isWeb){
		
		//Executa opera��es de PR�-PROCESSAMENTO
		text_sumario = preProccessText(text_sumario);
		text_selecionado = preProccessText(text_selecionado);
		
		//Separa texto selecionado em palavras
		String text_separado[] = text_selecionado.split(" ");
				
		String res = "";
		String res1 = "";

		//Separa texto em senten�as
		String[] sentencas = cogroo.sentDetect(text_sumario);
		for (String sentenca : sentencas) {
			
			//Tokeniza senten�a
			SentenceCogroo sc = new SentenceCogroo(sentenca);
			List<Token> tokens = null;
			cogroo.tokenizer(sc);

			//Aplica o NAMEFINDER
			cogroo.nameFinder(sc);
			
			//Expans�o de preposi��es
			cogroo.preTagger(sc);
			
			//Realiza POS_tagging
			cogroo.tagger(sc);
			tokens = sc.getTokens();
			
			//Procura onde est�o os termos selecionados
			//Compara um termo com o primeiro do vetor separado, caso encontre, ve se os termos
			//seguintes tamb�m s�o os esperados
			boolean igual = false;
			String tags = null;
			String palavras = null;
			
			for(int i=0; i < tokens.size(); i++){
				for(int j=0; j < text_separado.length; j++){
					if(text_separado[j].equals(tokens.get(i+j).getLexeme()))
						igual = true;
					else{
						igual = false;
						break;}}
				
				//Cria a string a ser retornada,
				//a partir do indice do primeiro termo esperado encontrado. 
				if(igual){
					for(int k=0; k < text_separado.length; k++){
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
	 * Executa etiqueta��o de texto recebido com o Cogroo
	 * @param text
	 * @return
	 */
	public String tagTextCogroo(String text, boolean isWeb){
		
		//Executa opera��es de PR�-PROCESSAMENTO
		text = preProccessText(text);
		
		String res = "";
		String res1 = "";

		//Separa texto em senten�as
		String[] sentencas = cogroo.sentDetect(text);
		for (String sentenca : sentencas) {
			
			//Tokeniza senten�a
			SentenceCogroo sc = new SentenceCogroo(sentenca);
			List<Token> tokens = null;
			cogroo.tokenizer(sc);

			//Aplica o NAMEFINDER
			cogroo.nameFinder(sc);
			
			//Expans�o de preposi��es
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
		
		//Executa opera��es de PR�-PROCESSAMENTO
		text = preProccessText(text);
		
		System.out.println(text);
		
		//Quantidade de palavras por regra
		int qtdePalavrasRegra = 0;
		
		//Armazena sa�da do resultado
		String res = "";
		
		//Lista de TOKENS
		List<Token> sent = new ArrayList<Token>();
		
		//Separa as senten�as e efetua pos-tagging
		String[] sentencas = cogroo.sentDetect(text);
		
		//INVERTE ORDEM DAS SENTEN�AS - Melhor aproveitamento para CONTINUIDADE/ENCAMINHAMENTO
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
    		 * VERIFICAR SE REGRA TEM SUBREGRAS, SE SIM, PERCORR�-LAS ANTES DE BUSCAR REGRA ATUAL
    		 * - Se n�o encontrar nenhuma das sub-regras, buscar regra-base
    		 * 
    		 * Quanto mais espec�fica(MAIOR QTDE DE TERMOS) uma regra tiver, MENOR A CHANCE DE OBTEN��O DE FALSO POSITIVO.
    		 * Portanto, se as regras mais espec�ficas forem analisadas anteriormente, o algoritmo estaria privilegiando regras
    		 * com maior chance de VERDADEIRO POSITIVO
    		 * 
    		 * OBS: Ser� que apenas uma invers�o de ordem das regras no algoritmo SEM SUB-REGRAS j� n�o resolveria?
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
    			
				//Obt�m quantidade de palavras da regra
		    	qtdePalavrasRegra = this.getRuleSize(r);
		        
		    	//System.out.println("Regra Atual: " + Rules.getPrintableWords(r) + "\n\n");
		        //System.out.println("Qtde palavras: " + sent.size());
		        //System.out.println("Qtde palavras regras: " + qtdePalavrasRegra);
		    	
		        //Percorre palavras
		        for(int i = 0; i < sent.size(); i++){
		        
		        	//Obt�m as palavras correspondentes a regra definida
		        	List<String> words = this.getWords(i, qtdePalavrasRegra, sent);
		        	wordsEtags = wordsEtags + "[" + i + "]" + "[" + sent.get(i).toString() + "]";
		        	wordsEtags = wordsEtags + "[" + sent.get(i).getMorphologicalTag() + "]";
//					System.out.println("words[i] " + i + " " + sent.get(i).getMorphologicalTag() + "");

		        	try{
		        		
			        	//Se n�o faltaram palavras para completar a regra
		        		if(words.size() == qtdePalavrasRegra){
			        	
				        	//Obt�m as TAGS das palavras correspondentes a regra definida
			        		String tags[] = this.getTags(i, qtdePalavrasRegra, sent);
				        	//Se foi encontrada ocorr�ncia da regra no trecho atual
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
			System.out.println("parada n�o encontrado");
		    Scanner scan = new Scanner(System.in);
		    scan.next();
			
		    return "N�o encontrado!";
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
	 * Obt�m o STEM da palavra
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
	    	text = text.replace("/mar/"," de mar�o de ");
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
	    	text = text.replace("/03/"," de mar�o de ");
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
	    	text = text.replace("/mar"," de mar�o");
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
	    	text = text.replace("/03"," de mar�o");
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
		
		
		text = text.replace(" AAS "," �cido acetil salic�lico ");
		text = text.replace(" ACTP "," angioplastia coron�ria transluminal percut�nea ");
		text = text.replace(" HCPA "," Hospital de Cl�nicas de Porto Alegre ");
		text = text.replace(" HAS "," hipertens�o arterial sist�mica ");
		text = text.replace(" VO "," via oral ");
		text = text.replace(" ICC "," insufici�ncia card�aca congestiva ");
		text = text.replace(" FE "," fra��o de eje��o ");
		text = text.replace(" ECG "," eletrocardiograma ");
		text = text.replace(" VE "," ventr�culo esquerdo ");
		text = text.replace(" IAM "," infarto agudo do mioc�rdio ");
		text = text.replace(" CD "," coron�ria direita ");
		text = text.replace(" DAE "," dimens�o do �trio esquerdo ");
		text = text.replace(" DA "," art�ria descendente anterior ");
		text = text.replace(" TC "," tomografia computadorizada ");
		text = text.replace(" CAT "," avalia��o cr�tica t�pica ");
		text = text.replace(" mg/d "," miligrama/decilitro ");
		text = text.replace(" CTI "," Centro de Terapia Intensiva ");
		text = text.replace(" DM "," diabetes melitus ");
		text = text.replace(" CRM "," cardio-resson�ncia magn�tica ");
		text = text.replace(" AE "," �trio esquerdo ");
		text = text.replace(" RN "," rec�m-nato ");
		text = text.replace(" STENT "," stent ");
		text = text.replace(" IG "," imunoglobulina ");
		text = text.replace(" IRC "," insufici�ncia renal cr�nica ");
		text = text.replace(" TIMI "," medida do fluxo coron�rio e microvascular ");
		text = text.replace(" APGAR "," Apgar ");
		text = text.replace(" NR "," n�o-reagente ");
		text = text.replace(" AVC "," acidente vascular cerebral ");
		text = text.replace(" ACD "," art�ria coron�ria direita ");
		text = text.replace(" CX "," art�ria circunflexa ");
		text = text.replace(" RX "," raio X ");
		text = text.replace(" DPOC "," doen�a pulmonar obstrutiva cr�nica ");
		text = text.replace(" PSAP "," press�o sist�lica da art�ria pulmonar ");
		text = text.replace(" PCR "," parada c�rdio-respirat�ria ");
		text = text.replace(" ECO "," ecocardiograma ");
		text = text.replace(" VD "," ventr�culo direito ");
		text = text.replace(" IC "," insufici�ncia card�aca ");
		text = text.replace(" EV "," endovenosa ");
		text = text.replace(" O2 "," oxig�nio ");
		text = text.replace(" FC "," fun��o card�aca ");
		text = text.replace(" NPH "," protamina neutra Hagedorn ");
		text = text.replace(" HCTZ "," hidroclortiazida ");
		text = text.replace(" FO "," ferida operat�ria ");
		text = text.replace(" INR "," raz�o normalizada internacional ");
		text = text.replace(" BID "," duas vezes ao dia ");
		text = text.replace(" PA "," press�o arterial ");
		text = text.replace(" SCA "," s�ndrome coronariana aguda ");
		text = text.replace(" VDRL "," laborat�rio de pesquisa de doen�as ven�reas ");
		text = text.replace(" ADA "," art�ria descendente anterior ");
		text = text.replace(" ACFA "," fibrila��o atrial cr�nica ");
		text = text.replace(" SL "," sublingual ");
		text = text.replace(" CI "," cardiopatia isqu�mica ");
		text = text.replace(" MP "," marca-passo ");
		text = text.replace(" ITU "," infec��o do trato urin�rio ");
		text = text.replace(" BCP "," broncopneumonia ");
		text = text.replace(" DM2 "," diabetes melitus tipo 2 ");
		text = text.replace(" UI "," unidades internacionais ");
		text = text.replace(" ATB "," antibi�tico ");
		text = text.replace(" TS "," tipo sang��neo ");
		text = text.replace(" TID "," dilata��o isqu�mica transit�ria ");
		text = text.replace(" FA "," fibrila��o atrial ");
		text = text.replace(" TCE "," tronco de coron�ria esquerda ");
		text = text.replace(" II "," 2 ");
		text = text.replace(" IM "," insufici�ncia mitral ");
		text = text.replace(" PS "," pronto socorro ");
		text = text.replace(" HIV "," v�rus da imunodefici�ncia humana ");
		text = text.replace(" mmHg "," mil�metro de merc�rio ");
		//text = text.replace(" NA "," s�dio ");//Retirado 05/09/2012
		text = text.replace(" HCV "," v�rus da hepatite C ");
		text = text.replace(" AP "," ausculta pulmonar ");
		text = text.replace(" IgG "," imunoglobulina G ");
		text = text.replace(" MIE "," membro inferior esquerdo ");
		text = text.replace(" PP "," parto prematuro ");
		text = text.replace(" TP "," trabalho de parto ");
		text = text.replace(" AC "," ausculta card�aca ");
		text = text.replace(" HMG "," hemograma ");
		text = text.replace(" ACX "," art�ria coronariana circunflexa ");
		text = text.replace(" EQU "," exame qualitativo de urina ");
		text = text.replace(" UTI "," Unidade de Terapia Intensiva ");
		text = text.replace(" IR "," insufici�ncia renal ");
		text = text.replace(" SV "," supraventricular ");
		text = text.replace(" DP "," doen�a periapical ");
		text = text.replace(" III "," 3 ");
		text = text.replace(" IV "," 4 ");
		text = text.replace(" MID "," membro inferior direito ");
		text = text.replace(" RNM "," resson�ncia nuclear magn�tica ");
		text = text.replace(" RN1 "," rec�m-nato 1 ");
		text = text.replace(" TSH "," horm�nio t�reo-estimulante ");
		text = text.replace(" CDI "," cardioversor-desfibrilador implant�vel ");
		text = text.replace(" VM "," ventila��o mec�nica ");
		text = text.replace(" BRE "," bloqueio de ramo esquerdo ");
		text = text.replace(" IGG "," imunoglobulina G ");
		text = text.replace(" EDA "," endoscopia digestiva alta ");
		text = text.replace(" IT "," transi��o interna ");
		text = text.replace(" BAV "," bloqueio atrioventricular ");
		text = text.replace(" CHAD "," concentrado de hem�cias adulto ");
		text = text.replace(" IgM "," imunoglobulina M ");
		text = text.replace(" DD "," di�metro diast�lico ");
		text = text.replace(" DS "," di�metro sist�lico ");
		text = text.replace(" HPS "," Hospital de Pronto Socorro ");
		text = text.replace(" CK "," creatinoquinase ");
		text = text.replace(" MEI "," medicina interna ");
		text = text.replace(" PC "," parada card�aca ");
		text = text.replace(" MG "," miligrama ");
		text = text.replace(" TOXO "," toxoplamose ");
		text = text.replace(" ADAE "," �trio direito �trio esquerdo ");
		text = text.replace(" IGM "," imunoglobulina M ");
		text = text.replace(" SC "," subcut�neo ");
		text = text.replace(" CA "," c�ncer ");
		text = text.replace(" AV "," �trio ventricular ");
		text = text.replace(" BAAR "," bacilo �lcool �cido resistente ");
		text = text.replace(" BT "," bilirrubina total ");
		text = text.replace(" EGD "," esofagogastroduodenoscopia ");
		text = text.replace(" TV "," taquicardias ventriculares ");
		text = text.replace(" QT "," quimioterapia ");
		text = text.replace(" TEP "," tromboembolismo pulmonar ");
		text = text.replace(" TVP "," trombose venosa profunda ");
		text = text.replace(" TA "," art�ria transversa ");
		text = text.replace(" CO "," mon�xido de carbono ");
		text = text.replace(" CP "," comprimido ");
		text = text.replace(" EHCPA "," servi�o de emerg�ncias do Hospital de Cl�nicas de Porto Alegre ");
		text = text.replace(" EPED "," especialidades pedi�tricas ");
		text = text.replace(" HBsAg "," ant�geno de superf�cie do v�rus da hepatite B ");
		text = text.replace(" BD "," morte cerebral ");
		text = text.replace(" SSST "," sem supradesn�vel do segmento ST ");
		text = text.replace(" DDVE "," di�metro diast�lico do ventr�culo esquerdo ");
		text = text.replace(" DCE "," depura��o de creatinina end�gena ");
		text = text.replace(" MsIs "," membros inferiores ");
		text = text.replace(" SF "," soro fisiol�gico ");
		text = text.replace(" VP "," vasopressina ");
		text = text.replace(" CPAP "," press�o positiva cont�nua em vias a�reas ");
		text = text.replace(" HD "," hemodi�lise ");
		text = text.replace(" ITB "," �ndice tornozelo/bra�o ");
		text = text.replace(" IRA "," insufici�ncia renal aguda ");
		text = text.replace(" TTO "," tratamento ");
		text = text.replace(" DSVE "," di�metro sist�lico do ventr�culo esquerdo ");
		text = text.replace(" IECA "," inibidor da enzima conversora de angiotensina ");
		text = text.replace(" VEF1 "," volume expirat�rio for�ado 1 ");
		text = text.replace(" BAVT "," bloqueio atrioventricular total ");
		text = text.replace(" CF "," colo femoral ");
		text = text.replace(" DVO "," dist�rbio ventilat�rio obstrutivo ");
		text = text.replace(" RTU "," ressec��o transuretral ");
		text = text.replace(" EAP "," edema agudo de pulm�o ");
		text = text.replace(" ICT "," �ndice cardiotor�cico ");
		text = text.replace(" MSE "," membro superior esquerdo ");
		text = text.replace(" ACx "," art�ria circunflexa A ");
		text = text.replace(" AVE "," acidente vascular encef�lico ");
		text = text.replace(" HMC "," hemocultura ");
		text = text.replace(" TB "," tuberculose ");
		text = text.replace(" B2 "," ausculta card�aca ");
		text = text.replace(" FR "," freq��ncia respirat�ria ");
		text = text.replace(" TE "," teste ergom�trico ");
		text = text.replace(" ATC "," angioplastia transluminal coron�ria ");
		text = text.replace(" IGO "," �ndice do grau de obesidade ");
		text = text.replace(" NPT "," nutri��o parenteral total ");
		text = text.replace(" CD4 "," grupo de diferencia��o 4 ");
		text = text.replace(" CMV "," citomegalov�rus ");
		text = text.replace(" EEG "," eletroencefalograma ");
		text = text.replace(" FV "," fibrila��o ventricular ");
		text = text.replace(" RV "," remodelamento ventricular ");
		text = text.replace(" VI "," 6 ");
		text = text.replace(" AJ "," antes do jantar ");
		text = text.replace(" HbsAg "," ant�geno de superf�cie da hepatite B ");
		text = text.replace(" PL "," pun��o lombar ");
		text = text.replace(" PSA "," ant�geno espec�fico da pr�stata ");
		text = text.replace(" RHZ "," rifampicina, isoniazida e pirazinamida ");
		text = text.replace(" TT "," transtor�cico ");
		text = text.replace(" CK-MB "," fra��o MB da creatinofosfoquinase ");
		text = text.replace(" MSD "," membro superior direito ");
		text = text.replace(" T4 "," tiroxina ");
		text = text.replace(" UTIP "," Unidade de Terapia Intensiva Pedi�trica ");
		text = text.replace(" AA "," ap�s almo�o ");
		text = text.replace(" ACO "," anticoagulante oral ");
		text = text.replace(" LDH "," desidrogenase l�ctica ");
		text = text.replace(" MM "," mieloma m�ltiplo ");
		text = text.replace(" NPO "," nada por via oral ");
		text = text.replace(" TGO "," transaminase glut�mica oxalac�tica ");
		text = text.replace(" ECGs "," eletrocardiogramas ");
		text = text.replace(" HVE "," hipertrofia ventricular esquerda ");
		text = text.replace(" METS "," equivalente metab�lico ");
		text = text.replace(" PBF "," perfil biof�sico fetal ");
		text = text.replace(" SNE "," sonda nasoenteral ");
		text = text.replace(" CVF "," capacidade vital for�ada ");
		text = text.replace(" DDD "," DDD ");
		text = text.replace(" HGT "," hemoglicoteste ");
		text = text.replace(" ILA "," �ndice de l�quido amni�tico ");
		text = text.replace(" OK "," ok ");
		text = text.replace(" SVD "," sobrecarga ventricular direita ");
		text = text.replace(" AIT "," ataque isqu�mico transit�rio ");
		text = text.replace(" HGTs "," hemoglicotestes ");
		text = text.replace(" MMII "," membros inferiores ");
		text = text.replace(" MgCx "," ramo marginal da art�ria circunflexa ");
		text = text.replace(" PAM "," press�o arterial m�dia ");
		text = text.replace(" PO "," p�s-operat�rio ");
		text = text.replace(" TET "," tubo endotraqueal ");
		text = text.replace(" HAP "," hipertens�o arterial pulmonar ");
		text = text.replace(" MTX "," metotrexato ");
		text = text.replace(" PMT "," prematuro ");
		text = text.replace(" PPL "," press�o pleural ");
		text = text.replace(" PUC "," Pontif�cia Universidade Cat�lica ");
		text = text.replace(" RDT "," radioterapia ");
		text = text.replace(" SIDA "," s�ndrome da imunodefici�ncia adquirida ");
		text = text.replace(" SN "," sistema nervoso ");
		text = text.replace(" VSG "," velocidade de sedimenta��o globular ");
		text = text.replace(" VSR "," v�rus sincicial respirat�rio ");
		text = text.replace(" BR "," bloqueio de ramo ");
		text = text.replace(" CE "," coron�ria esquerda ");
		text = text.replace(" DN "," data do nascimento ");
		text = text.replace(" ECA "," enzima conversora de angotensina ");
		text = text.replace(" EEF "," escala de express�es faciais ");
		text = text.replace(" FNB "," fenobarbital ");
		text = text.replace(" NS "," n�o significativo ");
		text = text.replace(" NTG "," nitroglicerina ");
		text = text.replace(" PROX "," pr�ximo ");
		text = text.replace(" SST "," supradesn�vel do segmento ST ");
		text = text.replace(" TGP "," transaminase glut�mico-pir�vica ");
		text = text.replace(" TIG "," imunoglobulina antitet�nica humana ");
		text = text.replace(" AINE "," antiinflamat�rio n�o-esteroidal ");
		text = text.replace(" CN "," cat�ter nasal ");
		text = text.replace(" CPRE "," colangiopancreatografia retr�grada endosc�pica ");
		text = text.replace(" HDA "," hemorragia digestiva alta ");
		text = text.replace(" LCR "," licor ");
		text = text.replace(" Mg-Cx "," ramo marginal da art�ria circunflexa ");
		text = text.replace(" PBE "," peritonite bacteriana espont�nea ");
		text = text.replace(" PN "," pneumonia ");
		text = text.replace(" BCF "," batimentos card�acos fetais ");
		text = text.replace(" CV "," carga viral ");
		text = text.replace(" HB "," hemoglobina ");
		text = text.replace(" IAo "," insufici�ncia a�rtica ");
		text = text.replace(" LSD "," lobo superior direito ");
		text = text.replace(" RxTx "," raio X de t�rax ");
		text = text.replace(" SAMU "," Servi�o de Atendimento M�dico de Urg�ncia ");
		text = text.replace(" x/dia "," vezes ao dia ");
		text = text.replace(" AD "," �trio direito ");
		text = text.replace(" ARV "," anti-retro viral ");
		text = text.replace(" AVCs "," acidentes vasculares cerebrais ");
		text = text.replace(" CCA "," cardiopatias cong�nitas em adultos ");
		text = text.replace(" CEA "," ant�geno carcinoembrion�rio ");
		text = text.replace(" CIV "," comunica��o interventricular ");
		text = text.replace(" DRGE "," doen�a do refluxo gastresof�gico ");
		text = text.replace(" GGT "," gama GT ");
		text = text.replace(" HBSAG "," ant�geno de superf�cie para hepatite B ");
		text = text.replace(" HF "," hist�ria familiar ");
		text = text.replace(" I-ECA "," inibidor da enzima conversora de angiotensina ");
		text = text.replace(" IAMs "," infarto agudo do mioc�rdio ");
		text = text.replace(" IVC "," insufici�ncia venosa cr�nica ");
		text = text.replace(" MgCX "," ramo moarginal da art�ria circunflexa ");
		text = text.replace(" SNG "," sonda nasog�strica ");
		text = text.replace(" TBC "," tuberculose ");
		text = text.replace(" VO2 "," volume de oxig�nio ");
		text = text.replace(" B12 "," B12 ");
		text = text.replace(" BRD "," bloqueio de ramo direito ");
		text = text.replace(" EA "," emerg�ncia ambulatorial ");
		text = text.replace(" FAN "," fator anti-nuclear ");
		text = text.replace(" FAV "," favor�vel ");
		text = text.replace(" HNSC "," Hospital Nossa Senhora da Concei��o ");
		text = text.replace(" MAP "," press�o m�dia de vias a�reas ");
		text = text.replace(" NEG "," negativo ");
		text = text.replace(" OMA "," otite m�dia aguda ");
		text = text.replace(" QMT "," quimioterapia ");
		text = text.replace(" S-PP "," sist�lico - parede posterior ");
		text = text.replace(" TX "," t�rax ");
		text = text.replace(" VCI "," veia cava inferior ");
		text = text.replace(" VCM "," volume corpuscular m�dio ");
		text = text.replace(" C3 "," complemento 3 ");
		text = text.replace(" CIG "," cigarro ");
		text = text.replace(" CPER "," colangiopancreatografia endosc�pica retr�grada ");
		text = text.replace(" DIPI "," dipiridamol ");
		text = text.replace(" EX "," ex ");
		text = text.replace(" HPB "," hiperplasia prost�tica benigna ");
		text = text.replace(" IVUS "," ultra-sonografia intravascular ");
		text = text.replace(" NBZ "," nebuliza��o ");
		text = text.replace(" PIG "," pequeno para idade gestacional ");
		text = text.replace(" RM "," resson�ncia magn�tica ");
		text = text.replace(" TCC "," tomografia computadorizada cardiovascular ");
		text = text.replace(" TSM "," tipo sang��neo da m�e ");
		text = text.replace(" VLP "," videolaparoscopia ");
		text = text.replace(" AIG "," adequado para a idade gestacional ");
		text = text.replace(" AINES "," antiinflamat�rios n�o-esteroidais ");
		text = text.replace(" CAPS "," Centro de Atendimento Psicoprofissionalizante ");
		text = text.replace(" CEC "," circula��o extracorp�rea ");
		text = text.replace(" CIPED "," Centro de Investiga��o de Doen�as Pedi�tricas ");
		text = text.replace(" CT "," centro de tratamento ");
		text = text.replace(" DG "," ramo diagonal ").replace(" Dg "," ramo diagonal ");//Adicionado 05/09/2012
		text = text.replace(" DPCD "," di�lise peritoneal continuada ");
		text = text.replace(" ESV "," extra-s�stoles ventriculares ");
		text = text.replace(" IMi "," infarto do mioc�rdio ");
		text = text.replace(" IVAS "," infec��es das vias a�reas superiores ");
		text = text.replace(" PEG "," pr�-ecl�mpsia grave ");
		text = text.replace(" R1 "," residente 1 ");
		text = text.replace(" RG "," regime geral ");
		text = text.replace(" RxT "," raio X de t�rax ");
		text = text.replace(" VD-AD "," ventr�culo direito - �trio direito ");
		text = text.replace(" VEF "," volume expirat�rio for�ado ");
		text = text.replace(" mL "," mililitro ");
		text = text.replace(" ABD "," detec��o autom�tica de fronteira ");
		text = text.replace(" ACTH "," horm�nio adrenocorticotr�fico ");
		text = text.replace(" AMO "," altera��o de medula �ssea ");
		text = text.replace(" BB "," beta-bloqueador ");
		text = text.replace(" BI "," duas ");
		text = text.replace(" BIA "," bal�o intra-a�rtico ");
		text = text.replace(" BX "," bi�psia ");
		text = text.replace(" C4 "," complemento 4 ");
		text = text.replace(" CEN "," cat�ter endonasal ");
		text = text.replace(" CKMB "," fra��o MB da creatinofosfoquinase ");
		text = text.replace(" DI "," dois ");
		text = text.replace(" DIP "," dipiridamol ");
		text = text.replace(" HP "," Helicobacter pylori ");
		text = text.replace(" LID "," lobo inferior direito ");
		text = text.replace(" MV "," murm�rio vesicular ");
		text = text.replace(" REED "," radiograma de es�fago, est�mago e duodeno ");
		text = text.replace(" RNI "," raz�o normalizada internacional ");
		text = text.replace(" STK "," streptoquinase ");
		text = text.replace(" T2 "," tempo 2 ");
		text = text.replace(" TPP "," trabalho de parto prematuro ");
		text = text.replace(" VPA "," valproato de s�dio ");
		text = text.replace(" mCi "," milicurie ");
		text = text.replace(" mg/dl "," miligrama/decilitro ");
		text = text.replace(" AIH "," Autoriza��o de Interna��o Hospitalar ");
		text = text.replace(" BEG "," bom estado geral ");
		text = text.replace(" CBZ "," carbamazepina ");
		text = text.replace(" CHILD "," Child ");
		text = text.replace(" CIA "," comunica��o interatrial ");
		text = text.replace(" D3 "," dia 3 ");
		text = text.replace(" DNA "," �cido desoxirribonucl�ico ");
		text = text.replace(" ENMG "," eletroneuromiografia ");
		text = text.replace(" ESQ "," esquerdo ");
		text = text.replace(" ESSV "," extra-s�stoles supraventriculares ");
		text = text.replace(" FBC "," freq��ncia do batimento ciliar ");
		text = text.replace(" FS "," freq��ncia sinusal ");
		text = text.replace(" HT "," hemat�crito ");
		text = text.replace(" ITUs "," infec��es do trato urin�rio ");
		text = text.replace(" ITr "," insufici�ncia tric�spide ");
		text = text.replace(" LT "," leucograma total ");
		text = text.replace(" PTU "," propiltiouracil ");
		text = text.replace(" QD "," quantidade di�ria ");
		text = text.replace(" QN "," quando necess�rio ");
		text = text.replace(" R2 "," residente 2 ");
		text = text.replace(" RS "," ritmo sinusal ");
		text = text.replace(" RXT "," raio X de t�rax ");
		text = text.replace(" SNC "," sistema nervoso central ");
		text = text.replace(" SO "," sala de observa��o ");
		text = text.replace(" SVE "," sobrecarga ventricular esquerda ");
		text = text.replace(" VD/AD "," ventr�culo direito / �trio direito ");
		text = text.replace(" VPP "," ventila��o com press�o positiva ");
		text = text.replace(" s/pp "," sist�lico / parede posterior ");
		text = text.replace(" AAA "," aneurisma da aorta abdominal ");
		text = text.replace(" AB "," AB ");
		text = text.replace(" AITs "," ataques isqu�micos transit�rios ");
		text = text.replace(" AMBU "," ambulat�rio ");
		text = text.replace(" BQLT "," bronquiolite ");
		text = text.replace(" CO2 "," di�xido de carbono ");
		text = text.replace(" CTC "," cortic�ide ");
		text = text.replace(" DPN "," dispn�ia parox�stica noturna ");
		text = text.replace(" DR "," doutor ");
		text = text.replace(" GH "," horm�nio do crescimento ");
		text = text.replace(" hdl "," lipoprote�nas de alta densidade ");
		text = text.replace(" ICE "," insufici�ncia congestiva esquerda ");
		text = text.replace(" IFI "," imunofluoresc�ncia indireta ");
		text = text.replace(" KG "," quilograma ");
		text = text.replace(" LBA "," lavado br�nquico alveolar ");
		text = text.replace(" LDL "," lipoprote�nas de baixa densidade ");
		text = text.replace(" M0 "," medula �ssea ");
		text = text.replace(" PAS "," press�o arterial sist�mica ");
		text = text.replace(" PFH "," prova de fun��o hep�tica ");
		text = text.replace(" PSP "," pun��o suprap�bica ");
		text = text.replace(" PT "," per�metro ");
		text = text.replace(" QI "," quociente de intelig�ncia ");
		text = text.replace(" RMN "," resson�ncia magn�tica ");
		text = text.replace(" SG "," assobrevida global ");
		text = text.replace(" SVs "," sinais vitais ");
		text = text.replace(" T1 "," tempo 1 ");
		text = text.replace(" i-ECA "," inibidores da enzima conversora de angiotensina ");
		text = text.replace(" iECA "," inibidores da enzima conversora de angiotensina ");
		text = text.replace(" mcg/d "," microgramas/decilitro ");
		text = text.replace(" ACM "," art�ria cerebral m�dia ");
		text = text.replace(" ACTPs "," angioplastias coronarianas transluminais percut�neas ");
		text = text.replace(" AESP "," atividade el�trica sem pulso ");
		text = text.replace(" AIRV "," altera��es inespec�ficas da repolariza��o ventricular ");
		text = text.replace(" ANTI "," anti ");
		text = text.replace(" AVCi "," acidente vascular cerebral isqu�mico ");
		text = text.replace(" AVEi "," acidente vascular encef�lico isqu�mico ");
		text = text.replace(" AZT "," zidovudina ");
		text = text.replace(" B6 "," B6 ");
		text = text.replace(" BQTE "," bronquite ");
		text = text.replace(" CCT "," carcinoma de c�lulas transicionais ");
		text = text.replace(" DM1 "," diabetes melitus tipo I ");
		text = text.replace(" EBV "," Epstein-Barr v�rus ");
		text = text.replace(" EGDA "," anastomose esofagogastroduodenal ");
		text = text.replace(" FAF "," arma de fogo ");
		text = text.replace(" HBV "," v�rus da hepatite B ");
		text = text.replace(" HCG "," horm�nio da gonadotrofina cori�nica ");
		text = text.replace(" HMV "," Hospital Moinhos de Vento ");
		text = text.replace(" HSL "," Hospital S�o Lucas ");
		text = text.replace(" IMC "," �ndice de massa corporal ");
		text = text.replace(" LH "," horm�nio luteinizante ");
		text = text.replace(" LIE "," lobo inferior esquerdo ");
		text = text.replace(" MAM "," art�ria mam�ria ");
		text = text.replace(" MED "," m�dio ");
		text = text.replace(" METs "," equivalentes metab�licos ");
		text = text.replace(" MGCX "," ramo marginal da art�ria circunflexa ");
		text = text.replace(" OAA "," obstru��o arterial aguda ");
		text = text.replace(" P/C "," polifenol/carboidrato ");
		text = text.replace(" PAAF "," pun��o aspirativa por agulha fina ");
		text = text.replace(" PCRs "," paradas cardio-respirat�rias ");
		text = text.replace(" PNA "," pielonefrite aguda ");
		text = text.replace(" R-x "," raio X ");
		text = text.replace(" SIADH "," s�ndrome de secre��o inapropriada de horm�nio anti-diur�tico ");
		text = text.replace(" SK "," estreptoquinase ");
		text = text.replace(" SOP "," s�ndrome do ov�rio polic�stico ");
		text = text.replace(" SUS "," Sistema �nico de Sa�de ");
		text = text.replace(" TGI "," trato gastrointestinal ");
		text = text.replace(" TMO "," transplante de medula �ssea ");
		text = text.replace(" TPO "," tireoperoxidase ");
		text = text.replace(" TVNS "," taquicardia ventricular n�o sustentada ");
		text = text.replace(" VD>AD "," ventr�culo direito > �trio direito ");
		text = text.replace(" cm2 "," cent�metro quadrado ");
		text = text.replace(" o2 "," oxig�nio ");
		text = text.replace(" ACV "," aparelho cardiovascular ");
		text = text.replace(" AMPI "," ampicilina ");
		text = text.replace(" BMO "," bi�psia de medula �ssea ");
		text = text.replace(" CIE "," car�tida interna esquerda ");
		text = text.replace(" CM "," cent�metro ");
		text = text.replace(" CPAPn "," press�o positiva cont�nua em vias a�reas nasal ");
		text = text.replace(" CR "," creatinina ");
		text = text.replace(" DMG "," diabetes melitus gestacional ");
		text = text.replace(" DMSA "," �cido dimercaptosucc�nico ");
		text = text.replace(" DRA "," doutora ");
		text = text.replace(" DV "," densitovolumetria ");
		text = text.replace(" DVP "," deriva��o ventr�culo peritoneal ");
		text = text.replace(" EMG "," emerg�ncia ");
		text = text.replace(" FEVE "," fra��o de eje��o do ventr�culo esquerdo ");
		text = text.replace(" FK "," trakolimus ");
		text = text.replace(" FiO2 "," fra��o de oxig�nio inspirado ");
		text = text.replace(" GA "," gasometria ");
		text = text.replace(" HCO3 "," bicarbonato ");
		text = text.replace(" HPIV "," hemorragia peri-intraventricular ");
		text = text.replace(" HS "," horas ");
		text = text.replace(" HTLV "," v�rus T-linfotr�pico humano ");
		text = text.replace(" ISQ "," isqu�mico ");
		text = text.replace(" KTTP "," tempo de tromboplastina parcialmente ativada ");
		text = text.replace(" LMA "," leucemia miel�ide aguda ");
		text = text.replace(" LOC "," l�cido, orientado e consciente ");
		text = text.replace(" LSE "," lobo superior esquerdo ");
		text = text.replace(" MA "," meningites ass�pticas ");
		text = text.replace(" MAC "," ambulat�rio de cardiologia ");
		text = text.replace(" MM-DA "," mam�ria - descendente anterior ");
		text = text.replace(" MMIIs "," membros inferiores ");
		text = text.replace(" MO "," medula �ssea ");
		text = text.replace(" MT "," metiltestosterona ");
		text = text.replace(" MTD "," dose m�xima tolerada ");
		text = text.replace(" NEO "," neonatal ");
		text = text.replace(" NEURO "," neurologia ");
		text = text.replace(" NPS "," nitroprussiato de s�dio ");
		text = text.replace(" ORL "," otorrinolaringologia ");
		text = text.replace(" PLAQ "," plaquetas ");
		text = text.replace(" PPNL "," propranolol ");
		text = text.replace(" PTH "," paratorm�nio ");
		text = text.replace(" PVC "," press�o venosa central ");
		text = text.replace(" PaO2 "," press�o arterial de oxig�nio ");
		text = text.replace(" RA "," r�mora atrial ");
		text = text.replace(" RDW "," amplitude de distribui��o eritrocit�ria ");
		text = text.replace(" RH "," rifampicina e isoniazida ");
		text = text.replace(" RxABD "," raio X abdominal ");
		text = text.replace(" SFA "," sofrimento fetal agudo ");
		text = text.replace(" SM "," silagem de milho ");
		text = text.replace(" SPA "," Servi�o de Pronto Atendimento ");
		text = text.replace(" SpO2 "," satura��o percut�nea de oxig�nio ");
		text = text.replace(" TAB "," transtorno afetivo bipolar ");
		text = text.replace(" TAP "," tempo de atividade de protrombina ");
		text = text.replace(" TARV "," terapia antiretroviral ");
		text = text.replace(" TG "," triglicer�deos ");
		text = text.replace(" TILT "," teste de inclina��o ortost�tica ");
		text = text.replace(" UCA "," cultura de urina ");
		text = text.replace(" UCI "," unidade de cuidados intermedi�rios ");
		text = text.replace(" URC "," urocultura ");
		text = text.replace(" US "," ultra-sonografia ");
		text = text.replace(" UTIN "," Unidade de Terapia Intensiva Neonatal ");
		text = text.replace(" VD28 "," ventr�culo direito 28 ");
		text = text.replace(" pH "," potencial hidrogeni�nico ");
		text = text.replace(" A2 "," amostra 2 ");
		text = text.replace(" ADC "," coeficiente de difus�o aparente ");
		text = text.replace(" AMgCx "," ramo marginal da art�ria circunflexa ");
		text = text.replace(" ANCA "," anticorpo anticitoplasma de neutr�filo ");
		text = text.replace(" ARV�s "," antiretrovirais ");
		text = text.replace(" BFM "," Berlin-Frankfurt-M�nster ");
		text = text.replace(" BQLTE "," bronquiolite ");
		text = text.replace(" CAPD "," di�lise peritoneal ambulatorial cr�nica ");
		text = text.replace(" CC "," cardiopatias cong�nitas ");
		text = text.replace(" CHC "," carcinoma hepatocelular ");
		text = text.replace(" CPT "," capacidade pulmonar total ");
		text = text.replace(" CREAT "," creatinina ");
		text = text.replace(" CTICC "," Centro de Tratamento Intensivo Cl�nico-cir�rgico ");
		text = text.replace(" DAOP "," doen�a arterial obstrutiva perif�rica ");
		text = text.replace(" DBPOC "," doen�a broncopulmonar obstrutiva cr�nica ");
		text = text.replace(" DC "," doen�a cel�aca ");
		text = text.replace(" DDI "," DDI ");
		text = text.replace(" DMO "," densidade mineral �ssea ");
		text = text.replace(" DRC "," doen�a renal cr�nica ");
		text = text.replace(" DX "," diagn�stico ");
		text = text.replace(" EF "," exame f�sico ");
		text = text.replace(" EFZ "," efavirenz ");
		text = text.replace(" ELLA "," endopr�tese arterial de perna esquerda ");
		text = text.replace(" EPF "," exame parasitol�gico de fezes ");
		text = text.replace(" EPO "," eritropoetina ");
		text = text.replace(" EQ "," esquema quimioter�pico ");
		text = text.replace(" ESBL "," produtoras de beta-lactamase com espectro estendido ");
		text = text.replace(" FOP "," forame oval patente ");
		text = text.replace(" FSH "," horm�nio fol�culo estimulante ");
		text = text.replace(" G3 "," gesta��o 3 ");
		text = text.replace(" HELLP "," anemia hemol�tica, n�veis elevados de enzimas hep�ticas e contagem baixa de plaquetas ");
		text = text.replace(" HMP "," hist�ria m�dica pregressa ");
		text = text.replace(" IAO "," insufici�ncia a�rtica ");
		text = text.replace(" ICP "," interven��o coron�ria percut�nea ");
		text = text.replace(" IGP "," idade gestacional no parto ");
		text = text.replace(" IN "," intranasais ");
		text = text.replace(" JUP "," jun��o uretero-pi�lica ");
		text = text.replace(" L1 "," lombar 1 ");
		text = text.replace(" LM "," lobo m�dio ");
		text = text.replace(" LV "," leite de vaca ");
		text = text.replace(" MI "," membro inferior ");
		text = text.replace(" MIBI "," metoxi-isobutil-isonitrila ");
		text = text.replace(" MI�s "," membros inferiores ");
		text = text.replace(" MRSA "," Staphylococcus aureus resistente � meticilina ");
		text = text.replace(" MTZ "," mirtazapina ");
		text = text.replace(" MsIS "," membros inferiores ");
		text = text.replace(" NC "," nervo craniano ");
		text = text.replace(" OBS "," observa��o ");
		text = text.replace(" OD "," olho direito ");
		text = text.replace(" OE "," olho esquerdo ");
		text = text.replace(" PCO2 "," press�o de di�xido de carbono ");
		text = text.replace(" PCP "," press�o capilar pulmonar ");
		text = text.replace(" PCT "," paciente ");
		text = text.replace(" PMAP "," press�o m�dia da art�ria pulmonar ");
		text = text.replace(" PNE "," portador de necessidades especiais ");
		text = text.replace(" PNTx "," pneumot�rax ");
		text = text.replace(" POA "," Porto Alegre ");
		text = text.replace(" PPD "," derivado prot�ico purificado ");
		text = text.replace(" PV "," parto vaginal ");
		text = text.replace(" QID "," quadrante inferior direito ");
		text = text.replace(" R-X "," raio X ");
		text = text.replace(" RBV "," ribavirina ");
		text = text.replace(" RC "," risco cardiovascular ");
		text = text.replace(" RCP "," reanima��o cardiopulmonar ");
		text = text.replace(" RD "," retinopatia diab�tica ");
		text = text.replace(" REAG "," reagente ");
		text = text.replace(" RGE "," refluxo gastresof�gico ");
		text = text.replace(" RHA "," ru�dos hidroa�reos ");
		text = text.replace(" RN2 "," rec�m-nato 2 ");
		text = text.replace(" RPD "," retinopatia diab�tica proliferativa ");
		text = text.replace(" RXTX "," raio X de t�rax ");
		text = text.replace(" S/N "," se necess�rio ");
		text = text.replace(" SMC "," servi�o m�dico de cirurgia ");
		text = text.replace(" SMO "," servi�o m�dico de oncologia ");
		text = text.replace(" SR "," senhor ");
		text = text.replace(" SVA "," sonda uretral pl�stica ");
		text = text.replace(" TCEC "," tempo de circula��o extracorp�rea ");
		text = text.replace(" TIFF "," Tiffeneau ");
		text = text.replace(" TOT "," tubo orotraqueal ");
		text = text.replace(" TSRN "," tipo sang��neo do rec�m-nato ");
		text = text.replace(" UBS "," Unidade B�sica de Sa�de ");
		text = text.replace(" VA "," vias a�reas ");
		text = text.replace(" VAD "," vincristina, adriblastina e dexametasona ");
		text = text.replace(" VB "," ves�cula biliar ");
		text = text.replace(" VCR "," vincristina ");
		text = text.replace(" VED "," di�metro diast�lico ");
		text = text.replace(" VEDF "," ventr�culo esquerdo di�stole final ");
		text = text.replace(" VES "," di�metro sist�lico ");
		text = text.replace(" VESF "," ventr�culo esquerdo s�stole final ");
		text = text.replace(" VSVE "," via de sa�da do ventr�culo esquerdo ");
		text = text.replace(" h/h "," de hora em hora ");
		text = text.replace(" A2RV "," altera��o de repolariza��o ventricular ");
		text = text.replace(" AA2 "," amino�cidos ");
		text = text.replace(" ACE "," art�ria coron�ria esquerda ");
		text = text.replace(" ADS "," amniocentese descompressiva seriada ");
		text = text.replace(" AI "," angina inst�vel ");
		text = text.replace(" AINEs "," antiinflamat�rios n�o-esteroidais ");
		text = text.replace(" ANGIO "," angiografia ");
		text = text.replace(" ARA "," antagonistas dos receptores da angiotensina ");
		text = text.replace(" ART "," art�ria ");
		text = text.replace(" AVEs "," acidente vascular encef�lico ");
		text = text.replace(" AVF "," ante-verso-flex�o ");
		text = text.replace(" B1 "," B1 ");
		text = text.replace(" B3 "," terceira bulha ");
		text = text.replace(" B4 "," quarta bulha ");
		text = text.replace(" BBloq "," beta-bloqueadores ");
		text = text.replace(" BC "," bloco cir�rgico ");
		text = text.replace(" BCG "," bacilo de Calmette-Gu�rin ");
		text = text.replace(" BCPs "," broncopneumonias ");
		text = text.replace(" BDAS "," bloqueios divisionais �ntero-superiores ");
		text = text.replace(" BIPAP "," press�o positiva em vias a�reas com dois n�veis ");
		text = text.replace(" BNF "," bulhas normofon�ticos ");
		text = text.replace(" BT:41 "," bilirrubina total ");
		text = text.replace(" BZD "," benzodiazep�nicos ");
		text = text.replace(" BiPAP "," press�o positiva em vias a�reas com dois n�veis ");
		text = text.replace(" C1 "," cesariana 1 ");
		text = text.replace(" CAt "," avalia��o cr�tica t�pica ");
		text = text.replace(" CD34 "," grupo de diferencia��o 34 ");
		text = text.replace(" CHAd "," concentrado de hem�cias adulto ");
		text = text.replace(" CIC "," cirurgia card�aca ");
		text = text.replace(" CID "," Classifica��o Internacional de Doen�as ");
		text = text.replace(" CIP "," carcinoma incidental da pr�stata ");
		text = text.replace(" CTi "," centro de terapia intensiva ");
		text = text.replace(" CVE "," cardiovers�o el�trica ");
		text = text.replace(" CVM "," contra��o volunt�ria m�xima ");
		text = text.replace(" Ca2 "," c�lcio ");
		text = text.replace(" CaT "," avalia��o cr�tica t�pica ");
		text = text.replace(" D14 "," dia 14 ");
		text = text.replace(" D4 "," dia 4 ");
		text = text.replace(" DA/Dg "," descendente anterior / primeira diagonal ");
		text = text.replace(" DBP "," di�metro biparietal ");
		text = text.replace(" DHEA "," deidroepiandrosterona ");
		text = text.replace(" DIU "," dispositivo intra-uterino ");
		text = text.replace(" DM-2 "," diabete melitus tipo 2 ");
		text = text.replace(" DMII "," diabete melitus tipo 2 ");
		text = text.replace(" DP-CD "," diagonal posterior - coron�ria direita ");
		text = text.replace(" DPP "," descolamento prematuro de placenta ");
		text = text.replace(" DPT "," espessamento peritoneal difuso ");
		text = text.replace(" DTS "," dose total semanal ");
		text = text.replace(" DVR "," dist�rbio ventilat�rio restritivo ");
		text = text.replace(" Dg-DA "," primeira diagonal / descendente anterior ");
		text = text.replace(" Dg1 "," primeira diagonal 1 ");
		text = text.replace(" Dm2 "," diabete melitus tipo 2 ");
		text = text.replace(" E-D "," esquerda-direita ");
		text = text.replace(" ECG�s "," ecografias ");
		text = text.replace(" ECT "," eletroconvulsoterapia ");
		text = text.replace(" EDSS "," escala ampliada do estado de incapacidade ");
		text = text.replace(" EFV "," efavirenz ");
		text = text.replace(" EME "," emerg�ncia ");
		text = text.replace(" EMEP "," emerg�ncia pedi�trica ");
		text = text.replace(" EP "," epit�lio pigmentado ");
		text = text.replace(" FCm�x "," fun��o card�aca m�xima ");
		text = text.replace(" FEJ "," fra��o de eje��o ");
		text = text.replace(" FEM "," feminino ");
		text = text.replace(" FEV "," fevereiro ");
		text = text.replace(" FEj "," fra��o de eje��o ");
		text = text.replace(" FH "," forma��o do hipocampo ");
		text = text.replace(" FID "," fossa il�aca direita ");
		text = text.replace(" FSV "," fundo de saco vaginal ");
		text = text.replace(" G4 "," gesta��o 4 ");
		text = text.replace(" G6PD "," glicose-6-fosfato dehidrogenase ");
		text = text.replace(" GI "," gastro intestinal ");
		text = text.replace(" GNMP "," glomerulonefrite membrano-proliferativa ");
		text = text.replace(" GRAFT "," Graft ");
		text = text.replace(" GRAM "," gram ");
		text = text.replace(" GT "," glutariltransferase ");
		text = text.replace(" GTS "," gotas ");
		text = text.replace(" HAS,e "," hipertens�o arterial sist�mica ");
		text = text.replace(" HBAE "," hemibloqueio anterior esquerdo ");
		text = text.replace(" HBP "," hiperplasia benigna da pr�stata ");
		text = text.replace(" HBs "," v�rus da hepatite B ");
		text = text.replace(" HCSA "," Hospital da Crian�a Santo Ant�nio ");
		text = text.replace(" HIC "," hemorragia intracraniana ");
		text = text.replace(" HM "," hipertermia maligna ");
		text = text.replace(" HMCs "," hemoculturas ");
		text = text.replace(" HNF "," heparina n�o-fracionada ");
		text = text.replace(" HTC "," hemat�crito ");
		text = text.replace(" HX "," hist�rico ");
		text = text.replace(" Ht/Hb "," hemat�crito/hemoglobina ");
		text = text.replace(" IA "," infarto agudo ");
		text = text.replace(" IAM's "," infartos agudos do mioc�rdio ");
		text = text.replace(" IF "," forma indeterminada ");
		text = text.replace(" IFN "," interferon alfa-recombinante ");
		text = text.replace(" IMT "," calibre intermedi�rio da car�tida ");
		text = text.replace(" IOT "," intuba��o orotraqueal ");
		text = text.replace(" IPC "," �ndice de potencial de contamina��o ");
		text = text.replace(" ISHAK "," Ishak ");
		text = text.replace(" ISRS "," inibidor seletivo da recapta��o de serotonina ");
		text = text.replace(" ITC "," insufici�ncia tric�spide ");
		text = text.replace(" IVa "," veia interventricular anterior ");
		text = text.replace(" IX "," 9 ");
		text = text.replace(" K2 "," pot�ssio ");
		text = text.replace(" L2-L3 "," lombar 2 - lombar 3 ");
		text = text.replace(" L3 "," lombar 3 ");
		text = text.replace(" L4 "," lombar 4 ");
		text = text.replace(" L4-L5 "," lombar 4 - lombar 5 ");
		text = text.replace(" L5-S1 "," lombar 5 - sacro 1 ");
		text = text.replace(" LE "," laparotomia exploradora ");
		text = text.replace(" LFN "," linfonodos ");
		text = text.replace(" LI "," liquido intersticial ");
		text = text.replace(" LLA "," leucemia linfoc�tica aguda ");
		text = text.replace(" LLC "," leucemia linfoc�tica cr�nica ");
		text = text.replace(" LMC "," leucemia miel�ide cr�nica ");
		text = text.replace(" LN "," linfonodo ");
		text = text.replace(" LNH "," linfoma n�o-Hodgkin ");
		text = text.replace(" LQR "," l�quor ");
		text = text.replace(" LUTS "," sintomas do trato urin�rio inferior ");
		text = text.replace(" MELD "," modelo para doen�a hep�tica terminal ");
		text = text.replace(" MF "," microorganismos filamentosos ");
		text = text.replace(" MGCx "," ramo marginal da art�ria circunflexa ");
		text = text.replace(" MGLIS "," art�ria coron�ria marginal localizada intrastent ");
		text = text.replace(" MMG "," mamografia ");
		text = text.replace(" MMSS "," membros superiores ");
		text = text.replace(" MSS "," membros superiores ");
		text = text.replace(" MSSA "," Staphylococcus aureus sens�vel � meticilin ");
		text = text.replace(" MSs "," membros superiores ");
		text = text.replace(" Mg1 "," primeira marginal ");
		text = text.replace(" MsSs "," membros superiores ");
		text = text.replace(" NAC "," neuropatia auton�mica cardiovascular ");
		text = text.replace(" NAN "," leite NAN ");
		text = text.replace(" ND "," nefropatia diab�tica ");
		text = text.replace(" NOV "," novembro ");
		text = text.replace(" NYHA "," Associa��o do Cora��o de Nova York ");
		text = text.replace(" Na3 "," pot�ssio ");
		text = text.replace(" OEA "," otoemisiones ac�sticas ");
		text = text.replace(" OFT "," oftalmologia ");
		text = text.replace(" OUT "," outubro ");
		text = text.replace(" P4 "," parto 4 ");
		text = text.replace(" PAAf "," pun��o aspirativa por agulha fina ");
		text = text.replace(" PAC "," pneumonia adquirida na comunidade ");
		text = text.replace(" PASP "," press�o sist�lica da art�ria pulmonar ");
		text = text.replace(" PAVM "," pneumonia associada � ventila��o mec�nica ");
		text = text.replace(" PFE "," peso fetal estimado ");
		text = text.replace(" PICC "," cat�ter central de inser��o perif�rica ");
		text = text.replace(" PNI "," psiconeuroimunologia ");
		text = text.replace(" PNM "," pneumonia ");
		text = text.replace(" PNP "," polineuropatia ");
		text = text.replace(" PO2 "," press�o parcial do oxig�nio ");
		text = text.replace(" POP "," procedimento operacional padr�o ");
		text = text.replace(" PS-MG "," Pronto Socorro de Minas Gerais ");
		text = text.replace(" PSG "," polissonografia ");
		text = text.replace(" PUCRS "," Pontif�cia Universidade Cat�lica do Rio Grande do Sul ");
		text = text.replace(" PVM "," prolapso da valva mitral ");
		text = text.replace(" R3 "," residente 3 ");
		text = text.replace(" RCR "," ressuscita��o cardio-respirat�ria ");
		text = text.replace(" RCT "," rastreamento corporal total com radioiodo ");
		text = text.replace(" RDNPM "," retardo do desenvolvimento neuropsicomotor ");
		text = text.replace(" RDP "," retinopatia diab�tica proliferativa ");
		text = text.replace(" RE "," ret�culo esquerdo ");
		text = text.replace(" RHJ "," refluxo hepato-jugular ");
		text = text.replace(" RR "," risco relativo ");
		text = text.replace(" RT "," radioterapia ");
		text = text.replace(" RTX "," neurotoxina resiniferatoxina ");
		text = text.replace(" RUB "," rub�ola ");
		text = text.replace(" S1 "," sacro 1 ");
		text = text.replace(" SAD "," sobrecarga atrial direita ");
		text = text.replace(" SAE "," sobrecarga atrial esquerda ");
		text = text.replace(" SCD "," seio coron�rio distal ");
		text = text.replace(" SMD "," s�ndrome mielodispl�sica ");
		text = text.replace(" SOG "," sonda orog�strica ");
		text = text.replace(" SP "," sala de politraumatizados ");
		text = text.replace(" SPECT "," spect card�aco ");
		text = text.replace(" SULFA "," sulfametoxazol ");
		text = text.replace(" T12 "," tor�cica 12 ");
		text = text.replace(" T4L "," tetraiodotironina ");
		text = text.replace(" T:0,7 "," troponina 0 ");
		text = text.replace(" T:2,1 "," troponina 2 ");
		text = text.replace(" THB "," transtorno de humor bipolar ");
		text = text.replace(" TIG5 "," taxa de infus�o de glicose ");
		text = text.replace(" TISQ "," tempo de isquemia ");
		text = text.replace(" TJV "," transfus�o intravascular ");
		text = text.replace(" TMP "," trimetoprim ");
		text = text.replace(" TRAM "," retalho miocut�neo transverso abdominal ");
		text = text.replace(" TSC "," tiragem subcostal ");
		text = text.replace(" TSE "," spin-eco turbo ");
		text = text.replace(" TSH:5 "," horm�nio t�reo-estimulante 5 ");
		text = text.replace(" TTG "," teste de toler�ncia a glicose ");
		text = text.replace(" TU "," trato urin�rio ");
		text = text.replace(" TVS "," taquicardias ventriculares monom�rficas sustentadas ");
		text = text.replace(" TnT "," troponina ");
		text = text.replace(" TxH "," transplante hep�tico ");
		text = text.replace(" UCC "," Unidade de Cardiopatias Cong�nitas ");
		text = text.replace(" UFC "," unidades formadoras de col�nia ");
		text = text.replace(" UR "," ur�ia ");
		text = text.replace(" URO "," urocultura ");
		text = text.replace(" VA-AD "," valvula anterior do �trio direito ");
		text = text.replace(" VANCO "," vancomicina ");
		text = text.replace(" VAS "," vias a�reas superiores ");
		text = text.replace(" VD17 "," ventr�culo direito 17 ");
		text = text.replace(" VEd "," di�stole do ventr�culo esquerdo ");
		text = text.replace(" VEs "," s�stole do ventr�culo esquerdo ");
		text = text.replace(" VMA "," �cido vanilmand�lico ");
		text = text.replace(" VR "," via retal ");
		text = text.replace(" Vo2 "," volume de oxig�nio ");
		text = text.replace(" XII "," 12 ");
		text = text.replace(" b2 "," beta 2 ");
		text = text.replace(" g/dia "," grama/dia ");
		text = text.replace(" mVE "," massa ventricular esquerda ");
		text = text.replace(" mnmHg "," mil�metros de merc�rio ");
		text = text.replace(" pCO2 "," press�o de di�xido de carbono ");
		text = text.replace(" r-x "," raio X ");
		text = text.replace(" s/n "," se necess�rio ");
		text = text.replace(" satO2 "," satura��o de oxig�nio ");
		text = text.replace(" vO "," via oral ");
		
		return text;
		
	}
	
	
	
	
	
	/**
	 * ANTIGOS M�TODOS DA CLASSE RULES
	 */
	
	
	
	
	
	
	
	/**
	 * Obt�m quantidade de palavras de uma regra
	 * @param arr
	 * @return
	 */
	//public static int getRuleSize(String[] arr){
	public int getRuleSize(Regra rule){
		return rule.termos.size();
	}
	
	/**
	 * Obt�m conjunto de palavras de acordo com a quantidade e posi��o definidas
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
	 * Obt�m conjunto de regras de acordo com a quantidade e posi��o definidas
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
		//Ex: Isso � uma frase - [V_INF][N_M_S][PREP][N_F_P]
		
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
				//TODO: Retirar underlines na obten��o do POS-tagging
				if(!tags[i].equalsIgnoreCase(termo + "_"))
				{
					return false;
				}
			}
		
		}

		return true;
		
	}
	
	/**
	 * Verifica � regra de STEMMING
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
	 * Verifica � regra de POS-TAG
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
	 * Obt�m vers�o para impress�o dos conte�dos de um array
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
	
	
	//TODO: Ajustar fun��o para popular lista de regras - List<Regra>
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
						
						//Se � regra de STEMMING n�o coloca UNDERLINE
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
