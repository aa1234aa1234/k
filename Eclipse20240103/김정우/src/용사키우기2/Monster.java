package 용사키우기2;

import java.io.Serializable;
import java.util.Random;

public class Monster implements Serializable{
	private String name;
	private int health,basehealth;
	private int attack;
	private int defense;
	private int xpdrop;
	private boolean flag=false;
	private int droprate;
	private Random r= new Random();
	public Monster(String n, int h, int a, int def, int xp) {
		name = n;
		basehealth = h;
		attack = a;
		defense = def;
		xpdrop = xp;
		droprate = r.nextInt(100)+1;
	}
	public Item getloot() {
		return LootUtilities.getLootPool().get(r.nextInt(LootUtilities.getLootPool().size()-1));
	}
	public int getDropRate() {
		return droprate;
	}
	public void resetStats() {
		health = basehealth;
	}
	public int getHealth() {
		return health;
	}
	public void setHealth(int health) {
		this.health = health;
	}
	public int getDefense() {
		return defense;
	}
	public void setDefense(int defense) {
		this.defense = defense;
	}
	public int getXpdrop() {
		return xpdrop;
	}
	public void setXpdrop(int xpdrop) {
		this.xpdrop = xpdrop;
	}
	public int getAttack() {
		return attack;
	}
	public void setAttack(int attack) {
		this.attack = attack;
	}
	public boolean isFlag() {
		return flag;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	public String getName() {
		return name;
	}
	public int attack(PlayerData player) {
		int damage=0;
		damage = (int) (attack-player.getStats().getDefense()*0.7);
		damage += damage / (r.nextInt(5) + 1);
		if(damage < 0) damage = 0;
		damage = player.armorEffect(damage);
		
		player.getStats().setHealth(player.getStats().getHealth()-damage);
		return damage;
	}
	public int skills() {
		return 0;
	}
	public boolean isboss() {
		return false;
	}
	public int getBasehealth() {
		return basehealth;
	}
	public void setBaseHealth(int a) {
		basehealth = a;
	}
	public String getInfo() {
		return "";
	}
}

class Boss extends Monster implements Serializable{
	private Random r = new Random();
	public Boss(String n, int h, int a, int def, int xp) {
		super(n, h, a, def, xp);
	}
	@Override
	public int skills() {
		return r.nextInt(7);
	}
	@Override
	public boolean isboss() {
		return true;
	}
	@Override
	public String getInfo() {
		String str = "\"" + getName() + "\"";
		str += " - " + getAttack() + " ATK  " + getDefense() + " DEF\n";
		str += "skill1 - hits you twice\nskill2 - overheals by 50%\nskill3 - decreases your max health by 33% of your atk\nskill4 - buffs stats by 99%\nskill5 - sets your hp to 50%\nskill6 - sets your hp to 1, skips your turn\nskill7 - def ignoring attack\nskill8 - copies your stats but better";  
		return str;
	}
}