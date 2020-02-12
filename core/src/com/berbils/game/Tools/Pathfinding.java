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
	
	private ArrayList<Vector2> getNeighbourNodes(Vector2 currentNode) {	
		// Create neighbours list
		ArrayList<Vector2> neighbourNodes = null;
		
		// Calculate possible neighbouring nodes
		Vector2 leftNode = new Vector2(currentNode.x - 1, currentNode.y);
		Vector2 upNode = new Vector2(currentNode.x, currentNode.y + 1);
		Vector2 rightNode = new Vector2(currentNode.x + 1, currentNode.y);
		Vector2 downNode = new Vector2(currentNode.x, currentNode.y - 1);
		
		// Check if nodes exist
		if (navigationGrid.contains(leftNode)) {
			neighbourNodes.add(leftNode);
		}
		if (navigationGrid.contains(upNode)) {
			neighbourNodes.add(upNode);
		}
		if (navigationGrid.contains(rightNode)) {
			neighbourNodes.add(rightNode);
		}
		if (navigationGrid.contains(downNode)) {
			neighbourNodes.add(downNode);
		}
		
		return neighbourNodes;
		
	}
	
	public ArrayList<Vector2> find(Vector2 start, Vector2 goal) {
		// Nodes that have been visited, but not expanded
		ArrayList<Vector2> openNodes = null;
		// Add first node
		openNodes.add(start);
		// Nodes that have been visited and expanded
		ArrayList<Vector2> closeNodes = null;
		
		if (!navigationGrid.contains(start) || !navigationGrid.contains(goal)) {
			// Start or end node is not navigable or does not exist.
			return null;
		}
		
		// Find path
		while (!openNodes.isEmpty()) {
			
			// Get closest neighbour as current node
			Vector2 currentNode = openNodes.get(0);
			
			// If goal node closest, solution found
			if (currentNode == goal) {
				break;
			}
			
			// Explore successor nodes
			for (Vector2 childNode : getNeighbourNodes(currentNode)) {
				
				if (openNodes.contains(childNode)) {
					// Child nodes of this node not yet expanded
					
				} else if (closeNodes.contains(childNode)) {
					// If node expanded, see if shorter route found
					
				}
				
				
			}
			
		}
		
		return navigationGrid;
	}

}
