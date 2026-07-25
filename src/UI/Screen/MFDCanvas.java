package UI.Screen;

import Sim.Entity;
import Sim.World;

import javax.swing.*;
import java.awt.*;

public class MFDCanvas extends JPanel {

    private World world;

    public MFDCanvas(World world){
        this.world = world;
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);

        // draw entities
        for(Entity e : world.getEntities().values()){
            if(e != world.player){
                //Draw
            }
        }
    }
}
