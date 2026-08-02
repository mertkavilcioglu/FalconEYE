package App;
import Core.World;
import UI.AppWindow;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.lang.reflect.InvocationTargetException;

public class EYEApp {
    private Timer simTimer;
    private AppWindow window;
    private World world;
    private int delta = 50;

    public void runWithWindow() throws InterruptedException, InvocationTargetException {
        world = new World(this);
        window = new AppWindow(this);
        window.setVisible(true);

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                simTimer = new Timer(delta, new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        world.update(delta);
                        //window.repaint();
                        window.getMfdView().getRadarScreen().getMfdCanvas().repaint();
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
