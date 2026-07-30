package UI.Screen;

import javax.swing.*;
import java.awt.*;

public class LeftOverlayPanel extends JPanel {
    public LeftOverlayPanel(){
        setBackground(Color.BLACK);
        setOpaque(false);

        int cells = 5;
        setLayout(new GridLayout(cells,1));

        for(int i=0 ; i<cells ; i++){
            JLabel text = new JLabel("section " + (i+1));
            text.setHorizontalAlignment(SwingConstants.CENTER);
            text.setOpaque(false);
            text.setForeground(Color.WHITE);
            add(text);
        }
    }
}
