package org.ag.ants.agent.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.jcip.annotations.ThreadSafe;

import org.ag.ants.agent.AntType;
import org.ag.ants.env.ChemicalCommStimulus;
import org.ag.ants.env.impl.FoodSourceAgent;
import org.ag.ants.env.impl.ForageStimulusType;
import org.ag.ants.env.impl.WarningStimulusType;
import org.ag.ants.task.impl.FindAndHideInNest;
import org.ag.ants.task.impl.FindHomeTask;
import org.ag.ants.task.impl.ForageTask;
import org.ag.common.agent.Agent;
import org.ag.common.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This type represent ants from the worker caste in the colony. They are the
 * main responsible for foraging. Ants from this caste execute the following
 * tasks:
 * - ForageTask
 * - FindHometask
 * - FindandHidInHest
 *
 * Workers have the <em>warningThreshold</em> field that defines what is the
 * warning pheromone threshold to trigger their reaction to danger.
 * 
 * @see WarningStimulusType
 * 
 * @author Luiz Abrahao <luiz@luizabrahao.com>
 *
 */
@ThreadSafe
public enum WorkerAntType implements AntType {
	TYPE;
	
	private static final Logger logger = LoggerFactory.getLogger(WorkerAntType.class);
	
	private static final String name = "type:ant:worker";
	private final List<Task> tasks;
	private final Map<String, Double> stimulusIncrementList;
	private static final int memorySize = 50;
	private final double amountOfFoodCapableToCollect = 0.1;
	private static final long milisecondsToWait = 5;
	private static final double warningThreshold = 0.5;

	WorkerAntType() {
		tasks = new ArrayList<Task>();
		tasks.add(new ForageTask());
		tasks.add(new FindHomeTask());
		tasks.add(new FindAndHideInNest());
		
		// TODO need to add Task to the end of FindAndHideInNest.
		
		stimulusIncrementList = new HashMap<String, Double>();
		stimulusIncrementList.put(ForageStimulusType.TYPE.getName(), 0.01);
		stimulusIncrementList.put(WarningStimulusType.TYPE.getName(), 0.05);
	}
	
	@Override public String getName() { return name; }
	@Override public List<Task> getTasks() { return tasks; }
	@Override public int getMemorySize() { return memorySize; }
	@Override public double getAmountOfFoodCapableToCollect() { return amountOfFoodCapableToCollect; }
	
	@Override
	public double getStimulusIncrement(String chemicalCommStimulusTypeName) {
		for (Map.Entry<String, Double> entry : stimulusIncrementList.entrySet()) {
			if (entry.getKey().equals(chemicalCommStimulusTypeName)) {
				return entry.getValue();
			}
		}
		
		throw new RuntimeException("WorkerType does not have an increment declared for '" + chemicalCommStimulusTypeName + "'");
	}

	@Override
	public void execute(Agent agent) {
		AntAgent ant = (AntAgent) agent;
		
		if (ant.getCurrentTask() != null) {
			// if it is trying to hide, just continue...
			if (ant.getCurrentTask().getName().equals(FindAndHideInNest.NAME)) {
				ant.getTaskByName(FindAndHideInNest.NAME).execute(agent);
				
				this.waitSomeTime();
				return;
			}
		}
		
		ChemicalCommStimulus warningStimulus = (ChemicalCommStimulus) ant.getCurrentNode().getCommunicationStimulus(WarningStimulusType.TYPE);
		
		if ((warningStimulus != null) && (warningStimulus.getIntensity() > warningThreshold)) {
			// if the ant is caring food it is likely it already is moving
			// towards the nest, so there is no need to invert its direction.
			if (!ant.isCarryingFood()) {
				ant.invertDirection();
			}
			
			ant.setCurrentTask(ant.getTaskByName(FindAndHideInNest.NAME));
			logger.debug("{} has switched to {}", agent.getId(), FindAndHideInNest.NAME);
			ant.setCurrentTask(FindAndHideInNest.NAME);
			ant.getTaskByName(FindAndHideInNest.NAME).execute(agent);
			
			this.waitSomeTime();
			return;
		}
		
		
		FoodSourceAgent foodSource = ant.findFoodSource();
		
		if ((foodSource != null) && (!ant.isCarryingFood())) {
			ant.collectFood(foodSource, amountOfFoodCapableToCollect);
			logger.debug("{} found a source food and will try to collect food.", agent.getId());
			
			if (ant.isCarryingFood()) {
				ant.invertDirection();
			}
		}
				
		if (!ant.isCarryingFood()) {
			ant.setCurrentTask(ForageTask.NAME);
			ant.getTaskByName(ForageTask.NAME).execute(agent);
		} else {
			ant.setCurrentTask(FindHomeTask.NAME);
			ant.getTaskByName(FindHomeTask.NAME).execute(agent);
		}
		
		this.waitSomeTime();
	}
	
	private void waitSomeTime() {
		try {
			Thread.sleep(milisecondsToWait);
		} catch (InterruptedException e) {
			// don't need to do anything...
		}
	}
}