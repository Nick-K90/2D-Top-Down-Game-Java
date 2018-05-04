package characters.Villagers;

import game.Direction;
import game2D.Animation;

/**
 * Created by Nikolaos Kouroumalos on 10/04/2018
 *
 * @author Nikolaos Kouroumalos
 * @version 0.1
 */
public class Dog extends Villager {


    private Animation walkingEast, walkingWest, walkingSouth, walkingNorth;
    private float ogX, ogY, leftPathX, rightPathX;
    private float speed = 0.1f;

    public Dog(Animation anim) {
        super(anim);

        walkingEast = new Animation();
        walkingEast.loadAnimationFromSheet("sprites/villagers/dog/dog-walking-right.png",4,1,80);
        walkingWest = new Animation();
        walkingWest.loadAnimationFromSheet("sprites/villagers/dog/dog-walking-left.png",4,1,80);

        walkingSouth = new Animation();
        walkingSouth.loadAnimationFromSheet("sprites/villagers/dog/dog-walking-down.png",4,1,80);
        walkingNorth = new Animation();
        walkingNorth.loadAnimationFromSheet("sprites/villagers/dog/dog-walking-up.png",4,1,80);

        this.setVelocityX(speed);
        this.setMoving(true);

    }

    public void setOG()
    {
        ogX = this.getX();
        ogY = this.getY();
    }

    public void getAction()
    {

            leftPathX = ogX - 150;
            rightPathX = ogX + 50;
            if (this.isMoving()) {
                if (this.getX() <= leftPathX) {
                    move(Direction.EAST);
                    setAnimation(walkingEast);
                }

                if (this.getX() >= rightPathX) {
                    move(Direction.WEST);
                    setAnimation(walkingWest);
                }
            } else {
                this.setVelocityX(0);
                pauseAnimation();
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
}
