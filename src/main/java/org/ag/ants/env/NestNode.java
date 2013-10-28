package org.ag.ants.env;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

/**
 * A nest node is part of a ant nest. It is extends the BasicNode adding a
 * propriety that represents the amount of food that particular node is capable
 * of storing.
 * 
 * NestNodes are assembled to form ant nests.
 * 
 * @see AntNest
 * @author Luiz Abrahao <luiz@luizabrahao.com>
 *
 */
@ThreadSafe
public class NestNode extends PheromoneNode {
	@GuardedBy("this") private double amountOfFoodHeld = 0;

	public synchronized double getAmountOfFoodHeld() {
		return amountOfFoodHeld;
	}

	public NestNode(String id) {
		super(id);
	}
	
	public synchronized void depositFood(final double amount) {
		this.amountOfFoodHeld += amount;
	}
}
