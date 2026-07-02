package App;
import Mathf.Vec3;
import Sim.World;
import UI.AppWindow;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.lang.reflect.InvocationTargetException;

public class EYEApp {
    private Timer simTimer;
    private AppWindow window;
    private World world;
    private int delta = 250;

    public void runWithWindow() throws InterruptedException, InvocationTargetException {
        window = new AppWindow(this);
        window.setVisible(true);

        world = new World(this);
        //world.createEntity(new Vec3(1,1,1), new Vec3(2,2,2));

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                //mapView.initializeTheMap();
                simTimer = new Timer(delta, new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        world.update(delta);
                        window.repaint();
                    }
                });
                simTimer.start();
            }
        });
    }
}
