package org.ag.ants.env;

import net.jcip.annotations.GuardedBy;

import org.ag.common.env.BasicNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//TODO document
public class FoodSourceNode extends BasicNode {
	private static final Logger logger = LoggerFactory
			.getLogger(FoodSourceNode.class);

	@GuardedBy("this")
	private double amountOfFoodAvailable;

	public FoodSourceNode(final String id, final double amountOfFood) {
		super(id);
		this.amountOfFoodAvailable = amountOfFood;
	}

	public synchronized double getAmountOfFoodAvailable() {
		return this.amountOfFoodAvailable;
	}

	public synchronized double collectFood(final double amount) {
		if (this.amountOfFoodAvailable == 0) {
			logger.trace("{}: there is no more food to be collected.",
					this.getId());
			return 0;
		}

		if (this.amountOfFoodAvailable > amount) {
			this.amountOfFoodAvailable = this.amountOfFoodAvailable - amount;
			logger.trace("{}: {} of food collected.", this.getId(), amount);
			
			return amount;

		} else {
			double amountAvailable = this.amountOfFoodAvailable;
			this.amountOfFoodAvailable = 0;
			logger.trace("{}: {} of food collected.", this.getId(),
					amountAvailable);
			
			return amountAvailable;
		}
	}
}
