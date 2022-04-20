package game;

import common.IToJsonObject;
import org.json.JSONArray;
import org.json.JSONObject;
import views.IViewFactory;
import views.boxes.BoxesFactory;
import views.boxes.GeometricFactory;
import views.icons.IconsFactory;

import java.util.concurrent.ConcurrentLinkedQueue;

public class GameConfig implements IToJsonObject {
    private Boolean isAutomatic;
    private ConcurrentLinkedQueue<IGameObject> gObjs = new ConcurrentLinkedQueue<>();
    private Integer level;
    private Integer round;
    private Integer boardWidth;
    private Integer timerTick;
    private IViewFactory defaultViewFactory;

    public GameConfig(){}
    public GameConfig(JSONObject configJsonObject){
        this.isAutomatic = configJsonObject.getBoolean("isAutomatic");
        this.level = configJsonObject.getInt("level");
        this.round = configJsonObject.getInt("round");
        this.boardWidth = configJsonObject.getInt("boardWidth");
        this.timerTick = configJsonObject.getInt("timerTick");
        this.defaultViewFactory = this.getDefaulViewFactoryFromJson(configJsonObject.getJSONObject("defaultViewFactory"));
        this.gObjs = this.getObjsFromJsonArray(configJsonObject.getJSONArray("gObjs"));
    }

    private IViewFactory getDefaulViewFactoryFromJson(JSONObject jObj) {
        IViewFactory viewFactory = null;
        String typeLabel = jObj.getString(TypeLabel);

        if (typeLabel.equals("BoxesFactory")){
            viewFactory = new BoxesFactory();
        }
        else if (typeLabel.equals("GeometricFactory")){
            viewFactory = new GeometricFactory();
        }
        else if (typeLabel.equals("IconsFactory")){
            viewFactory = new IconsFactory();
        }
        return  viewFactory;
    }
    private ConcurrentLinkedQueue<IGameObject> getObjsFromJsonArray(JSONArray jsonArray) {
        ConcurrentLinkedQueue<IGameObject> gObjsOfJSON = new ConcurrentLinkedQueue<>();
        jsonArray.forEach(x -> gObjsOfJSON.add(GameObjectsJSONFactory.getGameObject((JSONObject) x)));
        return gObjsOfJSON;
    }
    public Boolean getAutomatic() {
        return isAutomatic;
    }

    public void setAutomatic(Boolean automatic) {
        isAutomatic = automatic;
    }

    public ConcurrentLinkedQueue<IGameObject> getgObjs() {
        return gObjs;
    }

    public void setgObjs(ConcurrentLinkedQueue<IGameObject> gObjs) {
        this.gObjs = gObjs;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getRound() {
        return round;
    }

    public void setRound(Integer round) {
        this.round = round;
    }

    public Integer getBoardWidth() {
        return boardWidth;
    }

    public void setBoardWidth(Integer boardWidth) {
        this.boardWidth = boardWidth;
    }

    public Integer getTimerTick() {
        return timerTick;
    }

    public void setTimerTick(Integer timerTick) {
        this.timerTick = timerTick;
    }

    public IViewFactory getDefaultViewFactory() {
        return defaultViewFactory;
    }

    public void setDefaultViewFactory(IViewFactory defaultViewFactory) {
        this.defaultViewFactory = defaultViewFactory;
    }

    @Override
    public JSONObject toJSONObject() {
        JSONArray jsonArray = new JSONArray();
        this.gObjs.forEach(x-> jsonArray.put(((AbstractGameObject)x).toJSONObject()));
        JSONObject jObj = new JSONObject();
        jObj.put(IToJsonObject.TypeLabel, this.getClass().getSimpleName());
        jObj.put("isAutomatic", this.isAutomatic);
        jObj.put("gObjs", jsonArray);
        jObj.put("level", this.level);
        jObj.put("round", this.round);
        jObj.put("boardWidth", this.boardWidth);
        jObj.put("timerTick", this.timerTick);
        jObj.put("defaultViewFactory", this.defaultViewFactory.toJSONObject());
        return jObj;
    }
}
