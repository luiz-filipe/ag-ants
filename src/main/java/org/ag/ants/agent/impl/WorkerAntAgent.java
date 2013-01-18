package org.ag.ants.agent.impl;

import org.ag.ants.task.FindHomeNestTask;
import org.ag.ants.task.ForageTask;
import org.ag.common.env.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WorkerAntAgent extends AntAgent {
	private static final Logger logger = LoggerFactory
			.getLogger(WorkerAntAgent.class);
	public WorkerAntAgent(final String id, final Node currentNode,
			final boolean recordNodeHistory) {
		
		super(id, WorkerAntType.TYPE, currentNode, recordNodeHistory);
	}

	@Override
	public Void call() throws Exception {
		while (!Thread.currentThread().isInterrupted()) {
			if (!this.isCarryingFood()) {
				this.getTaskByName(ForageTask.NAME).execute(this);
			
			} else {
				this.getTaskByName(FindHomeNestTask.NAME).execute(this);
				
				if (this.isInHomeNest()) {
					this.depositFood();
				}
			}
			
			try {
				Thread.sleep(this.getAgentType().getMilisecondsToWait());
			
			} catch (InterruptedException e) {
				// reset the flag to allow thread terminate on its own...
				Thread.currentThread().interrupt();
			}
		}
		
		logger.info("[{}] asked to stop...", this.getId());
		return null;
	}

}
