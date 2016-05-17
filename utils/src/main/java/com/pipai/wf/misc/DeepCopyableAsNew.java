package com.pipai.wf.misc;

public interface DeepCopyableAsNew {

	/**
	 * Returns a new object containing a copy of everything inside, created as "new" (see CopyableAsNew).
	 */
	Object deepCopyAsNew();

}
