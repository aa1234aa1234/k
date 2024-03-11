package 용사키우기2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

import java.awt.*;

public class Room implements Serializable{
	private int[][] map = new int[7][7];
	private boolean hasloot = false,hassave = false;
	private int encounterincreaserate=0;
	private Random r = new Random();
	private ArrayList<Item> loot = new ArrayList<>();
	private String[] dialog;
	private HashMap<Point,EventObject> objects = new HashMap<>();
	public Room(int er) {
		encounterincreaserate = er;
		generateRoom();
	}
	public Room(int er,String[] setting) {
		encounterincreaserate = er;
		for(String a : setting) {
			switch(a) {
			case "hasloot":
				hasloot = true;
				break;
			case "hassave":
				hassave = true;
				break;
			default:
				String[] dialog = a.split("\\|");
				this.dialog = dialog;
			}
		}
		generateRoom();
	}
	
	public Room(int er,ArrayList<String> setting) {
		encounterincreaserate = er;
		generateRoom();
		for(String a : setting) {
			String[] msg = a.split("\\|");
			switch(msg[0]) {
			case "hasloot":
				hasloot = true;
				int y,x;
				if(msg.length < 2) {
					y = r.nextInt(6)+1;
					x = r.nextInt(7);
					while(map[y][x] != 0) {
						y = r.nextInt(6)+1;
						x = r.nextInt(7);
					}
					map[y][x] = 3;
				}
				else {
					y = Integer.valueOf(msg[1]);
					x = Integer.valueOf(msg[2]);
					map[y][x] = 3;
				}
				objects.put(new Point(x,y),new EventObject() {{}});
				break;
			case "hassave":
				y = r.nextInt(6)+1;
				x = r.nextInt(7);
				while(map[y][x] != 0) {
					y = r.nextInt(6)+1;
					x = r.nextInt(7);
				}
				map[y][x] = 5;
				hassave = true;
				break;
			default:
				if(msg.length < 2) {
					y = r.nextInt(6)+1;
					x = r.nextInt(7);
					while(map[y][x] != 0) {
						y = r.nextInt(6)+1;
						x = r.nextInt(7);
					}
				}
				else {
					y = Integer.valueOf(msg[1]);
					x = Integer.valueOf(msg[2]);
				}
				map[y][x] = 4;
				String[] dialog = msg[0].split("\\`");
				objects.put(new Point(x,y),new EventObject() {{setDialog(dialog);}});
			}
		}
		
	}
	
	private void generateRoom() {
		for(int i = 0; i<7; i++) {
			Arrays.fill(map[i], 0);
		}
		for(int i = 0; i<5; i++) {
			map[r.nextInt(7)][r.nextInt(7)] = 2;
		}
		map[0][3] = 1;
//		if(hasloot) {
//			map[r.nextInt(6)+1][r.nextInt(7)] = 3;
//		}
//		if(dialog != null) {
//			
//		}
//		if(hassave) {
//			map[0][1] = 5;
//		}
	}
	public int[][] getMap() {
		return map;
	}
	public int getEncounterRate() {
		return encounterincreaserate;
	}
	public ArrayList<Item> getLoot(int y, int x) {
		return objects.get(new Point(x,y)).getLoot();
	}
	public String[] getDialog(int y, int x) {
		return objects.get(new Point(x,y)).getDialog();
	}
}

class EventObject implements Serializable {
	private ArrayList<Item> loot = new ArrayList<>();
	private String[] dialog;
	public void setDialog(String[] dialog) { this.dialog = dialog; }
	public ArrayList<Item> getLoot() { return loot; }
	public String[] getDialog() { return dialog; }
}
