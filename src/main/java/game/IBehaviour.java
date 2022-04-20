package game;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

public interface IBehaviour {
    public Position moveToNextPosition(IGameObject jObj, ConcurrentLinkedQueue<IGameObject> jObjs);
    public Position approachTo(Position position, Position p);
    public ArrayList<Blossom> getBlossoms(ConcurrentLinkedQueue<IGameObject> jObjs);
}
