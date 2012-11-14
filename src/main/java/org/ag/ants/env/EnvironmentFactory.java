package org.ag.ants.env;

import org.ag.common.env.Direction;
import org.ag.common.env.Node;

public class EnvironmentFactory {
	private EnvironmentFactory() {}

	public static NestNode[][] createNestGrid(final String nestName,
			final int nLines, final int nColumns) {
		NestNode[][] nodes = new NestNode[nLines][nColumns];

		for (int l = 0; l < nLines; l++) {
			for (int c = 0; c < nColumns; c++) {
				nodes[l][c] = new NestNode(nestName + "-" + l + "," + c);

				if (c != 0) {
					nodes[l][c].setNeighbours(Direction.WEST, nodes[l][c - 1]);
				}

				if (l != 0) {
					nodes[l][c].setNeighbours(Direction.NORTH, nodes[l - 1][c]);
				}

				if ((l != 0) && (c != 0)) {
					nodes[l][c].setNeighbours(Direction.NORTH_WEST,
							nodes[l - 1][c - 1]);
				}

				if ((l != 0) && (c != nColumns - 1)) {
					nodes[l][c].setNeighbours(Direction.NORTH_EAST,
							nodes[l - 1][c + 1]);
				}
			}
		}

		return nodes;
	}

	public static Node[][] createPheromoneGrid(final int nLines,
			final int nColumns) {
		Node[][] nodes = new Node[nLines][nColumns];

		for (int l = 0; l < nLines; l++) {
			for (int c = 0; c < nColumns; c++) {
				nodes[l][c] = new PheromoneNode("n-" + l + "," + c);

				if (c != 0) {
					nodes[l][c].setNeighbours(Direction.WEST, nodes[l][c - 1]);
				}

				if (l != 0) {
					nodes[l][c].setNeighbours(Direction.NORTH, nodes[l - 1][c]);
				}

				if ((l != 0) && (c != 0)) {
					nodes[l][c].setNeighbours(Direction.NORTH_WEST,
							nodes[l - 1][c - 1]);
				}

				if ((l != 0) && (c != nColumns - 1)) {
					nodes[l][c].setNeighbours(Direction.NORTH_EAST,
							nodes[l - 1][c + 1]);
				}
			}
		}

		return nodes;
	}
}
