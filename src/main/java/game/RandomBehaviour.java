package game;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

public class RandomBehaviour implements IBehaviour{
    int blossomCounter = 0;
    @Override
    public Position moveToNextPosition(IGameObject jObj, ConcurrentLinkedQueue<IGameObject> jObjs) {
        ArrayList<Blossom> blossoms = getBlossoms(jObjs);
        Position position = jObj.getPosition();
        if (blossoms != null && blossoms.size() != 0 ){
            position = approachTo(jObj.getPosition(), this.getRandomBlossoms(blossoms).position);
        }
        return position;
    }

    @Override
    public Position approachTo(Position position, Position p) {
        int x = position.x;
        int y = position.y;
        if (position.x != p.x){
            x = position.x > p.x? position.x-1:position.x+1;
        }
        if (position.y != p.y){
            y = position.y > p.y? position.y-1:position.y+1;
        }
        return new Position(x, y);
    }

    @Override
    public ArrayList<Blossom> getBlossoms(ConcurrentLinkedQueue<IGameObject> jObjs) {
        ArrayList<Blossom> blossoms = new ArrayList<>();
        for (IGameObject obj: jObjs){
            if (obj instanceof Blossom){
                blossoms.add((Blossom) obj);
            }
        }
        return blossoms;
    }
    public Blossom getRandomBlossoms(ArrayList<Blossom> blossoms ) {
        Random r = new Random();
        int randomNumber = r.nextInt(blossoms.size());
        return blossoms.get(randomNumber);
    }
}