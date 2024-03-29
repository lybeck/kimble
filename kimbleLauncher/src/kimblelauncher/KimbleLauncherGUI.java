package kimblelauncher;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;

/**
 *
 * @author Christoffer
 */
public class KimbleLauncherGUI extends javax.swing.JFrame {

    private String filePath = "";

    /**
     * Creates new form KimbleLauncherGUI
     */
    public KimbleLauncherGUI() {
        initComponents();

        this.setTitle("Kimble Launcher");
        this.setLocationRelativeTo(null);

//        File kimbleJarFile = FileUtil.findFile("Kimble.jar", "/");
//        if (kimbleJarFile != null) {
//            this.filePath = kimbleJarFile.getParent();
//            this.pathTextField.setText(filePath);
//        } else {
//            this.pathTextField.setText("");
//        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jFileChooser1 = new javax.swing.JFileChooser();
        startGameButton = new javax.swing.JButton();
        startPlaybackButton = new javax.swing.JButton();
        startTournamentGameButton = new javax.swing.JButton();
        pathTextField = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        startGameButton.setText("Start Game");
        startGameButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startGameButtonActionPerformed(evt);
            }
        });

        startPlaybackButton.setText("Start Playback");
        startPlaybackButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startPlaybackButtonActionPerformed(evt);
            }
        });

        startTournamentGameButton.setText("Start Tournament Game");
        startTournamentGameButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startTournamentGameButtonActionPerformed(evt);
            }
        });

        pathTextField.setText("D:/Programmering/Java/Kimble/kimble/kimble/dist/");

        jLabel1.setText("Path:");

        jButton1.setText("Browse");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(startGameButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(startPlaybackButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(startTournamentGameButton, javax.swing.GroupLayout.DEFAULT_SIZE, 534, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(pathTextField)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pathTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(jButton1))
                .addGap(18, 18, 18)
                .addComponent(startGameButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(startPlaybackButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(startTournamentGameButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    private void startGameButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startGameButtonActionPerformed
        try {
            startMainMethod(pathTextField.getText(), "Kimble.jar", "kimble.Kimble");
        } catch (IOException ex) {
            Logger.getLogger(KimbleLauncherGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_startGameButtonActionPerformed

    private void startPlaybackButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startPlaybackButtonActionPerformed
        try {
            startMainMethod(pathTextField.getText(), "Kimble.jar", "kimble.playback.PlaybackStarterGUI");
        } catch (IOException ex) {
            Logger.getLogger(KimbleLauncherGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_startPlaybackButtonActionPerformed

    private void startTournamentGameButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startTournamentGameButtonActionPerformed
        try {
            startMainMethod(pathTextField.getText(), "Kimble.jar", "kimble.connection.serverside.TestServer");
        } catch (IOException ex) {
            Logger.getLogger(KimbleLauncherGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_startTournamentGameButtonActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        jFileChooser1.setCurrentDirectory(new File(filePath));
        jFileChooser1.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnVal = jFileChooser1.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            pathTextField.setText(jFileChooser1.getSelectedFile().getAbsolutePath());
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void startMainMethod(String dir, String fileName, String mainClass) throws IOException {

        ProcessBuilder pb = new ProcessBuilder("java", "-cp", dir + File.separator + fileName, mainClass);
        pb.directory(new File(dir));
        Process p = pb.start();

        new Thread(new StreamRedirecter(p.getInputStream(), System.out)).start();
        new Thread(new StreamRedirecter(p.getErrorStream(), System.err)).start();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(KimbleLauncherGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(KimbleLauncherGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(KimbleLauncherGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(KimbleLauncherGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new KimbleLauncherGUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JFileChooser jFileChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JTextField pathTextField;
    private javax.swing.JButton startGameButton;
    private javax.swing.JButton startPlaybackButton;
    private javax.swing.JButton startTournamentGameButton;
    // End of variables declaration//GEN-END:variables
}
