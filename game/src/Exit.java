import java.awt.*;

public class Exit extends Asset{
    private int NextRoomNo;
    private Point EnterPoint = new Point();
    public Exit(int Width, int Height, int x, int y) {
        super(Width, Height, x, y);
    }

    public int getNextRoomNo() {
        return NextRoomNo;
    }

    public void setNextRoomNo(int nextRoomNo) {
        NextRoomNo = nextRoomNo;
    }

    public Point getEnterPoint() {
        return EnterPoint;
    }

    public void setEnterPoint(Point enterPoint) {
        EnterPoint = enterPoint;
    }
}
