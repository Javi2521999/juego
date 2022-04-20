/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import common.IToJsonObject;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 *
 * @author juanangel
 */ // que pasa cuando se encuentra con ridingHood
public class Spider extends AbstractGameObject{
    RidingHood_3 ridingHood_3;
    public Spider(){}
    
    public Spider(Position position) {
        super(position);    
    }
      
    public Spider(Position position, int value){
        super(position, value, 1);
    }
    
    public Spider(Position position, int value, int life){
        super(position, value, life);
    }

    public Spider(Position position, int value, int life, RidingHood_3 ridingHood_3){
        super(position, value, life);
        this.ridingHood_3 = ridingHood_3;
    }
//aparecer otra spider dentro del tablero
    public Spider(JSONObject obj){
        super(obj);
    }

    public void printSpider(){
        System.out.println(this.toJSONObject());
    }
    public void setRidingHood_3(RidingHood_3 ridingHood_3){
        this.ridingHood_3 = ridingHood_3;
    }
    @Override
    public Position moveToNextPosition(){

        ArrayList<RidingHood_3> ridingHood_3s = new ArrayList<RidingHood_3>();
        ridingHood_3s.add(this.ridingHood_3);
        IGameObject target = AbstractGameObject.getClosest(this, ridingHood_3s);
        approachTo(target.getPosition());

        return position;
    }

    private void approachTo(Position p){
        if (position.x != p.x){
            position.x = position.x > p.x? position.x-1:position.x+1;
        }
        if (position.y != p.y){
            position.y = position.y > p.y? position.y-1:position.y+1;
        }
    }
    @Override
    public JSONObject toJSONObject() {
        JSONObject jObj = new JSONObject();
        jObj.put(IToJsonObject.TypeLabel, this.getClass().getSimpleName());
        jObj.put("value", this.value);
        jObj.put("lifes", this.lifes);
        jObj.put("mode", this.mode);
        jObj.put("position", this.position.toJSONObject());
        //jObj.put("ridingHood", this.ridingHood_3.toJSONObject());
        return jObj;
    }
           
}

