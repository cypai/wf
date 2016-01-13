package com.pipai.wf.artemis.event;

import com.badlogic.gdx.math.collision.Ray;

import net.mostlyoriginal.api.event.common.Event;

public class MouseHoverRayEvent implements Event {

	public final Ray pickRay;

	public MouseHoverRayEvent(Ray pickRay) {
		this.pickRay = pickRay;
	}

}
