package org.ag.ants.task;

/**
 * Adds the methods necessary to creat tasks that are going to be executed by 
 * ant agents.
 * 
 * @author Luiz Abrahao <luiz@luizabrahao.com>
 *
 */
public interface AntTask {
	/**
	 * Returns the selection weight for the agent in the north direction.
	 * 
	 * @return selection weight of north neighbour.
	 */
	double getNeighbourWeightNorth();

	/**
	 * Returns the selection weight for the agent in the north east.
	 * 
	 * @return selection weight of east neighbour.
	 */
	double getNeighbourWeightEast();
	
	/**
	 * Returns the selection weight for the agent in the south direction.
	 * 
	 * @return selection weight of south neighbour.
	 */
	double getNeighbourWeightSouth();
	
	/**
	 * Returns the selection weight for the agent in the west direction.
	 * 
	 * @return selection weight of west neighbour.
	 */
	double getNeighbourWeightWest();
}
