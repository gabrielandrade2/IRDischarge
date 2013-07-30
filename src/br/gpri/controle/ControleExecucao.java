package br.gpri.controle;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import br.gpri.janelas.JanelaExecucao;

public class ControleExecucao extends Variaveis{

	private JanelaExecucao Janela; 
	
	public ControleExecucao(){
		
		Janela = new JanelaExecucao();
		Janela.BotaoOk.addActionListener(this.Ok);
		Janela.BOTAOVOLTAR.addActionListener(this.Voltar);
		Janela.BotaoTrocar.addActionListener(this.Trocar);
		Janela.setLocationRelativeTo(null);
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
				JanelaCadRegra = new ControleCadastroRegra();
				JanelaCadRegra.abreJanela();
				
			}
		};
	
		 ActionListener Ok = new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					fechaJanela();
					JanelaResultados = new ControleResultados();
					JanelaResultados.abreJanela();
					
				}
			};
			
			 ActionListener Trocar = new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						
						
					}
				};
}
