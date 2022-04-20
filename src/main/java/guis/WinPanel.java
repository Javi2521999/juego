package guis;

import game.Game;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URL;

public class WinPanel extends JFrame {
//cuando se activa el win panel y de que fichero sale
    public WinPanel(Game game) {
        File url = new File("src/main/resources/images/you-win-8bit.gif");
        Icon icon = new ImageIcon(url.getAbsolutePath());
        JLabel label = new JLabel();
        label.setIcon(icon);
        JButton jButton = new JButton("Continue");
        jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                game.continueGame();
                setVisible(false); //you can't see me!
                dispose();
            }
        });
        JPanel jPanel = new JPanel();
        jPanel.add(label);
        setUndecorated(true);
        jPanel.setBackground(Color.black);
        getContentPane().add(label, BorderLayout.NORTH);
        getContentPane().add(jButton, BorderLayout.SOUTH);
        //getContentPane().add(jButton);
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        requestFocusInWindow();
    }

}
