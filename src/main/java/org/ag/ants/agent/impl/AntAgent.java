package org.ag.ants.agent.impl;

import java.util.LinkedList;
import java.util.Queue;

import org.ag.ants.agent.Ant;
import org.ag.ants.agent.AntType;
import org.ag.ants.env.ChemicalCommStimulusType;
import org.ag.ants.env.FoodSourceNode;
import org.ag.ants.env.NestNode;
import org.ag.ants.env.PheromoneNode;
import org.ag.common.agent.TaskAgent;
import org.ag.common.env.Coordinate;
import org.ag.common.env.Direction;
import org.ag.common.env.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

/**
 * Defines an agent that represents a ant. The movingDirection field represents the direction the agent is moving in
 * relation to the grid of nodes. This direction is used when the algorithm is calculating the probabilities of the
 * agent selecting what node it will move next.
 *
 * @author Luiz Abrahao <luiz@luizabrahao.com>
 * @see org.ag.ants.task.ForageTask
 */
@ThreadSafe
public abstract class AntAgent extends TaskAgent implements Ant {
    private static final Logger logger = LoggerFactory.getLogger(AntAgent.class);

    @GuardedBy("this")
    private Direction movingDirection;
    @GuardedBy("this")
    protected final Queue<Node> memory;
    @GuardedBy("this")
    private double amountOfFoodCarrying = 0;
    @GuardedBy("this")
    private int linesOffsetFromNest;
    @GuardedBy("this")
    private int columnsOffsetFromNest;

    /**
     * Constructs an ant with a unique identifier, a type, the node it will start from and a flag to request if the ant
     * should keep track of the nodes it has been or not.
     *
     * @param id ant's unique identifier.
     * @param agentType ant's type
     * @param currentNode node the ant will start from.
     * @param recordNodeHistory requests the agent to keep track of visited node if true.
     */
    public AntAgent(final String id, final AntType agentType, final Node currentNode, final boolean recordNodeHistory) {
        super(id, agentType, currentNode, recordNodeHistory);

        memory = new LinkedList<Node>();
        this.movingDirection = Direction.SOUTH;
    }

    @Override
    public synchronized Direction getMovingDirection() {
        return movingDirection;
    }

    @Override
    public AntType getAgentType() {
        return (AntType) super.getAgentType();
    }

    @Override
    public double collectFood() {
        this.amountOfFoodCarrying = ((FoodSourceNode) super.getCurrentNode()).collectFood((this.getAgentType())
                .getAmountOfFoodCapableToCollect());

        return this.amountOfFoodCarrying;
    }

    @Override
    public synchronized boolean isCarryingFood() {
        return (amountOfFoodCarrying != 0) ? true : false;
    }

    @Override
    public void incrementStimulusIntensity(final ChemicalCommStimulusType chemicalCommStimulusType) {

        // if the agent is in a nest it does not increment the chemical stimulus
        if (this.isInNest()) {
            return;
        }

        // update current node chemical stimulus intensity
        PheromoneNode currentNode = (PheromoneNode) this.getCurrentNode();
        this.updateNeighbour(currentNode, chemicalCommStimulusType, 0);

        // if the chemical stimulus is punctual, that is, it does not spread across to its neighbour nodes we don't need
        // to to anything else.
        if (chemicalCommStimulusType.getRadius() == 0) {
            return;
        }

        // if radius == 1, it's easier to update the only 4 neighbours that will receive an increase in the stimulus
        if (chemicalCommStimulusType.getRadius() == 1) {
            this.updateNeighbours(chemicalCommStimulusType, Direction.NORTH);
            this.updateNeighbours(chemicalCommStimulusType, Direction.EAST);
            this.updateNeighbours(chemicalCommStimulusType, Direction.SOUTH);
            this.updateNeighbours(chemicalCommStimulusType, Direction.WEST);

            return;
        }

        // updates the main rows and columns
        this.updateNeighbours(chemicalCommStimulusType, Direction.NORTH);
        this.updateNeighbours(chemicalCommStimulusType, Direction.EAST);
        this.updateNeighbours(chemicalCommStimulusType, Direction.SOUTH);
        this.updateNeighbours(chemicalCommStimulusType, Direction.WEST);

        // updates the nodes that don't fall in the same axis than the current node
        this.updateNeighbours(chemicalCommStimulusType, Direction.NORTH, Direction.EAST);
        this.updateNeighbours(chemicalCommStimulusType, Direction.NORTH, Direction.WEST);
        this.updateNeighbours(chemicalCommStimulusType, Direction.SOUTH, Direction.EAST);
        this.updateNeighbours(chemicalCommStimulusType, Direction.SOUTH, Direction.WEST);
    }

    @Override
    public void incrementStimulusIntensityMultipliedByFactor(
            final ChemicalCommStimulusType chemicalCommStimulusType,
            final int factor) {

        for (int i = 0; i < factor; i++) {
            this.incrementStimulusIntensity(chemicalCommStimulusType);
        }
    }

    /**
     * Updates the neighbour nodes in the same axis that the current node, that is, all nodes at the same line and same
     * column of the current node.
     *
     * @param chemicalCommStimulusType type of stimulus that will be updated.
     * @param direction direction of the neighbour nodes in relation to the current node.
     */
    private void updateNeighbours(final ChemicalCommStimulusType chemicalCommStimulusType, final Direction direction) {

        PheromoneNode currentNode = (PheromoneNode) this.getCurrentNode();

        for (int i = 0; i < chemicalCommStimulusType.getRadius(); i++) {
            if (currentNode == null) {
                break;
            }

            currentNode = (PheromoneNode) currentNode.getNeighbour(direction);

            if (currentNode != null) {
                this.updateNeighbour(currentNode, chemicalCommStimulusType, i + 1);
            }
        }
    }

    /**
     * Updates the chemical stimulus intensity of the neighbour nodes of the current node in relation to a vertical and
     * horizontal direction. If we assume the current to be the node (0,0), nodes to the right will be the nodes in the
     * EAST direction, and nodes above the current node will be in the NORTH direction. The way the intensity update
     * works, it updates all nodes that are above-right the, all nodes to the above-left, then all below-right and
     * finally the nodes below-left.
     *
     * @param chemicalCommStimulusType type of chemical stimulus that will be updated.
     * @param verticalDirection vertical direction of the nodes being updated, in relation to the current node.
     * @param horizontalDirection horizontal direction of the nodes being updated, in relation to the current node.
     */
    private void updateNeighbours(final ChemicalCommStimulusType chemicalCommStimulusType,
                                  final Direction verticalDirection,
                                  final Direction horizontalDirection) {

        PheromoneNode currentLineNode = (PheromoneNode) this.getCurrentNode().getNeighbour(verticalDirection);
        PheromoneNode currentNode = currentLineNode;

        for (int i = 0; i < chemicalCommStimulusType.getRadius() - 1; i++) {
            for (int j = 0; j < chemicalCommStimulusType.getRadius() - 1; j++) {
                if (currentNode == null) {
                    break;
                }

                currentNode = (PheromoneNode) currentNode.getNeighbour(horizontalDirection);

                if (currentNode != null) {
                    if (i + j < chemicalCommStimulusType.getRadius() - 1) {
                        this.updateNeighbour(currentNode, chemicalCommStimulusType, i + j + 1);
                    }
                }
            }

            if (currentNode == null) {
                break;
            }

            currentLineNode = (PheromoneNode) currentLineNode.getNeighbour(verticalDirection);
            currentNode = currentLineNode;
        }
    }

    /**
     * Updates given node's chemical stimulus of the given type based on the distance of the node to the agent's current
     * node. The further away is the node from the agent's current node the lesser will be the increase in the stimulus'
     * intensity.
     *
     * <p>The intensity update varies according to the following relation:</p>
     * <p>new_intensity = current_intensity + increase ∗ (1 / distance)</p>
     *
     * @param node node that will have the stimulus intensity's updated
     * @param chemicalCommStimulusType the type of stimulus that will be updated
     * @param distanceFromCurrentNode distance of the updated node to the agent's current node.
     */
    private void updateNeighbour(final PheromoneNode node,
                                 final ChemicalCommStimulusType chemicalCommStimulusType,
                                 final int distanceFromCurrentNode) {

        if (node == null) {
            return;
        }

        if (distanceFromCurrentNode == 0) {
            node.getCommunicationStimulus(chemicalCommStimulusType).increaseIntensity(
                    this.getAgentType().getStimulusIncrement(chemicalCommStimulusType.getName()));

            logger.trace("Node {} updated with {}", node.getId(), this.getAgentType().getStimulusIncrement(
                    chemicalCommStimulusType.getName()));

            return;
        }

        node.getCommunicationStimulus(chemicalCommStimulusType).increaseIntensity(
                this.getAgentType().getStimulusIncrement(chemicalCommStimulusType.getName()) / distanceFromCurrentNode);

        logger.trace("Node {} updated with {}", node.getId(),
                this.getAgentType().getStimulusIncrement(chemicalCommStimulusType.getName()) / distanceFromCurrentNode);
    }

    @Override
    public void addToMemory(Node node) {
        memory.add(node);

        if (memory.size() > this.getAgentType().getMemorySize()) {
            memory.poll();
        }
    }

    @Override
    public Node getLatestNodeFromMemory() {
        return this.memory.poll();
    }

    @Override
    public synchronized void depositFood() {
        ((NestNode) super.getCurrentNode())
                .depositFood(this.amountOfFoodCarrying);
    }

    @Override
    public Coordinate getVectorToNest() {
        return new Coordinate(-1 * linesOffsetFromNest,
                -1 * columnsOffsetFromNest);
    }

    protected void executePathIntegration(Direction direction) {
        switch (direction) {
            case NORTH:
                linesOffsetFromNest++;
                break;

            case NORTH_EAST:
                linesOffsetFromNest++;
                columnsOffsetFromNest++;
                break;

            case EAST:
                columnsOffsetFromNest++;
                break;

            case SOUTH_EAST:
                linesOffsetFromNest--;
                columnsOffsetFromNest++;
                break;

            case SOUTH:
                linesOffsetFromNest--;
                break;

            case SOUTH_WEST:
                linesOffsetFromNest--;
                columnsOffsetFromNest--;
                break;

            case WEST:
                columnsOffsetFromNest--;
                break;

            case NORTH_WEST:
                linesOffsetFromNest++;
                columnsOffsetFromNest--;
                break;
        }
    }

    @Override
    public void moveToNeighbour(Direction direction) {
        this.getCurrentNode().getNeighbour(direction).addAgent(this);
        this.movingDirection = direction;

        this.executePathIntegration(direction);
        this.addToMemory(this.getCurrentNode().getNeighbour(direction));
    }

    @Override
    public boolean isInNest() {
        return (this.getCurrentNode() instanceof NestNode);
    }

    @Override
    public boolean isInHomeNest() {
        final Coordinate c = this.getVectorToNest();

        return (this.getCurrentNode() instanceof NestNode) &&
                ((c.getColumn() == 0) && (c.getLine() == 0));
    }
}
