package org.ag.ants.simulation;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import net.jcip.annotations.NotThreadSafe;
import org.ag.ants.env.AntNest;
import org.ag.ants.env.ChemicalCommStimulusType;
import org.ag.ants.renderer.ChemicalStimulusRenderer;
import org.ag.common.renderer.Renderer;
import org.ag.common.simulation.Environment;
import org.ag.common.simulation.Simulation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Adds to the <i>Simulation</i> implementation methods related to ant simulations. It also executes the simulation in a
 * different way since the simulation itself does not need to submit the agents to its executor but use the ant nests to
 * do that.
 *
 * @author Luiz Filipe Abrahao <me@luizfilipe.com>
 */
@NotThreadSafe
public class AntSimulation extends Simulation {
    private static final Logger logger = LoggerFactory.getLogger(AntSimulation.class);
    public static final int POOL_SIZE = 1;

    /**
     * Constructs an ant simulation with a base path, the root folder where the simulation will run from and
     * an environment. Note that in a simulation involving ants the simulation does not handle the agents itself, this
     * is left with the ant nests, which have their one executor for their agents, so the poolSize is initialised with
     * 1 because it needs only one thread to be run, the one that stops the simulation.
     *
     * @param basePath simulation's base path.
     * @param environment environment the simulation will run on.
     */
    public AntSimulation(String basePath, Environment environment) {
        super(basePath, environment, POOL_SIZE);
    }

    /**
     * Returns the list of nests present in the environment.
     *
     * @return nests present in the environment
     */
    private List<AntNest> getAntNests() {
        return ((AntEnvironment) this.getEnvironment()).getNests();
    }

    /**
     * Schedules an chemical stimulus renderer with the chemical type the renderer should render, the filename the
     * output image will have, the delay the renderer will wait until being executed and the time unite for the delay.
     *
     * @param chemicalCommStimulusType type of chemical being rendered.
     * @param filename output image's name.
     * @param delay time the renderer will wait until being executed.
     * @param unit time unit for the delay.
     */
    public void scheduleChemicalStimulusRenderer(final ChemicalCommStimulusType chemicalCommStimulusType,
            final String filename, final long delay, final TimeUnit unit) {

        Renderer r = new ChemicalStimulusRenderer(chemicalCommStimulusType, filename, this.getEnvironment());
        this.scheduleRenderer(r, filename, delay, unit);
    }

    /**
     * Schedules an chemical stimulus renderer with the chemical type the renderer should render, the filename the
     * output image will have, the delay the renderer will wait until being executed and the time unite for the delay.
     *
     * @param chemicalCommStimulusType type of chemical being rendered.
     * @param filename output image's name.
     * @param colour color the render will use for the given chemical type.
     * @param delay time the renderer will wait until being executed.
     * @param unit time unit for the delay.
     */
    public void scheduleChemicalStimulusRenderer(final ChemicalCommStimulusType chemicalCommStimulusType,
            final String filename, final Color colour, final long delay, final TimeUnit unit) {

        Renderer r = new ChemicalStimulusRenderer(chemicalCommStimulusType, filename, this.getEnvironment(), colour);
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
                logger.info(e.getMessage());

            } catch (ExecutionException e) {
                logger.info(e.getMessage());
            }
        }
    }
}
