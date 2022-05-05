package game.levels;

import common.FileGameSaveLoad;
import game.objects.RidingHood;
import game.objects.*;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

public class LevelFactory {

    int mX;
    int mY;
    int numberOfElements = 5;

    public LevelFactory(int mX, int mY) {
        this.mX = mX;
        this.mY = mY;
    }
    public void update(int mX, int mY){
        this.mX = mX;
        this.mY = mY;
    }
    public ConcurrentLinkedQueue<IGameObject> getLevelScreen(Integer level, Integer round) {
        ConcurrentLinkedQueue<IGameObject> levelObjects;
        if(round == 0){
            levelObjects = this.getLevelScreenRoundZero(level);
        }else{
            levelObjects = this.getLevelScreenRoundX(level, round);
        }
        return levelObjects;
    }

    public ConcurrentLinkedQueue<IGameObject> getLevelScreenRoundZero(Integer level) {
        String basePath = "src/main/resources/games/levels/game"+level+".txt";//"src/resources/games/levels/game" + level + ".txt";
        ConcurrentLinkedQueue<IGameObject> levelObjects = FileGameSaveLoad.defaultLoad(basePath);
        return levelObjects;
    }

    public ConcurrentLinkedQueue<IGameObject> getLevelScreenRoundX(Integer level, Integer round) {
        ArrayList<Position> currentPositions = new ArrayList<>();
        ConcurrentLinkedQueue<IGameObject> levelObjects = new ConcurrentLinkedQueue<>();
        int totalNumberOfObjects = (this.numberOfElements * round + level) / 2;
        if(totalNumberOfObjects >= (mX * mY)) totalNumberOfObjects = totalNumberOfObjects / 2;
        for(int i = 0; i < totalNumberOfObjects; i++){
            Position position = getRandomPosition();
            while(currentPositions.contains(position)) {
                position = getRandomPosition();
            }
            currentPositions.add(position);
            levelObjects.add(this.getObject(level, position));
        }
        for(int i = 0; i < totalNumberOfObjects; i++){
            Position position = getRandomPosition();
            while(currentPositions.contains(position)) {
                position = getRandomPosition();
            }
            currentPositions.add(position);
            levelObjects.add(new Blossom(position,4, 10));
        }
        for(int i = 0; i < totalNumberOfObjects; i++) {
            Position position = getRandomPosition();
            while (currentPositions.contains(position)) {
                position = getRandomPosition();
            }
            currentPositions.add(position);
            levelObjects.add(new Rock(position, 4, 10));
        }
        return levelObjects;
    }

    public IGameObject getObject(Integer level, Position position) {
        IGameObject obj;
        if(level == 0){
            obj = new Fly(position, 4, 1);
        }else if(level == 1) {
            obj = new Bee(position, 10, 1);
        }else {
            obj = new Spider(position, 15, 1);
        }
        return obj;
    }

    public Position getRandomPosition(){
        int x = (int)(mX * Math.random());
        int y = (int)(mY * Math.random());
        return new Position(x, y);
    }
}
