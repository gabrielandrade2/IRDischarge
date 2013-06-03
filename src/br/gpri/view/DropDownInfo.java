package br.gpri.view;

import java.util.ArrayList;
import java.util.List;

import activerecord.BD;

public class DropDownInfo {
	
	private List <Integer> id;
	private List <String> texto;
	private List <String> descricao;
	
	public DropDownInfo(String tabela){
		this.id = new ArrayList<Integer>();
		this.texto = new ArrayList<String>();
		this.descricao = new ArrayList<String>();
		Interface.BD.selectDropDownListBox(tabela, this);
	}
	
	public void adiciona(Integer ID, String texto, String descricao){
		
		 this.id.add(ID); 
		 this.texto.add(texto);
		 this.descricao.add(descricao);
	}
	
	public String[] textoArray(){
		String a[] = new String[texto.size()];
		for(int i=0; i<texto.size(); i++)
			a[i] = texto.get(i);
		
		
		return a;
	}
	
	public String textoDescricao(int i){
		if(descricao.size() == 0)
			return "";
		else
		return descricao.get(i);
	}
	
}
