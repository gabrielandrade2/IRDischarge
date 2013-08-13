package br.gpri.controle;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

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
		setRadioButtonsVisible(Regra.getNumTermos());
		setTextoTermos(Regra.getNumTermos());	
	}
	
	public void abreJanela(){
		Janela.setVisible(true);
	}
	
	public void fechaJanela(){
		Janela.setVisible(false);
		Janela.dispose();
	}
	
	private void setRadioButtonsVisible(int numTermos){
		if(numTermos >= 1){
			Janela.jRadioButton1.setVisible(true);
			Janela.jRadioButton2.setVisible(true);
			Janela.jRadioButton3.setVisible(true);
		}
		if(numTermos >= 2){
			Janela.jRadioButton4.setVisible(true);
			Janela.jRadioButton5.setVisible(true);
			Janela.jRadioButton6.setVisible(true);
		}
		if(numTermos >= 3){
			Janela.jRadioButton7.setVisible(true);
			Janela.jRadioButton8.setVisible(true);
			Janela.jRadioButton9.setVisible(true);
		}
		if(numTermos >= 4){
			Janela.jRadioButton10.setVisible(true);
			Janela.jRadioButton11.setVisible(true);
			Janela.jRadioButton12.setVisible(true);
		}
		if(numTermos >= 5){
			Janela.jRadioButton13.setVisible(true);
			Janela.jRadioButton14.setVisible(true);
			Janela.jRadioButton15.setVisible(true);
		}
		if(numTermos >= 6){
			Janela.jRadioButton16.setVisible(true);
			Janela.jRadioButton17.setVisible(true);
			Janela.jRadioButton18.setVisible(true);
		}
		if(numTermos >= 7){
			Janela.jRadioButton19.setVisible(true);
			Janela.jRadioButton20.setVisible(true);
			Janela.jRadioButton21.setVisible(true);
		}
		if(numTermos >= 8){
			Janela.jRadioButton22.setVisible(true);
			Janela.jRadioButton23.setVisible(true);
			Janela.jRadioButton24.setVisible(true);
		}
		if(numTermos >= 9){
			Janela.jRadioButton25.setVisible(true);
			Janela.jRadioButton26.setVisible(true);
			Janela.jRadioButton27.setVisible(true);
		}
		if(numTermos >= 10){
			Janela.jRadioButton1.setVisible(true);
			Janela.jRadioButton1.setVisible(true);
			Janela.jRadioButton1.setVisible(true);
		}
		
	}
	
	private void setTextoTermos(int numTermos){
		if(numTermos >=1){
			Janela.TextoTermo1.setText(Termos.get(0).getTexto());
		}
		if(numTermos >=2){
			Janela.TextoTermo2.setText(Termos.get(1).getTexto());
		}
		if(numTermos >=3){
			Janela.TextoTermo3.setText(Termos.get(2).getTexto());
		}
		if(numTermos >=4){
			Janela.TextoTermo4.setText(Termos.get(3).getTexto());
		}
		if(numTermos >=5){
			Janela.TextoTermo5.setText(Termos.get(4).getTexto());
		}
		if(numTermos >=6){
			Janela.TextoTermo6.setText(Termos.get(5).getTexto());
		}
		if(numTermos >=7){
			Janela.TextoTermo7.setText(Termos.get(6).getTexto());
		}
		if(numTermos >=8){
			Janela.TextoTermo8.setText(Termos.get(7).getTexto());
		}
		if(numTermos >=9){
			Janela.TextoTermo9.setText(Termos.get(8).getTexto());
		}
		if(numTermos >=10){
			Janela.TextoTermo10.setText(Termos.get(9).getTexto());
		}
	}
	
	ActionListener Voltar = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			fechaJanela();

			
		}
	};

	 ActionListener Ok = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				fechaJanela();

				
			}
		};
	

}
