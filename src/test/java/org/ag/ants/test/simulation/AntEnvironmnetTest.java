package org.ag.ants.test.simulation;

import static org.junit.Assert.assertTrue;

import java.awt.Color;

import org.ag.ants.env.NestNode;
import org.ag.ants.env.PheromoneNode;
import org.ag.ants.simulation.AntEnvironment;
import org.junit.Test;

public class AntEnvironmnetTest {

	/**
	 * Check if the original nodes from the environment are replaced by the
	 * ones from the nest added to the grid.
	 */
	@Test
	public void nodePlacementTest() {
		final AntEnvironment env = new AntEnvironment(5, 5);
		
		env.createNestAt("nest", 1, 1, 3, 3, Color.BLACK);
		
		for (int l = 0; l < 5; l++) {
			for (int c = 0; c < 5; c++) {
				if (l == 0) {
					assertTrue(env.getNodeAt(l, c) instanceof PheromoneNode);
				}
				
				if ((l > 0) && (l < 4)) {
					if ((c == 0) || (c == 4)) {
						assertTrue(env.getNodeAt(l, c) instanceof PheromoneNode);
					
					} else {
						assertTrue(env.getNodeAt(l, c) instanceof NestNode);
					}
				}
				
				if (l == 4) {
					assertTrue(env.getNodeAt(l, c) instanceof PheromoneNode);
				}
			}
		}
	}
}
