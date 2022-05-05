/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.objects;

import common.IToJsonObject;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 *
 * @author juanangel
 */ // que pasa cuando se encuentra con ridingHood
public class Spider extends AbstractGameObject{
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

    public Spider(JSONObject obj){
        super(obj);
    }


    @Override
    public Position moveToNextPosition(){

        this.position = this.getBehaviour().moveToNextPosition(this, this.gObjs);
        /*IGameObject target = AbstractGameObject.getClosest(this, ridingHood_3s);
        approachTo(target.getPosition());*/
        return position;
    }

    @Override
    public JSONObject toJSONObject() {
        JSONObject jObj = new JSONObject();
        jObj.put(IToJsonObject.TypeLabel, this.getClass().getSimpleName());
        jObj.put("value", this.value);
        jObj.put("lifes", this.lifes);
        jObj.put("mode", this.mode);
        jObj.put("position", this.position.toJSONObject());
        return jObj;
    }
           
}

