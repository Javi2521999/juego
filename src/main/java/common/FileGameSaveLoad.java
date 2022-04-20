package common;

import game.Game;
import game.GameConfig;
import game.GameObjectsJSONFactory;
import game.IGameObject;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

public class FileGameSaveLoad {

    public static ConcurrentLinkedQueue<IGameObject> jFileChooserLoad() {
        System.out.println("Loading objects");
        ConcurrentLinkedQueue<IGameObject> gameObjects = new ConcurrentLinkedQueue<>();
        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

        int returnValue = jfc.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = jfc.getSelectedFile();
            JSONArray jArray = FileUtilities.readJsonsFromFile(selectedFile.getAbsolutePath());
            if (jArray != null){
                for (int i = 0; i < jArray.length(); i++){
                    JSONObject jObj = jArray.getJSONObject(i);
                    gameObjects.add(GameObjectsJSONFactory.getGameObject(jObj));
                }
            }
        }
        return gameObjects;
    }
    public static GameConfig jFileChooserLoadConfig() {
        System.out.println("Loading objects");
        GameConfig gameConfig = null;
        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

        int returnValue = jfc.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = jfc.getSelectedFile();
            JSONArray jArray = FileUtilities.readJsonsFromFile(selectedFile.getAbsolutePath());
            if (jArray != null){
                for (int i = 0; i < jArray.length(); i++){
                    JSONObject jObj = jArray.getJSONObject(i);
                    gameConfig = new GameConfig(jObj);
                }
            }
        }
        return gameConfig;
    }
    public static void jFileChooserSave(ConcurrentLinkedQueue<IGameObject> gameObjects){
        System.out.println("Saving objects");
        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

        int returnValue = jfc.showOpenDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = jfc.getSelectedFile();
            System.out.println(selectedFile.getAbsolutePath());
            if (gameObjects != null){
                JSONObject jObjs [] = new JSONObject[gameObjects.size()];
                for(int i = 0; i < jObjs.length; i++){
                    jObjs[i] = ((IToJsonObject)gameObjects.toArray()[i]).toJSONObject();
                }
                FileUtilities.writeJsonsToFile(jObjs, selectedFile.getAbsolutePath());
            }
        }
    }
    public static void jFileChooserSave(GameConfig gameConfig){
        System.out.println("Saving objects");
        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

        int returnValue = jfc.showOpenDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = jfc.getSelectedFile();
            System.out.println(selectedFile.getAbsolutePath());
            if (gameConfig != null){
                JSONObject jObjs [] = new JSONObject[1];
                jObjs[0] = gameConfig.toJSONObject();
                FileUtilities.writeJsonsToFile(jObjs, selectedFile.getAbsolutePath());
            }
        }
    }
    public static ConcurrentLinkedQueue<IGameObject> defaultLoad(String path) {
        System.out.println("Loading objects");
        ConcurrentLinkedQueue<IGameObject> gameObjects = new ConcurrentLinkedQueue<>();
        JSONArray jArray = FileUtilities.readJsonsFromFile(path);
        if (jArray != null){
            for (int i = 0; i < jArray.length(); i++){
                JSONObject jObj = jArray.getJSONObject(i);
                gameObjects.add(GameObjectsJSONFactory.getGameObject(jObj));
            }
        }
        return gameObjects;
    }

    public static GameConfig defaultLoadConfig(String path) {
        System.out.println("Loading objects");
        GameConfig gameConfig = null;
        JSONArray jArray = FileUtilities.readJsonsFromFile(path);
        if (jArray != null){
            for (int i = 0; i < jArray.length(); i++){
                JSONObject jObj = jArray.getJSONObject(i);
                gameConfig = new GameConfig(jObj);
            }
        }
        return gameConfig;
    }

    public static void defaultSave(ConcurrentLinkedQueue<IGameObject> gameObjects, String path) {
        System.out.println("Saving objects");
        if (gameObjects != null){
            JSONObject jObjs [] = new JSONObject[gameObjects.size()];
            for(int i = 0; i < jObjs.length; i++){
                jObjs[i] = ((IToJsonObject)gameObjects.toArray()[i]).toJSONObject();
            }
            FileUtilities.writeJsonsToFile(jObjs, path);
        }
    }
    public static void defaultSave(GameConfig gameConfig, String path) {
        System.out.println("Saving objects");
        if (gameConfig != null){
            JSONObject jObjs [] = new JSONObject[1];
            for(int i = 0; i < jObjs.length; i++){
                jObjs[i] = gameConfig.toJSONObject();
            }
            FileUtilities.writeJsonsToFile(jObjs, path);
        }
    }
}
