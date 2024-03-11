package 용사키우기2;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.text.AttributeSet.ColorAttribute;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class IntroScene extends JPanel implements KeyListener,Serializable {

	public interface IKeydown {
		void onKeyDown(int key);
	}
	private JLabel introText = new JLabel("");
	private int transparency = 0, dialognumber = 0, imagenumber = 0;
	private TimerClass dialogTimerClass1,dialogTimerClass2;
	private TimerClass[] transitionTimerClass = new TimerClass[2];
	private ArrayList<String> introlines = new ArrayList<>();
	private BufferedImage[] images;
	private IKeydown keydown;
	private ImagePanel imagepanel;
	private JPanel keyboard[] = new JPanel[2];
	private Key[] keys = new Key[54];
	private boolean doneflag = false, flag = false;
	private JLabel nameinput = new JLabel();
	private transient SpringLayout layout = new SpringLayout();
	int selectedkey = 0;
	public IntroScene() {
		keyboard[0] = new JPanel(new GridLayout(2,13));
		keyboard[1] = new JPanel(new GridLayout(2,13));
		keyboard[0].setPreferredSize(new Dimension(700,130));
		keyboard[0].setBackground(Color.BLACK);
		keyboard[1].setPreferredSize(new Dimension(700,100));
		keyboard[1].setBackground(Color.BLACK);
		nameinput.setForeground(Color.WHITE);
		nameinput.setFont(new Font(getFont().getName(),0,15));
		introText.setFont(new Font("SANS_SERIF",0, 30));
        introText.setPreferredSize(new Dimension(700,300));
        introText.setPreferredSize(new Dimension(500,300));
		introText.setHorizontalAlignment(SwingConstants.CENTER);
		introlines.add("A child happens to be appointed as the sacrificial lamb for the village god who they worship.");
		introlines.add("Determined to stay alive the child decides to kill the village leader.");
		images = new BufferedImage[introlines.size()];
		
		for(int i = 0; i<introlines.size(); i++) {
			try {
				//images[i] = ImageIO.read(new File("image" + i + ".png"));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			introlines.get(i).replace("\n", "</br>");
		}
		imagepanel = new ImagePanel(transparency);
		imagepanel.setPreferredSize(new Dimension(700,700));
		layout.putConstraint(SpringLayout.NORTH,introText,320,SpringLayout.NORTH,this);
		layout.putConstraint(SpringLayout.WEST, introText, 50, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, imagepanel, 0, SpringLayout.NORTH,this);
		layout.putConstraint(SpringLayout.WEST, imagepanel, 0, SpringLayout.WEST,this);
		layout.putConstraint(SpringLayout.NORTH, keyboard[0], 300, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.NORTH, keyboard[1], 430, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.NORTH, nameinput, 250, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.WEST, nameinput,300,SpringLayout.WEST,this);
//		layout.putConstraint(SpringLayout.EAST, keyboard[0], 10, SpringLayout.EAST, this);
//		layout.putConstraint(SpringLayout.WEST, keyboard[0], -10, SpringLayout.WEST, this);
//		layout.putConstraint(SpringLayout.EAST, keyboard[1], 10, SpringLayout.EAST, this);
//		layout.putConstraint(SpringLayout.WEST, keyboard[1], -10, SpringLayout.WEST, this);
		setLayout(layout);
		add(introText);
		//add(imagepanel);
		add(keyboard[0]);
		add(keyboard[1]);
		add(nameinput);
		keyboard[0].setVisible(false);
		keyboard[1].setVisible(false);
	}
	public void setKeydown(IKeydown a) {
		keydown = a;
	}
	private void Transition() {
		boolean flag = true;
		transitionTimerClass[0] = new TimerClass(5,e -> {
			//imagepanel.setTransparency(--transparency);
			transparency--;
			//imagepanel.repaint();
			repaint();
			if(transparency == 0) {
				((TimerClass)e.getSource()).stop();
				if(imagenumber < 4) repaint();
				dialogTimerClass2.start();
				return;
			}
		});
		transitionTimerClass[1] = new TimerClass(5, e -> {
			//imagepanel.setTransparency(++transparency);
			transparency++;
			//imagepanel.repaint();
			repaint();
			if(transparency == 255) {
				((TimerClass)e.getSource()).stop();
				imagenumber++;
				transitionTimerClass[0].start();
			}
			else if(transparency == 150) {
				introText.setText("");
			}
		});
		transitionTimerClass[1].start();
	}
	public void InitiateCutScene() {
		//main.addKeyListener(this);
		int[] idx = {0};
		repaint();
		dialogTimerClass2 = new TimerClass(100, e -> {
			idx[0]=0;
			dialognumber++;
			introText.setText("");
			dialogTimerClass1.start();
			((TimerClass)e.getSource()).stop();
		});
		dialogTimerClass1 = new TimerClass(200, e -> {
			if(dialognumber >= introlines.size()) {
				((TimerClass)e.getSource()).stop();
				introText.setText("<html><strong>ok thats it, thx.</strong><br></br><small>press z to continue.</small></html>");
				doneflag = true;
				return;
			}
			if(idx[0] < introlines.get(dialognumber).length()) {
				introText.setText(introText.getText().replace("<html>", "").replace("</html>", "").replace("<body>", "").replace("</body>", "").replace("<div>", "").replace("</div>", ""));
				String[] msg = introlines.get(dialognumber).split(" ");
				introText.setText("<html><body>" + introText.getText() + introlines.get(dialognumber).charAt(idx[0]) + "</body></html>");
				idx[0]++;
				repaint();
			}
			else {
				//dialogTimerClass2.start();
				((TimerClass)e.getSource()).stop();
				Transition();
			}
		});
		dialogTimerClass1.setInitialDelay(200);
		dialogTimerClass1.start();
	}
	public void endIntro() {
		if(dialognumber < introlines.size()) {
			dialogTimerClass2.stop();
			dialogTimerClass1.stop();
			if(transitionTimerClass[0] != null) {
				transitionTimerClass[0].stop();
				transitionTimerClass[1].stop();
			}
			Transition();
			dialognumber = introlines.size();
			dialogTimerClass1.setInitialDelay(0);
			//InitiateCutScene();
			repaint();
			doneflag = true;
		}
	}
	public void setkeyboard() {
		setBackground(Color.BLACK);
		keys[52] = new Key("OK");
		keys[53] = new Key("DEL");
		for(int i = 0,k=26; i<26 && k<52; i++,k++) {
			keys[i] = new Key(String.valueOf((char)('A'+i)));
			keys[k] = new Key(String.valueOf((char)('a'+i)));
			keyboard[0].add(keys[i]);
			keyboard[1].add(keys[k]);
		}
		keyboard[1].add(keys[52]);
		keyboard[1].add(keys[53]);
		keyboard[0].setVisible(true);
		keyboard[1].setVisible(true);
		repaint();
		selectedkey = 0;
		keys[selectedkey].setselected(true);
		keys[selectedkey].repaint();
	}
	public String presskey() {
		if(!keys[selectedkey].getText().equals("OK") && !keys[selectedkey].getText().equals("DEL")) {
			if(nameinput.getText().length() == 6) return "";
			nameinput.setText(nameinput.getText() + keys[selectedkey].getText());
		}
		else {
			if(keys[selectedkey].getText().equals("OK")) {
				return nameinput.getText();
			}
			if(nameinput.getText().length() == 0) return "";
			nameinput.setText(nameinput.getText().substring(0,nameinput.getText().length()-1));
		}
		return "";
	}
	public void updatekey(int previouskey) {
		keys[previouskey].repaint();
		keys[previouskey].setselected(false);
		keys[selectedkey].setselected(true);
		keys[selectedkey].repaint();
	}
	public void control() {
		//remove(introText);
		setkeyboard();
		revalidate();
		repaint();
		add(new JLabel("Name the child.") {{setFont(new Font("SERIF",0,60)); setForeground(Color.WHITE); setVerticalAlignment(SwingConstants.BOTTOM); setHorizontalAlignment(SwingConstants.CENTER);}});
		introText.setForeground(Color.WHITE);
		introText.setFont(new Font(getFont().getName(),0,15));
		introText.setVerticalAlignment(SwingConstants.CENTER);
		layout.putConstraint(SpringLayout.NORTH, introText, 445, SpringLayout.NORTH, this);
		introText.setText("<html><body>"
				+ "<div>"
				+ "<p>[Z} is the main method of interacting with the game.</p>"
				+ "<p>[X} can cancel menus or skip messages.</p>"
				+ "<p>[C} is for opening the main menu.</p>"
				+ "<p>You can use arrow keys to move around.<br/>You're represented via the red circle.</p>"
				+ "</div></body></html>");
		//dialognumber++;
	}
	public int getSelectedKey() {
		return selectedkey;
	}
	public void setSelectedKey(int a) {
		selectedkey = a;
	}
	public boolean getflag() {
		return dialognumber > introlines.size() && isdone() ? true : false;
	}
	public boolean isdone() {
		return doneflag && !transitionTimerClass[0].isRunning() && !transitionTimerClass[1].isRunning();
	}
	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		Graphics2D g = (Graphics2D)graphics;
		g.setColor(new Color(0,0,0,transparency));
		g.fillRect(0,0,700,700);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		keydown.onKeyDown(e.getKeyCode());
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
	}
}

class ImagePanel extends JPanel implements Serializable{
	private int transparency=0;
	public ImagePanel(int trans) {
		this.transparency = trans;
	}
	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		Graphics2D g = (Graphics2D)graphics;
		g.setColor(new Color(0,0,0,transparency));
		g.fillRect(0,0,700,700);
	}
	public void setTransparency(int a) {
		transparency = a;
	}
}

class Key extends JLabel implements Serializable{
	private String key = "";
	private boolean selected = false;
	public Key(String k) { 
		key = k;
		setText(key);
		setForeground(Color.WHITE);
		setPreferredSize(new Dimension(45,45));
		setFont(new Font(getFont().getName(), Font.PLAIN, 14));
	}
	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		if(selected) {
			setForeground(Color.YELLOW);
		}
		else {
			setForeground(Color.WHITE);
		}
	}
	public void setselected(boolean s) {
		selected = s;
	}
}