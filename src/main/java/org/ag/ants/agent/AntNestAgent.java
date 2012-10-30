package org.ag.ants.agent;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import org.ag.common.agent.AbstractAgent;
import org.ag.common.env.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Objects of this class represent ant nests. They have a special property named
 * amountOfFoodHeld, which contains the total amount of food that is held in a
 * particular nest. 
 * 
 * This agent should not be executed as a Java task. If a user tries to do so, a
 * RuntimeExecption is thrown.
 * 
 * @author Luiz Abrahao <luiz@luizabrahao.com>
 *
 */
@ThreadSafe
public class AntNestAgent extends AbstractAgent {
	private static final Logger logger = LoggerFactory.getLogger(AntNestAgent.class);
	@GuardedBy("this") private double amountOfFoodHeld = 0;
	
	public AntNestAgent(String id, Node currentNode) {
		super(id, AntNestType.TYPE, currentNode, false);
	}

	public synchronized double getAmountOfFoodHeld() { return amountOfFoodHeld; }

	@Override
	public Void call() throws Exception {
		throw new RuntimeException("Nests are not to be used as threads... " +
				"They just take advantage of the infrastructure of agents");
	}

	public void addPortionOfFood(final AntAgent agent, final double portion) {
		synchronized (this) {
			amountOfFoodHeld = amountOfFoodHeld + portion;
		}
		
		logger.debug(this.getId() + ": {} deposited {} of food.", agent.getId(), portion);
	}
}
