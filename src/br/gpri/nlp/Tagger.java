package br.gpri.nlp;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import activerecord.Acronimo;
import activerecord.BD;
import activerecord.Regra;
import activerecord.Subregra;
import activerecord.Termo;
import activerecord.TrechoEncontrado;
import br.usp.pcs.lta.cogroo.entity.Token;
import br.usp.pcs.lta.cogroo.entity.impl.runtime.SentenceCogroo;
import br.usp.pcs.lta.cogroo.util.viewer.CogrooWrapper;

public class Tagger{
	
	public CogrooWrapper cogroo;
	public final int conjuntoTeste = 1;
	
	private List<Acronimo> acronimos;
	
	public Tagger(BD BD){
		cogroo = new CogrooWrapper();
		acronimos = BD.selectAcronimos();
	}
	
	private String preProccessText(String text){
		//Adiciona espaço para acronimos no começo/final frase
		text = espacaTexto(text);
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
		//Remove espaco ponto
		text = removeEspacaPontuacao(text);
		//Retira espaco comeco/final frase
		text= removeEspacaTexto(text);
		
		return text;
	}
	
	private List<Token> processCogroo(String sentenca){
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
		return tokens;
	}
	
	//Tagger para Interface gráfica
	public Regra geraRegra(String text_sumario, String text_selecionado,int idElemento, int idRegra){
		
		List <Termo> termosregras = new ArrayList<Termo>();
		
		//Executa operações de PRÉ-PROCESSAMENTO
		text_sumario = preProccessText(text_sumario);
		text_selecionado = preProccessText(text_selecionado);
		
		//Separa texto selecionado em palavras
		SentenceCogroo selecionado = new SentenceCogroo(text_selecionado);
		cogroo.tokenizer(selecionado);
		cogroo.nameFinder(selecionado);
		cogroo.preTagger(selecionado);
		List<Token> text_separado = selecionado.getTokens();
						
		String previa = "";
		
		//Separa texto em sentenças
		String[] sentencas = cogroo.sentDetect(text_sumario);
		for (String sentenca : sentencas) {
			
			List<Token> tokens = processCogroo(sentenca);

			//Procura onde estão os termos selecionados
			//Compara um termo com o primeiro do vetor separado, caso encontre, ve se os termos
			//seguintes também são os esperados
			boolean igual = false;
			
			for(int i=0; i < tokens.size(); i++){
				for(int j=0; j < text_separado.size(); j++){
					try{
					if(text_separado.get(j).getLexeme().equals(tokens.get(i+j).getLexeme()))
						igual = true;
					else{
						igual = false;
						break;
					}
					}
					catch(IndexOutOfBoundsException e){
						System.out.println("Erro por texto selecionado estar em duas frases diferentes");
						e.printStackTrace();
						igual = false;
					
					}
				}
				
				//Cria a string a ser retornada,
				//a partir do indice do primeiro termo esperado encontrado. 
				if(igual){
					for(int k=0; k < text_separado.size(); k++){
						Token token = tokens.get(k+i);
						if(token.getLexeme().toString().equals("."));
						else{
							previa += "[" + token.getMorphologicalTag() + "]";
							Termo t = new Termo();
							t.setIdRegra(idRegra);
							t.setIdTermo(k);
							t.setTermo(token.getMorphologicalTag().toString());
							t.setTexto(token.getLexeme().toString());
							termosregras.add(t);
						}
					}
				}
			}
		}
		
		Regra r = new Regra();
		r.setId(idRegra);
		r.setPrevia(previa);
		r.setTexto(text_selecionado);
		r.setConjunto(conjuntoTeste);
		r.setElemento(idElemento);
		r.setTermos(termosregras);
		
		return r; 
	}
	
	public Subregra geraSubRegra(String text_sumario, String text_selecionado,int idRegra, int idSubRegra){
		
		List <Termo> termosregras = new ArrayList<Termo>();
		
		//Executa operações de PRÉ-PROCESSAMENTO
		text_sumario = preProccessText(text_sumario);
		text_selecionado = preProccessText(text_selecionado);
		
		//Separa texto selecionado em palavras
		SentenceCogroo selecionado = new SentenceCogroo(text_selecionado);
		cogroo.tokenizer(selecionado);
		cogroo.nameFinder(selecionado);
		cogroo.preTagger(selecionado);
		List<Token> text_separado = selecionado.getTokens();
						
		String previa = "";
		
		//Separa texto em sentenças
		String[] sentencas = cogroo.sentDetect(text_sumario);
		for (String sentenca : sentencas) {
			List<Token> tokens = processCogroo(sentenca);
			
			//Procura onde estão os termos selecionados
			//Compara um termo com o primeiro do vetor separado, caso encontre, ve se os termos
			//seguintes também são os esperados
			boolean igual = false;
			
			for(int i=0; i < tokens.size(); i++){
				for(int j=0; j < text_separado.size(); j++){
					if(text_separado.get(j).getLexeme().equals(tokens.get(i+j).getLexeme()))
						igual = true;
					else{
						igual = false;
						break;
					}
				}
				
				//Cria a string a ser retornada,
				//a partir do indice do primeiro termo esperado encontrado. 
				if(igual){
					for(int k=0; k < text_separado.size(); k++){
						Token token = tokens.get(k+i);
						if(token.getLexeme().toString().equals("."));
						else{
							previa += "[" + token.getMorphologicalTag() + "]";
							Termo t = new Termo();
							t.setIdRegra(idRegra);
							t.setIdSubregra(idSubRegra);
							t.setIdTermo(k);
							t.setTermo(token.getMorphologicalTag().toString());
							t.setTexto(token.getLexeme().toString());
							termosregras.add(t);
						}
					}
				}
			}
		}
		
		Subregra s = new Subregra();
		s.setId(idSubRegra);
		s.setIdRegra(idRegra);
		s.setPrevia(previa);
		s.setTexto(text_selecionado);
		s.setTermos(termosregras);
		
		return s; 
	}
	
	public List<TrechoEncontrado> executaRegra(String texto_sumario, List<Regra> regras){
		
		List<TrechoEncontrado> encontrados = new ArrayList<TrechoEncontrado>();
		
		//Executa operações de PRÉ-PROCESSAMENTO
		String text_sumario = preProccessText(texto_sumario);
							
		//Separa texto em sentenças
		String[] sentencas = cogroo.sentDetect(text_sumario);
		for (String sentenca : sentencas) {
			List<Token> tokens = processCogroo(sentenca);
			
			//Executa cada uma das regras
			for (Regra r : regras){
				
				boolean igual = false;
				
				
				//Procura por incidencia da regra
				for(int i=0; i < tokens.size(); i++){
					String trecho = "";
					for(int j=0; j < r.getNumTermos(); j++){
						String c;
						try{
							c = tokens.get(i+j).getMorphologicalTag().toString();
						}
						catch(IndexOutOfBoundsException e){
							c = "";
						}
						String d = r.getTermo(j).getTermo();
						if(c.contentEquals(d)){
							igual = true;
							trecho += tokens.get(i+j).getLexeme() + " ";
						}
						else{
							igual = false;
							break;
						}
					}
						//
						//Se toda a comparação for igual, se tiver subregras testa
						//Se tudo der certo, adiona na lista de encontrados e limpa a string trecho
						if(igual){
//							boolean testeSubregra = true;
							boolean encontrou_algum_sr = true;
							List<TrechoEncontrado> subregrasEncontrados = new ArrayList<TrechoEncontrado>();
							if(r.hasSubregra())
							{
								List<Subregra> subregras = r.getSubregras();
								executaSubRegra(subregras, tokens, trecho, subregrasEncontrados);
							}
//							if(testeSubregra){ trocado pela linha de baixo para manter os loops de regra e subregra parecidos
							if(encontrou_algum_sr){
								TrechoEncontrado t = new TrechoEncontrado();
								t.setRegra(r);
								t.setIsSubregra(false);
								t.setHasRegra(true);
								t.setTrechoEncontrado(trecho);
								trecho="";
								encontrados.add(t);
								igual = false;
								
								//Adiciona trechos das subregras na lista
								if(!subregrasEncontrados.isEmpty())
									for(int k=0; k<subregrasEncontrados.size(); k++)
										encontrados.add(subregrasEncontrados.get(k));
							}
							
					}
				}		
				
			}
		}
		return encontrados;	
	}
	
	private boolean executaSubRegra(List<Subregra> subregras, List<Token> tokens, String trecho, List<TrechoEncontrado> subregraEncontrados){
		//inicio alteração Fernando.
		//	testeSubregra = executaSubRegra(r.getSubregras()); //Testa as subregras, retorna true se ela validarem a regra
		boolean igual_sr = true;
		
		//Executa cada uma das subregras
		boolean encontrou_algum_sr = false;
		for (Subregra sr : subregras){
			
			igual_sr = false;
			
			
			//Procura por incidencia da subregra
			String trecho_sr = "";
			for(int i_sr=0; i_sr < tokens.size(); i_sr++){
				for(int j_sr=0; j_sr < sr.getNumTermos(); j_sr++){
					String c;
					try{
						c = tokens.get(i_sr+j_sr).getMorphologicalTag().toString();
					}
					catch(IndexOutOfBoundsException e){
						c = "";
					}
					String d = sr.getTermo(j_sr).getTermo();                
					if(c.contentEquals(d) && !trecho.contains((tokens.get(i_sr+j_sr).getLexeme()))){
						 
						igual_sr = true;
						trecho_sr += tokens.get(i_sr+j_sr).getLexeme() + " ";
					}
					else{
						igual_sr = false;
						trecho_sr="";
						break;
					}
				}
					//
				if(igual_sr){
					TrechoEncontrado t = new TrechoEncontrado();
					t.setSubregra(sr);
					t.setIsSubregra(true);
					t.setTrechoEncontrado(trecho_sr);
					trecho_sr = "";
					subregraEncontrados.add(t);
					encontrou_algum_sr=true;
					
						
			}
			}

		}
		return encontrou_algum_sr;
	}

	
	
	private String retiraErrosRecorrentes(String text){
		
		text = text.replaceFirst("#", "Paciente");

		return text;

		
	}
	
	private String retiraPontuacao(String text){
		
		return text.replaceAll("[-!?><=%;/#,@*]", " ");
		
	}
	
	private String espacaPontuacao(String text){
		
		return text.replace(".", " .");
	}
	
	private String removeEspacaPontuacao(String text){
		
		return text.replace(" .", ".");
	}
	
	private String espacaTexto(String text){
		
		return (" "+text+" ");
	}
	
	private String removeEspacaTexto(String text){
		
		return text.substring(1, (text.length()-1));
	}
	
	private String expandirData(String text){
		
		
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
	
	private String retiraStopWords(String text){
		
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
	
	private String expandirAcronimos(String text){
		
		 for(int i=0; i<acronimos.size(); i++){
			 Acronimo a = acronimos.get(i);
			 text = text.replace(a.getAcronimo() , a.getExpansao());
		 }
				
		return text;
		
	}
	
}