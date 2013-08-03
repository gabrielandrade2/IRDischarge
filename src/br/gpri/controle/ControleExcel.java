package br.gpri.controle;

import java.io.File;

import jxl.*;


public class ControleExcel {

	private Workbook Arquivo;
	private Sheet Planilha;
	
	public ControleExcel(){
		Arquivo = null;
		Planilha = null;
	}
	
	public void abreExcel(String caminho){
		try{
			Arquivo = Workbook.getWorkbook(new File(caminho));
			Planilha = Arquivo.getSheet(0);
		}
		
		catch(java.io.FileNotFoundException e){
				System.out.println("Arquivo não encontrado");}
		
		catch(Exception e){
			e.printStackTrace();}
	}
	
	public Cell getCelula(Integer linha){
		return (Planilha.getCell(0, linha));
	}
	
	public String getConteudoCelula(Integer linha){
		return (Planilha.getCell(0, linha).getContents());
	}
	
	public int getNumLinhas(){
		return (Planilha.getRows());
	}
}
