package game;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ClosestBehaviour implements IBehaviour {
    @Override
    public Position moveToNextPosition(IGameObject jObj, ConcurrentLinkedQueue<IGameObject> jObjs) {
        ArrayList<Blossom> blossoms = getBlossoms(jObjs);
        IGameObject target = AbstractGameObject.getClosest(jObj, blossoms);
        return approachTo(jObj.getPosition(),target.getPosition());
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
}
