package UI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class SpawnColumnPanel extends JPanel {

    private JLabel iconLabel;
    private JLabel titleLabel;

    private JButton closeSingleButton;
    private JButton closeFormationButton;

    private JButton mediumSingleButton;
    private JButton mediumFormationButton;

    private JButton longSingleButton;
    private JButton longFormationButton;

    private JButton deleteButton;

    private ImageIcon originalIcon;

    public SpawnColumnPanel(String title, Color titleColor){

        setOpaque(false);
        setBorder(new EmptyBorder(15,15,15,15));
        setLayout(new BorderLayout(0,15));

        // TOP
        JPanel top = new JPanel();
        top.setOpaque(false);
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));

        iconLabel = new JLabel("ICON");
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        titleLabel = new JLabel(title);
        titleLabel.setForeground(titleColor);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD,22f));

        top.add(iconLabel);
        top.add(Box.createVerticalStrut(10));
        top.add(titleLabel);

        // BUTTONS
        closeSingleButton = new ToolButton("Single");
        closeFormationButton = new ToolButton("Formation");

        mediumSingleButton = new ToolButton("Single");
        mediumFormationButton = new ToolButton("Formation");

        longSingleButton = new ToolButton("Single");
        longFormationButton = new ToolButton("Formation");

        deleteButton = new ToolButton("DELETE ALL", true);
        deleteButton.setBackground(new Color(120,35,35));

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        content.add(Box.createVerticalStrut(12));

        int gapBetweenSections = 35; //25

        content.add(createRangeSection(
                "CLOSE RANGE ",
                "(10 - 40 NM)",
                closeSingleButton,
                closeFormationButton));

        content.add(Box.createVerticalStrut(gapBetweenSections));

        content.add(createRangeSection(
                "MEDIUM RANGE",
                "(40 - 70 NM)",
                mediumSingleButton,
                mediumFormationButton));

        content.add(Box.createVerticalStrut(gapBetweenSections));

        content.add(createRangeSection(
                "LONG RANGE",
                "(70 - 100 NM)",
                longSingleButton,
                longFormationButton));

        content.add(Box.createVerticalStrut(gapBetweenSections));

        deleteButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        deleteButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, deleteButton.getPreferredSize().height));

        content.add(deleteButton);

        add(top, BorderLayout.NORTH);
        add(content, BorderLayout.CENTER);

        addComponentListener(new ComponentAdapter() {

            @Override
            public void componentResized(ComponentEvent e){
                updateIcon();
            }

        });
    }

    private JPanel createRangeSection(String title, String description, JButton singleButton, JButton formationButton){

        JPanel panel = new JPanel();
        panel.setOpaque(true);
        panel.setBackground(UITheme.SECTION_BG);

        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.BORDER,1),
                new EmptyBorder(12,12,12,12)
        ));

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setForeground(UITheme.TITLE);
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD,17f));

        JLabel descLabel = new JLabel(description);
        descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        descLabel.setForeground(UITheme.DESCRIPTION);
        descLabel.setFont(descLabel.getFont().deriveFont(Font.PLAIN,12f));

        // SEPARATOR
        JSeparator separator = new JSeparator();
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE,1));
        separator.setForeground(UITheme.SEPARATOR);
        separator.setBackground(UITheme.SEPARATOR);


        // BUTTONS
        singleButton.setText("Create Single");
        formationButton.setText("Create in Formation");
        JPanel buttons = new JPanel(new GridLayout(2,1,0,10));
        buttons.setOpaque(false);
        buttons.add(singleButton);
        buttons.add(formationButton);


        // LAYOUT
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(4));
        panel.add(descLabel);
        panel.add(Box.createVerticalStrut(6));
        panel.add(separator);
        panel.add(Box.createVerticalStrut(10));
        panel.add(buttons);

        return panel;
    }

    public JButton getCloseSingleButton(){
        return closeSingleButton;
    }

    public JButton getCloseFormationButton(){
        return closeFormationButton;
    }

    public JButton getMediumSingleButton(){
        return mediumSingleButton;
    }

    public JButton getMediumFormationButton(){
        return mediumFormationButton;
    }

    public JButton getLongSingleButton(){
        return longSingleButton;
    }

    public JButton getLongFormationButton(){
        return longFormationButton;
    }

    public JButton getDeleteButton(){
        return deleteButton;
    }

    public JLabel getIconLabel(){
        return iconLabel;
    }

    public void setIcon(ImageIcon icon){

        originalIcon = icon;
        updateIcon();
    }

    private void updateIcon(){

        if(originalIcon == null)
            return;

        int size = (int)(getWidth() * 0.35);

        if(size < 32)
            size = 32;

        Image img = originalIcon.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH);

        iconLabel.setIcon(new ImageIcon(img));
        iconLabel.setText("");
    }
}