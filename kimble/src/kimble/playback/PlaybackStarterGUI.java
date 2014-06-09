package kimble.playback;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import kimble.connection.logger.LogFile;
import kimble.graphic.Screen;

/**
 *
 * @author Christoffer
 */
public class PlaybackStarterGUI extends javax.swing.JFrame {

    private File file;
    private Thread playbackThread;

    /**
     * Creates new form PlaybackStarterGUI
     */
    public PlaybackStarterGUI() {
        initComponents();
        initPlaybackProfileComboBox();
        loadLogFiles();

        this.setTitle("Playback Logs");
        this.setLocationRelativeTo(null);
    }

    private void initPlaybackProfileComboBox() {
        playbackProfileComboBox.removeAllItems();
        for (int i = 0; i < PlaybackProfile.values().length; i++) {
            playbackProfileComboBox.addItem(PlaybackProfile.values()[i]);
        }
        playbackProfileComboBox.setSelectedItem(PlaybackProfile.FAST);
    }

    private void loadLogFiles() {
        File dir = new File("logs");
        if (!dir.exists()) {
            infoTextArea.append("logs directory doesn't exist. Run the project with logger enabled!" + "\n");
            startButton.setEnabled(false);
            return;
        }

        if (!dir.isDirectory()) {
            infoTextArea.append("logs is not a directory!" + "\n");
            startButton.setEnabled(false);
            return;
        }

        File[] files = dir.listFiles();

        if (files == null || files.length == 0) {
            infoTextArea.append("No log files in logs/. Run the project with logger enabled!" + "\n");
            startButton.setEnabled(false);
            return;
        }

        logFileList.setListData(files);
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        logFileList = new javax.swing.JList();
        jPanel1 = new javax.swing.JPanel();
        startButton = new javax.swing.JButton();
        playbackProfileComboBox = new javax.swing.JComboBox();
        jScrollPane2 = new javax.swing.JScrollPane();
        infoTextArea = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        logFileList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                logFileListMouseClicked(evt);
            }
        });
        logFileList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                logFileListValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(logFileList);

        startButton.setText("Start Selected");
        startButton.setEnabled(false);
        startButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(startButton, javax.swing.GroupLayout.DEFAULT_SIZE, 153, Short.MAX_VALUE)
                    .addComponent(playbackProfileComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(startButton)
                .addGap(18, 18, 18)
                .addComponent(playbackProfileComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        infoTextArea.setColumns(20);
        infoTextArea.setRows(5);
        jScrollPane2.setViewportView(infoTextArea);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 414, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 216, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 128, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void startButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startButtonActionPerformed
        playbackThread = new Thread(new Runnable() {

            @Override
            public void run() {
                infoTextArea.append("Started: " + file.getAbsolutePath() + "\n");
                try {
                    LogFile log = new Gson().fromJson(new BufferedReader(new FileReader(file)), LogFile.class);
                    PlaybackLogic logic = new PlaybackLogic(log);

                    Screen.setupNativesLWJGL();
                    PlaybackGraphic graphic = new PlaybackGraphic(logic, (PlaybackProfile) playbackProfileComboBox.getSelectedItem());
                    graphic.start();

                } catch (FileNotFoundException ex) {
                    Logger.getLogger(PlaybackStarterGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        playbackThread.start();
        startButton.setEnabled(false);
    }//GEN-LAST:event_startButtonActionPerformed


    private void logFileListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_logFileListValueChanged
        if (playbackThread == null || !playbackThread.isAlive()) {
            file = (File) logFileList.getSelectedValue();
            startButton.setEnabled(true);
        }
    }//GEN-LAST:event_logFileListValueChanged

    private void logFileListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logFileListMouseClicked
        if (logFileList.getModel().getSize() == 1) {
            startButton.setEnabled(true);
        }
    }//GEN-LAST:event_logFileListMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /*
         * Set the Nimbus look and feel
         */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel. For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PlaybackStarterGUI.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /*
         * Create and display the form
         */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new PlaybackStarterGUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea infoTextArea;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JList logFileList;
    private javax.swing.JComboBox playbackProfileComboBox;
    private javax.swing.JButton startButton;
    // End of variables declaration//GEN-END:variables
}
