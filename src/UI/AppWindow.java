package UI;
import App.EYEApp;
import javax.swing.*;
import java.awt.*;

public class AppWindow extends JFrame {
    private EYEApp app;
    public AppWindow(EYEApp app){
        super("FalconEYE");
        this.app = app;
        setSize(800, 600);
        setResizable(true);
        //setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout(0, 0));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
}
