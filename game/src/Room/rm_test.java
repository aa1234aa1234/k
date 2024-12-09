package Room;

import Main.Room;

import java.awt.*;

public class rm_test extends Room {

    private boolean flag = false;

    public rm_test() {
        super("maps/lol.png", new Point(500,350), 3);
    }

    public void setFlag(boolean a) {
        flag = a;
    }

    public boolean getFlag() {
        return flag;
    }
}
