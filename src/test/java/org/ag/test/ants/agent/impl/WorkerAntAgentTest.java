package org.ag.test.ants.agent.impl;

import java.awt.Color;
import java.awt.Dimension;
import java.util.concurrent.TimeUnit;

import org.ag.ants.env.impl.ForageStimulusType;
import org.ag.ants.simulation.AntEnvironment;
import org.ag.ants.simulation.AntSimulation;
import org.junit.Test;

public class WorkerAntAgentTest {

	@Test
	public void depositChemicalCommStimulusTest() throws Exception {
		final AntEnvironment env = new AntEnvironment(20, 20);
		final AntSimulation sim = new AntSimulation("target", env, 50);
		
		env.createNestAt("n", 10, 15, new Dimension(3, 3), 30, Color.blue);
		env.addFoodSourceAt("f", 10, 5, new Dimension(3, 3), Color.green, 20);
		sim.scheduleEnvironmentExploredRenderer("worker-explored.png", 10, TimeUnit.SECONDS);
		sim.scheduleEnvironmentElementRenderer("worker-env.png", 0, TimeUnit.SECONDS);
		sim.scheduleChemicalStimulusRenderer(ForageStimulusType.TYPE, "worker-forage.png", 10, TimeUnit.SECONDS);
		
		sim.composeImage("worker-composed.png", new String[] {"worker-env.png", "worker-forage.png"});
		
		sim.run(10, TimeUnit.SECONDS);
		
		String t = "";
		
	}
}
