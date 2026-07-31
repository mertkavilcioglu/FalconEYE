package UI.Screen;

import javax.swing.*;
import java.awt.*;

public class LeftOverlayPanel extends JPanel {

    private JLabel rangeLabel;
    private JLabel azimuthLabel;
    private JLabel barsLabel;

    private TrianglePanel upTriangle;
    private TrianglePanel downTriangle;

    public LeftOverlayPanel() {

        setBackground(Color.BLACK);
        setOpaque(false);

        setLayout(new GridLayout(5, 1));

        JPanel slot1 = new JPanel(new BorderLayout());
        slot1.setOpaque(false);

        upTriangle = new TrianglePanel(true);
        slot1.add(upTriangle, BorderLayout.CENTER);

        add(slot1);

        JPanel slot2 = new JPanel(new GridLayout(2, 1));
        slot2.setOpaque(false);

        rangeLabel = createLabel("40", 25);
        slot2.add(rangeLabel);

        downTriangle = new TrianglePanel(false);
        slot2.add(downTriangle);

        add(slot2);

        JPanel slot3 = new JPanel(new BorderLayout());
        slot3.setOpaque(false);

        int verticalMargin = 1;

        azimuthLabel = createLabelVertical("A", "6", verticalMargin);

        slot3.add(azimuthLabel, BorderLayout.CENTER);

        add(slot3);

        JPanel slot4 = new JPanel(new BorderLayout());
        slot4.setOpaque(false);

        barsLabel = createLabelVertical("4", "B", verticalMargin);

        slot4.add(barsLabel, BorderLayout.CENTER);

        add(slot4);


        JPanel slot5 = new JPanel();
        slot5.setOpaque(false);

        add(slot5);
    }

    //--------------------------------------------------
    // UPDATE FUNCTIONS
    //--------------------------------------------------

    public void setRange(int range) {

        rangeLabel.setText(String.valueOf(range));

        if(range >= 160){
            upTriangle.setTriangleEnabled(false);
        }
        else{
            upTriangle.setTriangleEnabled(true);
        }

        if(range <= 5){
            downTriangle.setTriangleEnabled(false);
        }
        else{
            downTriangle.setTriangleEnabled(true);
        }
        revalidate();
        repaint();
    }

    public void setAzimuth(int azimuth) {

        String value;

        switch (azimuth) {

            case 120:
                value = "6";
                break;

            case 60:
                value = "3";
                break;

            case 20:
                value = "1";
                break;

            default:
                value = "?";
                break;
        }

        azimuthLabel.setText("<html><div style='text-align:center;'>A<br>" + value + "</div></html>");
    }

    public void setBars(int bars) {

        barsLabel.setText("<html><div style='text-align:center;'>" + bars + "<br>B</div></html>");
    }

    private JLabel createLabel(String text){
        return new OverlayLabel(text);
    }

    private JLabel createLabel(String text, int margin){
        return new OverlayLabel(text, margin);
    }

    private JLabel createLabelVertical(String upper, String lower){
        return new OverlayLabel(
                "<html><div style='text-align:center;'>"
                        + upper +
                        "<br>" +
                        lower +
                        "</div></html>"
        );
    }

    private JLabel createLabelVertical(String upper, String lower, int margin){
        return new OverlayLabel(
                "<html><div style='text-align:center;'>"
                        + upper +
                        "<br>"
                        + lower +
                        "</div></html>",
                margin
        );
    }
}