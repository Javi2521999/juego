/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import common.FileUtilities;
import common.IToJsonObject;
import static common.IToJsonObject.TypeLabel;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.filechooser.FileSystemView;

import guis.CampoDeTextoYDialogo;
import org.json.JSONArray;
import org.json.JSONObject;
import sounds.GameSounds;
import views.IAWTGameView;
import views.IViewFactory;
import views.boxes.BoxesFactory;
import views.boxes.GeometricFactory;
import views.boxes.VNumberedBox;
import views.boxes.VNumberedCircle;
import views.icons.IconsFactory;

/**
 *
 * @author juanangel
 */
public class GameEditor_4 extends JFrame implements KeyListener {
    
    public static final int UP_KEY    = 38;
    public static final int DOWN_KEY  = 40;
    public static final int RIGTH_KEY = 39;
    public static final int LEFT_KEY  = 37;
    
    public static int CANVAS_WIDTH = 480;
    
    int boxSize = 40;
    int row, col;

    // Timer
    Timer timer;
    int tick = 200;

    Canvas canvas;
    JPanel canvasFrame;
    JLabel positionLabel;
    
    // Declare new buttons
    JButton btAddClover, btAddDandelion, btAddSpider, btClear, btStartStop;
    
    // Declare array of IGameObject and objects counter
    ArrayList<IGameObject> gameObjects = new ArrayList<>();
    int itemsCounter;
    
    // Declare File menu and Save and Load items
    String path = "C:\\Users\\Manuel\\Downloads\\main p6\\main\\src\\resources\\games\\default.txt";
    JMenuBar menuBar;
    JMenu menuFile, menuSettings, menuViews;
    JMenuItem itSave, itLoad, itChangeWidth, itCustomSave, itCustomLoad;
    JCheckBoxMenuItem ckIcon, ckBoxes, ckGeo;

    IViewFactory defaultViewFactory = new BoxesFactory();

    CampoDeTextoYDialogo cp;
    GameSounds gameSounds;
    
    public GameEditor_4() throws Exception{

        super("Game Editor v3");
        gameSounds = new GameSounds();
        cp = new CampoDeTextoYDialogo(this);
        timer = new Timer(tick,
                new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent ae){

                        System.out.println("tick de reloj");
                    }
                });
        ///////////////////////////////////////////////////////////////////////////////////
        // Define Buttos and buttons handlers.............................................
        btAddClover = new JButton("Clover");
        btAddClover.addActionListener(
            new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent ae){
                    System.out.println("Clover selected");
                    gameObjects.add(new Blossom(new Position(col, row), 10, 1));
                    printGameItems();
                    canvas.drawGameItems(gameObjects);
                    requestFocusInWindow();
                }
            }
        );
        
        btAddDandelion = new JButton("Dandelion");
        btAddDandelion.addActionListener(
            new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent ae){
                    System.out.println("Dandelion selected");
                    gameObjects.add(new Blossom(new Position(col, row), 5, 1));
                    printGameItems();
                    canvas.drawGameItems(gameObjects);
                    requestFocusInWindow();
                }
            }
        );
        
        btAddSpider = new JButton("Spider");
        btAddSpider.addActionListener(
            new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent ae){
                    System.out.println("Spider selected");
                    gameObjects.add(new Spider(new Position(col, row), 5, 1));
                    printGameItems();
                    canvas.drawGameItems(gameObjects);
                    requestFocusInWindow();
                }
            }
        );
        
        btClear = new JButton("Clear All");
        btClear.addActionListener(
            new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent ae){
                    System.out.println("Cleaning all.");
                    gameObjects.clear();
                    canvas.drawGameItems(gameObjects);
                    requestFocusInWindow();
                }
            }
        );

        btStartStop = new JButton("Start");
        btStartStop.addActionListener(
                new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent ae){

                        if (timer.isRunning()){
                            timer.stop();
                            btStartStop.setText("Start");
                        }
                        else{
                            timer.start();
                            gameSounds.play();
                            btStartStop.setText("Stop");

                        }
                        repaint();
                    }
                }
        );
        
        /////////////////////////////////////////////////////////////////////////////////
        // Define menu and menu items and menu handlers.
        menuBar = new JMenuBar();
        menuFile = new JMenu("File");
        itSave = new JMenuItem("Default save");
        itLoad = new JMenuItem("Default load");

        itSave.addActionListener(
            new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent ae){
                    System.out.println("Saving objects");
                    if (gameObjects != null){
                        JSONObject jObjs [] = new JSONObject[gameObjects.size()];
                        for(int i = 0; i < jObjs.length; i++){
                            jObjs[i] = ((IToJsonObject)gameObjects.get(i)).toJSONObject();
                        }
                        FileUtilities.writeJsonsToFile(jObjs, path);
                    }
                    requestFocusInWindow();
                }
            }
        );
        
        itLoad.addActionListener(
            new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent ae){
                    System.out.println("Loading objects");
                    JSONArray jArray = FileUtilities.readJsonsFromFile(path);
                    if (jArray != null){
                        gameObjects = new ArrayList<>();
                        for (int i = 0; i < jArray.length(); i++){
                            JSONObject jObj = jArray.getJSONObject(i);
                            String typeLabel = jObj.getString(TypeLabel);
                            gameObjects.add(GameObjectsJSONFactory.getGameObject(jObj));
                        }
                        printGameItems(); 
                        canvas.drawGameItems(gameObjects);
                    }
                    requestFocusInWindow();
                }
            }
        );

        itCustomSave = new JMenuItem("Custom save");
        itCustomLoad = new JMenuItem("Custom load");

        itCustomSave.addActionListener(
                new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent ae){
                        System.out.println("Saving objects");
                        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

                        int returnValue = jfc.showOpenDialog(null);
                        // int returnValue = jfc.showSaveDialog(null);

                        if (returnValue == JFileChooser.APPROVE_OPTION) {
                            File selectedFile = jfc.getSelectedFile();
                            System.out.println(selectedFile.getAbsolutePath());
                            if (gameObjects != null){
                                JSONObject jObjs [] = new JSONObject[gameObjects.size()];
                                for(int i = 0; i < jObjs.length; i++){
                                    jObjs[i] = ((IToJsonObject)gameObjects.get(i)).toJSONObject();
                                }
                                FileUtilities.writeJsonsToFile(jObjs, selectedFile.getAbsolutePath());
                            }
                            requestFocusInWindow();
                        }

                    }
                }
        );

        itCustomLoad.addActionListener(
                new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent ae){
                        System.out.println("Loading objects");
                        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

                        int returnValue = jfc.showOpenDialog(null);
                        // int returnValue = jfc.showSaveDialog(null);

                        if (returnValue == JFileChooser.APPROVE_OPTION) {
                            File selectedFile = jfc.getSelectedFile();
                            System.out.println();
                            JSONArray jArray = FileUtilities.readJsonsFromFile(selectedFile.getAbsolutePath());
                            if (jArray != null){
                                gameObjects = new ArrayList<>();
                                for (int i = 0; i < jArray.length(); i++){
                                    JSONObject jObj = jArray.getJSONObject(i);
                                    String typeLabel = jObj.getString(TypeLabel);
                                    gameObjects.add(GameObjectsJSONFactory.getGameObject(jObj));
                                }
                                printGameItems();
                                canvas.drawGameItems(gameObjects);
                            }
                            requestFocusInWindow();
                        }

                    }
                }
        );
        menuSettings = new JMenu("Settings");
        menuViews = new JMenu("Views");
        itChangeWidth = new JMenuItem("Change width");
        ckIcon = new JCheckBoxMenuItem("Icon view factory", false);
        ckBoxes = new JCheckBoxMenuItem("Boxes view factory", true);
        ckGeo = new JCheckBoxMenuItem("Geo view factory", false);

        itChangeWidth.addActionListener(
                new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent ae){
                        cp.setVisible(true);
                    }
                }
        );

        ckBoxes.addActionListener(
                new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent ae){
                        ckBoxes.setState(true);
                        ckIcon.setState(false);
                        ckGeo.setState(false);
                        defaultViewFactory = new BoxesFactory();
                        repaint();
                    }
                }
        );
        ckIcon.addActionListener(
                new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent ae){
                        ckBoxes.setState(false);
                        ckIcon.setState(true);
                        ckGeo.setState(false);
                        defaultViewFactory = new IconsFactory();
                        repaint();
                    }
                }
        );
        ckGeo.addActionListener(
                new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent ae){
                        ckBoxes.setState(false);
                        ckIcon.setState(false);
                        ckGeo.setState(true);
                        defaultViewFactory = new GeometricFactory();
                        repaint();
                    }
                }
        );

        /////////////////////////////////////////////////////////////////////////////////
        // Add position label and buttons to the window.
            
        positionLabel = new JLabel("[" + col + ", " + row + "]");
        positionLabel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED)); 
        positionLabel.setPreferredSize(new Dimension(120,40));
        positionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JPanel pnControls = new JPanel();
        pnControls.setLayout(new GridLayout(2,1));
        JPanel pnButtons = new JPanel();
        pnButtons.add(btAddClover);
        pnButtons.add(btAddDandelion);
        pnButtons.add(btAddSpider);
        pnButtons.add(btClear);
        pnButtons.add(btStartStop);

        
        pnControls.add(positionLabel);
        pnControls.add(pnButtons);

        /*menuSettings.addActionListener(
                new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent ae){
                        System.out.println("Changing sizes");
                        setVisible(false);
                        defaultViewFactory = new IconsFactory();
                        int numOfColumns = 7;
                        int new_width= numOfColumns * boxSize;
                        getContentPane().remove(canvasFrame);
                        canvas = new Canvas(new_width, boxSize);
                        canvas.setPreferredSize(new Dimension(new_width, new_width));
                        canvas.setBorder(BorderFactory.createLineBorder(Color.blue));

                        canvasFrame = new JPanel();
                        canvasFrame.setPreferredSize(new Dimension(new_width + 40, new_width + 40));
                        canvasFrame.add(canvas);
                        getContentPane().add(canvasFrame);
                        getContentPane().add(pnControls, BorderLayout.SOUTH);
                        setVisible(true);


                    }
                }
        );*/
        canvas = new Canvas(CANVAS_WIDTH, boxSize);
        canvas.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_WIDTH));
        canvas.setBorder(BorderFactory.createLineBorder(Color.blue));
       
        canvasFrame = new JPanel();
        canvasFrame.setPreferredSize(new Dimension(CANVAS_WIDTH + 40, CANVAS_WIDTH + 40));
        canvasFrame.add(canvas);
        getContentPane().add(canvasFrame);
        getContentPane().add(pnControls, BorderLayout.SOUTH);

        menuViews.add(ckBoxes);
        menuViews.add(ckGeo);
        menuViews.add(ckIcon);
        menuSettings.add(menuViews);
        menuSettings.add(itChangeWidth);
        menuFile.add(itSave);
        menuFile.add(itLoad);
        menuFile.add(itCustomSave);
        menuFile.add(itCustomLoad);
        menuBar.add(menuFile);
        menuBar.add(menuSettings);

        menuBar.setBorder(BorderFactory.createLineBorder(Color.blue));
        setJMenuBar(menuBar);             
        
        setSize (CANVAS_WIDTH + 40, CANVAS_WIDTH + 160);
        setResizable(false);
        setVisible(true);         
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);    
       
        addKeyListener(this);
        System.out.println(this.getFocusableWindowState());
        this.setFocusable(true);
    }

    public void setCanvasWidth(int width) {
        CANVAS_WIDTH = width;
        setVisible(false);
        defaultViewFactory = new IconsFactory();
        int numOfColumns = width;
        int new_width= numOfColumns * boxSize;
        getContentPane().remove(canvasFrame);
        canvas = new Canvas(new_width, boxSize);
        canvas.setPreferredSize(new Dimension(new_width, new_width));
        canvas.setBorder(BorderFactory.createLineBorder(Color.blue));

        canvasFrame = new JPanel();
        canvasFrame.setPreferredSize(new Dimension(new_width + 40, new_width + 40));
        canvasFrame.add(canvas);
        getContentPane().add(canvasFrame);
        //getContentPane().add(pnControls, BorderLayout.SOUTH);
        setVisible(true);
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
        System.out.println("code --> " + tecla);
        switch (tecla) {
            case UP_KEY:  
                System.out.println("UP_KEY");
                row--;
                break;
            case DOWN_KEY:
                System.out.println("DOWN_KEY");
                row++;                    
                break;
            case RIGTH_KEY:
                System.out.println("RIGTH_KEY");
                col++;
                break;
            case LEFT_KEY:
                System.out.println("LEFT_KEY");
                col--;
                break; 
        }
        positionLabel.setText("[" + col + ", " + row + "]");
        setInLimits();
        canvas.setSquareCoordinates(col, row);  
    }

    
    private void setInLimits(){
        
        int lastBox = (CANVAS_WIDTH/boxSize) - 1;
        
        if (col < 0){
            col = 0;
        }
        else if ( col > lastBox ){
            col = lastBox;
        }
        
        if (row < 0){
            row = 0;
        }
        else if ( row > lastBox){
            row = lastBox;
        } 
    }

    @Override
    public void keyReleased(KeyEvent ke) {
    }
    
    public static void main(String [] args) throws Exception{
       GameEditor_4 gui = new GameEditor_4();
    }


    class Canvas extends JPanel {

        int size, boxSize;
        int pX, pY;
        
        ArrayList<IGameObject> objects = new ArrayList<>();

        public Canvas(int size, int boxSize){
            this.size = size;
            this.boxSize = boxSize;
        }

        public void setSquareCoordinates(int x, int y){
            pX = x;
            pY = y;
            repaint();
        }

        public void setSizes(int size, int boxSize) {
            this.size = size;
            this.boxSize = boxSize;
            repaint();
        }

        public void paintComponent(Graphics g){
            super.paintComponent(g);   
            drawGrid(g);
            //g.setColor(Color.red);
            //g.fillRect(pX*boxSize+4, pY*boxSize+4, boxSize-8, boxSize-8);
            drawSquare(g, pX, pY);
            try {
                drawGameItems(g);
            } catch (Exception ex) {
                Logger.getLogger(GameEditor_4.class.getName()).log(Level.SEVERE, null, ex);
            }
        }     

        private void drawGrid(Graphics g){
            Color c = g.getColor();
            g.setColor(Color.LIGHT_GRAY);
            int nLines = size/boxSize;
            System.out.println("---- " + nLines);
            for (int i = 1; i < nLines; i++){
               g.drawLine(i*boxSize, 0, i*boxSize, size);
               g.drawLine(0, i*boxSize, size, i*boxSize);
            } 
            g.setColor(c);
        }

        private void drawSquare(Graphics g, int xCoord, int yCoord) {

            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(Color.BLUE);
            g2.setStroke(new BasicStroke(2));
            g2.drawRect(xCoord*boxSize-2, yCoord*boxSize-2, boxSize+4, boxSize+4);
        }
        
        public void drawGameItems(ArrayList<IGameObject> objects){
            this.objects = objects;
            repaint();
        }
        
        private void drawGameItems(Graphics g) throws Exception{
            
            IAWTGameView view = null;
            
            if (objects != null){
                for (IGameObject obj: objects){
                    if (obj != null){
                        view = defaultViewFactory.getView(obj, boxSize);
                        view.draw(g);
                    }
                }
            }
        }
    }
}