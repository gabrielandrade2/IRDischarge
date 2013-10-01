package br.gpri.controle;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import br.gpri.janelas.JanelaComentario;

public class ControleComentario extends Variaveis{

	private JanelaComentario Janela;
	Integer id;
	String comentario;
	public ControleComentario(int idResultado){
		Janela = new JanelaComentario();
		Janela.BotaoOk.addActionListener(this.Ok);
		Janela.TextoComentario.setEditable(true);
		id = idResultado;
		Janela.TextoComentario.setText(BD.selectComentario(id));
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
			comentario = Janela.TextoComentario.getText();
			BD.insertComentario(id, comentario);
			fechaJanela();
			
		}
	};
	
	
}
