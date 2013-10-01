package br.gpri.controle;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import activerecord.*;
import br.gpri.janelas.JanelaCadastroRegra;
import br.gpri.nlp.Tagger;

public class ControleCadastroRegra extends Variaveis{

	private Integer linha;
	private Integer numTextos;
	private JanelaCadastroRegra Janela;
	private List<Elemento> elementos;
	private List<Regra> regras;
	List<Subregra> subregras;
	List<String> textos = new ArrayList<String>();
	
		
	public ControleCadastroRegra(){
		linha = 0;
		numTextos = BD.getNumTextos(idUsuario, idArquivo);
		
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
		inicializaListas();
		buscaDropDownElementos();
		geraListaRegras();
		Janela.ListaRegras.setSelectedIndex(0);
		Janela.ListaTexto.setSelectedIndex(linha);
		Janela.Indice.setText("1");
		Janela.Indice.setEditable(true);	
		Janela.Indice.addActionListener(this.Indice);
		
	}
	
	public void abreJanela(){
		Janela.setVisible(true);
	}
	
	public void fechaJanela(){
		Janela.setVisible(false);
		Janela.dispose();
	}
	
	private void inicializaListas(){
		textos=(BD.selectTextos(idUsuario, idArquivo));
		Janela.ListaTexto.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		Janela.ListaTexto.setLayoutOrientation(JList.VERTICAL);
		Janela.ListaTexto.addListSelectionListener(this.Textos);
		DefaultListModel listaTexto = new DefaultListModel();
		for(int i=0; i<textos.size(); i++){
			listaTexto.addElement(textos.get(i));
			
		}
		Janela.ListaTexto.setModel(listaTexto);
		
		Janela.ListaRegras.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		Janela.ListaRegras.setLayoutOrientation(JList.VERTICAL);
		Janela.ListaRegras.setVisibleRowCount(5); //Provisório 5 itens verificar
		Janela.ListaRegras.addListSelectionListener(this.Regras);
		
		Janela.ListaSubRegras.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		Janela.ListaSubRegras.setLayoutOrientation(JList.VERTICAL);
		Janela.ListaSubRegras.setVisibleRowCount(5); //Provisório 5 itens verificar
		Janela.ListaSubRegras.addListSelectionListener(this.Subregra);
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
		regras = BD.selectRegraCadastro(linha,idArquivo,idUsuario);
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
		atualizaSubRegras();
	}
	
	 ActionListener Anterior = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			if(linha != 0){
				linha--;
				Janela.ListaTexto.setSelectedIndex(linha);
			}
			
		}
	};
	
	 ActionListener Proximo = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			if(linha != (numTextos)-1){
				linha++;
				Janela.ListaTexto.setSelectedIndex(linha);
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
		
			Tagger Tagger = new Tagger(BD); 
			Regra r = Tagger.geraRegra(Janela.TextoSumario1.getText(), Janela.TextoSumario1.getSelectedText(), idElemento, idRegra);
			r.setIdTexto(linha);
			r.setIdArquivo(idArquivo);
			
			JanelaIndices = new ControleIndice(r);
			JanelaIndices.abreJanela();
			
		}
	};
	
	
	ActionListener GerarSubRegra = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			Integer index = Janela.ListaRegras.getSelectedIndex();Janela.ListaSubRegras.getSelectedIndex();
			if(index.equals(null))
				System.out.println("É necessario selecionar uma regra");
			else{
				Regra r = regras.get(index);
				int idRegra = r.getId();
				
				int idSubRegra = BD.selectMaxIdSubRegra(idRegra);
						
				Tagger Tagger = new Tagger(BD);
				Subregra s = Tagger.geraSubRegra(Janela.TextoSumario1.getText(), Janela.TextoSumario1.getSelectedText(), idRegra, idSubRegra);
				JanelaIndices = new ControleIndice(s);
				JanelaIndices.abreJanela();
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
			atualizaSubRegras();
			if(regras.size() > 0){
					int a = Janela.ListaRegras.getSelectedIndex();
					if(a < 0){
						Janela.ListaRegras.setSelectedIndex(0);
						a = Janela.ListaRegras.getSelectedIndex();
					}
					Regra r = regras.get(a);Janela.ListaSubRegras.getSelectedIndex();
					Janela.ListaRegras.setToolTipText(r.getTexto());
			}
		}
	};
	
	ListSelectionListener Textos = new ListSelectionListener() {
		public void valueChanged(ListSelectionEvent Regras) {
			Janela.TextoSumario1.setText((String) Janela.ListaTexto.getSelectedValue());
			linha = Janela.ListaTexto.getSelectedIndex();
			Integer l = linha+1;
			Janela.Indice.setText(l.toString());
			System.out.println(linha);
			geraListaRegras();

		}
			
	};
	
	ActionListener Indice = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			Integer l = Integer.parseInt(Janela.Indice.getText());
			if(l>0){
				if(l<Janela.ListaTexto.getModel().getSize()){
					linha = l-1;
					Janela.ListaTexto.setSelectedIndex(linha);
				}
				else{
					l = Janela.ListaTexto.getModel().getSize();
					linha = l-1;
					Janela.ListaTexto.setSelectedIndex(linha);
				}
			}
			else{
				l = linha +1;
				Janela.Indice.setText(l.toString());
			}
		}
	};
	
	ListSelectionListener Subregra = new ListSelectionListener() {
		public void valueChanged(ListSelectionEvent Regras) {
			if(subregras.size() > 0){
				int a = Janela.ListaSubRegras.getSelectedIndex();
				if (a > 0){
					Subregra s =  subregras.get(a);
					Janela.ListaSubRegras.setToolTipText(s.getTexto());
				}
			}
		}
	};
	
	private void atualizaSubRegras(){
		DefaultListModel lista = new DefaultListModel();
		int index = Janela.ListaRegras.getSelectedIndex();
		if(index >= 0 && !regras.isEmpty()){
			subregras = BD.selectSubRegras(regras.get(index));
			if(subregras.isEmpty()){
				lista.addElement("Não existem subregras");
			}
			else{
				for(int i=0; i<subregras.size(); i++){
					lista.addElement(subregras.get(i).getPrevia());
				}
			}
		}
		else
			lista.addElement("Não existem subregras");
		Janela.ListaSubRegras.setModel(lista);
		Janela.ListaSubRegras.updateUI();
	}
	

}

