package com.pipai.wf.artemis.event;

import com.pipai.wf.battle.map.MapGraph;

import net.mostlyoriginal.api.event.common.Event;

public class MovementTileUpdateEvent implements Event {

	public MapGraph mapgraph;

	public MovementTileUpdateEvent(MapGraph mapgraph) {
		this.mapgraph = mapgraph;
	}

}
