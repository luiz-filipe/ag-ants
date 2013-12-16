package org.ag.ants.env;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import org.ag.common.env.BasicNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A FoodSourceNode object represents a node that contain some food. The amount of food is set at creation time and ants
 * that enter that node are able to collect food from it.
 *
 * <p>If the node has ran out of food and an ant tries to collect food from it, the node gives 'zero' food for the
 * agent, no exception is risen.</p>
 *
 * @author Filipe Abrahao <me@luizfilipe.com>
 */
@ThreadSafe
public class FoodSourceNode extends PheromoneNode {
    private static final Logger logger = LoggerFactory.getLogger(FoodSourceNode.class);

    @GuardedBy("this")
    private double amountOfFoodAvailable;

    /**
     * Constructs a food source node with an unique identifier and the amount of food the node will hold from the start.
     *
     * @param id node's unique identifier.
     * @param amountOfFood amount of food node will hold from start.
     */
    public FoodSourceNode(final String id, final double amountOfFood) {
        super(id);
        this.amountOfFoodAvailable = amountOfFood;
    }

    /**
     * Returns the amount of food present in the node.
     *
     * @return amount of food still available in the node.
     */
    public synchronized double getAmountOfFoodAvailable() {
        return this.amountOfFoodAvailable;
    }

    /**
     * Tries to collect a given amount of food from the node. If the node has less food than the amount requested it
     * returns only the total of food available, if there is no food left it will return 0.
     *
     * @param amount amount of food the agent is trying to collect.
     * @return amount of food collected.
     */
    public synchronized double collectFood(final double amount) {
        if (this.amountOfFoodAvailable == 0) {
            logger.trace("{}: there is no more food to be collected.", this.getId());
            return 0;
        }

        if (this.amountOfFoodAvailable > amount) {
            this.amountOfFoodAvailable = this.amountOfFoodAvailable - amount;
            logger.trace("{}: {} of food collected.", this.getId(), amount);

            return amount;

        } else {
            double amountAvailable = this.amountOfFoodAvailable;
            this.amountOfFoodAvailable = 0;
            logger.trace("{}: {} of food collected.", this.getId(), amountAvailable);

            return amountAvailable;
        }
    }

    @Override
    public String toString() {
        return "FoodSourceNode [id = " + getId() + ", amountOfFoodAvailable=" + amountOfFoodAvailable + "]";
    }


}
