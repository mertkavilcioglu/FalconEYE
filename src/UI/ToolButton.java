package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ToolButton extends JButton {

    private final boolean danger;

    public ToolButton(String text){
        this(text, false);
    }

    public ToolButton(String text, boolean danger){

        super(text);
        this.danger = danger;

        setFocusPainted(false);
        setFocusable(false);

        setForeground(Color.WHITE);
        setBackground(UITheme.BUTTON);

        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(danger ? UITheme.DELETE_BUTTON_HOVER : UITheme.BUTTON_HOVER);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(danger ? UITheme.DELETE_BUTTON : UITheme.BUTTON);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                setBackground(danger ? UITheme.DELETE_BUTTON_PRESSED : UITheme.BUTTON_PRESSED);
            }

            @Override
            public void mouseReleased(MouseEvent e){

                if(contains(e.getPoint())){
                    setBackground(danger ? UITheme.DELETE_BUTTON_HOVER : UITheme.BUTTON_HOVER);
                }
                else{
                    setBackground(danger ? UITheme.DELETE_BUTTON : UITheme.BUTTON);
                }
            }
        });

        setFont(getFont().deriveFont(Font.BOLD,15f));
        setBorder(BorderFactory.createLineBorder(Color.GRAY));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setPreferredSize(new Dimension(0, 50));
    }
}