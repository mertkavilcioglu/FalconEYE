package UI.MFD.OSB;

import UI.UITheme;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class OSBButton extends JButton {

    public OSBButton(String text){

        super(text);

        setPreferredSize(new Dimension(80,80));

        setBackground(UITheme.OSB);

        setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(UITheme.OSB,10),
                new LineBorder(Color.WHITE,5)
        ));

        setFocusable(false);
        setContentAreaFilled(false);
        setOpaque(true);
        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(UITheme.OSB_HOVER);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(UITheme.OSB);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                setBackground(UITheme.OSB_PRESSED);
            }

            @Override
            public void mouseReleased(MouseEvent e) {

                if(contains(e.getPoint()))
                    setBackground(UITheme.OSB_HOVER);
                else
                    setBackground(UITheme.OSB);
            }
        });
    }

    private void updateStyle(Color color){

        setBackground(color);

        setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(color,10),
                new LineBorder(Color.WHITE,5)
        ));
    }
}