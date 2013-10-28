package org.ag.ants.agent.impl;

import org.ag.ants.env.FoodSourceNode;
import org.ag.ants.task.FindHomeNestTask;
import org.ag.ants.task.ForageTask;
import org.ag.common.env.Direction;
import org.ag.common.env.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WorkerAntAgent extends AntAgent {
	private static final Logger logger = LoggerFactory
			.getLogger(WorkerAntAgent.class);
	public WorkerAntAgent(final String id, final Node currentNode,
			final boolean recordNodeHistory) {
		
		super(id, WorkerAntType.TYPE, currentNode, recordNodeHistory);
	}

	@Override
	public Void call() throws Exception {
		while (!Thread.currentThread().isInterrupted()) {
			if (!this.isCarryingFood()) {
				logger.trace("{} is not carrying food, so ForageTask will be executed...", this.getId());
				this.getTaskByName(ForageTask.NAME).execute(this);
				
				// if the agent is already in a food source let it pick-up some
				// of it.
				if (this.getCurrentNode() instanceof FoodSourceNode) {
					logger.trace("{} collected food.", this.getId());
					this.collectFood();
				}
				
				// if the agent is not in a food source, it should look at the
				// environment around it, checking if there is food around.
				final Direction directionToFood = this.isThereFoodAround();
				
				if (directionToFood != null) {
					this.moveToNeighbour(directionToFood);
					logger.trace("{} collected food.", this.getId());
					this.collectFood();
				}
			
			} else {
				logger.trace("{} is carrying food, so FindHomeNestTask will be executed...", this.getId());
				this.getTaskByName(FindHomeNestTask.NAME).execute(this);
				
				if (this.isInHomeNest()) {
					logger.trace("{} deposited food.", this.getId());
					this.depositFood();
				}
			}
			
			try {
				Thread.sleep(this.getAgentType().getMilisecondsToWait());
			
			} catch (InterruptedException e) {
				// reset the flag to allow thread terminate on its own...
				Thread.currentThread().interrupt();
			}
		}
		
		logger.info("[{}] asked to stop...", this.getId());
		return null;
	}
	
	private Direction isThereFoodAround() {
		for (Direction direction : Direction.values()) {
			final Node neighbour = this.getCurrentNode().getNeighbour(direction);
			
			if (neighbour instanceof FoodSourceNode) {
				return direction;
			}
		}
		
		return null;
	}

}
