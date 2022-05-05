package game.behaviour;

import game.objects.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public interface IBehaviour {
    public Position moveToNextPosition(IGameObject jObj, ConcurrentLinkedQueue<IGameObject> jObjs);
    public Position approachTo(Position position, Position p);
    public ArrayList<Blossom> getBlossoms(ConcurrentLinkedQueue<IGameObject> jObjs);
    public ArrayList<RidingHood> getRidingHood(ConcurrentLinkedQueue<IGameObject> jObjs);
    public ArrayList<Rock> getRocks(ConcurrentLinkedQueue<IGameObject> jObjs);
    public List<Position> getRocksPositions(ConcurrentLinkedQueue<IGameObject> jObjs);
    public double calculateDistanceBetweenPoints(double x1, double y1, double x2, double y2);
    public Position positionWithoutRocks(Position currentGameObjectPosition, Position targetGameObjectPostion, ConcurrentLinkedQueue<IGameObject> jObjs);
}
