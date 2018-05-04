package game;

import characters.Villagers.Ballador;
import characters.Villagers.Villager;
import game2D.Sprite;
import game2D.Tile;
import game2D.TileMap;

import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * Created by Nikolaos Kouroumalos on 16/03/2018
 *
 * @author Nikolaos Kouroumalos
 * @version 0.1
 */
public class Collision {


    public void checkNpcCollision(Sprite s1, Sprite s2) {

        //Villager villager = (Villager)s2;
        //villager.setMoving(true);

        if (s1.getX() + s1.getWidth() > s2.getX() + 24 && s1.getX() < s2.getX() && s1.getY() + s1.getHeight() > s2.getY() + 82 && s1.getY() + s1.getHeight() < s2.getY() + s2.getHeight() + 10) {
            s1.setX(s2.getX() - s2.getWidth() + 24);
            //villager.setMoving(false);
        }

        if (s1.getX() < s2.getX() + s2.getWidth() - 24 && s1.getX() + s1.getWidth() > s2.getX() + s2.getWidth() && s1.getY() + s1.getHeight() > s2.getY() + 82 && s1.getY() + s1.getHeight() < s2.getY() + s2.getHeight() + 10) {
            s1.setX(s2.getX() + s2.getWidth() - 24);
            //villager.setMoving(false);
        }

        if (s1.getY() + s1.getHeight() > s2.getY() + s2.getHeight() - 20 && s1.getY() < s2.getY() && s1.getX() + 32 > s2.getX() && s1.getX() + 32 < s2.getX() + s2.getWidth()) {
            s1.setY(s2.getY() - 20);
            //villager.setMoving(false);
        }

        if (s1.getY() > s2.getY() && s1.getY() + s1.getHeight() < s2.getY() + s2.getHeight() + 20 && s1.getX() + 32 > s2.getX() && s1.getX() + 32 < s2.getX() + s2.getWidth()) {
            s1.setY(s2.getY() + 20);
            //villager.setMoving(false);
        }


//        if (s1.getX() < s2.getX() + s2.getWidth() && s2.getX() < s1.getX() + s1.getWidth() && s1.getY() < s2.getY() + s2.getHeight() && s2.getY() < s1.getY() + s1.getHeight())
//        {
//            System.out.println("LOALAOASD");
//            Image img = loadImage("aa");
//            //s1.setX(s2.getX() - s1.getWidth());
//
//            villagerArrayList.remove(s2);
//        }
    }

    /**
     * Checks for sprite to sprite collision using the full size of the sprite.
     *
     * @param s1
     * @param s2
     * @return Returns true if collision has occured. False otherwise
     */
    public boolean checkSpriteCollision(Sprite s1, Sprite s2) {
        if (s1 == s2) {
            return false;
        }
        int s1x = Math.round(s1.getX());
        int s1y = Math.round(s1.getY());
        int s2x = Math.round(s2.getX());
        int s2y = Math.round(s2.getY());

        return (s1x < s2x + s2.getWidth() && s2x < s1x + s1.getWidth() && s1y < s2y + s2.getHeight() && s2y < s1y + s1.getHeight());
    }

    public boolean meleeWeaponCollision(Rectangle rect, Sprite s2, int xo, int yo) {
        if (rect.intersects(s2.getX() + xo, s2.getY() + yo, s2.getWidth(), s2.getHeight())) {
            return true;
        }
        return false;
    }

    private Point pointCache = new Point();

    private Point getTileCollision(Sprite sprite, TileMap collisionMap, float newX, float newY) {
        float fromX = Math.min(sprite.getX(), newX);
        float fromY = Math.min(sprite.getY(), newY);
        float toX = Math.max(sprite.getX(), newX);
        float toY = Math.max(sprite.getY(), newY);

        // get the tile locations
        //  collisionMap.getTile(collisionMap.getTileXC((int)fromX, (int)fromY) / collisionMap.getTileWidth(), collisionMap.getTileYC((int)fromX, (int)fromY) / collisionMap.getTileHeight());
        // collisionMap.getTile(collisionMap.getTileXC(((int)toX + sprite.getWidth() - 1), (int)toY + sprite.getHeight() - 1), collisionMap.getTileYC(((int)toX + sprite.getWidth() - 1), (int)toY + sprite.getHeight() - 1));
        int fromTileX = Math.round(fromX / collisionMap.getTileWidth());
        int fromTileY = Math.round(fromY / collisionMap.getTileHeight());
        int toTileX = Math.round((toX + sprite.getWidth() - 1) / collisionMap.getTileWidth());
        int toTileY = Math.round((toY + sprite.getHeight() - 1) / collisionMap.getTileHeight());

        for (int x = fromTileX; x <= toTileX; x++) {
            for (int y = fromTileY; y <= toTileY; y++) {
                if (x < 0 || x >= collisionMap.getMapWidth() || y < 0 || y >= collisionMap.getMapHeight() || collisionMap.getTile(x, y).getCharacter() == 'f') {
                    pointCache.setLocation(x, y);
                    return pointCache;
                }
            }
        }

        return null;
    }

    public void handleTileCollision(Sprite s, TileMap collisionMap, long elapsedTime) {
        // change x
        float dx = s.getVelocityX();
        float oldX = s.getX();
        float newX = oldX + dx * elapsedTime;
        Point tile = getTileCollision(s, collisionMap, newX, s.getY());

        if (tile == null) {
            s.setX(newX);
        } else {
            // line up with the tile boundary
            if (dx > 0) {
                s.setX(collisionMap.getTileXC((tile.x), 0) - s.getWidth());
            } else if (dx < 0) {
                s.setX(collisionMap.getTileXC((tile.x), 0) + 20);
            }
            collideHorizontal(s);
        }
        // change y
        float dy = s.getVelocityY();
        float oldY = s.getY();
        float newY = oldY + dy * elapsedTime;
        tile = getTileCollision(s, collisionMap, s.getX(), newY);
        if (tile == null) {
            s.setY(newY);
        } else {
            // line up with the tile boundary
            if (dy > 0) {
                s.setY(
                        collisionMap.getTileYC(0, (tile.y)) - s.getHeight());
            } else if (dy < 0) {
                s.setY(
                        collisionMap.getTileYC(0, (tile.y)) + 1);
            }
            collideVertical(s);
        }
    }

    private void collideHorizontal(Sprite s) {
        s.setVelocityX(0);
        s.setSpeed(0);

    }

    private void collideVertical(Sprite s) {
        s.setVelocityY(0);
        s.setSpeed(0);
    }


    /**
     * Checks and handles collisions with the tile map for the
     * given sprite 's'. Initial functionality is limited...
     *
     * @param s       The Sprite to check collisions for
     * @param elapsed How time has gone by
     */
    public void checkTileMapCollision(Sprite s, TileMap collisionMap, long elapsed) {
        //This is to prevent an issue when restarting a game while you are close to a wall.
        //The last frame might check for collision while the map has already been cleared and before
        //the new one gets loaded resulting in a division by zero.
        if (collisionMap.getTileWidth() == 0 || collisionMap.getTileHeight() == 0) {
            return;
        }

        if (s.getType() == 0) {

            int tileX = Math.round(s.getX() - 20f) / collisionMap.getTileWidth();
            int tileY = Math.round(s.getY() - 15f) / collisionMap.getTileHeight();

            for (int i = 0; i < 5; i++) {
                for (int x = 0; x < 5; x++) {
                    //System.out.print(tmap.getTile(tileX + x, tileY + i).getCharacter());
                    //USE AN ARRAYLIST OR JUST AN ARRAY!
                    if (collisionMap.getTile(tileX + x, tileY + i) != null && collisionMap.getTile(tileX + x, tileY + i).getCharacter() == 'f') {
                        handleMapCollision(s, collisionMap, collisionMap.getTile(tileX + x, tileY + i), collisionMap.getTile(tileX + x, tileY + i).getCharacter());
                        checkScreenEdges(s, collisionMap);
                    }
                }
                //System.out.println();
            }
        } else if (s.getType() == 1) {
            int tileX = Math.round(s.getX()) / collisionMap.getTileWidth();
            int tileY = Math.round(s.getY()) / collisionMap.getTileHeight();

            for (int i = 0; i < 3; i++) {
                for (int x = 0; x < 3; x++) {
                    if (collisionMap.getTile(tileX + x, tileY + i) != null && collisionMap.getTile(tileX + x, tileY + i).getCharacter() == 'f') {
                        handleProjectileMapCollision(s, collisionMap, collisionMap.getTile(tileX + x, tileY + i));
                        checkScreenEdges(s, collisionMap);
                    }
                }
            }
        }

//        if (player.getY() + player.getHeight() > tmap.getPixelHeight())
//        {
//        	// Put the player back on the map
//        	//player.setY(tmap.getPixelHeight() - player.getHeight());
//
//        	// and make them bounce
//        	//player.setVelocityY(-player.getVelocityY() * (0.03f * elapsed));
//        }
    }

    public void handleProjectileMapCollision(Sprite s, TileMap collisionMap, Tile tile) {
        if (s.getX() + s.getWidth() > tile.getXC() && s.getX() < tile.getXC() - collisionMap.getTileWidth() && s.getY() + (s.getHeight() / 2) > tile.getYC() && s.getY() + (s.getHeight() / 2) < tile.getYC() + collisionMap.getTileHeight()) {
            s.setX(tile.getXC() - s.getWidth());
            s.setVelocityX(0);
            s.setSpeed(0);
        }
        if (s.getX() < tile.getXC() + collisionMap.getTileWidth() && tile.getXC() < s.getX() + s.getWidth() && s.getY() + (s.getHeight() / 2) > tile.getYC() && s.getY() + (s.getHeight() / 2) < tile.getYC() + collisionMap.getTileHeight()) {
            s.setX(tile.getXC() + collisionMap.getTileWidth());
            s.setVelocityX(0);
            s.setSpeed(0);

        }
        if (s.getY() + s.getHeight() > tile.getYC() && s.getY() < tile.getYC() && s.getX() + (s.getWidth() / 2) > tile.getXC() && s.getX() + (s.getWidth() / 2) < tile.getXC() + collisionMap.getTileWidth()) {
            s.setY(tile.getYC() - s.getHeight());
            s.setVelocityY(0);
            s.setSpeed(0);
        }
        if (s.getY() > tile.getYC() && s.getY() < tile.getYC() + collisionMap.getTileHeight() && s.getX() + (s.getWidth() / 2) > tile.getXC() && s.getX() + (s.getWidth() / 2) < tile.getXC() + collisionMap.getTileWidth()) {
            s.setY(tile.getYC() + collisionMap.getTileHeight());
            s.setVelocityY(0);
            s.setSpeed(0);
        }
    }

    public void handleMapCollision(Sprite s, TileMap collisionMap, Tile tile, char ch) {
        if (s.getX() + s.getWidth() > tile.getXC() && s.getX() < tile.getXC() && s.getY() - s.getHeight() / 4 < tile.getYC() && s.getY() + s.getHeight() - 8 > tile.getYC()) {
            if (ch == 'f') {
                s.setX(tile.getXC() - s.getWidth());
                s.setVelocityX(0);
                //System.out.println("LEFT!!");
            }
//            if (ch == 's' && s.getY() + s.getX() > ((collisionMap.getTileWidth() * collisionMap.getTileWidth()) / 2) + tile.getXC() && s.getY() + s.getHeight() < tile.getYC() + collisionMap.getTileHeight() && s.getY() + s.getHeight() > tile.getYC() && s.getX() + s.getWidth() > tile.getXC() && s.getX() + s.getWidth() < tile.getXC() + collisionMap.getTileWidth())
//            {
//                //s.setVelocityX(-1.08f);
//                s.setY(s.getY() - 2 );
//                System.out.println("SOUTH WEST CORNER!!");
//            }
        }
        if (s.getX() < tile.getXC() + s.getWidth() / 2 && s.getX() > tile.getXC() && s.getY() - s.getHeight() / 4 < tile.getYC() && s.getY() + s.getHeight() - 8 > tile.getYC()) {
            if (ch == 'f') {
                s.setX(tile.getXC() + s.getWidth() / 2);
                s.setVelocityX(0);
                //System.out.println("RIGHT!!");
            }
        }
        //System.out.println("TILE Y: " + tile.getYC() + " PLAYER Y:" + s.getY());
        if (s.getY() + s.getHeight() > tile.getYC() && s.getY() < tile.getYC() && s.getX() + s.getWidth() - 6 > tile.getXC() && s.getX() < tile.getXC() + collisionMap.getTileWidth() - 6) {
            if (ch == 'f') {
                //System.out.println("UP!!" + " Player Y + Height: " + (s.getY() + s.getHeight()) + "Tile Y: " + tile.getYC());
                s.setY(tile.getYC() - s.getHeight());
                s.setVelocityY(0);
                //System.out.println("Tile Y: " + tile.getYC() + " Send to Y:" + (tile.getYC() - s.getHeight()) + " Player Y+Height: " + (s.getY() + s.getHeight()));
                //System.out.println("UP!!");
            }
//            if (ch == 's' && s.getY() + s.getX() > ((collisionMap.getTileWidth() * collisionMap.getTileWidth()) / 2) + tile.getXC() && s.getY() < tile.getYC() && s.getX() + s.getWidth() > tile.getXC() && s.getX() < tile.getXC() && s.getY() + s.getHeight() < tile.getYC() + collisionMap.getTileHeight())
//            {
//               //s.setVelocityX(-1.08f);
//               s.setX(s.getX() - 2 );
//                System.out.println("SOUTH WEST CORNER!!" + " Player x + y: " + (player.getX() + player.getY()) + " Tile stuff: " + (((collisionMap.getTileWidth() * collisionMap.getTileWidth()) / 2) + tile.getXC()));
//            }

        }
        //At the end its -6 instead of -4 in order to avoid some wierd collision while sprinting upwards and hitting either left or right buttons.
        if (s.getY() < tile.getYC() + collisionMap.getTileHeight() && s.getY() + s.getHeight() > tile.getYC() && s.getX() + s.getWidth() - 6 > tile.getXC() && s.getX() < tile.getXC() + collisionMap.getTileWidth() - 6) {
            if (ch == 'f') {
                s.setY(tile.getYC() + collisionMap.getTileHeight());
                s.setVelocityY(0);
                //System.out.println("DOWN!!");
            }
        }


    }

    /**
     * Used by enemies in order to "decide" when to start chansing the player. It depends on the
     * chasingRadius variable of the enemeny.
     *
     * @param player
     * @param enemy
     * @return
     */
    public boolean chasingRadiusCollision(Sprite player, Sprite enemy) {
        int dx, dy, minimum;

        dx = (int) player.getX() - (int) enemy.getX();
        dy = (int) player.getY() - (int) enemy.getY();
        minimum = (int) player.getRadius() + (int) enemy.getChasingRadius();

        return (((dx * dx) + (dy * dy)) < (minimum * minimum));

    }

    /**
     * Checking the edges of the map
     *
     * @param s
     * @param backgroundTiles
     */
    public void checkScreenEdges(Sprite s, TileMap backgroundTiles) {
        if (s.getX() > backgroundTiles.getMapWidth() * backgroundTiles.getTileWidth() - s.getWidth())
            s.setX(backgroundTiles.getMapWidth() * backgroundTiles.getTileWidth() - s.getWidth());
        else if (s.getX() < 0) s.setX(0);
        if (s.getY() > backgroundTiles.getMapHeight() * backgroundTiles.getTileHeight() - s.getHeight())
            s.setY(backgroundTiles.getMapHeight() * backgroundTiles.getTileHeight() - s.getHeight());
        else if (s.getY() < 0) s.setY(0);

    }
}
