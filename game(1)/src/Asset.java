import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class Asset {
    private HitBox collision;
    private BufferedImage image;

    public Asset(int Width, int Height, int x, int y, String imagepath) {
        try {
            collision = new HitBox(Width,Height,new Point(x,y));
            image = ImageIO.read(new File(imagepath));
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    public Asset(int Width, int Height, int x, int y){
        collision = new HitBox(Width,Height,new Point(x,y));
    }

    public HitBox getCollision() {
        return collision;
    }

    public void setCollision(HitBox collision) {
        this.collision = collision;
    }

    public BufferedImage getImage() { return image; }
}
