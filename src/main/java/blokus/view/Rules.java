package blokus.view;

import java.io.InputStream;
import java.util.ArrayList;

import blokus.model.Config;
import blokus.model.GameType;

/**
 * Rules
 */
public class Rules {
	private ArrayList<String> ruleFilesName;
	private int curRuleNo;

	private int nbRules;

	public Rules(GameType type) {
		if (type != null) {
			ruleFilesName = Config.i().getMany(Config.RULES + type);
			System.out.println(ruleFilesName);
			this.curRuleNo = 0;
			this.nbRules = Config.i().geti("nb_rules_" + type);
		} else {
			ruleFilesName = Config.i().getMany(Config.CONTROLES);
			System.out.println(ruleFilesName);
			this.curRuleNo = 0;
			this.nbRules = Config.i().geti("nb_controles");
		}
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