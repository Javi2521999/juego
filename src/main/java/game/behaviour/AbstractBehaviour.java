package game.behaviour;

import game.objects.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

public abstract class AbstractBehaviour implements IBehaviour{
    protected ConcurrentLinkedQueue<IGameObject> jObjs;

    @Override
    public Position positionWithoutRocks(Position currentGameObjectPosition, Position targetGameObjectPostion, ConcurrentLinkedQueue<IGameObject> jObjs){
        List<Position> rocksPositions = this.getRocksPositions(jObjs);
        Position position =  new Position(currentGameObjectPosition.getX(), currentGameObjectPosition.getY());
        ArrayList<Position> posiblePosition = new ArrayList<>();
        for(int i = 0; i < 8; i++){
            switch(i) {
                case 0:
                    posiblePosition.add(new Position(position.getX()+1,  position.getY()+1));
                    break;
                case 1:
                    posiblePosition.add(new Position(position.getX()+1,  position.getY()-1));
                    break;
                case 2:
                    posiblePosition.add(new Position(position.getX(),  position.getY()+1));
                    break;
                case 3:
                    posiblePosition.add(new Position(position.getX(),  position.getY()-1));
                    break;
                case 4:
                    posiblePosition.add(new Position(position.getX()+1,  position.getY()));
                    break;
                case 5:
                    posiblePosition.add(new Position(position.getX()-1,  position.getY()-1));
                    break;
                case 6:
                    posiblePosition.add(new Position(position.getX()-1,  position.getY()+1));
                    break;
                case 7:
                    posiblePosition.add(new Position(position.getX()-1,  position.getY()));
                    break;
            }
        }
        ArrayList<Position> postionToRemove = new ArrayList<>();
        for(Position posible: posiblePosition) {
            for (Position pos : rocksPositions) {
                if (pos.isEqual(posible)) {
                    postionToRemove.add(posible);
                }
            }
        }
        for(Position pos: postionToRemove){
            posiblePosition.remove(pos);
        }

        double distance = Double.MAX_VALUE;
        for(Position position1: posiblePosition){
            double dist = this.calculateDistanceBetweenPoints(position1.getX(), position1.getY(), targetGameObjectPostion.getX(), targetGameObjectPostion.getY());
            if( dist < distance){
                distance = dist;
                position = position1;
            }
        }

        return position;
    }

    @Override
    public Position approachTo(Position position, Position p) {
        int x = position.getX();
        int y = position.getY();
        if (x != p.getX()){
            x = x > p.getX() ? x-1:x+1;
        }
        if (y != p.getY()){
            y = y > p.getY() ? y-1:y+1;
        }
        Position newPosition =  new Position(x, y);

        for(Position pos: this.getRocksPositions(this.jObjs)){
            if(pos.isEqual(newPosition)){
                newPosition = this.positionWithoutRocks(position, p, this.jObjs);
            }
        }
        return newPosition;
    }
    @Override
    public ArrayList<RidingHood> getRidingHood(ConcurrentLinkedQueue<IGameObject> jObjs) {
        ArrayList<RidingHood> ridingHoods = new ArrayList<>();
        for (IGameObject obj: jObjs){
            if (obj instanceof RidingHood){
                ridingHoods.add((RidingHood) obj);
            }
        }
        return ridingHoods;
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
    @Override
    public ArrayList<Rock> getRocks(ConcurrentLinkedQueue<IGameObject> jObjs) {
        ArrayList<Rock> rocks = new ArrayList<>();
        for (IGameObject obj: jObjs){
            if (obj instanceof Rock){
                rocks.add((Rock) obj);
            }
        }
        return rocks;
    }
    @Override
    public List<Position> getRocksPositions(ConcurrentLinkedQueue<IGameObject> jObjs){
        return this.getRocks(jObjs).stream().map(x -> x.getPosition()).collect(Collectors.toList());
    }
    @Override
    public double calculateDistanceBetweenPoints(double x1, double y1, double x2, double y2) {
        return Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));
    }
}
