package br.gpri.controle;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import br.gpri.janelas.JanelaComentario;

public class ControleComentario extends Variaveis{

	private JanelaComentario Janela;
	
	
	public ControleComentario(){
		Janela = new JanelaComentario();
		Janela.BotaoOk.addActionListener(this.Ok);
		Janela.TextoComentario.setEditable(true);
		Janela.setLocationRelativeTo(null);
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
			
		}
	};
	
	
}
