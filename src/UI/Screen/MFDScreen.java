package UI.Screen;

import App.EYEApp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class MFDScreen extends JPanel {

    private TopOverlayPanel overlayNorth = new TopOverlayPanel();
    private BottomOverlayPanel overlaySouth = new BottomOverlayPanel();
    private RightOverlayPanel overlayEast = new RightOverlayPanel();
    private LeftOverlayPanel overlayWest = new LeftOverlayPanel();
    private int overlayThickness = 0;
    private MFDCanvas mfdCanvas;

    public MFDScreen(EYEApp app){
        mfdCanvas = new MFDCanvas(app.getWorld());
        setBackground(Color.BLACK);
        setLayout(new BorderLayout());
        add(overlayNorth, BorderLayout.NORTH);
        add(overlaySouth, BorderLayout.SOUTH);
        add(overlayEast, BorderLayout.EAST);
        add(overlayWest, BorderLayout.WEST);
        add(mfdCanvas, BorderLayout.CENTER);


        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                overlayThickness = (int)(getWidth() * 0.08);
                overlayNorth.setPreferredSize(new Dimension(0, overlayThickness));
                overlaySouth.setPreferredSize(new Dimension(0, overlayThickness));
                overlayEast.setPreferredSize(new Dimension(overlayThickness, 0));
                overlayWest.setPreferredSize(new Dimension(overlayThickness, 0));
                mfdCanvas.setOverlayThickness(overlayThickness);
                revalidate();
                repaint();
            }
        });

    }

    public int getOverlayThickness(){
        return overlayThickness;
    }

    public MFDCanvas getMfdCanvas(){
        return mfdCanvas;
    }
}
