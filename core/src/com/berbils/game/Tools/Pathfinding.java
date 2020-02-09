package com.berbils.game.Tools;

import java.util.ArrayList;

import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;

public class Pathfinding {
	
	private ArrayList<Vector2> navigationGrid;
	
	/**
	 * Constructor for pathfinding.
	 * Given a tiled map, the navigable tiles are identified and stored in a format accessible to the pathfinder.
	 * 
	 * @param map	The map containing a 'navigation' layer used for identifying navigable tiles.
	 */
	public Pathfinding(TiledMap map) {
		// Get defined navigation layer from tiled map
		TiledMapTileLayer mapNavLayer = (TiledMapTileLayer) map.getLayers().get("navigation");
		int mapWidth = map.getProperties().get("width", Integer.class);
		int mapHeight = map.getProperties().get("height", Integer.class);
		
		// Iterate through all tiles
		for (int x = 0; x < mapWidth; x++) {
			for (int y = 0; y < mapHeight; y++) {
				// Working with individual tile
				if (mapNavLayer.getCell(x, y).getTile().getProperties().get("walkable").equals(true)) {
					// Tile is walkable
					navigationGrid.add(new Vector2(x, y));
				}			
			}
		}
	}
	
	public ArrayList<Vector2> find(Vector2 start, Vector2 goal) {
		
		if (!navigationGrid.contains(start) || !navigationGrid.contains(goal)) {
			// Start or end node is not navigable or does not exist.
			return null;
		}
		
		// Pathfind
		
		return ArrayList<Vector2>;
	}

}
