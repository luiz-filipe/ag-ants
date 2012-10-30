package org.ag.ants.env;

import org.ag.common.env.CommunicationStimulusType;

/**
 * An abstraction of communication stimulus that represent chemical substances.
 * 
 * @author Luiz Abrahao <luiz@luizabrahao.com>
 *
 */
public interface ChemicalCommStimulusType extends CommunicationStimulusType {
	/**
	 * <p>Chemical stimulus decay over time. This method returns the factor that
	 * is used to calculate the new stimulus' intensity after one decaying 
	 * interaction.</p> 
	 * 
	 * <p>The new intensity after decay is calculated as follow:</p>
	 * 
	 * <p>intensity_new = intensity_current * (1 - decayFactor)</p>
	 * 
	 * @return decay factor for the chemical stimulus.
	 */
	double getDecayFactor();
	
	/**
	 * <p>Chemical stimulus have a radius of reach, this method returns how many
	 * nodes from the current agent's node will be affected by deposit of this
	 * stimulus</p>
	 * 
	 * @return number of nodes that will be updated around the agent's current
	 *         node.
	 */
	int getRadius();
}
