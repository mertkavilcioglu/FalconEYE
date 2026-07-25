package UI.Screen;

import Sim.Entity;
import Sim.World;

import javax.swing.*;
import java.awt.*;

public class MFDCanvas extends JPanel {

    private World world;

    public MFDCanvas(World world){
        this.world = world;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        drawEntities(g);
    }

    private void drawEntities(Graphics g) {

        for (Entity e : world.getEntities().values()) {
            if (e == world.player)
                continue;
            drawEntity(g, e);
        }
    }

    private void drawEntity(Graphics g, Entity e) {
        int radius = 8;
        g.setColor(Color.GREEN);

        //test cases
        drawDot(g, getWidth()/2, getHeight()/2, radius);
        drawDot(g, 0, 0, radius);
        drawDot(g, getWidth(), getHeight(), radius);
        //TODO: çizme işini düşmanların relative'ine göre aldırt
    }

    private void drawDot(Graphics g, int centerX, int centerY, int radius) {
        g.fillOval(centerX - radius / 2, centerY - radius / 2, radius, radius);
    }
}
