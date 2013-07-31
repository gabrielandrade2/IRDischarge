package br.gpri.controle;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import jxl.Cell;

import br.gpri.janelas.JanelaCadastroRegra;

public class ControleCadastroRegra extends Variaveis{

	private Integer linha;
	private JanelaCadastroRegra Janela;
		
	public ControleCadastroRegra(){
		linha = 0;
		
		Janela = new JanelaCadastroRegra();
		Janela.BotaoAnterior1.addActionListener(this.Anterior);
		Janela.BotaoProximo1.addActionListener(this.Proximo);
		Janela.CRBotaoExecutar.addActionListener(this.Executar);
		Janela.CRBotaoVoltar.addActionListener(this.Voltar);
		Janela.BotaoGerarRegra1.addActionListener(this.GerarRegra);
		Janela.BotaoSubRegra.addActionListener(this.GerarSubRegra);
		Janela.setLocationRelativeTo(null);
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
	
	 ActionListener GerarRegra = new ActionListener() {
			
		@Override
		public void actionPerformed(ActionEvent e) {
			JanelaElementos = new ControleElementos();
			JanelaElementos.abreJanela();
			
		}
	};
	
	 ActionListener GerarSubRegra = new ActionListener() {
			
		@Override
		public void actionPerformed(ActionEvent e) {
			JanelaElementos = new ControleElementos();
			JanelaElementos.abreJanela();
			
		}
	};
	
	
}
