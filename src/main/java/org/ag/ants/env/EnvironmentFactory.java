package org.ag.ants.env;

import java.awt.Dimension;

import org.ag.common.env.Direction;
import org.ag.common.env.Node;

//TODO document
public class EnvironmentFactory {
	private EnvironmentFactory() {
	}

	public static NestNode[][] createNestGrid(final String nestName,
			Dimension dimension) {
		NestNode[][] nodes = new NestNode[dimension.height][dimension.width];

		for (int l = 0; l < dimension.height; l++) {
			for (int c = 0; c < dimension.width; c++) {
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

				if ((l != 0) && (c != dimension.width - 1)) {
					nodes[l][c].setNeighbours(Direction.NORTH_EAST,
							nodes[l - 1][c + 1]);
				}
			}
		}

		return nodes;
	}

	public static Node[][] createPheromoneGrid(final Dimension dimension) {
		Node[][] nodes = new Node[dimension.height][dimension.width];

		for (int l = 0; l < dimension.height; l++) {
			for (int c = 0; c < dimension.width; c++) {
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

				if ((l != 0) && (c != dimension.width - 1)) {
					nodes[l][c].setNeighbours(Direction.NORTH_EAST,
							nodes[l - 1][c + 1]);
				}
			}
		}

		return nodes;
	}

	public static FoodSourceNode[][] createFoodSourceGrid(final String name,
			final Dimension dimension, final double totalAmountOfFood) {

		final FoodSourceNode[][] nodes = new FoodSourceNode[dimension.height][dimension.width];
		final double amountInEachNode = totalAmountOfFood
				/ (dimension.height * dimension.width);

		for (int l = 0; l < dimension.height; l++) {
			for (int c = 0; c < dimension.width; c++) {
				nodes[l][c] = new FoodSourceNode(name + "-" + l + "," + c,
						amountInEachNode);

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

				if ((l != 0) && (c != dimension.width - 1)) {
					nodes[l][c].setNeighbours(Direction.NORTH_EAST,
							nodes[l - 1][c + 1]);
				}
			}
		}

		return nodes;
	}
}
