package org.ag.ants.test.task;

import static org.junit.Assert.assertTrue;

import java.awt.Dimension;

import org.ag.ants.agent.impl.AntAgent;
import org.ag.ants.env.EnvironmentFactory;
import org.ag.ants.env.impl.ForageStimulusType;
import org.ag.ants.task.ForageTask;
import org.ag.ants.test.mock.TestAntAgent;
import org.ag.common.env.Direction;
import org.ag.common.env.Node;
import org.ag.common.task.Task;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ForageTaskTest {
	private static final Logger logger = LoggerFactory
			.getLogger(ForageTaskTest.class);

	/**
	 * Checks if the neighbour selection follows the model proposed. In this
	 * experiment a small environment of 3x3 nodes is created, an ant is used to
	 * setup the environment with different concentrations of pheromone:
	 * 
	 * - N: 0.7 - NE: 0.6 - E: 0.5 - SE: 0.4 - S: 0.3 - SW: 0.2 - W: 0.1 - NW: 0
	 * 
	 * Following these concentrations the probability of each neighbour being
	 * picked up is:
	 * 
	 * - N: 34% - NE: 25% - E: 18% - SE: 11% - S: 7% - SW: 3%; - W: 1%; - NW: 1%
	 */
	@Test
	public void nodeSelectionTest() {
		final int numberOfRepetitions = 10000;
		final double allowedErrorMargin = 0.05;
		final Node[][] env = EnvironmentFactory
				.createPheromoneGrid(new Dimension(3, 3));
		final AntAgent ant = new TestAntAgent("ant", env[1][1], false);
		final Task forageTask = new ForageTask();

		double timesN = 0;
		double timesNE = 0;
		double timesE = 0;
		double timesSE = 0;
		double timesS = 0;
		double timesSW = 0;
		double timesW = 0;
		double timesNW = 0;

		// prepare the environment by adding the communication stimulus to the
		// surrounding nodes.
		ant.moveToNeighbour(Direction.NORTH);
		ant.incrementStimulusIntensityMultipliedByFactor(
				ForageStimulusType.TYPE, 7);

		// Northeast
		ant.moveToNeighbour(Direction.EAST);
		ant.incrementStimulusIntensityMultipliedByFactor(
				ForageStimulusType.TYPE, 6);

		// East
		ant.moveToNeighbour(Direction.SOUTH);
		ant.incrementStimulusIntensityMultipliedByFactor(
				ForageStimulusType.TYPE, 5);

		// Southeast
		ant.moveToNeighbour(Direction.SOUTH);
		ant.incrementStimulusIntensityMultipliedByFactor(
				ForageStimulusType.TYPE, 4);

		// South
		ant.moveToNeighbour(Direction.WEST);
		ant.incrementStimulusIntensityMultipliedByFactor(
				ForageStimulusType.TYPE, 3);

		// Southwest
		ant.moveToNeighbour(Direction.WEST);
		ant.incrementStimulusIntensityMultipliedByFactor(
				ForageStimulusType.TYPE, 2);

		// West
		ant.moveToNeighbour(Direction.NORTH);
		ant.incrementStimulusIntensityMultipliedByFactor(
				ForageStimulusType.TYPE, 1);

		// Northwest is not necessary to update because will not contain
		// communication stimulus. So let's put the agent at the centre of the
		// environment again.
		ant.moveToNeighbour(Direction.EAST);
		assertTrue(ant.getCurrentNode().getId().equals("n-1,1"));

		for (int i = 0; i < numberOfRepetitions; i++) {
			forageTask.execute(ant);

			switch (ant.getMovingDirection()) {
			case NORTH:
				timesN++;
				ant.moveToNeighbour(Direction.SOUTH);
				break;

			case NORTH_EAST:
				timesNE++;
				ant.moveToNeighbour(Direction.SOUTH_WEST);
				break;

			case EAST:
				timesE++;
				ant.moveToNeighbour(Direction.WEST);
				break;

			case SOUTH_EAST:
				timesSE++;
				ant.moveToNeighbour(Direction.NORTH_WEST);
				break;

			case SOUTH:
				timesS++;
				ant.moveToNeighbour(Direction.NORTH);
				break;

			case SOUTH_WEST:
				timesSW++;
				ant.moveToNeighbour(Direction.NORTH_EAST);
				break;

			case WEST:
				timesW++;
				ant.moveToNeighbour(Direction.EAST);
				break;

			case NORTH_WEST:
				timesNW++;
				ant.moveToNeighbour(Direction.SOUTH_EAST);
				break;
			}
		}

		final double percentageN = timesN / numberOfRepetitions;
		final double percentageNE = timesNE / numberOfRepetitions;
		final double percentageE = timesE / numberOfRepetitions;
		final double percentageSE = timesSE / numberOfRepetitions;
		final double percentageS = timesS / numberOfRepetitions;
		final double percentageSW = timesSW / numberOfRepetitions;
		final double percentageW = timesW / numberOfRepetitions;
		final double percentageNW = timesNW / numberOfRepetitions;

		logger.info("Percentage N: {}", percentageN);
		logger.info("Percentage NE: {}", percentageNE);
		logger.info("Percentage E: {}", percentageE);
		logger.info("Percentage SE: {}", percentageSE);
		logger.info("Percentage S: {}", percentageS);
		logger.info("Percentage SW: {}", percentageSW);
		logger.info("Percentage W: {}", percentageW);
		logger.info("Percentage NW: {}", percentageNW);
		
		assertTrue((percentageN > (0.34 - allowedErrorMargin))
				&& (percentageN < (0.34 + allowedErrorMargin)));
		
		assertTrue((percentageNE > (0.25 - allowedErrorMargin))
				&& (percentageNE < (0.25 + allowedErrorMargin)));
		
		assertTrue((percentageE > (0.18 - allowedErrorMargin))
				&& (percentageE < (0.18 + allowedErrorMargin)));
		
		assertTrue((percentageSE > (0.11 - allowedErrorMargin))
				&& (percentageSE < (0.11 + allowedErrorMargin)));
		
		assertTrue((percentageS > (0.07 - allowedErrorMargin))
				&& (percentageS < (0.07 + allowedErrorMargin)));
		
		assertTrue((percentageSW > (0.03 - allowedErrorMargin))
				&& (percentageSW < (0.03 + allowedErrorMargin)));
		
		assertTrue((percentageW > (0.01 - allowedErrorMargin))
				&& (percentageW < (0.01 + allowedErrorMargin)));
		
		assertTrue((percentageNW > (0.01 - allowedErrorMargin))
				&& (percentageNW < (0.01 + allowedErrorMargin)));

	}

}
