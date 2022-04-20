package game;

import common.FileGameSaveLoad;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

public class LevelFactory {

    int mX;
    int mY;
    int numberOfElements = 5;
    RidingHood_3 target;

    public LevelFactory(int mX, int mY, RidingHood_3 target) {
        this.mX = mX;
        this.mY = mY;
        this.target = target;
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
        levelObjects.forEach(x -> {
            if(x instanceof Bee){
                ((Bee) x).gObjs = levelObjects;
            }else if(x instanceof Spider){
                ((Spider) x).ridingHood_3 = this.target;
            }
        });
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
            levelObjects.add(this.getObject(level, position, levelObjects));
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

    public IGameObject getObject(Integer level, Position position,ConcurrentLinkedQueue<IGameObject> levelObjects) {
        IGameObject obj;
        if(level == 0){
            obj = new Fly(position, 4, 1);
        }else if(level == 1) {
            obj = new Bee(position, 10, 1, levelObjects);
        }else {
            obj = new Spider(position, 15, 1, this.target);
        }
        return obj;
    }

    public Position getRandomPosition(){
        int x = (int)(mX * Math.random());
        int y = (int)(mY * Math.random());
        return new Position(x, y);
    }
}
