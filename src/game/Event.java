package game;

import game2D.Animation;
import game2D.Sprite;

/**
 * Created by Nikolaos Kouroumalos on 08/04/2018
 *
 * @author Nikolaos Kouroumalos
 * @version 0.1
 */
public class Event extends Sprite {
    private int eventID;

    public Event(Animation anim) {
        super(anim);
    }

    public void setEventID(int eventID)
    {
        this.eventID = eventID;
    }
    public int getEventID() {
        return eventID;
    }
}
