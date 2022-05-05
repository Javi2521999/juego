package guis;

import common.*;
import game.*;
import game.config.GameConfig;
import game.views.IViewFactory;
import game.views.boxes.BoxesFactory;
import game.views.boxes.GeometricFactory;
import game.views.icons.IconsFactory;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import static common.Constantes.DEFAULT_PATH;

public class GuiGame extends JFrame implements KeyListener, ActionListener {
    private JPanel canvasFrame, pnControls, pnButtons;
    private JButton btClear, btStartStop;
    private JMenuBar menuBar;
    private JMenu menuFile, menuSettings, menuViews, menuMode;
    private JMenuItem itSave, itLoad, itChangeWidth, itCustomSave, itCustomLoad;
    private JCheckBoxMenuItem ckIcon, ckBoxes, ckGeo, ckAutomatic, ckManual;
    private JLabel dataLabel;

    private GuiChangeCanvasWidthJDialog guiChangeCanvasWidthJDialog;


    private Game game;

    public GuiGame() throws Exception {
        super("Caperucita vs Bichos");
        this.dataLabel = new JLabel("Game info");
        this.dataLabel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        this.dataLabel.setPreferredSize(new Dimension(120,40));
        this.dataLabel.setHorizontalAlignment(SwingConstants.CENTER);

        this.btClear = new JButton("Reset level");
        this.btStartStop = new JButton("Start");

        this.menuBar = new JMenuBar();
        this.menuFile = new JMenu("File");
        this.menuSettings = new JMenu("Settings");
        this.menuViews = new JMenu("Views");
        this.menuMode = new JMenu("Mode");
        this.itSave = new JMenuItem("Default save");
        this.itLoad = new JMenuItem("Default load");
        this.itCustomSave = new JMenuItem("Custom save");
        this.itCustomLoad = new JMenuItem("Custom load");
        this.itChangeWidth = new JMenuItem("Change width");
        this.ckIcon = new JCheckBoxMenuItem("Icon view factory", false);
        this.ckBoxes = new JCheckBoxMenuItem("Boxes view factory", true);
        this.ckGeo = new JCheckBoxMenuItem("Geo view factory", false);
        this.ckAutomatic = new JCheckBoxMenuItem("Automatic", true);
        this.ckManual = new JCheckBoxMenuItem("Manual", false);

        this.btStartStop.addActionListener(this);
        this.btClear.addActionListener(this);
        this.itSave.addActionListener(this);
        this.itLoad.addActionListener(this);
        this.itCustomSave.addActionListener(this);
        this.itCustomLoad.addActionListener(this);
        this.itChangeWidth.addActionListener(this);
        this.ckBoxes.addActionListener(this);
        this.ckIcon.addActionListener(this);
        this.ckGeo.addActionListener(this);
        this.ckAutomatic.addActionListener(this);
        this.ckManual.addActionListener(this);

        this.menuViews.add(ckBoxes);
        this.menuViews.add(ckGeo);
        this.menuViews.add(ckIcon);
        this.menuMode.add(ckAutomatic);
        this.menuMode.add(ckManual);
        this.menuSettings.add(menuViews);
        this.menuSettings.add(menuMode);
        this.menuSettings.add(itChangeWidth);
        this.menuFile.add(itSave);
        this.menuFile.add(itLoad);
        this.menuFile.add(itCustomSave);
        this.menuFile.add(itCustomLoad);
        this.menuBar.add(menuFile);
        this.menuBar.add(menuSettings);

        this.menuBar.setBorder(BorderFactory.createLineBorder(Color.blue));
        this.setJMenuBar(menuBar);

        this.game = new Game(this);
        this.guiChangeCanvasWidthJDialog = new GuiChangeCanvasWidthJDialog(this);
        this.canvasFrame = this.game.getCanvasFrame();
        this.getContentPane().add(canvasFrame);

        this.pnControls = new JPanel();
        this.pnControls.setLayout(new GridLayout(2,1));
        this.pnButtons = new JPanel();
        this.pnButtons.add(btStartStop);
        this.pnButtons.add(btClear);
        this.pnControls.add(dataLabel);
        this.pnControls.add(pnButtons);
        this.getContentPane().add(pnControls, BorderLayout.SOUTH);

        this.setSize (this.game.getCanvasWidth() + 40, this.game.getCanvasWidth() + 160);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.updateCanvas();
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.addKeyListener(this);
        this.setFocusable(true);

    }

    public void updateCanvas() {
        this.setVisible(false);
        this.getContentPane().remove(this.canvasFrame);
        this.canvasFrame = this.game.getCanvasFrame();
        this.getContentPane().add(canvasFrame);
        this.setSize (this.game.getCanvasWidth() + 40, this.game.getCanvasWidth() + 160);
        this.setLocationRelativeTo(null);
        this.game.updateConfig();
        this.setVisible(true);
    }

    public void updateCanvasWidth(int width) {
        this.game.updateCanvasWidth(width);
        this.updateCanvas();
        this.repaint();
    }

    public void updateInfo(String info) {
        this.dataLabel.setText(info);
    }


    @Override
    public void keyTyped(KeyEvent ke) {
    }

    // Version 1
    @Override
    public void keyPressed(KeyEvent ke) {
        this.game.setMoveOfRidingHood(ke.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent ke) {
    }

    private void setViewsControl(Boolean boxes, Boolean icon, Boolean geo, IViewFactory viewFactory) {
        this.ckBoxes.setState(boxes);
        this.ckIcon.setState(icon);
        this.ckGeo.setState(geo);
        this.game.setDefaultViewFactory(viewFactory);
        //this.updateCanvas();
    }
//diferentes formas de ver las clases
    @Override
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        if(obj == ckGeo) {
            this.setViewsControl(false, false, true, new GeometricFactory());
        } else if(obj == ckIcon) {
            this.setViewsControl(false, true, false, new IconsFactory());
        } else if(obj == ckBoxes) {
            this.setViewsControl(true, false, false, new BoxesFactory());
        } else if(obj == itChangeWidth) {
            this.guiChangeCanvasWidthJDialog.setVisible(true);
        } else if(obj == btClear) {
            this.btStartStop.setText("Start");
            this.game.resetLevel();
        } else if(obj == btStartStop) {
            if (this.game.isRunning()){
                this.stopGame();
            } else{
                this.btStartStop.setText("Stop");
                this.game.startGame();
                this.menuFile.setEnabled(false);
                this.menuSettings.setEnabled(false);
            }
        } else if(obj == itCustomLoad){
            this.loadNewConfig(FileGameSaveLoad.jFileChooserLoadConfig());
        } else if(obj == itCustomSave) {
            FileGameSaveLoad.jFileChooserSave(this.game.getGameConfig());
        } else if(obj == itLoad) {
            this.loadNewConfig(FileGameSaveLoad.defaultLoadConfig(DEFAULT_PATH));
        }else if(obj == itSave) {
            FileGameSaveLoad.defaultSave(this.game.getGameConfig(), DEFAULT_PATH);
        }else if(obj == ckAutomatic) {
            this.ckAutomatic.setState(true);
            this.ckManual.setState(false);
            this.game.setAutomatic(true);
        }else if(obj == ckManual){
            this.ckManual.setState(true);
            this.ckAutomatic.setState(false);
            this.game.setAutomatic(false);
        }
        this.game.updateConfig();
        this.requestFocusInWindow();
    }

    public void stopGame() {
        this.btStartStop.setText("Start");
        this.game.stopGame();
        this.menuFile.setEnabled(true);
        this.menuSettings.setEnabled(true);
    }
    private void loadNewConfig(GameConfig config) {
        this.game.setGameConfig(config);
        if(config.getAutomatic()){
            this.ckAutomatic.setState(true);
            this.ckManual.setState(false);
        }else{
            this.ckAutomatic.setState(false);
            this.ckManual.setState(true);
        }
        if(config.getDefaultViewFactory() instanceof BoxesFactory){
            this.ckBoxes.setState(true);
            this.ckIcon.setState(false);
            this.ckGeo.setState(false);
        }else if(config.getDefaultViewFactory() instanceof GeometricFactory){
            this.ckBoxes.setState(false);
            this.ckIcon.setState(false);
            this.ckGeo.setState(true);
        }else{
            this.ckBoxes.setState(false);
            this.ckIcon.setState(true);
            this.ckGeo.setState(false);
        }
        this.guiChangeCanvasWidthJDialog.setCurrentWidth(config.getBoardWidth() / this.game.getBoxSize());
        this.updateCanvas();
        this.requestFocusInWindow();
    }


}