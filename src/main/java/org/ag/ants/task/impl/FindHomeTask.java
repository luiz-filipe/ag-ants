package org.ag.ants.task.impl;

import net.jcip.annotations.ThreadSafe;

import org.ag.ants.agent.impl.AntAgent;
import org.ag.ants.env.NestNode;
import org.ag.ants.env.impl.ForageStimulusType;
import org.ag.ants.task.AntTask;
import org.ag.common.agent.Agent;
import org.ag.common.env.Direction;
import org.ag.common.env.Node;
import org.ag.common.task.AbstractTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Ants execute this task after collecting food. The main objective of this task
 * is to take the agent back to their nest.
 * 
 * @author Luiz Abrahao <luiz@luizabrahao.com>
 *
 */
@ThreadSafe
public class FindHomeTask extends AbstractTask implements AntTask {
	private static final Logger logger = LoggerFactory.getLogger(FindHomeTask.class);
	public static final String NAME = "ant:task:find-home";

	public static final double WEIGHT_NORTH = 0.40;
	public static final double WEIGHT_EAST = 0.25;
	public static final double WEIGHT_SOUTH = 0.10;
	public static final double WEIGHT_WEST = 0.25;

	public FindHomeTask() {
		super(FindHomeTask.NAME);
	}

	@Override public double getNeighbourWeightNorth() { return WEIGHT_NORTH; }
	@Override public double getNeighbourWeightEast() { return WEIGHT_EAST; }
	@Override public double getNeighbourWeightSouth() { return WEIGHT_SOUTH; }
	@Override public double getNeighbourWeightWest() { return WEIGHT_WEST; }

	private boolean isAntInNest(Node currentNode) {
		if (currentNode instanceof NestNode) {
			return true;
		}
		
		return false;
	}
	

	@Override
	public void execute(Agent agent) {
		AntAgent ant = (AntAgent) agent;
		final boolean inNest = this.isAntInNest(agent.getCurrentNode());
		
		if (inNest) {
			// The agent has reached nest... Do something...
			logger.debug("{} deposited food in the nest", agent.getId());
			ant.depositFood((NestNode) ant.getCurrentNode());
			ant.invertDirection();
			
			return;
		}
		
		Node nodeToMoveTo = ant.getLatestNodeFromMemory();
		
		// if the agent runs out of memory
		if (nodeToMoveTo == null) {
			Direction d = AntTaskUtil.getDirectionToMoveTo(ant, ForageStimulusType.TYPE);
			nodeToMoveTo = agent.getCurrentNode().getNeighbour(d);
			
			if (nodeToMoveTo == null) {
				Direction newDirection = this.findRandomDirectionToMove(ant);
				nodeToMoveTo = ant.getCurrentNode().getNeighbour(newDirection);
				ant.setMovingDirection(newDirection);
			}
		}
		
		ant.incrementStimulusIntensityMultipliedByFactor(ForageStimulusType.TYPE, 2);
		nodeToMoveTo.addAgent(agent);
	}
	
	private Direction findRandomDirectionToMove(AntAgent agent) {
		Direction d = AntTaskUtil.getDirectionToMoveTo(agent, ForageStimulusType.TYPE);
		
		Node n = agent.getCurrentNode().getNeighbour(d);
		
		if (n == null) {
			return findRandomDirectionToMove(agent);
		}
		
		logger.debug("{} has changed its direction to {}", agent.getId(), d);
		
		return d;
	}
}
