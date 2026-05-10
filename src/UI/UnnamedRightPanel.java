package UI;

import javax.swing.*;
import java.awt.*;

public class UnnamedRightPanel extends JPanel {
    public UnnamedRightPanel(){
        setBackground(Color.DARK_GRAY);
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int)(screen.width * 0.21875);
        setPreferredSize(new Dimension(width, 0));
    }
}
