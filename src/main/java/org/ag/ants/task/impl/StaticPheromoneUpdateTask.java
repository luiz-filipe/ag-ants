package org.ag.ants.task.impl;

import net.jcip.annotations.ThreadSafe;

import org.ag.ants.agent.impl.StaticPheromoneUpdaterAgent;
import org.ag.ants.env.ChemicalCommStimulus;
import org.ag.common.agent.Agent;
import org.ag.common.env.CommunicationStimulus;
import org.ag.common.env.Direction;
import org.ag.common.env.Node;
import org.ag.common.task.AbstractTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Starting from its current node, it triggers the pheromone update method for
 * each node than it moves to the next node (towards east) in the same line
 * until it reaches the end of that line, when that happens it moved to the
 * next line and the process is repeated until it reaches the last line of the
 * grid or the number of lines the updater has processed is the number of lines
 * to be processed defined by the agents' field numberOfLinesToProcess.
 * 
 * @author Luiz Abrahao <luiz@luizabrahao.com>
 *
 */
@ThreadSafe
public class StaticPheromoneUpdateTask extends AbstractTask {
	private static final Logger logger = LoggerFactory.getLogger(StaticPheromoneUpdateTask.class);
	public static final String NAME = "Static updater";
	
	public StaticPheromoneUpdateTask() {
		super(StaticPheromoneUpdateTask.NAME);
	}

	@Override
	public void execute(Agent agent) {
		Node initialNode = agent.getCurrentNode();
		
		logger.debug("[{}] Starting pherormone update run", agent.getId());
		final int maximumNumberOfLines = ((StaticPheromoneUpdaterAgent) agent).getNumberOfLinesToProcess();
		
		Node startNode = agent.getCurrentNode();
		Node nodeToBeUpdated = startNode;
		int numberOfRowsProcessed = 0;
		
		for (;;) {
			for (;;) {
				
				for (CommunicationStimulus stimulus : nodeToBeUpdated.getCommunicationStimuli()) {
					if (stimulus instanceof ChemicalCommStimulus) {
						((ChemicalCommStimulus) stimulus).decayIntensity();
					}
				}
				
				if (nodeToBeUpdated.getNeighbour(Direction.EAST) != null) {
					nodeToBeUpdated = nodeToBeUpdated.getNeighbour(Direction.EAST);
				
				} else {
					break;
				}
			}
			
			if (startNode.getNeighbour(Direction.SOUTH) != null) {
				startNode = startNode.getNeighbour(Direction.SOUTH);
				nodeToBeUpdated = startNode;
			
			} else {
				break;
			}
			
			numberOfRowsProcessed++;
			
			if (numberOfRowsProcessed == maximumNumberOfLines) {
				break;
			}
		}
		
		agent.setCurrentNode(initialNode);
		logger.debug("[{}] Finished pherormone update run", agent.getId());
	}
}
