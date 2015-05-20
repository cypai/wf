package com.pipai.wf.battle.map;

import java.util.HashMap;
import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.agent.AgentState;
import com.pipai.wf.battle.log.BattleEventLoggable;
import com.pipai.wf.battle.log.BattleLog;

public class BattleMap implements BattleEventLoggable {

	private int m, n;	// Size of the map
	private HashMap<String, BattleMapCell> cellMap;
	private ArrayList<Agent> agents;
	private BattleLog log;

	public BattleMap(int m, int n) {
		this.agents = new ArrayList<Agent>();
		this.log = null;
		initializeMap(m, n);
	}

	public BattleMap(MapString mapString) {
		this.agents = new ArrayList<Agent>();
		this.m = mapString.getRows();
		this.n = mapString.getCols();
		initializeMap(this.m, this.n);
		for (GridPosition pos : mapString.getSolidPositions()) {
			this.getCell(pos).setTileEnvironmentObject(new FullCoverIndestructibleObject());
		}
		for (AgentState state : mapString.getAgentStates()) {
			this.addAgent(state);
		}
	}

	/*
	 * Creates an empty map of size m x n
	 */
	private void initializeMap(int m, int n) {
		this.m = m;
		this.n = n;

		this.cellMap = new HashMap<String, BattleMapCell>();
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				GridPosition cellPos = new GridPosition(i, j);
				BattleMapCell cell = new BattleMapCell(cellPos);
				this.cellMap.put(this.coordinatesToKey(cellPos), cell);
				if (i > 0) {
					BattleMapCell west = this.getCell(new GridPosition(i-1, j));
					west.setNeighbor(cell, Direction.E);
					cell.setNeighbor(west, Direction.W);
				}
				if (j > 0) {
					BattleMapCell south = this.getCell(new GridPosition(i, j-1));
					south.setNeighbor(cell, Direction.N);
					cell.setNeighbor(south, Direction.S);
				}
			}
		}
	}

	public String coordinatesToKey(GridPosition pos) {
		return pos.toString();
	}

	public BattleMapCell getCell(GridPosition pos) {
		return this.cellMap.get(this.coordinatesToKey(pos));
	}
	
	public BattleMapCell getCellInDirection(GridPosition pos, Direction d) {
		GridPosition cellPos = null;
		switch (d) {
		case W:
			cellPos = new GridPosition(pos.x-1, pos.y);
			break;
		case E:
			cellPos = new GridPosition(pos.x+1, pos.y);
			break;
		case N:
			cellPos = new GridPosition(pos.x, pos.y+1);
			break;
		case S:
			cellPos = new GridPosition(pos.x, pos.y-1);
			break;
		} 
		return this.cellMap.get(this.coordinatesToKey(cellPos));
	}

	public Agent getAgentAtPos(GridPosition pos) {
		return this.getCell(pos).getAgent();
	}

	public void addAgent(AgentState state) {
		Agent agent = new Agent(this, state);
		this.getCell(agent.getPosition()).setAgent(agent);
		agent.register(log);
		this.agents.add(agent);
	}

	public ArrayList<Agent> getAgents() {
		return this.agents;
	}

	public int getRows() { return this.m; }
	public int getCols() { return this.n; }

	public MapString getMapString() {
		return new MapString(this);
	}

	private Vector2 gridCenter(GridPosition pos) {
		return new Vector2(pos.x+0.5f, pos.y+0.5f);
	}

	public ArrayList<GridPosition> supercover(GridPosition a, GridPosition b) {
		return supercover(gridCenter(a), gridCenter(b));
	}

	private ArrayList<GridPosition> supercover(Vector2 a, Vector2 b) {
		ArrayList<GridPosition> points = new ArrayList<GridPosition>();
		int i;               // loop counter 
		int ystep, xstep;    // the step on y and x axis 
		int error;           // the error accumulated during the increment 
		int errorprev;       // *vision the previous value of the error variable 
		int y = (int)a.y, x = (int)a.x;  // the line points 
		int ddy, ddx;        // compulsory variables: the double values of dy and dx 
		int dx = (int)b.x - (int)a.x; 
		int dy = (int)b.y - (int)a.y; 
		// NB the last point can't be here, because of its previous point (which has to be verified) 
		if (dy < 0){ 
			ystep = -1; 
			dy = -dy; 
		}else 
			ystep = 1; 
		if (dx < 0){ 
			xstep = -1; 
			dx = -dx; 
		}else 
			xstep = 1; 
		ddy = 2 * dy;  // work with double values for full precision 
		ddx = 2 * dx; 
		if (ddx >= ddy){  // first octant (0 <= slope <= 1) 
			// compulsory initialization (even for errorprev, needed when dx==dy) 
			errorprev = error = dx;  // start in the middle of the square 
			for (i=0 ; i < dx ; i++){  // do not use the first point (already done) 
				x += xstep; 
				error += ddy; 
				if (error > ddx){  // increment y if AFTER the middle ( > ) 
					y += ystep; 
					error -= ddx; 
					// three cases (octant == right->right-top for directions below): 
					if (error + errorprev < ddx)  // bottom square also 
						points.add(new GridPosition(x, y-ystep)); 
					else if (error + errorprev > ddx)  // left square also 
						points.add(new GridPosition(x-xstep, y)); 
					/*else{  // corner: bottom and left squares also 
						points.add(new GridPosition(x, y-ystep)); 
						points.add(new GridPosition(x-xstep, y)); 
					} */
				} 
				points.add(new GridPosition(x, y)); 
				errorprev = error; 
			}
		} else {  // the same as above 
			errorprev = error = dy; 
			for (i=0 ; i < dy ; i++){ 
				y += ystep; 
				error += ddx; 
				if (error > ddy){ 
					x += xstep; 
					error -= ddy; 
					if (error + errorprev < ddy) 
						points.add(new GridPosition(x-xstep, y)); 
					else if (error + errorprev > ddy) 
						points.add(new GridPosition(x, y-ystep)); 
					/*else{ 
						points.add(new GridPosition(x-xstep, y)); 
						points.add(new GridPosition(x, y-ystep)); 
					} */
				} 
				points.add(new GridPosition(x, y)); 
				errorprev = error; 
			}
		}
		points.remove(new GridPosition((int)b.x, (int)b.y));
		return points;
	}

	public boolean checkTileSightBlockersInList(ArrayList<GridPosition> list) {
		for (GridPosition pos : list) {
			if (getCell(pos).hasTileSightBlocker()) {
				return false;
			}
		}
		return true;
	}
	/*
	 * Using the centers/corners to determine line of sight between two GridPositions
	 */
	public boolean lineOfSight(GridPosition a, GridPosition b) {
		return checkTileSightBlockersInList(supercover(gridCenter(a), gridCenter(b)));
	}

	@Override
	public void register(BattleLog log) {
		this.log = log;
		for (Agent a : agents) {
			a.register(log);
		}
	}
	
	
}