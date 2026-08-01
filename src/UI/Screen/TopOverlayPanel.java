package UI.Screen;

import javax.swing.*;
import java.awt.*;

public class TopOverlayPanel extends JPanel {

    private final JLabel crmLabel = createLabel("CRM");
    private final JLabel twsLabel = createLabel("TWS");
    private final JLabel normLabel = createLabel("NORM");
    private final JLabel ovrdLabel = createLabel("OVRD");
    private final JLabel cntlLabel = createLabel("CNTL");

    public TopOverlayPanel() {

        setOpaque(false);
        setLayout(null);

        add(crmLabel);
        add(twsLabel);
        add(normLabel);
        add(ovrdLabel);
        add(cntlLabel);
    }

    @Override
    public void doLayout() {

        super.doLayout();

        int w = getWidth();
        int h = getHeight();
        int xOffset = -(int)(w * 0.065);
        int labelWidth = w / 8;
        int labelHeight = h;

        crmLabel.setBounds((int)(w * 0.18) + xOffset, 0, labelWidth, labelHeight);
        twsLabel.setBounds((int)(w * 0.33) + xOffset, 0, labelWidth, labelHeight);
        normLabel.setBounds((int)(w * 0.50) + xOffset, 0, labelWidth, labelHeight);
        ovrdLabel.setBounds((int)(w * 0.67) + xOffset, 0, labelWidth, labelHeight);
        cntlLabel.setBounds((int)(w * 0.82) + xOffset, 0, labelWidth, labelHeight);
    }

    private JLabel createLabel(String text) {
        return new OverlayLabel(text);
    }
}