package Weapons;

import game.Direction;
import game2D.Animation;
import game2D.Sprite;


/**
 * Created by Nikolaos Kouroumalos on 06/03/2018
 *
 * @author Nikolaos Kouroumalos
 * @version 0.1
 */
public class Projectiles extends Sprite {


    private float speed = 2f;
    private int type = 1; //1 = Projectile
    private Direction direction;

    private int damage = 1;

    private Animation arrowNorth, arrowEast, arrowSouth, arrowWest;

    public Projectiles(Animation anim, Direction direction, float x, float y) {
        super(anim);
        setType();
        this.direction = direction;
        super.setSpeed(speed);


        arrowNorth = new Animation();
        arrowNorth.loadAnimationFromSheet("sprites/weapons/arrow-north.png", 1, 1, 80);
        arrowEast = new Animation();
        arrowEast.loadAnimationFromSheet("sprites/weapons/arrow.png", 1, 1, 80);
        arrowSouth = new Animation();
        arrowSouth.loadAnimationFromSheet("sprites/weapons/arrow-south.png", 1, 1, 80);
        arrowWest = new Animation();
        arrowWest.loadAnimationFromSheet("sprites/weapons/arrow-west.png", 1, 1, 80);

        this.setX(x);
        this.setY(y);
        this.show();
    }

    private void setType()
    {
        super.setType(type);
    }

    public void getAction()
    {
        switch (direction)
        {
            case NORTH: setVelocityY(-super.getSpeed()); this.setAnimation(arrowNorth); break;
            case EAST: setVelocityX(super.getSpeed()); this.setAnimation(arrowEast); break;
            case SOUTH: setVelocityY(super.getSpeed()); this.setAnimation(arrowSouth); break;
            case WEST: setVelocityX(-super.getSpeed()); this.setAnimation(arrowWest); break;
        }
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public int getDamage()
    {
        return damage;
    }

}
