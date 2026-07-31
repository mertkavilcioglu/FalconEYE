package UI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.HashMap;

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
    private SelectionListener selectionListener;

    private HashMap<Integer, TargetTreeNodes> nodeMap = new HashMap<>();

    private int hoveredRow = -1;

    public HierarchyView() {

        setBackground(UITheme.PANEL_BG);

        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) (screen.width * 0.21875);
        setPreferredSize(new Dimension(width, 0));

        setLayout(new BorderLayout());

        JPanel topContainer = new JPanel();
        topContainer.setOpaque(false);
        topContainer.setLayout(new BoxLayout(topContainer, BoxLayout.Y_AXIS));
        topContainer.setBorder(new EmptyBorder(20,20,20,20));

        JLabel title = new JLabel("TARGET HIERARCHY");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setForeground(UITheme.TITLE);
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
        description.setForeground(UITheme.DESCRIPTION);
        description.setFont(description.getFont().deriveFont(Font.PLAIN,18f));
        topContainer.add(description);
        topContainer.add(Box.createVerticalStrut(20));

        rootNode = new DefaultMutableTreeNode("TARGETS");

        treeModel = new DefaultTreeModel(rootNode);

        tree = new JTree(treeModel);

        tree.setRootVisible(true);
        tree.setShowsRootHandles(true);

        tree.setBackground(UITheme.CARD_BG);
        tree.setForeground(UITheme.TITLE);

        tree.setRowHeight(24);

        tree.setFont(tree.getFont().deriveFont(Font.PLAIN,16f));

        tree.addMouseMotionListener(new MouseMotionAdapter() {

            @Override
            public void mouseMoved(MouseEvent e) {

                int row = tree.getRowForLocation(e.getX(), e.getY());

                if(row != hoveredRow){
                    hoveredRow = row;
                    tree.repaint();
                }
            }
        });

        tree.addTreeSelectionListener(e -> {

            DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();


            if(node == null)
                return;

            int id = findEntityId(node);

            if(id == -1)
                return;

            if(selectionListener != null){
                selectionListener.onEntitySelected(id);
            }

        });

        tree.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseExited(MouseEvent e) {

                hoveredRow = -1;
                tree.repaint();
            }
        });

        DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer(){

            @Override
            public Component getTreeCellRendererComponent(
                    JTree tree,
                    Object value,
                    boolean sel,
                    boolean expanded,
                    boolean leaf,
                    int row,
                    boolean hasFocus){

                super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

                DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
                String text = node.getUserObject().toString();
                Font base = getFont();


                if(sel){
                    setBackgroundSelectionColor(UITheme.TREE_SELECTION);
                    setBackgroundNonSelectionColor(UITheme.TREE_BG);
                }
                else if(row == hoveredRow){
                    setBackgroundNonSelectionColor(UITheme.TREE_HOVER);
                }
                else{
                    setBackgroundNonSelectionColor(UITheme.TREE_BG);
                }

                if(text.startsWith("TARGETS")){
                    setForeground(UITheme.TARGETS);
                    setFont(base.deriveFont(Font.BOLD,18f));
                }
                else if(text.startsWith("FRIENDLIES")){
                    setForeground(UITheme.FRIENDLY);
                    setFont(base.deriveFont(Font.BOLD,18f));
                }
                else if(text.startsWith("ENEMIES")){
                    setForeground(UITheme.ENEMY);
                    setFont(base.deriveFont(Font.BOLD,18f));
                }
                else{
                    setForeground(UITheme.TITLE);
                    setFont(base.deriveFont(Font.PLAIN,16f));
                }

                return this;
            }
        };



        renderer.setBorderSelectionColor(null);

        renderer.setLeafIcon(null);
        renderer.setClosedIcon(null);
        renderer.setOpenIcon(null);


        tree.setCellRenderer(renderer);

        JScrollPane scrollPane = new JScrollPane(tree);

        scrollPane.setBorder(BorderFactory.createLineBorder(UITheme.BORDER, 1));
        scrollPane.getViewport().setBackground(UITheme.TREE_BG);

        scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {

            @Override
            protected void configureScrollBarColors() {
                thumbColor = UITheme.SCROLL_THUMB;
                trackColor = UITheme.SCROLL_TRACK;
            }

            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton();
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createZeroButton();
            }

            private JButton createZeroButton() {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(0,0));
                button.setMinimumSize(new Dimension(0,0));
                button.setMaximumSize(new Dimension(0,0));
                return button;
            }

            @Override
            protected Dimension getMinimumThumbSize() {
                return new Dimension(8, 30);
            }

            @Override
            protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {

                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if(isThumbRollover()){g2.setColor(UITheme.SCROLL_THUMB_HOVER);
                }
                else{
                    g2.setColor(UITheme.SCROLL_THUMB);
                }

                g2.fillRoundRect(thumbBounds.x + 2,
                        thumbBounds.y + 2,
                        thumbBounds.width - 4,
                        thumbBounds.height - 4,
                        8,
                        8
                );

                g2.dispose();
            }
        });

        topContainer.add(scrollPane);

        add(topContainer, BorderLayout.CENTER);

        setBorder(BorderFactory.createCompoundBorder(
                new EmptyBorder(10,10,10,10),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(UITheme.BORDER,3),
                        new EmptyBorder(10,10,10,10)
                )
        ));
    }


    public void addEntity(Entity e){

        boolean isFriendly = e.getIff() == Entity.IFF.FRIEND;

        DefaultMutableTreeNode parentNode;

        if(isFriendly){
            parentNode = (DefaultMutableTreeNode) rootNode.getChildAt(0);
        }
        else{
            parentNode = (DefaultMutableTreeNode) rootNode.getChildAt(1);
        }

        TargetTreeNodes nodes = new TargetTreeNodes();

        nodes.target = new DefaultMutableTreeNode((isFriendly ? "Friendly #" : "Enemy #") + e.getId());

        nodes.range = new DefaultMutableTreeNode();
        nodes.bearing = new DefaultMutableTreeNode();
        nodes.altitude = new DefaultMutableTreeNode();
        nodes.speed = new DefaultMutableTreeNode();

        nodes.target.add(nodes.range);
        nodes.target.add(nodes.bearing);
        nodes.target.add(nodes.altitude);
        nodes.target.add(nodes.speed);

        DefaultMutableTreeNode details = new DefaultMutableTreeNode("Details");

        DefaultMutableTreeNode rel = new DefaultMutableTreeNode("Relative Position");

        nodes.relX = new DefaultMutableTreeNode();
        nodes.relY = new DefaultMutableTreeNode();
        nodes.relZ = new DefaultMutableTreeNode();

        rel.add(nodes.relX);
        rel.add(nodes.relY);
        rel.add(nodes.relZ);

        DefaultMutableTreeNode pos = new DefaultMutableTreeNode("Position");

        nodes.posX = new DefaultMutableTreeNode();
        nodes.posY = new DefaultMutableTreeNode();
        nodes.posZ = new DefaultMutableTreeNode();

        pos.add(nodes.posX);
        pos.add(nodes.posY);
        pos.add(nodes.posZ);

        DefaultMutableTreeNode vel = new DefaultMutableTreeNode("Velocity");

        nodes.velX = new DefaultMutableTreeNode();
        nodes.velY = new DefaultMutableTreeNode();
        nodes.velZ = new DefaultMutableTreeNode();

        vel.add(nodes.velX);
        vel.add(nodes.velY);
        vel.add(nodes.velZ);

        details.add(rel);
        details.add(pos);
        details.add(vel);

        nodes.target.add(details);

        parentNode.add(nodes.target);

        nodeMap.put(e.getId(), nodes);

        updateEntity(e);
        updateCounters();
    }

    public void updateEntity(Entity e){

        TargetTreeNodes nodes = nodeMap.get(e.getId());

        if(nodes == null)
            return;

        Transform player = e.getWorld().getPlayer().getComponent(Transform.class);
        Transform target = e.getComponent(Transform.class);

        Vec3 rel = new Vec3(
                target.position.x-player.position.x,
                target.position.y-player.position.y,
                target.position.z-player.position.z
        );

        Radar radar = e.getWorld().getPlayer().getComponent(Radar.class);

        double rangeNM = metersToNM(rel.length());
        double bearing = Math.toDegrees(Math.atan2(rel.x, rel.y));

        if(bearing < 0)
            bearing += 360;

        bearing -= radar.getHeading();

        while(bearing > 180)
            bearing -= 360;

        while(bearing < -180)
            bearing += 360;

        double altitudeFt = metersToFeet(target.position.z);

        Velocity velocity = e.getComponent(Velocity.class);
        Vec3 vel = velocity.getVelocity();

        double speedKt = metersPerSecondToKnots(vel.length());

        // HEADER
        nodes.range.setUserObject(String.format("Range : %.1f NM",rangeNM));
        nodes.bearing.setUserObject(String.format("Bearing : %+d°", (int)Math.round(bearing)));
        nodes.altitude.setUserObject(String.format("Altitude : %.0f ft", altitudeFt));
        nodes.speed.setUserObject(String.format("Speed : %.0f kt", speedKt));


        // RELATIVE
        nodes.relX.setUserObject(String.format("X : %.2f NM", metersToNM(rel.x)));
        nodes.relY.setUserObject(String.format("Y : %.2f NM", metersToNM(rel.y)));
        nodes.relZ.setUserObject(String.format("Z : %.0f ft", metersToFeet(rel.z)));


        // POSITION
        nodes.posX.setUserObject(String.format("X : %.2f NM", metersToNM(target.position.x)));
        nodes.posY.setUserObject(String.format("Y : %.2f NM", metersToNM(target.position.y)));
        nodes.posZ.setUserObject(String.format("Z : %.0f ft", metersToFeet(target.position.z)));


        // VELOCITY
        nodes.velX.setUserObject(String.format("X : %.1f kt", metersPerSecondToKnots(vel.x)));
        nodes.velY.setUserObject(String.format("Y : %.1f kt", metersPerSecondToKnots(vel.y)));
        nodes.velZ.setUserObject(String.format("Z : %.1f kt", metersPerSecondToKnots(vel.z)));

        treeModel.nodeChanged(nodes.range);
        treeModel.nodeChanged(nodes.bearing);
        treeModel.nodeChanged(nodes.altitude);
        treeModel.nodeChanged(nodes.speed);

        treeModel.nodeChanged(nodes.relX);
        treeModel.nodeChanged(nodes.relY);
        treeModel.nodeChanged(nodes.relZ);

        treeModel.nodeChanged(nodes.posX);
        treeModel.nodeChanged(nodes.posY);
        treeModel.nodeChanged(nodes.posZ);

        treeModel.nodeChanged(nodes.velX);
        treeModel.nodeChanged(nodes.velY);
        treeModel.nodeChanged(nodes.velZ);
    }

    private int extractEntityId(String text){

        int index = text.lastIndexOf('#');

        if(index == -1)
            return -1;

        return Integer.parseInt(text.substring(index + 1));
    }

    public void initialize(World world){

        rootNode.removeAllChildren();

        DefaultMutableTreeNode friendlies = new DefaultMutableTreeNode("FRIENDLIES");
        DefaultMutableTreeNode enemies = new DefaultMutableTreeNode("ENEMIES");

        rootNode.add(friendlies);
        rootNode.add(enemies);

        nodeMap.clear();

        for(Entity e : world.getEntities().values()){

            if(e == world.getPlayer())
                continue;

            addEntity(e);
        }

        treeModel.reload();
        updateCounters();

        expandAllExceptDetails();

    }

    public void updateAll(World world){

        for(Entity e : world.getEntities().values()){

            if(e == world.getPlayer())
                continue;

            updateEntity(e);
        }
    }

    public void removeEntity(Entity e){

        TargetTreeNodes nodes = nodeMap.remove(e.getId());

        if(nodes == null)
            return;

        MutableTreeNode parent = (MutableTreeNode) nodes.target.getParent();

        if(parent != null){
            treeModel.removeNodeFromParent(nodes.target);
        }

        updateCounters();
    }

    private void updateCounters(){

        DefaultMutableTreeNode friendlies = (DefaultMutableTreeNode) rootNode.getChildAt(0);
        DefaultMutableTreeNode enemies = (DefaultMutableTreeNode) rootNode.getChildAt(1);
        friendlies.setUserObject("FRIENDLIES (" + friendlies.getChildCount() + ")");
        enemies.setUserObject("ENEMIES (" + enemies.getChildCount() + ")");

        treeModel.nodeChanged(friendlies);
        treeModel.nodeChanged(enemies);
    }

    private int getParentEntityId(DefaultMutableTreeNode detailsNode){
        DefaultMutableTreeNode parent = (DefaultMutableTreeNode) detailsNode.getParent();
        return extractEntityId(parent.getUserObject().toString());
    }

    private void expandAllExceptDetails(){

        expandNode(new TreePath(rootNode));
    }

    private void expandNode(TreePath path){

        DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
        String text = node.getUserObject().toString();

        if(!text.equals("Details")){
            tree.expandPath(path);
        }

        for(int i = 0; i < node.getChildCount(); i++){

            TreePath childPath = path.pathByAddingChild(node.getChildAt(i));

            if(!node.getChildAt(i).toString().equals("Details")){
                expandNode(childPath);
            }
        }
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

    public interface SelectionListener{
        void onEntitySelected(int entityId);
    }

    public void setSelectionListener(SelectionListener listener){
        this.selectionListener = listener;
    }

    private int findEntityId(DefaultMutableTreeNode node){

        while(node != null){

            int id = extractEntityId(node.getUserObject().toString());

            if(id != -1)
                return id;

            node = (DefaultMutableTreeNode) node.getParent();
        }

        return -1;
    }
}