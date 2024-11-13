import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Player {
    private DoublePoint position = new DoublePoint(0,0);
    private Point location = new Point();
    private HitBox collisionbox, interactionbox;
    private BufferedImage[] image = new BufferedImage[3];
    private int direction = 0;
    private Interaction it;
    private int speed = 5;
    private int walking = 0;

    public Player(String imagepath, int width, int height) {
        try {
            image[0] = ImageIO.read(new File("player_sprites/(baka).png"));
            image[1] = ImageIO.read(new File("player_sprites/(baka)(baka).png"));
            image[2] = ImageIO.read(new File("player_sprites/(baka)(baka)(baka).png"));
            collisionbox = new HitBox(width, (int) (height/2.0), new Point());
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

    public DoublePoint getPosition() {
        return position;
    }

    public void setPosition(Point pos) { position = new DoublePoint(pos.x,pos.y); location = new Point(pos.x,pos.y); collisionbox.setLocation((int) (location.x-(int)collisionbox.getWidth()/2.0), (int) (location.y-(int)collisionbox.getHeight()/2.0)); System.out.println(collisionbox); }

    public Point getLocation() { return location; }

    public void setLocation(Point pos) { location = pos; }

    public BufferedImage getImage() { return image[walking]; }

    public void ChangeDir(int dir) {
        direction = dir;
    }

    public void setWalking(int a) {
        walking = a;
    }

    public int isWalking() { return walking; }

    public int getDirection() { return direction; }

    public boolean move(boolean left, boolean right, boolean up, boolean bottom, int XViewPortStart, int YViewPortStart, int XViewPortEnd, int YViewPortEnd, int imgWidth, int imgHeight, Room room) {
        Point offset = new Point((int) position.x, (int) position.y);
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
        if(xchange == 0 && ychange == 0) return false;
        hitBox.x += xchange;hitBox.y+=ychange;
        //hitBox.translate(0, (int) (hitBox.getHeight()/2.0));
        //hitBox.y -= (int) (hitBox.getHeight()/2.0);
        if(room.getMap() != null) {
            if((hitBox.y)/Room.getTileSize() <= 0 || (hitBox.x)/Room.getTileSize() <= 0 || (hitBox.y+hitBox.getHeight()*2)/Room.getTileSize() >= imgHeight/Room.getTileSize() || (hitBox.x+hitBox.getWidth())/Room.getTileSize() >= imgWidth/Room.getTileSize()) return false;
//            else if(room.getMap()[(hitBox.y-(int)hitBox.getHeight()/2)/Room.getTileSize()][(hitBox.x)/Room.getTileSize()] || room.getMap()[(hitBox.y+(int)hitBox.getHeight()+(int)hitBox.getHeight()/2)/Room.getTileSize()][(hitBox.x+(int)hitBox.getWidth())/Room.getTileSize()]) {
//
//                return false;
//            }
            for(int y = (hitBox.y-(int) (hitBox.getHeight()/2.0))/Room.getTileSize(); y<=(hitBox.y+hitBox.getHeight()+hitBox.getHeight()/2)/Room.getTileSize(); y++) {


                for(int x = (hitBox.x)/Room.getTileSize(); x<=(hitBox.x+hitBox.getWidth())/Room.getTileSize(); x++) {
                    //if(room.getMap()[y][x].getVal() && hitBox.intersects(room.getMap()[y][x].getArea())) return false;
                    if(room.getMap()[y][x].getVal()) return false;
                }
            }

        }

        if(hitBox.getLocation().x < 0 || hitBox.getLocation().x+hitBox.getWidth() > imgWidth || hitBox.getLocation().y < 0 || hitBox.getLocation().y+hitBox.getHeight()*2 > imgHeight) { System.out.println("blocked"); return false;}
        for(Asset e : room.getAssets()) {
            if(hitBox.intersects(e.getCollision())){
                if(e instanceof Exit) { System.out.println(e.getCollision()); MoveRooms((Exit)e); }
                return false;
            }
        }
        collisionbox.x += xchange; collisionbox.y+=ychange;
        position.translate(offset.x-position.x,offset.y-position.y);
        location.translate(xchange,ychange);
        return true;
    }

    public void MoveRooms(Exit exit) {
        it.moveroom(exit);
    }

    public void AdjustPosition(Point location, int imageWidth, int imageHeight, int XViewPort, int YViewPort) {
        int x=0,y=0;
        if(location.x-XViewPort > 0 && location.x+XViewPort < imageWidth) x = 500;
        else if((location.x-XViewPort <= 0 && location.x+XViewPort >= imageWidth) || (location.x-XViewPort <= 0 && location.x+XViewPort < imageWidth)) x = location.x;
        else if(location.x-XViewPort > 0 && location.x+XViewPort >= imageWidth) x = location.x-(imageWidth-XViewPort*2);
        if(location.y-YViewPort > 0 && location.y+YViewPort < imageHeight) y = 350;
        else if((location.y-YViewPort <= 0 && location.y+YViewPort >= imageHeight) || (location.y-YViewPort <= 0 && location.y+YViewPort < imageHeight)) y = location.y;
        else if(location.y-YViewPort > 0 && location.y+YViewPort >= imageHeight) y = location.y-(imageHeight-YViewPort*2);
        this.location.x = location.x;
        this.location.y = location.y;
        position.x = x;
        position.y = y;
        collisionbox.setLocation((int) (location.x-(int)collisionbox.getWidth()/2.0), (int) (location.y-(int)collisionbox.getHeight()/2.0));
    }
}

interface Interaction {
    void interact();
    void moveroom(Exit exit);
}

class DoublePoint {
    public double x=0.0,y=0.0;
    public DoublePoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void translate(double v, double v1) {
        x += v;
        y += v1;
    }
}