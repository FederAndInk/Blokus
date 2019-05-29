package blokus.view;

import java.util.ArrayList;
import java.io.InputStream;

import blokus.model.Config;
import blokus.model.GameType;
import javafx.scene.image.Image;

/**
 * Rules
 */
public class Rules {
	private ArrayList<String> ruleFilesName;
	private int curRuleNo;

	private int nbRules;

	public Rules(GameType type) {
		ruleFilesName = Config.i().getMany(Config.RULES + type);
		System.out.println(ruleFilesName);
		this.curRuleNo = 0;
		this.nbRules = Config.i().geti("nb_rules_" + type);
	}

	public void next() {
		curRuleNo = (curRuleNo + 1);
	}

	public void prev() {
		curRuleNo = (curRuleNo - 1);
	}

	public boolean hasPrev() {
		return curRuleNo > 0;
	}

	public boolean hasNext() {
		return curRuleNo < nbRules - 1;
	}

	public InputStream get() {
		System.out.println(ruleFilesName.get(curRuleNo));
		return Config.load(ruleFilesName.get(curRuleNo));
	}

}