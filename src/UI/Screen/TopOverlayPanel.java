package UI.Screen;

import javax.swing.*;
import java.awt.*;

public class TopOverlayPanel extends JPanel {
    public TopOverlayPanel(){
        setBackground(Color.BLACK);

        int cells = 7;
        setLayout(new GridLayout(1, cells));

        for(int i=0 ; i<cells ; i++){
            if(i==0 || i== 6){
                add(new JLabel(" "));
                continue;
            }
            JLabel text = new JLabel("section " + (i));
            text.setHorizontalAlignment(SwingConstants.CENTER);
            text.setForeground(Color.WHITE);
            add(text);
        }

    }
}
