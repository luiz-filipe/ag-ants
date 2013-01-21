package org.ag.ants.task;

import org.ag.ants.env.ChemicalCommStimulus;
import org.ag.ants.env.PheromoneNode;
import org.ag.ants.env.impl.ForageStimulusType;
import org.ag.common.agent.Agent;
import org.ag.common.env.Direction;
import org.ag.common.task.AbstractTask;
import org.ag.ants.agent.impl.AntAgent;

/**
 * The Forage task implements a node selection model to direct the ant towards 
 * a food source using pheromone trails. This model takes in consideration the
 * intensity of forage pheromone present in the 8 neighbour nodes around the 
 * agent.
 * 
 *  Firstly the pheromone intensities of the neighbour nodes are read. After
 *  that the function <em>exp(intensity * coefficient)</em> is applied for each
 *  of the neighbour nodes. The results from this step are used to calculate the
 *  probability of each neighbour node of being selected.
 *  
 *  Note that the coefficient is an arbitrary number used to enhance the
 *  probability of the nodes with pheromone in it. See 
 *  <em>model/Node Selection.xlsx</em> for more details.
 *  
 * @author Luiz Abrahao <luiz@luizabrahao.com>
 * 
 */
public class ForageTask extends AbstractTask {
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
	
	/**
	 * Selects one neighbour node to move to, taking in consideration the amount
	 * of pheromone present in the environment around the agent.
	 * 
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
		final int coefficient = 3;
		final double[] pheromoneIntensity = new double[8];
		final double[] intensityRates = new double[8];
		double sumIntensities = 0;
		
		pheromoneIntensity[0] = ForageTask.getForageIntensity(Direction.NORTH, ant);
		pheromoneIntensity[1] = ForageTask.getForageIntensity(Direction.NORTH_EAST, ant);
		pheromoneIntensity[2] = ForageTask.getForageIntensity(Direction.EAST, ant);
		pheromoneIntensity[3] = ForageTask.getForageIntensity(Direction.SOUTH_EAST, ant);
		pheromoneIntensity[4] = ForageTask.getForageIntensity(Direction.SOUTH, ant);
		pheromoneIntensity[5] = ForageTask.getForageIntensity(Direction.SOUTH_WEST, ant);
		pheromoneIntensity[6] = ForageTask.getForageIntensity(Direction.WEST, ant);
		pheromoneIntensity[7] = ForageTask.getForageIntensity(Direction.NORTH_WEST, ant);

		for (int i = 0; i < 8; i++) {
			pheromoneIntensity[i] = Math.exp(pheromoneIntensity[i] * coefficient);
			sumIntensities = sumIntensities + pheromoneIntensity[i];
		}
		
		for (int i = 0; i < 8; i++) {
			intensityRates[i] = pheromoneIntensity[i] / sumIntensities;
		}
		
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
