package application;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;

@SuppressWarnings("serial")
public class Encounter implements Serializable{

	private ArrayList<Monster> monsters;
	private String name;
	private int xp;
	
	
	public Encounter() {
		monsters = new ArrayList<>();
		xp = 0;
		name = "";
	}
	
	public Encounter(ArrayList<Monster> monsters, String name, int xp) {
		this.monsters = monsters;
		this.name = name;
		this.xp = xp;
	}
	
	public void addMonster(Monster monster) {
		monsters.add(monster);
	}
	
	public void removeMonster(Monster monster) {
		monsters.remove(monster);
	}
	
	public ArrayList<Monster> getMonsters(){
		return monsters;
	}

	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public void calcXp() {
		int xpSubtotal = 0;
		for(Monster monster : monsters) {
			xpSubtotal += monster.getXp();
		}
		switch(monsters.size()) {
		case 1:
			xp = xpSubtotal;
			break;
		case 2:
			xp = (int) (xpSubtotal * 1.5);
			break;
		case 3: case 4: case 5: case 6:
			xp = xpSubtotal * 2;
			break;
		case 7: case 8: case 9: case 10:
			xp = (int) (xpSubtotal * 2.5);
			break;
		case 11: case 12: case 13: case 14:
			xp = xpSubtotal * 3;
			break;
		default: 
			xp = xpSubtotal * 4;
			break;
		}
	}
	
	public int getXp() {
		return xp;
	}

	public void sortMonsters() {
		monsters.sort(new Comparator<Monster>() {
			public int compare(Monster m1, Monster m2) {
				return m1.getName().compareToIgnoreCase(m2.getName());
			}
		});
	}
	
	public void sortMonstersInit() {
		monsters.sort(new Comparator<Monster>() {
			public int compare(Monster m1, Monster m2) {
				return m2.getInit() - m1.getInit();
			}
		});
	}
	
	public Encounter getCopy() {
		return new Encounter(monsters, name, xp);
	}
	
	
	
}
