package org.ag.ants.task;

import org.ag.ants.agent.Ant;
import org.ag.common.agent.Agent;
import org.ag.common.env.Coordinate;
import org.ag.common.env.Direction;
import org.ag.common.task.AbstractTask;

public class FindHomeTask extends AbstractTask {
	public static final String NAME = "ant:task:find-home";
	
	public FindHomeTask() {
		super(NAME);
	}

	@Override
	public void execute(Agent agent) {
		final Ant ant = (Ant) agent;
		final Coordinate c = ant.getVectorToNest();

		/* note that there is no check if the ant is actually able to move to
		 * a node in a given direction. So far it's supposed that the
		 * environment is free of obstacles and the ant would be able to go back
		 * to the nest, wherever it is at the moment the task is executed.
		 */
		if (c.getColumn() == 0) {
			if (c.getLine() == 0) {
				return;
			}
			
			if (c.getLine() > 0) {
				ant.moveToNeighbour(Direction.NORTH);
			}
			
			if (c.getLine() < 0) {
				ant.moveToNeighbour(Direction.SOUTH);
			}
		}
		
		if (c.getColumn() > 0) {
			if (c.getLine() == 0) {
				ant.moveToNeighbour(Direction.WEST);
			}
			
			if (c.getLine() > 0) {
				ant.moveToNeighbour(Direction.NORTH_WEST);
			}
			
			if (c.getLine() < 0) {
				ant.moveToNeighbour(Direction.SOUTH_WEST);
			}
		}
		
		if (c.getColumn() < 0) {
			if (c.getLine() == 0) {
				ant.moveToNeighbour(Direction.EAST);
			}
			
			if (c.getLine() > 0) {
				ant.moveToNeighbour(Direction.NORTH_EAST);
			}
			
			if (c.getLine() < 0) {
				ant.moveToNeighbour(Direction.SOUTH_EAST);
			}
		}
	}
}
