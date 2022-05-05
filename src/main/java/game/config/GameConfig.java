package game.config;
//importar las diferentes clases que se vana llamar en esta clase.
import common.IToJsonObject;
import game.objects.AbstractGameObject;
import game.objects.GameObjectsJSONFactory;
import game.objects.IGameObject;
import org.json.JSONArray;
import org.json.JSONObject;
import game.views.IViewFactory;
import game.views.boxes.BoxesFactory;
import game.views.boxes.GeometricFactory;
import game.views.icons.IconsFactory;

import java.util.concurrent.ConcurrentLinkedQueue;
//
public class GameConfig implements IToJsonObject {
    private Boolean isAutomatic;
    private ConcurrentLinkedQueue<IGameObject> gObjs;
    private Integer level;
    private Integer round;
    private Integer boardWidth;
    private Integer timerTick;
    private IViewFactory defaultViewFactory;

    public GameConfig(){
        this.isAutomatic = false;
        this.level = 0;
        this.round = 0;
        this.boardWidth = 480;
        this.timerTick = 600;
        this.defaultViewFactory = new BoxesFactory();    
        this.gObjs = new ConcurrentLinkedQueue<>();
    }
    public GameConfig(JSONObject configJsonObject){
        this.isAutomatic = configJsonObject.getBoolean("isAutomatic");
        this.level = configJsonObject.getInt("level");
        this.round = configJsonObject.getInt("round");
        this.boardWidth = configJsonObject.getInt("boardWidth");
        this.timerTick = configJsonObject.getInt("timerTick");
        this.defaultViewFactory = this.getDefaulViewFactoryFromJson(configJsonObject.getJSONObject("defaultViewFactory"));
        this.gObjs = this.getObjsFromJsonArray(configJsonObject.getJSONArray("gObjs"));
    }
// este metodo permite el cambio de forma de ver a caperucita, flores, abejas...
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
//saber el lvl
    public Integer getLevel() {
        return level;
    }
//recibir nivel e enviar nivel
    public void setLevel(Integer level) {
        this.level = level;
    }
//igual k el lvl, dar valor entero al round y en el siguiente sacarlo a la cuadricula
    public Integer getRound() {
        return round;
    }

    public void setRound(Integer round) {
        this.round = round;
    }
//igual que anterior pero con como es de grande la cuadricula para despues poder cambiarla al gusto
    public Integer getBoardWidth() {
        return boardWidth;
    }

    public void setBoardWidth(Integer boardWidth) {
        this.boardWidth = boardWidth;
    }
//lo mismo con el timer
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
    
    public static void main (String [] args) {
        /* INICIO DEL JUEGO: CONFIGURACION POR DEFECTO */
        GameConfig gameConfig = new GameConfig();
        System.out.println(gameConfig.toJSONObject());
        
        /* PASA LA PARTIDA */
        gameConfig.setBoardWidth(300);
        gameConfig.setLevel(3);
        
        /* FINAL DEL JUEGO: CONFIGURACION MODIFICADA (GUARDAR PARTIDA) */
        System.out.println(gameConfig.toJSONObject());
                
        /* CARGAR DEL JUEGO */
        GameConfig gameConfig2 = new GameConfig(gameConfig.toJSONObject());
        System.out.print(gameConfig2.toJSONObject());
                
    }
}
