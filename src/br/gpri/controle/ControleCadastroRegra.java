package br.gpri.controle;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import jxl.Cell;

import br.gpri.janelas.JanelaCadastroRegra;

public class ControleCadastroRegra extends Variaveis{

	protected Integer linha;
	protected JanelaCadastroRegra Janela;
	protected Cell celula;
	
	
	public ControleCadastroRegra(){
		linha = 0;
		celula = null;
		
		Janela = new JanelaCadastroRegra();
		Janela.BotaoAnterior1.addActionListener(this.Anterior);
		Janela.BotaoProximo1.addActionListener(this.Proximo);
		Janela.CRBotaoExecutar.addActionListener(this.Executar);
		Janela.CRBotaoVoltar.addActionListener(this.Voltar);
	}
	
	public void abreJanela(){
		Janela.setVisible(true);
	}
	
	public void fechaJanela(){
		Janela.setVisible(false);
		Janela.dispose();
	}
	 ActionListener Anterior = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			
		}
	};
	
	 ActionListener Proximo = new ActionListener() {
			
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			
		}
	};
	
	 ActionListener Executar = new ActionListener() {
			
		@Override
		public void actionPerformed(ActionEvent e) {
			fechaJanela();
			JanelaExecucao = new ControleExecucao();
			JanelaExecucao.abreJanela();
		}
	};
	
	 ActionListener Voltar = new ActionListener() {
			
		@Override
		public void actionPerformed(ActionEvent e) {
			fechaJanela();
			JanelaArquivo = new ControleArquivo();
			JanelaArquivo.abreJanela();
			
		}
	};
	
	
}
