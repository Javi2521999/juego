/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.objects;

import org.json.JSONObject;


/**
 *
 * @author juanangel
 */
public class Bee extends AbstractGameObject {
    public Bee(){}
    public Bee(Position position) { super(position); }
    public Bee(Position position, int value) { super(position, value, 1);  }
    public Bee(Position position, int value, int life) { super(position, value, life); }
    public Bee(JSONObject obj) { super(obj); }

    @Override
    public Position moveToNextPosition(){
        this.position = this.getBehaviour().moveToNextPosition(this, this.gObjs);
        return this.position;
    }

}
