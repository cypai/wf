package com.pipai.wf.config;


public class WFConfig {

	private static BattleProperties battle;

	public static void unsetConfig() {
		battle = null;
	}

	public static void resetConfig() {
		battle = new BattleProperties();
	}

	/**
	 * Sets BattleProperties configuration. Generally should only be used for testing.
	 */
	public static void setBattleProps(BattleProperties props) {
		battle = props;
	}

	public static BattleProperties battleProps() {
		return battle;
	}

}
