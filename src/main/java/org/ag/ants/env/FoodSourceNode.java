package org.ag.ants.env;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import org.ag.common.env.BasicNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A FoodSourceNode object represents a node that contain some food. The amount
 * of food is set at creation time and ants that enter that node are able to 
 * collect food from it.
 * 
 * If the node has ran out of food and an ant tries to collect food from it, the
 * node gives 'zero' food for the agent, no exception is risen.
 * 
 * @author Luiz Abrahao <luiz@luizabrahao.com>
 */
@ThreadSafe
public class FoodSourceNode extends PheromoneNode {
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

	@Override
	public String toString() {
		return "FoodSourceNode [id = " + getId()
				+ ", amountOfFoodAvailable=" + amountOfFoodAvailable + "]";
	}
	
	
}
