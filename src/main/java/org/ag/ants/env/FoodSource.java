package org.ag.ants.env;

import java.awt.Color;
import java.awt.Dimension;

import org.ag.common.env.EnvironmentElement;

public class FoodSource extends EnvironmentElement {

	public FoodSource(String id, Dimension dimension, double totalAmountOfFood,
			Color colour) {
		super(id, dimension, colour, EnvironmentFactory.createFoodSourceGrid(
				id, dimension, totalAmountOfFood));
	}

	public FoodSource(String id, Dimension dimension, double totalAmountOfFood) {
		super(id, dimension, new Color(89, 133, 39), EnvironmentFactory
				.createFoodSourceGrid(id, dimension, totalAmountOfFood));
	}
}