package org.ag.ants.task;

import org.ag.ants.agent.AntAgent;
import org.ag.ants.env.ChemicalCommStimulus;
import org.ag.ants.env.ChemicalCommStimulusType;
import org.ag.common.agent.Agent;
import org.ag.common.env.Direction;
import org.ag.common.env.Node;

import net.jcip.annotations.ThreadSafe;

/**
 * Utility methods related to ant tasks.
 * 
 * @author Luiz Abrahao <luiz@luizabrahao.com>
 *
 */
@ThreadSafe
public class AntTaskUtil {
	private AntTaskUtil() {}
	
	/**
	 * Returns the intensity of a chemical stimulus present in a neighbour in
	 * the requested direction.
	 * 
	 * @param agent agent that will be used as reference 
	 * @param direction direction to look for neighbour node
	 * @param chemicalCommStimulusType chemical stimulus type to find intensity
	 * @return intensity of chemical stimulus of neighbour in requested
	 *         direction
	 */
	public static double getNeighbourForagePheromone(Agent agent, Direction direction, ChemicalCommStimulusType chemicalCommStimulusType) {
		Node n = agent.getCurrentNode().getNeighbour(direction);
		
		if (n == null) {
	    	return 0;
	    
	    } else  {
	    	ChemicalCommStimulus foragePheromone = (ChemicalCommStimulus) n.getCommunicationStimulus(chemicalCommStimulusType);
	    	
	    	if (foragePheromone == null) {
	    		return 0;
	    	}
	    	
	    	return foragePheromone.getIntensity();
	    }
	}

	/**
	 * Uses chemical stimulus to decide which node the agent should move next.
	 * Each neighbour node has a weight, which is multiplied by the node's
	 * stimulus' intensity. The resulting rate WEIGHT * INTENSITY is normalised
	 * by the sum of the rates to find the proportional probability of a node
	 * being picked.
	 * 
	 * This is necessary to add a stochastic behaviour to the agent, simulating
	 * external forces like wind that can sometimes take agents away from the
	 * optimal path. 
	 * 
	 * @param agent Agent that is going to move.
	 * @return Node to move to.
	 */
	public static Direction getDirectionToMoveTo(AntAgent agent, ChemicalCommStimulusType chemicalCommStimulusType,
									   double weightNort, double weightEast, double weightSouth, double weightWest) {
		
		double pheromoneNorth = 0;
		double pheromoneEast = 0;
		double pheromoneSouth = 0;
		double pheromoneWest = 0;
		
		if (agent.getMovingDirection() == null) {
			agent.setMovingDirection(Direction.SOUTH);
		}
		
		synchronized (agent.getCurrentNode()) {
			// First we need to work out what are the neighbours nodes in
			// relation to the agent's current movement.
			Direction northOfTheAgent = Direction.NORTH;
			Direction eastOfTheAgent = Direction.EAST;
			Direction southOfTheAgent = Direction.SOUTH;
			Direction westOfTheAgent = Direction.WEST;
			
			// Transformation of direction from being related to the
			// grid to being related to the agent's moving direction.
			// If the agent is moving north, nothing is needed to be done.
			if (agent.getMovingDirection() == Direction.EAST) {
				northOfTheAgent = Direction.EAST;
				eastOfTheAgent = Direction.SOUTH;
				southOfTheAgent = Direction.WEST;
				westOfTheAgent = Direction.NORTH;

			} else if (agent.getMovingDirection() == Direction.SOUTH) {
				northOfTheAgent = Direction.SOUTH;
				eastOfTheAgent = Direction.WEST;
				southOfTheAgent = Direction.NORTH;
				westOfTheAgent = Direction.EAST;
		    
		    } else if (agent.getMovingDirection() == Direction.WEST) {
		    	northOfTheAgent = Direction.WEST;
			    eastOfTheAgent = Direction.NORTH;
			    southOfTheAgent = Direction.EAST;
			    westOfTheAgent = Direction.SOUTH;
		    }
		    
		    // Get the intensity of the pheromone of the neighbour nodes. The
		    // directions are all in relation to the agent movement direction
		    // and not to the grid.
			pheromoneNorth = AntTaskUtil.getNeighbourForagePheromone(agent, northOfTheAgent, chemicalCommStimulusType);
			pheromoneEast = AntTaskUtil.getNeighbourForagePheromone(agent, eastOfTheAgent, chemicalCommStimulusType);
			pheromoneSouth = AntTaskUtil.getNeighbourForagePheromone(agent, southOfTheAgent, chemicalCommStimulusType);
			pheromoneWest = AntTaskUtil.getNeighbourForagePheromone(agent, westOfTheAgent, chemicalCommStimulusType);

			double rateNorth = pheromoneNorth * weightNort;
			double rateEast = pheromoneEast * weightEast;
			double rateSouth = pheromoneSouth * weightSouth;
			double rateWest = pheromoneWest * weightWest;
			
			final double sumRates = rateNorth + rateEast + rateSouth + rateWest;
			
			rateNorth = rateNorth / sumRates;
			rateEast = rateEast / sumRates;
			rateSouth = rateSouth / sumRates;
			rateWest = rateWest / sumRates;
			
			final double randomPoint = Math.random();

			if (rateNorth >= randomPoint) {
				return northOfTheAgent;
			}
			
			if ((rateNorth < randomPoint) && (rateEast >= randomPoint)) {
				return eastOfTheAgent;
			}
			
			if ((rateEast < randomPoint) && (rateSouth >= randomPoint)) {
				return southOfTheAgent;
			}
			
			if ((rateSouth < randomPoint) && (rateWest >= randomPoint)) {
				return westOfTheAgent;
			}
			
			return Direction.randomDirection();
		}
	}
	
	public static Direction getDirectionToMoveTo(AntAgent agent, ChemicalCommStimulusType chemicalCommStimulusType) {
		return AntTaskUtil.getDirectionToMoveTo(agent, chemicalCommStimulusType, ForageTask.WEIGHT_NORTH, ForageTask.WEIGHT_EAST, ForageTask.WEIGHT_SOUTH, ForageTask.WEIGHT_WEST);
	}
	
}
