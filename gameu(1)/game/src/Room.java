import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Room {
    private BufferedImage image;
    private Rectangle size;
    private Point spawnpoint;
    private ArrayList<Asset> assets = new ArrayList<>();

    public Room(String imagepath, Rectangle size, Point spawnpoint) {
        try {
            image = ImageIO.read(new File(imagepath));
            this.size = size;
            this.spawnpoint = spawnpoint;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public BufferedImage getImage() {
        return image;
    }

    public Point getSpawnPoint() {
        return spawnpoint;
    }

    public ArrayList<Asset> getAssets() {
        return assets;
    }

    public void setAssets(ArrayList<Asset> assets) {
        this.assets = assets;
    }
}
