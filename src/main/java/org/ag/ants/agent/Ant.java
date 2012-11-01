package org.ag.ants.agent;

import org.ag.ants.agent.impl.AntNestAgent;
import org.ag.ants.env.ChemicalCommStimulusType;
import org.ag.ants.env.impl.FoodSourceAgent;
import org.ag.common.agent.Agent;
import org.ag.common.env.Direction;
import org.ag.common.env.Node;

/**
 * Defines the basic API that must be implemented by any agent that represent a
 * ant.
 * 
 * @author Luiz Abrahao <luiz@luizabrahao.com>
 * 
 */
public interface Ant {
	/**
	 * Each agent moves towards to a direction, this effectively means that the
	 * ant has a bigger probability of selecting a neighbour node on that
	 * direction than in any other.
	 * 
	 * @return Direction direction the ant is moving towards to.
	 */
	Direction getMovingDirection();

	/**
	 * Changes the direction the agent is moving towards to.
	 * 
	 * @param movingDirection
	 *            Direction the agent is more likely to follow.
	 */
	void setMovingDirection(Direction movingDirection);

	/**
	 * An agent has a map of ChemicalStimulusType and how much they increment
	 * each stimulus they are sensitive to. This method looks up that list and
	 * increment the node's current chemical stimulus accordingly to each agent
	 * type.
	 * 
	 * @param chemicalCommStimulusType
	 *            Chemical stimulus that will be updated.
	 */
	void incrementStimulusIntensity(
			ChemicalCommStimulusType chemicalCommStimulusType);

	/**
	 * Collects food from a food source.
	 * 
	 * @param foodSource
	 *            Food source that the food will be collected from
	 * @param amountToCollect
	 *            Amount of food the agent will try to collect.
	 * @return Double the amount of food the agent actually collected.
	 */
	double collectFood(Agent foodSource, double amountToCollect);

	/**
	 * Checks if the agent is caring food.
	 * 
	 * @return boolean True if the agent is caring food, false otherwise.
	 */
	boolean isCarryingFood();

	/**
	 * Each agent should have a short term memory of where the agent has been.
	 * This method adds a new method to the agent's memory.
	 * 
	 * @param node
	 *            Node where the agent has visited.
	 */
	void addToMemory(Node node);

	/**
	 * Scans all agents in the current node, if any agent that is a food source
	 * is found, it is returned. If there are more than one food sources in the
	 * same node, the first to be found is returned. But it should not happen.
	 * There is no reason to have two food sources agents in the same node, as
	 * ther is no different types of food.
	 * 
	 * @return FoodSourceAgent food source
	 */
	FoodSourceAgent findFoodSource();

	/**
	 * Returns the most recent node that the agent contains in his memory.
	 * 
	 * @return Node Latest node visited.
	 */
	Node getLatestNodeFromMemory();

	/**
	 * Sometimes a agent needs to reinforce something that it has learned, a way
	 * to do that is to deposit big quantities of chemical stimulus to tell it
	 * fellow agents something. For example, when a worker is caring food, it
	 * deposits more ForageStimulus than when it is not caring food. That
	 * indirectively tells other ants that they are on the right path that leads
	 * to a food source.
	 * 
	 * So this method deposits the normal amount of stimulus multiplied by the
	 * factor that is passed as parameter.
	 * 
	 * @param chemicalCommStimulusType
	 *            Stimulus to be deposited
	 * @param factor
	 *            Factor that the stimulus increment will be multiplied by
	 */
	void incrementStimulusIntensityMultipliedByFactor(
			final ChemicalCommStimulusType chemicalCommStimulusType, int factor);

	/**
	 * Inverts the agents moving direction. If the agent is moving NORTH it
	 * should be moving SOUTH after this method is called. The same for all the
	 * other directions:
	 * 
	 * NORTH -> SOUTH
	 * NOTH_EAST -> SOUTH_WEST
	 * EAST -> WEST
	 * SOUT_EAST -> NORTH_WEST
	 * SOUTH -> NORTH
	 * SOUTH_WEST -> NORTH_EAST
	 * WEST -> EAST
	 * NORTH_WEST -> SOUTH_EAST
	 */
	void invertDirection();

	/**
	 * If an agent is caring food, it will be able to deposit the food is caring
	 * in a nest.
	 * 
	 * @param nest
	 *            AntNestAgent Nest the food will be deposited.
	 */
	void depositFood(AntNestAgent nest);
}
