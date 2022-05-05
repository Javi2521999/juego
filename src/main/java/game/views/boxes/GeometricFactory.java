package game.views.boxes;

import common.IToJsonObject;
import game.objects.*;
import game.views.IViewFactory;
import org.json.JSONObject;
import game.views.IAWTGameView;

import java.awt.*;

public class GeometricFactory implements IViewFactory, IToJsonObject {

    public IAWTGameView getView(IGameObject gObj, int length) throws Exception {

        IAWTGameView view = new VNumberedCircle(gObj, length);

        if (gObj instanceof Fly){
            view = new VNumberedCircle(gObj, length, Color.gray, "Fly"); ;
        }
        else if (gObj instanceof Bee){
            view = new VNumberedCircle(gObj, length, Color.YELLOW, "Bee");
        }
        else if (gObj instanceof Spider){
            view = new VNumberedCircle(gObj, length, Color.black, "Spider");
        }
        else if (gObj instanceof RidingHood){
            view = new VNumberedCircle(gObj, length, Color.red, "Hood");
        }
        else if (gObj instanceof Rock){
            view = new VNumberedCircle(gObj, length, Color.green, "Rock");
        }
        else if (gObj instanceof Blossom){
            if (gObj.getValue() < 10){
                view = new VNumberedCircle(gObj, length, Color.pink, "DLion");
            }
            else {
                view = new VNumberedCircle(gObj, length, Color.GREEN, "Clover");
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
