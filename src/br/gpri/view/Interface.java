package br.gpri.view;

import java.io.File;
import java.util.ArrayList;


import javax.swing.JFrame;

import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;

import nlp.TaggerStemSub;
import activerecord.BD;

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
	public static BD BD;
	//DropDownListBox
	public static DropDownInfo list1;
	public static DropDownInfo list2;
	
	
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
				System.out.println("Arquivo não encontrado");}
		
		catch(Exception e){
			e.printStackTrace();}
	}
	
	protected static void Janela(){
		caminho = new String();
		linha = new Integer(0);
		Tagger = new TaggerStemSub();
		BD = new BD();
		termosregras = new ArrayList<String>();
		list1 = new DropDownInfo("elementos");
		list2 = new DropDownInfo("termosregras");
		window = new Janela();
		windowinsert = new JanelaInsert();
		window.abrejanela();
	}
	
	//Faz os inserts usando o método criado dentro da classe Regra
	protected static void Inserts(){
		System.out.println("Erro Insert Regra: " + BD.insertBD(regras));
		for (int i=0; i < termosregras.size(); i++)
			System.out.println("Erro Insert TermosRegras["+ i + "]: " + BD.insertBD(termosregras.get(i)));}
	
		
}


