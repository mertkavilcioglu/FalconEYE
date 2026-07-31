package UI.Screen;

import javax.swing.*;
import java.awt.*;

public class TrianglePanel extends JPanel {

    private final boolean up;

    public TrianglePanel(boolean up){
        this.up = up;

        setOpaque(true);
        setBackground(Color.BLACK);
    }

    @Override
    protected void paintComponent(Graphics g){

        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D)g;

        g2.setStroke(new BasicStroke(2f));
        g2.setColor(Color.WHITE);

        int size = Math.min(getWidth(), getHeight()) / 2;

        int cx = getWidth()/2;
        int cy = getHeight()/2;
        int offset = getHeight() / 3;

        Polygon p = new Polygon();

        int halfBase = size / 2;
        int halfHeight = size / 3;

        if(up){

            int drawY = cy + offset;

            p.addPoint(cx, drawY - halfHeight);
            p.addPoint(cx - halfBase, drawY + halfHeight);
            p.addPoint(cx + halfBase, drawY + halfHeight);

        }else{

            int drawY = cy - offset+1;

            p.addPoint(cx - halfBase, drawY - halfHeight);
            p.addPoint(cx + halfBase, drawY - halfHeight);
            p.addPoint(cx, drawY + halfHeight);
        }

        g2.drawPolygon(p);
    }
}

