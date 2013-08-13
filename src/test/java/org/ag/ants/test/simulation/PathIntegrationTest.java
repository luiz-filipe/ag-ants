package org.ag.ants.test.simulation;

import java.awt.Color;
import java.awt.Dimension;
import java.util.concurrent.TimeUnit;

import org.ag.ants.simulation.AntEnvironment;
import org.ag.ants.simulation.AntSimulation;

public class PathIntegrationTest {
	public static void main(String[] args) {
		final AntEnvironment env = new AntEnvironment(500, 500);
		final AntSimulation sim = new AntSimulation("target/", env, 100);
		
		env.createNestAt("nest", env.getHeight() / 2 - 5, env.getWidth() / 2 - 5, new Dimension(10, 10), 100, Color.BLUE);
		env.addFoodSourceAt("fs-01", 100, 100, new Dimension(10, 10), Color.GREEN, 30.0);
		env.addFoodSourceAt("fs-02", 45, 245, new Dimension(10, 10), Color.GREEN, 30.0);
		env.addFoodSourceAt("fs-03", 100, 400, new Dimension(10, 10), Color.GREEN, 30.0);
		env.addFoodSourceAt("fs-04", 245, 445, new Dimension(10, 10), Color.GREEN, 30.0);
		env.addFoodSourceAt("fs-05", 400, 400, new Dimension(10, 10), Color.GREEN, 30.0);
		env.addFoodSourceAt("fs-06", 445, 245, new Dimension(10, 10), Color.GREEN, 30.0);
		env.addFoodSourceAt("fs-07", 400, 100, new Dimension(10, 10), Color.GREEN, 30.0);
		env.addFoodSourceAt("fs-08", 245, 45, new Dimension(10, 10), Color.GREEN, 30.0);
		
		
		sim.scheduleEnvironmentElementRenderer("env-0.png", 0, TimeUnit.SECONDS);
		
		sim.run(1, TimeUnit.SECONDS);
	}
}
