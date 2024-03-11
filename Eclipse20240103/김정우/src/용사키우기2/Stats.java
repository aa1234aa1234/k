package 용사키우기2;

import java.io.Serializable;

public class Stats implements Serializable{
	private int basehealth = 100,health=100, defaulthealth = 100;
	private int baseattack = 20,attack=20, defaultattack = 20;
	private int basedefense = 20,defense=20, defaultdefense = 20;
	private int attackbuff = 0, defensebuff = 0;
	private int xp=0;
	private int level=1;
	private int encounterrate=0;
	private int nextlevel=20;
	public int getHealth() {
		return health;
	}
	public void setHealth(int health) {
		this.health = health;
	}
	public int getAttack() {
		return (int) (attack+((attack/100.0)*attackbuff));
	}
	public void setAttack(int attack) {
		this.attack = attack;
	}
	public int getDefense() {
		return defense+(defense/100*defensebuff);
	}
	public void setDefense(int defense) {
		this.defense = defense;
	}
	public int getEncounterrate() {
		return encounterrate;
	}
	public void setEncounterrate(int encounterrate) {
		this.encounterrate = encounterrate;
	}
	public int getXp() {
		return xp;
	}
	public void setXp(int xp) {
		this.xp = xp;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getNextlevel() {
		return nextlevel;
	}
	public void setNextlevel(int nextlevel) {
		this.nextlevel = nextlevel;
	}
	public int getBaseHealth() {
		return basehealth;
	}
	public int getBaseAttack() {
		return baseattack;
	}
	public int getBaseDefense() {
		return basedefense;
	}
	public void setBaseHealth(int basehealth) {
		this.basehealth = basehealth;
	}
	public void setBaseAttack(int baseattack) {
		this.baseattack = baseattack;
	}
	public void setBaseDefense(int basedefense) {
		this.basedefense = basedefense;
	}
	public int getDefaultHealth() {
		return defaulthealth;
	}
	public int getDefaultAttack() {
		return defaultattack;
	}
	public int getDefaultDefense() {
		return defaultdefense;
	}
	public int getAttackBuff() {
		return attackbuff;
	}
	public void setAttackBuff(int attackbuff) {
		this.attackbuff = attackbuff;
	}
	public int getDefenseBuff() {
		return defensebuff;
	}
	public void setDefenseBuff(int defensebuff) {
		this.defensebuff = defensebuff;
	}
	
}