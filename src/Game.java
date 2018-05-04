import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

import Weapons.Projectiles;
import Weapons.Weapon;
import characters.Monsters.Bat;
import characters.Monsters.GreenStatue;
import characters.Monsters.Monster;
import characters.Monsters.SkeletonArcher;
import characters.Player;
import characters.Villagers.Ballador;
import characters.Villagers.Dog;
import characters.Villagers.Soldier;
import characters.Villagers.Villager;
import game.*;
import game.Event;
import game2D.*;

import javax.swing.*;

// Game demonstrates how we can override the GameCore class
// to create our own 'game'. We usually need to implement at
// least 'draw' and 'update' (not including any local event handling)
// to begin the process. You should also add code to the 'init'
// method that will initialise event handlers etc. By default GameCore
// will handle the 'Escape' key to quit the game but you should
// override this with your own event handler.

/**
 * @author Nikolaos Kouroumalos
 */
@SuppressWarnings("serial")

public class Game extends GameCore {
    // Useful game constants
//    private int screenWidth = 1280;
//    private int screenHeight = 720;

//    float 	lift = 0.005f;
//    float	gravity = 0.0001f;
//    private float speed = 0.2f;

    // Game state flags
//    boolean flap = false;
    boolean pause = false;
    boolean loading = true;

    private Sound music;
    private Sprite exitButton;

    // Game resources
    Animation heroAnimation;

    Animation npcAnimation;

    private Player player = null;
    private Weapon weapon = null;
    private Animation arrow = null;

    private float playerPositionX;
    private float playerPositionY;

    private float tempX;
    private float tempY;
    private boolean attackOnce = false;
    private boolean oneProjectile = false;
    private boolean useFrameOnce = true;

    private boolean playerSwordAttack = false;
    private boolean playerBowAttack = false;
    private Rectangle swordBoundingBox;

    private Collision collision = new Collision();
    private Debug debug = new Debug();

    private MapID mapID;
    private MapID previousMapID;

    private ArrayList<Villager> villagerArrayList = new ArrayList<>();
    private ArrayList<Monster> monsterArrayList = new ArrayList<>();
    private ArrayList<Character> npcArrayList = new ArrayList<>(Arrays.asList('b', 'd', 'm', 'e', 'g', 's', 'c'));
    private ArrayList<Sprite> spriteDepthArrayList = new ArrayList<>();
    private ArrayList<Projectiles> projectilesArrayList = new ArrayList<>();
    private ArrayList<Event> eventsList = new ArrayList<>();

    private ArrayList<Sprite> playerHealth = new ArrayList<>();


    ArrayList<Sprite> clouds = new ArrayList<>();

    TileMap backgroundTiles = new TileMap();    // Our tile map, note that we load it in init()
    TileMap backgroundDecorations = new TileMap();
    TileMap backgroundBuildingsMountains = new TileMap();
    TileMap foregroundTiles = new TileMap();
    TileMap foregroundBuildings = new TileMap();
    TileMap collisionMap = new TileMap();
    TileMap charactersMap = new TileMap();
    TileMap eventMap = new TileMap();
    long total;                    // The score will be the total time elapsed since a crash

    /**
     * Initialise the class, e.g. set up variables, load images,
     * create animations, register event handlers
     */
    public void init() {

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setTitle("Game");
        addMouseListener(this);
        try {
            setCursor(Toolkit.getDefaultToolkit().createCustomCursor(new ImageIcon("images/cursor.png").getImage(), new Point(0, 0), "custom cursor"));
        } catch (Exception e) {
        }

        mapID = MapID.VILLAGE;
        previousMapID = MapID.NONE;

        Image exitImage = loadImage("images/exit.png");
        Animation exitAnimation = new Animation();
        exitAnimation.addFrame(exitImage,0);
        exitButton = new Sprite(exitAnimation);

        music = new Sound("sounds/village.wav");
        music.start();

        initialiseGame();
    }

    /**
     * You will probably want to put code to restart a game in
     * a separate method so that you can call it to restart
     * the game.
     */
    public void initialiseGame() {

        loading = true;
        loadMap(mapID);
        setUpPlayer();

        arrow = new Animation();
        arrow.loadAnimationFromSheet("sprites/weapons/arrow-north.png", 1, 1, 80);

        for (int row = 0; row < charactersMap.getMapHeight(); row++) {
            for (int col = 0; col < charactersMap.getMapWidth(); col++) {
                if (npcArrayList.contains(charactersMap.getTileChar(col, row))) {
                    if (charactersMap.getTileChar(col, row) == 'b') {
                        Image balladorIMG = loadImage("sprites/villagers/ballador/ballador-facing-down.png");
                        npcAnimation = new Animation();
                        npcAnimation.addFrame(balladorIMG, 0);
                        Ballador ballador = new Ballador(npcAnimation);

                        villagerArrayList.add(ballador);
                        villagerArrayList.get(villagerArrayList.size() - 1).setX(charactersMap.getTile(col, row).getXC());
                        villagerArrayList.get(villagerArrayList.size() - 1).setY(charactersMap.getTile(col, row).getYC());
                        villagerArrayList.get(villagerArrayList.size() - 1).show();
                        ballador.setOG();
                    }

                    if (charactersMap.getTileChar(col, row) == 'd') {

                        //Need to have the animation inside a sub-classed villager shit.
                        npcAnimation = new Animation();
                        npcAnimation.loadAnimationFromSheet("images/mystwin.png", 4, 1, 200);
                        Villager villager = new Villager(npcAnimation);
                        villagerArrayList.add(villager);
                        villagerArrayList.get(villagerArrayList.size() - 1).setX(charactersMap.getTile(col, row).getXC());
                        villagerArrayList.get(villagerArrayList.size() - 1).setY(charactersMap.getTile(col, row).getYC());
                        villagerArrayList.get(villagerArrayList.size() - 1).show();

                    }

                    if (charactersMap.getTileChar(col, row) == 'e') {

                        npcAnimation = new Animation();
                        npcAnimation.loadAnimationFromSheet("sprites/villagers/dog/dog-sitting.png", 1, 1, 200);
                        Dog dog = new Dog(npcAnimation);
                        villagerArrayList.add(dog);
                        villagerArrayList.get(villagerArrayList.size() - 1).setX(charactersMap.getTile(col, row).getXC());
                        villagerArrayList.get(villagerArrayList.size() - 1).setY(charactersMap.getTile(col, row).getYC());
                        villagerArrayList.get(villagerArrayList.size() - 1).show();
                        dog.setOG();

                    }

                    if (charactersMap.getTileChar(col, row) == 'c') {

                        npcAnimation = new Animation();
                        npcAnimation.loadAnimationFromSheet("sprites/villagers/soldier/soldier.png", 1, 1, 200);
                        Soldier soldier = new Soldier(npcAnimation);
                        villagerArrayList.add(soldier);
                        villagerArrayList.get(villagerArrayList.size() - 1).setX(charactersMap.getTile(col, row).getXC());
                        villagerArrayList.get(villagerArrayList.size() - 1).setY(charactersMap.getTile(col, row).getYC());
                        villagerArrayList.get(villagerArrayList.size() - 1).show();
                    }

                    if (charactersMap.getTileChar(col, row) == 'm') {

                        //Need to have the animation inside a sub-classed villager shit.
                        npcAnimation = new Animation();
                        npcAnimation.loadAnimationFromSheet("images/bat.png", 1, 1, 200);
                        Bat bat = new Bat(npcAnimation);
                        monsterArrayList.add(bat);
                        monsterArrayList.get(monsterArrayList.size() - 1).setX(charactersMap.getTile(col, row).getXC());
                        monsterArrayList.get(monsterArrayList.size() - 1).setY(charactersMap.getTile(col, row).getYC());
                        monsterArrayList.get(monsterArrayList.size() - 1).show();
                    }
                    if (charactersMap.getTileChar(col, row) == 'g') {

                        //Need to have the animation inside a sub-classed villager shit.
                        npcAnimation = new Animation();
                        npcAnimation.loadAnimationFromSheet("images/bat.png", 1, 1, 200);
                        GreenStatue greenStatue = new GreenStatue(npcAnimation);
                        monsterArrayList.add(greenStatue);
                        monsterArrayList.get(monsterArrayList.size() - 1).setX(charactersMap.getTile(col, row).getXC());
                        monsterArrayList.get(monsterArrayList.size() - 1).setY(charactersMap.getTile(col, row).getYC());
                        monsterArrayList.get(monsterArrayList.size() - 1).show();
                    }
                    if (charactersMap.getTileChar(col, row) == 's') {

                        //Need to have the animation inside a sub-classed villager shit.
                        npcAnimation = new Animation();
                        npcAnimation.loadAnimationFromSheet("images/bat.png", 1, 1, 200);
                        SkeletonArcher skeletonArcher = new SkeletonArcher(npcAnimation);
                        monsterArrayList.add(skeletonArcher);
                        monsterArrayList.get(monsterArrayList.size() - 1).setX(charactersMap.getTile(col, row).getXC());
                        monsterArrayList.get(monsterArrayList.size() - 1).setY(charactersMap.getTile(col, row).getYC());
                        monsterArrayList.get(monsterArrayList.size() - 1).show();
                    }


                }
            }
        }

        for (int row = 0; row < eventMap.getMapHeight(); row++) {
            for (int col = 0; col < eventMap.getMapWidth(); col++) {
                if (eventMap.getTileChar(col, row) == 'e') {
                    Image blankIMG = loadImage("images/blank-sprite-64x64.png");
                    Animation blankAnimation = new Animation();
                    blankAnimation.addFrame(blankIMG, 0);

                    //1 == cave entrace from village
                    Event event = new Event(blankAnimation);
                    event.setEventID(1);

                    eventsList.add(event);
                    eventsList.get(eventsList.size() - 1).setX(eventMap.getTile(col, row).getXC());
                    eventsList.get(eventsList.size() - 1).setY(eventMap.getTile(col, row).getYC());
                    eventsList.get(eventsList.size() - 1).show();
                }

                if (eventMap.getTileChar(col, row) == 'v') {
                    Image blankIMG = loadImage("images/blank-sprite-64x64.png");
                    Animation blankAnimation = new Animation();
                    blankAnimation.addFrame(blankIMG, 0);

                    //2 == village entrace from cave
                    Event event = new Event(blankAnimation);
                    event.setEventID(2);

                    eventsList.add(event);
                    eventsList.get(eventsList.size() - 1).setX(eventMap.getTile(col, row).getXC());
                    eventsList.get(eventsList.size() - 1).setY(eventMap.getTile(col, row).getYC());
                    eventsList.get(eventsList.size() - 1).show();
                }
                if (eventMap.getTileChar(col, row) == 'c') {
                    Image blankIMG = loadImage("images/blank-sprite-64x64.png");
                    Animation blankAnimation = new Animation();
                    blankAnimation.addFrame(blankIMG, 0);

                    //3 == cave 2 entrace from cave 1
                    Event event = new Event(blankAnimation);
                    event.setEventID(3);

                    eventsList.add(event);
                    eventsList.get(eventsList.size() - 1).setX(eventMap.getTile(col, row).getXC());
                    eventsList.get(eventsList.size() - 1).setY(eventMap.getTile(col, row).getYC());
                    eventsList.get(eventsList.size() - 1).show();
                }
                if (eventMap.getTileChar(col, row) == 't') {
                    Image blankIMG = loadImage("images/blank-sprite-64x64.png");
                    Animation blankAnimation = new Animation();
                    blankAnimation.addFrame(blankIMG, 0);

                    //2 == cave 1 entrance from cave 2
                    Event event = new Event(blankAnimation);
                    event.setEventID(4);

                    eventsList.add(event);
                    eventsList.get(eventsList.size() - 1).setX(eventMap.getTile(col, row).getXC());
                    eventsList.get(eventsList.size() - 1).setY(eventMap.getTile(col, row).getYC());
                    eventsList.get(eventsList.size() - 1).show();
                }
            }
        }
    }

    private void loadMap(MapID mapID) {
        //Only way to "unload" the maps in case of reset/loading the next map.
        backgroundTiles = new TileMap();
        backgroundDecorations = new TileMap();
        backgroundBuildingsMountains = new TileMap();
        foregroundTiles = new TileMap();
        foregroundBuildings = new TileMap();
        collisionMap = new TileMap();
        charactersMap = new TileMap();
        eventMap = new TileMap();

        if (mapID == MapID.VILLAGE) {
            // Load the tile map and print it out so we can check it is valid
            backgroundTiles.loadMap("maps/village/background_tiles", "village_background_tiles.txt");
            backgroundDecorations.loadMap("maps/village/background_decorations", "village_background_decorations.txt");
            backgroundBuildingsMountains.loadMap("maps/village/background_buildings_mountains", "village_background_buildings_mountains.txt");
            foregroundTiles.loadMap("maps/village/foreground_tiles", "village_foreground_tiles.txt");
            foregroundBuildings.loadMap("maps/village/foreground_buildings", "village_foreground_buildings.txt");
            collisionMap.loadMap("maps/village/collision_map", "village-collision-map.txt");
            charactersMap.loadMap("maps/village/characters_map", "characters_map.txt");
            eventMap.loadMap("maps/village/event_map", "event_map.txt");

            if (previousMapID == MapID.NONE) {
                setPlayerPosition(864, 280);
                previousMapID = mapID;
            } else if (previousMapID == MapID.CAVE_1) {
                setPlayerPosition(2048, 210);
                previousMapID = mapID;
            }


            if (music.isFinished()) {
                music = new Sound("sounds/village.wav");
                music.start();
            }

        } else if (mapID == MapID.CAVE_1) {
            backgroundTiles.loadMap("maps/cave/background_tiles", "cave_1_background_tiles.txt");
            collisionMap.loadMap("maps/cave/collision_map", "cave_1_collision_map.txt");
            backgroundDecorations.loadMap("maps/cave/background_decorations", "cave_1_background_decorations.txt");
            backgroundBuildingsMountains.loadMap("maps/cave/background_buildings_mountains", "cave_1_walls.txt");
            charactersMap.loadMap("maps/cave/characters_map", "cave_1_characters_map.txt");
            eventMap.loadMap("maps/cave/event_map", "cave_1_event_map.txt");

            if (previousMapID == MapID.VILLAGE) {
                setPlayerPosition(448, 1644);
                previousMapID = mapID;
            } else if (previousMapID == MapID.CAVE_2) {
                setPlayerPosition(3713, 1317);
                previousMapID = mapID;
            }
        } else if (mapID == MapID.CAVE_2) {
            backgroundTiles.loadMap("maps/cave/background_tiles", "cave_2_background_tiles.txt");
            collisionMap.loadMap("maps/cave/collision_map", "cave_2_collision_map.txt");
            backgroundDecorations.loadMap("maps/cave/background_decorations", "cave_2_background_decorations.txt");
            backgroundBuildingsMountains.loadMap("maps/cave/background_buildings_mountains", "cave_2_walls.txt");
            charactersMap.loadMap("maps/cave/characters_map", "cave_2_characters_map.txt");
            eventMap.loadMap("maps/cave/event_map", "cave_2_event_map.txt");

            if (previousMapID == MapID.CAVE_1) {
                setPlayerPosition(1475, 304);
                previousMapID = mapID;
            }
        }

        loading = false;


    }

    private void setUpPlayer() {
        Image hero = loadImage("images/hero-facing-down.png");

        heroAnimation = new Animation();
        heroAnimation.addFrame(hero, 0);
        player = new Player(heroAnimation);

        Image noWeapon = loadImage("images/empty-sprite.png");
        Animation noWeaponAnimation = new Animation();
        noWeaponAnimation.addFrame(noWeapon, 0);
        weapon = new Weapon(noWeaponAnimation);

        player.setX(playerPositionX);
        player.setY(playerPositionY);
        player.setVelocityX(0);
        player.setVelocityY(0);
        player.show();

        weapon.setX(player.getX());
        weapon.setY(player.getY());
        weapon.setVelocityX(player.getVelocityX());
        weapon.setVelocityY(player.getVelocityY());

        updatePlayerHealth();
        swordBoundingBox = new Rectangle();


    }

    private void setPlayerPosition(float x, float y) {
        playerPositionX = x;
        playerPositionY = y;
    }

    private void updatePlayerHealth() {
        playerHealth.clear();
        for (int i = 0; i < player.getHealth(); i++) {
            Image heart = loadImage("sprites/heart/heart.png");
            Animation heartAnimation = new Animation();
            heartAnimation.addFrame(heart, 0);

            playerHealth.add(new Sprite(heartAnimation));
            if (i == 0) {
                playerHealth.get(i).setX(50);
            } else {
                playerHealth.get(i).setX(playerHealth.get(i - 1).getX() + 40);
            }

            playerHealth.get(i).setY(50);
            playerHealth.get(i).show();
        }
    }


    private void resetMap() {
        villagerArrayList.clear();
        monsterArrayList.clear();
        projectilesArrayList.clear();
        spriteDepthArrayList.clear();
        eventsList.clear();

        initialiseGame();
    }

    int xo = 0;
    int yo = 0;

    /**
     * Draw the current state of the game
     */
    public void draw(Graphics2D g) {
        if (!pause && !loading) {

            xo = adjustXOffset(player);
            yo = adjustYOffset(player);

            g.setColor(Color.black);
            g.fillRect(0, 0, getWidth(), getHeight());

            backgroundTiles.draw(g, xo, yo);
            backgroundBuildingsMountains.draw(g, xo, yo);
            backgroundDecorations.draw(g, xo, yo);

            if (!projectilesArrayList.isEmpty()) {
                for (int i = 0; i < projectilesArrayList.size(); i++) {
                    projectilesArrayList.get(i).setOffsets(xo, yo);
                    projectilesArrayList.get(i).draw(g);

                }
            }


            player.setOffsets(xo, yo);


            if (!villagerArrayList.isEmpty()) {
                for (int i = 0; i < villagerArrayList.size(); i++) {
                    villagerArrayList.get(i).setOffsets(xo, yo);
                    if (villagerArrayList.get(i).getY() < player.getY()) // The player is beaneath so the player has smaller higher Y value
                    {
                        villagerArrayList.get(i).setzDepth(2);
                    } else {
                        villagerArrayList.get(i).setzDepth(4);
                    }
                }
            }

            if (!monsterArrayList.isEmpty()) {
                for (int i = 0; i < monsterArrayList.size(); i++) {
                    monsterArrayList.get(i).setOffsets(xo, yo);

                    if (monsterArrayList.get(i).getY() < player.getY() && monsterArrayList.get(i).getMonsterID() != MonsterID.BAT) {
                        monsterArrayList.get(i).setzDepth(2);
                    } else {
                        monsterArrayList.get(i).setzDepth(4);
                    }
                }
            }


            spriteDepthArrayList.clear();
            spriteDepthArrayList.addAll(villagerArrayList);
            spriteDepthArrayList.addAll(monsterArrayList);
            spriteDepthArrayList.add(player);
            spriteDepthArrayList.add(weapon);

            //Collections.sort(spriteDepthArrayList);

            for (int i = 1; i < spriteDepthArrayList.size(); i++) {
                int keyDepth = spriteDepthArrayList.get(i).getzDepth();
                Sprite key = spriteDepthArrayList.get(i);
                int j = i - 1;
                while (j >= 0 && spriteDepthArrayList.get(j).getzDepth() > keyDepth) {
                    spriteDepthArrayList.set(j + 1, spriteDepthArrayList.get(j));
                    spriteDepthArrayList.set(j, key);
                    j--;
                }

            }

            for (int i = 0; i < spriteDepthArrayList.size(); i++) {
                spriteDepthArrayList.get(i).drawTransformed(g);
            }

            foregroundTiles.draw(g, xo, yo);
            foregroundBuildings.draw(g, xo, yo);

            //collisionMap.draw(g, xo, yo);
            //eventMap.draw(g,xo,yo);

//            for (int i = 0; i < projectilesArrayList.size(); i++) {
//                debug.DEBUG_TileMapCollisionSprite(projectilesArrayList.get(i), xo, yo, collisionMap, g);
//            }


            double fpsRounded = Math.round(super.getFPS() * 100.0) / 100.0;
            String msg = Double.toString(fpsRounded);
            g.setColor(Color.GREEN);
            g.drawString(msg, getWidth() - 80, 50);

            // g.setColor(Color.RED);
            //g.drawLine((int) player.getX() + xo, (int) player.getY() + yo, backgroundTiles.getPixelWidth(), (int) player.getY() + yo);
            //g.draw(swordBoundingBox);

            for (int i = 0; i < playerHealth.size(); i++) {
                playerHealth.get(i).draw(g);
            }

        }
        else if(pause)
        {
            exitButton.setX((getWidth() / 2));
            exitButton.setY(getHeight() / 2);
            exitButton.show();
            exitButton.draw(g);
        }
    }


    /**
     * Update any sprites and check for collisions
     *
     * @param elapsed The elapsed time between this call and the previous call of elapsed
     */
    public void update(long elapsed) {

        if (!pause) {

            // Make adjustments to the speed of the sprite due to gravity
            //player.setVelocityY(player.getVelocityY()+(gravity*elapsed));
            //cauldron.setAnimationSpeed(1.0f);

            for (int i = 0; i < playerHealth.size(); i++) {
                playerHealth.get(i).update(elapsed);
            }
            if (!villagerArrayList.isEmpty()) {
                for (int i = 0; i < villagerArrayList.size(); i++) {
                    //villagerArrayList.get(i).setAnimationSpeed(1.0f);
                    villagerArrayList.get(i).getAction();
                    villagerArrayList.get(i).update(elapsed);
                    //checkTileMapCollision(villagerArrayList.get(i), elapsed);
                    collision.checkTileMapCollision(villagerArrayList.get(i), collisionMap, elapsed);
                }
            }

            if (!monsterArrayList.isEmpty()) {
                for (int i = 0; i < monsterArrayList.size(); i++) {
                    monsterArrayList.get(i).getAction(player);
                    monsterArrayList.get(i).update(elapsed);
                    if (monsterArrayList.get(i).getMonsterID() == MonsterID.BAT) {
                        continue;
                    }
                    collision.checkTileMapCollision(monsterArrayList.get(i), collisionMap, elapsed);
                }
            }

            //To avoid an out of bounds exception for when an arrow hits a villager first and then the same
            //index is "looked at" again for monsters.
            projectileLoop:
            if (!projectilesArrayList.isEmpty()) {
                for (int i = 0; i < projectilesArrayList.size(); i++) {

                    projectilesArrayList.get(i).getAction();
                    projectilesArrayList.get(i).update(elapsed);
                    //checkTileMapCollision(projectilesArrayList.get(i), elapsed);
                    //handleTileCollision(projectilesArrayList.get(i), elapsed);
                    collision.handleTileCollision(projectilesArrayList.get(i), collisionMap, elapsed);

                    if (!villagerArrayList.isEmpty()) {
                        for (int x = 0; x < villagerArrayList.size(); x++) {
                            if (collision.checkSpriteCollision(projectilesArrayList.get(i), villagerArrayList.get(x))) {
                                villagerArrayList.get(x).deductHealth(projectilesArrayList.get(i).getDamage());
                                projectilesArrayList.remove(i);
                                Sound arrow = new Sound("sounds/arrow-hit.wav");
                                arrow.start();
                                break projectileLoop;
                            }
                        }
                    }

                    if (!monsterArrayList.isEmpty()) {
                        for (int x = 0; x < monsterArrayList.size(); x++) {
                            if (collision.checkSpriteCollision(projectilesArrayList.get(i), monsterArrayList.get(x))) {
                                monsterArrayList.get(x).deductHealth(projectilesArrayList.get(i).getDamage());
                                projectilesArrayList.remove(i);
                                Sound arrow = new Sound("sounds/arrow-hit.wav");
                                arrow.start();
                                break projectileLoop;
                            }
                        }
                    }

                    if (collision.checkSpriteCollision(projectilesArrayList.get(i), player)) {
                        player.deductHealth(1);
                        updatePlayerHealth();
                        projectilesArrayList.remove(i);
                        Sound arrow = new Sound("sounds/arrow-hit.wav");
                        arrow.start();
                        break projectileLoop;
                    }
                }
            }

            if (playerSwordAttack) {
                swordAttack();
            } else if (playerBowAttack) {
                bowAttack();
            }
            //npc1.setAnimationSpeed(1.0f);
            player.getAction();
            weapon.getAction();
            //player.setAnimationSpeed(1.0f);

//       	if (flap)
//       	{
//       		player.setAnimationSpeed(1.8f);
//       		player.setVelocityY(-0.04f);
//            player.setVelocityX(0.04f);
//       	}

//       	for (Sprite s: clouds)
//       		s.update(elapsed);


            // Now update the sprites animation and position
            player.update(elapsed);
            weapon.update(elapsed);
            collision.checkTileMapCollision(player, collisionMap, elapsed);
            //collision.handleTileCollision(player,collisionMap,elapsed);
            //handleTileCollision(player, elapsed);
            //checkTileMapCollision(npc1,elapsed);

            if (!villagerArrayList.isEmpty()) {
                for (int i = 0; i < villagerArrayList.size(); i++) {
                    if (villagerArrayList.get(i).isDead()) {
                        villagerArrayList.remove(i);
                        Sound enemy_kill = new Sound("sounds/enemy-kill.wav");
                        enemy_kill.start();
                        continue;
                    }
                    collision.checkNpcCollision(player, villagerArrayList.get(i));
                }
            }

            if (!monsterArrayList.isEmpty()) {
                for (int i = 0; i < monsterArrayList.size(); i++) {
                    if (monsterArrayList.get(i).isDead()) {
                        monsterArrayList.remove(i);
                        Sound enemy_kill = new Sound("sounds/enemy-kill.wav");
                        enemy_kill.start();
                        continue;
                    }
                    if (collision.checkSpriteCollision(player, monsterArrayList.get(i))) {
                        player.deductHealth(monsterArrayList.get(i).getDamage());
                        updatePlayerHealth();
                    }

                    if (monsterArrayList.get(i).isSpawnProjectile()) {
                        if (!oneProjectile) {
                            Projectiles projectile = new Projectiles(arrow, Direction.WEST, monsterArrayList.get(i).getX() + 28, monsterArrayList.get(i).getY() + 30);
                            projectilesArrayList.add(projectile);
                            oneProjectile = true;
                            Sound arrow = new Sound("sounds/bow.wav");
                        }

                        if (monsterArrayList.get(i).getAnimation().getCurrFrameIndex() != 1) {
                            oneProjectile = false;
                        }
                    }


                }

            }


            for (int i = 0; i < eventsList.size(); i++) {
                if (collision.checkSpriteCollision(player, eventsList.get(i))) {
                    if (eventsList.get(i).getEventID() == 1) {
                        System.out.println("cave_1");
                        Sound entrance = new Sound("sounds/entrance.wav");
                        entrance.start();
                        mapID = MapID.CAVE_1;
                        resetMap();
                        break;
                    }
                    if (eventsList.get(i).getEventID() == 2) {
                        System.out.println("Village");
                        Sound entrance = new Sound("sounds/entrance.wav");
                        entrance.start();
                        mapID = MapID.VILLAGE;
                        resetMap();
                        break;
                    }
                    if (eventsList.get(i).getEventID() == 3) {
                        System.out.println("cave_2");
                        Sound entrance = new Sound("sounds/entrance.wav");
                        entrance.start();
                        mapID = MapID.CAVE_2;
                        resetMap();
                        break;
                    }
                    if (eventsList.get(i).getEventID() == 4) {
                        System.out.println("Villager");
                        Sound entrance = new Sound("sounds/entrance.wav");
                        entrance.start();
                        mapID = MapID.CAVE_1;
                        resetMap();
                        break;
                    }
                }
            }

        }

        if (player.getHealth() <= 0) {
            resetMap();
        }
    }


    /**
     * Override of the keyPressed event defined in GameCore to catch our
     * own events
     *
     * @param e The event that has been generated
     */
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_ESCAPE) stop();

        if (key == KeyEvent.VK_UP) {
            player.moveNorth(true);
        }

        if (key == KeyEvent.VK_DOWN) {
            player.moveSouth(true);
        }

        if (key == KeyEvent.VK_RIGHT) {
            player.moveEast(true);
        }

        if (key == KeyEvent.VK_LEFT) {
            player.moveWest(true);
        }

        if (key == KeyEvent.VK_R) {
            resetMap();
        }

        if (key == KeyEvent.VK_P) {
            if (pause) {
                pause = false;
            } else {
                pause = true;
            }
        }

//        if (key == KeyEvent.VK_D) {
//            for (int i = 0; i < spriteDepthArrayList.size(); i++) {
//                System.out.print(spriteDepthArrayList.get(i).getzDepth() + ",");
//
//            }
//            System.out.println("");
//        }

        if (key == KeyEvent.VK_A) {
            attackWithBow();
        }

//    	if (key == KeyEvent.VK_S)
//    	{
//    		// Example of playing a sound as a thread
//    		Sound s = new Sound("sounds/caw.wav");
//    		s.start();
//    	}

        if (key == KeyEvent.VK_S) {
            attackWithSword();
        }
//        if (key == KeyEvent.VK_SPACE) {
//            System.out.println("Player X: " + player.getX() + " Player Y:" + player.getY());
//            System.out.println("Player x + y: " + (player.getX() + player.getY()));
//        }
//        if (key == KeyEvent.VK_H) {
//            System.out.print("Player Height: " + player.getHeight() + " Player Width: " + player.getWidth());
//        }
        if (key == KeyEvent.VK_SHIFT) {
            player.sprint(true);
        }
    }

    public void keyReleased(KeyEvent e) {

        int key = e.getKeyCode();

        switch (key) {
            case KeyEvent.VK_ESCAPE:
                stop();
                break;
            case KeyEvent.VK_UP:
                player.setVelocityY(0);
                player.moveNorth(false);
                player.resetAnimation();
                //sword.setVelocityY(0); sword.moveNorth(false); sword.resetAnimation();
                break;
            case KeyEvent.VK_DOWN:
                player.setVelocityY(0);
                player.moveSouth(false);
                player.resetAnimation();
                //sword.setVelocityY(0); sword.moveSouth(false); sword.resetAnimation();
                break;
            case KeyEvent.VK_RIGHT:
                player.setVelocityX(0);
                player.moveEast(false);
                player.resetAnimation();
                //sword.setVelocityX(0); sword.moveEast(false); sword.resetAnimation();
                break;
            case KeyEvent.VK_LEFT:
                player.setVelocityX(0);
                player.moveWest(false);
                player.resetAnimation();
                //sword.setVelocityX(0); sword.moveWest(false); sword.resetAnimation();
                break;
            case KeyEvent.VK_SHIFT:
                player.sprint(false);
            case KeyEvent.VK_A:
                attackOnce = false;
                break;
            case KeyEvent.VK_S:
                useFrameOnce = true;
                attackOnce = false;
                break;
            default:
                break;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int key = e.getButton();
        if(!pause){
            if (e.getButton() == MouseEvent.BUTTON1) {
                attackWithSword();
            }
            //Using both button 2 and 3 because my mouse has a clickable wheel and it registers as button 3.
            if (e.getButton() == MouseEvent.BUTTON2 || e.getButton() == MouseEvent.BUTTON3) {
                attackWithBow();
            }
        }
        else if (pause)
        {
            if (e.getButton() == MouseEvent.BUTTON1) {
                if (checkButtonClicked(exitButton))
                {
                    System.exit(0);
                }
            }
        }


    }

    @Override
    public void mouseReleased(MouseEvent e) {
        int key = e.getButton();

        switch (key) {
            case 1:
                useFrameOnce = true;
                attackOnce = false;
                break;
            case 3:
                attackOnce = false;
                break;
        }
    }

    private void attackWithSword() {
        if (player.getDirection() == Direction.WEST) {
            if (!attackOnce) {
                player.setSwordAttack(true);
                playerSwordAttack = true;
                weapon.setDirection(Direction.WEST);
                weapon.setWeaponID(1);
                weapon.setOffsets(xo - 110, yo + 10);
                weapon.setX(player.getX());
                weapon.setY(player.getY());
                weapon.show();
                swordBoundingBox = new Rectangle((int) player.getX() + xo - 110, (int) player.getY() + yo + 15, 130, 60);
                tempX = player.getX();
                player.setVelocityX(-0.5f);
                attackOnce = true;
                Sound s = new Sound("sounds/sword-swing.wav");
                s.start();
            }

        }
        if (player.getDirection() == Direction.EAST) {
            if (!attackOnce) {
                player.setSwordAttack(true);
                playerSwordAttack = true;
                weapon.setDirection(Direction.EAST);
                weapon.setWeaponID(1);
                weapon.setOffsets(xo - 30, yo + 10);
                weapon.setX(player.getX());
                weapon.setY(player.getY());
                weapon.show();
                swordBoundingBox = new Rectangle((int) player.getX() + player.getWidth() + xo - 20, (int) player.getY() + yo + 15, 130, 60);
                tempX = player.getX();
                player.setVelocityX(0.5f);
                attackOnce = true;
                Sound s = new Sound("sounds/sword-swing.wav");
                s.start();
            }

        }
        if (player.getDirection() == Direction.NORTH) {
            if (!attackOnce) {
                player.setSwordAttack(true);
                playerSwordAttack = true;
                weapon.setDirection(Direction.NORTH);
                weapon.setWeaponID(1);
                weapon.setOffsets(xo - 60, yo - 35);
                weapon.setX(player.getX());
                weapon.setY(player.getY());
                weapon.show();
                swordBoundingBox = new Rectangle((int) player.getX() + xo - 60, (int) player.getY() + yo - 40, 200, 80);
                tempY = player.getY();
                player.setVelocityY(-0.5f);
                attackOnce = true;
                Sound s = new Sound("sounds/sword-swing.wav");
                s.start();
            }

        }
        if (player.getDirection() == Direction.SOUTH) {
            if (!attackOnce) {
                player.setSwordAttack(true);
                playerSwordAttack = true;
                weapon.setDirection(Direction.SOUTH);
                weapon.setWeaponID(1);
                weapon.setOffsets(xo - 60, yo + 10);
                weapon.setX(player.getX());
                weapon.setY(player.getY());
                weapon.show();
                swordBoundingBox = new Rectangle((int) player.getX() + xo - 60, (int) player.getY() + yo + 60, 200, 80);
                tempY = player.getY();
                player.setVelocityY(0.5f);
                attackOnce = true;
                Sound s = new Sound("sounds/sword-swing.wav");
                s.start();
            }

        }
    }

    private void attackWithBow() {
        if (player.getDirection() == Direction.EAST) {
            if (!attackOnce) {
                player.setBowAttack(true);
                playerBowAttack = true;
                attackOnce = true;

                weapon.setDirection(Direction.EAST);
                weapon.setWeaponID(2);
                weapon.setOffsets(xo + 23, yo - 15);
                weapon.setX(player.getX());
                weapon.setY(player.getY());
                weapon.show();

                Projectiles projectile = new Projectiles(arrow, Direction.EAST, player.getX() + 25, player.getY() + 28);
                projectilesArrayList.add(projectile);

                Sound s = new Sound("sounds/bow.wav");
                s.start();
            }

        }
        if (player.getDirection() == Direction.WEST) {

            if (!attackOnce) {
                player.setBowAttack(true);
                playerBowAttack = true;
                attackOnce = true;

                weapon.setDirection(Direction.WEST);
                weapon.setWeaponID(2);
                weapon.setOffsets(xo - 13, yo - 15);
                weapon.setX(player.getX());
                weapon.setY(player.getY());
                weapon.show();

                Projectiles projectile = new Projectiles(arrow, Direction.WEST, player.getX() - 5, player.getY() + 28);
                projectilesArrayList.add(projectile);

                Sound s = new Sound("sounds/bow.wav");
                s.start();
            }
        }

        if (player.getDirection() == Direction.SOUTH) {
            if (!attackOnce) {
                player.setBowAttack(true);
                playerBowAttack = true;
                attackOnce = true;

                weapon.setDirection(Direction.SOUTH);
                weapon.setWeaponID(2);
                weapon.setOffsets(xo + 15, yo - 5);
                weapon.setX(player.getX());
                weapon.setY(player.getY());
                weapon.show();

                Projectiles projectile = new Projectiles(arrow, Direction.SOUTH, player.getX() + 28, player.getY() + 60);
                projectilesArrayList.add(projectile);

                Sound s = new Sound("sounds/bow.wav");
                s.start();
            }
        }

        if (player.getDirection() == Direction.NORTH) {
            if (!attackOnce) {
                player.setBowAttack(true);
                playerBowAttack = true;
                attackOnce = true;

                weapon.setDirection(Direction.NORTH);
                weapon.setWeaponID(2);
                weapon.setOffsets(xo + 20, yo - 27);
                weapon.setX(player.getX());
                weapon.setY(player.getY());
                weapon.show();

                Projectiles projectile = new Projectiles(arrow, Direction.NORTH, player.getX() + 28, player.getY() + 10);
                projectilesArrayList.add(projectile);

                Sound s = new Sound("sounds/bow.wav");
                s.start();
            }
        }
    }

    private boolean checkButtonClicked(Sprite s1) {
        if (s1.getX() < getMousePosition().x && s1.getX() + s1.getWidth() > getMousePosition().x && s1.getY() < getMousePosition().getY() && s1.getY() + s1.getHeight() > getMousePosition().getY()) {
            return true;
        }
        else {return false;}
    }

    /**
     * Calculates the x offset based on the sprites {@link Sprite#getX()}
     *
     * @return the x offset
     */
    private int adjustXOffset(Sprite sprite) {
        int xOff = getWidth() / 2 - Math.round(sprite.getX());

        if (xOff > 0) {
            return 0;
        }

        if (xOff < getWidth() - backgroundTiles.getMapWidth() * backgroundTiles.getTileWidth()) {
            return getWidth() - backgroundTiles.getMapWidth() * backgroundTiles.getTileWidth();
        }

        return xOff;
    }

    /**
     * Calculates the y offset based on the sprites {@link Sprite#getX()}
     *
     * @return the y offset
     */
    private int adjustYOffset(Sprite sprite) {
        int yOff = getHeight() / 2 - Math.round(sprite.getY());

        if (yOff > 0) {
            return 0;
        }

        if (yOff < getHeight() - backgroundTiles.getMapHeight() * backgroundTiles.getTileHeight()) {
            return getHeight() - backgroundTiles.getMapHeight() * backgroundTiles.getTileHeight();
        }

        return yOff;
    }


    private void bowAttack() {
        if (weapon.getAnimation().hasLooped()) {
            weapon.hide();
            player.resetAnimation();
            player.setBowAttack(false);
            playerBowAttack = false;
            player.setMoving(true);
            weapon.getAnimation().resetLooped();
        }
    }

    private void swordAttack() {
        if (player.getX() < tempX - 15) {
            player.setVelocityX(0);
        } else if (player.getX() > tempX + 15) {
            player.setVelocityX(0);
        }

        if (player.getY() < tempY - 15) {
            player.setVelocityY(0);
        } else if (player.getY() > tempY + 15) {
            player.setVelocityY(0);
        }

        if (weapon.getAnimation().getCurrFrameIndex() == 1 && useFrameOnce) {
            for (int i = 0; i < monsterArrayList.size(); i++) {
                if (collision.meleeWeaponCollision(swordBoundingBox, monsterArrayList.get(i), xo, yo)) {
                    monsterArrayList.get(i).deductHealth(weapon.getDamage());
                }
            }
            useFrameOnce = false;
            swordBoundingBox = new Rectangle();
        }

        if (weapon.getAnimation().hasLooped()) {
            player.setVelocityX(0);
            player.setVelocityY(0);
            weapon.hide();
            player.resetAnimation();
            player.setSwordAttack(false);
            playerSwordAttack = false;
            player.setMoving(true);
            weapon.getAnimation().resetLooped();
        }

    }
}
