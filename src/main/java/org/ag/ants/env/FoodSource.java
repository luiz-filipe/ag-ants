package org.ag.ants.env;

import java.awt.Color;
import java.awt.Dimension;

import org.ag.common.env.BasicEnvironmentElement;
import org.ag.common.env.EnvironmentElement;

/**
 *
 */
public class FoodSource extends BasicEnvironmentElement {

    public FoodSource(final String id, final Dimension dimension, final double totalAmountOfFood, Color colour) {
        super(id, dimension, colour, EnvironmentFactory.createFoodSourceGrid(id, dimension, totalAmountOfFood));
    }

    public FoodSource(final String id, final Dimension dimension, final double totalAmountOfFood) {
        super(id, dimension, new Color(89, 133, 39), EnvironmentFactory .createFoodSourceGrid(id, dimension,
                totalAmountOfFood));
    }
}