package org.ag.ants.task;

import org.ag.ants.agent.Ant;
import org.ag.ants.env.impl.ForageStimulusType;
import org.ag.common.agent.Agent;
import org.ag.common.env.Coordinate;
import org.ag.common.env.Direction;
import org.ag.common.task.AbstractTask;

/**
 * This task implements the techniques described here <i>http://plus.maths.org/content/finding-way-home</i> to guide the
 * agent to its home nest. That is an overly simplified implementation of what actually happens in some of species.
 */
public class FindHomeNestTask extends AbstractTask {
    public static final String NAME = "ant:task:find-home";

    public FindHomeNestTask() {
        super(NAME);
    }

    @Override
    public void execute(Agent agent) {
        final Ant ant = (Ant) agent;
        final Coordinate c = ant.getVectorToNest();

        // note that there is no check if the ant is actually able to move to a node in a given direction. So far it's
        // supposed that the environment is free of obstacles and the ant would be able to go back to the nest, wherever
        // it is at the moment the task is executed.
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
                ant.moveToNeighbour(Direction.EAST);
            }

            if (c.getLine() > 0) {
                ant.moveToNeighbour(Direction.NORTH_EAST);
            }

            if (c.getLine() < 0) {
                ant.moveToNeighbour(Direction.SOUTH_EAST);
            }
        }

        if (c.getColumn() < 0) {
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

        ant.incrementStimulusIntensity(ForageStimulusType.TYPE);
    }
}
