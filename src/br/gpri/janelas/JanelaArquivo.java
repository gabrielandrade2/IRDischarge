package br.gpri.janelas;

/**
 *
 * @author sikeira
 */
public class JanelaArquivo extends javax.swing.JFrame {

    public JanelaArquivo() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        radioButtonGroup = new javax.swing.ButtonGroup();
        jScrollPane1 = new javax.swing.JScrollPane();
        AList = new javax.swing.JList();
        jLabel1 = new javax.swing.JLabel();
        ACadastrarRadio = new javax.swing.JRadioButton();
        AExecutarRadio = new javax.swing.JRadioButton();
        ABotaoOk = new javax.swing.JButton();
        ABotaoVoltar = new javax.swing.JButton();
        AbreArquivo = new javax.swing.JFileChooser();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jScrollPane1.setViewportView(AList);

        jLabel1.setText("Arquivos");

        
        //Radio Buttons
        radioButtonGroup.add(ACadastrarRadio);
        radioButtonGroup.add(AExecutarRadio);
        
        ACadastrarRadio.setText("Cadastrar");
        AExecutarRadio.setText("Executar");
        

        ABotaoOk.setText("OK");
        ABotaoVoltar.setText("Voltar");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(ACadastrarRadio)
                        .addGap(18, 18, 18)
                        .addComponent(AExecutarRadio)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(ABotaoVoltar, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(ABotaoOk, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 475, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(238, 238, 238)
                                .addComponent(jLabel1)))
                        .addGap(0, 51, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(40, 40, 40)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 327, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 17, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ACadastrarRadio)
                    .addComponent(AExecutarRadio)
                    .addComponent(ABotaoOk)
                    .addComponent(ABotaoVoltar))
                .addContainerGap())
        );

        pack();
    }

    private void ACadastrarRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ACadastrarRadioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ACadastrarRadioActionPerformed

    /**
     * @param args the command line arguments
     */
    public void inicia() {
 
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

      
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton ABotaoOk;
    public javax.swing.JButton ABotaoVoltar;
    public javax.swing.JRadioButton ACadastrarRadio;
    public javax.swing.JRadioButton AExecutarRadio;
    public javax.swing.JList AList;
    private javax.swing.ButtonGroup radioButtonGroup;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    public javax.swing.JFileChooser AbreArquivo;
    // End of variables declaration//GEN-END:variables
}