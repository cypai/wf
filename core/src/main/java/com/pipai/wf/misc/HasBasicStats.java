package com.pipai.wf.misc;

public interface HasBasicStats {

	BasicStats getBasicStats();

	default int getHP() {
		return getBasicStats().getHP();
	}

	default int getMaxHP() {
		return getBasicStats().getMaxHP();
	}

	default int getMP() {
		return getBasicStats().getMP();
	}

	default int getMaxMP() {
		return getBasicStats().getMaxMP();
	}

	default int getAP() {
		return getBasicStats().getAP();
	}

	default int getMaxAP() {
		return getBasicStats().getMaxAP();
	}

	default int getAim() {
		return getBasicStats().getAim();
	}

	default int getMobility() {
		return getBasicStats().getMobility();
	}

	default int getDefense() {
		return getBasicStats().getDefense();
	}

}
