package UI;

import javax.swing.*;
import java.awt.*;

public class HierarchyView extends JPanel {
    public HierarchyView(){
        setBackground(Color.GREEN);
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int)(screen.width * 0.21875);
        setPreferredSize(new Dimension(width, 0));
    }
}
