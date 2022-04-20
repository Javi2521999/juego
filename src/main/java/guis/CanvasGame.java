package guis;

import game.IGameObject;
import views.IAWTGameView;
import views.IViewFactory;
import views.boxes.BoxesFactory;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
//
public class CanvasGame extends JPanel {
    int size, boxSize;
    int pX, pY;

    ConcurrentLinkedQueue<IGameObject> objects = new ConcurrentLinkedQueue<>();
    public IViewFactory defaultViewFactory = new BoxesFactory();

    public CanvasGame(int size, int boxSize, IViewFactory defaultViewFactory){
        this.size = size;
        this.boxSize = boxSize;
        this.defaultViewFactory = defaultViewFactory;
        setPreferredSize(new Dimension(size, size));
        setBorder(BorderFactory.createLineBorder(Color.blue));
    }

    public void setSquareCoordinates(int x, int y){
        pX = x;
        pY = y;
        repaint();
    }

    public void setSizes(int size, int boxSize) {
        this.size = size;
        this.boxSize = boxSize;
        repaint();
    }

    public void setDefaultViewFactory(IViewFactory defaultViewFactory) {
        this.defaultViewFactory = defaultViewFactory;
        repaint();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        drawGrid(g);
        //g.setColor(Color.red);
        //g.fillRect(pX*boxSize+4, pY*boxSize+4, boxSize-8, boxSize-8);
        drawSquare(g, pX, pY);
        try {
            drawGameItems(g);
        } catch (Exception ex) {
            Logger.getLogger(game.GameEditor_4.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void drawGrid(Graphics g){
        Color c = g.getColor();
        g.setColor(Color.LIGHT_GRAY);
        int nLines = size/boxSize;
        System.out.println("---- " + nLines);
        for (int i = 1; i < nLines; i++){
            g.drawLine(i*boxSize, 0, i*boxSize, size);
            g.drawLine(0, i*boxSize, size, i*boxSize);
        }
        g.setColor(c);
    }

    private void drawSquare(Graphics g, int xCoord, int yCoord) {

        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.BLUE);
        g2.setStroke(new BasicStroke(2));
        g2.drawRect(xCoord*boxSize-2, yCoord*boxSize-2, boxSize+4, boxSize+4);
    }

    public void drawGameItems(ConcurrentLinkedQueue<IGameObject> gObjects){
        this.objects = gObjects;
        repaint();
    }

    private void drawGameItems(Graphics g) throws Exception{

        IAWTGameView view = null;

        if (objects != null){
            for (IGameObject obj: objects){
                if (obj != null){
                    view = defaultViewFactory.getView(obj, boxSize);
                    view.draw(g);
                }
            }
        }
    }
}
