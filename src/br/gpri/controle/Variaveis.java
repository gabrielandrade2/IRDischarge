package br.gpri.controle;

import activerecord.BD;
import activerecord.Arquivo;

public class Variaveis {
	
	protected static int idUsuario = 0;
	protected static ControleExcel Excel = new ControleExcel();
	protected static BD BD = new BD();
	protected static int idArquivo;
		
	protected static ControleArquivo JanelaArquivo;
	protected static ControleCadastroRegra JanelaCadRegra;
	protected static ControleCadastroUsuario JanelaCadUsuario;
	protected static ControleIndice JanelaIndices;
	protected static ControleExecucao JanelaExecucao;
	protected static ControleLogin JanelaLogin;
	protected static ControleResultados JanelaResultados;
	}

