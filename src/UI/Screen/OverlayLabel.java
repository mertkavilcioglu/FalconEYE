package UI.Screen;

import javax.swing.*;
import java.awt.*;

public class OverlayLabel extends JLabel {

    private final int margin;

    public OverlayLabel(String text) {
        this(text, 10);
    }

    public OverlayLabel(String text, int margin) {
        super(text, SwingConstants.CENTER);
        this.margin = margin;

        setForeground(Color.WHITE);
        setFont(getFont().deriveFont(Font.BOLD, getFont().getSize2D() *2f + 2));

        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {

        Graphics2D g2 = (Graphics2D) g.create();

        int boxW = (int)(getWidth() * 0.52);
        int boxH = (int)(getHeight() * 0.46);

        boxW += margin * 2;
        boxH += margin * 2;

        int x = (getWidth() - boxW) / 2;
        int y = (getHeight() - boxH) / 2;

        g2.setColor(Color.BLACK);
        g2.fillRoundRect(x, y, boxW, boxH, 4, 4);

        g2.dispose();

        super.paintComponent(g);
    }
}