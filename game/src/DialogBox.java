import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.LinkedList;
import java.util.Queue;

public class DialogBox extends JLabel {

    private Queue<String> printQueue = new LinkedList<>();
    public DialogBox() {
        setVisible(true);
        setFont(new Font("Comic Sans MS",Font.PLAIN,30));
        setBackground(Color.BLACK);
        setForeground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(Color.WHITE,5));
        setOpaque(true);
        setText("* skibidi toilet");
        setBounds(94,418,812,214);
        //setLocation(94,418);
        //setPreferredSize(new Dimension(812,214));
    }

    @Override
    public void setText(String text) {
        text = "<html><div style='padding: 10px 20px;'>" + text + "</div></html>";
        super.setText(text);
    }

    public void printText(String str) {
        
    }
}
