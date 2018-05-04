package characters.Monsters;

import characters.Player;
import game.Collision;
import game.MonsterID;
import game2D.Animation;
import game2D.Sprite;

/**
 * Created by Nikolaos Kouroumalos on 09/04/2018
 *
 * @author Nikolaos Kouroumalos
 * @version 0.1
 */
public class Bat extends Monster {

    private float speed;
    private Collision collision;
    private boolean chase;

    private Animation batAnimation, deathAnimation;

    private static final int HEALTH = 1;
    private static final int DAMAGE = 1;

    public Bat(Animation anim) {
        super(anim, MonsterID.BAT, HEALTH, DAMAGE);

        batAnimation = new Animation();
        batAnimation.loadAnimationFromSheet("sprites/monsters/bat.png", 2,1,100);

        deathAnimation = new Animation();
        deathAnimation.loadAnimationFromSheet("sprites/monsters/monster-death.png", 4,1,100);

        setAnimation(batAnimation);
        setAnimationSpeed(1);
        pauseAnimation();

        speed = 0.2f;
        collision = new Collision();
        chase = false;

        setChasingRadius(300);
    }


    @Override
    public void getAction(Player player) {
        //super.getAction(playerX, playerY);

        if(getHealth() > 0) {

            checkIfPlayerInRange(player);

            if (chase) {

                playAnimation();

                rotate(player.getX(), player.getY());

                if (getX() < player.getX()) {
                    setVelocityX(speed);
                } else {
                    setVelocityX(-speed);
                }
                if (getY() < player.getY()) {
                    setVelocityY(speed);
                } else {
                    setVelocityY(-speed);
                }
            }
        }
        else
        {
            setAnimation(deathAnimation);
            if(getAnimation().hasLooped())
            {
                setDead();
            }

        }

    }

    /**
     * Checks if the player is within the bats radius in order to start chasing him.
     * @param player The player
     */
    private void checkIfPlayerInRange(Sprite player)
    {
        if(collision.chasingRadiusCollision(player, this))
        {
            chase = true;
        }
    }

    /**
     * A method that calculates the angle for rotation based on the players X and Y position as well
     * as the monsters X and Y.
     * @param playerX
     * @param playerY
     */
    private void rotate(float playerX, float playerY)
    {
        float axisX, axisY;
        axisX = getX();
        axisY = getY();
        float cartesianX,cartesianY;
        cartesianX = playerX - axisX;
        cartesianY = playerY - axisY;
        float x, y;
        x = cartesianX * cartesianX;
        y = cartesianY * cartesianY;
        float radius = (float)Math.sqrt(x + y);

        float angleInRadians = (float)Math.acos(cartesianX / radius);

        float angleInDegrees = angleInRadians * 57;

        setRotation(angleInDegrees);
    }

    @Override
    public void deductHealth(int amountToDeduct) {
        super.deductHealth(amountToDeduct);
    }
}
