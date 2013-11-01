package org.ag.test.ants.mock;

import org.ag.ants.env.ChemicalCommStimulusType;

public enum MockChemicalCommStimulusType implements ChemicalCommStimulusType {
	TYPE;

	private static final String name = "ant:env:test:mock";
	private static final double decay_factor = 0.1;
	private static final int radius = 0;
	
	@Override public String getName() { return name; }
	@Override public double getDecayFactor() { return decay_factor; }
	@Override public int getRadius() { return radius; }
}
