package UI.MFD.OSB;

import Sim.Components.Radar;
import UI.MFD.Screen.MFDScreen;

import javax.swing.*;
import java.awt.*;

public class LeftOSBPanel extends JPanel {

    private final Radar radar;
    private final MFDScreen mfdScreen;


    public LeftOSBPanel(int width, int height, int[] buttonNumbers, Radar radar, MFDScreen mfdScreen){

        this.radar = radar;
        this.mfdScreen = mfdScreen;

        setPreferredSize(new Dimension(width, height));
        setBackground(Color.GRAY);

        setLayout(new GridLayout(7,1));

        int buttonIndex = 0;

        for(int i = 0; i < 7; i++){

            if(i == 0 || i == 6){

                add(Box.createGlue());
                continue;
            }

            JPanel wrapper = new JPanel(new GridBagLayout());
            wrapper.setOpaque(false);
            int osbNumber = buttonNumbers[buttonIndex];

            JButton btn = new OSBButton("");
            buttonIndex++;

            btn.addActionListener(e -> {
                switch(osbNumber){

                    case 20:
                        radar.increaseRange();
                        updateRange();
                        //System.out.println("OSB 20 RANGE +");
                        break;

                    case 19:
                        radar.decreaseRange();
                        updateRange();
                        //System.out.println("OSB 19 RANGE -");
                        break;

                    case 18:
                        radar.cycleAzimuth();
                        updateAzimuth();
                        //System.out.println("OSB 18 AZIMUTH");
                        break;

                    case 17:
                        radar.cycleBars();
                        updateBars();
                        //System.out.println("OSB 17 BARS");
                        break;

                    case 16:
                        //System.out.println("OSB 16 PRESSED");
                        break;

                    default:
                        //System.out.println("OSB " + osbNumber + " PRESSED");
                        break;
                }
            });


            wrapper.add(btn);
            add(wrapper);
        }
    }

    private void updateAzimuth(){

        mfdScreen.updateAzimuthLabel(radar.getAzimuth());

        mfdScreen.getMfdCanvas().repaint();
    }

    private void updateRange(){

        mfdScreen.updateRangeLabel(radar.getRangeNM());
        mfdScreen.getMfdCanvas().repaint();
    }

    private void updateBars(){

        mfdScreen.updateBarsLabel(
                radar.getBars()
        );


        mfdScreen.getMfdCanvas().repaint();
    }
}