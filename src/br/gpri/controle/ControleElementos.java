package br.gpri.controle;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import br.gpri.janelas.JanelaElementos;
import activerecord.*;

public class ControleElementos extends Variaveis{
	
	private JanelaElementos Janela;
	private Regra Regra;
	
	public ControleElementos(Regra r){
		Janela = new JanelaElementos();
		Janela.BotaoOK.addActionListener(this.Ok);
		Janela.BotaoVoltar.addActionListener(this.Voltar);
		this.Regra = r;
		
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
