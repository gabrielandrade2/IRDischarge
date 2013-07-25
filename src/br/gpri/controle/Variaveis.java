package br.gpri.controle;

import activerecord.BD;

public class Variaveis {
	
	protected static int idUsuario = 0;
	protected static String caminhoArquivo = new String();
	protected static ControleExcel Excel = new ControleExcel();
	protected static BD BD = new BD();
	
	protected static ControleArquivo JanelaArquivo;
	protected static ControleCadastroRegra JanelaCadRegra;
	protected static ControleCadastroUsuario JanelaCadUsuario;
	protected static ControleElementos JanelaElementos;
	protected static ControleExecucao JanelaExecucao;
	protected static ControleLogin JanelaLogin;
	protected static ControleResultados JanelaResultados;
	}
