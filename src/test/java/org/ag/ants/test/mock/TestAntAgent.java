package org.ag.ants.test.mock;

import java.util.List;

import org.ag.ants.agent.AntType;
import org.ag.ants.agent.impl.AntAgent;
import org.ag.ants.env.impl.ForageStimulusType;
import org.ag.common.env.Node;
import org.ag.common.task.Task;

public class TestAntAgent extends AntAgent {
	public TestAntAgent(String id, Node currentNode, boolean recordNodeHistory) {
		super(id, TestAntAgentType.TYPE, currentNode, recordNodeHistory);
	}

	@Override
	public Void call() throws Exception {
		return null;
	}

	public int getNumberOfNodesInMemory() {
		return this.memory.size();
	}

	private enum TestAntAgentType implements AntType {
		TYPE;

		@Override
		public List<Task> getTasks() {
			throw new RuntimeException("Method not implemented");
		}

		@Override
		public String getName() {
			return "ants:agent:type:test:abstract-ant-agent";
		}

		@Override
		public double getStimulusIncrement(String chemicalCommStimulusTypeName) {
			if (chemicalCommStimulusTypeName.equals(ForageStimulusType.TYPE
					.getName())) {
				return 0.1;
			}
			
			throw new RuntimeException(
					"TestAntAgent does not support the chemical stimulus: "
							+ chemicalCommStimulusTypeName);
		}

		@Override
		public double getAmountOfFoodCapableToCollect() {
			throw new RuntimeException("Method not implemented");
		}

		@Override
		public int getMemorySize() {
			return 10;
		}

		@Override
		public long getMilisecondsToWait() {
			return 200;
		}
	}
}
