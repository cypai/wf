package com.pipai.wf.artemis.event;

import com.badlogic.gdx.math.collision.Ray;

import net.mostlyoriginal.api.event.common.Event;

public class RightClickRayEvent implements Event {

	public final Ray pickRay;

	public RightClickRayEvent(Ray pickRay) {
		this.pickRay = pickRay;
	}

}
