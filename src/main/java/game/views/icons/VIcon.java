package game.views.icons;

import game.objects.IGameObject;
import game.objects.Position;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import game.views.AbstractGameView;


public class VIcon extends AbstractGameView {
	
    private BufferedImage image;
    
    public VIcon(IGameObject mObject, String imgFile, int l) throws Exception {
       super(mObject, l);
       image = ImageIO.read(new File(imgFile));

    }

    @Override
    public void draw(Graphics g) {       
        Position coord = gObj.getPosition();
	g.drawImage(image, length * coord.getX(), length * coord.getY(), length, length, null);		
    }
}
