package game.behaviour;

import game.objects.*;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ClosestBehaviour extends AbstractBehaviour {
    @Override
    public Position moveToNextPosition(IGameObject jObj, ConcurrentLinkedQueue<IGameObject> jObjs) {
        this.jObjs = jObjs;
        ArrayList<? extends AbstractGameObject> targets;
        if(jObj instanceof Spider){
            targets = this.getRidingHood(jObjs);
        }else{
            targets = this.getBlossoms(jObjs);
        }
        IGameObject target = AbstractGameObject.getClosest(jObj, targets);
        return approachTo(jObj.getPosition(),target.getPosition());
    }
}
