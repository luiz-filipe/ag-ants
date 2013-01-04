package org.ag.ants.env;

import org.ag.common.annotation.FrameworkExclusive;
import org.ag.common.env.BasicCommunicationStimulus;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

/**
 * Represent chemical interactions between agents and the environment.
 * Interaction might not be the right word for it, but objects of this class are
 * interactions that some agents have with the environment and are possibly used
 * by other agents to acquire information.
 * 
 * @author Luiz Abrahao <luiz@luizabrahao.com>
 * 
 */
@ThreadSafe
public class ChemicalCommStimulus extends BasicCommunicationStimulus {
	@GuardedBy("this")
	private double intensity = 0;

	public ChemicalCommStimulus(
			final ChemicalCommStimulusType chemicalCommStimulusType) {
		super(chemicalCommStimulusType);
	}

	public synchronized double getIntensity() {
		return this.intensity;
	}

	/**
	 * This should be used by the framework only, if user code calls this method
	 * directly something is wrong. It is used during environment setup.
	 * 
	 * @param intensity
	 *            Intensity of the chemical stimulus
	 */
	@FrameworkExclusive
	public synchronized void setIntensity(final double intensity) {
		this.intensity = intensity;
	}

	/**
	 * Just an utility override to cast the super class' type field to
	 * ChemicalCommStimulusType as it is necessary to do this quite often.
	 */
	@Override
	public ChemicalCommStimulusType getType() {
		return (ChemicalCommStimulusType) super.getType();
	}

	/**
	 * Increases the current intensity of the stimulus by the amount passed as
	 * parameter
	 * 
	 * @param amount
	 *            Double Amount to incremenet the intensity by.
	 */
	public synchronized void increaseIntensity(final double amount) {
		if ((this.intensity + amount) > 1) {
			return;
		}

		this.intensity = this.intensity + amount;
	}

	/**
	 * This method effectively controls how the intensity of the stimulus decay.
	 * It is used by agents that execute chemical stimulus evaporation.
	 */
	public synchronized void decayIntensity() {
		this.intensity = this.intensity * (1 - this.getType().getDecayFactor());
	}
}
