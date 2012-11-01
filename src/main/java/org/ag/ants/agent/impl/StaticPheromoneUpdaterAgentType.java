package org.ag.ants.agent.impl;

import java.util.ArrayList;
import java.util.List;

import org.ag.ants.task.impl.StaticPheromoneUpdateTask;
import org.ag.common.agent.TaskAgentType;
import org.ag.common.task.Task;

import net.jcip.annotations.ThreadSafe;

/**
 * Represents an agent that triggers the pheromone update method of the nodes
 * that compose a grid. As required it implements the singleton pattern.
 * 
 * Workers have the following tasks:
 * - StaticPheromoneUpdateTask
 * 
 * @author Luiz Abrahao <luiz@luizabrahao.com>
 *
 */
@ThreadSafe
public enum StaticPheromoneUpdaterAgentType implements TaskAgentType {
	TYPE;
	
	private static final String name = "type:helper:pheromone-updater";
	private final List<Task> tasks;

	StaticPheromoneUpdaterAgentType() {
		tasks = new ArrayList<Task>();
		tasks.add(new StaticPheromoneUpdateTask());
	}
	
	@Override public String getName() { return name; }
	@Override public List<Task> getTasks() { return tasks; }
}