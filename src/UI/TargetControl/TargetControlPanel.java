package UI.TargetControl;

import UI.UITheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class TargetControlPanel extends JPanel {

    private SpawnColumnPanel friendlyPanel;
    private SpawnColumnPanel enemyPanel;

    public TargetControlPanel() {

        setBackground(UITheme.PANEL_BG);

        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) (screen.width * 0.21875);
        setPreferredSize(new Dimension(width, 0));

        setLayout(new BorderLayout());

        JPanel topContainer = new JPanel();
        topContainer.setOpaque(false);
        topContainer.setLayout(new BoxLayout(topContainer, BoxLayout.Y_AXIS));
        topContainer.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("CREATE TARGETS");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setForeground(UITheme.TITLE);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 35f));

        topContainer.add(title);
        topContainer.add(Box.createVerticalStrut(10));


        JSeparator separator = new JSeparator();
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));

        topContainer.add(separator);
        topContainer.add(Box.createVerticalStrut(12));

        JLabel description = new JLabel(
                "<html><div style='text-align:center;'>"
                        + "Create targets in random positions "
                        + "within the maximum radar coverage."
                        + "</div></html>"
        );

        description.setAlignmentX(Component.CENTER_ALIGNMENT);
        description.setHorizontalAlignment(SwingConstants.CENTER);
        description.setForeground(UITheme.DESCRIPTION);
        description.setFont(description.getFont().deriveFont(Font.PLAIN, 18f));

        topContainer.add(description);
        topContainer.add(Box.createVerticalStrut(20));

        JPanel cards = new JPanel(new GridLayout(1, 2, 15, 0));
        cards.setOpaque(false);

        friendlyPanel = new SpawnColumnPanel("FRIENDLY", UITheme.FRIENDLY);
        enemyPanel = new SpawnColumnPanel("ENEMY", UITheme.ENEMY);

        JPanel friendlyCard = createCard(friendlyPanel);
        JPanel enemyCard = createCard(enemyPanel);

        cards.add(friendlyCard);
        cards.add(enemyCard);
        topContainer.add(cards);

        add(topContainer, BorderLayout.NORTH);

        setBorder(BorderFactory.createCompoundBorder(
                new EmptyBorder(10, 10, 10, 10),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(UITheme.BORDER, 3),
                        new EmptyBorder(10, 10, 10, 10)
                )
        ));

        ImageIcon friendlyIcon = new ImageIcon(getClass().getResource("/Assets/nato_friendly_air.png"));
        ImageIcon enemyIcon = new ImageIcon(getClass().getResource("/Assets/nato_enemy_air.png"));

        friendlyPanel.setIcon(friendlyIcon);
        enemyPanel.setIcon(enemyIcon);


    }

    private JPanel createCard(JPanel content) {

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(UITheme.CARD_BG);

        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.BORDER),
                new EmptyBorder(10, 10, 10, 10)));

        card.add(content);
        return card;
    }

    public SpawnColumnPanel getFriendlyPanel() {
        return friendlyPanel;
    }

    public SpawnColumnPanel getEnemyPanel() {
        return enemyPanel;
    }
}