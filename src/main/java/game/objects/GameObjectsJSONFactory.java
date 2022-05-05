/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.objects;

import static common.IToJsonObject.TypeLabel;

import org.json.JSONObject;

/**
 *
 * @author juanangel
 */ //añadir las diferentes clases
public class GameObjectsJSONFactory {
    
    public static IGameObject getGameObject(JSONObject jObj) {
        
        IGameObject gObj = null;
        
        String typeLabel = jObj.getString(TypeLabel);

        if (typeLabel.equals("Blossom")){
            gObj = new Blossom(jObj);
        }
        else if (typeLabel.equals("Spider")){
            gObj = new Spider(jObj);
        }
        else if (typeLabel.equals("Bee")){
            gObj = new Bee(jObj);
        }
        else if (typeLabel.equals("Fly")){
            gObj = new Fly(jObj);
        }
        else if (typeLabel.equals("RidingHood")){
            gObj = new RidingHood(jObj);
        }
        else if (typeLabel.equals("Rock")){
            gObj = new Rock(jObj);
        }
        return gObj;
    }

}
