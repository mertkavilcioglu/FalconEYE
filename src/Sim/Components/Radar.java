package Sim.Components;

import Sim.Component;
import Sim.Entity;
import Sim.RadarContact;

import java.util.HashMap;

public class Radar extends Component {
    private int range = 74080; // 40NM
    private int azimuth = 60;
    private int bars = 4;
    private HashMap<Integer, RadarContact> contacts = new HashMap<>();

    public Radar(){

    }

    @Override
    public void update(int deltaTime) {
       // System.out.println("RADAR");
        scan();
    }

    public void scan(){
        for(Entity e : parent.getWorld().getEntities().values()){
            if(e == parent)
                continue;
            if(!contacts.containsKey(e.getId())){
                RadarContact data = new RadarContact(e); //todo: radar taraması yaparken contacts icinde yoksa ekle,
                // TODO: varsa data güncelle yap ve boylelikle RadarContact icinde Entity tutma konum velocity tut.
                // TODO: Şimdilik entity kullanıyor olacak
                contacts.put(e.getId(),data );
            }
            else{
                //TODO: Var olan datayı yeni bilgilerle güncelle ki next frame'de radar yeni konumları çizsin
                //TODO: burayı yapınca canvastaki drawEntity parametresini de entity değil radarContact'a çevir
            }

            /*

                 if(!contacts.containsKey(e.getId())) yerine

                 RadarContact c = contacts.get(id);

                    if(c == null)
                        createContact();
                    else
                        updateContact();

                   yapabilirsin

             */

        }
    }

    public int getRange(){
        return range;
    }

    public int getAzimuth(){
        return azimuth;
    }

    public HashMap<Integer, RadarContact> getContacts() {
        return contacts;
    }

}
