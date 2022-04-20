package game;

import org.json.JSONObject;

import java.util.Random;
//aparicion random de rocas
public class Rock extends AbstractGameObject{
    Random ran = new Random();

    public Rock(){}

    public Rock(Position position) {
        super(position);
    }

    public Rock(Position position, int value){
        super(position, value, 1);
    }

    public Rock(Position position, int value, int life){
        super(position, value, life);
    }

    public Rock(JSONObject obj){
        super(obj);
    }

    public void printRock(){
        System.out.println(this.toJSONObject());
    }

    @Override
    public Position moveToNextPosition(){
        return position;
    }
}
