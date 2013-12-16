package org.ag.test.ants.task;

import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.awt.Dimension;

import org.ag.ants.agent.impl.AntAgent;
import org.ag.ants.env.AntNest;
import org.ag.ants.env.EnvironmentFactory;
import org.ag.ants.task.FindHomeNestTask;
import org.ag.test.ants.mock.TestAntAgent;
import org.ag.common.env.Direction;
import org.ag.common.env.Node;
import org.ag.common.task.Task;
import org.junit.Before;
import org.junit.Test;

public class FindHomeNestTaskTest {
    private final int nLines = 20;
    private final int nColumns = 20;
    private Node[][] env;
    private AntAgent ant;

    @Before
    public void setupEnvironment() {
        env = EnvironmentFactory.createPheromoneGrid(new Dimension(nLines, nColumns));
        ant = new TestAntAgent("ant", env[nLines / 2][nColumns / 2], true);
    }

    @Test
    public void findNestTest() {
        final Task task = new FindHomeNestTask();
        final AntNest nest = new AntNest("nest", new Dimension(1, 1), 10, Color.BLACK);
        nest.connectToNeighbours(env[2][8]);

        ant = new TestAntAgent("ant", env[2][8], true);

        for(int i = 0; i < 5; i++) {
            ant.moveToNeighbour(Direction.SOUTH_WEST);
        }

        assertTrue(ant.getCurrentNode().getId().equals("n-7,3"));
        assertTrue(ant.getVectorToNest().getColumn() == 5);
        assertTrue(ant.getVectorToNest().getLine() == 5);

        for (int i = 0; i < 5; i++) {
            task.execute(ant);
        }

        assertTrue(ant.getVectorToNest().getLine() == 0);
        assertTrue(ant.getVectorToNest().getColumn() == 0);
    }
}
