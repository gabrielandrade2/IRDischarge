package br.gpri.controle;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonModel;

import br.gpri.janelas.JanelaIndices;
import activerecord.*;

public class ControleIndice extends Variaveis{
	
	private JanelaIndices Janela;
	private Regra Regra;
	private Subregra Subregra;
	private List<Termo> Termos;
	private boolean isSubregra;
	
	public ControleIndice(Regra r){
		Janela = new JanelaIndices();
		this.isSubregra = false;
		this.Regra = r;
		this.Termos = Regra.getTermos();
		inicializaJanela();
		setVisivel(Regra.getNumTermos());
		setTextoTermos(Regra.getNumTermos());
		int altura = 200+Regra.getNumTermos()*20;
		Janela.setSize(400, altura);
		
	}
	
	public ControleIndice(Subregra s){
		Janela = new JanelaIndices();
		this.isSubregra = true;
		this.Subregra = s;
		this.Termos = Subregra.getTermos();
		inicializaJanela();
		setVisivel(Subregra.getNumTermos());
		setTextoTermos(Subregra.getNumTermos());
		int altura = 200+Regra.getNumTermos()*20;
		Janela.setSize(400, altura);
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
        Janela.BotaoOk.addActionListener(this.Ok);
		Janela.BotaoVoltar.addActionListener(this.Voltar);
		Janela.setLocationRelativeTo(null);
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
			Janela.TextoTermo[i].setVisible(true);
		}
		for(int i=0; i< numTermos*3; i++){
			Janela.jRadioButton[i].setVisible(true);
		}
		
	}
	
	private void setTextoTermos(int numTermos){
		for(int i=0; i<numTermos; i++)
			Janela.TextoTermo[i].setText(Termos.get(i).getTexto());
	}
	
	private void getRadioSelecionados(int numTermos){
		for(int i=0; i<numTermos; i++){
			for(int j=0; j<3; j++){
				if(Janela.jRadioButton[(3*i)+j].isSelected()){
					String s = Janela.jRadioButton[(3*i)+j].getText();
					if(s.matches("1º Nível"))
						Termos.get(i).setOrdem(1);
					if(s.matches("2º Nível"))
						Termos.get(i).setOrdem(2);
					if(s.matches("Nenhum"))
						Termos.get(i).setOrdem(0);
				}
			}
		}
	}
	
	ActionListener Voltar = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			fechaJanela();

			
		}
	};

	 ActionListener Ok = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(isSubregra){
					getRadioSelecionados(Subregra.getNumTermos());
					Subregra.setTermos(Termos);
					boolean erro = BD.insertSubRegra(Subregra);
					if(erro)
						System.out.println("Erro ao inserir a subregra no Banco de Dados");
					else
						System.out.println("Subregra inserida no Banco de Dados com sucesso");
				}
				else{
					getRadioSelecionados(Regra.getNumTermos());
					Regra.setTermos(Termos);
					boolean erro = BD.insertRegra(idUsuario, Regra);
					if(erro)
						System.out.println("Erro ao inserir a regra no Banco de Dados");
					else
						System.out.println("Regra inserida no Banco de Dados com sucesso");
				}
				
				JanelaCadRegra.geraListaRegras();
				fechaJanela();		
			}
		};


}
