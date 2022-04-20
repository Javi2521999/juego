package guis;

import common.*;
import game.*;
import sounds.GameSounds;
import views.IAWTGameView;
import views.IViewFactory;
import views.boxes.BoxesFactory;
import views.boxes.GeometricFactory;
import views.icons.IconsFactory;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import common.IToJsonObject;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;

public class GuiGame extends JFrame implements KeyListener, ActionListener {


    public int CANVAS_WIDTH = 480;
    public int boxSize = 40;

    public CanvasGame canvas;
    JPanel canvasFrame;
    JLabel positionLabel;

    // Declare new buttons
    JButton btClear, btStartStop;

    // Declare array of IGameObject and objects counter
    public ConcurrentLinkedQueue<IGameObject> gameObjects = new ConcurrentLinkedQueue<>();
    int itemsCounter;

    // Declare File menu and Save and Load items
    JMenuBar menuBar;
    JMenu menuFile, menuSettings, menuViews, menuMode;
    JMenuItem itSave, itLoad, itChangeWidth, itCustomSave, itCustomLoad;
    JCheckBoxMenuItem ckIcon, ckBoxes, ckGeo, ckAutomatic, ckManual;
    JLabel dataLabel;
    IViewFactory defaultViewFactory = new BoxesFactory();

    GuiChangeCanvasWidthJDialog guiChangeCanvasWidthJDialog;
    GameSounds gameSounds;
    public static String DEFAULT_PATH = "src/main/resources/games/default.txt";
    private Game game;
//nombre que sale arriba y los distintas configuraciones y que hacer con el juego si guardarlo o pausarlo manual authomatico  cambiar apariencia
    public GuiGame() throws Exception{

        super("Caperucita vs Bichos");

        this.gameSounds = new GameSounds();
        this.guiChangeCanvasWidthJDialog = new GuiChangeCanvasWidthJDialog(this);


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

//añadirlos
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
//color linea
        this.menuBar.setBorder(BorderFactory.createLineBorder(Color.blue));
        this.setJMenuBar(menuBar);

        JPanel pnControls = new JPanel();
        pnControls.setLayout(new GridLayout(2,1));
        JPanel pnButtons = new JPanel();
        pnButtons.add(btStartStop);
        pnButtons.add(btClear);
        pnControls.add(dataLabel);
        pnControls.add(pnButtons);
//dimension incial
        this.canvas = new CanvasGame(CANVAS_WIDTH, boxSize, this.defaultViewFactory);
        this.canvasFrame = new JPanel();
        this.canvasFrame.setPreferredSize(new Dimension(CANVAS_WIDTH + 40, CANVAS_WIDTH + 40));
        this.canvasFrame.add(canvas);
        this.getContentPane().add(canvasFrame);
        this.getContentPane().add(pnControls, BorderLayout.SOUTH);

        this.game = new Game(this, CANVAS_WIDTH, this.boxSize);
        this.setSize (CANVAS_WIDTH + 40, CANVAS_WIDTH + 160);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.addKeyListener(this);
        System.out.println(this.getFocusableWindowState());
        this.setFocusable(true);
    }
//añadir la propia dimension
    public void setCanvasWidth(int width) {
        this.setVisible(false);
        CANVAS_WIDTH = width * boxSize;
        this.getContentPane().remove(canvasFrame);
        this.canvas = new CanvasGame(CANVAS_WIDTH, boxSize, this.defaultViewFactory);
        this.canvasFrame = new JPanel();
        this.canvasFrame.setPreferredSize(new Dimension(CANVAS_WIDTH + 40, CANVAS_WIDTH + 40));
        this.canvasFrame.add(canvas);
        this.getContentPane().add(canvasFrame);
        this.setSize (CANVAS_WIDTH + 40, CANVAS_WIDTH + 160);
        this.setLocationRelativeTo(null);
        this.game.update();
        //this.game.startGame();
        this.setVisible(true);
    }

    public void updateInfo(String info) {
        this.dataLabel.setText(info);
    }

    private void printGameItems(){
        System.out.println("Objects Added to Game are: ");
        for (IGameObject obj: gameObjects){
            System.out.println( ( (IToJsonObject) obj).toJSONObject());
        }
    }

    @Override
    public void keyTyped(KeyEvent ke) {
    }

    // Version 1
    @Override
    public void keyPressed(KeyEvent ke) {
        int tecla = ke.getKeyCode();
        System.out.println("moving key");
        this.game.setTecla(tecla);

    }



    @Override
    public void keyReleased(KeyEvent ke) {
    }
//diferentes formas de ver las clases
    @Override
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        if(obj == ckGeo) {
            ckBoxes.setState(false);
            ckIcon.setState(false);
            ckGeo.setState(true);
            defaultViewFactory = new GeometricFactory();
            this.canvas.setDefaultViewFactory(defaultViewFactory);
            repaint();
        } else if(obj == ckIcon) {
            ckBoxes.setState(false);
            ckIcon.setState(true);
            ckGeo.setState(false);
            defaultViewFactory = new IconsFactory();
            this.canvas.setDefaultViewFactory(defaultViewFactory);
            repaint();
        } else if(obj == ckBoxes) {
            ckBoxes.setState(true);
            ckIcon.setState(false);
            ckGeo.setState(false);
            defaultViewFactory = new BoxesFactory();
            this.canvas.setDefaultViewFactory(defaultViewFactory);
            repaint();
            //cambiar dimensiones
        } else if(obj == itChangeWidth) {
            guiChangeCanvasWidthJDialog.setVisible(true);
            //boton reset level
        } else if(obj == btClear) {
            System.out.println("Reset level");
            btStartStop.setText("Start");
            this.game.resetLevel();
            //gameObjects.clear();
            //canvas.drawGameItems(gameObjects);
            requestFocusInWindow();
            // boton start 
        } else if(obj == btStartStop) {
            if (this.game.isRunning()){
                btStartStop.setText("Start");
                this.game.stopGame();
                this.menuFile.setEnabled(true);
                this.menuSettings.setEnabled(true);
            }
            //boton stop si esta jugando
            else{
                gameSounds.play();
                btStartStop.setText("Stop");
                this.game.startGame();
                this.menuFile.setEnabled(false);
                this.menuSettings.setEnabled(false);
            }
            repaint();
            // boton cargar partida
        } else if(obj == itCustomLoad){
            GameConfig config = FileGameSaveLoad.jFileChooserLoadConfig();
            this.loadNewConfig(config);
            /*this.defaultViewFactory = config.getDefaultViewFactory();
            this.CANVAS_WIDTH = config.getBoardWidth();
            if(config.getAutomatic()){
                this.ckAutomatic.setState(true);
                this.ckManual.setState(false);
            }else{
                this.ckAutomatic.setState(false);
                this.ckManual.setState(true);
            }
            if(config.getDefaultViewFactory() instanceof BoxesFactory){
                ckBoxes.setState(true);
                ckIcon.setState(false);
                ckGeo.setState(false);
            }else if(config.getDefaultViewFactory() instanceof GeometricFactory){
                ckBoxes.setState(false);
                ckIcon.setState(false);
                ckGeo.setState(true);
            }else{
                ckBoxes.setState(false);
                ckIcon.setState(true);
                ckGeo.setState(false);
            }
            this.guiChangeCanvasWidthJDialog.setCurrentWidth(config.getBoardWidth() / this.boxSize);
            this.setCanvasWidth(config.getBoardWidth() / this.boxSize);
            this.gameObjects = config.getgObjs();
            this.game.setGameConfig(config);
            if(gameObjects.size() > 0){
                printGameItems();
                canvas.drawGameItems(gameObjects);
            }
            requestFocusInWindow();*/
            //boton guardar partida
        } else if(obj == itCustomSave) {
            FileGameSaveLoad.jFileChooserSave(this.game.getGameConfig());
            requestFocusInWindow();
        } else if(obj == itLoad) {
            GameConfig gameConfig = FileGameSaveLoad.defaultLoadConfig(DEFAULT_PATH);
            this.loadNewConfig(gameConfig);
            /*if(gameObjects.size() > 0){
                printGameItems();
                canvas.drawGameItems(gameObjects);
            }
            requestFocusInWindow();*/
            //dudas
        }else if(obj == itSave) {
            FileGameSaveLoad.defaultSave(this.game.getGameConfig(), DEFAULT_PATH);
            requestFocusInWindow();
            //boton modo automatico
        }else if(obj == ckAutomatic) {
            ckAutomatic.setState(true);
            ckManual.setState(false);
            game.setAutomatic(true);
            //boton modo manual
        }else if(obj == ckManual){
            ckManual.setState(true);
            ckAutomatic.setState(false);
            game.setAutomatic(false);
        }

        this.game.update();
        requestFocusInWindow();

    }
//el juego nada mas arrancar????
    public void loadNewConfig(GameConfig config) {

        this.defaultViewFactory = config.getDefaultViewFactory();
        this.CANVAS_WIDTH = config.getBoardWidth();
        if(config.getAutomatic()){
            this.ckAutomatic.setState(true);
            this.ckManual.setState(false);
        }else{
            this.ckAutomatic.setState(false);
            this.ckManual.setState(true);
        }
        if(config.getDefaultViewFactory() instanceof BoxesFactory){
            ckBoxes.setState(true);
            ckIcon.setState(false);
            ckGeo.setState(false);
        }else if(config.getDefaultViewFactory() instanceof GeometricFactory){
            ckBoxes.setState(false);
            ckIcon.setState(false);
            ckGeo.setState(true);
        }else{
            ckBoxes.setState(false);
            ckIcon.setState(true);
            ckGeo.setState(false);
        }
        this.guiChangeCanvasWidthJDialog.setCurrentWidth(config.getBoardWidth() / this.boxSize);
        this.setCanvasWidth(config.getBoardWidth() / this.boxSize);
        this.gameObjects = config.getgObjs();
        this.game.setGameConfig(config);
        if(gameObjects.size() > 0){
            printGameItems();
            canvas.drawGameItems(gameObjects);
        }
        requestFocusInWindow();
    }
    public void updateCanvasDefaultViewFactory(IViewFactory defaultViewFactory){
        this.canvas.defaultViewFactory = defaultViewFactory;
    }
    public void setSquareCoordinates(int x, int y){
        this.canvas.setSquareCoordinates(x,y);
        
    }
    public IViewFactory getViewFactory(){
        return this.canvas.defaultViewFactory;
    } 
    public void drawGameItems(ConcurrentLinkedQueue<IGameObject> gObjects){
        this.canvas.drawGameItems(gObjects);
    }
            
}