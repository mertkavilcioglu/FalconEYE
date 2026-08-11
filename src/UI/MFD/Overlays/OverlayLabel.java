package UI.MFD.Overlays;

import javax.swing.*;
import java.awt.*;

public class OverlayLabel extends JLabel {

    private final int margin;
    private float fontScale = 1.0f;

    public OverlayLabel(String text) {
        this(text, 10);
    }

    public OverlayLabel(String text, int margin) {
        super(text, SwingConstants.CENTER);
        this.margin = margin;

        setForeground(Color.WHITE);
        setFont(getFont().deriveFont(Font.BOLD, 20f));
        setOpaque(false);
    }

    public OverlayLabel(String text, float fontScale) {
        this(text);
        this.fontScale = fontScale;
    }

    public OverlayLabel(String text, int margin, float fontScale) {
        this(text, margin);
        this.fontScale = fontScale;
    }

    @Override
    protected void paintComponent(Graphics g) {

        updateFontSize();

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

    private void updateFontSize() {
        float size = Math.max(16f, Math.min(30f, getHeight() * 0.29f * fontScale));
        setFont(getFont().deriveFont(Font.BOLD, size));
    }
}