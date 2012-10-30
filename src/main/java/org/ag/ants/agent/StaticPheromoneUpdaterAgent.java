package org.ag.ants.agent;

import org.ag.common.agent.TaskAgent;
import org.ag.common.env.Node;

import net.jcip.annotations.ThreadSafe;

/**
 * This agent sweeps a grid, starting from its current node, it triggers the
 * pheromone update method for each node than it moves to the next node
 * (towards east) in the same line until it reaches the end of that line, when
 * that happens it moved to the next line and the process is repeated until it
 * reaches the last line of the grid or the number of lines the updater has
 * processed is the number of lines to be processed defined by the variable 
 * numberOfLinesToProcess.
 * 
 * The reason to have the numberOfLinesToProcess is that in that way we can
 * create more than one updater for the same grid, and assign slices of the
 * grid for each updater.
 * 
 * @author Luiz Abrahao <luiz@luizabrahao.com>
 *
 */
@ThreadSafe
public class StaticPheromoneUpdaterAgent extends TaskAgent {
	private final int numberOfLinesToProcess;
	
	public StaticPheromoneUpdaterAgent(String id, Node currentNode, int numberOfLinesToProcess) {
		super(id, StaticPheromoneUpdaterAgentType.TYPE, currentNode, false);
		
		this.numberOfLinesToProcess = numberOfLinesToProcess;
	}

	public int getNumberOfLinesToProcess() { return numberOfLinesToProcess; }

	@Override
	public Void call() throws Exception {
		this.getTaskList().get(0).execute(this);
		
		return null;
	}
}
