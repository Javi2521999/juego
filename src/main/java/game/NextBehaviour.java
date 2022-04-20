package game;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

public class NextBehaviour implements IBehaviour{
    int blossomCounter = 0;
    ArrayList<Rock> rocks = new ArrayList<>();
    @Override
    public Position moveToNextPosition(IGameObject jObj, ConcurrentLinkedQueue<IGameObject> jObjs) {
        ArrayList<Blossom> blossoms = getBlossoms(jObjs);
        this.rocks = this.getRocks(jObjs);
        Position position = jObj.getPosition();
        if (blossoms != null && blossoms.size() != 0 ){
            position = approachTo(jObj.getPosition(), blossoms.get(blossomCounter).position);
        }
        return position;
    }

    @Override
    public Position approachTo(Position position, Position p) {
        ArrayList<Position> rocksPositions = new ArrayList<>();
        this.rocks.forEach(x -> rocksPositions.add(x.getPosition()));
        int x = position.x;
        int y = position.y;
        if (position.x != p.x){
            x = position.x > p.x? position.x-1:position.x+1;
        }
        if (position.y != p.y){
            y = position.y > p.y? position.y-1:position.y+1;
        }
        Position newPosition =  new Position(x, y);

        boolean check = true;
        for(Position pos: rocksPositions){
            if(pos.isEqual(newPosition)){
                newPosition = positionWithoutRocks(position, rocksPositions, p);
            }
        }
        return newPosition;
    }

    public double calculateDistanceBetweenPoints(
            double x1,
            double y1,
            double x2,
            double y2) {
        return Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));
    }

    public Position positionWithoutRocks(Position hoodPosition, ArrayList<Position> rocksPositions, Position blossomPosition){
        Position position =  new Position(hoodPosition.getX(), hoodPosition.getY());
        ArrayList<Position> posiblePosition = new ArrayList<>();

        for(int i = 0; i < 8; i++){
            switch(i) {
                case 0:
                    posiblePosition.add(new Position(position.x+1,  position.y+1));
                    break;
                case 1:
                    posiblePosition.add(new Position(position.x+1,  position.y-1));
                    break;
                case 2:
                    posiblePosition.add(new Position(position.x,  position.y+1));
                    break;
                case 3:
                    posiblePosition.add(new Position(position.x,  position.y-1));
                    break;
                case 4:
                    posiblePosition.add(new Position(position.x+1,  position.y));
                    break;
                case 5:
                    posiblePosition.add(new Position(position.x-1,  position.y-1));
                    break;
                case 6:
                    posiblePosition.add(new Position(position.x-1,  position.y+1));
                    break;
                case 7:
                    posiblePosition.add(new Position(position.x-1,  position.y));
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
            double dist = calculateDistanceBetweenPoints(position1.getX(), position1.getY(), blossomPosition.getX(), blossomPosition.getY());
            if( dist < distance){
                distance = dist;
                position = position1;
            }
        }



        return position;
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

    public ArrayList<Rock> getRocks(ConcurrentLinkedQueue<IGameObject> jObjs) {
        ArrayList<Rock> rocks = new ArrayList<>();
        for (IGameObject obj: jObjs){
            if (obj instanceof Rock){
                rocks.add((Rock) obj);
            }
        }
        return rocks;
    }
}
