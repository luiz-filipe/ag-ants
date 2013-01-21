package org.ag.ants.agent.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.jcip.annotations.ThreadSafe;

import org.ag.ants.agent.AntType;
import org.ag.ants.env.impl.ForageStimulusType;
import org.ag.ants.env.impl.WarningStimulusType;
import org.ag.ants.task.FindHomeNestTask;
import org.ag.ants.task.ForageTask;
import org.ag.common.task.Task;

/**
 * This type represent ants from the worker caste in the colony. They are the
 * main responsible for foraging. Ants from this caste execute the following
 * tasks: 
 *    - ForageTask
 *    - FindHomeNestTask
 * 
 * @author Luiz Abrahao <luiz@luizabrahao.com>
 * 
 */
@ThreadSafe
public enum WorkerAntType implements AntType {
	TYPE;

	private static final String name = "ants:agent:type:worker";
	private final List<Task> tasks;
	private final Map<String, Double> stimulusIncrementList;
	private static final int memorySize = 50;
	private final double amountOfFoodCapableToCollect = 0.1;
	private static final long milisecondsToWait = 5;

	WorkerAntType() {
		tasks = new ArrayList<Task>();
		tasks.add(new ForageTask());
		tasks.add(new FindHomeNestTask());
		
		stimulusIncrementList = new HashMap<String, Double>();
		stimulusIncrementList.put(ForageStimulusType.TYPE.getName(), 0.01);
		stimulusIncrementList.put(WarningStimulusType.TYPE.getName(), 0.05);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public List<Task> getTasks() {
		return tasks;
	}

	@Override
	public int getMemorySize() {
		return memorySize;
	}

	@Override
	public double getAmountOfFoodCapableToCollect() {
		return amountOfFoodCapableToCollect;
	}
	
	@Override
	public long getMilisecondsToWait() {
		return milisecondsToWait;
	}

	@Override
	public double getStimulusIncrement(String chemicalCommStimulusTypeName) {
		for (Map.Entry<String, Double> entry : stimulusIncrementList.entrySet()) {
			if (entry.getKey().equals(chemicalCommStimulusTypeName)) {
				return entry.getValue();
			}
		}

		throw new RuntimeException(
				"WorkerType does not have an increment declared for '"
						+ chemicalCommStimulusTypeName + "'");
	}
}