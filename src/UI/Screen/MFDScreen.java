package UI.Screen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class MFDScreen extends JPanel {

    TopOverlayPanel overlayNorth = new TopOverlayPanel();
    BottomOverlayPanel overlaySouth = new BottomOverlayPanel();
    RightOverlayPanel overlayEast = new RightOverlayPanel();
    LeftOverlayPanel overlayWest = new LeftOverlayPanel();

    public MFDScreen(){
        setBackground(Color.BLACK);
        setLayout(new BorderLayout());
        add(overlayNorth, BorderLayout.NORTH);
        add(overlaySouth, BorderLayout.SOUTH);
        add(overlayEast, BorderLayout.EAST);
        add(overlayWest, BorderLayout.WEST);


        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int thickness = (int)(getWidth() * 0.08);
                overlayNorth.setPreferredSize(new Dimension(0, thickness));
                overlaySouth.setPreferredSize(new Dimension(0, thickness));
                overlayEast.setPreferredSize(new Dimension(thickness, 0));
                overlayWest.setPreferredSize(new Dimension(thickness, 0));
                revalidate();
            }
        });

    }
}
