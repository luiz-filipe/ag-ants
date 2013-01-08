package org.ag.ants.task;

import org.ag.ants.env.ChemicalCommStimulus;
import org.ag.ants.env.PheromoneNode;
import org.ag.ants.env.impl.ForageStimulusType;
import org.ag.common.agent.Agent;
import org.ag.common.env.Direction;
import org.ag.common.task.AbstractTask;
import org.ag.ants.agent.impl.AntAgent;

/**
 * 
 * @author Luiz Abrahao <luiz@luizabrahao.com>
 * 
 */
//TODO document
public class ForageTask extends AbstractTask {
	public static final String NAME = "ant:task:forage";

	// Represents the probability of a node in any possible direction (there
	// are 8 in total) of being picked.
	private static final double initial_proability = 1 / 8;

	// If a node has some pheromone deposited in it, the pheromone_factor can
	// be added to increase the probability of nodes with pheromone being
	// chosen instead of nodes with no pheromone at all.
	private static final double pheromone_factor = 1;

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
	
	/**
	 * Uses the forage chemical stimulus for deciding where the agent should go
	 * next. Each node around the agent has the initial probability of being
	 * picked of 1/8, this is multiplied by the resulting of the sum 
	 * (pheromone_factor + the actual pheromone concentration in the neighbour 
	 * node). The final probability for each neighbour is used in a monte-carlo
	 * like decision structure.
	 * 
	 * Note that this implementation does not take in consideration the
	 * direction the ant is travelling. This could be important otherwise the
	 * ant might get stuck going back and forth for no reason.
	 * 
	 * @param ant Agent that will be moved to the new location
	 * @return Direction of the neighbour node the agent will move to.
	 */
	private static Direction chooseDirectionToMove(final Agent ant) {
		final double intensityNorth = ForageTask.getForageIntensity(
				Direction.NORTH, ant);
		final double intensityNorthEast = ForageTask.getForageIntensity(
				Direction.NORTH_EAST, ant);
		final double intensityEast = ForageTask.getForageIntensity(
				Direction.EAST, ant);
		final double intensitySouthEast = ForageTask.getForageIntensity(
				Direction.SOUTH_EAST, ant);
		final double intensitySouth = ForageTask.getForageIntensity(
				Direction.SOUTH, ant);
		final double intensitySouthWest = ForageTask.getForageIntensity(
				Direction.SOUTH_WEST, ant);
		final double intensityWest = ForageTask.getForageIntensity(
				Direction.WEST, ant);
		final double intensityNorthWest = ForageTask.getForageIntensity(
				Direction.NORTH_WEST, ant);

		final double sumOfIntensities = intensityNorth + intensityNorthEast
				+ intensityEast + intensitySouthEast + intensitySouth
				+ intensitySouthWest + intensityWest + intensityNorthWest;

		final double northRate = intensityNorth / sumOfIntensities;
		final double northEastRate = intensityNorthEast / sumOfIntensities;
		final double eastRate = intensityEast / sumOfIntensities;
		final double southEastRate = intensitySouthEast / sumOfIntensities;
		final double southRate = intensitySouth / sumOfIntensities;
		final double southWestRate = intensitySouthWest / sumOfIntensities;
		final double westRate = intensityWest / sumOfIntensities;

		final double probabilityNorth = initial_proability
				* (pheromone_factor + northRate);
		final double probabilityNorthEast = initial_proability
				* (pheromone_factor + northEastRate);
		final double probabilityEast = initial_proability
				* (pheromone_factor + eastRate);
		final double probabilitySouthEast = initial_proability
				* (pheromone_factor + southEastRate);
		final double probabilitySouth = initial_proability
				* (pheromone_factor + southRate);
		final double probabilitySouthWest = initial_proability
				* (pheromone_factor + southWestRate);
		final double probabilityWest = initial_proability
				* (pheromone_factor + westRate);
		
		final double randomPoint = Math.random();
		double cumulativeSum = probabilityNorth;
		
		if (randomPoint <= cumulativeSum) {
			return Direction.NORTH;
		}
		
		cumulativeSum = cumulativeSum + probabilityNorthEast;
		
		if (randomPoint <= cumulativeSum) {
			return Direction.NORTH_EAST;
		}
		
		cumulativeSum = cumulativeSum + probabilityEast;

		if (randomPoint <= cumulativeSum) {
			return Direction.EAST;
		}
		
		cumulativeSum = cumulativeSum + probabilitySouthEast;
		
		if (randomPoint <= cumulativeSum) {
			return Direction.SOUTH_EAST;
		}
		
		cumulativeSum = cumulativeSum + probabilitySouth;
		
		if (randomPoint <= cumulativeSum) {
			return Direction.SOUTH;
		}
		
		cumulativeSum = cumulativeSum + probabilitySouthWest;
		
		if (randomPoint <= cumulativeSum) {
			return Direction.SOUTH_WEST;
		}
		
		cumulativeSum = cumulativeSum + probabilityWest;
		
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
