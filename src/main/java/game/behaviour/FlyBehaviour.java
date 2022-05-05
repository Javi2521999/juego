package game.behaviour;

import game.objects.Fly;
import game.objects.IGameObject;
import game.objects.Position;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.json.JSONObject;

public class FlyBehaviour extends AbstractBehaviour {

    Random ran = new Random();

    @Override
    public Position moveToNextPosition(IGameObject jObj, ConcurrentLinkedQueue<IGameObject> jObjs) {
        Position position = jObj.getPosition();
        this.jObjs = jObjs;

        int randomMove = ran.nextInt(7);
        
        int x = position.getX();
        int y = position.getY();
        
        switch (randomMove) {
            case 0:
                x += 1;
                y += 1;
                break;
            case 1:
                x += 1;
                y -= 1;
                break;
            case 2:
                x = 1;
                y += 1;
                break;
            case 3:
                x = 1;
                y -= 1;
                break;
            case 4:
                x += 1;
                y = 1;
                break;
            case 5:
                x -= 1;
                y -= 1;
                break;
            case 6:
                x -= 1;
                y += 1;
                break;
            case 7:
                x -= 1;
                y = 1;
                break;
        }
        position.setX(x);
        position.setY(y);
        return position;
        
    }
}
