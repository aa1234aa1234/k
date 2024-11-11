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
    private Tile[][] map;
    private static int tile_size = 10;

    public Room(String imagepath, Point spawnpoint) {
        try {
            int x=0,y=0;
            image = new BufferedImage(1000,700,BufferedImage.TYPE_INT_ARGB);
            BufferedImage img = ImageIO.read(new File(imagepath));
            this.spawnpoint = spawnpoint;
            if(img.getWidth() < 1000) x = (1000-img.getWidth())/2;
            if(img.getHeight() < 700) y = (700-img.getHeight())/2;
            if(x > 0 || y > 0) {
                image.getGraphics().drawImage(img,x,y,null);
                size = new Rectangle(x,y,img.getWidth(),img.getHeight());
            }
            else {
                image = ImageIO.read(new File(imagepath));
                size = new Rectangle(0,0,image.getWidth(),image.getHeight());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadCollision(BufferedImage image) {
        try {
            BufferedImage collisionMap = image;
            double mapWidth = collisionMap.getWidth()/tile_size;
            double mapHeight = collisionMap.getHeight()/tile_size;
            map = new Tile[(int) mapHeight+1][(int) mapWidth+1];
            for (int y = 0; y < mapHeight; y++) {
                for (int x = 0; x < mapWidth; x++) {
                    map[y][x] = isTileWalkable(image, (int) (x * tile_size), (int) (y * tile_size));
                    //System.out.print(map[y][x] + " ");
                }
                //System.out.println();
            }
            System.out.println(map.length + " fewafawef");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Tile isTileWalkable(BufferedImage image, int startX, int startY) {
        // Analyze the tile by checking the average color in this tile
        int pixelCount = 0;
        int totalBrightness = 0;

        for (int y = startY; y < startY + tile_size; y++) {
            for (int x = startX; x < startX + tile_size; x++) {
                int color = image.getRGB(x,y);
                if(((color >> 24) & 0xff) == 0) { pixelCount++; continue; }
                // Extract brightness based on the color (black and white threshold)
                int red = (color >> 16) & 0xff;
                int green = (color >> 8) & 0xff;
                int blue = color & 0xff;
                int brightness = (red + green + blue) / 3;
                totalBrightness += brightness;
                pixelCount++;
            }
        }

        // If the average brightness is above a threshold, assume it's walkable
        int averageBrightness = totalBrightness / pixelCount;
        return new Tile(averageBrightness > 127 ? false : true,new Rectangle(startX,startY, (int) tile_size, (int) tile_size)); // adjust threshold based on your image colors
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

    public Tile[][] getMap() {
        return map; 
    }

    public static int getTileSize() {
        return tile_size; 
    }
}

class Tile {
    private boolean value = true;
    private Rectangle area;

    public Tile(boolean val, Rectangle area) {
        value = val;
        this.area = area;
    }

    public Rectangle getArea() { return area;}

    public boolean getVal() { return value; }
}
