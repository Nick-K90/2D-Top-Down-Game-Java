package game;

import game2D.Sprite;
import game2D.TileMap;

import java.awt.*;

/**
 * Created by Nikolaos Kouroumalos on 16/03/2018
 *
 * @author Nikolaos Kouroumalos
 * @version 0.1
 */
public class Debug {


    public void DEBUG_TileMapCollisionSprite(Sprite s, int xo, int yo, TileMap collisionMap, Graphics2D g)
    {
        g.setColor(Color.yellow);
        int mapX;
        int mapY;

        if (s.getType() == 0) {
            int tileX = Math.round(s.getX() - 20f) / collisionMap.getTileHeight();
            int tileY = Math.round(s.getY() - 15f) / collisionMap.getTileHeight();
            for (int i = 0; i < 5; i++) {
                for (int x = 0; x < 5; x++) {
                    mapX = collisionMap.getTileXC(tileX + x, tileY + i);
                    mapY = collisionMap.getTileYC(tileX + x, tileY + i);
                    g.drawRect(mapX + xo, mapY + yo, 32, 32);
                }

            }


        }
        else if (s.getType() == 1)
        {
            int tileX = Math.round(s.getX()) / collisionMap.getTileHeight();
            int tileY = Math.round(s.getY()) / collisionMap.getTileHeight();
            for (int i = 0; i < 3; i++) {
                for (int x = 0; x < 3; x++) {
                    mapX = collisionMap.getTileXC(tileX + x, tileY + i);
                    mapY = collisionMap.getTileYC(tileX + x, tileY + i);
                    g.drawRect(mapX + xo, mapY + yo, 32, 32);
                }

            }
        }

        g.setColor(Color.RED);
        g.drawRect((int) s.getX() + xo, (int) s.getY() + yo, s.getWidth(), s.getHeight());
    }


    public void DEBUG_showCollisionMap(TileMap collisionMap, int xo, int yo, Graphics2D g)
    {
        for (int i = 0; i < collisionMap.getMapWidth(); i++)
        {
            for (int x = 0; x < collisionMap.getMapHeight(); x++)
            {
                //System.out.println(collisionMap.getTile(i,x).getCharacter());
                if (collisionMap.getTile(i,x).getCharacter() == 'e')
                {
                    g.drawRect(collisionMap.getTileXC(i,x) + xo, collisionMap.getTileYC(i,x) + yo, 32,32);
                }
            }
        }
    }

}
