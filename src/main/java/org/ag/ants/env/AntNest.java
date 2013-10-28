package org.ag.ants.env;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import net.jcip.annotations.ThreadSafe;

import org.ag.ants.agent.impl.AntAgent;
import org.ag.ants.agent.impl.WorkerAntAgent;
import org.ag.common.env.EnvironmentElement;
import org.ag.common.env.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An AntNest is composed by an array of nestNode objects that represent an ant
 * nest. It has some utility methods for creating ants, it's also important to
 * note that a AntNest has a life-cycle because it executes its own tasks 
 * (agents) using its dedicated executor service, so the simulation handler must
 * call these methods when starting and finishing simulations.
 * 
 * The <em>defaultDelayOfTaskSubmission</em> determines how slow the nest will
 * submit its tasks (agents) to the executor. Instead of submitting all the
 * agents at the same time it submits one, waits for the period specified by
 * the <em>defaultDelayOfTaskSubmission</em> to submit the next one.
 * 
 * @author Luiz Abrahao <luiz@luizabrahao.com>
 * 
 */
//TODO add functionality to add/produce different types of ants.
@ThreadSafe
public class AntNest extends EnvironmentElement {
	private static final Logger logger = LoggerFactory.getLogger(AntNest.class);
	private static long defaultDelayOfTaskSubmission = 100;

	private final int maximumNumberOfAnts;
	private final List<AntAgent> ants = new ArrayList<AntAgent>();

	private final ScheduledExecutorService executor;

	public AntNest(final String id, final Dimension dimension,
			final int maximumNumberOfAnts, final Color colour) {

		super(id, dimension, colour, EnvironmentFactory.createNestGrid(id,
				dimension));

		this.maximumNumberOfAnts = maximumNumberOfAnts;
		this.executor = Executors.newScheduledThreadPool(maximumNumberOfAnts);
	}

	public AntNest(final String id, final Dimension dimension,
			final int maximumNumberOfAnts) {
		super(id, dimension, new Color(140, 98, 57), EnvironmentFactory
				.createNestGrid(id, dimension));

		this.maximumNumberOfAnts = maximumNumberOfAnts;
		this.executor = Executors.newScheduledThreadPool(maximumNumberOfAnts);
	}

	public double getTotalFoodHeld() {
		double total = 0;

		for (int l = 0; l < this.getDimension().height; l++) {
			for (int c = 0; c < this.getDimension().width; c++) {
				total = total
						+ ((NestNode) this.getNode(l, c)).getAmountOfFoodHeld();
			}
		}

		return total;
	}

	public int getMaximuNumberOfAnts() {
		return this.maximumNumberOfAnts;
	}

	public List<Future<Void>> open() {
		if (ants.size() == 0) {
			this.addBunchOfWorkers("worker:" + this.getId(), maximumNumberOfAnts);
		}
		
		final List<Future<Void>> futures = new ArrayList<Future<Void>>();

		for(AntAgent ant : this.ants) {
			if (Thread.currentThread().isInterrupted()) {
				return futures;
			}
			
			futures.add(executor.submit(ant));
			
			try {
				Thread.sleep(defaultDelayOfTaskSubmission);
			
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
		
		return futures;
	}

	public void close() {
		executor.shutdownNow();
		logger.info("Nest [{}]: starting orderly shutdown process...",
				this.getId());

		try {
			if (!executor.awaitTermination(30, TimeUnit.SECONDS)) {
				logger.error("Could not kill nest '{}', forcing stop.",
						this.getId());
			}

		} catch (InterruptedException e) {
			executor.shutdownNow();
			Thread.currentThread().interrupt();
		}
	}

	public void addBunchOfWorkers(final String prefix, final int numberOfAnts) {
		final Random random = new Random();

		// Distribute the new agents randomically throughout the nest's nodes.
		for (int i = 0; i < numberOfAnts; i++) {
			final Node randomNestNode = this.getNode(
					random.nextInt(this.getDimension().height),
					random.nextInt(this.getDimension().width));

			this.ants.add(new WorkerAntAgent(prefix + "-" + i, randomNestNode,
					false));
		}

	}
}
