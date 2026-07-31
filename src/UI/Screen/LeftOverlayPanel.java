package UI.Screen;

import javax.swing.*;
import java.awt.*;

public class LeftOverlayPanel extends JPanel {

    private JLabel rangeLabel;
    private JLabel azimuthLabel;
    private JLabel barsLabel;

    public LeftOverlayPanel() {

        setBackground(Color.BLACK);
        setOpaque(false);

        setLayout(new GridLayout(5, 1));

        JPanel slot1 = new JPanel(new GridLayout(2, 1));
        slot1.setOpaque(false);

        slot1.add(new JLabel());

        TrianglePanel upTriangle = new TrianglePanel(true);
        slot1.add(upTriangle);

        add(slot1);

        JPanel slot2 = new JPanel(new GridLayout(2, 1));
        slot2.setOpaque(false);

        rangeLabel = createLabel("80");
        slot2.add(rangeLabel);

        TrianglePanel downTriangle = new TrianglePanel(false);
        slot2.add(downTriangle);

        add(slot2);

        JPanel slot3 = new JPanel(new BorderLayout());
        slot3.setOpaque(false);

        azimuthLabel = createLabelVertical("A", "6");

        slot3.add(azimuthLabel, BorderLayout.CENTER);

        add(slot3);

        JPanel slot4 = new JPanel(new BorderLayout());
        slot4.setOpaque(false);

        barsLabel = createLabelVertical("4", "B");

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

        azimuthLabel.setText(
                "<html><div style='text-align:center;'>A<br>" + value + "</div></html>"
        );
    }

    public void setBars(int bars) {

        barsLabel.setText(
                "<html><div style='text-align:center;'>" + bars + "<br>B</div></html>"
        );
    }
    
    private JLabel createLabel(String text){

        JLabel label = new JLabel(text);

        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);
        label.setForeground(Color.WHITE);
        label.setOpaque(true);
        label.setBackground(Color.BLACK);

        label.setFont(label.getFont().deriveFont(Font.BOLD, 24f));

        return label;
    }

    private JLabel createLabelVertical(String upper,String lower){

        JLabel label = new JLabel(
                "<html><div style='text-align:center;'>"+upper+"<br>"+lower+"</div></html>"
        );

        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);
        label.setForeground(Color.WHITE);

        label.setOpaque(true);
        label.setBackground(Color.BLACK);

        label.setFont(label.getFont().deriveFont(Font.BOLD, 24f));

        return label;
    }
}