import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class DialogBox extends JLabel {
    public DialogBox() {
        setVisible(true);
        setFont(new Font("Serif",Font.PLAIN,40));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(Color.BLACK,5));
        //setBounds(94,418,812,214);
        setLocation(94,418);
        setPreferredSize(new Dimension(812,+214));
    }
}
