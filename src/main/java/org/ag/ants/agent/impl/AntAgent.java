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
 * Defines an agent that represents a ant. The movingDirection field represents
 * the direction the agent is moving in relation to the grid of nodes. This
 * direction is used when the algorithm is calculating the probabilities of the
 * agent selecting what node it will move next.
 * 
 * @author Luiz Abrahao <luiz@luizabrahao.com>
 * @see ForageTask
 */
@ThreadSafe
public abstract class AntAgent extends TaskAgent implements Ant {
	private static final Logger logger = LoggerFactory
			.getLogger(AntAgent.class);

	@GuardedBy("this")
	private Direction movingDirection;
	@GuardedBy("this")
	protected final Queue<Node> memory;
	@GuardedBy("this")
	private double amountOfFoodCarring = 0;
	@GuardedBy("this")
	private int linesOffsetFromNest;
	@GuardedBy("this")
	private int columnsOffsetFromNest;

	@Override
	public synchronized Direction getMovingDirection() {
		return movingDirection;
	}

	public AntAgent(final String id, final AntType agentType,
			final Node currentNode, final boolean recordNodeHistory) {
		super(id, agentType, currentNode, recordNodeHistory);

		memory = new LinkedList<Node>();
		this.movingDirection = Direction.SOUTH;
	}

	@Override
	public AntType getAgentType() {
		return (AntType) super.getAgentType();
	}

	@Override
	public double collectFood() {
		//TODO re-think how should collect and deposit food.... don't think it's
		// a good a idea to do casting like this... or at least need to think
		// about synchronisation because these methods will be called from
		// outside the agent.
		this.amountOfFoodCarring = ((FoodSourceNode) super.getCurrentNode())
				.collectFood(((AntType) this.getAgentType())
						.getAmountOfFoodCapableToCollect());

		return this.amountOfFoodCarring;
	}

	@Override
	public synchronized boolean isCarryingFood() {
		return (amountOfFoodCarring != 0) ? true : false;
	}

	@Override
	public void incrementStimulusIntensity(
			final ChemicalCommStimulusType chemicalCommStimulusType) {
		PheromoneNode currentNode = (PheromoneNode) this.getCurrentNode();
		this.updateNeighbour(currentNode, chemicalCommStimulusType, 0);

		// if the chemical stimulus is punctual, that is, it does not spread
		// across to its nodes neighbours we don't need to to anything else.
		if (chemicalCommStimulusType.getRadius() == 0) {
			return;
		}

		// if radius == 1, only main rows and columns will be updated, so let's
		// call to only them to be updated and return after that, to save
		// computational resources as much as we can
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

		this.updateNeighbours(chemicalCommStimulusType, Direction.NORTH,
				Direction.EAST);
		this.updateNeighbours(chemicalCommStimulusType, Direction.NORTH,
				Direction.WEST);
		this.updateNeighbours(chemicalCommStimulusType, Direction.SOUTH,
				Direction.EAST);
		this.updateNeighbours(chemicalCommStimulusType, Direction.SOUTH,
				Direction.WEST);
	}

	@Override
	public void incrementStimulusIntensityMultipliedByFactor(
			final ChemicalCommStimulusType chemicalCommStimulusType,
			final int factor) {

		for (int i = 0; i < factor; i++) {
			this.incrementStimulusIntensity(chemicalCommStimulusType);
		}
	}

	private void updateNeighbours(
			final ChemicalCommStimulusType chemicalCommStimulusType,
			final Direction direction) {

		PheromoneNode currentNode = (PheromoneNode) this.getCurrentNode();

		for (int i = 0; i < chemicalCommStimulusType.getRadius(); i++) {
			if (currentNode == null) {
				break;
			}

			currentNode = (PheromoneNode) currentNode.getNeighbour(direction);

			if (currentNode != null) {
				this.updateNeighbour(currentNode, chemicalCommStimulusType,
						i + 1);
			}
		}
	}

	private void updateNeighbours(
			final ChemicalCommStimulusType chemicalCommStimulusType,
			final Direction verticalDirection,
			final Direction horizontalDirection) {

		PheromoneNode currentLineNode = (PheromoneNode) this.getCurrentNode()
				.getNeighbour(verticalDirection);
		PheromoneNode currentNode = currentLineNode;

		for (int i = 0; i < chemicalCommStimulusType.getRadius() - 1; i++) {
			for (int j = 0; j < chemicalCommStimulusType.getRadius() - 1; j++) {
				if (currentNode == null) {
					break;
				}

				currentNode = (PheromoneNode) currentNode
						.getNeighbour(horizontalDirection);

				if (currentNode != null) {
					if (i + j < chemicalCommStimulusType.getRadius() - 1) {
						this.updateNeighbour(currentNode,
								chemicalCommStimulusType, i + j + 1);
					}
				}
			}

			if (currentNode == null) {
				break;
			}

			currentLineNode = (PheromoneNode) currentLineNode
					.getNeighbour(verticalDirection);
			currentNode = currentLineNode;
		}
	}

	private void updateNeighbour(final PheromoneNode node,
			final ChemicalCommStimulusType chemicalCommStimulusType,
			final int distanceFromCurrentNode) {

		if (node == null) {
			return;
		}

		if (distanceFromCurrentNode == 0) {
			node.getCommunicationStimulus(chemicalCommStimulusType)
					.increaseIntensity(
							this.getAgentType().getStimulusIncrement(
									chemicalCommStimulusType.getName()));
			logger.trace(
					"Node {} updated with {}",
					node.getId(),
					this.getAgentType().getStimulusIncrement(
							chemicalCommStimulusType.getName()));

			return;
		}

		node.getCommunicationStimulus(chemicalCommStimulusType)
				.increaseIntensity(
						this.getAgentType().getStimulusIncrement(
								chemicalCommStimulusType.getName())
								/ distanceFromCurrentNode);
		logger.trace(
				"Node {} updated with {}",
				node.getId(),
				this.getAgentType().getStimulusIncrement(
						chemicalCommStimulusType.getName())
						/ distanceFromCurrentNode);
	}

	@Override
	public void addToMemory(Node node) {
		memory.add(node);

		if (memory.size() > this.getAgentType().getMemorySize()) {
			memory.poll();
		}
	}

	public Node getLatestNodeFromMemory() {
		return this.memory.poll();
	}

	@Override
	public synchronized void depositFood() {
		((NestNode) super.getCurrentNode())
				.depositFood(this.amountOfFoodCarring);
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
		this.executePathIntegration(direction);
		this.addToMemory(this.getCurrentNode().getNeighbour(direction));
	}
}
