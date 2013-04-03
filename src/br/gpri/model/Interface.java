package br.gpri.model;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFrame;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import br.gpri.view.Janela;

public class Interface extends JFrame {

	public static String caminho;
	public static Cell celula;
	public static Integer linha;
	public static Workbook Excel;
	public static Sheet Planilha;
	
	public static void ExecutaInterface() {
		Janela();
					
	}

	public static void Excel(){
		try{
			Excel = Workbook.getWorkbook(new File(caminho));
			Planilha = Excel.getSheet(0);
			celula = Planilha.getCell(0, 0);
		
		}
		catch(java.io.FileNotFoundException e){
				System.out.println("Arquivo não encontrado");}
		
		catch(Exception e){
			e.printStackTrace();}
	}
	
	public static void Janela(){
		caminho = new String();
		linha = new Integer(0);
		Janela window = new Janela();
		window.abrejanela();
	}
	
}
