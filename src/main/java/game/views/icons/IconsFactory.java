/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.views.icons;

import common.IToJsonObject;
import game.objects.*;
import org.json.JSONObject;
import game.views.IAWTGameView;
import game.views.IViewFactory;

/**
 *
 * @author juanangel
 */
public class IconsFactory implements IViewFactory, IToJsonObject {
    
    public IAWTGameView getView(IGameObject gObj, int length) throws Exception {
        IAWTGameView view = null;
        
        if (gObj instanceof Fly){
           view = new VIcon(gObj, "src/main/resources/images/fly.jpg", length); 
        }
        else if (gObj instanceof Bee){
           view = new VIcon(gObj, "src/main/resources/images/bee.jpg", length); 
        }  
        else if (gObj instanceof RidingHood){
           view = new VIcon(gObj, "src/main/resources/images/caperucita.jpg", length); 
        } 
        else if (gObj instanceof Spider){
           view = new VIcon(gObj, "src/main/resources/images/spider.jpg", length);
        }
        else if (gObj instanceof Rock){
            view = new VIcon(gObj, "src/main/resources/images/conejo.jpg", length);
        }
        else if (gObj instanceof Blossom){
           if (gObj.getValue() < 10){
                view = new VIcon(gObj, "src/main/resources/images/dandelion2.jpg", length); 
           }
           else {
                view = new VIcon(gObj, "src/main/resources/images/clover.jpg",  length); 
           }
        }
            
        return view;
    }

    @Override
    public JSONObject toJSONObject() {
        JSONObject jObj = new JSONObject();
        jObj.put(IToJsonObject.TypeLabel, this.getClass().getSimpleName());
        return jObj;
    }
}
