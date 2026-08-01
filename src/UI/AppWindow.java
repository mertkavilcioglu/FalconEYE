package UI;
import App.EYEApp;
import Sim.Entity;

import javax.swing.*;
import java.awt.*;

public class AppWindow extends JFrame {
    private EYEApp app;
    private HierarchyView hierarchyView;
    private TargetControlPanel targetControlPanel;
    private MFDView mfdView;


    public AppWindow(EYEApp app){
        super("FalconEYE");
        this.app = app;
        //setSize(1600, 900);

        hierarchyView = new HierarchyView();
        targetControlPanel = new TargetControlPanel();
        mfdView = new MFDView(app);

        hierarchyView.setSelectionListener(entityId -> {
            mfdView.getRadarScreen().getMfdCanvas().setSelectedEntityId(entityId);
            mfdView.getRadarScreen().getMfdCanvas().refreshRadarDisplay();
        });

        targetControlPanel.getFriendlyPanel().getDeleteButton().addActionListener(e -> {
            app.getWorld().removeAllEntities(Entity.IFF.FRIEND);
            hierarchyView.rebuild(app.getWorld());
            mfdView.getRadarScreen().getMfdCanvas().refreshRadarDisplay();
        });

        targetControlPanel.getEnemyPanel().getDeleteButton().addActionListener(e -> {
            app.getWorld().removeAllEntities(Entity.IFF.HOSTILE);
            hierarchyView.rebuild(app.getWorld());
            mfdView.getRadarScreen().getMfdCanvas().refreshRadarDisplay();
        });




        Rectangle bounds = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        setBounds(bounds);
        setResizable(false);
        setLayout(new BorderLayout(0,0));
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        add(hierarchyView, BorderLayout.WEST);
        add(targetControlPanel, BorderLayout.EAST);
        add(mfdView, BorderLayout.CENTER);

        hierarchyView.initialize(app.getWorld());
        new Timer(250, e -> hierarchyView.updateAll(app.getWorld())).start();
    }

    public MFDView getMfdView(){
        return mfdView;
    }
}
