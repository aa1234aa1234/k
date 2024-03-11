package 용사키우기2;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.OverlayLayout;
import javax.swing.SpringLayout;
import javax.swing.Timer;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.text.AttributeSet.ColorAttribute;

import 용사키우기2.Menu;
import 용사키우기2.IntroScene.IKeydown;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.util.Queue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class mainclass extends JFrame implements KeyListener,Serializable {
	private final int screenWidth = 1000, screenHeight = 1000;
	private PlayerData player = new PlayerData();
	private Point playerpos = new Point(3,6);
	private JPanel panel = new JPanel();
	private GamePanel mainmap = new GamePanel(this);
	private Menu dialogbox = new Menu();
	private SaveMenu savemenu = new SaveMenu();
	private StartingScreen startscreen = new StartingScreen();
	private transient BattleMenu battlemenu = new BattleMenu(this);
	private IntroScene intro = new IntroScene();
	private ArrayList<Room> rooms = new ArrayList<>(); 
	private String currentMenu = "overworld",lastMenu="";
	private int roomno = 0, menubutton = 0, item = 0, itemmenu;
	private transient Menu[] menu = new Menu[2];
	private Random r = new Random();
	private transient Monster[] monsters = new Monster[7];
	private transient ArrayList<Item> items = new ArrayList<>();
	private boolean flag =false, isPaused = false, printinprogress = false, hasNextMessage = false;
	private Queue<TimerClass> TimerClassQueue = new LinkedList<>();
	private Queue<String> messageQueue = new LinkedList<>();
	private TimerClass currentTimerClass;
	private transient SpringLayout layout = new SpringLayout();
	private String saveplayername="placeholdername";
	private BossBattle bossfight = new BossBattle(this);
	private int saveroomno=-1;
	public mainclass() {
		super("rpg");
		initialize();
	}
	public static void main(String[] args) {
		new mainclass();
	}
	private void initialize() {
		resetgamedata();
		setgamedata();
		initializegamewindow();
		loadoverworld();
		((CardLayout)panel.getLayout()).show(panel,"bossfight");
		for(KeyListener listen : getKeyListeners()) {
			removeKeyListener(listen);
		}
		bossfight.setlistener();
	}
	private void resetgamedata() {
		repaint();
		player = new PlayerData();
		playerpos = new Point(3,6);
		panel = new JPanel();
		mainmap = new GamePanel(this);
		dialogbox = new Menu();
		savemenu = new SaveMenu();
		startscreen = null;
		startscreen = new StartingScreen();
		battlemenu = new BattleMenu(this);
		intro = new IntroScene();
		rooms = new ArrayList<>(); 
		currentMenu = "overworld";
		lastMenu="";
		roomno = 0;
		menubutton = 0;
		item = 0;
		itemmenu=0;
		menu = new Menu[2];
		monsters = new Monster[7];
		items = new ArrayList<>();
		flag =false;
		isPaused = false;
		printinprogress = false;
		hasNextMessage = false;
		TimerClassQueue = new LinkedList<>();
		messageQueue = new LinkedList<>();
		layout = new SpringLayout();
		saveplayername = "placeholdername";
		saveroomno = -1;
		panel.repaint(new Rectangle(0,0,700,780));
		mainmap.repaint();
		startscreen.repaint();
	}
	private void setgamedata() {
		new LootUtilities();
		monsters[0] = new Monster("Villager",50,10,5,15);
		monsters[1] = new Monster("Farmer",100,20,10,50);
		monsters[2] = new Monster("Nitwit",150,25,7,45);
		monsters[3] = new Monster("Hunter",250,60,60,600);
		monsters[4] = new Monster("Village Idiot",245,70,45,550);
		monsters[5] = new Monster("Village Mob",595,185,127,12345);
		monsters[6] = new Boss("Villager Leader",1000,1000,1000,12314151);
		items.add(new Equipment("WEAPON_TYPE_1","WEAPON",15));
		items.add(new Equipment("WEAPON_TYPE_2","WEAPON",30,1));
		items.add(new Equipment("WEAPON_TYPE_3","WEAPON",45,2));
		items.add(new Equipment("WEAPON_TYPE_4","WEAPON",60,3));
		items.add(new Equipment("ARMOR_TYPE_1_1","ARMOR",30));
		items.add(new Equipment("ARMOR_TYPE_1_2","ARMOR",80,3));
		items.add(new Equipment("ARMOR_TYPE_1_3","ARMOR",50,1));
		items.add(new Equipment("ARMOR_TYPE_2","ARMOR",25,2));
		for(int i = 0; i<20; i++) {
			switch(i) {
			case 1:
				//rooms.add(new Room(r.nextInt(10)+1,new String[] {"hassave"}));
				rooms.add(new Room(r.nextInt(10)+1,new ArrayList<String>() {{add("hassave");}}));
				break;
			case 18:
				rooms.add(new Room(r.nextInt(10)+1,new ArrayList<String>() {{add("hassave"); add("hasloot|5|1");}})); 
				rooms.get(i).getLoot(5,1).add(items.get(3));
				rooms.get(i).getLoot(5,1).add(items.get(5));
				rooms.get(i).getLoot(5,1).add(new Item("potion","allstatsup",50));
				rooms.get(i).getLoot(5,1).add(new Item("more potion","allstatsup",50));
				break;
			case 3:
				rooms.add(new Room(r.nextInt(10)+1,new ArrayList<String>() {{add("hasloot|5|1");}}));
				rooms.get(i).getLoot(5, 1).add(items.get(0));
				break;
			case 2:
				rooms.add(new Room(r.nextInt(10)+1,new ArrayList<String>() {{add("hasloot|5|1");}}));
				//rooms.add(new Room(r.nextInt(10)+1,new String[] {"hasloot"}));
				rooms.get(i).getLoot(5,1).add(items.get(2));
				break;
			case 0:
				String dialog = "Congrats! You interacted with an object.`Press [X] to exit or skip messages.`Press arrow keys to move around.";
				String dialog2 = "Gray circles are non interactable objects.`Yellow circles are save points.`Brown circles are chests. You can interact with them to gain items.`The burgundy tinted circles is how you progress.\nStep into them to move to the next room.";
				String dialog3 = "You can only proceed. Once you've progressed there is no going back.";
				rooms.add(new Room(r.nextInt(10)+1,new ArrayList<String>() {{add(dialog + "|6|0"); add(dialog2 + "|6|1"); add(dialog3);}}));
				break;
			default:
				rooms.add(new Room(r.nextInt(10)+1));
			}
			
		}
		for(Monster a : monsters) {
			System.out.println(a.getDropRate());
		}
	}
	JLabel test = new JLabel();
	private void startintro() {
		for(KeyListener listen : getKeyListeners()) {
			removeKeyListener(listen);
		}
		addKeyListener(intro);
		intro.InitiateCutScene();
	}
	private void endintro() {
		for(KeyListener listen : getKeyListeners()) {
			removeKeyListener(listen);
		}
		addKeyListener(this);
	}

	private void initializegamewindow() {
		Container c = getContentPane();
		
		panel.setLayout(new CardLayout());
		test.setPreferredSize(new Dimension(150,150));
		c.add(test);
		c.setLayout(layout);
		addKeyListener(this);
		c.add(panel);
		menu[0] = new Menu();
		menu[1] = new Menu();
		((SpringLayout)mainmap.getLayout()).putConstraint(SpringLayout.NORTH,dialogbox,420,SpringLayout.NORTH,mainmap);
		((SpringLayout)mainmap.getLayout()).putConstraint(SpringLayout.WEST,dialogbox,50,SpringLayout.WEST,mainmap);
		((SpringLayout)mainmap.getLayout()).putConstraint(SpringLayout.NORTH,menu[0],100,SpringLayout.NORTH,mainmap);
		((SpringLayout)mainmap.getLayout()).putConstraint(SpringLayout.WEST,menu[0],90,SpringLayout.WEST,mainmap);
		((SpringLayout)mainmap.getLayout()).putConstraint(SpringLayout.NORTH,menu[1],100,SpringLayout.NORTH,mainmap);
		((SpringLayout)mainmap.getLayout()).putConstraint(SpringLayout.WEST,menu[1],220,SpringLayout.WEST,mainmap);
		((SpringLayout)mainmap.getLayout()).putConstraint(SpringLayout.WEST,savemenu,100,SpringLayout.WEST,mainmap);
		((SpringLayout)mainmap.getLayout()).putConstraint(SpringLayout.NORTH,savemenu,217,SpringLayout.WEST,mainmap);
		mainmap.add(dialogbox);
		mainmap.add(menu[0]);
		mainmap.add(menu[1]);
		mainmap.add(savemenu);
		dialogbox.setPreferredSize(new Dimension(600,200));
		menu[0].setPreferredSize(new Dimension(120,200));
		menu[1].setPreferredSize(new Dimension(340,325));//300 200
		intro.setPreferredSize(new Dimension(getWidth(),getHeight()));
		
		panel.add(intro,"introscene");
		panel.add(mainmap,"overworld");
		panel.add(battlemenu,"battlemenu");
		panel.add(startscreen,"savescreen");
		panel.add(bossfight,"bossfight");
		intro.setKeydown(new IKeydown() {
			public void onKeyDown(int key) {
				switch(key) {
				case KeyEvent.VK_Z:
//					if(intro.getflag()) {

//						break;
//					}
					if(intro.isdone()) {
						if(new File("save.txt").exists()) {
							loadsavefile();
							return;
						}
						intro.control();
						intro.setKeydown(new IKeydown() {
							@Override
							public void onKeyDown(int key) {
								// TODO Auto-generated method stub
								int previous = intro.getSelectedKey();
								switch(key) {
								case KeyEvent.VK_UP:
									int move = (intro.getSelectedKey() >= 39 ? 14 : 13);
									if(intro.getSelectedKey()-move < 0) return;
									intro.setSelectedKey(intro.getSelectedKey() - move);
									break;
								case KeyEvent.VK_DOWN:
									move = (intro.getSelectedKey() >= 26 ? 14 : 13);
									if(intro.getSelectedKey()+move > 53) return;
									intro.setSelectedKey(intro.getSelectedKey() + move);
									break;
								case KeyEvent.VK_LEFT:
									if(intro.getSelectedKey()-1 < 0) return;
									intro.setSelectedKey(intro.getSelectedKey() - 1);
									break;
								case KeyEvent.VK_RIGHT:
									if(intro.getSelectedKey()+1 > 53) return;
									intro.setSelectedKey(intro.getSelectedKey() + 1);
									break;
								case KeyEvent.VK_Z:
									String name = intro.presskey();
									if(name.length() != 0) {
										player.setName(name);
										endintro();
										((CardLayout)panel.getLayout()).show(panel,"overworld");
									}
									break;
								}
								intro.updatekey(previous);
							}
						});
						return;
//						endintro();
//						System.out.println("test");
					}
					intro.endIntro();
					break;
				}
			}
		});
		layout.putConstraint(SpringLayout.NORTH,panel,50,SpringLayout.NORTH,c);
		layout.putConstraint(SpringLayout.SOUTH,panel,730,SpringLayout.NORTH,c);
		layout.putConstraint(SpringLayout.WEST,panel,150,SpringLayout.WEST,c);
		panel.setPreferredSize(new Dimension(700,780));
		setSize(screenWidth,screenHeight);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		startintro();
	}
	private void loadsavefile() {
		endintro();
		((CardLayout)panel.getLayout()).show(panel,"savescreen");
		startscreen.repaint();
		startscreen.getSaveScreen().setBorder(new CompoundBorder(new LineBorder(Color.WHITE, 4), new EmptyBorder(10, 10, 10, 10)));
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File("save.txt")))){
			player = (PlayerData) ois.readObject();
			roomno = (int) ois.readObject();
			item = (int) ois.readObject();
			menubutton = (int) ois.readObject();
			itemmenu = (int) ois.readObject();
			playerpos = (Point) ois.readObject();
			rooms = (ArrayList<Room>) ois.readObject();
			monsters = (Monster[]) ois.readObject();
			saveplayername = player.getName();
			saveroomno = roomno;
		}catch(Exception e) {
			e.printStackTrace();
		}
		currentMenu = "startingscreen";
		startscreen.getSaveScreen().setDialog(saveplayername, saveroomno);
	}
	private void loadoverworld() {
		panel.repaint();
	}
	private void enterbattle(Monster enemy) {
		CardLayout layout = (CardLayout)panel.getLayout();
		layout.next(panel);
		removeKeyListener(this);
		battlemenu.InitializeEncounter(enemy);
		enemy.setFlag(true);
	}
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
	}
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		switch(currentMenu) {
		case "overworld":
			move(e.getKeyCode());
			break;
		case "menu":
			menu(e.getKeyCode());
			break;
		case "items":
			items(e.getKeyCode());
			break;
		case "itemmenu":
			itemmenu(e.getKeyCode());
			break;
		case "dialog":
			switch(e.getKeyCode()) {
			case KeyEvent.VK_Z:
				if(flag) return;
				else if(isPaused) {
					isPaused = false;
					if(printinprogress) {
						dialogbox.setText("");
					}
					else {
						dialogbox.setVisible(false);
						currentMenu = lastMenu.equals("itemmenu") ? (lastMenu="items") : lastMenu;
					}
					
				}
				break;
			case KeyEvent.VK_X:
				if(currentTimerClass == null) return;
				if(flag) {
					String msg = messageQueue.poll();
					if(msg == null) return;
					//dialogbox.setText(msg.contains("<br/>") ? ("<html><body><div>" + msg + "</div></body></html>") : msg);
					dialogbox.setText("<html><body><div>" + msg + "</div></body></html>");
					isPaused = !isPaused;
			        flag = false;
//			    	if(hasNextMessage) isPaused = true; 
//			        else if(!hasNextMessage) hasNextMessage = true;
			        
			        currentTimerClass=TimerClassQueue.poll();
			        if(TimerClassQueue.isEmpty()) {
				        printinprogress = false;
			        }
			        currentTimerClass=TimerClassQueue.peek();
				}
			}
			break;
		case "savemenu":
			savemenu(e.getKeyCode());
			break;
		case "startingscreen":
			savescreen(e.getKeyCode());
			break;
		}
	}
	private void savescreen(int a) {
		switch(a) {
		case KeyEvent.VK_RIGHT:
			startscreen.getSaveScreen().setSelection(startscreen.getSaveScreen().getSelection() < 1 ? 1 : 1);
			break;
		case KeyEvent.VK_LEFT:
			startscreen.getSaveScreen().setSelection(startscreen.getSaveScreen().getSelection() > 0 ? 0 : 0);
			break;
		case KeyEvent.VK_Z:
			if(startscreen.getSaveScreen().getSelections()[startscreen.getSaveScreen().getSelection()].equals("Reset")) {
				File file = new File("save.txt");
				file.delete();
				getContentPane().removeAll();
				initialize();
				
				revalidate();
				repaint();
				return;
			}
			currentMenu = "overworld";
			((CardLayout)panel.getLayout()).show(panel,"overworld");
			break;
		default:
			return;
		}
		startscreen.getSaveScreen().setDialog(player.getName(), roomno);
	}
	private void save() {
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("save.txt")));
			oos.writeObject(player);
			oos.writeObject(roomno);
			oos.writeObject(item);
			oos.writeObject(menubutton);
			oos.writeObject(itemmenu);
			oos.writeObject(playerpos);
			oos.writeObject(rooms);
			oos.writeObject(monsters);
			oos.flush();
			oos.close();
		}catch(IOException e) {
			System.out.println(e.getClass().toString());
		}
	}
	public void savemenu(int a) {
		switch(a) {
		case KeyEvent.VK_LEFT:
			if(savemenu.getSelection() == -1) return;
			savemenu.setSelection(savemenu.getSelection() > 0 ? savemenu.getSelection()-1 : 0);
			break;
		case KeyEvent.VK_RIGHT:
			if(savemenu.getSelection() == -1) return;
			savemenu.setSelection(savemenu.getSelection() < savemenu.getSelections().length ? savemenu.getSelection()+1 : savemenu.getSelection());
			break;
		case KeyEvent.VK_Z:
			if(savemenu.getSelection() == -1) return;
			if(savemenu.getSelections()[savemenu.getSelection()].equals("YES")) {
				savemenu.setSelection(-1);
				saveplayername = player.getName();
				saveroomno = roomno;
				save();
			}
			else {
				savemenu.setVisible(false);
				currentMenu = "overworld";
				return;
			}
			break;
		case KeyEvent.VK_X:
			savemenu.setVisible(false);
			currentMenu = "overworld";
			return;
		}
		displaySaveMenu();
	}
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
	}
	private void ItemMenuFunc() {
		if(itemmenu == 0) {
			printDialog(player.getInventory()[item].getName() + " was discarded.");
			player.removeItem(item);
			if(player.getItemCount() < 1) {
				lastMenu = "menu";
				currentMenu = "menu";
				displayMenu();
				printItemMenu();
				return;
			}
			if(item == player.getItemCount()) item--;
		}
		else if(itemmenu == 1){
			printDialog(player.getInventory()[item].getInfo());
		}
		else {
			String type = player.getInventory()[item].getType();
			if(type.equals("weapon") || type.equals("armor")) {
				printDialog("You equipped the " + player.getInventory()[item].getName() + ".");
				Equipment temp = player.getEquipment(type);
				player.Equip(type,(Equipment)player.getInventory()[item]);
				player.getInventory()[item] = temp;
				if(temp == null) {
					player.removeItem(item);
					if(item == player.getItemCount()) item--;
				}
			}
//			else if(type.equals("armor")) {
//				printDialog("You equipped the " + player.getInventory()[item].getName() + ".");
//				Item temp = player.getArmor();
//				player.equipArmor(player.getInventory()[item]);
//				player.getInventory()[item] = temp;
//				if(temp == null) {
//					player.removeItem(item);
//				}
//				if(item > 1 && item == player.getItemCount()-1) item--;
//			}
			else {
				printDialog("You used the " + player.getInventory()[item].getName() + ".");
				player.UseItem(item);
			}
		}
		//currentMenu = player.getItemCount() < 1 ? "menu" : "items";
		if(player.getItemCount() < 1) {
			lastMenu = "menu";
			currentMenu = "menu";
			displayMenu();
			return;
		}
		printItemMenu();
		return;
	}
	private void itemmenu(int a) {
		switch(a) {
		case KeyEvent.VK_LEFT:
			if(itemmenu > 0) {
				itemmenu--;
			}
			break;
		case KeyEvent.VK_RIGHT:
			if(itemmenu < 2) {
				itemmenu++;
			}
			break;
		case KeyEvent.VK_X:
			currentMenu = "items";
			break;
		case KeyEvent.VK_Z:
			ItemMenuFunc();
			break;
		}
		printItemMenu();
	}
	private void menu(int a) {
		switch(a) {
		case KeyEvent.VK_UP:
			if(menubutton > 0) {
				menubutton--;
			}
			break;
		case KeyEvent.VK_DOWN:
			if(menubutton < 1) {
				menubutton++;
			}
			break;
		case KeyEvent.VK_Z:
			if(menubutton == 1) {
				if(player.getItemCount() < 1) return; 
				currentMenu = "items";
			}
			break;
		case KeyEvent.VK_X:
			currentMenu = "overworld";
			menu[0].setVisible(false);
			menu[1].setVisible(false);
			break;
		}
		displayMenu();
		displaySideMenu();
	}
	private void items(int a) {
		switch(a) {
		case KeyEvent.VK_UP:
			if(item > 0) {
				item--;
			}
			break;
		case KeyEvent.VK_DOWN:
			if(item < player.getItemCount()-1) {
				item++;
			}
			break;
		case KeyEvent.VK_X:
			currentMenu = "menu";
			displayMenu();
			break;
		case KeyEvent.VK_Z:
			currentMenu = "itemmenu";
			printItemMenu();
			return;
		}
		printItemMenu();
	}
	private void move(int a) {
		int y = playerpos.y; int x = playerpos.x;
		switch(a) {
		case KeyEvent.VK_UP:
//			if(playerpos.y > 0 && rooms.get(roomno).getMap()[playerpos.y-1][playerpos.x] != 1 && rooms.get(roomno).getMap()[playerpos.y-1][playerpos.x] != 3) {
//				playerpos.y--;
//			}
			if(playerpos.y > 0 && rooms.get(roomno).getMap()[playerpos.y-1][playerpos.x] <= 1) {
				playerpos.y--;
			}
			player.getDirection().y = -1;
			player.getDirection().x = 0;
			break;
		case KeyEvent.VK_DOWN:
//			if(playerpos.y < 6 && rooms.get(roomno).getMap()[playerpos.y+1][playerpos.x] != 1 && rooms.get(roomno).getMap()[playerpos.y+1][playerpos.x] != 3) {
//				playerpos.y++;
//			}
			if(playerpos.y < 6 && rooms.get(roomno).getMap()[playerpos.y+1][playerpos.x] <= 1) {
				playerpos.y++;
			}
			player.getDirection().y = 1;
			player.getDirection().x = 0;
			break;
		case KeyEvent.VK_RIGHT:
//			if(playerpos.x < 6 && rooms.get(roomno).getMap()[playerpos.y][playerpos.x+1] != 1 && rooms.get(roomno).getMap()[playerpos.y][playerpos.x+1] != 3) {
//				playerpos.x++;
//			}
			if(playerpos.x < 6 && rooms.get(roomno).getMap()[playerpos.y][playerpos.x+1] <= 1) {
				playerpos.x++;
			}
			player.getDirection().x = 1;
			player.getDirection().y = 0; 
			break;
		case KeyEvent.VK_LEFT:
//			if(playerpos.x > 0 && rooms.get(roomno).getMap()[playerpos.y][playerpos.x-1] != 1 && rooms.get(roomno).getMap()[playerpos.y][playerpos.x-1] != 3) {
//				playerpos.x--;
//			}
			if(playerpos.x > 0 && rooms.get(roomno).getMap()[playerpos.y][playerpos.x-1] <= 1) {
				playerpos.x--;
			}
			player.getDirection().x = -1;
			player.getDirection().y = 0; 
			break;
		case KeyEvent.VK_C:
			currentMenu = "menu";
			displayMenu();
			displaySideMenu();
			menu[0].setVisible(true);
			menu[1].setVisible(true);
			return;
		case KeyEvent.VK_Z:
			int yy = playerpos.y + player.getDirection().y, xx = playerpos.x + player.getDirection().x;
			if(yy > 6 || xx > 6 || xx < 0 || y < 0) break;
			switch(rooms.get(roomno).getMap()[yy][xx]) {
			case 3:
				getLoot(yy,xx);
				break;
			case 4:
				displayDialog(yy,xx);
				break;
			case 5:
				savemenu.setSelection(0);
				displaySaveMenu();
			}
			break;
		case KeyEvent.VK_S:
			player.getStats().setEncounterrate(200);
			break;
		}
		if(playerpos.x != x || playerpos.y != y) {
			player.getStats().setEncounterrate(player.getStats().getEncounterrate()+rooms.get(roomno).getEncounterRate());

			test.setText(String.valueOf(player.getStats().getEncounterrate()));
			if(player.getStats().getEncounterrate() >= 100) {
				enterbattle(monsters[r.nextInt(monsters.length-1)]);
			}
		}
		if(rooms.get(roomno).getMap()[playerpos.y][playerpos.x] == 1) {
			roomno++;
			player.getStats().setEncounterrate(0);
			playerpos = new Point(3,6);
			if(roomno == rooms.size()-1) {
				initiatebossfight();
			}
		}
		loadoverworld();
	}
	private void displayDialog(int y, int x) {
		for(String str : rooms.get(roomno).getDialog(y,x)) {
			printDialog(str);
		}
	}
	private void getLoot(int y, int x) {
		ArrayList<Item> loot = rooms.get(roomno).getLoot(y,x);
		if(player.getItemCount() + loot.size() > 8) {
			printDialog("Your inventory is full.");
			return;
		}
		for(int i = 0; i<loot.size(); i++) {
			player.getInventory()[player.setItemCount(player.getItemCount()+1)] = loot.get(i);
			
			printDialog("You got the " + loot.get(i).getName() + ".");
		}
		rooms.get(roomno).getMap()[playerpos.y+player.getDirection().y][playerpos.x+player.getDirection().x] = 0;
	}
	private void initiatebossfight() {
		printDialog("bossfight incoming.");
		TimerClass t = new TimerClass(45, e -> {
			if(currentTimerClass != (TimerClass)e.getSource()) return;
			else if(isPaused) return;
			enterbattle(monsters[monsters.length-1]);
			dialogbox.setVisible(false);
			currentMenu = lastMenu.equals("itemmenu") ? (lastMenu="items") : lastMenu;
			((TimerClass)e.getSource()).stop();
		});
		TimerClassQueue.add(t);
		t.start();
	}
	public void endgame(boolean gameover) {
		currentTimerClass = null;
		isPaused = false;
		currentTimerClass = TimerClassQueue.poll();
		if(!gameover) {
			this.printDialog("you won.\ngg");
		}
		else {
			dialogbox.setFont(new Font(dialogbox.getFont().getName(),Font.BOLD,90));
			this.printDialog("you lost.");
		}
		removeKeyListener(this);
	}
	private void displayMenu() {
		String[] a = {"STATS","ITEMS"};
		StringBuilder str = new StringBuilder("<html>");
		for(int i = 0; i<2; i++) {
			if(i == menubutton && currentMenu.equals("menu")) {
				str.append("<p>•&nbsp;" + a[i] +"</p>");
			}
			else str.append("<p>&nbsp;&nbsp;" + a[i] + "</p>");
		}
		str.append("</html>");
		menu[0].setText(str.toString());
	}
	private void displaySideMenu() {
		StringBuilder str = new StringBuilder("<html>");
		if(menubutton == 0) {
			int weaponatk=0,armordef=0;
			String weaponname="",armorname="";
			if(player.getWeapon() != null) {
				weaponatk = player.getWeapon().getPotency();
				weaponname = player.getWeapon().getName();
			}
			if(player.getArmor() != null) {
				armordef = player.getArmor().getPotency();
				armorname = player.getArmor().getName();
			}
			str.append(player.getName() + "<br/><br/>LVL " + player.getStats().getLevel() + "<br/>HP " + player.getStats().getHealth() + "<br/><br/>ATK " + player.getStats().getAttack() + " (" + weaponatk + ")" + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;EXP: " + player.getStats().getXp() + "<br/>DEF " + player.getStats().getDefense() + " (" + armordef + ")" + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;NEXT: " + player.getStats().getNextlevel() + "<br/><br/><br/>" + "WEAPON: " + weaponname + "<br/>" + "ARMOR: " + armorname);
			menu[1].setText(str.toString());
		}
		else {
			printItemMenu();
		}
	}
	private void printItemMenu() {
		StringBuilder str = new StringBuilder("<html>");
		String[] a = {"DROP", "INFO", "USE"};
		for(int i = 0; i<player.getItemCount(); i++) {
			if(i == item && currentMenu.equals("items")) {
				str.append("<p>•   " + player.getInventory()[i].getName() +"</p>");
			}
			else str.append("<p>&nbsp;&nbsp;" + player.getInventory()[i].getName()+"</p>");
		}
		for(int i = 0; i<a.length; i++) {
			if(currentMenu.equals("itemmenu")) str.append((i == itemmenu ? "• " : "") + a[i] + "  ");
			else str.append(a[i] + "  ");
		}
		str.append("</html>");
		menu[1].setText(str.toString());
	}
	private void displaySaveMenu() {
		savemenu.setVisible(true);
		lastMenu = currentMenu;
		currentMenu = "savemenu";
		savemenu.setDialog(saveplayername, saveroomno);
	}
	public void printDialog(String dialog) {
		dialogbox.setVisible(true);
		flag=true;
		printinprogress = true;
		lastMenu = currentMenu;
		
		StringBuilder str = new StringBuilder();
		if(dialog.contains("\n")) {
			//str.append("<html>");
			str.append(dialog.replace("\n", "<br/>"));
			//str.append("</html>");
		}
		else str.append(dialog);
		messageQueue.add(str.toString());
		print(str.toString());
	}
	private void print(String msg) {
		int[] currentIndex = {0};
		//String dialog = messageQueue.poll();
		String dialog = msg;
		if(dialog == null) return;
    	TimerClass TimerClass = new TimerClass(45, e -> {
    		if(currentTimerClass != (TimerClass)e.getSource()) return;
    		else if(isPaused) return;
    		flag = true;
    		currentMenu = "dialog";
    		String lmao = "";
			if (currentIndex[0] < dialog.length()) {
				//if(dialog.charAt(currentIndex[0]) == '\\'
				lmao = dialogbox.getText().replace("<html>", "").replace("</html>", "").replace("<body>", "").replace("</body>", "").replace("<div>", "").replace("</div>", "");
		        //dialogbox.setText(lmao + dialogbox.getText() + dialog.charAt(currentIndex[0]));
				dialogbox.setText("<html><body><div>" + lmao + dialog.charAt(currentIndex[0]) + "</div></body></html>");
		        currentIndex[0]++;
		    } 
			else {
		        ((TimerClass) e.getSource()).stop();
		        isPaused = !isPaused;
		        flag = false;
//		    	if(hasNextMessage) isPaused = true; 
//		        else if(!hasNextMessage) hasNextMessage = true;
		        
		        currentTimerClass=TimerClassQueue.poll();
		        if(TimerClassQueue.isEmpty()) {
			        printinprogress = false;
		        }
		        currentTimerClass=TimerClassQueue.peek();
		        messageQueue.poll();
		    }
        });
    	TimerClassQueue.add(TimerClass);
    	currentTimerClass = TimerClassQueue.peek();
    	TimerClass.start();
		dialogbox.setText("");
		//while(TimerClass.isRunning()) {}
	}
	public Point getPlayerpos() {
		return playerpos;
	}
	public JPanel getPanel() {
		return panel;
	}
	public GamePanel getMainMap() {
		return mainmap;
	}
	public JLabel getDialogbox() {
		return dialogbox;
	}
	public ArrayList<Room> getRooms() {
		return rooms;
	}
	public int getRoomno() {
		return roomno;
	}
	public PlayerData getPlayer() {
		return player;
	}
	public BattleMenu getBattleMenu() {
		return battlemenu;
	}
}

class GamePanel extends JPanel implements Serializable{
	private transient mainclass main;
	public GamePanel(mainclass main) {
		this.main = main;
		SpringLayout layout = new SpringLayout();
		setLayout(layout);
	}
	public void paintComponent(Graphics graphic) {
		super.paintComponent(graphic);
		int[][] map = main.getRooms().get(main.getRoomno()).getMap();
		graphic.setColor(Color.BLACK);
		graphic.fillRect(0,0,getWidth(),getHeight());
		for(int i=0,p=0; i<7; i++,p+=84) {
			p+=10;
			for(int j = 0,k=0; j<getWidth(); j+=91,k++) {
				j+=10;
				switch(map[i][k]) {
				case 0:
					graphic.setColor(Color.WHITE);
					break;
				case 2:
					graphic.setColor(Color.GRAY);
					break;
				case 1:
					graphic.setColor(new Color(150,120,120));
					break;
				case 3:
					graphic.setColor(new Color(183,117,57));
					break;
				case 4:
					graphic.setColor(new Color(102,251,56));
					break;
				case 5:
					graphic.setColor(Color.YELLOW);
					break;
				}
				if(i == main.getPlayerpos().y && k == main.getPlayerpos().x) graphic.setColor(Color.RED);
				graphic.fillOval(j,p,84,84);
			}
		}
	}
}

class Menu extends JLabel implements Serializable{
	public Menu() {
		setVisible(false);
		setFont(new Font(getFont().getName(),0,20));
		setOpaque(true);
		setBorder(new CompoundBorder(new LineBorder(Color.BLACK, 4), new EmptyBorder(10, 10, 10, 10)));
	}
}

class StartingScreen extends JPanel {
	private SaveMenu savescreen;
	private BufferedImage img;
	public StartingScreen() {
		savescreen = new SaveMenu();
		try {
			img = ImageIO.read(new File("startscreen.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		savescreen.setVisible(true);
		savescreen.setSelections(new String[] {"Continue","Reset"});
		savescreen.setTitle("Save File");
		setPreferredSize(new Dimension(700,780));
		savescreen.setPreferredSize(new Dimension(700,780));
		setLayout(null);
		int x = (getPreferredSize().width - savescreen.getPreferredSize().width) / 2;
        int y = (getPreferredSize().height - savescreen.getPreferredSize().height) / 2;
		savescreen.setBounds(x,y,savescreen.getPreferredSize().width,savescreen.getPreferredSize().height);
		add(savescreen);
		savescreen.setBorder(new CompoundBorder(new LineBorder(Color.WHITE, 4), new EmptyBorder(10, 10, 10, 10)));
	}
	public SaveMenu getSaveScreen() {
		return savescreen;
	}
	public void setSaveScreen(SaveMenu a) {
		savescreen = a;
	}
	public void paintComponent(Graphics graphic) {
		super.paintComponent(graphic);
		graphic.drawImage(img,0,0,this);
	}
}

class SaveMenu extends Menu implements Serializable{
	private int selection = 0;
	private String[] selections = {"YES", "CLOSE"};
	private String title="SAVE";
	public SaveMenu() {
		super();
		setBackground(Color.BLACK);
		setFont(new Font(getFont().getName(),0,35));
		setForeground(Color.WHITE);
		setBorder(new CompoundBorder(new LineBorder(Color.WHITE, 4), new EmptyBorder(10, 10, 10, 10)));
		setPreferredSize(new Dimension(500,225));
		setHorizontalAlignment(JLabel.CENTER);
	}
	public void setDialog(String playername, int roomno) {
		String dialog = 
				"<html><body style='text-align: center;'><p><font color='#FFD700'>%s</font></p><p>%s %4s</p>";
		for(int i = 0; i<getSelections().length; i++) {
			if(i == getSelection()) {
				dialog += "•<font color='#FFD700'>%2s</font>";
			}
			else {
				dialog += "%2s";
			}
			dialog += "&nbsp;&nbsp;";
		}
		dialog += "</body></html>";
		setText(String.format(dialog, title, playername, "room " + roomno,getSelections()[0],getSelections()[1]));
	}
	public String[] getSelections() {
		return selections;
	}
	public void setSelections(String[] a) {
		selections = a;
	}
	public int getSelection() {
		return selection;
	}
	public void setSelection(int a) {
		selection = a;
	}
	public void setTitle(String t) {
		title = t;
	}
}

