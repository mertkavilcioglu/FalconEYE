package UI;
import App.EYEApp;
import Sim.Entity;
import Sim.World;

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




        // CREATE SINGLE FRIENDLY
        targetControlPanel.getFriendlyPanel().getCloseSingleButton().addActionListener(e -> {
                    app.getWorld().createRandomEntity(Entity.IFF.FRIEND, World.SpawnRange.CLOSE);
                    refreshUI();
                });

        targetControlPanel.getFriendlyPanel().getMediumSingleButton().addActionListener(e -> {
                    app.getWorld().createRandomEntity(Entity.IFF.FRIEND, World.SpawnRange.MEDIUM);
                    refreshUI();
                });

        targetControlPanel.getFriendlyPanel().getLongSingleButton().addActionListener(e -> {
                    app.getWorld().createRandomEntity(Entity.IFF.FRIEND, World.SpawnRange.LONG);
                    refreshUI();
                });

        // CREATE SINGLE FRIENDLY
        targetControlPanel.getEnemyPanel().getCloseSingleButton().addActionListener(e -> {
            app.getWorld().createRandomEntity(Entity.IFF.HOSTILE, World.SpawnRange.CLOSE);
            refreshUI();
        });

        targetControlPanel.getEnemyPanel().getMediumSingleButton().addActionListener(e -> {
            app.getWorld().createRandomEntity(Entity.IFF.HOSTILE, World.SpawnRange.MEDIUM);
            refreshUI();
        });

        targetControlPanel.getEnemyPanel().getLongSingleButton().addActionListener(e -> {
            app.getWorld().createRandomEntity(Entity.IFF.HOSTILE, World.SpawnRange.LONG);
            refreshUI();
        });










        //DELETE BUTTONS
        targetControlPanel.getFriendlyPanel().getDeleteButton().addActionListener(e -> {
            app.getWorld().removeAllEntities(Entity.IFF.FRIEND);
            refreshUI();
        });

        targetControlPanel.getEnemyPanel().getDeleteButton().addActionListener(e -> {
            app.getWorld().removeAllEntities(Entity.IFF.HOSTILE);
            refreshUI();
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

    private void refreshUI(){

        hierarchyView.rebuild(app.getWorld());
        mfdView.getRadarScreen().getMfdCanvas().refreshRadarDisplay();
    }

    public MFDView getMfdView(){
        return mfdView;
    }
}
