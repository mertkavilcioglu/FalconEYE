package App;
import UI.AppWindow;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.lang.reflect.InvocationTargetException;

public class EYEApp {
    private Timer simTimer;
    private AppWindow window;

    public void runWithWindow() throws InterruptedException, InvocationTargetException {
        window = new AppWindow(this);
        window.setVisible(true);

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                int delta = 1000; // 1000
                //mapView.initializeTheMap();

                simTimer = new Timer(delta, new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // UPDATE EVERY COMPONENT

                        window.repaint();
                    }
                });
            }
        });
    }
}
