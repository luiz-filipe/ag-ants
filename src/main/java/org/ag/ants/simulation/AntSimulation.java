package org.ag.ants.simulation;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.ag.ants.env.AntNest;
import org.ag.ants.env.ChemicalCommStimulusType;
import org.ag.ants.renderer.ChemicalStimulusRenderer;
import org.ag.common.renderer.Renderer;
import org.ag.common.simulation.Environment;
import org.ag.common.simulation.Simulation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AntSimulation extends Simulation {
	private static final Logger logger = LoggerFactory
			.getLogger(AntSimulation.class);

	public AntSimulation(String basePath, Environment environment, int poolSize) {
		super(basePath, environment, poolSize);
	}

	private List<AntNest> getAntNests() {
		return ((AntEnvironment) this.getEnvironment()).getNests();
	}

	public void scheduleChemicalStimulusRenderer(
			final ChemicalCommStimulusType chemicalCommStimulusType,
			final String filename, final long delay, final TimeUnit unit) {

		Renderer r = new ChemicalStimulusRenderer(chemicalCommStimulusType, filename, this.getEnvironment());

		this.scheduleRenderer(r, filename, delay, unit);
	}

	public void scheduleChemicalStimulusRenderer(
			final ChemicalCommStimulusType chemicalCommStimulusType,
			final String filename, final Color colour, final long delay,
			final TimeUnit unit) {

		Renderer r = new ChemicalStimulusRenderer(chemicalCommStimulusType,
				filename, this.getEnvironment(), colour);

		this.scheduleRenderer(r, filename, delay, unit);
	}

	@Override
	public void run(long time, TimeUnit unit) {
		final List<Future<Void>> antFutures = new ArrayList<Future<Void>>();
		logger.info("Starting ant simulation...");

		for (AntNest nest : this.getAntNests()) {
			antFutures.addAll(nest.open());
		}

		executor.schedule(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				for (AntNest nest : getAntNests()) {
					nest.close();
				}
				executor.shutdownNow();

				try {
					if (!executor.awaitTermination(30, TimeUnit.SECONDS)) {
						logger.error("Could not stop simulation!");
					}

				} catch (InterruptedException e) {
					executor.shutdownNow();
					Thread.currentThread().interrupt();
				}

				return null;
			}
		}, time, unit);

		this.getRendererManager().run();

		for (Future<Void> antFuture : antFutures) {
			try {
				antFuture.get();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
