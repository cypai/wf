package com.pipai.wf.misc;

public interface Copyable {

	/**
	 * Returns a copied version of the object. This replaces clone (since clone is strange).
	 */
	Object copy();

}
