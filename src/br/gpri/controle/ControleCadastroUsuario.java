package br.gpri.controle;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import br.gpri.janelas.JanelaCadastroUsuario;

public class ControleCadastroUsuario extends Variaveis {
	
	private String nome;
	private String email;
	private String usuario;
	private String senha;
	private JanelaCadastroUsuario Janela;
	
	public ControleCadastroUsuario() {
		Janela = new JanelaCadastroUsuario();
		Janela.CBotaoOk.addActionListener(OK);
		Janela.CBotaoVoltar.addActionListener(Voltar);
		Janela.setLocationRelativeTo(null);
	}
	
	public void abreJanela(){
		Janela.setVisible(true);
	}
	
	public void fechaJanela(){
		Janela.setVisible(false);
		Janela.dispose();
		JanelaLogin = new ControleLogin();
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
  	  		boolean erro = BD.insertUsuario(usuario,senha,nome,email);
  	  		if(erro)
  	  			System.out.println("Falha ao cadastrar usuário");
  	  		else
  	  			System.out.println("Usuário cadastrado com sucesso");
  	  		fechaJanela();
  	   	 }
	};
	
}

