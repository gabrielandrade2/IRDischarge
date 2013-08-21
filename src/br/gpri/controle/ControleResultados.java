package br.gpri.controle;

import br.gpri.janelas.JanelaResultados;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControleResultados extends Variaveis{
	
	private JanelaResultados Janela;
	
	public ControleResultados(){
		Janela = new JanelaResultados();
		Janela.BotaoOk.addActionListener(this.Ok);
		
		
	}
	
	public void abreJanela(){
		Janela.setVisible(true);
	}
	
	public void fechaJanela(){
		Janela.setVisible(false);
		Janela.dispose();
	}
	
	 ActionListener Ok = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				fechaJanela();
				JanelaArquivo = new ControleArquivo();
				JanelaArquivo.abreJanela();
				
			}
		};
	

}
