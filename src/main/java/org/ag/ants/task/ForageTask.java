package org.ag.ants.task;

import org.ag.ants.env.ChemicalCommStimulus;
import org.ag.ants.env.PheromoneNode;
import org.ag.ants.env.impl.ForageStimulusType;
import org.ag.common.agent.Agent;
import org.ag.common.env.Direction;
import org.ag.common.task.AbstractTask;
import org.ag.ants.agent.impl.AntAgent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Luiz Abrahao <luiz@luizabrahao.com>
 * 
 */
//TODO document
public class ForageTask extends AbstractTask {
	private static final Logger logger = LoggerFactory
			.getLogger(ForageTask.class);
	public static final String NAME = "ant:task:forage";

	public ForageTask() {
		super(NAME);
	}

	@Override
	public void execute(Agent ant) {
		Direction directionToMove = null;
		
		while (!Thread.currentThread().isInterrupted()) {
			directionToMove = ForageTask.chooseDirectionToMove(ant);
			
			if (ant.getCurrentNode().getNeighbour(directionToMove) != null) {
				break;
			}
		}
		
		((AntAgent) ant).moveToNeighbour(directionToMove);
	}
	
	//TODO update documentation
	/**
	 * Note that this implementation does not take in consideration the
	 * direction the ant is travelling. This could be important otherwise the
	 * ant might get stuck going back and forth for no reason.
	 * 
	 * Also remember that we have 8 possible directions to move to.
	 * 
	 * @param ant Agent that will be moved to the new location
	 * @return Direction of the neighbour node the agent will move to.
	 */
	private static Direction chooseDirectionToMove(final Agent ant) {
		final double[] intensities = new double[8];
		final double[] intensityRates = new double[8];
		double minimumIntensityRate = 0;
		
		intensities[0] = ForageTask.getForageIntensity(Direction.NORTH, ant);
		intensities[1] = ForageTask.getForageIntensity(Direction.NORTH_EAST, ant);
		intensities[2] = ForageTask.getForageIntensity(Direction.EAST, ant);
		intensities[3] = ForageTask.getForageIntensity(Direction.SOUTH_EAST, ant);
		intensities[4] = ForageTask.getForageIntensity(Direction.SOUTH, ant);
		intensities[5] = ForageTask.getForageIntensity(Direction.SOUTH_WEST, ant);
		intensities[6] = ForageTask.getForageIntensity(Direction.WEST, ant);
		intensities[7] = ForageTask.getForageIntensity(Direction.NORTH_WEST, ant);

//		logger.trace("Intensities of neighbour nodes: ");
//		logger.trace(" - north: {}", intensities[0]);
//		logger.trace(" - north-east: {}", intensities[1]);
//		logger.trace(" - east: {}", intensities[2]);
//		logger.trace(" - south-east: {}", intensities[3]);
//		logger.trace(" - south: {}", intensities[4]);
//		logger.trace(" - south-west: {}", intensities[5]);
//		logger.trace(" - west: {}", intensities[6]);
//		logger.trace(" - north-west: {}", intensities[7]);
		
		// We square the intensities to give an exponential curve to the
		// final probabilities, otherwise it would be linear.
		for (int i = 0; i < 8; i++) {
			intensities[i] = intensities[i] * intensities[i];
		}
		
		final double sumOfIntensities = intensities[0] + intensities[1] + 
				intensities[2] + intensities[3] + intensities[4] +
				intensities[5] + intensities[6] + intensities[7];

		intensityRates[0] = intensities[0] / sumOfIntensities;
		intensityRates[1] = intensities[1] / sumOfIntensities;
		intensityRates[2] = intensities[2] / sumOfIntensities;
		intensityRates[3] = intensities[3] / sumOfIntensities;
		intensityRates[4] = intensities[4] / sumOfIntensities;
		intensityRates[5] = intensities[5] / sumOfIntensities;
		intensityRates[6] = intensities[6] / sumOfIntensities;
		intensityRates[7] = intensities[7] / sumOfIntensities;
		
		// find the minimum non-zero intensity rate, that will be assigned to
		// the neighbours nodes with no forage pheromone in it. Note that all
		// the other nodes will have this intensity rate to theirs and the rates
		// will be recalculated again.
		for (int i = 0; i < 8; i++) {
			if (intensityRates[i] < minimumIntensityRate) {
				minimumIntensityRate = intensityRates[i];
			}
		}
		
		double finalSumIntensityRates = 0;

		for (int i = 0; i < 8; i++) {
			intensityRates[i] = intensityRates[i] + minimumIntensityRate;
			finalSumIntensityRates = finalSumIntensityRates + intensityRates[i];
		}

		intensityRates[0] = intensityRates[0] / finalSumIntensityRates;
		intensityRates[1] = intensityRates[1] / finalSumIntensityRates;
		intensityRates[2] = intensityRates[2] / finalSumIntensityRates;
		intensityRates[3] = intensityRates[3] / finalSumIntensityRates;
		intensityRates[4] = intensityRates[4] / finalSumIntensityRates;
		intensityRates[5] = intensityRates[5] / finalSumIntensityRates;
		intensityRates[6] = intensityRates[6] / finalSumIntensityRates;
		intensityRates[7] = intensityRates[7] / finalSumIntensityRates;
		
		final double randomPoint = Math.random();
		double cumulativeSum = intensityRates[0];
		
		if (randomPoint <= cumulativeSum) {
			return Direction.NORTH;
		}
		
		cumulativeSum = cumulativeSum + intensityRates[1];
		
		if (randomPoint <= cumulativeSum) {
			return Direction.NORTH_EAST;
		}
		
		cumulativeSum = cumulativeSum + intensityRates[2];

		if (randomPoint <= cumulativeSum) {
			return Direction.EAST;
		}
		
		cumulativeSum = cumulativeSum + intensityRates[3];
		
		if (randomPoint <= cumulativeSum) {
			return Direction.SOUTH_EAST;
		}
		
		cumulativeSum = cumulativeSum + intensityRates[4];
		
		if (randomPoint <= cumulativeSum) {
			return Direction.SOUTH;
		}
		
		cumulativeSum = cumulativeSum + intensityRates[5];
		
		if (randomPoint <= cumulativeSum) {
			return Direction.SOUTH_WEST;
		}
		
		cumulativeSum = cumulativeSum + intensityRates[6];
		
		if (randomPoint <= cumulativeSum) {
			return Direction.WEST;
		}
		
		return Direction.NORTH_WEST;
	}

	private static double getForageIntensity(Direction direction, Agent ant) {
		final PheromoneNode neighbour = (PheromoneNode) ant.getCurrentNode()
				.getNeighbour(direction);

		if (neighbour == null) {
			return 0;
		}

		final ChemicalCommStimulus stimulus = neighbour
				.getCommunicationStimulus(ForageStimulusType.TYPE);

		// if no forage pheromone was deposited yet, the stimulus will be null
		// and the best we can do is to return 0.
		if (stimulus == null) {
			return 0;
		}

		return stimulus.getIntensity();
	}
}
