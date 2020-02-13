package com.berbils.game.Tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

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
		
		if (!navigationGrid.contains(start) || !navigationGrid.contains(goal)) {
			// Start or end node is not navigable or does not exist.
			return null;
		}
		
		// Nodes that have been visited, but not expanded
		TreeMap<Vector2, Integer> openNodes = new TreeMap<Vector2, Integer>();
		// Add first node
		openNodes.put(start, (int) 0);
		
		// Nodes that have been visited and expanded
		TreeMap<Vector2, Integer> closeNodes = new TreeMap<Vector2, Integer>();
		
		// Record of parent nodes
		HashMap<Vector2, Vector2> parentOf = new HashMap<Vector2, Vector2>();
		
		// List to return
		ArrayList<Vector2> path = null;
		path.add(start);
		
		// Find path
		while (!openNodes.isEmpty()) {
			
			// Get closest neighbour as current node
			
			Vector2 currentNode = openNodes.firstEntry().getKey();
			
			// If goal node closest, solution found
			if (currentNode == goal) {
				while (parentOf.get(currentNode) != null) {
					path.add(currentNode);
					currentNode = parentOf.get(currentNode);
				}
			}
			
			// Explore successor nodes
			for (Vector2 childNode : getNeighbourNodes(currentNode)) {
				if (openNodes.containsKey(childNode)) {
					// If child is in the open list
					if (openNodes.get(childNode) > openNodes.get(currentNode) + 1) {
						// Change child cost if shorter
						openNodes.replace(childNode, openNodes.get(currentNode) + 1);
						parentOf.put(childNode, currentNode);
					}
				} else if (closeNodes.containsKey(childNode)) {
					// If child in closed list
					if (closeNodes.get(childNode) > closeNodes.get(currentNode) + 1) {
						// Change child cost if shorter
						closeNodes.replace(childNode, closeNodes.get(currentNode) + 1);
						parentOf.put(childNode, currentNode);
					}
				} else {
					// Add successor to open list
					openNodes.put(childNode, openNodes.get(currentNode));
					parentOf.put(childNode, currentNode);
				}
			}
			// Add current node to closed list
			closeNodes.put(currentNode, openNodes.get(currentNode));
		}
		
		return path;
	}
	
}
