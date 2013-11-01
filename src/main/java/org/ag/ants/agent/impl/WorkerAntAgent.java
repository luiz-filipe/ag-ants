package org.ag.ants.agent.impl;

import org.ag.ants.env.FoodSourceNode;
import org.ag.ants.task.FindHomeNestTask;
import org.ag.ants.task.ForageTask;
import org.ag.common.env.Direction;
import org.ag.common.env.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A worker agent represents an ant from the Worker cast of an ant colony.
 *
 * @see ForageTask
 * @see FindHomeNestTask
 *
 * @author Filipe Abrahao <me@luizfilipe.com>
 */
public class WorkerAntAgent extends AntAgent {
    private static final Logger logger = LoggerFactory.getLogger(WorkerAntAgent.class);

    /**
     * Constructs an worker ant with an unique identifier, the node it will start from and the flag requesting it to
     * record the nodes it has visited or not.
     *
     * @param id agent's unique identifier
     * @param currentNode node agent will start from
     * @param recordNodeHistory if true, the agent keeps track of nodes it has visited.
     */
    public WorkerAntAgent(final String id, final Node currentNode, final boolean recordNodeHistory) {
        super(id, WorkerAntType.TYPE, currentNode, recordNodeHistory);
    }

    /**
     * Workers forage for food sources in the environment using the <i>ForageTask</i>, when they come across a food
     * source they try to collect food from the food source, if successful they switch to the <i>FindHomeNestTask</i>
     * task. If the ant reaches a node part of their home nest they deposit the food they are carrying into the nest
     * and switch back to the <i>ForageTask</i>.
     *
     * @return Void
     * @throws Exception if ant cannot complete its tasks.
     */
    @Override
    public Void call() throws Exception {
        while (!Thread.currentThread().isInterrupted()) {
            if (!this.isCarryingFood()) {
                logger.trace("{} is not carrying food, so ForageTask will be executed...", this.getId());
                this.getTaskByName(ForageTask.NAME).execute(this);

                // if the agent is already in a food source let it pick-up some of it.
                if (this.getCurrentNode() instanceof FoodSourceNode) {
                    logger.trace("{} collected food.", this.getId());
                    this.collectFood();
                }

                // if the agent is not in a food source, it should look at the environment around it, checking if there
                // is food around.
                final Direction directionToFood = this.isThereFoodAround();

                if (directionToFood != null) {
                    this.moveToNeighbour(directionToFood);
                    logger.trace("{} collected food.", this.getId());
                    this.collectFood();
                }

            } else {
                logger.trace("{} is carrying food, so FindHomeNestTask will be executed...", this.getId());
                this.getTaskByName(FindHomeNestTask.NAME).execute(this);

                if (this.isInHomeNest()) {
                    logger.trace("{} deposited food.", this.getId());
                    this.depositFood();
                }
            }

            try {
                Thread.sleep(this.getAgentType().getMillisecondsToWait());

            } catch (InterruptedException e) {
                // reset the flag to allow thread terminate on its own...
                Thread.currentThread().interrupt();
            }
        }

        logger.info("[{}] asked to stop...", this.getId());
        return null;
    }

    /**
     * Increases the worker capability of finding food, by looking for food in all the nodes around it instead only at
     * the node it is currently in.
     *
     * @return direction of food source around agent.
     */
    private Direction isThereFoodAround() {
        for (Direction direction : Direction.values()) {
            final Node neighbour = this.getCurrentNode().getNeighbour(direction);

            if (neighbour instanceof FoodSourceNode) {
                return direction;
            }
        }

        return null;
    }

}
