package org.ag.test.ants.env;

import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.awt.Dimension;

import org.ag.ants.env.AntNest;
import org.ag.ants.env.EnvironmentFactory;
import org.ag.common.env.Direction;
import org.ag.common.env.Node;
import org.junit.Test;

public class AntNestTest {
	@Test
	public void nestPlacementTest() {
		final int nLines = 5;
		final int nColumns = 5;
		
		final Node[][] env = EnvironmentFactory.createPheromoneGrid(new Dimension(nLines, nColumns));
		final AntNest nest = new AntNest("nest", new Dimension(3, 3), 10, Color.BLACK);
		
		nest.connectToNeighbours(env[1][1]);

		// first line
		assertTrue(nest.getNode(0, 0).getNeighbour(Direction.NORTH).getId().equals("n-0,1"));
		assertTrue(nest.getNode(0, 0).getNeighbour(Direction.NORTH_EAST).getId().equals("n-0,2"));
		assertTrue(nest.getNode(0, 0).getNeighbour(Direction.SOUTH_WEST).getId().equals("n-2,0"));
		assertTrue(nest.getNode(0, 0).getNeighbour(Direction.WEST).getId().equals("n-1,0"));
		assertTrue(nest.getNode(0, 0).getNeighbour(Direction.NORTH_WEST).getId().equals("n-0,0"));
		
		assertTrue(nest.getNode(0, 1).getNeighbour(Direction.NORTH).getId().equals("n-0,2"));
		assertTrue(nest.getNode(0, 1).getNeighbour(Direction.NORTH_EAST).getId().equals("n-0,3"));
		assertTrue(nest.getNode(0, 1).getNeighbour(Direction.NORTH_WEST).getId().equals("n-0,1"));
		
		assertTrue(nest.getNode(0, 2).getNeighbour(Direction.NORTH).getId().equals("n-0,3"));
		assertTrue(nest.getNode(0, 2).getNeighbour(Direction.NORTH_EAST).getId().equals("n-0,4"));
		assertTrue(nest.getNode(0, 2).getNeighbour(Direction.EAST).getId().equals("n-1,4"));
		assertTrue(nest.getNode(0, 2).getNeighbour(Direction.SOUTH_EAST).getId().equals("n-2,4"));
		assertTrue(nest.getNode(0, 2).getNeighbour(Direction.NORTH_WEST).getId().equals("n-0,2"));
		
		// second line
		assertTrue(nest.getNode(1, 0).getNeighbour(Direction.SOUTH_WEST).getId().equals("n-3,0"));
		assertTrue(nest.getNode(1, 0).getNeighbour(Direction.WEST).getId().equals("n-2,0"));
		assertTrue(nest.getNode(1, 0).getNeighbour(Direction.NORTH_WEST).getId().equals("n-1,0"));
		
		assertTrue(nest.getNode(1, 2).getNeighbour(Direction.NORTH_EAST).getId().equals("n-1,4"));
		assertTrue(nest.getNode(1, 2).getNeighbour(Direction.EAST).getId().equals("n-2,4"));
		assertTrue(nest.getNode(1, 2).getNeighbour(Direction.SOUTH_EAST).getId().equals("n-3,4"));
		
		// third and last line
		assertTrue(nest.getNode(2, 0).getNeighbour(Direction.SOUTH_EAST).getId().equals("n-4,2"));
		assertTrue(nest.getNode(2, 0).getNeighbour(Direction.SOUTH).getId().equals("n-4,1"));
		assertTrue(nest.getNode(2, 0).getNeighbour(Direction.SOUTH_WEST).getId().equals("n-4,0"));
		assertTrue(nest.getNode(2, 0).getNeighbour(Direction.WEST).getId().equals("n-3,0"));
		assertTrue(nest.getNode(2, 0).getNeighbour(Direction.NORTH_WEST).getId().equals("n-2,0"));
		
		assertTrue(nest.getNode(2, 1).getNeighbour(Direction.SOUTH_EAST).getId().equals("n-4,3"));
		assertTrue(nest.getNode(2, 1).getNeighbour(Direction.SOUTH).getId().equals("n-4,2"));
		assertTrue(nest.getNode(2, 1).getNeighbour(Direction.SOUTH_WEST).getId().equals("n-4,1"));
		
		assertTrue(nest.getNode(2, 2).getNeighbour(Direction.NORTH_EAST).getId().equals("n-2,4"));
		assertTrue(nest.getNode(2, 2).getNeighbour(Direction.EAST).getId().equals("n-3,4"));
		assertTrue(nest.getNode(2, 2).getNeighbour(Direction.SOUTH_EAST).getId().equals("n-4,4"));
		assertTrue(nest.getNode(2, 2).getNeighbour(Direction.SOUTH).getId().equals("n-4,3"));
		assertTrue(nest.getNode(2, 2).getNeighbour(Direction.SOUTH_WEST).getId().equals("n-4,2"));
		
		// original nodes test
		assertTrue(env[0][0].getNeighbour(Direction.SOUTH_EAST).getId().equals("nest-0,0"));
		assertTrue(env[0][1].getNeighbour(Direction.SOUTH).getId().equals("nest-0,0"));
		assertTrue(env[0][2].getNeighbour(Direction.SOUTH).getId().equals("nest-0,1"));
		assertTrue(env[0][3].getNeighbour(Direction.SOUTH).getId().equals("nest-0,2"));
	}
}
