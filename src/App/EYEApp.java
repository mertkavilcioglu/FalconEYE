package App;
import Mathf.Vec3;
import Sim.Entity;
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
        world = new World(this);
        window = new AppWindow(this);
        window.setVisible(true);

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                //mapView.initializeTheMap();
                simTimer = new Timer(delta, new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        world.update(delta);
                        //window.repaint(); //TODO: herhangi bir panelin çiziminde sıkıntı olursa burayı tekrar aç ve dene
                        window.getMfdView()
                                .getRadarScreen()
                                .getMfdCanvas()
                                .repaint();
                    }
                });
                simTimer.start();
            }
        });
    }

    public AppWindow getWindow(){
        return window;
    }

    public World getWorld(){
        return world;
    }
}
