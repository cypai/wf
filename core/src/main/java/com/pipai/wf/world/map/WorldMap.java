package com.pipai.wf.world.map;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

public class WorldMap {

	private TiledMap tiledMap;

	public WorldMap() {
		tiledMap = new TmxMapLoader().load("map/map.tmx");
	}

	public TiledMap getTiledMap() {
		return tiledMap;
	}

}
