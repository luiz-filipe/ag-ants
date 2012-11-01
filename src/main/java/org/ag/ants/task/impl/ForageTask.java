package org.ag.ants.task.impl;

import net.jcip.annotations.ThreadSafe;

import org.ag.ants.agent.impl.AntAgent;
import org.ag.ants.env.impl.ForageStimulusType;
import org.ag.ants.task.AntTask;
import org.ag.common.agent.Agent;
import org.ag.common.env.Direction;
import org.ag.common.env.Node;
import org.ag.common.task.AbstractTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is the most used task by <em>workers</em>. It used executed when agents
 * wish to forage.
 * 
 * @author Luiz Abrahao <luiz@luizabrahao.com>
 *
 */

@ThreadSafe
public class ForageTask extends AbstractTask implements AntTask {
	private static final Logger logger = LoggerFactory.getLogger(ForageTask.class);
	public static final String NAME = "ant:task:forage";
	
	public static final double WEIGHT_NORTH = 0.40;
	public static final double WEIGHT_EAST = 0.25;
	public static final double WEIGHT_SOUTH = 0.1;
	public static final double WEIGHT_WEST = 0.25;

	public ForageTask() {
		super(ForageTask.NAME);
	}

	@Override public double getNeighbourWeightNorth() { return WEIGHT_NORTH; }
	@Override public double getNeighbourWeightEast() { return WEIGHT_EAST; }
	@Override public double getNeighbourWeightSouth() { return WEIGHT_SOUTH; }
	@Override public double getNeighbourWeightWest() { return WEIGHT_WEST; }
	
	@Override
	public void execute(Agent agent) {
		AntAgent ant = (AntAgent) agent;
		
		Direction d = AntTaskUtil.getDirectionToMoveTo(ant, ForageStimulusType.TYPE);
		Node nodeToMoveTo = agent.getCurrentNode().getNeighbour(d);
		
		if (nodeToMoveTo == null) {
			Direction newDirection = this.findRandomDirectionToMove(ant);
			nodeToMoveTo = ant.getCurrentNode().getNeighbour(newDirection);
			ant.setMovingDirection(newDirection);
		}
		
		ant.incrementStimulusIntensity(ForageStimulusType.TYPE);

		ant.addToMemory(ant.getCurrentNode());
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