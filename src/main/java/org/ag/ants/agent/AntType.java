package org.ag.ants.agent;

import org.ag.common.agent.Agent;
import org.ag.common.agent.TaskAgentType;

/**
 * Formalises the public API for ant agents. Ants are implemented having limited
 * memory size and limited capability of collecting food. Different types of 
 * ants have different memory sizes and can collect different amounts of food.
 * Also the different types of ant interact differently with the environment so
 * each type of ants define a list of chemical stimulus and how much of each of
 * them the agent is able to lay in every interaction. 
 * 
 * Ant agent type that implements this interface should be declared as
 * enumeration, therefore they are singletons by definition.
 * 
 * @author Luiz Abrahao <luiz@luizabrahao.com>
 *
 */
public interface AntType extends TaskAgentType {
	/**
	 * Returns the amount of chemical stimulus that the ant type is enable of
	 *         depositing in each time.
	 * 
	 * @param chemicalCommStimulusTypeName Name of the chemical communications
	 *        stimulus
	 * @return amount that agent type is capable of deposit for that particular
	 *         chemical stimulus
	 */
	double getStimulusIncrement(String chemicalCommStimulusTypeName);
	
	/**
	 * Returns the amount of nodes the agent type is capable of remembering
	 * being in.
	 * @return amount of nodes agent is able to remember.
	 */
	int getMemorySize();
	
	/**
	 * In this method the agent type uses the agent's local context to decide
	 * which task the agent will run next. 
	 * 
	 * @param agent Agent that will execute the task.
	 */
	void execute(Agent agent);
	
	/**
	 * Returns the amount of food the agent type is capable of collecting when
	 * the agent finds a food source.
	 * 
	 * @return Amount of food the agent is capable of collecting from a food
	 * 		   source
	 */
	double getAmountOfFoodCapableToCollect();
}
