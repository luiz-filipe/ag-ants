package org.ag.ants.env;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

/**
 * A nest node is part of a ant nest. It is extends the BasicNode adding a propriety that represents the amount of food
 * that particular node is capable of storing. NestNodes are assembled to form ant nests.
 *
 * @see AntNest
 * @author Filipe Abrahao <me@luizfilipe.com>
 *
 */
@ThreadSafe
public class NestNode extends PheromoneNode {
    @GuardedBy("this") private double amountOfFoodHeld = 0;

    /**
     * Constructs a nest node with an unique identifier.
     * @param id node's unique identifier
     */
    public NestNode(String id) {
        super(id);
    }

    /**
     * Returns the amount of food present in the node.
     * @return amount of food present in the node.
     */

    public synchronized double getAmountOfFoodHeld() {
        return amountOfFoodHeld;
    }

    /**
     * Stores a given amount of food in the nest. Note that currently that are no limit on how much food a nest node
     * is capable of handling.
     *
     * @param amount amount of food to be deposited in the nest.
     */
    public synchronized void depositFood(final double amount) {
        this.amountOfFoodHeld += amount;
    }
}
