package org.ag.test.ants.experiment;

import java.awt.Color;
import java.awt.Dimension;
import java.util.concurrent.TimeUnit;

import org.ag.ants.env.impl.ForageStimulusType;
import org.ag.ants.simulation.AntEnvironment;
import org.ag.ants.simulation.AntSimulation;

public class RoundFoodSources {
    public static void main(String[] args) {
        final AntEnvironment env = new AntEnvironment(new Dimension(500, 500));
        final AntSimulation sim = new AntSimulation("target/", env);
        final Color envColour = new Color(255, 255, 255, 0);

        env.createNestAt("nest", env.getHeight() / 2 - 5, env.getWidth() / 2 - 5, new Dimension(10, 10), 100,
                Color.BLUE);

        env.addFoodSourceAt("fs-01", 100, 100, new Dimension(10, 10), Color.GREEN, 30.0);
        env.addFoodSourceAt("fs-02", 45, 245, new Dimension(10, 10), Color.GREEN, 30.0);
        env.addFoodSourceAt("fs-03", 100, 400, new Dimension(10, 10), Color.GREEN, 30.0);
        env.addFoodSourceAt("fs-04", 245, 445, new Dimension(10, 10), Color.GREEN, 30.0);
        env.addFoodSourceAt("fs-05", 400, 400, new Dimension(10, 10), Color.GREEN, 30.0);
        env.addFoodSourceAt("fs-06", 445, 245, new Dimension(10, 10), Color.GREEN, 30.0);
        env.addFoodSourceAt("fs-07", 400, 100, new Dimension(10, 10), Color.GREEN, 30.0);
        env.addFoodSourceAt("fs-08", 245, 45, new Dimension(10, 10), Color.GREEN, 30.0);

        sim.scheduleEnvironmentElementRenderer("round-env.png", 0, TimeUnit.SECONDS);
        sim.scheduleEnvironmentExploredRenderer("round-exp-10s.png", envColour, new Color(118, 87, 131), 10,
                TimeUnit.SECONDS);
        sim.scheduleEnvironmentExploredRenderer("round-exp-20s.png", envColour, new Color(77, 175, 124), 20,
                TimeUnit.SECONDS);
        sim.scheduleEnvironmentExploredRenderer("round-exp-30s.png", envColour, new Color(235, 201, 94), 30,
                TimeUnit.SECONDS);
        sim.scheduleEnvironmentExploredRenderer("round-exp-40s.png", envColour, new Color(230, 93, 57), 40,
                TimeUnit.SECONDS);
        sim.scheduleEnvironmentExploredRenderer("round-exp-50s.png", envColour, new Color(91, 81, 71), 50,
                TimeUnit.SECONDS);
        sim.scheduleChemicalStimulusRenderer(ForageStimulusType.TYPE, "round-pheromone-50s.png", 50,
                TimeUnit.SECONDS);
        sim.scheduleChemicalStimulusRenderer(ForageStimulusType.TYPE, "round-pheromone-100s.png", 100,
                TimeUnit.SECONDS);
        sim.scheduleChemicalStimulusRenderer(ForageStimulusType.TYPE, "round-pheromone-180s.png", 180,
                TimeUnit.SECONDS);

        sim.composeImage("round-exp-final.png", new String[]{
                "round-exp-50s.png",
                "round-exp-40s.png",
                "round-exp-30s.png",
                "round-exp-20s.png",
                "round-exp-10s.png",
                "round-env.png"});

        sim.run(180, TimeUnit.SECONDS);
    }
}
