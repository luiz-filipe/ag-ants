package org.ag.ants.agent;

import java.util.ArrayList;
import java.util.List;

import net.jcip.annotations.ThreadSafe;

import org.ag.common.env.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class for creating populations of agents. 
 * 
 * @author Luiz Abrahao <luiz@luizabrahao.com>
 *
 */
@ThreadSafe
public class AntAgentFactory {
	// avoid any other class to extend this one.
	private AntAgentFactory() {}
	
	private static final Logger logger = LoggerFactory.getLogger(AntAgentFactory.class);
			
	/**
	 * Sometimes it is necessary to have a reasonable large number of agents to
	 * use in simulations. This method helps users to create populations of
	 * ants of the Worker type. Note that the entire population will be placed
	 * at the same node.
	 * 
	 * @param numberOfAgents The size of the population to be generated
	 * @param namePrefix Each agent will receive a id that starts with the
	 *        prefix plus a number, e.g. 'prefix-01'.
	 * @param initialNode The node the population is going to be placed at.
	 * @return List of ant agents.
	 */
	public static List<AntAgent> produceBunchOfWorkers(final int numberOfAgents, final String namePrefix, final Node initialNode) {
		List<AntAgent> agents = new ArrayList<AntAgent>();
		
		for (int i = 0; i < numberOfAgents; i++) {
			agents.add(new AntAgent(namePrefix + "-" + i, WorkerAntType.TYPE, initialNode, false));
		}
		
		logger.debug("{} agents created with prefix '{}'", agents.size(), namePrefix);
		return agents;
	}
		
	/**
	 * In cases that the population produced should be sprang across a line of
	 * the grid, this method allows the population to be initialised in chucks
	 * spaced by the parameter horizonalSpacing.
	 * 
	 * Note that if the number of agents is not a multiple of the number of
	 * agents per node, a rounded number of agents will be return.
	 * 
	 * @param numberOfAgents Size of the population
	 * @param namePrefix namePrefix Each agent will receive a id that starts
	 *        with the prefix plus a number, e.g. 'prefix-01'.
	 * @param grid Node grid
	 * @param line The index of the line of the grid that the population will
	 *        be placed at
	 * @param startColumn The index of the column that the population will
	 *        start to be placed.
	 * @param horizontalSpacing The number of nodes that separates the chucks
	 *        of the population
	 * @param numberOfAgentsPerNode Number of agents at each chuck
	 * @return List of agents.
	 */
	public static List<AntAgent> produceBunchOfWorkers(final int numberOfAgents, final String namePrefix, final Node[][] grid, final int line, 
			                                           final int startColumn, final int horizontalSpacing, final int numberOfAgentsPerNode) {
		List<AntAgent> agents = new ArrayList<AntAgent>();
		int interations = numberOfAgents / numberOfAgentsPerNode;
		int colunm = startColumn;
		int nAgentsCreated = 0;
		
		// let's check if the grid has enough columns to allocate the population
		// nOfInterations = nOfAgents / nOfAgents per node, rounded
		// nGaps = nOfInterations - 1, number of gaps of size horizontalSpacing
		// numberOfColumnsNeeded = nOfInterations + nGaps * gapSize
		//                       = nOfInterations + (nOfInterations - 1) * gapSize
		//                       = nOfInterations ( 1 + gapSize) - gapSize
		//
		// index of last column needed = startColumn + numberOfColumnsNeeded - 1;
		final int numberOfColumnsNeeded = interations * (1 + horizontalSpacing) - horizontalSpacing;
		final int lastColumnIndexNeeded = startColumn + numberOfColumnsNeeded - 1;

		try {
			grid[line][lastColumnIndexNeeded].getId();
		
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new RuntimeException("The grid has not enough cloumns to create the population! Index of last column needed =" + lastColumnIndexNeeded);
		}
		
		for (int i = 0; i < interations; i++) {
			for (int j = 0; j < numberOfAgentsPerNode; j++) {
				agents.add(new AntAgent(namePrefix + "-" + nAgentsCreated, WorkerAntType.TYPE, grid[line][colunm], false));
				nAgentsCreated++;
			}
			colunm += horizontalSpacing + 1;
		}
		
		logger.info("{} agents created with prefix '{}'", agents.size(), namePrefix);
		return agents;
	}
}
