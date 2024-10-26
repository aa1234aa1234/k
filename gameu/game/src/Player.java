import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Player {
    private Point position = new Point(), location = new Point();
    private Rectangle collisionbox, interactionbox;
    private BufferedImage image;
    private int direction;
    private Interaction it;
    private int speed = 5;

    public Player(String imagepath) {
        try {
            image = ImageIO.read(new File(imagepath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void interact() {
        it.interact();
    }

    public void setInteraction(Interaction it) {
        this.it = it;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point pos) { position = pos; location = pos; }

    public Point getLocation() { return location; }

    public void setLocation(Point pos) { location = pos; }

    public BufferedImage getImage() { return image; }

    public Point move(boolean left, boolean right, boolean up, boolean bottom, int XViewPortStart, int YViewPortStart, int XViewPortEnd, int YViewPortEnd, int imgWidth, int imgHeight, Camera camera) {
        Point offset = position;
        if(left) {
            //location.x -= speed;
            //position.x -= XViewPortStart < 0 || XViewPortEnd > 0 ? 0 : speed;
            position.x -= XViewPortStart == 0 || (XViewPortEnd == 1000 || XViewPortEnd == imgWidth) ? speed : 0;
        }
        if(right) {
            //location.x += speed;
            //position.x += XViewPortEnd == 1000 || XViewPortEnd == imgWidth ? speed : 0;
            position.x += (XViewPortEnd == 1000 || XViewPortEnd == imgWidth) ? speed : 0;
        }
        if(up) {
            //location.y -= speed*1.5;
            //position.y -= YViewPortStart != 0 ? 0 : speed;
            position.y -= (YViewPortEnd == 700 || YViewPortEnd == imgHeight) || (YViewPortStart == 0) ? speed : 0;
        }
        if(bottom) {
            //location.y += speed*1.5;
            //position.y += YViewPortEnd == 700 || YViewPortEnd == imgHeight ? speed*1.5 : 0;
            position.y += (YViewPortEnd == 700 || YViewPortEnd == imgHeight) ? speed : 0;
        }
        offset.translate(offset.x-position.x,offset.y-position.y);
        return offset;
    }
}

interface Interaction {
    public void interact();
}