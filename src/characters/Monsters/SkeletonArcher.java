package characters.Monsters;

import characters.Player;
import game.Collision;
import game.MonsterID;
import game2D.Animation;
import game2D.Sprite;

/**
 * Created by Nikolaos Kouroumalos on 10/04/2018
 *
 * @author Nikolaos Kouroumalos
 * @version 0.1
 */
public class SkeletonArcher extends Monster {

    private static final int HEALTH = 3;
    private static final int DAMAGE = 1;

    private float speed;
    private Collision collision;
    private boolean chase;
    private boolean attackOnce;

    private Animation walkingEast, walkingWest, walkingSouth, walkingNorth;
    private Animation shootingLeft;
    private Animation deathAnimation;

    public SkeletonArcher(Animation anim) {
        super(anim, MonsterID.SKELETON_ARCHER, HEALTH, DAMAGE);

        walkingEast = new Animation();
        walkingEast.loadAnimationFromSheet("sprites/monsters/skeleton_archer/skeleton-walking-right.png", 3, 1, 100);
        walkingWest = new Animation();
        walkingWest.loadAnimationFromSheet("sprites/monsters/skeleton_archer/skeleton-walking-left.png", 3, 1, 100);
        walkingNorth = new Animation();
        walkingNorth.loadAnimationFromSheet("sprites/monsters/skeleton_archer/skeleton-walking-up.png", 3, 1, 100);
        walkingSouth = new Animation();
        walkingSouth.loadAnimationFromSheet("sprites/monsters/skeleton_archer/skeleton-walking-down.png", 3, 1, 100);

        shootingLeft = new Animation();
        shootingLeft.loadAnimationFromSheet("sprites/monsters/skeleton_archer/skeleton-shooting-left.png", 3, 1, 380);

        deathAnimation = new Animation();
        deathAnimation.loadAnimationFromSheet("sprites/monsters/monster-death.png", 4, 1, 100);

        speed = 0.2f;
        collision = new Collision();
        chase = false;

        setChasingRadius(400);
        attackOnce = false;

        setAnimation(walkingEast);
        pauseAnimation();
    }

    @Override
    public void getAction(Player player) {
        //super.getAction(playerX, playerY);

        if (getHealth() > 0) {

            checkIfPlayerInRange(player);

            if (chase) {

                playAnimation();

                if (getX() > player.getX() + 400) {
                    setVelocityX(0);
                    setAnimation(shootingLeft);

                    if (getAnimation().getCurrFrameIndex() == 1) {
                        setSpawnProjectile(true);
                    } else {
                        setSpawnProjectile(false);
                    }

                } else {
                    setSpawnProjectile(false);
                    setAnimation(walkingEast);
                    setVelocityX(speed);

                }

                if (getY() < player.getY() - 10 && getY() != player.getY() && getY() < player.getY() + 30) {
                    setSpawnProjectile(false);
                    setAnimation(walkingSouth);
                    setVelocityY(speed);
                } else if (getY() > player.getY() + 10 && getY() != player.getY() && getY() > player.getY() - 30) {
                    setSpawnProjectile(false);
                    setAnimation(walkingNorth);
                    setVelocityY(-speed);
                } else {
                    if (getAnimation() != shootingLeft) {
                        setSpawnProjectile(false);
                    }
                    setVelocityY(0);
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
