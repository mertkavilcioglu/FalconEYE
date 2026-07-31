package UI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;

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

        for(Entity e : world.getEntities().values()){

            if (e == world.getPlayer())
                continue;

            String targetType;

            switch (e.getIff()) {

                case FRIEND:
                    targetType = "Friendly";
                    break;

                case HOSTILE:
                    targetType = "Enemy";
                    break;

                case SUSPECT:
                    targetType = "Suspect";
                    break;

                case NEUTRAL:
                    targetType = "Neutral";
                    break;

                default:
                    targetType = "Unknown";
                    break;
            }

            DefaultMutableTreeNode targetNode =
                    new DefaultMutableTreeNode(targetType + " #" + e.getId());

            //------------------------------------
            // ID
            //------------------------------------

            targetNode.add(new DefaultMutableTreeNode(
                    "ID : " + e.getId()
            ));

            //------------------------------------
            // Relative Position
            //------------------------------------

            Transform player =
                    world.getPlayer().getComponent(Transform.class);

            Transform target =
                    e.getComponent(Transform.class);

            Vec3 rel = new Vec3(
                    target.position.x-player.position.x,
                    target.position.y-player.position.y,
                    target.position.z-player.position.z
            );

            DefaultMutableTreeNode relNode =
                    new DefaultMutableTreeNode("Relative Position");

            relNode.add(new DefaultMutableTreeNode(
                    String.format("X : %.2f NM",metersToNM(rel.x))
            ));

            relNode.add(new DefaultMutableTreeNode(
                    String.format("Y : %.2f NM",metersToNM(rel.y))
            ));

            relNode.add(new DefaultMutableTreeNode(
                    String.format("Z : %.0f ft",metersToFeet(rel.z))
            ));

            targetNode.add(relNode);

            //------------------------------------
            // Position
            //------------------------------------

            DefaultMutableTreeNode posNode =
                    new DefaultMutableTreeNode("Position");

            posNode.add(new DefaultMutableTreeNode(
                    String.format("X : %.2f NM",
                            metersToNM(target.position.x))
            ));

            posNode.add(new DefaultMutableTreeNode(
                    String.format("Y : %.2f NM",
                            metersToNM(target.position.y))
            ));

            posNode.add(new DefaultMutableTreeNode(
                    String.format("Z : %.0f ft",
                            metersToFeet(target.position.z))
            ));

            targetNode.add(posNode);

            //------------------------------------
            // Velocity
            //------------------------------------

            Velocity vel = e.getComponent(Velocity.class);

            DefaultMutableTreeNode velNode =
                    new DefaultMutableTreeNode("Velocity");

            velNode.add(new DefaultMutableTreeNode(
                    String.format("X : %.1f kt",
                            metersPerSecondToKnots(
                                    vel.getVelocity().x))
            ));

            velNode.add(new DefaultMutableTreeNode(
                    String.format("Y : %.1f kt",
                            metersPerSecondToKnots(
                                    vel.getVelocity().y))
            ));

            velNode.add(new DefaultMutableTreeNode(
                    String.format("Z : %.1f kt",
                            metersPerSecondToKnots(
                                    vel.getVelocity().z))
            ));

            targetNode.add(velNode);

            //------------------------------------

            rootNode.add(targetNode);
        }

        treeModel.reload();

        for(int i=0;i<tree.getRowCount();i++)
            tree.expandRow(i);
    }



    //--------------------------------------------------
    // GETTERS
    //--------------------------------------------------

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