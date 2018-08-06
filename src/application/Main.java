package application;
	
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Comparator;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class Main extends Application {
	
	//A List object holds all of the Monster objects
	//in memory and allows them to be easily referenced
	private ArrayList<Monster> monsters = new ArrayList<>();
	private ArrayList<Encounter> encounters = new ArrayList<>();
	
	Scene home, monsterDB, encounter, play;
	Stage stage;
	//screen size info
	int screenWidth = 1050;
	int screenHeight = 650;
	
	//menu components
	Button monstersMB, encountersMB, playMB;
	HBox menu;
	
	//home components
	VBox homeRoot;
	Label welcomeMsg;
	Button githubButton, tutorialButton;
	HBox homeButtons;
	
	//monsterDB components
	HBox monsterDBPane, monsterNameArea, monsterButtonArea;
	VBox monsterDBRoot, monsterList, monsterInfoArea;
	ScrollPane monsterListScroller;
	GridPane monsterInfo;
	Label mDBStrLbl, mDBDexLbl, mDBConLbl, mDBIntLbl, mDBWisLbl, mDBChrLbl;
	Label mDBStrMod, mDBDexMod, mDBConMod, mDBIntMod, mDBWisMod, mDBChrMod;
	TextField mDBStrText, mDBDexText, mDBConText, mDBIntText, mDBWisText, mDBChrText;
	Label mDBAcLbl, mDBCrLbl, mDBXpLbl, mDBNameLbl, mDBHpLbl, mDBHpBaseLbl, mDBActionsLbl, mDBSpacing;
	TextField mDBAcText, mDBCrText, mDBXpText, mDBNameText, mDBHpText, mDBHpBaseText;
	TextArea mDBActionsText;
	ComboBox<String> mDBHpDice;
	Button mDBSave, mDBDelete, mDBNew;
	ArrayList<Button> mDBList;
	
	//encounter components
	HBox encounterArea, encounterButtons, encounterNameArea;
	VBox encounterPane, encounterMonsters, encounterInfoArea, encounterInfoPane, encounterRoot;
	ScrollPane encounterPaneScroller, encounterMonstersScroller, encounterInfoPaneScroller;
	Button encSave, encDelete;
	TextField encNameText;
	Label encNameLbl;
	ArrayList<Button> eList, eMDBList, eMList; 
	Encounter curEncounter;
	
	//play components
	HBox playArea, playInfoArea;
	VBox playRoot, playSelector, playMonsterArea;
	ScrollPane playSelectorScroller, playMonsterAreaScroller;
	Label playNameLbl, playXpLbl;
	ArrayList<Button> pList;
	ArrayList<HBox> pMList;
	Encounter selectedEncounter;
	
	
	@Override
	public void start(Stage primaryStage) {
		stage = primaryStage;
		
		//load monsters and encounters from files
		loadMonstersFromFile();
		loadEncountersFromFile();		
		
		configureMenu();
		configureHomeScene();
		
		primaryStage.setScene(home);
		primaryStage.setTitle("Tabletop RPG Encounter Wizard");
		primaryStage.setResizable(false);
		primaryStage.show();
		
		configureMonsterDBComponents();		
		configureEncounterComponents();
		configurePlayComponents();
	}
	
	private void updateMonsterScene() {
		mDBList = new ArrayList<>();
		for (Monster monster : monsters) {
			Button monsterButton = new Button(monster.getName() + " : CR - " + monster.getCr());
			monsterButton.getStyleClass().add("monsterButton");
			monsterButton.setOnAction(event -> loadMonster(monster));
			mDBList.add(monsterButton);
		}
		monsterList = new VBox();
		monsterList.getChildren().addAll(mDBList);
		monsterList.getStyleClass().add("listArea");
		monsterListScroller = new ScrollPane(monsterList);
		monsterButtonArea = new HBox(10, mDBSave, mDBDelete);
		monsterButtonArea.setAlignment(Pos.CENTER);
		monsterNameArea = new HBox(10, mDBNameLbl, mDBNameText);
		monsterInfoArea = new VBox(10, monsterNameArea, monsterInfo, mDBActionsLbl, mDBActionsText, monsterButtonArea);
		monsterInfoArea.setPadding(new Insets(20));
		monsterDBPane = new HBox(10, monsterListScroller, monsterInfoArea);
		monsterDBRoot = new VBox(10, menu, monsterDBPane);
		monsterDB = new Scene(monsterDBRoot, screenWidth, screenHeight);
		monsterDB.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
	}
	
	public void updateEncounterScene() {
		eMDBList = new ArrayList<>();
		eList = new ArrayList<>();
		for (Monster monster : monsters) {
			Button monsterButton = new Button(monster.getName() + " : CR - " + monster.getCr());
			monsterButton.getStyleClass().add("monsterButton");
			monsterButton.setOnAction(event -> addMonster(monster));
			eMDBList.add(monsterButton);
		}
		for (Encounter enc : encounters) {
			Button encounterButton = new Button(enc.getName());
			encounterButton.getStyleClass().add("encounterButton");
			encounterButton.setOnAction(event -> loadEncounter(enc));
			eList.add(encounterButton);
		}
		eMList = new ArrayList<>();
		for (Monster monster : curEncounter.getMonsters()) {
			Button listedMonsterButton = new Button(monster.getName());
			listedMonsterButton.getStyleClass().add("encounterButton");
			listedMonsterButton.setOnAction(event -> {
				curEncounter.removeMonster(monster);
				updateEncounterScene();
			});
			eMList.add(listedMonsterButton);
		}
		encounterPane = new VBox();
		encounterPane.getChildren().addAll(eList);
		encounterPane.getStyleClass().add("encounterList");
		encounterPaneScroller = new ScrollPane(encounterPane);
		encounterMonsters = new VBox();
		encounterMonsters.getChildren().addAll(eMDBList);
		encounterMonsters.getStyleClass().add("listArea");
		encounterMonstersScroller = new ScrollPane(encounterMonsters);
		encounterNameArea = new HBox(5, encNameLbl, encNameText);
		encounterButtons = new HBox(10, encSave, encDelete);
		encounterInfoArea = new VBox();
		encounterInfoArea.getChildren().addAll(eMList);
		encounterInfoArea.getStyleClass().add("encounterMonsterList");
		encounterInfoPaneScroller = new ScrollPane(encounterInfoArea);
		encounterInfoPane = new VBox(10, encounterNameArea, encounterInfoPaneScroller, encounterButtons);
		encounterArea = new HBox(10, encounterPaneScroller, encounterMonstersScroller, encounterInfoPane);
		encounterRoot = new VBox(10, menu, encounterArea);
		encounter = new Scene(encounterRoot, screenWidth, screenHeight);
		encounter.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		stage.setScene(encounter);
	}
	
	public void updatePlayScene() {
		pList = new ArrayList<>();
		for(Encounter enc : encounters) {
			Button playEncounterButton = new Button(enc.getName());
			playEncounterButton.getStyleClass().add("encounterButton");
			playEncounterButton.setOnAction(event -> playEncounter(enc));
			pList.add(playEncounterButton);
		}
		playSelector = new VBox();
		playSelector.getChildren().addAll(pList);
		playSelector.getStyleClass().add("encounterList");
		playSelectorScroller = new ScrollPane(playSelector);
		pMList = new ArrayList<>();
		for(Monster monster : selectedEncounter.getMonsters()) {
			monster.setInit((int)Math.ceil(Math.random() * 20));
			System.out.println(monster.hashCode());
		}
		selectedEncounter.sortMonstersInit();
		for(Monster monster : selectedEncounter.getMonsters()) {
			Label mName = new Label(monster.getName());
			Label mXp = new Label (monster.getXp() + " XP");
			Label mHp = new Label (monster.getRandHp() + " HP");
			TextField mDmg = new TextField();
			Button mUpdate = new Button("-->");
			Label mInit = new Label(monster.getInit() + "");
			mUpdate.getStyleClass().add("smButton");
			mName.getStyleClass().add("medTextLong");
			mXp.getStyleClass().add("medText");
			mHp.getStyleClass().add("medText");
			mDmg.getStyleClass().add("tallStatArea");
			mInit.getStyleClass().add("medText");
			HBox mInfo = new HBox(20, mName, mXp, mHp, mDmg, mUpdate, mInit);
			mInfo.getStyleClass().add("monsterArea");
			mUpdate.setOnAction(event -> {
				int curHp = Integer.parseInt(mHp.getText().substring(0, 1));
				curHp -= Integer.parseInt(mDmg.getText());
				if(curHp <= 0) {
					curHp = 0;
					mInfo.getStyleClass().add("deadMonsterArea");
				}
				mHp.setText(curHp + " HP");
				mDmg.setText("");
			});
			//monster stats tooltip
			Tooltip.install(mInfo, buildTooltip(monster));
			pMList.add(mInfo);
		}
		playNameLbl.setText(selectedEncounter.getName());
		playXpLbl.setText(selectedEncounter.getXp() + " XP");
		playInfoArea = new HBox(playNameLbl, playXpLbl);
		playMonsterArea = new VBox(10, playInfoArea);
		playMonsterArea.getChildren().addAll(pMList);
		playMonsterArea.getStyleClass().add("playMonsterList");
		playMonsterAreaScroller = new ScrollPane(playMonsterArea);
		playArea = new HBox(20, playSelectorScroller, playMonsterAreaScroller);
		playRoot = new VBox(10, menu, playArea);
		play = new Scene(playRoot, screenWidth, screenHeight);
		play.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		stage.setScene(play);
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	private static void calcMod(TextField val, Label mod) {
		int statVal = 10;
		String statValRaw = val.getText();
		if(statValRaw.matches("\\d+")) {
			statVal = Integer.parseInt(statValRaw);
		}
		int modVal = (int) Math.ceil(statVal / 2) - 5;
		mod.setText(modVal + "");
	}
	
	private static int calcMod(int val) {
		return (int) Math.ceil(val / 2) - 5;
	}
	
	private void saveMonster() {
		for (Monster monster : monsters) {
			if(monster.getName().equals(mDBNameText.getText())) {
				monsters.remove(monster);
				break;
			}
		}
		Monster monster = new Monster();
		monster.setStr(convStat(mDBStrText.getText()));
		monster.setDex(convStat(mDBDexText.getText()));
		monster.setCon(convStat(mDBConText.getText()));
		monster.setIntl(convStat(mDBIntText.getText()));
		monster.setWis(convStat(mDBWisText.getText()));
		monster.setChr(convStat(mDBChrText.getText()));
		monster.setAc(convStat(mDBAcText.getText()));
		monster.setCr(convStatDouble(mDBCrText.getText()));
		monster.setXp(convStat(mDBXpText.getText()));
		monster.setHp(convStat(mDBHpText.getText()));
		monster.setHpDice(mDBHpDice.getValue());
		monster.setHpBase(convStat(mDBHpBaseText.getText()));
		monster.setName(mDBNameText.getText());
		monster.setActions(mDBActionsText.getText());
		monsters.add(monster);
		monsters.sort(new Comparator<Monster>() {
			public int compare(Monster m1, Monster m2) {
				return m1.getName().compareToIgnoreCase(m2.getName());
			}
			
		});
		updateMonsterScene();
		mDBClearFields();
		saveMonstersToFile();
	}
	
	private static int convStat(String str) {
		if(str.matches("\\d+")) {
			return Integer.parseInt(str);
		}else {
			return 0;
		}
	}
	
	private static double convStatDouble(String str) {
		if(str.matches("\\d+.?\\d*")) {
			return Double.parseDouble(str);
		}else {
			return 0;
		}
	}
	
	private void deleteMonster() {
		for (Monster monster : monsters) {
			if(monster.getName().equals(mDBNameText.getText())) {
				monsters.remove(monster);
				break;
			}
		}
		updateMonsterScene();
		mDBClearFields();
		saveMonstersToFile();
	}
	
	private void mDBClearFields() {
		mDBStrText.setText("");
		mDBDexText.setText("");
		mDBConText.setText("");
		mDBIntText.setText("");
		mDBWisText.setText("");
		mDBChrText.setText("");
		mDBNameText.setText("");
		mDBAcText.setText("");
		mDBCrText.setText("");
		mDBXpText.setText("");
		mDBHpText.setText("");
		mDBHpDice.setValue("");
		mDBHpBaseText.setText("");
		mDBActionsText.setText("");
	}
	
	private void loadMonster(Monster monster) {
		mDBStrText.setText(monster.getStr() + "");
		mDBDexText.setText(monster.getDex() + "");
		mDBConText.setText(monster.getCon() + "");
		mDBIntText.setText(monster.getIntl() + "");
		mDBWisText.setText(monster.getWis() + "");
		mDBChrText.setText(monster.getChr() + "");
		mDBNameText.setText(monster.getName());
		mDBAcText.setText(monster.getAc() + "");
		mDBCrText.setText(monster.getCr() + "");
		mDBXpText.setText(monster.getXp() + "");
		mDBHpText.setText(monster.getHp() + "");
		mDBHpDice.setValue(monster.getHpDice());
		mDBHpBaseText.setText(monster.getHpBase() + "");
		mDBActionsText.setText(monster.getActions());
	}
	
	private void saveEncounter() {
		for (Encounter enc : encounters) {
			if(enc.getName().equals(encNameText.getText())) {
				encounters.remove(enc);
				break;
			}
		}
		curEncounter.setName(encNameText.getText());
		curEncounter.calcXp();
		encounters.add(curEncounter);
		encounters.sort(new Comparator<Encounter>() {
			public int compare(Encounter e1, Encounter e2) {
				return e1.getName().compareToIgnoreCase(e2.getName());
			}
		});
		clearEncounter();
		saveEncountersToFile();
	}
	
	private void addMonster(Monster monster) {
		curEncounter.addMonster(monster.getCopy());
		curEncounter.sortMonsters();
		updateEncounterScene();
	}
	
	private void deleteEncounter() {
		for (Encounter enc : encounters) {
			if(enc.getName().equals(encNameText.getText())) {
				encounters.remove(enc);
				break;
			}
		}
		clearEncounter();
		saveEncountersToFile();
	}
	
	private void clearEncounter() {
		encNameText.setText("");
		curEncounter = new Encounter();
		updateEncounterScene();
	}
	
	private void loadEncounter(Encounter enc) {
		curEncounter = enc;
		encNameText.setText(enc.getName());
		curEncounter.sortMonsters();
		updateEncounterScene();
	}
	
	private void playEncounter(Encounter enc) {
		selectedEncounter = enc.getCopy();
		updatePlayScene();
	}
	
	private void configureMenu() {
		//menu components
			monstersMB = new Button("Monsters");
			monstersMB.getStyleClass().add("menuButton");
			monstersMB.setOnAction(event -> {
				updateMonsterScene();
				stage.setScene(monsterDB);
			});
			encountersMB = new Button("Design Encounters");
			encountersMB.setOnAction(event -> {
				curEncounter = new Encounter();
				updateEncounterScene();
				stage.setScene(encounter);
			});
			encountersMB.getStyleClass().add("menuButton");
			playMB = new Button("Run Encounter");
			playMB.setOnAction(event -> {
				selectedEncounter = new Encounter();
				updatePlayScene();
				stage.setScene(play);
			});
			playMB.getStyleClass().add("menuButton");
			menu = new HBox(monstersMB, encountersMB, playMB);
	}
	
	private void configureHomeScene() {
		welcomeMsg = new Label("Welcome to the Tabletop RPG Encounter Wizard. This tool "
				+ "was created to help design and run combat-oriented encounters. If you "
				+ "aren't sure what to do, check out the tutorial. Problems or suggestions? "
				+ "Check out my Github.");
		welcomeMsg.getStyleClass().add("welcomeMsg");
		githubButton = new Button("Github");
		githubButton.setOnAction(event -> {
			try {
				java.awt.Desktop.getDesktop().browse(new URI("https://github.com/hunterdubbs"));
			} catch (IOException | URISyntaxException e) {
				e.printStackTrace();
			}
		});
		tutorialButton = new Button("Tutorial");
		homeButtons = new HBox(20, githubButton, tutorialButton);
		homeButtons.setAlignment(Pos.CENTER);
		homeRoot = new VBox(200, menu, welcomeMsg, homeButtons);
		homeRoot.setAlignment(Pos.TOP_CENTER);
		home = new Scene(homeRoot, screenWidth, screenHeight);
		
		home.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
	}
	
	private void configureMonsterDBComponents() {
		//monsterDB components
		mDBStrLbl = new Label("Str");
		mDBDexLbl = new Label("Dex");
		mDBConLbl = new Label("Con");
		mDBIntLbl = new Label("Int");
		mDBWisLbl = new Label("Wis");
		mDBChrLbl = new Label("Chr");
				
		mDBStrText = new TextField();
		mDBDexText = new TextField();
		mDBConText = new TextField();
		mDBIntText = new TextField();
		mDBWisText = new TextField();
		mDBChrText = new TextField();
				
		mDBStrText.getStyleClass().add("statArea");
		mDBDexText.getStyleClass().add("statArea");
		mDBConText.getStyleClass().add("statArea");
		mDBIntText.getStyleClass().add("statArea");
		mDBWisText.getStyleClass().add("statArea");
		mDBChrText.getStyleClass().add("statArea");
				
		mDBStrMod = new Label("+0");
		mDBDexMod = new Label("+0");
		mDBConMod = new Label("+0");
		mDBIntMod = new Label("+0");
		mDBWisMod = new Label("+0");
		mDBChrMod = new Label("+0");
				
		mDBStrText.setOnKeyReleased(event -> calcMod(mDBStrText, mDBStrMod));
		mDBDexText.setOnKeyReleased(event -> calcMod(mDBDexText, mDBDexMod));
		mDBConText.setOnKeyReleased(event -> calcMod(mDBConText, mDBConMod));
		mDBIntText.setOnKeyReleased(event -> calcMod(mDBIntText, mDBIntMod));
		mDBWisText.setOnKeyReleased(event -> calcMod(mDBWisText, mDBWisMod));
		mDBChrText.setOnKeyReleased(event -> calcMod(mDBChrText, mDBChrMod));
				
		mDBAcLbl = new Label("AC");
		mDBCrLbl = new Label("CR");
		mDBXpLbl = new Label("XP");
		mDBHpLbl = new Label("HP");
		mDBHpBaseLbl = new Label("+");
		mDBNameLbl = new Label("Monster Name");
		mDBActionsLbl = new Label("Actions");
		mDBSpacing = new Label();
				
		mDBAcText = new TextField();
		mDBCrText = new TextField();
		mDBXpText = new TextField();
		mDBHpText = new TextField();
		mDBNameText = new TextField();
		mDBActionsText = new TextArea();
		mDBHpDice = new ComboBox<String>();
		mDBHpBaseText = new TextField();
		mDBHpDice.getItems().addAll("d4", "d6", "d8", "d10", "d12", "d20");
				
		mDBAcText.getStyleClass().add("statArea");
		mDBCrText.getStyleClass().add("statArea");
		mDBXpText.getStyleClass().add("medStatArea");
		mDBHpText.getStyleClass().add("statArea");
		mDBHpBaseText.getStyleClass().add("statArea");
		mDBNameText.getStyleClass().add("lgStatArea");
		mDBActionsText.getStyleClass().add("actionsArea");
				
		monsterInfo = new GridPane();
		monsterInfo.setHgap(10);
		monsterInfo.setVgap(10);
		monsterInfo.setPadding(new Insets(30, 80, 10, 80));
				
		mDBSave = new Button("Save Changes");
		mDBDelete = new Button("Delete Monster");
				
		monsterInfo.add(mDBAcLbl, 0, 0);
		monsterInfo.add(mDBAcText, 1, 0);
		monsterInfo.add(mDBCrLbl, 2, 0);
		monsterInfo.add(mDBCrText, 3, 0);
		monsterInfo.add(mDBXpLbl, 6, 2);
		monsterInfo.add(mDBXpText, 6, 3);
		monsterInfo.add(mDBHpLbl, 4, 0);
		monsterInfo.add(mDBHpText, 5, 0);
		monsterInfo.add(mDBHpBaseLbl, 7, 0);
		monsterInfo.add(mDBHpBaseText, 8, 0);
		monsterInfo.add(mDBHpDice, 6, 0);
		monsterInfo.add(mDBSpacing, 0, 1);
		monsterInfo.add(mDBStrLbl, 0, 2);
		monsterInfo.add(mDBDexLbl, 1, 2);
		monsterInfo.add(mDBConLbl, 2, 2);
		monsterInfo.add(mDBIntLbl, 3, 2);
		monsterInfo.add(mDBWisLbl, 4, 2);
		monsterInfo.add(mDBChrLbl, 5, 2);
		monsterInfo.add(mDBStrText, 0, 3);
		monsterInfo.add(mDBDexText, 1, 3);
		monsterInfo.add(mDBConText, 2, 3);
		monsterInfo.add(mDBIntText, 3, 3);
		monsterInfo.add(mDBWisText, 4, 3);
		monsterInfo.add(mDBChrText, 5, 3);
		monsterInfo.add(mDBStrMod, 0, 4);
		monsterInfo.add(mDBDexMod, 1, 4);
		monsterInfo.add(mDBConMod, 2, 4);
		monsterInfo.add(mDBIntMod, 3, 4);
		monsterInfo.add(mDBWisMod, 4, 4);
		monsterInfo.add(mDBChrMod, 5, 4);
		
		mDBSave.setOnAction(event -> {
			saveMonster();
			stage.setScene(monsterDB);
		});
		mDBDelete.setOnAction(event -> {
			deleteMonster();
			stage.setScene(monsterDB);
		});
	}
	
	private void configureEncounterComponents() {
		encNameLbl = new Label("Name");
		encNameText = new TextField();
		encNameText.getStyleClass().add("nameArea");
		encSave = new Button("Save");
		encSave.setOnAction(event -> saveEncounter());
		encDelete = new Button("Delete");
		encDelete.setOnAction(event -> deleteEncounter());
	}
	
	private void configurePlayComponents() {
		playNameLbl = new Label("Select an Encounter to Begin");
		playNameLbl.getStyleClass().add("lgTextLong");
		playXpLbl = new Label();
		playXpLbl.getStyleClass().add("lgText");
	}
	
	private Tooltip buildTooltip(Monster monster) {
		Label name = new Label(monster.getName());
		name.getStyleClass().add("tooltipBold");
		Label actions = new Label(monster.getActions());
		actions.getStyleClass().add("tooltip");
		GridPane gp = new GridPane();
		gp.setHgap(10);
		gp.getStyleClass().add("tooltip");
		gp.add(new Label("AC"), 0, 0);
		gp.add(new Label(monster.getAc() + ""), 1, 0);
		gp.add(new Label(), 0, 1);
		gp.add(new Label("Str"), 0, 2);
		gp.add(new Label("Dex"), 0, 3);
		gp.add(new Label("Con"), 0, 4);
		gp.add(new Label("Int"), 0, 5);
		gp.add(new Label("Wis"), 0, 6);
		gp.add(new Label("Chr"), 0, 7);
		gp.add(new Label(monster.getStr() + ""), 1, 2);
		gp.add(new Label(monster.getDex() + ""), 1, 3);
		gp.add(new Label(monster.getCon() + ""), 1, 4);
		gp.add(new Label(monster.getIntl() + ""), 1, 5);
		gp.add(new Label(monster.getWis() + ""), 1, 6);
		gp.add(new Label(monster.getChr() + ""), 1, 7);
		gp.add(new Label(calcMod(monster.getStr()) + ""), 2, 2);
		gp.add(new Label(calcMod(monster.getDex()) + ""), 2, 3);
		gp.add(new Label(calcMod(monster.getCon()) + ""), 2, 4);
		gp.add(new Label(calcMod(monster.getIntl()) + ""), 2, 5);
		gp.add(new Label(calcMod(monster.getWis()) + ""), 2, 6);
		gp.add(new Label(calcMod(monster.getChr()) + ""), 2, 7);
		
		Tooltip tt = new Tooltip();
		tt.setGraphic(new VBox(10, name, gp, actions));
		return tt;
	}
	
	private void saveMonstersToFile() {
		try {
			FileOutputStream out = new FileOutputStream(new File("monsters.dew"));
			ObjectOutputStream objOut = new ObjectOutputStream(out);
			objOut.writeObject(monsters);
			objOut.close();
			out.close();
		} catch (IOException e) {
			System.out.println("failed to save file");
		}
	}
	
	private void loadMonstersFromFile() {
		try {
			FileInputStream in = new FileInputStream(new File("monsters.dew"));
			ObjectInputStream objIn = new ObjectInputStream(in);
			monsters = (ArrayList<Monster>) objIn.readObject();
			objIn.close();
			in.close();
		} catch (IOException e) {
			System.out.println("file not found");
		} catch (ClassNotFoundException e) {
			System.out.println("failed to read file");
		}
	}
	
	private void saveEncountersToFile() {
		try {
			FileOutputStream out = new FileOutputStream(new File("encounters.dew"));
			ObjectOutputStream objOut = new ObjectOutputStream(out);
			objOut.writeObject(encounters);
			objOut.close();
			out.close();
		} catch (IOException e) {
			System.out.println("failed to save file");
		}
	}
	
	private void loadEncountersFromFile() {
		try {
			FileInputStream in = new FileInputStream(new File("encounters.dew"));
			ObjectInputStream objIn = new ObjectInputStream(in);
			encounters = (ArrayList<Encounter>) objIn.readObject();
			objIn.close();
			in.close();
		} catch (IOException e) {
			System.out.println("file not found");
		} catch (ClassNotFoundException e) {
			System.out.println("failed to read file");
		}
	}
}
