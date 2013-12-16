package org.ag.ants.env.impl;

import org.ag.ants.env.PheromoneNode;
import org.ag.common.env.Direction;

import net.jcip.annotations.ThreadSafe;

/**
 * Utility class for creation of environments to be used in ant simulations.
 *
 * @author Filipe Abrahao <me@luizfilipe.com>
 *
 */
@ThreadSafe
public class AntEnvironmentFactory {
    private AntEnvironmentFactory() {}

    /**
     * Initialises an environment based on PheromoneNode objects. This environment has rectangular shape and each node
     * are assigned an identifier following the pattern: "n+lineNumber,columnNumber", e.g. "n3,2" corresponds to the
     * node at the third line and second column.
     *
     * @param nLines number of lines the grid will contain
     * @param nColumns number of column the grid will contain.
     * @return A two dimensional array of interconnected PheromoneNode objects.
     */
    public static PheromoneNode[][] createPheromoneNodeGrid(final int nLines, final int nColumns) {
        final PheromoneNode[][] nodes = new PheromoneNode[nLines][nColumns];

        for (int l = 0; l < nLines; l++) {
            for (int c = 0; c < nColumns; c++) {
                nodes[l][c] = new PheromoneNode("n" + l + "," + c);

                if (c != 0) {
                    nodes[l][c].setNeighbours(Direction.WEST, nodes[l][c - 1]);
                }

                if (l != 0) {
                    nodes[l][c].setNeighbours(Direction.NORTH, nodes[l - 1][c]);
                }
            }
        }

        return nodes;
    }
}
