package br.gpri.controle;

import java.util.ArrayList;

import activerecord.BD;


public class Principal {
	
	public static String Usuario;
	public static String caminhoArquivo;
	public static ControleExcel Excel;
	public static BD BD;
	
	//Janelas
	public static ControleArquivo JanelaArquivo;
	public static ControleCadastroRegra JanelaCadRegra;
	public static ControleCadastroUsuario JanelaCadUsuario;
	public static ControleElementos JanelaElementos;
	public static ControleExecucao JanelaExecucao;
	public static ControleLogin JanelaLogin;
	public static ControleResultados JanelaResultados;
		

	public static void main(String[] args) {
		iniciaComponentes();
		
		JanelaLogin.abreJanela();
		
		

	}

	public static void iniciaComponentes(){
		Usuario = new String();
		caminhoArquivo = new String();
		Excel = new ControleExcel();
		BD = new BD();
		
		//Inicia Janelas
		JanelaArquivo = new ControleArquivo();
		JanelaCadRegra = new ControleCadastroRegra();
		JanelaCadUsuario = new ControleCadastroUsuario();
		JanelaElementos = new ControleElementos();
		JanelaExecucao = new ControleExecucao();
		JanelaLogin = new ControleLogin();
		JanelaResultados = new ControleResultados();
	}
	
	
}
