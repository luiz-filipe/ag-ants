package org.ag.ants.simulation;

import java.util.ArrayList;
import java.util.List;

import org.ag.ants.env.AntNest;
import org.ag.ants.env.EnvironmentFactory;
import org.ag.ants.env.NestNode;
import org.ag.common.agent.Agent;
import org.ag.common.env.Node;
import org.ag.common.simulation.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AntEnvironment implements Environment {
	private static final Logger logger = LoggerFactory
			.getLogger(AntEnvironment.class);

	private final Node[][] env;
	private final int nLines;
	private final int nColumns;
	private final List<AntNest> nests;

	@Override
	public int getNumberOfLines() {
		return nLines;
	}

	@Override
	public int getNumberOfColumns() {
		return nColumns;
	}

	public AntEnvironment(final int nLines, final int nColumns) {
		this.nLines = nLines;
		this.nColumns = nColumns;
		this.nests = new ArrayList<AntNest>();

		this.env = EnvironmentFactory.createPheromoneGrid(nLines, nColumns);
	}

	@Override
	public void placeAgentAt(final Agent agent, final int line, final int column) {
		throw new RuntimeException("Instead of adding agents directly to the "
				+ "environment you should add ant nests...");
	}

	@Override
	public void placeAgentAtTheMiddle(final Agent agent) {
		throw new RuntimeException("Instead of adding agents directly to the "
				+ "environment you should add ant nests...");

	}

	@Override
	public Node getNodeAt(final int line, final int column) {
		if ((line < 0) || (column < 0)) {
			logger.error("Cannot return node. line and column parameters"
					+ "must be greater or equal to 0.");

			return null;
		}

		if (line > nLines - 1) {
			logger.error("Cannot return node. The maximum allowed value for"
					+ " line is: {}", nLines - 1);

			return null;
		}

		if (column > nColumns - 1) {
			logger.error("Cannot return node. The maximum allowed value for "
					+ "column is: {}", nColumns - 1);

			return null;
		}

		return env[line][column];
	}

	public void createNestAt(final String id, final int line, final int column,
			final int nLines, final int nColumns) {
		
		// test if the new node would overlap an existent node
		for (int l = 0; l < nLines; l++) {
			for (int c = 0; c < nColumns; c++) {
				 if (env[line + l][column + c] instanceof NestNode) {
					 logger.error("Could not add nest [{}]. Specified " +
					 		"position overlaps already existing nest.", id);
					 
					 return;
				 }
			}
		}
		
		AntNest nest = new AntNest(id, nLines, nColumns);
		nest.connectToNeighbours(env[line][column]);
		
		// replace old nodes in the grid by the nest nodes
		for (int l = 0; l < nLines; l++) {
			for (int c = 0; c < nColumns; c++) {
				// this null is just to help the garbage collector...
				env[line + l][column + c] = null;
				env[line + l][column + c] = nest.getNode(l, c);
			}
		}
		
		nests.add(nest);
	}
}
