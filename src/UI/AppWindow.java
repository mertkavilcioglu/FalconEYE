package UI;
import App.EYEApp;
import javax.swing.*;
import java.awt.*;

public class AppWindow extends JFrame {
    private EYEApp app;
    public AppWindow(EYEApp app){
        super("FalconEYE");
        this.app = app;
        //setSize(1600, 900);

        Rectangle bounds = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        setBounds(bounds);
        setResizable(false);
        setLayout(new BorderLayout(0,0));
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        add(new HierarchyView(), BorderLayout.WEST);
        add(new UnnamedRightPanel(), BorderLayout.EAST);
        add(new MFDView(this), BorderLayout.CENTER);
    }
}
