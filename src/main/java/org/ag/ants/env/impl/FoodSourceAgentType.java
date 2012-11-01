package org.ag.ants.env.impl;

import java.util.List;

import org.ag.common.agent.TaskAgentType;
import org.ag.common.task.Task;

import net.jcip.annotations.ThreadSafe;

/**
 * Declares the agnet type for food sources.
 * 
 * @author Luiz Abrahao <luiz@luizabrahao.com>
 *
 */

@ThreadSafe
public enum FoodSourceAgentType implements TaskAgentType {
	TYPE;
	
	private final String name = "type:ant:food-source";

	@Override
	public String getName() { return name; }

	@Override public List<Task> getTasks() { return null; }
}
