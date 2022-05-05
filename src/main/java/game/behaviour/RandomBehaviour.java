package game.behaviour;

import game.objects.IGameObject;
import game.objects.Blossom;
import game.objects.Position;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

public class RandomBehaviour extends AbstractBehaviour {

    @Override
    public Position moveToNextPosition(IGameObject jObj, ConcurrentLinkedQueue<IGameObject> jObjs) {
        ArrayList<Blossom> blossoms = getBlossoms(jObjs);
        this.jObjs = jObjs;
        Position position = jObj.getPosition();
        if (blossoms != null && blossoms.size() != 0 ){
            //
            position = approachTo(jObj.getPosition(), this.getRandomBlossoms(blossoms).getPosition());
        }
        return position;
    }

    public Blossom getRandomBlossoms(ArrayList<Blossom> blossoms ) {
        Random r = new Random();
        int randomNumber = r.nextInt(blossoms.size());
        return blossoms.get(randomNumber);
    }
}