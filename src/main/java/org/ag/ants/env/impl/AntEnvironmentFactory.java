package org.ag.ants.env.impl;

import java.util.ArrayList;
import java.util.List;

import org.ag.ants.agent.impl.AntNestAgent;
import org.ag.ants.env.PheromoneNode;
import org.ag.common.env.Direction;
import org.ag.common.env.Node;

import net.jcip.annotations.ThreadSafe;

/**
 * Utility class for creation of environments to be used in ant simulations.
 * 
 * @author Luiz Abrahao <luiz@luizabrahao.con>
 *
 */
@ThreadSafe
public class AntEnvironmentFactory {
	private AntEnvironmentFactory() {}
	
	/**
	 * Initialises an environment based on PheromoneNode objects.
	 * This environment has rectangular shape and each node are assigned an 
	 * identifier following the pattern: "n+lineNumber,colunmNumber",
	 * e.g. "n3,2" corresponds to the node at the third line and second column.
	 * 
	 * @param nLines number of lines the grid will contain
	 * @param nColunms number of column the grid will contain.
	 * @return A two dimensional array of interconnected PheromoneNode objects.
	 */
	public static PheromoneNode[][] createPheromoneNodeGrid(int nLines, int nColumns) {
		PheromoneNode[][] nodes = new PheromoneNode[nLines][nColumns];
		
		for(int l = 0; l < nLines; l++) {
			for (int c = 0; c < nColumns; c++) {
				nodes[l][c] = new PheromoneNode("n" + l + "," + c);
				
				if (c != 0) {
					nodes[l][c].setNeighbours(Direction.WEST, nodes[l][c - 1]);
				}
				
				if (l != 0) {
					nodes[l][c].setNeighbours(Direction.NORTH, nodes[l - 1][c]);
				}
			}
		}
		
		return nodes;
	}

	/**
	 * Creates a row of food sources and place them on the environment starting
	 * from the <em>initialNode</em>.
	 * 
	 * @param initialNode The node where the row of food sources will start
	 * @param numberOfSources number of food sources to be created
	 * @param amountOfFoodInEachSource amount of food held in each food source
	 * @return list of food sources created
	 */
	public static List<FoodSourceAgent> placeRowOfFoodSources(Node initialNode, int numberOfSources, double amountOfFoodInEachSource) {
		List<FoodSourceAgent> foodSources = new ArrayList<FoodSourceAgent>();
		Node currentNode = initialNode;
		
		for (int i = 0; i < numberOfSources; i++) {
			foodSources.add(new FoodSourceAgent("food-source-" + i, currentNode, amountOfFoodInEachSource));
			
			currentNode = currentNode.getNeighbour(Direction.EAST);
			
			if (currentNode == null) {
				break;
			}
		}
		
		return foodSources;
	}

	/**
	 * Utility method to count the total amount of food held by nests contained
	 * in a list of AntNestAgent objects. 
	 * @param nests list of nests
	 * 
	 * @return total amount of food hold in all nests of the list.
	 * @see AntNestAgent
	 */
	public static double sumFoodCollected(List<AntNestAgent> nests) {
		double result = 0;
		
		for (AntNestAgent nest : nests) {
			result = result + nest.getAmountOfFoodHeld();
		}
		
		return result;
	}
}
