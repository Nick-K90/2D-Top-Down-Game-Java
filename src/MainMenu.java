import game2D.Animation;
import game2D.GameCore;
import game2D.ScreenManager;
import game2D.Sprite;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * Created by Nikolaos Kouroumalos on 10/04/2018
 *
 * @author Nikolaos Kouroumalos
 * @version 0.1
 */
public class MainMenu extends GameCore {

    private Sprite playButton, windowModeButton, fullScreenModeButton;
    private Animation playButtonAnimation, windowButtonAnimation, fullScreenButtonAnimation;
    private boolean fullScreen;

    private Image test;

    /**
     * The obligatory main method that creates
     * an instance of our class and starts it running
     *
     * @param args The list of parameters this program might use (ignored)
     */
    public static void main(String[] args) {
        MainMenu mainMenu = new MainMenu();
        mainMenu.init();
        mainMenu.run(false, 1280, 720);

    }

    private void init() {
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setTitle("Game Launcher");

        fullScreen = false;

        try {
            setCursor(Toolkit.getDefaultToolkit().createCustomCursor(new ImageIcon("images/cursor.png").getImage(), new Point(0, 0), "custom cursor"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        addMouseListener(this);

        test = loadImage("images/main-menu-bg.png");
        Image playButtonImage = loadImage("images/play-up.png");
        Image windowModeButtonImage = loadImage("images/window-up.png");
        Image fullScreen = loadImage("images/fullscreen-up.png");

        fullScreenButtonAnimation = new Animation();
        fullScreenButtonAnimation.addFrame(fullScreen, 0);

        fullScreenModeButton = new Sprite(fullScreenButtonAnimation);
        fullScreenModeButton.setX(650);
        fullScreenModeButton.setY(400);
        fullScreenModeButton.show();


        windowButtonAnimation = new Animation();
        windowButtonAnimation.addFrame(windowModeButtonImage, 0);

        windowModeButton = new Sprite(windowButtonAnimation);
        windowModeButton.setX(350);
        windowModeButton.setY(400);
        windowModeButton.show();

        playButtonAnimation = new Animation();
        playButtonAnimation.addFrame(playButtonImage, 0);

        playButton = new Sprite(playButtonAnimation);
        playButton.setX(500);
        playButton.setY(500);
        playButton.show();


    }

    public void draw(Graphics2D g) {

        //I don't know what i did wrong here but won't work otherwise.
        super.paint(g);

        g.drawImage(test,0,0,null);
        fullScreenModeButton.draw(g);
        windowModeButton.draw(g);
        playButton.draw(g);


        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.PLAIN, 30));
        g.drawString("A / Left Mouse Click = Bow", 400, 200);
        g.drawString("S / Right Mouse Click = Sword", 400, 250);
        g.drawString("Left Shift = Sprint", 400, 300);
        g.drawString("P = Pause", 400, 350);

        if (fullScreen) {
            g.drawString("Current mode is: Full Screen Mode", playButton.getX() - 110, playButton.getY() - 10);
        } else {
            g.drawString("Current mode is: Window Mode", playButton.getX() - 110, playButton.getY() - 10);
        }

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (checkButtonClicked(windowModeButton)) {
            Image windowButtonImage = loadImage("images/window-down.png");

            Animation windowButtonAnimation = new Animation();
            windowButtonAnimation.addFrame(windowButtonImage, 0);

            windowModeButton = new Sprite(windowButtonAnimation);
            windowModeButton.setX(350);
            windowModeButton.setY(400);
            windowModeButton.show();
        }

        if (checkButtonClicked(fullScreenModeButton)) {
            Image windowButtonImage = loadImage("images/fullscreen.png");

            Animation fullScreenButtonAnimation = new Animation();
            fullScreenButtonAnimation.addFrame(windowButtonImage, 0);

            fullScreenModeButton = new Sprite(fullScreenButtonAnimation);
            fullScreenModeButton.setX(650);
            fullScreenModeButton.setY(400);
            fullScreenModeButton.show();
        }

        if (checkButtonClicked(playButton)) {
            Image playButtonImage = loadImage("images/play-down.png");

            Animation playButtonAnimation = new Animation();
            playButtonAnimation.addFrame(playButtonImage, 0);

            playButton = new Sprite(playButtonAnimation);
            playButton.setX(500);
            playButton.setY(500);
            playButton.show();
        }
    }

    public void mouseClicked(MouseEvent e) {

        if (checkButtonClicked(fullScreenModeButton))
        {
            fullScreen = true;
        }

        if (checkButtonClicked(windowModeButton)) {
            fullScreen = false;
        }

        if (checkButtonClicked(playButton))
        {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    if (fullScreen)
                    {
                        Game gct = new Game();
                        gct.init();
                        gct.run(true,1920,1080);
                    }
                    else
                    {
                        Game gct = new Game();
                        gct.init();
                        gct.run(false,1280,720);
                    }
                }

            });
            thread.start();
            this.setVisible(false);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        windowModeButton.setAnimation(windowButtonAnimation);
        fullScreenModeButton.setAnimation(fullScreenButtonAnimation);
        playButton.setAnimation(playButtonAnimation);

    }

    private boolean checkButtonClicked(Sprite s1) {
        if (s1.getX() < getMousePosition().x && s1.getX() + s1.getWidth() > getMousePosition().x && s1.getY() < getMousePosition().getY() && s1.getY() + s1.getHeight() > getMousePosition().getY()) {
        return true;
        }
        else {return false;}
    }

}
