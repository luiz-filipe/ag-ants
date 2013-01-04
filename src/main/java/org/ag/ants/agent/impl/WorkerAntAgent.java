package org.ag.ants.agent.impl;

import org.ag.common.env.Node;

public class WorkerAntAgent extends AntAgent {

	public WorkerAntAgent(final String id, final Node currentNode,
			final boolean recordNodeHistory) {
		
		super(id, WorkerAntType.TYPE, currentNode, recordNodeHistory);
	}

	@Override
	public Void call() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
