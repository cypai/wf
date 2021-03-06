package com.pipai.wf.battle.vision;

public final class FogOfWar {

	// public static final Color NEVER_SEEN_COLOR = Color.BLACK;
	// public static final Color SEEN_COLOR = Color.GRAY;
	// public static final Color VISIBLE_COLOR = Color.WHITE;
	//
	// private Texture fogOfWarStateTexture;
	// private Pixmap visibilityPixmap;
	// private BattleMap map;
	// private HashSet<GridPosition> visibleTiles;
	// private HashMap<AgentGuiObject, ArrayList<GridPosition>> agentVisibleTiles;
	// private List<AgentGuiObject> agents;
	// private BattleConfiguration config;
	//
	// public FogOfWar(BattleMap map, List<AgentGuiObject> agents) {
	// this(map, agents, new BattleConfiguration());
	// }
	//
	// public FogOfWar(BattleMap map, List<AgentGuiObject> agents, BattleConfiguration config) {
	// this.config = config;
	// visibleTiles = new HashSet<GridPosition>();
	// agentVisibleTiles = new HashMap<AgentGuiObject, ArrayList<GridPosition>>();
	// for (AgentGuiObject a : agents) {
	// agentVisibleTiles.put(a, new ArrayList<GridPosition>());
	// }
	// this.map = map;
	// this.agents = new ArrayList<AgentGuiObject>(agents);
	// visibilityPixmap = new Pixmap(map.getCols(), map.getRows(), Format.RGBA8888);
	// fullScan();
	// }
	//
	// public Texture getFogOfWarStateTexture() {
	// return fogOfWarStateTexture;
	// }
	//
	// public Set<GridPosition> visibleTiles() {
	// return visibleTiles;
	// }
	//
	// public void setVisible(GridPosition tile) {
	// visibleTiles.add(tile);
	// visibilityPixmap.setColor(VISIBLE_COLOR);
	// visibilityPixmap.drawPixel(tile.getX(), tile.getY());
	// }
	//
	// public void setNotVisible(GridPosition tile) {
	// visibleTiles.remove(tile);
	// visibilityPixmap.setColor(SEEN_COLOR);
	// visibilityPixmap.drawPixel(tile.getX(), tile.getY());
	// }
	//
	// public void fullScan() {
	// visibleTiles.clear();
	// if (fogOfWarStateTexture != null) {
	// fogOfWarStateTexture.dispose();
	// }
	// visibilityPixmap.setColor(NEVER_SEEN_COLOR);
	// visibilityPixmap.fill();
	// visibilityPixmap.setColor(VISIBLE_COLOR);
	//
	// for (AgentGuiObject a : agents) {
	// GridPosition pos = a.getDisplayPosition();
	// visibilityPixmap.fillCircle(pos.getX(), pos.getY(), config.sightRange());
	// // spiralPathScan(a);
	// }
	// fogOfWarStateTexture = new Texture(visibilityPixmap);
	// }
	//
	// public void update(AgentGuiObject changedAgent) {
	// fullScan();
	// }
	//
	// /**
	// * TODO: Will be finished later
	// */
	// @SuppressWarnings("unused")
	// private void spiralPathScan(AgentGuiObject a) {
	// agentVisibleTiles.get(a).clear();
	// GridPosition center = a.getDisplayPosition();
	// LinkedList<GridPosition> queue = new LinkedList<>();
	// initializeSpiralPathQueue(queue, center);
	// while (queue.size() > 0) {
	// GridPosition tile = queue.poll();
	// setVisible(tile);
	// agentVisibleTiles.get(a).add(tile);
	// BattleMapCell cell = map.getCell(tile);
	// if (cell != null && !cell.hasTileSightBlocker() && center.distance(tile) < config.sightRange()) {
	// passLight(queue, center, tile);
	// }
	// }
	// }
	//
	// private static void initializeSpiralPathQueue(LinkedList<GridPosition> queue, GridPosition center) {
	// queue.add(new GridPosition(center.getX() + 1, center.getY()));
	// queue.add(new GridPosition(center.getX(), center.getY() + 1));
	// queue.add(new GridPosition(center.getX() - 1, center.getY()));
	// queue.add(new GridPosition(center.getX(), center.getY() - 1));
	// }
	//
	// private static void passLight(LinkedList<GridPosition> queue, GridPosition center, GridPosition tile) {
	// Direction dirToTile = center.directionTo(tile);
	// switch (dirToTile) {
	// case E:
	// queue.add(new GridPosition(tile.getX(), tile.getY() - 1));
	// queue.add(new GridPosition(tile.getX() + 1, tile.getY()));
	// queue.add(new GridPosition(tile.getX(), tile.getY() + 1));
	// break;
	// case NE:
	// queue.add(new GridPosition(tile.getX() + 1, tile.getY()));
	// queue.add(new GridPosition(tile.getX(), tile.getY() + 1));
	// break;
	// case N:
	// queue.add(new GridPosition(tile.getX() + 1, tile.getY()));
	// queue.add(new GridPosition(tile.getX(), tile.getY() + 1));
	// queue.add(new GridPosition(tile.getX() - 1, tile.getY()));
	// break;
	// case NW:
	// queue.add(new GridPosition(tile.getX(), tile.getY() + 1));
	// queue.add(new GridPosition(tile.getX() - 1, tile.getY()));
	// break;
	// case W:
	// queue.add(new GridPosition(tile.getX(), tile.getY() + 1));
	// queue.add(new GridPosition(tile.getX() - 1, tile.getY()));
	// queue.add(new GridPosition(tile.getX(), tile.getY() - 1));
	// break;
	// case SW:
	// queue.add(new GridPosition(tile.getX() - 1, tile.getY()));
	// queue.add(new GridPosition(tile.getX(), tile.getY() - 1));
	// break;
	// case S:
	// queue.add(new GridPosition(tile.getX() - 1, tile.getY()));
	// queue.add(new GridPosition(tile.getX(), tile.getY() - 1));
	// queue.add(new GridPosition(tile.getX() + 1, tile.getY()));
	// break;
	// case SE:
	// queue.add(new GridPosition(tile.getX(), tile.getY() - 1));
	// queue.add(new GridPosition(tile.getX() + 1, tile.getY()));
	// break;
	// default:
	// throw new IllegalStateException("Received unexpected direction for the direction to tile: " + dirToTile);
	// }
	// }

}
