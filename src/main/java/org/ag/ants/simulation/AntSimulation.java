package org.ag.ants.simulation;

import java.util.ArrayList;
import java.util.List;

import org.ag.ants.env.AntNest;
import org.ag.common.simulation.Environment;
import org.ag.common.simulation.Simulation;

public class AntSimulation extends Simulation {
	final List<AntNest> nests = new ArrayList<AntNest>();

	public AntSimulation(String basePath, Environment environment, int poolSize) {
		super(basePath, environment, poolSize);
	}

	public void addNest(final AntNest nest) {
		this.nests.add(nest);
	}

}
