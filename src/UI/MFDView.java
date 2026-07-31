package UI;

import App.EYEApp;
import Sim.Components.Radar;
import UI.Screen.MFDScreen;

import javax.swing.*;
import java.awt.*;

public class MFDView extends JPanel {

    private MFDScreen radarScreen;
    private OSBPanel topOSBs = new OSBPanel(0, 150, new int[]{1,2,3,4,5});
    private OSBPanel rightOSBs = new OSBPanel(150, 0, new int[]{6,7,8,9,10});
    private OSBPanel botOSBs = new OSBPanel(0, 150, new int[]{15,14,13,12,11});

    private LeftOSBPanel leftOSBs;



    public MFDView(EYEApp app){
        setLayout(new BorderLayout());
        radarScreen = new MFDScreen(app);
        leftOSBs = new LeftOSBPanel(150, 0, new int[]{20,19,18,17,16}, app.getWorld().player.getComponent(Radar.class),radarScreen);

        add(topOSBs, BorderLayout.NORTH);
        add(botOSBs, BorderLayout.SOUTH);
        add(leftOSBs, BorderLayout.WEST);
        add(rightOSBs, BorderLayout.EAST);

        add(radarScreen, BorderLayout.CENTER);

    }

    public MFDScreen getRadarScreen(){
        return radarScreen;
    }



}
