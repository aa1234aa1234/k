import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Player {
    private Point position = new Point(), location = new Point();
    private HitBox collisionbox, interactionbox;
    private BufferedImage image;
    private int direction;
    private Interaction it;
    private int speed = 5;

    public Player(String imagepath, int width, int height) {
        try {
            image = ImageIO.read(new File(imagepath));
            collisionbox = new HitBox(width,height/2, new Point());
            //interactionbox = new HitBox()
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

    public void setPosition(Point pos) { position = pos; location = new Point(pos.x,pos.y); collisionbox.setLocation(location.x-(int)collisionbox.getWidth()/2,collisionbox.getLocation().y = location.y-(int)collisionbox.getHeight()/2); System.out.println(collisionbox); }

    public Point getLocation() { return location; }

    public void setLocation(Point pos) { location = pos; }

    public BufferedImage getImage() { return image; }

    public boolean move(boolean left, boolean right, boolean up, boolean bottom, int XViewPortStart, int YViewPortStart, int XViewPortEnd, int YViewPortEnd, int imgWidth, int imgHeight, Room room) {
        Point offset = new Point(position.x,position.y);
        HitBox hitBox = new HitBox((int)collisionbox.getWidth(), (int)collisionbox.getHeight(),new Point((int)collisionbox.getX(),(int)collisionbox.getY()));
        int xchange = 0, ychange = 0;
        if(left) {
            xchange -= speed;
            //position.x -= XViewPortStart < 0 || XViewPortEnd > 0 ? 0 : speed;
            offset.x -= XViewPortStart == 0 || (XViewPortEnd == 1000 || XViewPortEnd == imgWidth) ? speed : 0;
        }
        if(right) {
            xchange += speed;
            //position.x += XViewPortEnd == 1000 || XViewPortEnd == imgWidth ? speed : 0;
            offset.x += (XViewPortEnd == 1000 || XViewPortEnd == imgWidth) ? speed : 0;
        }
        if(up) {
            ychange -= speed;
            //position.y -= YViewPortStart != 0 ? 0 : speed;
            offset.y -= (YViewPortEnd == 700 || YViewPortEnd == imgHeight) || (YViewPortStart == 0) ? speed : 0;
        }
        if(bottom) {
            ychange += speed;
            //position.y += YViewPortEnd == 700 || YViewPortEnd == imgHeight ? speed*1.5 : 0;
            offset.y += (YViewPortEnd == 700 || YViewPortEnd == imgHeight) ? speed : 0;
        }
        hitBox.x += xchange;hitBox.y+=ychange;
        if(hitBox.getLocation().x < 0 || hitBox.getLocation().x+hitBox.getWidth() > imgWidth || hitBox.getLocation().y < 0 || hitBox.getLocation().y+hitBox.getHeight()*2 > imgHeight) return false;
        for(Asset e : room.getAssets()) {
            if(hitBox.intersects(e.getCollision())) return false;
        }
        collisionbox.x += xchange; collisionbox.y+=ychange;
        position.translate(offset.x-position.x,offset.y-position.y);
        location.translate(xchange,ychange);
        return true;
    }
}

interface Interaction {
    public void interact();
}