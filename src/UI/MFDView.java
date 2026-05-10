package UI;

import javax.swing.*;
import java.awt.*;

public class MFDView extends JPanel {
    private OSBPanel topOSBs =
            new OSBPanel(
                    0,
                    150,
                    new int[]{1,2,3,4,5}
            );

    private OSBPanel rightOSBs =
            new OSBPanel(
                    150,
                    0,
                    new int[]{6,7,8,9,10}
            );

    private OSBPanel botOSBs =
            new OSBPanel(
                    0,
                    150,
                    new int[]{15,14,13,12,11}
            );

    private OSBPanel leftOSBs =
            new OSBPanel(
                    150,
                    0,
                    new int[]{20,19,18,17,16}
            );


    private JPanel radarScreen = new JPanel();

    public MFDView(JFrame frame){

        setLayout(new BorderLayout());

        radarScreen.setBackground(Color.BLACK);

        add(topOSBs, BorderLayout.NORTH);
        add(botOSBs, BorderLayout.SOUTH);
        add(leftOSBs, BorderLayout.WEST);
        add(rightOSBs, BorderLayout.EAST);

        add(radarScreen, BorderLayout.CENTER);
    }
}
