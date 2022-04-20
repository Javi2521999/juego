package game;

import guis.CanvasGame;
import guis.GuiGame;
import guis.WinPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;
//nose
public class Game implements ActionListener {

    public int canvasWidth;
    public int boxSize;
    private CanvasGame canvas;
    private GuiGame gui;
    ConcurrentLinkedQueue<IGameObject> gObjs = new ConcurrentLinkedQueue<>();
    RidingHood_3 ridingHood;// = new RidingHood_3(new Position(0,0), 1, 1, gObjs);
    int screenCounter = 0;
    int roundCounter = 0;
    // Timer
    Timer timer;
    int tick = 400;
    private LevelFactory levelFactory;
    public static final int UP_KEY    = 38;
    public static final int DOWN_KEY  = 40;
    public static final int RIGTH_KEY = 39;
    public static final int LEFT_KEY  = 37;
    int tecla = UP_KEY;
    int row, col;
    private GameConfig gameConfig = new GameConfig();

    public Game(GuiGame gui, CanvasGame canvas, int canvasWidth, int boxSize) throws Exception{
        this.gui = gui;
        this.canvas = canvas;
        this.canvasWidth = canvasWidth;
        this.boxSize = boxSize;
        ridingHood = new RidingHood_3(new Position(0,0), 1, 1, gObjs);
        gObjs.add(ridingHood);
        this.timer = new Timer(tick, this);
        int width = (canvasWidth/boxSize);
        this.levelFactory = new LevelFactory(width, width, this.ridingHood);
        loadNewBoard(screenCounter, roundCounter);
        this.updateConfig();

    }

    public GameConfig getGameConfig() {
        return this.gameConfig;
    }
//nose
    //la interaccion de las diferentes clases entre si
    public void setGameConfig(GameConfig gameConfig) {
        this.gObjs.clear();
        this.gameConfig = gameConfig;
        this.canvasWidth = this.gameConfig.getBoardWidth();
        this.ridingHood = (RidingHood_3) this.gameConfig.getgObjs().stream().filter(x -> x instanceof RidingHood_3).collect(Collectors.toList()).get(0);
        this.ridingHood.setAutomatic(this.gameConfig.getAutomatic());
        this.gObjs = this.gameConfig.getgObjs();
        this.screenCounter = this.gameConfig.getLevel();
        this.roundCounter = this.gameConfig.getRound();
        this.canvas.defaultViewFactory = this.gameConfig.getDefaultViewFactory();
        this.tick = this.gameConfig.getTimerTick();
        this.canvas.setSquareCoordinates(this.ridingHood.getPosition().getX(), this.ridingHood.getPosition().getY());
        this.gObjs.forEach(x -> {
            if(x instanceof Spider){
                ((Spider) x).setRidingHood_3(this.ridingHood);
            }else if(x instanceof Bee){
                ((Bee) x).setBlossoms(this.gObjs);
            }
        });
    }

    public void updateConfig(){
        this.gameConfig.setBoardWidth(this.canvasWidth);
        this.gameConfig.setAutomatic(this.ridingHood.automatic);
        this.gameConfig.setgObjs(this.gObjs);
        this.gameConfig.setLevel(this.screenCounter);
        this.gameConfig.setRound(this.roundCounter);
        this.gameConfig.setDefaultViewFactory(this.canvas.defaultViewFactory);
        this.gameConfig.setTimerTick(this.tick);
    }

    public void setTecla(int tecla){
        this.tecla = tecla;
    }
    public void update() {
        this.canvas = this.gui.canvas;
        this.canvasWidth = this.gui.CANVAS_WIDTH;
        this.boxSize = this.gui.boxSize;
        this.updateConfig();
        //this.gObjs = this.gui.gameObjects;
    }
    //modo automatico
    public void setAutomatic(boolean automatic){
        this.ridingHood.setAutomatic(automatic);
        this.updateConfig();
    }
    //movimientos de riddinghood con teclado
    public void moveRidingHood(){
        switch (tecla) {
            case UP_KEY:
                System.out.println("UP_KEY");
                row--;
                this.ridingHood.moveUp();
                break;
            case DOWN_KEY:
                System.out.println("DOWN_KEY");
                this.ridingHood.moveDown();
                row++;
                break;
            case RIGTH_KEY:
                System.out.println("RIGTH_KEY");
                col++;
                this.ridingHood.moveRigth();
                break;
            case LEFT_KEY:
                System.out.println("LEFT_KEY");
                col--;
                this.ridingHood.moveLeft();
                break;
        }
        //positionLabel.setText("[" + col + ", " + row + "]");
        setInLimits();

    }
    //contadores para movimientos de moscas arañas y las abejas limites
    public void continueGame(){
        this.startGame();
        loadNewBoard(screenCounter, roundCounter);
    }
    public void startGame() {
        this.timer.start();
    }
    public void stopGame() {
        this.timer.stop();
    }
    public boolean isRunning() { return timer.isRunning();}
    public void playGame() {
        System.out.println("tick");
        gObjs.stream().forEach(x -> {
            if(x instanceof Fly || x instanceof Spider){
                x.moveToNextPosition();
                setInLimitsGeneral((AbstractGameObject)x);
            }else if(x instanceof Bee){
                ((Bee) x).setBlossoms(this.gObjs);
                x.moveToNextPosition();
                setInLimitsBee((AbstractGameObject)x);
            }

        });
        //asegurar que riddinghood esta en una buena posicion sobre el atablero
        ridingHood.moveToNextPosition();
        setInLimits();
        canvas.setSquareCoordinates(ridingHood.getPosition().getX(), ridingHood.getPosition().getY());
        int process = processCell();
        if (process == 1){
            screenCounter++;
            if(screenCounter > 2) {
                screenCounter = 0;
                roundCounter += 1;
            }
            if(tick > 100) {
                tick-=20;
                timer.stop();
                timer.setDelay(tick);
                timer.start();

            }
            //activar panel ganador
            ridingHood.incLifes(1);
            this.stopGame();
            WinPanel winPanel = new WinPanel(this);

        } else if(process == -1){
            ridingHood.setValue(0);
            ridingHood.incLifes(-ridingHood.getLifes());
            loadNewBoard(screenCounter, roundCounter);
        }
        this.gui.updateInfo(ridingHood.toString());
        this.canvas.drawGameItems(gObjs);
        this.gui.repaint();
        this.updateConfig();
    }

    /*
    Procesa la celda en la que se encuentra caperucita.
    Si Caperucita está sobre un blossom añade su valor al de Caperucita
    y lo elimina del tablero.
    Devuelve el número de blossoms que hay en el tablero. y si las bee se las comen
    */
    private int processCell(){
        int response = 0;
        Position rhPos = ridingHood.getPosition();
        for (IGameObject gObj: gObjs){
            if(gObj != ridingHood && gObj instanceof Blossom && rhPos.isEqual(gObj.getPosition())){
                int v = ridingHood.getValue() + gObj.getValue();
                ridingHood.setValue(v);
                gObjs.remove(gObj);
            } else if(gObj != ridingHood && (gObj instanceof Fly || gObj instanceof Bee) && rhPos.isEqual(gObj.getPosition())) {
                int v = ridingHood.getValue() - gObj.getValue();
                ridingHood.setValue(v);
                gObjs.remove(gObj);
            } else if(gObj != ridingHood && gObj instanceof Spider && rhPos.isEqual(gObj.getPosition())) {
                ridingHood.incLifes(-gObj.getLifes());
                gObjs.remove(gObj);
            }
        }
        this.checkIfBeesEatBlossoms();
        if(ridingHood.getLifes() <= 0){
            response = -1;
        }else{
            response = gObjs.stream().filter(x -> x instanceof Blossom || x instanceof RidingHood_3).collect(Collectors.toList()).size();
        }
        return response;
    }

    public void checkIfBeesEatBlossoms() {
        List<IGameObject> bees = this.gObjs.stream().filter(x -> x instanceof Bee).collect(Collectors.toList());
        List<IGameObject> blossoms = this.gObjs.stream().filter(x -> x instanceof Blossom).collect(Collectors.toList());
        for (IGameObject bee: bees) {
            Position positionBee = bee.getPosition();
            for(IGameObject blossom: blossoms) {
                if(positionBee.isEqual(blossom.getPosition())) {
                    this.gObjs.remove(blossom);
                }
            }
        }
    }
    /*
    Comprueba que Caperucita no se sale del tablero.
    En caso contrario corrige su posición
    */
    private void setInLimits(){

        int lastBox = (canvasWidth/boxSize) - 1;

        if (ridingHood.getPosition().getX() < 0){
            ridingHood.position.x = 0;
        }
        else if ( ridingHood.getPosition().getX() > lastBox ){
            ridingHood.position.x = lastBox;
        }

        if (ridingHood.getPosition().getY() < 0){
            ridingHood.position.y = 0;
        }
        else if (ridingHood.getPosition().getY() > lastBox){
            ridingHood.position.y = lastBox;
        }
    }
    private void setInLimitsGeneral(AbstractGameObject obj){

        int lastBox = (canvasWidth/boxSize) - 1;

        if (obj.getPosition().getX() < 0){
            obj.position.x = 0;
        }
        else if ( obj.getPosition().getX() > lastBox ){
            obj.position.x = lastBox;
        }

        if (obj.getPosition().getY() < 0){
            obj.position.y = 0;
        }
        else if (obj.getPosition().getY() > lastBox){
            obj.position.y = lastBox;
        }
    }

    private void setInLimitsBee(AbstractGameObject obj){

        int lastBox = (canvasWidth/boxSize) - 1;

        if (obj.getPosition().getX() < 0 || obj.getPosition().getX() > lastBox || obj.getPosition().getY() < 0 || obj.getPosition().getY() > lastBox){
            gObjs.remove(obj);
        }
    }
    /*
    Carga un nuevo tablero
    */
    private void loadNewBoard(int counter, int roundCounter){
        this.gObjs.clear();
        this.ridingHood.setGameMode(1);
        this.gObjs.add(ridingHood);
        this.gObjs.addAll(this.levelFactory.getLevelScreen(counter, roundCounter));
        this.gObjs.forEach(x -> System.out.println(x.toString()));
        this.updateConfig();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(!ridingHood.automatic){
            this.moveRidingHood();
        }
        this.playGame();
    }

    public void resetLevel() {
        this.stopGame();
        this.loadNewBoard(this.screenCounter, this.roundCounter);
        this.canvas.drawGameItems(this.gObjs);

    }
    public void loadGame(ConcurrentLinkedQueue<IGameObject> gObjs){
        this.gObjs = gObjs;
    }
    public ConcurrentLinkedQueue<IGameObject> getGameToSave(){
        return this.gObjs;
    }
}
