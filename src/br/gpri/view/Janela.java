/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * Janela.java
 *
 * Created on 19/03/2013, 21:09:49
 */
package br.gpri.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;



public class Janela extends javax.swing.JFrame  implements ActionListener {

    // Declaração das variáveis                  
    private JButton BotaoAbrir;
    private JButton BotaoAnterior;
    //private JButton BotaoGerarRegra;
    private JButton BotaoProximo;
    private JButton BotaoSelecionar;
    private JTextField TextoCaminhoArquivo;
    private JTextField TextoGeraRegra;
    private JTextField TextoRegra;
    private JTextArea TextoSumario;
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JLabel jLabel3;
    private JLabel jLabel4;
    private JScrollPane Scroll;
    
	
    public Janela() {
        initComponents();
    }

    public void initComponents() {

        TextoSumario = new javax.swing.JTextArea();
        TextoGeraRegra = new javax.swing.JTextField();
        TextoRegra = new javax.swing.JTextField();
        BotaoProximo = new javax.swing.JButton();
        BotaoAnterior = new javax.swing.JButton();
        //BotaoGerarRegra = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        TextoCaminhoArquivo = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        BotaoAbrir = new javax.swing.JButton();
        BotaoSelecionar = new javax.swing.JButton();
        Scroll = new JScrollPane(TextoSumario);

                
        //Quebra de Linha Caixa de texto Sumário
        TextoSumario.setLineWrap(true);
        TextoSumario.setWrapStyleWord(true);  
        
        //Scroll da Caixa de Texto do Sumário
        
        
        
        //Habilitando ActionListener Botões      
		BotaoAbrir.addActionListener(this.Abre);
        BotaoProximo.addActionListener(this.Proximo);
		BotaoAnterior.addActionListener(this.Anterior);
		BotaoSelecionar.addActionListener(this.Seleciona);
		//BotaoGerarRegra.addActionListener(this.GeraRegra);
		
		//Desabilitando edição
		TextoSumario.setEditable(false);
		TextoGeraRegra.setEditable(false);
		TextoRegra.setEditable(false);
		
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(800, 600));

        BotaoAnterior.setText("Anterior");

        BotaoProximo.setText("Próximo");

        //BotaoGerarRegra.setText("Gerar Regra");

        jLabel1.setText("Sumário");

        jLabel2.setText("Texto");

        jLabel3.setText("Regra");

        jLabel4.setText("Caminho para o Arquivo");

        BotaoAbrir.setText("Abrir");

        BotaoSelecionar.setText("Gerar Regra");

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(TextoSumario)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, BotaoAnterior, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, BotaoProximo, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(BotaoSelecionar, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE)))
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(TextoGeraRegra)
                            .add(TextoRegra))
                        .add(6, 6, 6))
                        //.add(BotaoGerarRegra, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 110, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel1)
                            .add(jLabel2)
                            .add(jLabel3)
                            .add(jLabel4)
                            .add(layout.createSequentialGroup()
                                .add(TextoCaminhoArquivo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 470, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(BotaoAbrir, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 80, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                        .add(0, 220, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel4)
                .add(2, 2, 2)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(TextoCaminhoArquivo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(BotaoAbrir))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel1)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(BotaoAnterior)
                        .add(18, 18, 18)
                        .add(BotaoProximo)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 162, Short.MAX_VALUE)
                        .add(BotaoSelecionar))
                    .add(TextoSumario))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel2)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(TextoGeraRegra, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 60, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    //.add(BotaoGerarRegra))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel3)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(TextoRegra, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 60, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap()
                )
        );
        
        pack();
    }// </editor-fold>                        

    
    public void abrejanela() {
    
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Janela.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Janela.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Janela.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Janela.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new Janela().setVisible(true);
            }
        });
    }
    
  
    //Funcionalidade BotÃ£o Abrir
    ActionListener Abre = new ActionListener() {
        public void actionPerformed(ActionEvent Abre) {
                Interface.caminho = TextoCaminhoArquivo.getText();
                System.out.println(Interface.caminho);
                Interface.Excel();
                TextoSumario.setText(Interface.celula.getContents());
        }
	};
	
	//Funcionalidade BotÃ£o Proximo/Anterior
	ActionListener Proximo = new ActionListener() {
	    public void actionPerformed(ActionEvent Proximo) {
	        if(Interface.linha != Interface.Planilha.getRows()){
	    	  Interface.linha++;
	    	  System.out.println(Interface.linha);
	    	  Interface.celula = Interface.Planilha.getCell(0, Interface.linha);
	    	  TextoSumario.setText(Interface.celula.getContents());
	    	  
	    	  //Limpa Caixa de Texto Regras
	    	  TextoGeraRegra.setText(null);
	    	  TextoRegra.setText(null);
	      }
	    }
	};
	ActionListener Anterior = new ActionListener() {
        public void actionPerformed(ActionEvent Anterior) {
        	if(Interface.linha != 0){
  	    	  Interface.linha--;
  	    	  System.out.println(Interface.linha);
  	    	  Interface.celula = Interface.Planilha.getCell(0, Interface.linha);
  	    	  TextoSumario.setText(Interface.celula.getContents());
  	    	  
  	    	  //Limpa Caixa de Texto Regras
	    	  TextoGeraRegra.setText(null);
	    	  TextoRegra.setText(null);
  	      }       
        }
	};
	
	//Funcionalidade Botão Selecionar Texto
	ActionListener Seleciona = new ActionListener() {
        public void actionPerformed(ActionEvent Seleciona) {
        TextoGeraRegra.setText(TextoSumario.getSelectedText());
        TextoRegra.setText(Interface.Tagger.TaggerInterface(TextoSumario.getText(), TextoGeraRegra.getText(),false));
    	
    	//Inserts
    	Interface.windowinsert.ExecutaJanela();
        }
	};

	
	//ActionPerformed Genérico (Não faz nada)
	public void actionPerformed(ActionEvent e) {
	}
                 
}
