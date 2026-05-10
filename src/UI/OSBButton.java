package UI;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class OSBButton extends JButton {
    public OSBButton(String text){
        super(text);
        setPreferredSize(new Dimension(80,80));;
        setBackground(Color.LIGHT_GRAY);
        setBorder(BorderFactory.createCompoundBorder(new LineBorder(Color.LIGHT_GRAY,10), new LineBorder(Color.WHITE,5)));
        setFocusable(false);
    }
}
