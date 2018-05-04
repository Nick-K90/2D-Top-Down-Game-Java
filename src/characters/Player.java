package characters;

import characters.Villagers.Villager;
import game.Direction;
import game2D.Animation;
import game2D.Sound;
import game2D.Velocity;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Nikolaos Kouroumalos on 02/03/2018
 *
 * @author Nikolaos Kouroumalos
 * @version 0.1
 */
public class Player extends Villager {

    private final static float BASE_SPEED = 0.2f;
    private Direction direction;
    private float speed;
    private boolean invulnerable;
    private Timer timer;
    private boolean moving;
    private long timeSinceLastHit;
    private Velocity velocity;

    private ArrayList<Direction> movementDirectionArrayList = new ArrayList<>();

    private Animation heroAnimMovingRight, heroAnimMovingLeft, heroAnimMovingUp, heroAnimMovingDown;
    private Animation heroBowEast, heroBowWest, heroBowNorth, heroBowSouth;
    private Animation heroSwordRight, heroSwordLeft, heroSwordNorth, heroSwordSouth;
    private boolean heroMovingEast, heroMovingWest, heroMovingNorth, heroMovingSouth, sprint;
    private boolean bowAttack, swordAttack;
    private int speedMultiplier;

    public Player(Animation anim) {
        super(anim);

        heroAnimMovingRight = new Animation();
        heroAnimMovingRight.loadAnimationFromSheet("images/hero-moving-right-spreadsheet.png", 9, 1, 80);
        heroAnimMovingDown = new Animation();
        heroAnimMovingDown.loadAnimationFromSheet("images/hero-moving-down-spreadsheet.png", 9, 1, 80);
        heroAnimMovingLeft = new Animation();
        heroAnimMovingLeft.loadAnimationFromSheet("images/hero-moving-left-spreadsheet.png", 9, 1, 80);
        heroAnimMovingUp = new Animation();
        heroAnimMovingUp.loadAnimationFromSheet("images/hero-moving-up-spreadsheet.png", 9, 1, 80);


        heroBowEast = new Animation();
        heroBowEast.loadAnimationFromSheet("images/hero-bow-right-spritesheet.png", 3, 1, 80);
        heroBowWest = new Animation();
        heroBowWest.loadAnimationFromSheet("images/hero-bow-left-spritesheet.png", 3, 1, 80);
        heroBowSouth = new Animation();
        heroBowSouth.loadAnimationFromSheet("images/hero-bow-down-spritesheet.png", 3, 1, 80);
        heroBowNorth = new Animation();
        heroBowNorth.loadAnimationFromSheet("images/hero-bow-up-spritesheet.png", 3, 1, 80);

        heroSwordLeft = new Animation();
        heroSwordLeft.loadAnimationFromSheet("images/hero-sword-left-spreadsheet.png", 3, 1, 80);
        heroSwordRight = new Animation();
        heroSwordRight.loadAnimationFromSheet("images/hero-sword-right-spritesheet.png", 3, 1, 80);
        heroSwordNorth = new Animation();
        heroSwordNorth.loadAnimationFromSheet("images/hero-sword-up-spritesheet.png", 3, 1, 80);
        heroSwordSouth = new Animation();
        heroSwordSouth.loadAnimationFromSheet("images/hero-sword-down-spritesheet.png", 3, 1, 80);



        moving = true;
        speed = BASE_SPEED;
        speedMultiplier = 2;
        direction = Direction.SOUTH;
        invulnerable = false;
        velocity = new Velocity();

        setHealth(12);



    }

    public void moveEast(Boolean east)
    {
        if(east)
        {
            if(!movementDirectionArrayList.contains(Direction.EAST))
            {
                movementDirectionArrayList.add(Direction.EAST);
            }

        }
        else
        {
            movementDirectionArrayList.remove(Direction.EAST);
        }

        //They have to be Booleans so i can have multiple (thats the case for now at least).
        heroMovingEast = east;
    }

    public void moveWest(Boolean west)
    {
        if(west)
        {
            if(!movementDirectionArrayList.contains(Direction.WEST))
            {
                movementDirectionArrayList.add(Direction.WEST);
            }

        }
        else
        {
            movementDirectionArrayList.remove(Direction.WEST);
        }
        heroMovingWest = west;

    }

    public void moveNorth(Boolean north)
    {
        //direction = Direction.NORTH;
        if(north)
        {
            if(!movementDirectionArrayList.contains(Direction.NORTH))
            {
                movementDirectionArrayList.add(Direction.NORTH);
            }

        }
        else
        {
            movementDirectionArrayList.remove(Direction.NORTH);
        }
        heroMovingNorth = north;

    }

    public void moveSouth(Boolean south)
    {
        //direction = Direction.SOUTH;
        if(south)
        {
            if(!movementDirectionArrayList.contains(Direction.SOUTH))
            {
                movementDirectionArrayList.add(Direction.SOUTH);
            }

        }
        else
        {
            movementDirectionArrayList.remove(Direction.SOUTH);
        }
        heroMovingSouth = south;
    }

    //TODO have only one boolean and use the directions to decide
    public void setBowAttack(boolean bowAttack)
    {
        this.bowAttack = bowAttack;
    }

    public void setSwordAttack (boolean swordAttack)
    {
        this.swordAttack = swordAttack;
    }

    public void sprint(Boolean sprint)
    {
        this.sprint = sprint;
    }

    public void getAction()
    {
        if(isMoving() && !bowAttack && !swordAttack) {
            if (heroMovingEast || heroMovingWest || heroMovingNorth || heroMovingSouth) {
                if (sprint)
                {
                    speed = BASE_SPEED * speedMultiplier;
                    this.setAnimationSpeed(speedMultiplier);
                }
                else
                {
                    speed = BASE_SPEED;
                    this.setAnimationSpeed(1);
                }

                if (movementDirectionArrayList.get(0) == Direction.EAST)
                {
                    setVelocityX(speed);
                    setAnimation(heroAnimMovingRight);
                    direction = Direction.EAST;

                    if (movementDirectionArrayList.size() > 1) {
                        if (movementDirectionArrayList.get(1) == Direction.SOUTH) {
                            setVelocityY(speed);
                        } else if (movementDirectionArrayList.get(1) == Direction.NORTH) {
                            setVelocityY(-speed);
                        }
                    }

                }

                if (movementDirectionArrayList.get(0) == Direction.WEST)
                {
                    setVelocityX(-speed);
                    setAnimation(heroAnimMovingLeft);
                    direction = Direction.WEST;

                    if (movementDirectionArrayList.size() > 1) {
                        if (movementDirectionArrayList.get(1) == Direction.SOUTH) {
                            setVelocityY(speed);
                        } else if (movementDirectionArrayList.get(1) == Direction.NORTH) {
                            setVelocityY(-speed);
                        }
                    }

                }
                if (movementDirectionArrayList.get(0) == Direction.SOUTH)
                {
                    setVelocityY(speed);
                    setAnimation(heroAnimMovingDown);
                    direction = Direction.SOUTH;

                    if (movementDirectionArrayList.size() > 1) {
                        if (movementDirectionArrayList.get(1) == Direction.EAST) {
                            setVelocityX(speed);
                        } else if (movementDirectionArrayList.get(1) == Direction.WEST) {
                            setVelocityX(-speed);
                        }
                    }

                }

                if (movementDirectionArrayList.get(0) == Direction.NORTH)
                {
                    setVelocityY(-speed);
                    setAnimation(heroAnimMovingUp);
                    direction = Direction.NORTH;

                    if (movementDirectionArrayList.size() > 1) {
                        if (movementDirectionArrayList.get(1) == Direction.EAST) {
                            setVelocityX(speed);
                        } else if (movementDirectionArrayList.get(1) == Direction.WEST) {
                            setVelocityX(-speed);
                        }
                    }

                }
                this.playAnimation();
            }
            else
            {
                this.pauseAnimation();
            }


        }
        if (bowAttack)
        {

            if (direction == Direction.EAST) {
                setAnimation(heroBowEast);
            }
            else if (direction == Direction.WEST)
            {
                setAnimation(heroBowWest);
            }
            else if (direction == Direction.NORTH)
            {
                setAnimation(heroBowNorth);
            }
            else if (direction == Direction.SOUTH)
            {
                setAnimation(heroBowSouth);
            }
            this.playAnimation();
        }

        if (swordAttack)
        {
            if (direction == Direction.WEST)
            {
                setAnimation(heroSwordLeft);
            }
            if (direction == Direction.EAST)
            {
                setAnimation(heroSwordRight);
            }
            if (direction == Direction.NORTH)
            {
                setAnimation(heroSwordNorth);
            }
            if (direction == Direction.SOUTH)
            {
                setAnimation(heroSwordSouth);
            }

            this.playAnimation();
        }

    }

    public boolean isMoving() {
        return moving;
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    public void resetAnimation()
    {
        switch (direction)
        {
            case EAST: setAnimation(heroAnimMovingRight); setAnimationFrame(0); break;
            case WEST: setAnimation(heroAnimMovingLeft); setAnimationFrame(0); break;
            case NORTH: setAnimation(heroAnimMovingUp); setAnimationFrame(0); break;
            case SOUTH: setAnimation(heroAnimMovingDown); setAnimationFrame(0); break;
        }
    }


    public Direction getDirection() {
        return direction;
    }

    public Animation getHeroAnimMovingRight() {
        return heroAnimMovingRight;
    }

    public Animation getHeroAnimMovingLeft() {
        return heroAnimMovingLeft;
    }

    public Animation getHeroAnimMovingUp() {
        return heroAnimMovingUp;
    }

    public Animation getHeroAnimMovingDown() {
        return heroAnimMovingDown;
    }


    @Override
    public void deductHealth(int amountToDeduct) {
        if(!invulnerable) {
            super.deductHealth(amountToDeduct);
            invulnerable = true;
            Sound hurt = new Sound("sounds/hurt.wav");
            hurt.start();
            timer = new Timer(true);
            timer.schedule(new Invurnerable(), 2 * 1000);
        }
    }

    class Invurnerable extends TimerTask {
        public void run() {
            invulnerable = false;
            timer.cancel(); //Terminate the timer thread
        }
    }
//    class RemindTask extends TimerTask {
//        public void run() {
//            System.out.println("Time's up!");
//            toolkit.beep();
//            //timer.cancel(); // Not necessary because
//            // we call System.exit
//            System.exit(0);   // Stops the AWT thread
//            // (and everything else)
//        }
//    }
}
