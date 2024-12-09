package Objects;

import Main.Asset;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class obj_test extends Asset {
    private boolean flag = false;
    private Timer animationTimer;
    private BufferedImage[] animation = new BufferedImage[2];
    private int animationFrame = 0;

    public obj_test() {
        super(70, 100, 500, 350, new Rectangle(500,350,70,100), "assets/bob.png");

        try {
            animation[0] = ImageIO.read(new File("assets/bob.png"));
            animation[1] = ImageIO.read(new File("assets/bob_02.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setFlag(boolean a) {
        flag = a;
    }

    public boolean getFlag() {
        return flag;
    }

    public void setAnimationTimer(Timer t) {
        animationTimer = t;
    }

    public Timer getAnimationTimer() {
        return animationTimer;
    }

    public void setAnimationFrame(int a) {
        animationFrame = a;
    }

    public int getAnimationFrame() {
        return animationFrame;
    }

    @Override
    public BufferedImage getImage() {
        return animation[animationFrame];
    }
}
