package characters.Villagers;

import game.Direction;
import game2D.Animation;

/**
 * Created by Nikolaos Kouroumalos on 04/03/2018
 *
 * @author Nikolaos Kouroumalos
 * @version 0.1
 */
public class Ballador extends Villager {

    private float speed = 0.1f;
    private float ogX, ogY, leftPathX, rightPathX;
    private int health;

    private Animation balladorAnimMovingRight, balladorAnimMovingLeft, heroAnimMovingUp, heroAnimMovingDown;

    public Ballador(Animation anim) {
        super(anim);

        this.setVelocityX(speed);
        this.setMoving(true);

        balladorAnimMovingRight = new Animation();
        balladorAnimMovingRight.loadAnimationFromSheet("sprites/villagers/ballador/ballador-walking-east-spritesheet.png", 9, 1, 80);
        balladorAnimMovingLeft = new Animation();
        balladorAnimMovingLeft.loadAnimationFromSheet("sprites/villagers/ballador/ballador-walking-west-spritesheet.png", 9, 1, 80);

        setAnimation(balladorAnimMovingRight);

        health = 3;

    }

    public void setOG()
    {
        ogX = this.getX();
        ogY = this.getY();
    }

    public void getAction()
    {
        if (health > 0) {
            leftPathX = ogX - 150;
            rightPathX = ogX + 50;
            if (this.isMoving()) {
                if (this.getX() <= leftPathX) {
                    move(Direction.EAST);
                    setAnimation(balladorAnimMovingRight);
                }

                if (this.getX() >= rightPathX) {
                    move(Direction.WEST);
                    setAnimation(balladorAnimMovingLeft);
                }
            } else {
                this.setVelocityX(0);
                pauseAnimation();
            }
        }
        else
        {
            pauseAnimation();
            setVelocityX(0);
            setDead();

        }
    }

    private void move(Direction direction)
    {
        switch (direction)
        {
            case EAST: this.setVelocityX(speed); break;
            case WEST: this.setVelocityX(-speed); break;
        }
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

}
