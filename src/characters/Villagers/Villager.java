package characters.Villagers;

import game2D.Animation;
import game2D.Sprite;

/**
 * Created by Nikolaos Kouroumalos on 02/03/2018
 *
 * @author Nikolaos Kouroumalos
 * @version 0.1
 */
public class Villager extends Sprite {

    private int id;
    private String name;
    private int health;
    private boolean isDead;

    private boolean moving;

    public Villager(Animation anim) {
        super(anim);
    }

    public void getAction()
    {

    }

    public boolean isMoving() {
        return moving;
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    public void deductHealth(int amountToDeduct)
    {
        if (health > 0)
        {
            health -= amountToDeduct;
        }
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getHealth()
    {
        return health;
    }

    public void setDead()
    {
        isDead = true;
    }

    public boolean isDead()
    {
        return isDead;
    }

}
