package Main;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Player {
    private DoublePoint position = new DoublePoint(0,0);
    private Point location = new Point();
    private HitBox collisionbox;
    private HitBox[] interactionbox = new HitBox[4];
    private BufferedImage[][] image = new BufferedImage[4][3];
    private int direction = 0;
    private Interaction it;
    private int speed = 5;
    private int walking = 0;

    public Player(String imagepath, int width, int height) {
        try {
            BufferedImage image = ImageIO.read(new File("player_sprites/ehehe.png"));
            int[] ycoord = {1*248,3*248,2*248,0};
            int[] xcoord = {0*176,1*176,3*176};
            for(int i = 0; i<4; i++) {
                for(int j = 0; j<3; j++) {
                    this.image[i][j] = image.getSubimage(xcoord[j],ycoord[i],168,232);
                }

            }
            collisionbox = new HitBox(width, (int) (height/2.0), new Point());

            //interactionbox = new Main.HitBox()
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

    public void setPosition(Point pos) {
        position = new DoublePoint(pos.x,pos.y);
        //actual collision is set to
        location = new Point(pos.x,pos.y); collisionbox.setLocation((int) (location.x-(int)collisionbox.getWidth()/2.0), (int) (location.y-(int)collisionbox.getHeight()/2.0));

        System.out.println(collisionbox);
    }

    public Point getLocation() { return location; }

    public void setLocation(Point pos) { location = pos; }

    public BufferedImage getImage() { return image[direction][walking]; }

    public void ChangeDir(int dir) {
        direction = dir;
    }

    public void setWalking(int a) {
        walking = a;
    }

    public int isWalking() { return walking; }

    public int getDirection() { return direction; }

    public HitBox getInteractionBox() { return interactionbox[direction]; }

    public void setInteractionBox() {
        interactionbox[0] = new HitBox(collisionbox.width, (int) (collisionbox.height/2.0),new Point((collisionbox.x)-collisionbox.width,collisionbox.y+collisionbox.height));
        interactionbox[1] = new HitBox(collisionbox.width, (int) (collisionbox.height),new Point((collisionbox.x),collisionbox.y-collisionbox.height/2));
        interactionbox[2] = new HitBox(collisionbox.width, (int) (collisionbox.height/2.0),new Point((collisionbox.x)+collisionbox.width/2,collisionbox.y+collisionbox.height));
        interactionbox[3] = new HitBox(collisionbox.width, (int) (collisionbox.height/2.0),new Point((collisionbox.x),collisionbox.y+collisionbox.height));
    }

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


        //hitBox.translate(0, (int) (hitBox.getHeight()/2.0));
        //hitBox.y -= (int) (hitBox.getHeight()/2.0);
        if(room.getMap() != null) {
            if((hitBox.y+ychange)/Room.getTileSize() <= 0 || (hitBox.x+xchange)/Room.getTileSize() <= 0 || (hitBox.y+ychange+hitBox.getHeight()*2)/Room.getTileSize() >= (double) imgHeight /Room.getTileSize() || (hitBox.x+xchange+hitBox.getWidth())/Room.getTileSize() >= (double) imgWidth /Room.getTileSize()) return false;
//            else if(room.getMap()[(hitBox.y-(int)hitBox.getHeight()/2)/Main.Room.getTileSize()][(hitBox.x)/Main.Room.getTileSize()] || room.getMap()[(hitBox.y+(int)hitBox.getHeight()+(int)hitBox.getHeight()/2)/Main.Room.getTileSize()][(hitBox.x+(int)hitBox.getWidth())/Main.Room.getTileSize()]) {
//
//                return false;
//            }
            int a = xchange, b = ychange;//-(int) (hitBox.getHeight()/2.0)
            for(int y = (hitBox.y+b)/Room.getTileSize(); y<=(hitBox.y+hitBox.getHeight()+hitBox.getHeight()/2+b)/Room.getTileSize(); y++) {
                for(int x = (hitBox.x+a)/Room.getTileSize(); x<=(hitBox.x+a+hitBox.getWidth())/Room.getTileSize(); x++) {
                    //if(room.getMap()[y][x].getVal() && hitBox.intersects(room.getMap()[y][x].getArea())) return false;
                    if(room.getMap()[y][x].getVal()) return false;
                }
            }
//            for(int a = 0,b=0; a!=xchange && b != ychange; a+=xchange < 0 ? -1 : 1, b+=ychange < 0 ? -1 : 1) {
//                System.out.println(a + " " + b);
//
//            }


        }
        hitBox.x += xchange;hitBox.y+=ychange;
        if(hitBox.getLocation().x < 0 || hitBox.getLocation().x+hitBox.getWidth() > imgWidth || hitBox.getLocation().y < 0 || hitBox.getLocation().y+hitBox.getHeight()*2 > imgHeight) { System.out.println("blocked"); return false;}
        for(Pair<Asset,Integer> e : room.getAssets()) {
            if(hitBox.intersects(e.getFirst().getCollision())){
                if(e.getFirst() instanceof Exit) { System.out.println(e.getFirst().getCollision()); MoveRooms((Exit)e.getFirst()); }
                return false;
            }
        }
        collisionbox.x += xchange; collisionbox.y+=ychange;
        position.translate(offset.x-position.x,offset.y-position.y);
        location.translate(xchange,ychange);
        setInteractionBox();
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
        setInteractionBox();
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