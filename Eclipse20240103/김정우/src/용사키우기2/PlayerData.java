package 용사키우기2;

import java.util.Random;
import java.awt.*;
import java.io.Serializable;

public class PlayerData implements Serializable{
	private Stats stats = new Stats();
	private String name = "placeholdername";
	private Item[] Inventory = new Item[8];
	private int itemCount=0;
	private Random r = new Random();
	private Equipment EquippedArmor, EquippedWeapon;
	private Point direction = new Point(0,0);
	public Stats getStats() {
		return stats;
	}
	public void setStats(Stats s) {
		stats = s;
	}
	public int Fight(Monster enemy) {
		int damage=0;
		damage = (int) (stats.getAttack()-enemy.getDefense()*0.7);
		damage += damage / (r.nextInt(5) + 1);
		if(damage < 0) damage = 0;
		damage = weaponEffect(damage,enemy.isboss());
		
		enemy.setHealth(enemy.getHealth()-damage);
		return damage;
	}
	public Point getDirection() {
		return direction;
	}
	public boolean Defend() {
		int defboost = stats.getDefense()/5;
		if(stats.getDefense()+defboost >= (stats.getBaseDefense()*2*stats.getLevel())) return false;
		stats.setDefense(stats.getDefense() + defboost);
		return true;
	}
	public boolean Think() {
		int atkboost = stats.getAttack()/5;
		if(stats.getAttack()+atkboost >= (stats.getBaseAttack()*2*stats.getLevel())) return false;
		stats.setAttack(stats.getAttack() + atkboost);
		return true;
	}
	public void checkLevel(int xp) {
		stats.setXp(stats.getXp()+xp);
		while(stats.getXp() >= stats.getNextlevel()) {
			stats.setXp(stats.getXp()-stats.getNextlevel());
			stats.setLevel(stats.getLevel()+1);
			stats.setNextlevel((stats.getLevel()+1)*(stats.getLevel()*2+5));
		}
		stats.setBaseAttack(stats.getDefaultAttack()*stats.getLevel());
		stats.setBaseDefense(stats.getDefaultDefense()*stats.getLevel());
		stats.setBaseHealth(stats.getDefaultHealth()*stats.getLevel());
	}
	public void resetStats() {
		stats.setBaseAttack(stats.getDefaultAttack()*stats.getLevel());
		stats.setBaseDefense(stats.getDefaultDefense()*stats.getLevel());
		stats.setBaseHealth(stats.getDefaultHealth()*stats.getLevel());
		stats.setAttackBuff(0);
		stats.setDefenseBuff(0);
		stats.setHealth(stats.getBaseHealth());
		stats.setAttack(stats.getBaseAttack()+(EquippedWeapon != null ? EquippedWeapon.getPotency() : 0));
		stats.setDefense(stats.getBaseDefense()+(EquippedArmor != null ? EquippedArmor.getPotency() : 0));
	}
	public String UseItem(int idx) {
		Item item = Inventory[idx];
		String str = "";
		int potency;
		switch(item.getType().toLowerCase()) {
		case "heal":
			potency = stats.getHealth()/100*item.getPotency();
			stats.setHealth(((potency + stats.getHealth()) > stats.getHealth() ? stats.getHealth() : (potency + stats.getHealth())));
			str = "You were healed for " + potency;
			break;
		case "atkup":
			potency = stats.getAttack()/100*item.getPotency();
			stats.setAttack(stats.getAttack() + potency);
			str = "Your attack increased by " + potency;
			break;
		case "defup":
			potency = stats.getDefense()/100*item.getPotency();
			stats.setDefense(stats.getDefense() + potency);
			str = "Your defense increased by " + potency;
			break;
		case "allstatsup":
			potency = stats.getAttack()/100*item.getPotency();
			potency = stats.getDefense()/100*item.getPotency();
			stats.setAttack(stats.getAttack() + potency);
			stats.setDefense(stats.getDefense() + potency);
			str = "All your stats were increased by " + item.getPotency() + "%";
			break;
		default:
			str = item.getType();
		}
		removeItem(idx);
		return str;
	}
	public void removeItem(int idx) {
		Inventory[idx] = null;
		for(int i = idx; i<Inventory.length-1; i++) {
			Inventory[i] = Inventory[i+1];
		}
		itemCount--;
	}
	public int armorEffect(int dmg) {
		if(EquippedArmor == null) return dmg;
		switch(EquippedArmor.getEffect())  {
		case 3:
			return (int)(dmg*0.01);
		default:
			return dmg;
		}
	}
	public void armorEffect() {
		if(EquippedArmor == null) return;
		switch(EquippedArmor.getEffect())  {
		case 3:
			int heal = stats.getBaseHealth()/3+stats.getHealth();
			stats.setHealth(heal > stats.getBaseHealth() ? stats.getBaseHealth() : heal);
			int atkbuff = stats.getAttackBuff()+10, defbuff = stats.getDefenseBuff()+10;
			stats.setAttackBuff(atkbuff > 100 ? 100 : atkbuff);
			stats.setDefenseBuff(defbuff > 100 ? 100 : defbuff);
		}
	}
	public int weaponEffect(int dmg, boolean isboss) {
		if(EquippedWeapon == null) return dmg;
		switch(EquippedWeapon.getEffect()) {
		case 1:
			return dmg+dmg/4;
		case 2:
			return dmg*2;
		case 3:
			dmg = stats.getAttack();
			dmg += dmg / (r.nextInt(5) + 1);
			if(isboss) {
				return dmg + (int) (dmg * 2.5);
			}
			return dmg;
		default:
			return dmg;
		}
	}
	public Item[] getInventory() {
		return Inventory;
	}
	public int getItemCount() {
		return itemCount;
	}
	public int setItemCount(int itemCount) {
		this.itemCount = itemCount;
		return itemCount-1;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Item getWeapon() {
		return EquippedWeapon;
	}
	public Item getArmor() {
		return EquippedArmor;
	}
	private void equipWeapon(Item weapon) {
		EquippedWeapon = (Equipment)weapon;
		stats.setAttack(stats.getBaseAttack() + EquippedWeapon.getPotency());
	}
	private void equipArmor(Item armor) {
		EquippedArmor = (Equipment)armor;
		stats.setDefense(stats.getBaseDefense() + EquippedArmor.getPotency());
	}
	public Equipment getEquipment(String type) {
		if(type.equals("weapon")) {
			return EquippedWeapon;
		}
		else {
			return EquippedArmor;
		}
		//return type.equals("weapon") ? EquippedWeapon : EquippedArmor;
	}
	public void Equip(String type, Equipment equipment) {
		if(type.equals("weapon" )) {
			equipWeapon(equipment);
		}
		else {
			equipArmor(equipment);
		}
	}
}
