package UI.MFD.OSB;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class OSBPanel extends JPanel {
    public static HashMap<Integer,JButton> OSBList = new HashMap<>();

    public OSBPanel(int width, int height, int[] buttonNumbers){

        setPreferredSize(new Dimension(width, height));
        setBackground(Color.GRAY);

        // TOP & BOTTOM
        if(height > width){

            setLayout(new GridLayout(1,9));
            int buttonIndex = 0;
            for(int i = 0 ; i < 9 ; i++){

                if(i == 1 || i == 7){
                    add(Box.createGlue());
                    continue;
                }

                JPanel wrapper = new JPanel(new GridBagLayout());
                JButton btn;


                if(i == 0 || i == 8){ // corner buttons
                    btn = new JButton();
                    btn.setPreferredSize(new Dimension(80,120));
                    btn.setBackground(Color.LIGHT_GRAY);
                    btn.setFocusable(false);
                }
                else{ // OSBs
                    btn = new OSBButton("");
                    OSBList.put(buttonNumbers[buttonIndex], btn);
                    buttonIndex++;
                }

                wrapper.add(btn);
                wrapper.setOpaque(false);
                add(wrapper);
            }
            //System.out.println(OSBList.keySet());
        }

        // LEFT & RIGHT
        else {

            setLayout(new GridLayout(7,1));
            int buttonIndex = 0;
            for(int i = 0 ; i < 7 ; i++){

                if(i == 0 || i == 6){
                    add(Box.createGlue());
                    continue;
                }

                JPanel wrapper = new JPanel(new GridBagLayout());
                JButton btn = new OSBButton("");
                OSBList.put(buttonNumbers[buttonIndex], btn);
                buttonIndex++;

                wrapper.add(btn);
                wrapper.setOpaque(false);
                add(wrapper);
            }
            //System.out.println(OSBList.keySet());
        }
    }
}