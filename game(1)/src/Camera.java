import java.awt.*;

public class Camera {
    private int XViewport = 500, YViewport = 350;
    private Point position = new Point();

    public boolean move(boolean left, boolean right, boolean up, boolean bottom, Point playerpos, int imgWidth, int imgHeight) {
        if(left) {
            //position.x -= position.x-XViewport-5 < 0 ? 0 : 5;

            //if(playerpos.x-XViewport > 0 && playerpos.x+XViewport <= imgWidth) position.x -= 5;
            position.x -= position.x-XViewport > 0 && playerpos.x-XViewport <= 5 ? 5 : 0;
        }
        if(right) {
            //location.x += speed;
            //if(playerpos.x-XViewport > 0 && position.x+XViewport < imgWidth) position.x += 5;
            //position.x += position.x+XViewport+5 < imgWidth ? 5 : 0;

            //if(playerpos.x+XViewport < imgWidth && playerpos.x-XViewport > 0) position.x += 5;
            position.x += playerpos.x-XViewport > 0 && position.x+XViewport < imgWidth ? 5 : 0;
        }
        if(up) {
            //location.y -= speed*1.5;

            //if(playerpos.y-YViewport > 0 && playerpos.y+YViewport <= imgHeight) position.y -= 5;
            position.y -= position.y-YViewport > 0 && playerpos.y-YViewport <= 5 ? 5 : 0;
        }
        if(bottom) {
            //location.y += speed*1.5;
            //if(position.y+YViewport < imgHeight) position.y += 5;
            //position.y += position.y+YViewport+5 < imgHeight ? 5 : 0;

            //if(playerpos.y+YViewport < imgHeight && playerpos.y-YViewport > 0) position.y += 5;
            position.y += playerpos.y-YViewport > 0 && position.y+YViewport < imgHeight ? 5 : 0;
        }
        return true;
    }

    public void AdjustPosition(Point playerLocation, int imgWidth, int imgHeight) {
        int x=0,y=0;
        if(playerLocation.x-XViewport <= 0) x = XViewport;
        else if(playerLocation.x+XViewport < imgWidth) x = playerLocation.x;
        else x = imgWidth-XViewport;
        if(playerLocation.y-YViewport <= 0) y = YViewport;
        else if(playerLocation.y+YViewport < imgHeight) y = playerLocation.y;
        else y = imgHeight-YViewport;
        position.x = x;
        position.y = y;
    }

    public int getXViewport() {
        return XViewport;
    }

    public void setXViewport(int XViewport) {
        this.XViewport = XViewport;
    }

    public int getYViewport() {
        return YViewport;
    }

    public void setYViewport(int YViewport) {
        this.YViewport = YViewport;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }
}
