package org.ag.ants.env;

import net.jcip.annotations.GuardedBy;

import org.ag.common.env.BasicNode;

public class NestNode extends BasicNode {
	@GuardedBy("this") private double amountOfFoodHeld = 0;

	public synchronized double getAmountOfFoodHeld() {
		return amountOfFoodHeld;
	}

	public NestNode(String id) {
		super(id);
	}
	
	public synchronized void depositFood(double amount) {
		this.amountOfFoodHeld += amount;
	}
}
