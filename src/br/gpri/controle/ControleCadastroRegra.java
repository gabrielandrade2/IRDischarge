package br.gpri.controle;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import jxl.Cell;

import br.gpri.janelas.JanelaCadastroRegra;

public class ControleCadastroRegra extends Principal{

	protected Integer linha;
	protected JanelaCadastroRegra Janela;
	protected Cell celula;
		
	public ControleCadastroRegra(){
		linha = 0;
		celula = null;
		
		Janela = new JanelaCadastroRegra();
	}
	
	public void abreJanela(){
		Janela.setVisible(true);
	}
	
	public void fechaJanela(){
		Janela.setVisible(false);
		Janela.dispose();
	}
	
}
