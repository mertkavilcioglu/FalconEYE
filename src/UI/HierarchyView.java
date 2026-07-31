package UI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;

import Sim.Components.Radar;
import Sim.Entity;
import Sim.World;
import Sim.Components.Transform;
import Sim.Components.Velocity;
import Mathf.Vec3;

public class HierarchyView extends JPanel {

    private final DefaultMutableTreeNode rootNode;
    private final DefaultTreeModel treeModel;
    private final JTree tree;

    public HierarchyView() {

        setBackground(Color.DARK_GRAY);

        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) (screen.width * 0.21875);
        setPreferredSize(new Dimension(width, 0));

        setLayout(new BorderLayout());

        //----------------------------------------
        // TOP
        //----------------------------------------

        JPanel topContainer = new JPanel();
        topContainer.setOpaque(false);
        topContainer.setLayout(new BoxLayout(topContainer, BoxLayout.Y_AXIS));
        topContainer.setBorder(new EmptyBorder(20,20,20,20));

        JLabel title = new JLabel("TARGET HIERARCHY");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setForeground(Color.WHITE);
        title.setFont(title.getFont().deriveFont(Font.BOLD,35f));

        topContainer.add(title);
        topContainer.add(Box.createVerticalStrut(10));

        JSeparator separator = new JSeparator();
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE,1));

        topContainer.add(separator);
        topContainer.add(Box.createVerticalStrut(12));

        JLabel description = new JLabel(
                "<html><div style='text-align:center;'>"
                        + "Displays every target currently "
                        + "existing in the simulation."
                        + "</div></html>"
        );

        description.setAlignmentX(Component.CENTER_ALIGNMENT);
        description.setHorizontalAlignment(SwingConstants.CENTER);
        description.setForeground(Color.LIGHT_GRAY);
        description.setFont(description.getFont().deriveFont(Font.PLAIN,18f));

        topContainer.add(description);
        topContainer.add(Box.createVerticalStrut(20));

        //----------------------------------------
        // TREE
        //----------------------------------------

        rootNode = new DefaultMutableTreeNode("TARGETS");

        treeModel = new DefaultTreeModel(rootNode);

        tree = new JTree(treeModel);

        tree.setRootVisible(true);
        tree.setShowsRootHandles(true);

        tree.setBackground(new Color(70,70,70));
        tree.setForeground(Color.WHITE);

        tree.setRowHeight(24);

        tree.setFont(tree.getFont().deriveFont(Font.PLAIN,16f));

        DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();

        renderer.setBackgroundNonSelectionColor(new Color(70,70,70));
        renderer.setBackgroundSelectionColor(new Color(90,90,90));

        renderer.setTextNonSelectionColor(Color.WHITE);
        renderer.setTextSelectionColor(Color.WHITE);

        renderer.setBorderSelectionColor(null);

        renderer.setLeafIcon(null);
        renderer.setClosedIcon(null);
        renderer.setOpenIcon(null);

        tree.setCellRenderer(renderer);

        JScrollPane scrollPane = new JScrollPane(tree);

        scrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        scrollPane.getViewport().setBackground(new Color(70,70,70));

        topContainer.add(scrollPane);

        add(topContainer, BorderLayout.CENTER);

        //----------------------------------------
        // OUTER BORDER
        //----------------------------------------

        setBorder(BorderFactory.createCompoundBorder(
                new EmptyBorder(10,10,10,10),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.GRAY,3),
                        new EmptyBorder(10,10,10,10)
                )
        ));
    }

    public void rebuild(World world){

        rootNode.removeAllChildren();
        DefaultMutableTreeNode friendliesNode = new DefaultMutableTreeNode("FRIENDLIES (0)");
        DefaultMutableTreeNode enemiesNode = new DefaultMutableTreeNode("ENEMIES (0)");

        rootNode.add(friendliesNode);
        rootNode.add(enemiesNode);

        int friendlyCount = 0;
        int enemyCount = 0;

        for(Entity e : world.getEntities().values()){

            if (e == world.getPlayer())
                continue;

            boolean isFriendly = e.getIff() == Entity.IFF.FRIEND;

            DefaultMutableTreeNode targetNode;

            if(isFriendly){
                targetNode = new DefaultMutableTreeNode("Friendly #" + e.getId());
                friendlyCount++;
            }
            else{
                targetNode = new DefaultMutableTreeNode("Enemy #" + e.getId());
                enemyCount++;
            }


            // ID ****************************************************************
           // targetNode.add(new DefaultMutableTreeNode("ID : " + e.getId()));


            // Relative Position ****************************************************************
            Transform player = world.getPlayer().getComponent(Transform.class);
            Transform target = e.getComponent(Transform.class);

            Vec3 rel = new Vec3(
                    target.position.x-player.position.x,
                    target.position.y-player.position.y,
                    target.position.z-player.position.z
            );

            double rangeNM = metersToNM(rel.length());

            double bearing = Math.toDegrees(
                    Math.atan2(rel.x, rel.y));

            if(bearing < 0)
                bearing += 360;


            Radar radar = world.getPlayer().getComponent(Radar.class);
            bearing -= radar.getHeading();

            while(bearing > 180)
                bearing -= 360;

            while(bearing < -180)
                bearing += 360;

            double altitudeFt = metersToFeet(target.position.z);

            Velocity velocity = e.getComponent(Velocity.class);
            Vec3 velVec = velocity.getVelocity();
            double speedKt = metersPerSecondToKnots(velVec.length());

            targetNode.add(new DefaultMutableTreeNode(String.format("Range : %.1f NM", rangeNM)));
            targetNode.add(new DefaultMutableTreeNode(String.format("Bearing : %+d°", (int)Math.round(bearing))));
            targetNode.add(new DefaultMutableTreeNode(String.format("Altitude : %.0f ft", altitudeFt)));
            targetNode.add(new DefaultMutableTreeNode(String.format("Speed : %.0f kt", speedKt)));

            DefaultMutableTreeNode relNode = new DefaultMutableTreeNode("Relative Position");

            relNode.add(new DefaultMutableTreeNode(String.format("X : %.2f NM",metersToNM(rel.x))));
            relNode.add(new DefaultMutableTreeNode(String.format("Y : %.2f NM",metersToNM(rel.y))));
            relNode.add(new DefaultMutableTreeNode(String.format("Z : %.0f ft",metersToFeet(rel.z))));

            targetNode.add(relNode);


            // Position ****************************************************************
            DefaultMutableTreeNode posNode = new DefaultMutableTreeNode("Position");
            posNode.add(new DefaultMutableTreeNode(String.format("X : %.2f NM", metersToNM(target.position.x))));
            posNode.add(new DefaultMutableTreeNode(String.format("Y : %.2f NM", metersToNM(target.position.y))));
            posNode.add(new DefaultMutableTreeNode(String.format("Z : %.0f ft", metersToFeet(target.position.z))));

            targetNode.add(posNode);


            // Velocity ****************************************************************


            DefaultMutableTreeNode velNode = new DefaultMutableTreeNode("Velocity");

            velNode.add(new DefaultMutableTreeNode(String.format("X : %.1f kt", metersPerSecondToKnots(velocity.getVelocity().x))));
            velNode.add(new DefaultMutableTreeNode(String.format("Y : %.1f kt", metersPerSecondToKnots(velocity.getVelocity().y))));
            velNode.add(new DefaultMutableTreeNode(String.format("Z : %.1f kt", metersPerSecondToKnots(velocity.getVelocity().z))));

            targetNode.add(velNode);


            if(isFriendly)
                friendliesNode.add(targetNode);
            else
                enemiesNode.add(targetNode);
        }

        friendliesNode.setUserObject("FRIENDLIES (" + friendlyCount + ")");
        enemiesNode.setUserObject("ENEMIES (" + enemyCount + ")");

        treeModel.reload();

        for(int i=0;i<tree.getRowCount();i++)
            tree.expandRow(i);
    }



    public DefaultMutableTreeNode getRootNode() {
        return rootNode;
    }

    public DefaultTreeModel getTreeModel() {
        return treeModel;
    }

    public JTree getTree() {
        return tree;
    }

    private double metersToNM(double meters){
        return meters / 1852.0;
    }

    private double metersPerSecondToKnots(double mps){
        return mps * 1.943844;
    }

    private double metersToFeet(double meters){
        return meters * 3.28084;
    }
}