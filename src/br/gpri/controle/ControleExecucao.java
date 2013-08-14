package br.gpri.controle;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.table.DefaultTableModel;

import activerecord.Conjunto;
import activerecord.Elemento;
import activerecord.Regra;
import br.gpri.janelas.JanelaExecucao;

public class ControleExecucao extends Variaveis{

	private JanelaExecucao Janela; 
	private List<Elemento> elementos;
	private List<Conjunto> conjuntos;
	private List<Regra> regras;
	private JCheckBox[] checkbox;
	
	public ControleExecucao(){
		
		Janela = new JanelaExecucao();
		Janela.BotaoOk.addActionListener(this.Ok);
		Janela.BOTAOVOLTAR.addActionListener(this.Voltar);
		Janela.BotaoTrocar.addActionListener(this.Trocar);
		Janela.setLocationRelativeTo(null);
		
		buscaDropDownElementos();
		buscaDropDownConjuntos();
		criaTabela();
	}
	
	private void criaTabela(){
	regras = BD.selectRegraExecucao(idUsuario);
	DefaultTableModel tabela = new DefaultTableModel() {  
	    @Override  
	    public Class<?> getColumnClass(int column) {  
	        if (column == 0)  
	            return Boolean.class;  
	        return super.getColumnClass(column);  
	    }  };
	checkbox = new JCheckBox[regras.size()];
	for(int i=0; i<checkbox.length;i++){
		checkbox[i]=new JCheckBox();
		
	}
	
	tabela.addColumn("Seleção");
	tabela.addColumn("Texto");
	tabela.addColumn("Regra");
	tabela.addColumn("Elemento");
	 
	for(int i=0; i<regras.size();i++){
		Regra r = regras.get(i);
		Object[] o={true,r.getTexto(), r.getPrevia(), r.getElemento()};
		tabela.addRow(o);
		
	}
	Janela.TabelaExecucao.setModel(tabela);
		
		
	}
	
	private void buscaDropDownElementos(){
		elementos = BD.selectElemento();
		Elemento todos = new Elemento();
		todos.setId(0);
		todos.setNome("TODOS");
		todos.setDescricao("Todos os elementos");
		elementos.add(0, todos);
		DefaultComboBoxModel lista = new DefaultComboBoxModel();
		for(int i=0; i<elementos.size(); i++){
			lista.addElement(elementos.get(i).getNome());
		}
		Janela.DropDownlistboxElementos.setModel(lista); 
		Janela.DropDownlistboxElementos.addActionListener(this.DropDownListBoxElem);
		if(!elementos.isEmpty())
			Janela.DropDownlistboxElementos.setSelectedIndex(0);
	}
	
	private void buscaDropDownConjuntos(){
		conjuntos = BD.selectConjunto();
		Conjunto todos = new Conjunto();
		todos.setId(0);
		todos.setNome("TODOS");
		conjuntos.add(0, todos);
		DefaultComboBoxModel lista = new DefaultComboBoxModel();
		for(int i=0; i<conjuntos.size(); i++){
			lista.addElement(conjuntos.get(i).getNome());
		}
		
		Janela.DropDownlistboxConjunto.setModel(lista); 
		Janela.DropDownlistboxConjunto.addActionListener(this.DropDownListBoxConj);
		if(!conjuntos.isEmpty())
			Janela.DropDownlistboxConjunto.setSelectedIndex(0);
	}
	
	public void abreJanela(){
		Janela.setVisible(true);
	}
	
	public void fechaJanela(){
		Janela.setVisible(false);
		Janela.dispose();
	}
	
	 ActionListener Voltar = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				fechaJanela();
				JanelaCadRegra.abreJanela();
				
			}
		};
	
		 ActionListener Ok = new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					fechaJanela();
					JanelaResultados = new ControleResultados();
					JanelaResultados.abreJanela();
					
				}
			};
		
		 ActionListener Trocar = new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					
					
				}
			};
			
	
		ActionListener DropDownListBoxElem = new ActionListener() {
			public void actionPerformed(ActionEvent DropDownListBox) {
				int item = Janela.DropDownlistboxElementos.getSelectedIndex();
				String tooltip = elementos.get(item).getDescricao();
				Janela.DropDownlistboxElementos.setToolTipText(tooltip);
			}
		};

		ActionListener DropDownListBoxConj = new ActionListener() {
			public void actionPerformed(ActionEvent DropDownListBox) {
				int item = Janela.DropDownlistboxConjunto.getSelectedIndex();
				String tooltip = elementos.get(item).getDescricao();
				Janela.DropDownlistboxConjunto.setToolTipText(tooltip);
			}
		};
}
