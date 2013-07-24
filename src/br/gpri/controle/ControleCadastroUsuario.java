package br.gpri.controle;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import br.gpri.janelas.JanelaCadastroUsuario;

public class ControleCadastroUsuario extends Variaveis{
	
	String nome;
	String email;
	String usuario;
	String senha;
	JanelaCadastroUsuario Janela;
	
	public ControleCadastroUsuario() {
		Janela = new JanelaCadastroUsuario();
		Janela.CBotaoOk.addActionListener(OK);
		Janela.CBotaoVoltar.addActionListener(Voltar);
	}
	
	public void abreJanela(){
		Janela.setVisible(true);
	}
	
	public void fechaJanela(){
		Janela.setVisible(false);
		Janela.dispose();
		JanelaLogin.abreJanela();
	}
	
	ActionListener Voltar = new ActionListener() {
	      public void actionPerformed(ActionEvent Voltar) {
	    	  fechaJanela();
	      }
	};	
	
    ActionListener OK = new ActionListener() {
	    public void actionPerformed(ActionEvent OK) {
	    	nome = Janela.getNome();
  	  		email = Janela.getEmail();
  	  		usuario = Janela.getUsuario();
  	  		senha = Janela.getSenha();
  	  		boolean erro = BD.insertBD("INSERT INTO usuarios VALUES('"+usuario+"','"+senha+"','"+nome+"','"+email+"');");
  	  		if(erro)
  	  			System.out.println("Falha ao cadastrar usuário");
  	  		else
  	  			System.out.println("Usuário cadastrado com sucesso");
  	  		fechaJanela();
  	   	 }
	};
	
}

