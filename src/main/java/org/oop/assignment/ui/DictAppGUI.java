package org.oop.assignment.ui;

import org.oop.assignment.DictClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DictAppGUI extends JFrame {

    private JTextArea resultBox = new JTextArea(15, 80);
    private JTextField wordString = new JTextField(30);
    private DictClient client;
    private String dict = "fd-eng-lat";

    public DictAppGUI(DictClient client) {
        super("OOP Assignment - Dict Protocol Service");
        this.client = client;
        Container pane = this.getContentPane();
        Font f = new Font("Monospaced", Font.PLAIN, 12);
        resultBox.setFont(f);
        resultBox.setEditable(false);
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(1, 1, 10, 10));
        JScrollPane jsp = new JScrollPane(resultBox);
        centerPanel.add(jsp);
        pane.add("Center", centerPanel);
        JPanel northPanel = new JPanel();
        JPanel northPanelTop = new JPanel();
        northPanelTop.setLayout(new FlowLayout(FlowLayout.LEFT));
        northPanelTop.add(new JLabel("Word: "));
        northPanelTop.add("North", wordString);
        JButton translateButton = new JButton("Translate");
        northPanelTop.add(translateButton);
        northPanel.setLayout(new BorderLayout(2,1));
        northPanel.add("North", northPanelTop);
        JPanel northPanelBottom = new JPanel();
        northPanelBottom.setLayout(new GridLayout(1,3,5,5));
                
        northPanel.add("Center", northPanelBottom);
        pane.add("North", northPanel);

        ActionListener al = new TranslateWords();
        translateButton.addActionListener(al);
        wordString.addActionListener(al);
    }

    public static void main(String[] args) {
        DictClient dictClient = new DictClient();
        DictAppGUI app = new DictAppGUI(dictClient);
        app.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        app.pack();
        EventQueue.invokeLater(new FrameShower(app));
    }

    private record FrameShower(Frame frame) implements Runnable {
        @Override
        public void run() {
            frame.setVisible(true);
        }
    }

    private class TranslateWords implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            resultBox.setText("");
            SwingWorker<String, Object> worker = new Translate();
            worker.execute();
        }
    }

    private class Translate extends SwingWorker<String, Object> {
        @Override
        protected String doInBackground() throws Exception {
            String word = wordString.getText();
            return client.callTranslation(wordString.getText(), dict);
        }

        @Override
        protected void done() {
            try {
                resultBox.setText(get());
            } catch (Exception e) {
                JOptionPane.showMessageDialog(DictAppGUI.this,
                        e.getMessage(), "Translate Failed", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
