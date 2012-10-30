package org.ag.ants.agent;

import org.ag.common.agent.AgentType;

import net.jcip.annotations.ThreadSafe;

/**
 * Define the type of agent that identify ant nests.
 * 
 * @author Luiz Abrahao <luiz@luizabrahao.com>
 *
 */
@ThreadSafe
public enum AntNestType implements AgentType {
	TYPE;
	
	private static final String name = "ant:agent:nest";
	
	@Override public String getName() { return name; }
}
