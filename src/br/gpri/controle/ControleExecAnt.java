package br.gpri.controle;

import br.gpri.janelas.JanelaExecAnt;

public class ControleExecAnt {
	
	private JanelaExecAnt Janela;

	public ControleExecAnt(){
		Janela = new JanelaExecAnt();
		
		
	}
	public void abreJanela(){
		Janela.setVisible(true);
	}
	
	public void fechaJanela(){
		Janela.setVisible(false);
		Janela.dispose();
	}
	
}
