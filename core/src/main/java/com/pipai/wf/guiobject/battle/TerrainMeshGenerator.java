package com.pipai.wf.guiobject.battle;

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.pipai.wf.battle.map.BattleMap;

public class TerrainMeshGenerator {

	public static Mesh generateMeshFromBattleMap(BattleMap map, int squareSize) {
		final int posComponents = 3;		// x, y, z
		final int texCoordComponents = 2;	// u, v
		final int gridPosComponents = 2;	// x, y
		final int numComponents = posComponents + texCoordComponents + gridPosComponents;
		final int numTriangles = map.getCols() * map.getRows() * 2;
		final int numVerts = numTriangles * 3;

		Mesh mesh = new Mesh(true, numVerts, 0,
				new VertexAttribute(Usage.Position, posComponents, "a_position"),
				new VertexAttribute(Usage.TextureCoordinates, texCoordComponents, "a_texCoord0"),
				new VertexAttribute(Usage.Generic, 2, "a_gridPos"));

		float[] verts = new float[numVerts * numComponents];

		int index = 0;
		for (int y = 0; y < map.getRows(); y++) {
			for (int x = 0; x < map.getCols(); x++) {
				// Bottom left
				verts[index++] = squareSize * x;
				verts[index++] = squareSize * y;
				verts[index++] = 0;
				verts[index++] = 0;
				verts[index++] = 0;
				verts[index++] = x;
				verts[index++] = y;
				// Top left
				verts[index++] = squareSize * x;
				verts[index++] = squareSize * (y + 1);
				verts[index++] = 0;
				verts[index++] = 0;
				verts[index++] = 1;
				verts[index++] = x;
				verts[index++] = y;
				// Top right
				verts[index++] = squareSize * (x + 1);
				verts[index++] = squareSize * (y + 1);
				verts[index++] = 0;
				verts[index++] = 1;
				verts[index++] = 1;
				verts[index++] = x;
				verts[index++] = y;
				// Bottom left
				verts[index++] = squareSize * x;
				verts[index++] = squareSize * y;
				verts[index++] = 0;
				verts[index++] = 0;
				verts[index++] = 0;
				verts[index++] = x;
				verts[index++] = y;
				// Top right
				verts[index++] = squareSize * (x + 1);
				verts[index++] = squareSize * (y + 1);
				verts[index++] = 0;
				verts[index++] = 1;
				verts[index++] = 1;
				verts[index++] = x;
				verts[index++] = y;
				// Bottom right
				verts[index++] = squareSize * (x + 1);
				verts[index++] = squareSize * y;
				verts[index++] = 0;
				verts[index++] = 1;
				verts[index++] = 0;
				verts[index++] = x;
				verts[index++] = y;
			}
		}
		mesh.setVertices(verts);
		return mesh;
	}

}
