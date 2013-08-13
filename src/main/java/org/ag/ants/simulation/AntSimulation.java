package org.ag.ants.simulation;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import org.ag.ants.env.AntNest;
import org.ag.common.simulation.Environment;
import org.ag.common.simulation.Simulation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AntSimulation extends Simulation {
	private static final Logger logger = LoggerFactory.getLogger(AntSimulation.class);
	
	public AntSimulation(String basePath, Environment environment, int poolSize) {
		super(basePath, environment, poolSize);
	}

	private List<AntNest> getAntNests() {
		return ((AntEnvironment) this.getEnvironment()).getNests();
	}
	
	@Override
	public void run(long time, TimeUnit unit) {
		logger.info("Starting ant simulation...");
		
		for (AntNest nest : this.getAntNests()) {
			nest.open();
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
		
		this.submitRenderers();
	}
}
