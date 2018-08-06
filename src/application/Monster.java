package application;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Monster implements Serializable{
	
	private int hp, hpBase, ac, xp, str, dex, intl, wis, chr, con, init;
	private double cr;
	private String name, actions, hpDice;
	
	public Monster() {
		hp = 0;
		hpBase = 0;
		ac = 0;
		cr = 0;
		xp = 0;
		str = 10;
		dex = 10;
		intl = 10;
		wis = 10;
		chr = 10;
		con = 10;
		name = "New Monster";
		actions = "";
		hpDice = "d4";
		init = 0;
	}
	
	
	
	public Monster(int hp, int hpBase, int ac, int xp, int str, int dex, int intl, int wis, int chr, int con, int init,
			double cr, String name, String actions, String hpDice) {
		super();
		this.hp = hp;
		this.hpBase = hpBase;
		this.ac = ac;
		this.xp = xp;
		this.str = str;
		this.dex = dex;
		this.intl = intl;
		this.wis = wis;
		this.chr = chr;
		this.con = con;
		this.init = init;
		this.cr = cr;
		this.name = name;
		this.actions = actions;
		this.hpDice = hpDice;
	}



	public Monster getCopy() {
		return new Monster(hp, hpBase, ac, xp, str, dex, intl, wis, chr, con, init, cr, name, actions, hpDice);
	}
	
	public void setActions(String str) {
		actions = str;
	}
	
	public String getActions(){
		return actions;
	}
	
	public void setHpDice(String str) {
		if(str != null && !str.equals("")) hpDice = str;
	}
	
	public String getHpDice() {
		return hpDice;
	}

	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}
	
	public int getHpBase() {
		return hpBase;
	}
	
	public void setHpBase(int hpBase) {
		this.hpBase = hpBase;
	}

	public int getStr() {
		return str;
	}

	public void setStr(int str) {
		this.str = str;
	}

	public int getDex() {
		return dex;
	}

	public void setDex(int dex) {
		this.dex = dex;
	}

	public int getIntl() {
		return intl;
	}

	public void setIntl(int intl) {
		this.intl = intl;
	}

	public int getWis() {
		return wis;
	}

	public void setWis(int wis) {
		this.wis = wis;
	}

	public int getChr() {
		return chr;
	}

	public void setChr(int chr) {
		this.chr = chr;
	}

	public int getCon() {
		return con;
	}

	public void setCon(int con) {
		this.con = con;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public int getAc() {
		return ac;
	}

	public void setAc(int ac) {
		this.ac = ac;
	}

	public double getCr() {
		return cr;
	}

	public void setCr(double cr) {
		this.cr = cr;
	}

	public int getXp() {
		return xp;
	}

	public void setXp(int xp) {
		this.xp = xp;
	}
	
	public int getRandHp() {
		int total = 0;
		int diceType = Integer.parseInt(hpDice.substring(1));
		for(int i = 0; i < hp; i++) {
			total += getDiceRoll(diceType);
		}
		return total + hpBase;
	}
	
	private int getDiceRoll(int diceType) {
		return (int) Math.ceil(Math.random() * diceType);
	}
	
	public int getInit() {
		return init;
	}
	
	public void setInit(int init) {
		this.init = init;
	}
}
