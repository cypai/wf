package com.pipai.wf.unit.abilitytree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.pipai.wf.misc.DeepCopyableAsNew;
import com.pipai.wf.unit.ability.Ability;

public class AbilityTree implements DeepCopyableAsNew {

	private AbilityTreeNode root;
	private ArrayList<ArrayList<AbilityTreeNode>> abilities;

	AbilityTree() {
		abilities = new ArrayList<>();
	}

	public int getHeight() {
		return abilities.size() + 1;
	}

	public List<AbilityTreeNode> getAbilitiesAtHeight(int height) {
		if (height > getHeight()) {
			return new ArrayList<>();
		}
		if (height == 1) {
			return Arrays.asList(root);
		} else {
			return abilities.get(height - 2);
		}
	}

	@Override
	public AbilityTree deepCopyAsNew() {
		AbilityTree tree = new AbilityTree();
		tree.root = new AbilityTreeNode(root.getAbility().copyAsNew());
		for (ArrayList<AbilityTreeNode> levelAbilities : abilities) {
			ArrayList<AbilityTreeNode> copy = new ArrayList<>();
			levelAbilities.forEach((node) -> {
				copy.add(new AbilityTreeNode(node.getAbility().copyAsNew()));
			});
			tree.abilities.add(copy);
		}
		return tree;
	}

	public int getHighestTakenLevel() {
		int level = 0;
		while (level <= getHeight()) {
			level += 1;
			boolean taken = false;
			for (AbilityTreeNode node : getAbilitiesAtHeight(level)) {
				if (node.isTaken()) {
					taken = true;
					break;
				}
			}
			if (!taken) {
				return level - 1;
			}
		}
		return level - 1;
	}

	public static class Builder {

		private AbilityTree tree;
		private ArrayList<AbilityTreeNode> current;

		public Builder(Ability root) {
			tree = new AbilityTree();
			tree.root = new AbilityTreeNode(root);
			current = new ArrayList<>();
		}

		public Builder addAbilityAtCurrentLevel(Ability ability) {
			current.add(new AbilityTreeNode(ability));
			return this;
		}

		public Builder goToNextLevel() {
			tree.abilities.add(current);
			current = new ArrayList<>();
			return this;
		}

		public AbilityTree build() {
			if (current.size() > 0) {
				tree.abilities.add(current);
			}
			return tree;
		}

	}

}
