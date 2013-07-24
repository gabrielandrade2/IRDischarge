package br.gpri.controle;

import activerecord.BD;

public class Variaveis {

	public String Usuario;
	public String caminhoArquivo;
	public ControleExcel Excel;
	public BD BD;
	
	//Janelas
	public ControleArquivo JanelaArquivo;
	public ControleCadastroRegra JanelaCadRegra;
	public ControleCadastroUsuario JanelaCadUsuario;
	public ControleElementos JanelaElementos;
	public ControleExecucao JanelaExecucao;
	public ControleLogin JanelaLogin;
	public ControleResultados JanelaResultados;
	
	public Variaveis(){
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
