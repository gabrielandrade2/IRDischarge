package br.gpri.janelas;

import javax.swing.DefaultListModel;

/**
 *
 * @author sikeira
 */
public class JanelaArquivo extends javax.swing.JFrame {

    public JanelaArquivo() {
        initComponents();
    }

    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        
        jScrollPane1 = new javax.swing.JScrollPane();
        AList = new javax.swing.JList();
        lista = new DefaultListModel();
        jLabel1 = new javax.swing.JLabel();
        ACadastrarRadio = new javax.swing.JRadioButton();
        AExecutarRadio = new javax.swing.JRadioButton();
        ABotaoOk = new javax.swing.JButton();
        ABotaoVoltar = new javax.swing.JButton();
        BotaoAbrir = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        AbreArquivo = new javax.swing.JFileChooser();
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        
     
        BotaoAbrir.setText("Importar");

        jLabel2.setText("INTEMED");
        
        jScrollPane1.setViewportView(AList);

        jLabel1.setText("Arquivos");

        
        //Radio Buttons
        buttonGroup1.add(ACadastrarRadio);
        buttonGroup1.add(AExecutarRadio);
        
        ACadastrarRadio.setText("Cadastrar");
        AExecutarRadio.setText("Executar");
        

        ABotaoOk.setText("OK");
        ABotaoVoltar.setText("Sair");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 475, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(238, 238, 238)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel1))))
                        .addGap(0, 65, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(ACadastrarRadio)
                        .addGap(18, 18, 18)
                        .addComponent(AExecutarRadio)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(ABotaoVoltar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(BotaoAbrir)
                            .addComponent(ABotaoOk, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addGap(15, 15, 15)
                .addComponent(BotaoAbrir)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 327, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ACadastrarRadio)
                    .addComponent(AExecutarRadio)
                    .addComponent(ABotaoOk)
                    .addComponent(ABotaoVoltar))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>                        

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(JanelaArquivo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JanelaArquivo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JanelaArquivo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JanelaArquivo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new JanelaArquivo().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify                     
    public javax.swing.JButton ABotaoOk;
    public javax.swing.JButton ABotaoVoltar;
    public javax.swing.JRadioButton ACadastrarRadio;
    public javax.swing.JRadioButton AExecutarRadio;
    public javax.swing.JList AList;
    public DefaultListModel lista;
    public javax.swing.JButton BotaoAbrir;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    public javax.swing.JFileChooser AbreArquivo;
    // End of variables declaration           
}