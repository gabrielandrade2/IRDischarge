package br.gpri.controle;

import br.gpri.janelas.JanelaExecucao;

public class ControleExecucao extends Principal{

	JanelaExecucao Janela; 
	
	public ControleExecucao(){
		
	}
	
	public void abreJanela(){
		Janela.setVisible(true);
	}
	
	public void fechaJanela(){
		Janela.setVisible(false);
		Janela.dispose();
	}
}
