package UI;
import App.EYEApp;
import javax.swing.*;
import java.awt.*;

public class AppWindow extends JFrame {
    private EYEApp app;
    private HierarchyView hierarchyView;
    private UnnamedRightPanel unnamedRightPanel;
    private MFDView mfdView;


    public AppWindow(EYEApp app){
        super("FalconEYE");
        this.app = app;
        //setSize(1600, 900);

        hierarchyView = new HierarchyView();
        unnamedRightPanel = new UnnamedRightPanel();
        mfdView = new MFDView(app);


        Rectangle bounds = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        setBounds(bounds);
        setResizable(false);
        setLayout(new BorderLayout(0,0));
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        add(hierarchyView, BorderLayout.WEST);
        add(unnamedRightPanel, BorderLayout.EAST);
        add(mfdView, BorderLayout.CENTER);
    }

    public MFDView getMfdView(){
        return mfdView;
    }
}
