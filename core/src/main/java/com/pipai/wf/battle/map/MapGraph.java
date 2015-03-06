package com.pipai.wf.battle.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Comparator;

/*
 * To be used as a disposable BattleMap representation for Dijkstra's and other pathfinding algorithms
 */

public class MapGraph {
	
	private Node root;
	private HashMap<String, Node> nodeMap;
	private ArrayList<GridPosition> reachableList;
	
	private class Node {
		private GridPosition pos;
		private ArrayList<Node> edges;
		private boolean added, visited;
		private int cost, totalCost;
		private Node path;
		
		public Node(GridPosition pos) {
			this.pos = pos;
			this.cost = 1;
			this.edges = new ArrayList<Node>();
		}
		
		public void addEdge(Node node) {
			this.edges.add(node);
		}
		
		public boolean isVisited() { return this.visited; }
		public void visit() { this.visited = true; }
		public boolean isAdded() { return this.added; }
		public void setAdded() { this.added = true; }
		public int getTotalCost() { return this.totalCost; }
		public void setTotalCost(int totalCost) { this.totalCost = totalCost; }
		public int getCost() { return this.cost; }
		public void setPath(Node from) { this.path = from; }
		public Node getPath() { return this.path; }
		public GridPosition getPosition() { return this.pos; }
		
		public void clear() {
			this.edges = new ArrayList<Node>();
			this.visited = false;
			this.totalCost = 0;
			this.path = null;
		}
		
		public ArrayList<Node> getNeighbors() {
			return this.edges;
		}
		
	}
	
	private class NodeComparator implements Comparator<Node> {
	    @Override
	    public int compare(Node x, Node y) {
	        return 0;
	    }
	}
	
	public MapGraph(BattleMap map, GridPosition start, int mobility, int jumpHeight) {
		initialize(map.getCols(), map.getRows(), start);
		runDijkstra(mobility, jumpHeight);
	}
	
	private void initialize(int width, int height, GridPosition rootPos) {
		this.nodeMap = new HashMap<String, Node>();
		for (int x=0; x<width; x++) {
			for (int y=0; y<height; y++) {
				GridPosition cellPos = new GridPosition(x, y);
				Node cell = new Node(cellPos);
				this.nodeMap.put(cellPos.toString(), cell);
				if (x > 0) {
					Node west = this.getNode(new GridPosition(x-1, y));
					west.addEdge(cell);	//Will add height-weights later
					cell.addEdge(west);
				}
				if (y > 0) {
					Node south = this.getNode(new GridPosition(x, y-1));
					south.addEdge(cell);
					cell.addEdge(south);
				}
				if (rootPos != null && x == rootPos.x && y == rootPos.y) {
					this.root = cell;
				}
			}
		}
	}
	
	private Node getNode(GridPosition pos) {
		return this.nodeMap.get(pos.toString());
	}
	
	private void runDijkstra(int mobility, int jumpHeight) {
		this.reachableList = new ArrayList<GridPosition>();
		PriorityQueue<Node> pqueue = new PriorityQueue<Node>(mobility*mobility, new NodeComparator());
		Node current = this.root;
		while (current != null) {
			this.reachableList.add(current.getPosition());
			current.visit();
			for (Node node : current.getNeighbors()) {
				if (!node.isVisited() && !node.isAdded()) {
					int totalCost = node.getCost() + current.getTotalCost();
					if (totalCost <= mobility) {
						node.setAdded();
						node.setTotalCost(totalCost);
						node.setPath(current);
						pqueue.add(node);
					}
				}
			}
			current = pqueue.poll();
		}
	}
	
	private void clearPathfinding() {
		for (GridPosition pos : this.reachableList) { 
			Node node = this.getNode(pos);
			node.clear();
		}
		this.reachableList = null;
		this.root = null;
	}
	
	public ArrayList<GridPosition> getMovableCellPositions() {
		@SuppressWarnings("unchecked")
		ArrayList<GridPosition> list = (ArrayList<GridPosition>) this.reachableList.clone();
		return list;
	}
	
	public LinkedList<GridPosition> getPath(GridPosition destinationPos) {
		if (!this.getNode(destinationPos).isVisited()) { return null; }
		LinkedList<GridPosition> pathList = new LinkedList<GridPosition>();
		Node path = this.getNode(destinationPos);
		while (path != null) {
			pathList.addFirst(path.getPosition());
			path = path.getPath();
		}
		return pathList;
	}
	
	public boolean canMoveTo(GridPosition pos) {
		return this.getNode(pos).isVisited();
	}
	
}