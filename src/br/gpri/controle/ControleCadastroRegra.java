package br.gpri.controle;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import activerecord.*;
import br.gpri.janelas.JanelaCadastroRegra;
import br.grpi.nlp.Tagger;

public class ControleCadastroRegra extends Variaveis{

	private Integer linha;
	private JanelaCadastroRegra Janela;
	private List<Elemento> elementos;
	private List<Regra> regras;
	
		
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
		Janela.TextoSumario1.setEditable(false);
		Janela.TextoSumario1.setLineWrap(true);
		Janela.TextoSumario1.setWrapStyleWord(true);
		inicializaListasRegras();
		buscaDropDownElementos();
		geraListaRegras();
	
																	//Tirar o textocaminhoarquivo, textogeraregra e textoregra da janela
		Janela.TextoSumario1.setText(Excel.getConteudoCelula(linha)); //Tem 2 textosumario na janela também depois tirar o 1 do final
	}
	
	public void abreJanela(){
		Janela.setVisible(true);
	}
	
	public void fechaJanela(){
		Janela.setVisible(false);
		Janela.dispose();
	}
	
	private void inicializaListasRegras(){
		Janela.ListaRegras.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		Janela.ListaRegras.setLayoutOrientation(JList.VERTICAL);
		Janela.ListaRegras.setVisibleRowCount(5); //Provisório 5 itens verificar
		Janela.ListaRegras.addListSelectionListener(this.Regras);
		
		Janela.ListaSubRegras.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		Janela.ListaSubRegras.setLayoutOrientation(JList.VERTICAL);
		Janela.ListaSubRegras.setVisibleRowCount(5); //Provisório 5 itens verificar
	}
	
	private void buscaDropDownElementos(){
		elementos = BD.selectElemento();
		DefaultComboBoxModel lista = new DefaultComboBoxModel();
		for(int i=0; i<elementos.size(); i++){
			lista.addElement(elementos.get(i).getNome());
		}
		
		Janela.DropDownListBox3.setModel(lista); //Tirar o 3 do final
		Janela.DropDownListBox3.addActionListener(this.DropDownListBox);
		if(!elementos.isEmpty())
			Janela.DropDownListBox3.setSelectedIndex(0);
	}
	
	protected void geraListaRegras(){
		regras = BD.selectRegraCadastro(linha,caminhoArquivo,idUsuario);
		DefaultListModel lista = new DefaultListModel();
		if(regras.isEmpty())
			lista.addElement("Não existem regras");
		else
			for(int i=0; i<regras.size(); i++)
				lista.addElement(regras.get(i).getPrevia());
	
		Janela.ListaRegras.setModel(lista);
		Janela.ListaRegras.updateUI();
		Janela.ListaRegras.setSelectedIndex(0);
		Janela.ListaRegras.ensureIndexIsVisible(0);
	}
	
	 ActionListener Anterior = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			if(linha != 0){
				linha--;
				System.out.println(linha);
				Janela.TextoSumario1.setText(Excel.getConteudoCelula(linha)); //Tirar o 1 do final
				
				geraListaRegras();
			}
			
		}
	};
	
	 ActionListener Proximo = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			if(linha != Excel.getNumLinhas()){
				linha++;
				System.out.println(linha); //DEBUG, mas acho que deveria ter um número contando do lado do texto, talvez editavel
				Janela.TextoSumario1.setText(Excel.getConteudoCelula(linha)); //Tirar o 1 do final
				
				geraListaRegras();
			}			
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
		public void actionPerformed(ActionEvent e) {
			int idElemento = elementos.get(Janela.DropDownListBox3.getSelectedIndex()).getId();  //tirar o 3 do dropdown
			int idRegra = BD.selectMaxIdRegra();
			System.out.println(idRegra);
			System.out.println(idRegra);
			System.out.println(idRegra);
			
			
			Tagger Tagger = new Tagger(); 
			Regra r = Tagger.geraRegra(Janela.TextoSumario1.getText(), Janela.TextoSumario1.getSelectedText(), idElemento, idRegra);
			r.setIdTexto(linha);
			r.setCaminhoArquivo(caminhoArquivo);
			
			JanelaElementos = new ControleElementos(r);
			JanelaElementos.abreJanela();
			
		}
	};
	
	
	ActionListener GerarSubRegra = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			Integer index = Janela.ListaRegras.getSelectedIndex();
			if(index.equals(null))
				System.out.println("É necessario selecionar uma regra");
			else{
				Regra r = regras.get(index);
				int idRegra = r.getId();
				
				int idSubRegra = BD.selectMaxIdSubRegra(idRegra);
				System.out.println(idSubRegra);
				System.out.println(idSubRegra);
				System.out.println(idSubRegra);
							
				Tagger Tagger = new Tagger();
				Subregra s = Tagger.geraSubRegra(Janela.TextoSumario1.getText(), Janela.TextoSumario1.getSelectedText(), idRegra, idSubRegra);
				JanelaElementos = new ControleElementos(s);
				JanelaElementos.abreJanela();
			}
		}
	};
	
	ActionListener DropDownListBox = new ActionListener() {
		public void actionPerformed(ActionEvent DropDownListBox) {
			int item = Janela.DropDownListBox3.getSelectedIndex();
			String tooltip = elementos.get(item).getDescricao();
			Janela.DropDownListBox3.setToolTipText(tooltip);	//Tirar o 3 do final
		}
	};
	
	ListSelectionListener Regras = new ListSelectionListener() {
		public void valueChanged(ListSelectionEvent Regras) {
			DefaultListModel lista = new DefaultListModel();
			int index = Janela.ListaRegras.getSelectedIndex();
			if(index > 0){
				int idRegra = regras.get(index).getId();
				List<Subregra> subregras = BD.selectSubRegra(idRegra);
				if(subregras.isEmpty()){
					lista.addElement("Não existem subregras");
				}
				else{
					for(int i=0; i<subregras.size(); i++){
						lista.addElement(subregras.get(i).getPrevia());
					}Janela.ListaRegras.setSelectedIndex(0);
				}
			}
			else
				lista.addElement("Não existem subregras");
			Janela.ListaSubRegras.setModel(lista);
			Janela.ListaSubRegras.updateUI();
			
		}
	};
}

