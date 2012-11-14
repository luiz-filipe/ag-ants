package org.ag.ants.env;

import org.ag.common.env.Direction;
import org.ag.common.env.Node;
/**
 * An AntNest is composed by an array of nestNode objects that represent an ant
 * nest.
 * 
 * @author Luiz Abrahao <luiz@luizabrahao.com>
 *
 */
public class AntNest {
	private final String id;
	private final NestNode[][] nestNodes;
	private final int nLines;
	private final int nColumns;

	public AntNest(final String id, final int nLines, final int nColumns) {
		this.id = id;
		this.nLines = nLines;
		this.nColumns = nColumns;

		nestNodes = EnvironmentFactory.createNestGrid(id, nLines, nColumns);
	}

	public String getId() {
		return id;
	}

	public NestNode getNode(final int line, final int column) {
		if ((line >= 0) && (line <= nLines - 1)) {
			if ((column >= 0) && (column <= nColumns - 1)) {
				return nestNodes[line][column];
			}
		}

		return null;
	}
	
	/**
	 * Create the connection between the nest nodes from the ant nest and the
	 * other nodes of the environment. The starting point for the nest placement
	 * is the initialNode parameter, which determines where the most top-left
	 * node of the nest will be located.
	 * 
	 * @param initialNode
	 *            The node that will be replaced by the top-left node from the
	 *            nest.
	 */
	public void connectToNeighbours(final Node initialNode) {
		Node initialLineNode = initialNode;
		Node currentNode = initialNode;

		for (int l = 0; l < nLines; l++) {
			for (int c = 0; c < nColumns; c++) {
				// first column updates
				if (c == 0) {
					if (l == 0) {
						nestNodes[l][c].setNeighbours(Direction.NORTH,
								currentNode.getNeighbour(Direction.NORTH));
						nestNodes[l][c].setNeighbours(Direction.WEST,
								currentNode.getNeighbour(Direction.WEST));
						nestNodes[l][c].setNeighbours(Direction.NORTH_WEST,
								currentNode.getNeighbour(Direction.NORTH_WEST));
					}

					if ((l > 0) && (l < nLines - 1)) {
						nestNodes[l][c].setNeighbours(Direction.WEST,
								currentNode.getNeighbour(Direction.WEST));
					}

					if (l == nLines - 1) {
						nestNodes[l][c].setNeighbours(Direction.SOUTH,
								currentNode.getNeighbour(Direction.SOUTH));
						nestNodes[l][c].setNeighbours(Direction.SOUTH_WEST,
								currentNode.getNeighbour(Direction.SOUTH_WEST));
						nestNodes[l][c].setNeighbours(Direction.WEST,
								currentNode.getNeighbour(Direction.WEST));
					}

					currentNode = currentNode.getNeighbour(Direction.EAST);
				}

				// middle columns updates
				if ((c > 0) && c < nColumns - 1) {
					if (l == 0) {
						nestNodes[l][c].setNeighbours(Direction.NORTH,
								currentNode.getNeighbour(Direction.NORTH));
					}

					if (l == nLines - 1) {
						nestNodes[l][c].setNeighbours(Direction.SOUTH,
								currentNode.getNeighbour(Direction.SOUTH));
					}

					currentNode = currentNode.getNeighbour(Direction.EAST);
				}

				// last column updates
				if (c == nColumns - 1) {
					if (l == 0) {
						nestNodes[l][c].setNeighbours(Direction.NORTH,
								currentNode.getNeighbour(Direction.NORTH));
						nestNodes[l][c].setNeighbours(Direction.NORTH_EAST,
								currentNode.getNeighbour(Direction.NORTH_EAST));
						nestNodes[l][c].setNeighbours(Direction.EAST,
								currentNode.getNeighbour(Direction.EAST));
					}

					if ((l > 0) && (l < nLines - 1)) {
						nestNodes[l][c].setNeighbours(Direction.EAST,
								currentNode.getNeighbour(Direction.EAST));
					}

					if (l == nLines - 1) {
						nestNodes[l][c].setNeighbours(Direction.EAST,
								currentNode.getNeighbour(Direction.EAST));
						nestNodes[l][c].setNeighbours(Direction.SOUTH_EAST,
								currentNode.getNeighbour(Direction.SOUTH_EAST));
						nestNodes[l][c].setNeighbours(Direction.SOUTH,
								currentNode.getNeighbour(Direction.SOUTH));
					}

					initialLineNode = initialLineNode
							.getNeighbour(Direction.SOUTH);
					currentNode = initialLineNode;
				}
			}
		}
	}

	@Override
	public final int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + nColumns;
		result = prime * result + nLines;
		return result;
	}

	@Override
	public final boolean equals(Object obj) {
		if (!(obj instanceof AntNest)) {
			return false;
		}

		AntNest other = (AntNest) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (nColumns != other.nColumns)
			return false;
		if (nLines != other.nLines)
			return false;
		return true;
	}
}
