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


public class Janela extends javax.swing.JFrame  implements ActionListener {

    // Declaração das variáveis                  
    private javax.swing.JButton BotaoAbrir;
    private javax.swing.JButton BotaoAnterior;
    private javax.swing.JButton BotaoGerarRegra;
    private javax.swing.JButton BotaoProximo;
    private javax.swing.JButton BotaoSelecionar;
    private javax.swing.JTextField TextoCaminhoArquivo;
    private javax.swing.JTextField TextoGeraRegra;
    private javax.swing.JTextField TextoRegra;
    private javax.swing.JTextField TextoSumario;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
	
    public Janela() {
        initComponents();
    }

    public void initComponents() {

        TextoSumario = new javax.swing.JTextField();
        TextoGeraRegra = new javax.swing.JTextField();
        TextoRegra = new javax.swing.JTextField();
        BotaoProximo = new javax.swing.JButton();
        BotaoAnterior = new javax.swing.JButton();
        BotaoGerarRegra = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        TextoCaminhoArquivo = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        BotaoAbrir = new javax.swing.JButton();
        BotaoSelecionar = new javax.swing.JButton();

        
        //Habilitando ActionListener Botões      
		BotaoAbrir.addActionListener(this.Abre);
        BotaoProximo.addActionListener(this.Proximo);
		BotaoAnterior.addActionListener(this.Anterior);
		BotaoSelecionar.addActionListener(this.Seleciona);
		BotaoGerarRegra.addActionListener(this.GeraRegra);
		
		
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(800, 600));

        BotaoAnterior.setText("Anterior");

        BotaoProximo.setText("Próximo");

        BotaoGerarRegra.setText("Gerar Regra");

        jLabel1.setText("Sumário");

        jLabel2.setText("Texto");

        jLabel3.setText("Regra");

        jLabel4.setText("Caminho para o Arquivo");

        BotaoAbrir.setText("Abrir");

        BotaoSelecionar.setText("Selecionar");

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
                        .add(6, 6, 6)
                        .add(BotaoGerarRegra, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 110, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
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
                    .add(TextoGeraRegra, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 60, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(BotaoGerarRegra))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel3)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(TextoRegra, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 60, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
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
  	      }       
        }
	};
	
	//Funcionalidade Botão Selecionar Texto
	ActionListener Seleciona = new ActionListener() {
        public void actionPerformed(ActionEvent Seleciona) {
        TextoGeraRegra.setText(TextoSumario.getSelectedText());         
        }
	};

	//Funcionalidade Botão Gerar Regra
	ActionListener GeraRegra = new ActionListener() {
	        public void actionPerformed(ActionEvent GeraRegra) {
	        	String Tagged_Text;//String para teste
	        	Tagged_Text = Interface.Tagger.tagTextCogroo(TextoGeraRegra.getText(), false);
	        	TextoRegra.setText(Tagged_Text);
	        }
		};
	
	//ActionPerformed Genérico (Não faz nada)
	public void actionPerformed(ActionEvent e) {
	}
                 
}
