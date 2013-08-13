package org.ag.ants.simulation;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import org.ag.ants.env.AntNest;
import org.ag.ants.env.EnvironmentFactory;
import org.ag.ants.env.FoodSource;
import org.ag.ants.env.FoodSourceNode;
import org.ag.ants.env.NestNode;
import org.ag.common.simulation.AbsractEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AntEnvironment extends AbsractEnvironment {
	private static final Logger logger = LoggerFactory
			.getLogger(AntEnvironment.class);

	private final List<AntNest> nests;
	private final List<FoodSource> foodSources;

	public List<AntNest> getNests() {
		return nests;
	}

	public List<FoodSource> getFoodSources() {
		return foodSources;
	}

	public AntEnvironment(final int height, final int width) {
		super(EnvironmentFactory.createPheromoneGrid(new Dimension(height,
				width)), new Dimension(height, width));

		nests = new ArrayList<AntNest>();
		foodSources = new ArrayList<FoodSource>();
	}

	public void createNestAt(final String id, final int line, final int column,
			final Dimension dimension, final int maximumNumberOfAnts,
			final Color color) {

		// test if the new node would overlap an existent node
		for (int l = 0; l < dimension.height; l++) {
			for (int c = 0; c < dimension.width; c++) {
				if (this.getNodeAt(line + 1, column + 1) instanceof NestNode) {
					logger.error("Could not add nest [{}]. Specified "
							+ "position overlaps already existing nest.", id);

					return;
				}
			}
		}

		final AntNest nest = new AntNest(id, new Dimension(dimension.height,
				dimension.width), maximumNumberOfAnts, color);

		this.nests.add(nest);
		this.addEnvironmentElement(nest, line, column);
	}

	public void addFoodSourceAt(final String id, final int line,
			final int column, final Dimension dimension, final Color colour,
			final double totalAmountOfFood) {

		// test if the new node would overlap an existent node
		for (int l = 0; l < dimension.height; l++) {
			for (int c = 0; c < dimension.width; c++) {
				if (this.getNodeAt(line + 1, column + 1) instanceof FoodSourceNode) {
					logger.error("Could not add food source [{}]. Specified "
							+ "position overlaps already existing food source.", id);

					return;
				}
			}
		}
		
		final FoodSource fs = new FoodSource(id, dimension, totalAmountOfFood, colour);
		this.foodSources.add(fs);
		this.addEnvironmentElement(fs, line, column);
	}
}
