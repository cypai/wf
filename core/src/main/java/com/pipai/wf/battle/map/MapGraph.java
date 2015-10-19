package com.pipai.wf.battle.map;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * To be used as a disposable BattleMap representation for Dijkstra's and other pathfinding algorithms
 */

public class MapGraph {

	private static final Logger logger = LoggerFactory.getLogger(MapGraph.class);

	private Node root;
	private HashMap<String, Node> nodeMap;
	private float[] moveBounds;
	private ArrayList<ArrayList<GridPosition>> reachableLists;
	private boolean DEBUG;
	private int ap, apmax;

	private class Edge {
		private Node destination;
		private float cost;

		public Edge(Node destination, float cost) {
			this.destination = destination;
			this.cost = cost;
		}

		public Node getDestination() {
			return destination;
		}

		public float cost() {
			return cost;
		}
	}

	private class Node {
		private GridPosition pos;
		private ArrayList<Edge> edges;
		private boolean added, visited;
		private float totalCost;
		private Node path;
		private int apNeeded;

		public Node(GridPosition pos) {
			this.pos = pos;
			this.edges = new ArrayList<Edge>();
			this.apNeeded = Integer.MAX_VALUE;
		}

		public void addEdge(Node node) {
			this.edges.add(new Edge(node, (float) pos.distance(node.pos)));
		}

		public boolean isVisited() {
			return this.visited;
		}

		public void visit() {
			this.visited = true;
		}

		public boolean isAdded() {
			return this.added;
		}

		public void setAdded() {
			this.added = true;
		}

		public float getTotalCost() {
			return this.totalCost;
		}

		public void setTotalCost(float totalCost) {
			this.totalCost = totalCost;
		}

		// public int getCost() { return this.cost; }
		public void setPath(Node from) {
			this.path = from;
		}

		public Node getPath() {
			return this.path;
		}

		public GridPosition getPosition() {
			return this.pos;
		}

		public void setAPNeeded(int ap) {
			this.apNeeded = ap;
		}

		public int getAPNeeded() {
			return this.apNeeded;
		}

		public ArrayList<Edge> getEdges() {
			return this.edges;
		}

		@Override
		public String toString() {
			String s = "Node: " + this.pos + " Edges [ ";
			for (Edge edge : this.edges) {
				Node node = edge.getDestination();
				s += "{" + node.getPosition() + " " + String.valueOf(node.isVisited()) + " " + String.valueOf(node.isAdded()) + "} ";
			}
			s += "]";
			return s;
		}
	}

	private class NodeComparator implements Comparator<Node> {
		@Override
		public int compare(Node x, Node y) {
			if (x.totalCost > y.totalCost) {
				return 1;
			} else if (x.totalCost < y.totalCost) {
				return -1;
			}
			return 0;
		}
	}

	public MapGraph(BattleMap map, GridPosition start, int mobility, int ap, int apMax) {
		this(map, start, mobility, ap, apMax, false);
	}

	public MapGraph(BattleMap map, GridPosition start, int mobility, int ap, int apMax, boolean debug) {
		DEBUG = debug;
		this.ap = ap;
		this.apmax = apMax;
		this.reachableLists = new ArrayList<ArrayList<GridPosition>>();
		if (ap > 0) {
			for (int i = 0; i < ap; i++) {
				this.reachableLists.add(new ArrayList<GridPosition>());
			}
			calcApMoveBounds(mobility, ap, apMax);
			initialize(map, start);
			runDijkstra(this.moveBounds[this.moveBounds.length - 1], ap, apMax);
		}
	}

	private void initialize(BattleMap map, GridPosition rootPos) {
		int width = map.getCols();
		int height = map.getRows();
		this.nodeMap = new HashMap<String, Node>();
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				GridPosition cellPos = new GridPosition(x, y);
				if (map.getCell(cellPos).isEmpty() || cellPos.equals(rootPos)) {
					Node cell = new Node(cellPos);
					this.nodeMap.put(cellPos.toString(), cell);
					Node west = this.getNode(new GridPosition(x - 1, y));
					if (west != null) {
						west.addEdge(cell);	// Will add height-weights later
						cell.addEdge(west);
					}
					Node south = this.getNode(new GridPosition(x, y - 1));
					if (south != null) {
						south.addEdge(cell);
						cell.addEdge(south);
					}
					Node sw = this.getNode(new GridPosition(x - 1, y - 1));
					if (sw != null) {
						sw.addEdge(cell);	// Will add height-weights later
						cell.addEdge(sw);
					}
					Node nw = this.getNode(new GridPosition(x - 1, y + 1));
					if (nw != null) {
						nw.addEdge(cell);
						cell.addEdge(nw);
					}
					if (rootPos != null && x == rootPos.x && y == rootPos.y) {
						this.root = cell;
					}
				}
			}
		}
	}

	private void calcApMoveBounds(int mobility, int ap, int apMax) {
		this.moveBounds = new float[ap];
		float mobilitySegment = ((float) mobility) / ((float) apMax);
		for (int i = 1; i <= ap; i++) {
			this.moveBounds[i - 1] = mobilitySegment * i;
		}
	}

	private Node getNode(GridPosition pos) {
		if (this.nodeMap == null) {
			return null;
		}
		return this.nodeMap.get(pos.toString());
	}

	private int apRequiredToMoveTo(Node destination) {
		float DELTA = 0.000001f;
		for (int i = 1; i <= this.moveBounds.length; i++) {
			if (destination.getTotalCost() - DELTA <= this.moveBounds[i - 1]) {
				return i;
			}
		}
		return Integer.MAX_VALUE;
	}

	private void runDijkstra(float maxMove, int ap, int apMax) {
		PriorityQueue<Node> pqueue = new PriorityQueue<>((int) (maxMove * maxMove), new NodeComparator());
		Node current = this.root;
		while (current != null) {
			if (!current.getPosition().equals(this.root.getPosition())) {
				int apNeeded = apRequiredToMoveTo(current);
				int index = apNeeded - 1;
				ArrayList<GridPosition> reachableList = null;
				reachableList = this.reachableLists.get(index);
				reachableList.add(current.getPosition());
				current.setAPNeeded(apNeeded);
			}
			current.visit();
			if (DEBUG) {
				logger.debug("Current " + current);
			}
			for (Edge edge : current.getEdges()) {
				Node node = edge.getDestination();
				if (DEBUG) {
					logger.debug("Checking " + node.getPosition());
				}
				if (!node.isVisited() && !node.isAdded()) {
					float totalCost = edge.cost() + current.getTotalCost();
					if (totalCost <= maxMove) {
						if (DEBUG) {
							logger.debug("Added " + node.getPosition() + " Dist " + String.valueOf(totalCost));
						}
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

	@SuppressWarnings("unchecked")
	public ArrayList<GridPosition> getMovableCellPositions(int ap) {
		ArrayList<GridPosition> list;
		try {
			list = this.reachableLists.get(ap - 1);
		} catch (IndexOutOfBoundsException e) {
			list = new ArrayList<GridPosition>();
		}
		return (ArrayList<GridPosition>) list.clone();
	}

	public LinkedList<GridPosition> getPath(GridPosition destinationPos) {
		if (this.getNode(destinationPos) == null || !this.getNode(destinationPos).isVisited()) {
			return new LinkedList<GridPosition>();
		}
		LinkedList<GridPosition> pathList = new LinkedList<>();
		Node path = this.getNode(destinationPos);
		while (path != null) {
			pathList.addFirst(path.getPosition());
			path = path.getPath();
		}
		return pathList;
	}

	public boolean canMoveTo(GridPosition pos) {
		return this.getNode(pos) != null && this.getNode(pos).isVisited();
	}

	public GridPosition startingPosition() {
		return root.getPosition();
	}

	public int apRequiredToMoveTo(GridPosition destination) {
		return this.getNode(destination).getAPNeeded();
	}

	public int getAP() {
		return ap;
	}

	public int getAPMax() {
		return apmax;
	}

}