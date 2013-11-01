package org.ag.ants.agent;

import org.ag.ants.env.ChemicalCommStimulusType;
import org.ag.common.env.Coordinate;
import org.ag.common.env.Direction;
import org.ag.common.env.Node;

/**
 * Defines the basic API that must be implemented by any agent that represent a ant.
 *
 * @author Luiz Abrahao <luiz@luizabrahao.com>
 */
public interface Ant {
    /**
     * Each agent moves towards to a direction, this effectively means that the ant has a bigger probability of
     * selecting a neighbour node on that direction than in any other. For example, an ant is more likely to move to the
     * node in front of it than the one behind.
     *
     * @return direction the ant is moving towards to.
     */
    Direction getMovingDirection();

    /**
     * An agent has a map of ChemicalStimulusType and how much they increment each stimulus they are sensitive to. This
     * method looks up that list and increment the node's current chemical stimulus accordingly to each agent type.
     *
     * @param chemicalCommStimulusType chemical stimulus that will be updated.
     */
    void incrementStimulusIntensity(ChemicalCommStimulusType chemicalCommStimulusType);

    /**
     * Collects food from a food source.
     *
     * @return the amount of food the agent actually managed collected.
     */
    double collectFood();

    /**
     * Checks if the agent is caring food.
     *
     * @return true if the agent is caring food, false otherwise.
     */
    boolean isCarryingFood();

    /**
     * Each agent should have a short term memory of where the agent has been. This method adds a new method to the
     * agent's memory.
     *
     * @param node node where the agent has visited.
     */
    void addToMemory(Node node);

    /**
     * Returns the most recent node that the agent contains in his memory.
     *
     * @return latest node visited.
     */
    Node getLatestNodeFromMemory();

    /**
     * Sometimes a agent needs to reinforce something that it has learned, a way to do that is to deposit big quantities
     * of chemical stimulus to 'tell' it fellow agents something. For example, when a worker is caring food, it deposits
     * more ForageStimulus than when it is not caring food. That interactively tells other ants that they are on the
     * right path that leads to a food source.
     *
     * <p>So this method deposits the normal amount of stimulus multiplied by the factor that is passed as parameter.
     * </p>
     *
     * @param chemicalCommStimulusType type of the stimulus to be deposited.
     * @param factor factor that the stimulus increment will be multiplied by.
     */
    void incrementStimulusIntensityMultipliedByFactor(final ChemicalCommStimulusType chemicalCommStimulusType,
                                                      int factor);

    /**
     * If an agent is caring food, it will be able to deposit the food is caring in a nest.
     */
    void depositFood();

    /**
     * Returns the resulting vector of the path integration processed by the agent.
     *
     * @return vector that points the position of the nest in relation to the agent's
     */
    Coordinate getVectorToNest();

    /**
     * Moves the agent to the neighbour in the specified direction and updates the agent's internal navigation and
     * memory mechanisms.
     *
     * @param direction direction of neighbour node that the ant will move to.
     */
    void moveToNeighbour(Direction direction);

    /**
     * True if the ant is currently in a node that belongs to a nest. It does not necessarily means that the ant is in
     * the nest it belongs to.
     *
     * @return true if the ant is in a node that represents a nest, false otherwise.
     */
    boolean isInNest();

    /**
     * True if the ant is currently in a node that belongs to the nest the ant belong to.
     *
     * @return true if ant is the home nest, false otherwise.
     */
    boolean isInHomeNest();
}
