package UI;

import javax.swing.*;
import java.awt.*;

public class ToolButton extends JButton {

    public ToolButton(String text){

        super(text);

        setFocusPainted(false);
        setFocusable(false);

        setForeground(Color.WHITE);
        setBackground(new Color(60,60,60));

        setFont(getFont().deriveFont(Font.BOLD,15f));

        setBorder(BorderFactory.createLineBorder(Color.GRAY));

        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        setPreferredSize(new Dimension(0, 50));
    }
}