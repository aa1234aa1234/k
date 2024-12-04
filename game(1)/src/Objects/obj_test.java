package Objects;

import Main.Asset;

import java.awt.*;

public class obj_test extends Asset {
    private boolean flag = false;

    public obj_test() {
        super(70, 100, 500, 350, new Rectangle(500,350,70,100), "assets/bob.png");
    }

    public void setFlag(boolean a) {
        flag = a;
    }

    public boolean getFlag() {
        return flag;
    }
}
