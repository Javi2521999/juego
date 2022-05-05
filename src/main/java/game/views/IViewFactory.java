/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.views;

import common.IToJsonObject;
import game.objects.IGameObject;

/**
 *
 * @author juanangel
 */
public interface IViewFactory extends IToJsonObject {
   IAWTGameView getView(IGameObject gObj, int length) throws Exception; 
}
