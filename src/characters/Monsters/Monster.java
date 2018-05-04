package characters.Monsters;

import characters.Player;
import game.MonsterID;
import game2D.Animation;
import game2D.Sprite;

/**
 * Created by Nikolaos Kouroumalos on 09/04/2018
 *
 * @author Nikolaos Kouroumalos
 * @version 0.1
 */
public class Monster extends Sprite {

    private MonsterID monsterID;
    private int health;
    private boolean isDead;
    private int damage;
    private boolean spawnProjectile;


    public Monster(Animation anim,MonsterID monsterID, int health, int damage) {
        super(anim);

        this.monsterID = monsterID;
        this.health = health;
        this.damage = damage;

        spawnProjectile = false;
    }

    public void getAction(Player player)
    {

    }

    public void deductHealth(int amountToDeduct)
    {
        if (health > 0)
        {
            health -= amountToDeduct;
        }
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

    public MonsterID getMonsterID() {
        return monsterID;
    }
    public int getDamage() {
        return damage;
    }

    public boolean isSpawnProjectile() {
        return spawnProjectile;
    }

    public void setSpawnProjectile(boolean spawnProjectile)
    {
        this.spawnProjectile = spawnProjectile;
    }
}
