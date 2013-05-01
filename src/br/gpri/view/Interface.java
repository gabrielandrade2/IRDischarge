package br.gpri.view;

import java.io.File;
import java.util.ArrayList;


import javax.swing.JFrame;

import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;

import nlp.TaggerStemSub;
import activerecord.Regra;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;


public class Interface extends JFrame {

	public static Janela window;
	public static JanelaInsert windowinsert;
	protected static String caminho;
	protected static Cell celula;
	protected static Integer linha;
	protected static Workbook Excel;
	protected static Sheet Planilha;
	protected static TaggerStemSub Tagger;
	//Inserts
	public static ArrayList <String> termosregras;
	public static String regras;
	public static Regra Regra;
	
	
	public static void ExecutaInterface() {
		Janela();
					
	}

	//Abre o arquivo xls e pega a primeira celula da coluna A
	protected static void Excel(){
		try{
			Excel = Workbook.getWorkbook(new File(caminho));
			Planilha = Excel.getSheet(0);
			celula = Planilha.getCell(0, 0);}
		
		catch(java.io.FileNotFoundException e){
				System.out.println("Arquivo n�o encontrado");}
		
		catch(Exception e){
			e.printStackTrace();}
	}
	
	protected static void Janela(){
		caminho = new String();
		linha = new Integer(0);
		Tagger = new TaggerStemSub();
		Regra = new Regra();
		termosregras = new ArrayList<String>();
		window = new Janela();
		windowinsert = new JanelaInsert();
		window.abrejanela();
	}
	
	//Faz os inserts usando o m�todo criado dentro da classe Regra
	protected static void Inserts(){
		System.out.println("Erro Insert Regra: " + Regra.InsertElement(regras));
		for (int i=0; i < termosregras.size(); i++)
			System.out.println("Erro Insert TermosRegras["+ i + "]: " + Regra.InsertElement(termosregras.get(i)));}
		
}