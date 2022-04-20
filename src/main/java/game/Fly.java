/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import org.json.JSONObject;

import java.util.Random;


/**
 *
 * @author juanangel //movimientos y cargar fly 
 */
public class Fly extends AbstractGameObject{
    Random ran = new Random();

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
    
    public void printFly(){
        System.out.println(this.toJSONObject());
    }

    @Override 
    // posibles movimientos de las fly
    public Position moveToNextPosition(){
        int randomMove = ran.nextInt(7);

        switch(randomMove) {
            case 0:
                position.x+=1;
                position.y+=1;
                break;
            case 1:
                position.x+=1;
                position.y-=1;
                break;
            case 2:
                position.x=1;
                position.y+=1;
                break;
            case 3:
                position.x=1;
                position.y-=1;
                break;
            case 4:
                position.x+=1;
                position.y=1;
                break;
            case 5:
                position.x-=1;
                position.y-=1;
                break;
            case 6:
                position.x-=1;
                position.y+=1;
                break;
            case 7:
                position.x-=1;
                position.y=1;
                break;
        }

        return position;
    }
}
