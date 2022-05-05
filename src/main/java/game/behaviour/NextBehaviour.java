package game.behaviour;

import game.objects.IGameObject;
import game.objects.Blossom;
import game.objects.Position;
import game.objects.Rock;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

public class NextBehaviour extends AbstractBehaviour{

    @Override
    public Position moveToNextPosition(IGameObject jObj, ConcurrentLinkedQueue<IGameObject> jObjs) {
        this.jObjs = jObjs;
        int blossomCounter = 0;
        ArrayList<Blossom> blossoms = this.getBlossoms(jObjs);
        Position position = jObj.getPosition();

        if (blossoms != null && blossoms.size() != 0 ){
            position = approachTo(jObj.getPosition(), blossoms.get(blossomCounter).getPosition());
        }
        return position;
    }
}
