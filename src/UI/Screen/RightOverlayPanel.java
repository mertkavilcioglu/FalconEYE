package UI.Screen;

import javax.swing.*;
import java.awt.*;

public class RightOverlayPanel extends JPanel {

    public RightOverlayPanel() {

        setOpaque(false);
        setLayout(new GridLayout(5, 1));

        for (int i = 0; i < 5; i++) {

            if (i == 1) {

                JPanel section = new JPanel(new GridLayout(4, 1));
                section.setOpaque(false);

                JLabel contLabel = new OverlayLabel("CONT");

                section.add(contLabel);
                section.add(new JLabel());
                section.add(new JLabel());
                section.add(new JLabel());

                add(section);
            }
            else {
                add(new JLabel());
            }
        }
    }
}