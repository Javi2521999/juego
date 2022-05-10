package game;

import game.canvas.GamePanel;
import game.config.GameConfig;
import game.levels.LevelFactory;
import game.objects.*;
import guis.GuiGame;
import game.canvas.WinPanel;
import game.sounds.GameSounds;
import game.views.IViewFactory;
import game.views.boxes.BoxesFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

import static common.Constantes.*;

public class Game implements ActionListener {
    private GuiGame gui;
    private GameSounds gameSounds = new GameSounds();
    private GamePanel canvas;
    private GameConfig gameConfig = new GameConfig();
    private GameConfig gameConfigForResetLevel = new GameConfig();
    private WinPanel winPanel = new WinPanel(this);

    private int canvasWidth = 480;
    private int boxSize = 40;
    private int levelCounter = 0;
    private int roundCounter = 0;
    private int tick = 600;
    private int direction = UP_KEY;

    private Timer timer;

    private IViewFactory defaultViewFactory = new BoxesFactory();
    private ConcurrentLinkedQueue<IGameObject> gObjs = new ConcurrentLinkedQueue<>();
    private RidingHood ridingHood = new RidingHood(new Position(0,0), 1, 1);
    private RidingHood ridingHoodResetLevel = new RidingHood(new Position(0,0), 1, 1);
    private LevelFactory levelFactory = new LevelFactory(this.canvasWidth/boxSize, this.canvasWidth/boxSize);


    public Game(GuiGame gui) {
        this.winPanel.setVisible(false);
        this.gui = gui;
        this.timer = new Timer(tick, this);
        this.canvas = new GamePanel(this.canvasWidth, this.boxSize, this.defaultViewFactory);
        this.updateConfig();
        this.loadNewBoard();
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("tick");
        if(!ridingHood.automatic){
            this.updateRidingHoodDirection();
        }
        this.playGame();
    }

    // GuiGame
    public int getBoxSize() { return this.boxSize; }
    public int getCanvasWidth() {
        return this.canvasWidth;
    }
    public JPanel getCanvasFrame() {
        this.canvas = new GamePanel(this.canvasWidth, boxSize, this.defaultViewFactory);
        JPanel canvasFrame = new JPanel();
        canvasFrame.setPreferredSize(new Dimension(this.canvasWidth + 40, this.canvasWidth + 40));
        canvasFrame.add(this.canvas);
        this.canvas.repaint();
        canvasFrame.repaint();
        this.canvas.drawGameItems(this.gObjs);
        this.canvas.setSquareCoordinates(this.ridingHood.getPosition().getX(), this.ridingHood.getPosition().getY());
        return canvasFrame;
    }
    public GameConfig getGameConfig() {
        return this.gameConfig;
    }
    public boolean isRunning() { return timer.isRunning();}

    public void setDefaultViewFactory(IViewFactory defaultViewFactory) {
        this.defaultViewFactory = defaultViewFactory;
        this.canvas.setDefaultViewFactory(defaultViewFactory);
        this.canvas.repaint();
    }
    public void setGameConfig(GameConfig gameConfig) {
        this.gObjs.clear();
        this.gameConfig = gameConfig;
        this.canvasWidth = this.gameConfig.getBoardWidth();
        this.ridingHood = (RidingHood) this.gameConfig.getgObjs().stream().filter(x -> x instanceof RidingHood).collect(Collectors.toList()).get(0);
        this.ridingHood.setAutomatic(this.gameConfig.getAutomatic());
        this.ridingHood.setGameMode(2);
        this.ridingHood.gObjs = this.gameConfig.getgObjs();
        this.gObjs = this.gameConfig.getgObjs();
        this.levelCounter = this.gameConfig.getLevel();
        this.roundCounter = this.gameConfig.getRound();
        this.canvas.defaultViewFactory = this.gameConfig.getDefaultViewFactory();
        this.tick = this.gameConfig.getTimerTick();
        this.canvas.setSquareCoordinates(this.ridingHood.getPosition().getX(), this.ridingHood.getPosition().getY()); //canvas.setSquareCoordinates(this.ridingHood.getPosition().getX(), this.ridingHood.getPosition().getY());
        this.gObjs.forEach(x -> { x.setgObjs(this.gObjs);});
    }
    public void setMoveOfRidingHood(int direction){
        this.direction = direction;
    }
    public void setAutomatic(boolean automatic){
        this.ridingHood.setAutomatic(automatic);
        this.updateConfig();
    }

    public void updateConfig(){
        this.gameConfig.setBoardWidth(this.canvasWidth);
        this.gameConfig.setAutomatic(this.ridingHood.automatic);
        this.gameConfig.setgObjs(this.gObjs);
        this.gameConfig.setLevel(this.levelCounter);
        this.gameConfig.setRound(this.roundCounter);
        this.gameConfig.setDefaultViewFactory(this.defaultViewFactory);
        this.gameConfig.setTimerTick(this.tick);
    }
    public void updateCanvasWidth(int witdh) {
        this.canvasWidth = witdh * this.boxSize;
        this.levelFactory.update(this.canvasWidth/boxSize, this.canvasWidth/boxSize);
        this.updateConfig();
        this.canvas.repaint();
    }
    public void updateRidingHoodDirection(){
        switch (this.direction) {
            case UP_KEY:
                System.out.println("UP_KEY");
                this.ridingHood.moveUp();
                break;
            case DOWN_KEY:
                System.out.println("DOWN_KEY");
                this.ridingHood.moveDown();
                break;
            case RIGTH_KEY:
                System.out.println("RIGTH_KEY");
                this.ridingHood.moveRigth();
                break;
            case LEFT_KEY:
                System.out.println("LEFT_KEY");
                this.ridingHood.moveLeft();
                break;
        }
    }

    public void startGame() {
        this.timer.start();
    }
    private void playGame() {
        this.moveAllGameObjects();
        this.processCell();
        this.updateGameStatus();
    }
    public void continueGame(){
        this.gui.stopGame();
        this.loadNewBoard();
    }
    public void stopGame() {
        this.timer.stop();
    }

    private void moveAllGameObjects() {
        this.gObjs.stream().forEach(x -> {
            x.setgObjs(this.gObjs);
            x.moveToNextPosition();
            this.checkGuiLimits((AbstractGameObject)x);
        });
        this.canvas.setSquareCoordinates(this.ridingHood.getPosition().getX(), this.ridingHood.getPosition().getY());

    }
    private void processCell(){
        int process = 0;
        Position rhPos = this.ridingHood.getPosition();
        for (IGameObject gObj: this.gObjs){
            if(gObj != ridingHood && gObj instanceof Blossom && rhPos.isEqual(gObj.getPosition())){
                this.ridingHood.setValue(this.ridingHood.getValue() + gObj.getValue());
                this.gObjs.remove(gObj);
            } else if(gObj != this.ridingHood && (gObj instanceof Fly || gObj instanceof Bee) && rhPos.isEqual(gObj.getPosition())) {
                this.ridingHood.setValue(this.ridingHood.getValue() - gObj.getValue());
                this.gObjs.remove(gObj);
            } else if(gObj != this.ridingHood && gObj instanceof Spider && rhPos.isEqual(gObj.getPosition())) {
                this.ridingHood.incLifes(-gObj.getLifes());
                this.gObjs.remove(gObj);
            }
        }
        this.checkIfBeesEatBlossoms();

        if(this.ridingHood.getLifes() <= 0){
            process = -1;
        }else{
            process = this.gObjs.stream().filter(x -> x instanceof Blossom || x instanceof RidingHood).collect(Collectors.toList()).size();
        }

        if (process == 1){
            this.levelCounter++;
            if(this.levelCounter > 2) {
                this.levelCounter = 0;
                this.roundCounter += 1;
            }
            if(this.tick > 100) {
                this.tick-=20;
                this.timer.stop();
                this.timer.setDelay(this.tick);
                this.timer.start();
            }
            this.ridingHood.incLifes(1);
            this.stopGame();
            this.updateConfig();
            this.gameSounds.play(DEFAULT_SOUND);
            this.winPanel.setVisible(true);

        } else if(process == -1){
            this.ridingHood.setValue(0);
            this.ridingHood.incLifes(-ridingHood.getLifes());
            this.resetLevel();
            this.loadNewBoard();
        }
    }
    private void updateGameStatus() {
        this.gui.updateInfo(ridingHood.toString() + " Level: " + this.levelCounter + " Round: " + this.roundCounter );
        this.canvas.drawGameItems(gObjs);
        this.gui.repaint();
        this.updateConfig();
    }

    private void checkGuiLimits(AbstractGameObject obj){
        int lastBox = (canvasWidth/boxSize) - 1;
        if (obj instanceof Bee) {
            if (obj.getPosition().getX() < 0 || obj.getPosition().getX() > lastBox || obj.getPosition().getY() < 0 || obj.getPosition().getY() > lastBox){
                gObjs.remove(obj);
            }
        } else {
            if (obj.getPosition().getX() < 0){
                obj.getPosition().setX(0);
            }
            else if ( obj.getPosition().getX() > lastBox ){
                obj.getPosition().setX(lastBox) ;
            }

            if (obj.getPosition().getY() < 0){
                obj.getPosition().setY(0);
            }
            else if (obj.getPosition().getY() > lastBox){
                obj.getPosition().setY(lastBox);
            }
        }

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

    private void loadNewBoard(){
        this.gameConfigForResetLevel = new GameConfig(this.gameConfig.toJSONObject());
        this.ridingHoodResetLevel.setValue(this.ridingHood.getValue());
        this.ridingHoodResetLevel.setLifes(this.ridingHood.getLifes());
        this.gObjs.clear();
        this.ridingHood.setGameMode(2);
        this.gObjs.add(this.ridingHood);
        this.gObjs.addAll(this.levelFactory.getLevelScreen(this.gameConfig.getLevel(), this.gameConfig.getRound()));
        this.gObjs.forEach(x -> x.setgObjs(this.gObjs));
        this.updateConfig();
        this.canvas.drawGameItems(this.gObjs);
    }
    public void resetLevel() {
        this.gui.stopGame();
        this.gameConfig = new GameConfig(this.gameConfigForResetLevel.toJSONObject());
        this.ridingHood.setValue(this.ridingHoodResetLevel.getValue());
        this.ridingHood.setLifes(this.ridingHoodResetLevel.getLifes());
        this.loadNewBoard();
    }
     
    
}
