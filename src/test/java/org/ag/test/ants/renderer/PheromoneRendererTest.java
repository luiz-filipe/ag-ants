package org.ag.test.ants.renderer;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.ag.ants.env.ChemicalCommStimulus;
import org.ag.ants.env.PheromoneNode;
import org.ag.ants.env.impl.ForageStimulusType;
import org.ag.ants.renderer.ChemicalStimulusRenderer;
import org.ag.ants.simulation.AntEnvironment;
import org.ag.common.renderer.RenderedImage;
import org.junit.Test;

public class PheromoneRendererTest {
	@Test
	public void rendererTest() throws InterruptedException, ExecutionException {
		final AntEnvironment environment = new AntEnvironment(100, 100);
		final ExecutorService executor = Executors.newFixedThreadPool(1);
		final ChemicalStimulusRenderer renderer = new ChemicalStimulusRenderer(ForageStimulusType.TYPE, "test-renderer-pheromone.png", environment);
		double accIntensity = 0;
		
		for (int i = 10; i < 90; i++) {
			final PheromoneNode node = (PheromoneNode) environment.getNodeAt(10, i);
			node.addCommunicationStimulus(new ChemicalCommStimulus(ForageStimulusType.TYPE));
			node.incrementStimulusIntensity(ForageStimulusType.TYPE, accIntensity);
			accIntensity = accIntensity + 0.005;
		}
		
		Future<RenderedImage> future = executor.submit(renderer);
		ImageWriter.writeImage(future.get().getImage(), "target/test-renderer-pheromone.png");
	}
}
