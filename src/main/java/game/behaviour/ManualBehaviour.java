package game.behaviour;

import game.objects.Fly;
import game.objects.IGameObject;
import game.objects.Position;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.json.JSONObject;
import game.objects.AbstractGameObject;
import game.objects.RidingHood;
public class ManualBehaviour extends AbstractBehaviour {

    @Override
    public Position moveToNextPosition(IGameObject jObj, ConcurrentLinkedQueue<IGameObject> jObjs) {
        
        Position position = jObj.getPosition();
        int x = position.getX();
        int y = position.getY();
       
        boolean isRock = false;
        Position nextPosition = new Position(position.getX(), position.getY());
        int x2 =  nextPosition.getX() + ((RidingHood)jObj).dX;
        int y2 = nextPosition.getY() + ((RidingHood)jObj).dY;
        nextPosition.setX(x2);
        nextPosition.setY(y2);
        for (Position pos : getRocksPositions(jObjs)) {
            if (pos.isEqual(nextPosition)) {
                isRock = true;
            }
        }
        if (!isRock) {
            position.setX(x2); 
            position.setY(y2);
        }
        return position;
    }

  
        
    }


