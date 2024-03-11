package 용사키우기2;

import java.io.Serializable;
import java.util.ArrayList;

public class Item implements Serializable{
	private String name;
	private String type;
	private int potency;
	public Item(String n, String t, int p) {
		name = n;
		type = t;
		potency = p;
	}
	public Item(String n, String t) {
		name = n;
		type = t;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type.toLowerCase();
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getPotency() {
		return potency;
	}
	public void setPotency(int potency) {
		this.potency = potency;
	}
	public String getInfo() {
		String str = "\"" + name + "\"" + " - ";
		switch(type.toLowerCase()) {
		case "heal":
			str += "heals health by ";
			break;
		case "atkup":
			str += "increases attack by ";
			break;
		case "defup":
			str += "increases defense by ";
			break;
		case "allstatsup":
			str += "increases all your stats by ";
			break;
		default:
			str += type;
			break;
		}
		if(potency == 0) return str;
		str += potency + ".";
		return str;
	}
}

class Equipment extends Item implements Serializable {
	private int effect;
	public Equipment(String n, String t, int p, int e) {
		super(n, t, p);
		effect = e;
	}
	public Equipment(String n, String t, int p) {
		super(n, t, p);
	}
	@Override
	public String getInfo() {
		String str = "\"" + getName() + "\"" + " - ";
		switch(getType()) {
		case "weapon":
			str += getPotency() + " ATK";
			break;
		case "armor":
			str += getPotency() + " DEF";
			break;
		}
		return str;
	}
	public int getEffect() {
		return effect;
	}
}

class LootUtilities implements Serializable{
	private static ArrayList<Item> lootpool = new ArrayList<>();
	public LootUtilities() {
		lootpool.add(new Item("Lesser Health Potion","heal",10));
		lootpool.add(new Item("Health Potion","heal",20));
		lootpool.add(new Item("Medium Health Potion","heal",50));
		lootpool.add(new Item("Large Health Potion","heal",100));
		lootpool.add(new Item("Strength Potion","atkup",30));
		lootpool.add(new Item("Strength II Potion","atkup",50));
		lootpool.add(new Item("Resistance Potion","defup",30));
		lootpool.add(new Item("Resistance II Potion","defup",50));
		lootpool.add(new Item("potion","allstatsup",200));
		lootpool.add(new Item("potion","allstatsup",2));
		lootpool.add(new Item("potion","allstatsup",500));
		lootpool.add(new Item("grimace shake","this doesnt actually do anything lmao"));
	}
	public static ArrayList<Item> getLootPool() {
		return lootpool;
	}
	
}