package org.ag.ants.env;

import java.awt.Color;
import java.awt.Dimension;

import org.ag.common.env.EnvironmentElement;

/**
 * An AntNest is composed by an array of nestNode objects that represent an ant
 * nest.
 * 
 * @author Luiz Abrahao <luiz@luizabrahao.com>
 * 
 */
public class AntNest extends EnvironmentElement {
	public AntNest(final String id, final Dimension dimension,
			final Color colour) {

		super(id, dimension, colour, EnvironmentFactory.createNestGrid(id,
				dimension));
	}

	public AntNest(final String id, final Dimension dimension) {
		super(id, dimension, new Color(140, 98, 57), EnvironmentFactory
				.createNestGrid(id, dimension));
	}

	public double getTotalFoodHeld() {
		double total = 0;

		for (int l = 0; l < this.getDimension().height; l++) {
			for (int c = 0; c < this.getDimension().width; c++) {
				total = total
						+ ((NestNode) this.getNode(l, c)).getAmountOfFoodHeld();
			}
		}

		return total;
	}
}
