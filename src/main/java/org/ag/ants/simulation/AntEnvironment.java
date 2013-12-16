package org.ag.ants.simulation;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;
import org.ag.ants.env.AntNest;
import org.ag.ants.env.EnvironmentFactory;
import org.ag.ants.env.FoodSource;
import org.ag.ants.env.FoodSourceNode;
import org.ag.ants.env.NestNode;
import org.ag.common.simulation.AbstractEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implements an environment to be used for ant simulations. It adds some useful methods, for handling food sources and
 * ant nests for example.
 *
 * @author Luiz Filipe Abrahao <me@luizfilipe.com>
 */
@ThreadSafe
public class AntEnvironment extends AbstractEnvironment {
    private static final Logger logger = LoggerFactory.getLogger(AntEnvironment.class);

    @GuardedBy("this")
    private final List<AntNest> nests;
    @GuardedBy("this")
    private final List<FoodSource> foodSources;

    /**
     * Creates an environment formed by <i>PheromoneNode</i> nodes with the given dimension.
     *
     * @param dimension dimension of environment.
     */
    public AntEnvironment(final Dimension dimension) {
        super(EnvironmentFactory.createPheromoneGrid(dimension), dimension);

        nests = new ArrayList<AntNest>();
        foodSources = new ArrayList<FoodSource>();
    }

    /**
     * Returns a unmodifiable list of existing nests in the environment
     *
     * @return list of nests present in the environment.
     */
    public List<AntNest> getNests() {
        return Collections.unmodifiableList(nests);
    }

    /**
     * Returns a unmodifiable list of the food sources present in the environment.
     *
     * @return list of food sources.
     */
    public List<FoodSource> getFoodSources() {
        return Collections.unmodifiableList(foodSources);
    }

    /**
     * Creates and add a node to the environment at a given position. Before creating and adding the node to the
     * environment the method checks if another nest is present in the footprint that the new node will have, if that
     * is the case it will not add the node and log an error message.
     *
     * @param id nest's unique identifier
     * @param line line where the first row of nest nodes will be placed.
     * @param column column where the fist nest node will be placed.
     * @param dimension nest's dimension.
     * @param maximumNumberOfAnts maximum number of ants the nest can hold.
     * @param color the colour the nest will be rendered.
     */
    public synchronized void createNestAt(final String id, final int line, final int column, final Dimension dimension,
                             final int maximumNumberOfAnts, final Color color) {

        // test if the new node would overlap an existent node
        for (int l = 0; l < dimension.height; l++) {
            for (int c = 0; c < dimension.width; c++) {
                if (this.getNodeAt(line + 1, column + 1) instanceof NestNode) {
                    logger.error("Could not add nest [{}]. Specified  position overlaps already existing nest.", id);

                    return;
                }
            }
        }

        final AntNest nest = new AntNest(id, new Dimension(dimension.height, dimension.width), maximumNumberOfAnts,
                color);

        this.nests.add(nest);
        this.addEnvironmentElement(nest, line, column);
    }

    /**
     * Creates and add a food source to the environment at a given position. Before creating and adding the food source,
     * the method checks if another food source already exists at the given position, if that is the case, an error
     * message is logged and the new food source is discarded.
     *
     * @param id food source's unique id.
     * @param line line where the first row of food nodes will be placed.
     * @param column column where the fist food node will be placed.
     * @param dimension food source's dimension.
     * @param colour colour the food source will be rendered.
     * @param totalAmountOfFood total amount of food the source will hold.
     */
    public synchronized void addFoodSourceAt(final String id, final int line, final int column, final Dimension dimension,
                                final Color colour, final double totalAmountOfFood) {

        // test if the new node would overlap an existent node
        for (int l = 0; l < dimension.height; l++) {
            for (int c = 0; c < dimension.width; c++) {
                if (this.getNodeAt(line + 1, column + 1) instanceof FoodSourceNode) {
                    logger.error("Could not add food source [{}]. Specified  position overlaps already existing food " +
                            "source.", id);

                    return;
                }
            }
        }

        final FoodSource fs = new FoodSource(id, dimension, totalAmountOfFood, colour);
        this.foodSources.add(fs);
        this.addEnvironmentElement(fs, line, column);
    }
}