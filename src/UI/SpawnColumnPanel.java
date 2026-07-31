package UI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class SpawnColumnPanel extends JPanel {

    private JLabel iconLabel;
    private JLabel titleLabel;

    private JButton createButton;
    private JButton formationButton;
    private JButton deleteButton;

    private ImageIcon originalIcon;

    public SpawnColumnPanel(String title, Color titleColor){

        setOpaque(false);
        setBorder(new EmptyBorder(15,15,15,15));

        setLayout(new BorderLayout(0,15));


        //---------------- TOP ----------------

        JPanel top = new JPanel();
        top.setOpaque(false);
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));


        iconLabel = new JLabel("ICON");
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        titleLabel = new JLabel(title);
        titleLabel.setForeground(titleColor);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        Font f = titleLabel.getFont();
        titleLabel.setFont(
                f.deriveFont(Font.BOLD, 22f)
        );

        top.add(iconLabel);
        top.add(Box.createVerticalStrut(10));
        top.add(titleLabel);


        //---------------- BUTTONS ----------------

        JPanel buttons = new JPanel(new GridLayout(3,1,0,20));
        buttons.setOpaque(false);

        createButton = new ToolButton("Create Single");

        formationButton = new ToolButton("Create in Formation");

        deleteButton = new ToolButton("Delete All");

        deleteButton.setBackground(new Color(120,35,35));

        buttons.add(createButton);
        buttons.add(formationButton);
        buttons.add(deleteButton);

        add(top, BorderLayout.NORTH);
        add(buttons, BorderLayout.CENTER);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateIcon();
            }
        });
    }


    public JButton getCreateButton(){
        return createButton;
    }

    public JButton getFormationButton(){
        return formationButton;
    }

    public JLabel getIconLabel(){
        return iconLabel;
    }

    public JButton getDeleteButton() {
        return deleteButton;
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

        Image img = originalIcon.getImage().getScaledInstance(
                size,
                size,
                Image.SCALE_SMOOTH
        );

        iconLabel.setIcon(new ImageIcon(img));
        iconLabel.setText("");
    }
}