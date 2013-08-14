package br.gpri.controle;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonModel;

import br.gpri.janelas.JanelaElementos;
import activerecord.*;

public class ControleElementos extends Variaveis{
	
	private JanelaElementos Janela;
	private Regra Regra;
	private List<Termo> Termos;
	
	public ControleElementos(Regra r){
		Janela = new JanelaElementos();
		Janela.BotaoOK.addActionListener(this.Ok);
		Janela.BotaoVoltar.addActionListener(this.Voltar);
		this.Regra = r;
		this.Termos = Regra.getTermos();
		inicializaJanela();
		setVisivel(Regra.getNumTermos());
		setTextoTermos(Regra.getNumTermos());
		//Janela.setLocationRelativeTo(null);
	}
	
	public void abreJanela(){
		Janela.setVisible(true);
	}
	
	public void fechaJanela(){
		Janela.setVisible(false);
		Janela.dispose();
	}
	
	private void inicializaJanela(){
		//Adiciona Labels Radio Buttons		
		for(int i=0; i<Janela.jRadioButton.length; i++){
			Janela.jRadioButton[i].setText("1º Nível");
        	i++;
        	Janela.jRadioButton[i].setText("2º Nível");
        	i++;
        	Janela.jRadioButton[i].setText("Nenhum");
        	Janela.jRadioButton[i].setSelected(true);
        }
        
		//Define grupo dos Radio Buttons
        for(int i=0; i<Janela.GrupoTermo.length; i++)
        	for(int j=0; j<3; j++)
        		Janela.GrupoTermo[i].add(Janela.jRadioButton[(i*3)+j]);
        
        setInvisivel();

	}
	
	private void setInvisivel(){
		//Radio Buttons e Caixas de Texto invisíveis
        for(int i=0; i<Janela.jRadioButton.length; i++)
        	Janela.jRadioButton[i].setVisible(false);
    
        for (int i=0; i<Janela.TextoTermo.length; i++)
        	Janela.TextoTermo[i].setVisible(false);
	}
	
	private void setVisivel(int numTermos){
		//Seta Radio Buttons e Caixas de Texto Visiveis dinamicamente.
		for(int i=0; i< numTermos; i++){
			Janela.jRadioButton[i].setVisible(true);
			Janela.TextoTermo[i].setVisible(true);
		}
	}
	
	private void setTextoTermos(int numTermos){
		for(int i=0; i<numTermos; i++)
			Janela.TextoTermo[i].setText(Termos.get(i).getTexto());
	}
	
	private void getRadioSelecionados(int numTermos){
		for(int i=0; i<numTermos; i++){
			if(Janela.jRadioButton[i].isSelected()){
				String s = Janela.jRadioButton[i].getText();
				if(s.matches("1º Nível"))
					Termos.get(i).setOrdem(1);
				if(s.matches("2º Nível"))
					Termos.get(i).setOrdem(2);
				if(s.matches("Nenhum"))
					Termos.get(i).setOrdem(0);
			}
		}
		Regra.setTermos(Termos);
	}
	
	ActionListener Voltar = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			fechaJanela();

			
		}
	};

	 ActionListener Ok = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getRadioSelecionados(Regra.getNumTermos());
				
				//Inserir BD
				fechaJanela();

				
			}
		};


}
