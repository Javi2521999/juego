/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.objects;

import org.json.JSONObject;

import java.util.Random;


/**
 *
 * @author juanangel //movimientos y cargar fly 
 */
public class Fly extends AbstractGameObject{
   int mode = 3;
    public Fly(){}
    
    public Fly(Position position) {
        super(position);    
    }
      
    public Fly(Position position, int value){
        super(position, value, 1);
    }
    
    public Fly(Position position, int value, int life){
        super(position, value, life);
    }
    
    public Fly(JSONObject obj){
        super(obj);
    }

     @Override
    public Position moveToNextPosition(){
        this.position = this.getBehaviour().moveToNextPosition(this, this.gObjs);
        return this.position;
    }
}
