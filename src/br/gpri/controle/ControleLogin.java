package br.gpri.controle;

import java.awt.event.ActionEvent;
import activerecord.*;
import java.awt.event.ActionListener;
import java.sql.ResultSet;

import br.gpri.janelas.JanelaLogin;

public class ControleLogin extends Variaveis {

	private JanelaLogin Janela;
	
		
	public ControleLogin(){
		Janela = new JanelaLogin();
		Janela.BotaoCadastro.addActionListener(Cadastro);
		Janela.BotaoLogin.addActionListener(Login);
		Janela.setLocationRelativeTo(null);
	}

	public void abreJanela(){
		Janela.setVisible(true);
	}
	
	public void fechaJanela(){
		Janela.setVisible(false);
		Janela.dispose();
	}
	
	private boolean verificaSenha(Login l){
		if(l.getUsuario() != ""){
			String a = Janela.getSenha();
			String b = l.getSenha();
			if(a.equals(b))
				return true;}
		
		return false;
	}
		
	ActionListener Cadastro = new ActionListener() {
        public void actionPerformed(ActionEvent Cadastro) {
		fechaJanela();
		JanelaCadUsuario = new ControleCadastroUsuario();
		JanelaCadUsuario.abreJanela();
		}
    };
	
	ActionListener Login = new ActionListener() {
        public void actionPerformed(ActionEvent Login) {
        	Login l = new Login();
        	l = BD.selectLogin(Janela.getUsuario());
        	boolean aceito = verificaSenha(l);
        	
        	if(aceito){
        		Usuario = l.getUsuario();
        		fechaJanela();
        		JanelaArquivo = new ControleArquivo();
        		JanelaArquivo.abreJanela(); 
        	}
        	else{
        		System.out.println("Login falhou");
        		Janela.limpaCampos();
        	}
        }
	};
 }
