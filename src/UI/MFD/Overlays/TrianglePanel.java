package UI.MFD.Overlays;

import UI.UIScale;

import javax.swing.*;
import java.awt.*;

public class TrianglePanel extends JPanel {

    private final boolean up;
    private boolean enabled = true;

    public TrianglePanel(boolean up){
        this.up = up;

        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g){

        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();

        g2.setStroke(new BasicStroke(Math.max(1f, UIScale.scale(2))));

        int size = Math.min(getWidth(), getHeight()) / 2;

        int cx = getWidth() / 2;
        int cy = getHeight() / 2;
        int offset = getHeight() / 3;

        int halfBase = size / 2;
        int halfHeight = size / 3;

        int drawY;

        if (up)
            drawY = cy + offset + UIScale.scale(13);
        else
            drawY = cy - offset + 1;


        int boxW = size + 10;
        int boxH = size + 8;

        int extra = getHeight() * 2 / 3;

        g2.setColor(Color.BLACK);

        if (up) {

            g2.fillRect(
                    cx - boxW / 3,
                    drawY - boxH / 3,
                    boxW,
                    boxH + extra
            );

        } else {

            g2.fillRect(
                    cx - boxW / 3,
                    drawY - boxH / 3 - extra,
                    boxW,
                    boxH + extra
            );
        }

        if(!enabled){
            g2.dispose();
            return;
        }

        Polygon p = new Polygon();

        if (up) {

            p.addPoint(cx, drawY - halfHeight);
            p.addPoint(cx - halfBase, drawY + halfHeight);
            p.addPoint(cx + halfBase, drawY + halfHeight);

        } else {

            p.addPoint(cx - halfBase, drawY - halfHeight);
            p.addPoint(cx + halfBase, drawY - halfHeight);
            p.addPoint(cx, drawY + halfHeight);
        }

        g2.setColor(Color.WHITE);
        g2.drawPolygon(p);

        g2.dispose();
    }

    public void setTriangleEnabled(boolean enabled){

        this.enabled = enabled;
        repaint();

    }
}