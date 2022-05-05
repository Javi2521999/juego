package game.views.boxes;

import game.objects.IGameObject;
import game.objects.Position;
import game.views.AbstractGameView;

import java.awt.*;

public class VNumberedTriangle extends AbstractGameView {

    Color myColor = Color.blue;
    String legend = new String();

    public VNumberedTriangle(IGameObject mObject, int length, Color c, String legend) throws Exception{
        super(mObject, length);
        myColor = c;
        this.legend = legend;
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        Position coord = gObj.getPosition();
        int x [] = {coord.getX()*length, ((int)length*coord.getX()*coord.getX()/2), length + length*coord.getX()};
        int y [] = {length*coord.getY(), coord.getY(), length*coord.getY()};
        int n = 3;
        Color c = g2.getColor();
        g2.setColor(myColor);
        g2.setStroke(new BasicStroke(2));
        g2.fillPolygon(x,y,n);
        g2.setColor(Color.WHITE);
        g2.drawString(legend, length*coord.getX()+6, length*coord.getY()+36);
        g2.setColor(c);
    }
}
