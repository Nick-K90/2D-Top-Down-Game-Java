package Weapons;

import game.Direction;
import game2D.Animation;
import game2D.Sprite;

/**
 * Created by Nikolaos Kouroumalos on 31/03/2018
 *
 * @author Nikolaos Kouroumalos
 * @version 0.1
 */
public class Weapon extends Sprite {

    private Direction direction;
    private int weaponID;
    private Animation swordWest, swordEast, swordNorth, swordSouth;
    private Animation bowWest, bowEast, bowNorth, bowSouth;

    private int damage;
    private int swordDamage = 1;

    public Weapon(Animation anim) {
        super(anim);

        swordWest = new Animation();
        swordWest.loadAnimationFromSheet("images/sword-left-swing.png", 3, 1, 80);
        swordEast = new Animation();
        swordEast.loadAnimationFromSheet("images/sword-right-swing.png", 3, 1, 80);
        swordSouth = new Animation();
        swordSouth.loadAnimationFromSheet("images/sword-down-swing.png", 3, 1, 80);
        swordNorth = new Animation();
        swordNorth.loadAnimationFromSheet("images/sword-up-swing.png", 3, 1, 80);

        bowEast = new Animation();
        bowEast.loadAnimationFromSheet("images/bow-right-attack.png", 3, 1, 80);
        bowWest = new Animation();
        bowWest.loadAnimationFromSheet("images/bow-left-attack.png", 3, 1, 80);
        bowSouth = new Animation();
        bowSouth.loadAnimationFromSheet("images/bow-down-attack.png", 3, 1, 80);
        bowNorth = new Animation();
        bowNorth.loadAnimationFromSheet("images/bow-up-attack.png", 3, 1, 80);
        weaponID = 0;

    }

    public void getAction() {

        if (direction == Direction.WEST)
        {
            if (weaponID == 1)
            {
                damage = swordDamage;
                setAnimation(swordWest);
            }
            else if(weaponID == 2)
            {
                setAnimation(bowWest);
            }
        }
        else if (direction == Direction.EAST)
        {
            if (weaponID == 1)
            {
                damage = swordDamage;
                setAnimation(swordEast);
            }
            else if(weaponID == 2)
            {
                setAnimation(bowEast);
            }
        }
        else if (direction == Direction.NORTH)
        {
            if (weaponID == 1)
            {
                damage = swordDamage;
                setAnimation(swordNorth);
            }
            else if(weaponID == 2)
            {
                setAnimation(bowNorth);
            }
        }
        else if (direction == Direction.SOUTH)
        {
            if (weaponID == 1)
            {
                damage = swordDamage;
                setAnimation(swordSouth);
            }
            else if(weaponID == 2)
            {
                setAnimation(bowSouth);
            }
        }

        this.setAnimationSpeed(1);
        this.playAnimation();
    }

    public void setDirection(Direction direction)
    {
        this.direction = direction;
    }

    public void setWeaponID(int weaponID)
    {
        this.weaponID = weaponID;
    }

    public void resetAnimation()
    {
        setAnimation(swordWest);
        setAnimationFrame(2);
    }

    public int getDamage()
    {
        return damage;
    }


}
