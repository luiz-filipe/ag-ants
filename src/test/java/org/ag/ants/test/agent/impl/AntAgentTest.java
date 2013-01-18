package org.ag.ants.test.agent.impl;

import static org.junit.Assert.assertTrue;

import java.awt.Dimension;

import org.ag.ants.agent.impl.AntAgent;
import org.ag.ants.env.EnvironmentFactory;
import org.ag.ants.env.PheromoneNode;
import org.ag.ants.env.impl.ForageStimulusType;
import org.ag.ants.test.mock.TestAntAgent;
import org.ag.common.env.Coordinate;
import org.ag.common.env.Direction;
import org.ag.common.env.Node;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AntAgentTest {
	private static final Logger logger = LoggerFactory
			.getLogger(AntAgentTest.class);
	
	private final int nLines = 20;
	private final int nColumns = 20;
	private Node[][] env;
	private AntAgent ant;

	@Before
	public void setupEnvironment() {
		env = EnvironmentFactory.createPheromoneGrid(new Dimension(nLines,
				nColumns));
		ant = new TestAntAgent("ant", env[nLines / 2][nColumns / 2], true);
	}

	/**
	 * Every time the ant navigates through the environment it should execute
	 * the path integration procedure to be able to go back to its nest. 
	 */
	@Test
	public void pathIntegrationTest() {
		assertTrue(ant.getVectorToNest().equals(new Coordinate(0, 0)));
		assertTrue(ant.getCurrentNode().getId().equals("n-10,10"));
		
		for (int i = 0; i < 5; i++) {
			ant.moveToNeighbour(Direction.SOUTH);
		}
		
		assertTrue(ant.getVectorToNest().equals(new Coordinate(5, 0)));
		assertTrue(ant.getCurrentNode().getId().equals("n-15,10"));
		
		for (int i = 0; i < 7; i++) {
			ant.moveToNeighbour(Direction.EAST);
		}
		
		assertTrue(ant.getVectorToNest().equals(new Coordinate(5, -7)));
		assertTrue(ant.getCurrentNode().getId().equals("n-15,17"));
	}
	
	/**
	 * Checks if the agent's memory does not 'remember' more nodes than it
	 * should. An agent's memory capacity is defined by the agent's type. In
	 * the test case is 10 nodes.
	 */
	@Test
	public void memoryCapacityTest() {
		final TestAntAgent testAnt = (TestAntAgent) ant;
		assertTrue(ant.getLatestNodeFromMemory() == null);
		
		for (int i = 0; i < 5; i++) {
			ant.moveToNeighbour(Direction.SOUTH);
		}
		
		assertTrue(testAnt.getNumberOfNodesInMemory() == 5);
		
		for (int i = 0; i < 10; i++) {
			ant.moveToNeighbour(Direction.NORTH);
		}
		assertTrue(testAnt.getNumberOfNodesInMemory() == 10);
	}
	
	@Test
	public void pheromoneIncrementTest() {
		final TestAntAgent testAnt = (TestAntAgent) ant;
		
		testAnt.incrementStimulusIntensity(ForageStimulusType.TYPE);
		PheromoneNode node = (PheromoneNode) testAnt.getCurrentNode().getNeighbour(Direction.NORTH);
		
		logger.info("North: {}", node.getCommunicationStimulus(ForageStimulusType.TYPE).getIntensity());
		
		//TODO finish test
	}
}
