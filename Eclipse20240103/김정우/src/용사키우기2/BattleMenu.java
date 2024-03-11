package 용사키우기2;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import java.awt.event.*;
import java.awt.*;
import java.awt.image.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import java.awt.event.*;
import java.awt.*;
import java.awt.image.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.CountDownLatch;

public class BattleMenu extends JPanel implements Serializable {
	private final int screenWidth=700,screenHeight = 700;
	private transient mainclass main;
	private Monster monster;
	private JLabel dialogbox = new JLabel(""),info = new JLabel("");
	private MenuButton[] buttons = new MenuButton[5];
	private int selectedbutton = 0, selecteditem = 0;
	private boolean printflag=false;
	private boolean isPaused=false;
	private boolean printinprogress = false;
	private boolean hasNextMessage = false;
	private Queue<String> messageQueue = new LinkedList<>();
	private Queue<TimerClass> timerQueue = new LinkedList<>();
	private TimerClass currentTimer;
	private boolean itemmenu = false,gameover;
	private String currentMenu = "main";
	private String lastestdialog;
	private int lasthealth=0;
	private SpringLayout layout = new SpringLayout();
	private Random r = new Random();
	public BattleMenu(mainclass main) {
		this.main = main;
		setBackground(Color.BLACK);
		dialogbox.setBorder(new CompoundBorder(new LineBorder(Color.WHITE, 4), new EmptyBorder(10, 10, 10, 100)));
		dialogbox.setForeground(Color.WHITE);
		dialogbox.setPreferredSize(new Dimension(600,200));
		info.setVerticalAlignment(SwingConstants.TOP);
		info.setFont(new Font("Courier New",10, 15));
		info.setForeground(Color.WHITE);
		info.setPreferredSize(new Dimension(600,50));
		
		setLayout(layout);
		add(dialogbox);
		add(info);
		layout.putConstraint(SpringLayout.NORTH,dialogbox,350,SpringLayout.NORTH,this);
		layout.putConstraint(SpringLayout.WEST,dialogbox,50,SpringLayout.WEST,this);
		layout.putConstraint(SpringLayout.EAST,dialogbox,-50,SpringLayout.EAST,this);
		layout.putConstraint(SpringLayout.NORTH,info,620,SpringLayout.NORTH,this);
		layout.putConstraint(SpringLayout.WEST,info,50,SpringLayout.WEST,this);
		buttons[0] = new MenuButton("FIGHT");
		buttons[1] = new MenuButton("DEFEND");
		buttons[2] = new MenuButton("THINK");
		buttons[3] = new MenuButton("ITEM");
		buttons[4] = new MenuButton("FLEE");
		for(int i = 0; i<5; i++) {
			add(buttons[i]);
			buttons[i].setPreferredSize(new Dimension(112,50));
			buttons[i].setFunction(i);
			layout.putConstraint(SpringLayout.NORTH,buttons[i],565,SpringLayout.NORTH,this);
			layout.putConstraint(SpringLayout.WEST,buttons[i],50+(i*112+i*8),SpringLayout.WEST,this);
			//layout.putConstraint(SpringLayout.EAST,buttons[i],700-(50+(i*112)),SpringLayout.EAST,this);
		}

		buttons[selectedbutton].setText(buttons[selectedbutton].getName() + " 🔴");
	}
	private void DisplayPlayerInfo() {
		info.setText("<html>" + main.getPlayer().getName() + "&nbsp;" + "LVL " + main.getPlayer().getStats().getLevel() + "&nbsp;" + main.getPlayer().getStats().getHealth() + "/" + main.getPlayer().getStats().getBaseHealth() + "&nbsp;" + "</html>");
	}
	public void InitializeEncounter(Monster enemy) {
		for(int i = 0; i<buttons.length; i++) buttons[i].setText(buttons[i].getName());
		selectedbutton = 0;
		buttons[selectedbutton].setText(buttons[selectedbutton].getName() + " 🔴");
		monster = enemy;
		monster.resetStats();
		main.getPlayer().resetStats();
		DisplayPlayerInfo();
		hasNextMessage = false;
		if(monster.isboss()) { 
			hasNextMessage = true; 
			buttons[4].setName("CHECK"); 
			buttons[4].setFunction(5);
			for(int i = 0; i<buttons.length; i++) buttons[i].setText(buttons[i].getName());
			buttons[selectedbutton].setText(buttons[selectedbutton].getName() + " 🔴");
			printDialog("bossfight."); 
			bossfight(7); 
		}
		else printDialog("You encountered " + enemy.getName() + (enemy.isFlag() ? " again.." : "!"));
		main.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				switch(currentMenu) {
				case "main":
					menucontrol(e.getKeyCode());
					break;
				case "checkmenu":
				case "fightmenu":
					submenu(e.getKeyCode());
					break;
				}
			}
		});
	}
	private void submenu(int a) {
		switch(a) {
		case KeyEvent.VK_Z:
			switch(currentMenu) {
			case "fightmenu":
				if(attackenemy()) {
					EndBattle();
					currentMenu = "main";
					return;
				}
				break;
			case "checkmenu":
				printCheckInfo();
				break;
			}
			myturn();
			new Timer(10, e->{
				if(isPaused) {
					enemyturn();
					printflag = false;
					((Timer)e.getSource()).stop();
				}
			}).start();
			//enemyturn();
			currentMenu = "main";
			break;
		case KeyEvent.VK_X:
			currentMenu = "main";
			String msg = lastestdialog;
			if(msg == null) return;
			dialogbox.setText((msg.contains("<br/>") ? ("<html>" + msg + "</html>") : msg));
			break;
		}
	}
	private void menucontrol(int a) {
		switch(a) {
		case KeyEvent.VK_UP:
			if(!itemmenu) return;
			if(selecteditem > 0) {
				selecteditem--;
			}
			printItemMenu();
			break;
		case KeyEvent.VK_DOWN:
			if(!itemmenu) return;
			if(selecteditem < 7) {
				selecteditem++;
			}
			printItemMenu();
			break;
		case KeyEvent.VK_LEFT:
			if(itemmenu) return;
			buttons[selectedbutton].setText(buttons[selectedbutton].getName());
			if(selectedbutton == 0) selectedbutton = 4;
			else {
				selectedbutton--;
			}
			break;
		case KeyEvent.VK_RIGHT:
			if(itemmenu) return;
			buttons[selectedbutton].setText(buttons[selectedbutton].getName());
			if(selectedbutton == 4) selectedbutton = 0;
			else {
				selectedbutton++;
			}
			break;
		case KeyEvent.VK_Z:
			if(itemmenu) {
				itemmenu = !itemmenu;
				if(main.getPlayer().getInventory()[selecteditem] instanceof Equipment) {
					String type = main.getPlayer().getInventory()[selecteditem].getType();
					Item currentequipment = main.getPlayer().getEquipment(type);
					Equipment temp = main.getPlayer().getEquipment(type);
					main.getPlayer().Equip(type, (Equipment)main.getPlayer().getInventory()[selecteditem]);
					printDialog("You equipped the " + main.getPlayer().getInventory()[selecteditem]);
					main.getPlayer().getInventory()[selecteditem] = temp;
					if(currentequipment == null) {
						if(selecteditem > 0 && selecteditem == main.getPlayer().setItemCount(main.getPlayer().getItemCount()-1)) selecteditem--;
						main.getPlayer().removeItem(selecteditem);
					}
					
				}
				else {
					printDialog("You used " + main.getPlayer().getInventory()[selecteditem].getName() + ".\n" + main.getPlayer().UseItem(selecteditem) + ".");
					if(selecteditem > 0 && selecteditem == main.getPlayer().getItemCount()-1) selecteditem--;	
				}
				enemyturn();
				break;
			}
			if(printflag) return;
			else if(isPaused) {
				isPaused = false;
				if(printinprogress) {
					dialogbox.setText("");
				}
				else {
					executeFunction(buttons[selectedbutton].getFunction());
				}
				return;
			}
			executeFunction(buttons[selectedbutton].getFunction());
			break;
		case KeyEvent.VK_X:
			if(itemmenu) { 
				itemmenu = false;  
				String msg = lastestdialog;
				if(msg == null) return;
				dialogbox.setText((msg.contains("<br/>") ? ("<html>" + msg + "</html>") : msg));
				return; 
			}
			if(currentTimer == null) return;
			if(!isPaused) {
				currentTimer.stop();
				String msg = messageQueue.poll();
				if(msg == null) return;
				dialogbox.setText((msg.contains("<br/>") ? ("<html>" + msg + "</html>") : msg));
		        //isPaused = !isPaused;
		        printflag = false;
		    	if(hasNextMessage) isPaused = true; 
		        else if(!hasNextMessage) hasNextMessage = true;
		        
		        currentTimer=timerQueue.poll();
		        if(timerQueue.isEmpty()) {
			        printinprogress = false;
		        }
		        currentTimer=timerQueue.peek();
		        if(currentTimer != null) currentTimer.setFlag();
			}
			return;
		}
		buttons[selectedbutton].setText(buttons[selectedbutton].getName() + " 🔴");
	}
	private void displayHealthBar() {
		int dummy = monster.isboss() ? 300 : 180;
		int height = 30;
		int health = (int) (dummy*(100/(monster.getBasehealth()/(double)monster.getHealth()))/100) < 0 ? 0 : (int) (dummy*(100/(monster.getBasehealth()/(double)monster.getHealth()))/100);
		int[] counter = {lasthealth};
		int width = lasthealth > dummy ? lasthealth : dummy;
		Graphics graphics = getGraphics();
		
		Timer t1 = new Timer(monster.getHealth()-lasthealth <= 0 ? 600 : 1200, e-> {
			repaint();
			printflag = false;
		});
		t1.setRepeats(false);
		Timer t = new Timer(1, e-> {
			printflag = true;
			if(monster.getHealth()-lasthealth <= 0) {
				counter[0]-=(monster.isboss() ? 3 : 1)*6;
			}
			else counter[0]-=monster.isboss() ? 3 : 1;
			if(lasthealth+health <= 0) {
				graphics.setColor(Color.RED);
				graphics.fillRect((getWidth()-width)/2,280,width,height);
				graphics.setColor(Color.GREEN);
				graphics.fillRect((getWidth()-width)/2,280,counter[0],height);
				((Timer)e.getSource()).stop();
				t1.start();
			}
			graphics.setColor(Color.RED);
			graphics.fillRect((getWidth()-width)/2,280,width,height);
			graphics.setColor(Color.GREEN);
			graphics.fillRect((getWidth()-width)/2,280,counter[0],height);
			if(counter[0] <= health || counter[0] <= 0) {
				((Timer)e.getSource()).stop();
				t1.start();
			}
		});
		t.start();
	}
	private boolean attackenemy() {
		lasthealth = (int) (monster.isboss() ? 300*(100/(monster.getBasehealth()/(double)monster.getHealth()))/100 : 180*(100/(monster.getBasehealth()/(double)monster.getHealth()))/100);
		int damage = main.getPlayer().Fight(monster);
		printDialog("You dealt " + damage + " damage to " + monster.getName() + ".");
		displayHealthBar();
		if(monster.getHealth() <= 0) {
			if(monster.isboss()) gameover = false;
			if(main.getPlayer().getStats().getXp()+monster.getXpdrop() < main.getPlayer().getStats().getNextlevel()) printDialog("You won!\nYou gained " + monster.getXpdrop() + " xp!");
			else {
				printDialog("You won!\nYou gained " + monster.getXpdrop() + " xp!\nYou leveled up!");
				
			}
			main.getPlayer().checkLevel(monster.getXpdrop());
			DisplayPlayerInfo();
			
			if(r.nextInt(100)+1 <= monster.getDropRate()) {
				Item loot = monster.getloot();
				if(main.getPlayer().getItemCount() == 8) {
					printDialog("Your INVENTORY was too full to receive item.");
				}
				else {
					main.getPlayer().getInventory()[main.getPlayer().setItemCount(main.getPlayer().getItemCount()+1)] = loot;
					printDialog(loot.getName() + " was added to your INVENTORY.");
				}
			}
			
			return true;
		}
		return false;
	}
	private void printSubMenu() {
		dialogbox.setText("🔴 " + monster.getName() + "   " + monster.getHealth() + "/" + monster.getBasehealth());
	}
	private void printCheckInfo() {
		printDialog(monster.getInfo());
	}
	public void executeFunction(int func) {
		switch(func) {
		case 0:
			currentMenu = "fightmenu";
			printSubMenu();
			return;
		case 1:
			if(main.getPlayer().Defend()) printDialog("You defended!\nDefense rose significantly.");
			else {
				printDialog("Cannot defend more.");
			}
			break;
		case 2:
			if(main.getPlayer().Think()) printDialog("You thought about ways to defeat the enemy.\nAttack increased!");
			else printDialog("Cannot think more.");
			break;
		case 3:
			if(main.getPlayer().getInventory()[0] == null) {
				printDialog("No items to use.");
				break;
			}
			itemmenu = true;
			printItemMenu();
			return;
		case 4:
			printDialog("You fled.");
			EndBattle();
			return;
		case 5:
			currentMenu = "checkmenu";
			printSubMenu();
			return;
		}
		new Timer(10, e->{
			if(isPaused) {
				enemyturn();
				printflag = false;
				((Timer)e.getSource()).stop();
			}
		}).start();
		
	}
	private void myturn() {
		return;
//		myTurn.start();
	}
	private void enemyturn() {
		boolean flag[] = {false,false};
		Timer timer = new Timer(10, e->{
			Timer t = ((Timer)e.getSource());
			if(!flag[0]) {
				if(monster.isboss()) {
					bossfight(monster.skills());
				}
				else printDialog(monster.getName() + " dealt " + monster.attack(main.getPlayer()) + " damage to you.");
				if(printflag) printflag = false;
				flag[0] = true;
				if(main.getPlayer().getStats().getHealth() <= 0) {
					//flag[0] = true;
					flag[1] = true;
					if(monster.isboss()) gameover = true;
					printDialog("You lost!\nlmao");
					printflag = false;
					EndBattle(); 
				}
			}
			if((currentTimer == null && !printinprogress && isPaused) || (currentTimer != null && currentTimer.getFlag())) {
				t.stop();
				DisplayPlayerInfo();
				return;
//				if(main.getPlayer().getStats().getHealth() <= 0 && currentTimer == null) {
//					//flag[0] = true;
//					printDialog("You lost!\nlmao");
//					EndBattle(); 
//				}
			}
		});
		timer.start();
		//DisplayPlayerInfo();
		//boolean flag[] = {false};
//		new Timer(10, e-> {
//			
//			if(currentTimer == null || (!printinprogress && isPaused)) {
//				DisplayPlayerInfo();
//				((Timer)e.getSource()).stop();
//				main.getPlayer().armorEffect();
//				DisplayPlayerInfo();
//			}
//			else if((flag[0] || timerQueue.peek() != null) && isPaused && printinprogress && timerQueue.size() == 2) {
//				DisplayPlayerInfo();
//				((Timer)e.getSource()).stop();
//				main.getPlayer().armorEffect();
//				DisplayPlayerInfo();
//			}
//			else if((flag[0] || timerQueue.peek() != null) && isPaused && printinprogress) {
//				DisplayPlayerInfo();
//				((Timer)e.getSource()).stop();
//				main.getPlayer().armorEffect();
//				DisplayPlayerInfo();
//			}
//		}).start();
	}
	private void EndBattle() {
		TimerClass timer = new TimerClass(100, e -> {
			if(currentTimer != (Timer)e.getSource()) return;
			else if(isPaused) return;
			selecteditem = 0;
			selectedbutton = 0;
			printflag=false;
			isPaused=false;
			printinprogress = false;
			lasthealth = 0;
			messageQueue = new LinkedList<>();
			timerQueue = new LinkedList<>();
			currentMenu = "main";
			for(KeyListener listen : main.getKeyListeners()) {
				main.removeKeyListener(listen);
			}
			main.getPlayer().getStats().setEncounterrate(0);
			main.addKeyListener(main);
			((CardLayout)main.getPanel().getLayout()).previous(main.getPanel());
			if(monster.isboss()) {
				main.endgame(gameover);
			}
			((Timer)e.getSource()).stop();
		});
		timerQueue.add(timer);
		timer.start();
	}
	public void printDialog(String dialog) {
		printflag=true;
		printinprogress = true;
		StringBuilder str = new StringBuilder();
		if(dialog.contains("\n")) {
			str.append(dialog.replace("\n", "<br/>"));
		}
		else str.append(dialog);
		messageQueue.add(str.toString());
		print(str.toString());
	}
	private void print(String msg) {
		int[] currentIndex = {0};
		String dialog = msg;
		if(dialog == null) return;
    	TimerClass timer = new TimerClass(45, e -> {
    		if(currentTimer != (Timer)e.getSource()) return;
    		else if(isPaused) return;
    		printflag = true;
    		String lmao = "";
			if (currentIndex[0] < dialog.length()) {
				
				lmao = dialogbox.getText().replace("<html>", "").replace("</html>", "");
				dialogbox.setText("<html>" + lmao + dialog.charAt(currentIndex[0]) + "</html>");
		        currentIndex[0]++;
		    } 
			else {
	        	((Timer) e.getSource()).stop();
	        	if(hasNextMessage) isPaused = true; 
		        else if(!hasNextMessage) hasNextMessage = true;
	        	printflag = false;
		        currentTimer=timerQueue.poll();
		        if(timerQueue.isEmpty()) {
			        printinprogress = false;
		        }
		        currentTimer=timerQueue.peek();
		        lastestdialog = messageQueue.peek();
		        messageQueue.poll();
		        if(currentTimer != null) currentTimer.setFlag();
		        //isPaused = !isPaused;
		        
		    }
        });
    	timerQueue.add(timer);
    	currentTimer = timerQueue.peek();
    	timer.start();
		if(!isPaused) dialogbox.setText("");
		//while(timer.isRunning()) {}
	}
	private void printItemMenu() {
		String str = "<html>";
		for(int i = 0; i<main.getPlayer().getItemCount(); i++) {
			if(i == selecteditem) {
				str += "<p>•&nbsp;" + main.getPlayer().getInventory()[i].getName() +"</p>";
			}
			else str += "<p>&nbsp;&nbsp;" + main.getPlayer().getInventory()[i].getName()+"</p>";
		}
		str += "</html>";
		dialogbox.setText(str);
	}
	private void bossfight(int r) {
		switch(r) {
		case 0:
			printDialog(monster.getName() + " used skill1");
			monster.setAttack(monster.getAttack()+monster.getAttack()/99*100);
			printDialog(monster.getName() + " dealt " + monster.attack(main.getPlayer()) + " damage to you.\n" + monster.getName() + " dealt " + monster.attack(main.getPlayer()) + " damage to you.");
			break;
		case 1:
			printDialog(monster.getName() + " used skill2");
			monster.setHealth(monster.getBasehealth()/2+monster.getHealth());
			break;
		case 2:
			printDialog(monster.getName() + " used skill3");
			main.getPlayer().getStats().setBaseHealth(main.getPlayer().getStats().getBaseAttack()/33*100);
			main.getPlayer().getStats().setHealth(main.getPlayer().getStats().getHealth() > main.getPlayer().getStats().getBaseHealth() ? main.getPlayer().getStats().getBaseHealth() : main.getPlayer().getStats().getHealth());
			break;
		case 3:
			printDialog(monster.getName() + " used skill4");
			monster.setAttack(monster.getAttack() + monster.getAttack()/99*100);
			monster.setDefense(monster.getAttack() + monster.getAttack()/99*100);
			break;
		case 4:
			printDialog(monster.getName() + " used skill5");
			main.getPlayer().getStats().setHealth(main.getPlayer().getStats().getHealth()/2);
			break;
		case 5:
			printDialog(monster.getName() + " used skill6");
			main.getPlayer().getStats().setHealth(1);
			enemyturn();
			break;
		case 6:
			printDialog(monster.getName() + " used skill7");
			main.getPlayer().getStats().setDefense(0);
			printDialog(monster.getName() + " dealt " + monster.attack(main.getPlayer()) + " damage to you.");
			break;
		case 7:
			printDialog(monster.getName() + " used skill8");
			monster.setHealth(main.getPlayer().getStats().getBaseHealth() > monster.getHealth() ? main.getPlayer().getStats().getBaseHealth()*2 : monster.getBasehealth());
			monster.setBaseHealth(monster.getHealth());
			monster.setAttack(main.getPlayer().getStats().getBaseAttack() > monster.getAttack() ? main.getPlayer().getStats().getBaseAttack()*2 : monster.getAttack()*3);
			monster.setDefense(main.getPlayer().getStats().getBaseDefense() > monster.getDefense() ? main.getPlayer().getStats().getBaseDefense()*2 : monster.getDefense()*3);
			break;
		}
	}
}



class MenuButton extends JLabel implements Serializable{
	private int function;
	private String text;
	public MenuButton() {
		setForeground(Color.WHITE);
		setBorder(new CompoundBorder(new LineBorder(Color.WHITE, 4), new EmptyBorder(10, 10, 10, 10)));
	}
	public MenuButton(String text) {
		this.text = text;
		setForeground(Color.WHITE);
		setBorder(new CompoundBorder(new LineBorder(Color.WHITE, 4), new EmptyBorder(10, 10, 10, 10)));
		setText(this.text);
	}
	public void setFunction(int a) { 
		function = a;
	}
	public int getFunction() {
		return function;
	}
	public String getName() {
		return text;
	}
	public void setName(String n) {
		text = n;
	}
}

