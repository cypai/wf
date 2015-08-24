package com.pipai.wf.config;


public class WFConfig {
	
	private static BattleProperties battle;
	
	public static void unsetConfig() {
		battle = null;
	}
	
	public static void resetConfig() {
		battle = new BattleProperties();
	}
	
	public static BattleProperties battleProps() {
		return battle;
	}
	
}
