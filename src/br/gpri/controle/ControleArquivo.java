package br.gpri.controle;

import br.gpri.janelas.JanelaArquivo;

public class ControleArquivo {

	JanelaArquivo Janela;
	
	public ControleArquivo(){
		Janela = new JanelaArquivo();
		Janela.ABotaoOk.addActionListener(null);
		Janela.ABotaoVoltar.addActionListener(null);
	}
	
	public void abreJanela(){
		Janela.inicia();
		Janela.setVisible(true);
	}


}
