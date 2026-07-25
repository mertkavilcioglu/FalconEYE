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
                int thickness = (int)(getWidth() * 0.08);
                overlayNorth.setPreferredSize(new Dimension(0, thickness));
                overlaySouth.setPreferredSize(new Dimension(0, thickness));
                overlayEast.setPreferredSize(new Dimension(thickness, 0));
                overlayWest.setPreferredSize(new Dimension(thickness, 0));
                revalidate();
                repaint();
            }
        });

    }

    public MFDCanvas getMfdCanvas(){
        return mfdCanvas;
    }
}
