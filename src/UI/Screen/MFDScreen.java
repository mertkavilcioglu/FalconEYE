package UI.Screen;

import App.EYEApp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class MFDScreen extends JPanel {

    private final TopOverlayPanel overlayNorth = new TopOverlayPanel();
    private final BottomOverlayPanel overlaySouth = new BottomOverlayPanel();
    private final LeftOverlayPanel overlayWest = new LeftOverlayPanel();
    private final RightOverlayPanel overlayEast = new RightOverlayPanel();

    private final MFDCanvas mfdCanvas;
    private int overlayThickness = 0;

    public MFDScreen(EYEApp app) {

        setLayout(null);
        setBackground(Color.BLACK);

        mfdCanvas = new MFDCanvas(app.getWorld());

        add(mfdCanvas);
        add(overlayNorth);
        add(overlaySouth);
        add(overlayWest);
        add(overlayEast);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {

                overlayThickness = (int)(getWidth() * 0.08);
                layoutChildren();
                mfdCanvas.setOverlayThickness(overlayThickness);
                repaint();
            }
        });
    }

    private void layoutChildren() {

        int w = getWidth();
        int h = getHeight();

        mfdCanvas.setBounds(0, 0, w, h);

        overlayNorth.setBounds(overlayThickness, 0, w - overlayThickness * 2, overlayThickness);
        overlaySouth.setBounds(overlayThickness, h - overlayThickness, w - overlayThickness * 2, overlayThickness);
        overlayWest.setBounds(0, overlayThickness, overlayThickness, h - overlayThickness * 2);
        overlayEast.setBounds(w - overlayThickness, overlayThickness, overlayThickness, h - overlayThickness * 2);
    }

    public int getOverlayThickness() {
        return overlayThickness;
    }

    public MFDCanvas getMfdCanvas() {
        return mfdCanvas;
    }
}