package UI.Screen;

import javax.swing.*;
import java.awt.*;

public class BottomOverlayPanel extends JPanel {

    private final JLabel swapLabel = createLabel("SWAP");
    private final JLabel fcrLabel  = createLabel("FCR");
    private final JLabel testLabel = createLabel("TEST");
    private final JLabel dteLabel  = createLabel("DTE");
    private final JLabel dcltLabel = createLabel("DCLT");

    public BottomOverlayPanel() {

        setOpaque(false);
        setLayout(null);

        add(swapLabel);
        add(fcrLabel);
        add(testLabel);
        add(dteLabel);
        add(dcltLabel);
    }

    @Override
    public void doLayout() {

        super.doLayout();

        int w = getWidth();
        int h = getHeight();

        int labelWidth = w / 8;
        int labelHeight = h;

        int xOffset = -(int)(w * 0.065);

        swapLabel.setBounds((int)(w * 0.18) + xOffset, 0, labelWidth, labelHeight);
        fcrLabel .setBounds((int)(w * 0.33) + xOffset, 0, labelWidth, labelHeight);
        testLabel.setBounds((int)(w * 0.50) + xOffset, 0, labelWidth, labelHeight);
        dteLabel .setBounds((int)(w * 0.67) + xOffset, 0, labelWidth, labelHeight);
        dcltLabel.setBounds((int)(w * 0.82) + xOffset, 0, labelWidth, labelHeight);
    }

    private JLabel createLabel(String text) {
        return new OverlayLabel(text);
    }
}