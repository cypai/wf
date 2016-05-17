package com.pipai.wf.battle.event;

import java.util.LinkedList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BattleLog {

	private static final Logger LOGGER = LoggerFactory.getLogger(BattleLog.class);

	private LinkedList<BattleEvent> log;

	public BattleLog() {
		log = new LinkedList<BattleEvent>();
	}

	public BattleEvent getLastEvent() {
		return log.peekLast();
	}

	public void logEvent(BattleEvent ev) {
		LOGGER.debug(buildStringLog(ev, 0));
		log.add(ev);
	}

	private static String buildStringLog(BattleEvent ev, int depth) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < depth; i++) {
			builder.append('-');
		}
		builder.append("[" + ev.getClass().getSimpleName() + "] ");
		builder.append(ev.toString());
		for (BattleEvent chain : ev.getChainEvents()) {
			builder.append("\n" + buildStringLog(chain, depth + 1));
		}
		return builder.toString();
	}

	public void clear() {
		log.clear();
	}

}
