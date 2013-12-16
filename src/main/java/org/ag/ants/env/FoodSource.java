package org.ag.ants.env;

import java.awt.Color;
import java.awt.Dimension;

import org.ag.common.env.BasicEnvironmentElement;

/**
 * Represents a food source, which is formed by a collection of <i>FoodSourceNode</i>
 *
 * @author Filipe Abrahao <me@luizfilipe.com>
 * @see FoodSourceNode
 */
public class FoodSource extends BasicEnvironmentElement {

    /**
     * Constructs a food source with an unique id, its dimension, the total amount of food present and the colour that
     * will be used when rendering the element onto the screen.
     *
     * @param id unique identifier.
     * @param dimension food source's dimension.
     * @param totalAmountOfFood amount of food that will be evenly spread between the food source's nodes.
     * @param colour colour the element will be rendered.
     */
    public FoodSource(final String id, final Dimension dimension, final double totalAmountOfFood, Color colour) {
        super(id, dimension, colour, EnvironmentFactory.createFoodSourceGrid(id, dimension, totalAmountOfFood));
    }

    /**
     * Constructs a food source with an unique id, its dimension, the total amount of food present. The element will be
     * assigned the default colour: (89, 133, 39).
     *
     * @param id unique identifier.
     * @param dimension food source's dimension.
     * @param totalAmountOfFood amount of food that will be evenly spread between the food source's nodes.
     */
    public FoodSource(final String id, final Dimension dimension, final double totalAmountOfFood) {
        super(id, dimension, new Color(89, 133, 39), EnvironmentFactory .createFoodSourceGrid(id, dimension,
                totalAmountOfFood));
    }
}