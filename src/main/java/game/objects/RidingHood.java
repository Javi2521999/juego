/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.objects;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

import game.behaviour.BehaviourFactory;
import game.behaviour.IBehaviour;
import game.behaviour.AbstractBehaviour;
import org.json.JSONObject;

/**
 *
 * @author juanangel
 */
public class RidingHood extends AbstractGameObject {

    public int dX, dY;
    public boolean automatic = true;

    public RidingHood(Position position) {
        super(position);
    }

    public RidingHood(Position position, int value, int life) {
        super(position, value, life);
    }

    public RidingHood(JSONObject jObj) {
        super(jObj);
    }

    /**
     * Cada vez que se invoca se dirige hacia el siguiente blossom, moviéndose
     * una posición en x y otra en y. Cuando ha pasado por todos los blossoms
     * avanza en diagonal hacia abajo a las derecha.
     *
     * @return posición en la que se encuentra después de ejecutarse el método.
     */


    public void setAutomatic(boolean automatic) {
        this.automatic = automatic;
        if(this.automatic){
            this.setGameMode(2);
        }else{this.setGameMode(4);}

    }

    public void moveRigth() {
        dY = 0;
        dX = 1;
    }

    public void moveLeft() {
        dY = 0;
        dX = -1;
    }

    public void moveUp() {
        dY = -1;
        dX = 0;
    }

    public void moveDown() {
        dY = 1;
        dX = 0;
    }

    @Override
    public Position moveToNextPosition() {
        this.position = this.getBehaviour().moveToNextPosition(this, this.gObjs);
        return this.position;
    }
}
