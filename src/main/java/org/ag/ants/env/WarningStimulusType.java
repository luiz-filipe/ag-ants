package org.ag.ants.env;

import net.jcip.annotations.ThreadSafe;

/**
 * Delcares the chemical communication stimulus used by ants to signal danger.
 * 
 * @author Luiz Abrahao <luiz@luizabrahao.com>
 *
 */

@ThreadSafe
public enum WarningStimulusType implements ChemicalCommStimulusType {
	TYPE;

	private static final String name = "ant:env:stimulus:attack";
	private static final double decay_factor = 0.2;
	private static final int radius = 5;
	
	@Override public String getName() { return name; }
	@Override public double getDecayFactor() { return decay_factor; }
	@Override public int getRadius() { return radius; }
}
