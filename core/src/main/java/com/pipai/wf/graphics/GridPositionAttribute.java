package com.pipai.wf.graphics;

import com.badlogic.gdx.graphics.g3d.Attribute;
import com.badlogic.gdx.utils.NumberUtils;
import com.pipai.wf.battle.map.GridPosition;

public class GridPositionAttribute extends Attribute {
	public final static String GridPositionAlias = "gridPosition";
	public final static long Type = register(GridPositionAlias);
	protected static long Mask = Type;

	/**
	 * Method to check whether the specified type is a valid
	 * GridPositionAttribute type
	 */
	public static Boolean is(final long type) {
		return (type & Mask) != 0;
	}

	public GridPosition value;

	public GridPositionAttribute() {
		super(Type);
	}

	public GridPositionAttribute(final GridPosition value) {
		super(Type);
		this.value = new GridPosition(value);
	}

	/** copy constructor */
	public GridPositionAttribute(GridPositionAttribute other) {
		this(other.value);
	}

	@Override
	public Attribute copy() {
		return new GridPositionAttribute(this);
	}

	@Override
	public int hashCode() {
		final int prime = 17;
		final long v = NumberUtils.doubleToLongBits(value.x + Integer.MAX_VALUE + value.y);
		return prime * super.hashCode() + (int) (v ^ (v >>> 32));
	}

	@Override
	public int compareTo(Attribute o) {
		if (type != o.type) {
			return type < o.type ? -1 : 1;
		}
		GridPosition otherValue = ((GridPositionAttribute) o).value;
		return value.equals(otherValue) ? 0 : (value.x < otherValue.x ? -1 : 1);	// Arbitrary left to right comparison
	}
}
