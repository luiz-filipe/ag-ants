package org.ag.ants.renderer;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import org.ag.ants.env.ChemicalCommStimulus;
import org.ag.ants.env.ChemicalCommStimulusType;
import org.ag.ants.env.PheromoneNode;
import org.ag.common.renderer.AbstractRenderer;
import org.ag.common.renderer.RenderedImage;
import org.ag.common.simulation.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChemicalStimulusRenderer extends AbstractRenderer {
    private static final Logger logger = LoggerFactory.getLogger(ChemicalStimulusRenderer.class);
    private final Color pheromoneColor;
    private final ChemicalCommStimulusType chemicalCommStimulusType;

    /**
     * Constructs a chemical stimulus renderer with a chemical stimulus type, a name and the environment it will render.
     * The default colour assigned to the stimulus type will be: (255, 0, 0, 255).
     *
     * @param chemicalCommStimulusType chemical stimulus type to be rendered.
     * @param name name given to the renderer.
     * @param environment environment the renderer will render.
     */
    public ChemicalStimulusRenderer(ChemicalCommStimulusType chemicalCommStimulusType, String name,
                                    Environment environment) {
        super(name, environment);
        this.pheromoneColor = new Color(255, 0, 0, 255);
        this.chemicalCommStimulusType = chemicalCommStimulusType;
    }

    /**
     * Constructs a chemical stimulus renderer with a chemical stimulus type, a name and the environment it will render,
     * and the colour that will be used to render the stimulus.
     *
     * @param chemicalCommStimulusType chemical stimulus type to be rendered.
     * @param name name given to the renderer.
     * @param environment environment the renderer will render.
     * @param pheromoneColor colour will be used for the stimulus type.
     */
    public ChemicalStimulusRenderer(ChemicalCommStimulusType chemicalCommStimulusType, String name,
                                    Environment environment, Color pheromoneColor) {
        super(name, environment);
        this.pheromoneColor = pheromoneColor;
        this.chemicalCommStimulusType = chemicalCommStimulusType;
    }

    /**
     * The renderer visits every node of the environment and calculate how much transparent the stimulus rendered will
     * be depending on its intensity. There are 255 different levels of transparency.
     *
     * @return rendered image.
     * @throws Exception if renderer cannot finish.
     */
    @Override
    public RenderedImage call() throws Exception {
        final BufferedImage bufferedImage = new BufferedImage(environment.getWidth(), environment.getHeight(),
                BufferedImage.TYPE_INT_ARGB);

        final Graphics2D g2d = bufferedImage.createGraphics();

        for (int l = 0; l < environment.getHeight(); l++) {
            if (Thread.currentThread().isInterrupted()) {
                logger.info("Pheromone renderer was interrupted and will not complete...");

                g2d.dispose();
                break;
            }

            for (int c = 0; c < environment.getWidth(); c++) {
                final PheromoneNode node = (PheromoneNode) environment.getNodeAt(l, c);
                final ChemicalCommStimulus stimulus = node.getCommunicationStimulus(this.chemicalCommStimulusType);
                double intensity = 0;

                if (stimulus != null) {
                    intensity = stimulus.getIntensity();
                }

                if (intensity != 0) {
                    logger.trace("r=" + pheromoneColor.getRed() + ", g=" + pheromoneColor.getGreen() + ", b=" +
                            pheromoneColor.getBlue() + ", a=" + Math.round(intensity * 255));
                    g2d.setColor(new Color(pheromoneColor.getRed(), pheromoneColor.getGreen(), pheromoneColor.getBlue(),
                            (int) Math.round(intensity * 255)));

                } else {
                    g2d.setColor(new Color(255, 255, 255, 0));
                }

                g2d.drawLine(c, l, c, l);
            }
        }

        g2d.dispose();
        logger.trace("Finished rendering pheromone image.");
        return new RenderedImage(this.getOutputName(), bufferedImage);
    }

}
