package br.gpri.controle;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import br.gpri.janelas.JanelaElementos;

public class ControleElementos {
	
	private JanelaElementos Janela;
	
	public ControleElementos(){
		Janela = new JanelaElementos();
		Janela.BotaoOK.addActionListener(this.Ok);
		Janela.BotaoVoltar.addActionListener(this.Voltar);
		
	}
	
	public void abreJanela(){
		Janela.setVisible(true);
	}
	
	public void fechaJanela(){
		Janela.setVisible(false);
		Janela.dispose();
	}
	
	ActionListener Voltar = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			fechaJanela();

			
		}
	};

	 ActionListener Ok = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				fechaJanela();

				
			}
		};
	

}
