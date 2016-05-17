package com.pipai.wf.misc;

public interface CopyableAsNew {

	/**
	 * Returns a "new" version of the object (as in, HP is restored, item use refreshed, etc.)
	 */
	Object copyAsNew();

}
