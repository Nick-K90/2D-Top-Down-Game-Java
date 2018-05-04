package characters.Monsters;

import characters.Player;
import game.Collision;
import game.Direction;
import game.MonsterID;
import game2D.Animation;
import game2D.Sprite;

/**
 * Created by Nikolaos Kouroumalos on 10/04/2018
 *
 * @author Nikolaos Kouroumalos
 * @version 0.1
 */
public class GreenStatue extends Monster {

    private float speed;
    private Collision collision;
    private boolean chase;

    private static final int HEALTH = 6;
    private static final int DAMAGE = 1;

    private Animation walkingEast, walkingWest, walkingSouth, walkingNorth, deathAnimation;

    public GreenStatue(Animation anim) {
        super(anim, MonsterID.GREEN_STATUE, HEALTH, DAMAGE);

        walkingEast = new Animation();
        walkingEast.loadAnimationFromSheet("sprites/monsters/green_statue/green-statue-walking-left.png", 3, 1, 100);
        walkingWest = new Animation();
        walkingWest.loadAnimationFromSheet("sprites/monsters/green_statue/green-statue-walking-right.png", 3, 1, 100);
        walkingSouth = new Animation();
        walkingSouth.loadAnimationFromSheet("sprites/monsters/green_statue/green-statue-walking-down.png", 3, 1, 100);
        walkingNorth = new Animation();
        walkingNorth.loadAnimationFromSheet("sprites/monsters/green_statue/green-statue-walking-up.png", 3, 1, 100);

        deathAnimation = new Animation();
        deathAnimation.loadAnimationFromSheet("sprites/monsters/monster-death.png", 3, 1, 80);

        speed = 0.1f;
        collision = new Collision();
        chase = false;

        setAnimation(walkingWest);
        pauseAnimation();
        setChasingRadius(300);
    }

    @Override
    public void getAction(Player player) {
        //super.getAction(playerX, playerY);

        if (getHealth() > 0) {

            checkIfPlayerInRange(player);

            if (chase) {

                playAnimation();

                if (getX() < player.getX()) {
                    setVelocityX(speed);
                    if (player.getDirection() == Direction.EAST) {
                        setAnimation(walkingWest);
                    }
                } else {
                    setVelocityX(-speed);
                    if (player.getDirection() == Direction.WEST) {
                        setAnimation(walkingEast);
                    }
                }
                if (getY() < player.getY()) {
                    setVelocityY(speed);
                    if (player.getDirection() == Direction.SOUTH) {
                        setAnimation(walkingSouth);
                    }

                } else {
                    setVelocityY(-speed);
                    if (player.getDirection() == Direction.NORTH) {
                        setAnimation(walkingNorth);
                    }

                }
            }
        } else {
            setAnimation(deathAnimation);
            if (getAnimation().hasLooped()) {
                setDead();
            }

        }

    }

    /**
     * Checks if the player is within the bats radius in order to start chasing him.
     *
     * @param player The player
     */
    private void checkIfPlayerInRange(Sprite player) {
        if (collision.chasingRadiusCollision(player, this)) {
            chase = true;
        }
    }

    @Override
    public void deductHealth(int amountToDeduct) {
        super.deductHealth(amountToDeduct);
    }

}
