/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 *
 * @author juanangel
 */// cargar blossom y posicion en el tablero 
public class Bee extends AbstractGameObject {

    ConcurrentLinkedQueue<IGameObject> gObjs = new ConcurrentLinkedQueue<>();
    public Bee(){}
    
    public Bee(Position position) {
        super(position);    
    }
      
    public Bee(Position position, int value){
        super(position, value, 1);
    }
    
    public Bee(Position position, int value, int life, ConcurrentLinkedQueue<IGameObject> gObjs ){
        super(position, value, life);
        this.gObjs = gObjs;
    }
    
    public Bee(JSONObject obj){
        super(obj);
    }    
    
    public void printBee(){
        System.out.println(this.toJSONObject());
    }

    @Override
// ir hacia las blossom
    public Position moveToNextPosition(){

        ArrayList<Blossom> blossoms = getBlossoms();
        IGameObject target = AbstractGameObject.getClosest(this, blossoms);
        approachTo(target.getPosition());

        return position;
    }

    private ArrayList<Blossom> getBlossoms(){
        ArrayList<Blossom> blossoms = new ArrayList<>();
        for (IGameObject obj: gObjs){
            if (obj instanceof Blossom){
                blossoms.add((Blossom) obj);
            }
        }
        return blossoms;
    }

    private void approachTo(Position p){
        if (position.x != p.x){
            position.x = position.x > p.x? position.x-1:position.x+1;
        }
        if (position.y != p.y){
            position.y = position.y > p.y? position.y-1:position.y+1;
        }
    }
    public void setBlossoms(ConcurrentLinkedQueue<IGameObject> gObjs){
        this.gObjs = gObjs;
    }

}
