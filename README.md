# AG Ants

A specialisation of [AG Common](https://github.com/luiz-filipe/ag-common) that
describes a colony of ants and its main components (Ants and Nests) and the
environment (Chemical communication and food sources) necessary to simulate the
behaviour of an ant colony.

With this implementation is possible to describe different species of ants varying their
behavior based on their tasks, within each specie the framework provides ways to specifies
different communication stimulus for different castes as well as different ways to react
to these stimuli.

This is work is part of my final MSc project that can be [downloaded here](https://github.com/luiz-filipe/msc-project/raw/master/report/MSc-Report.pdf),
in the MSc report I studied how varying some parameters of the pheromone deposited by
working ants affected how much food the colony as a whole was able to collect.

## 30 second introduction

Actually this is an 180 second introduction because that is how long we are going to run
the following simulation:

1. Create an environment that has the dimension 500 x 500 and a simulation instance to
   handle the simulation itself.
2. Create an Ant nest at the center of the environment, this nest will create an maximum
   of 100 ants.
3. Add 8 food sources around the environment (they are rendered in green in the resulting
   images).
4. Schedule some renders to create visual snapshots of the environment to help us visualise
   what nodes have been visited by the ants and where they have deposited pheromone.

```Java
public class RoundFoodSources {
    public static void main(String[] args) {
        // 1
        final AntEnvironment env = new AntEnvironment(new Dimension(500, 500));
        final AntSimulation sim = new AntSimulation("target/", env);
        final Color envColour = new Color(255, 255, 255, 0);

        // 2
        env.createNestAt("nest", env.getHeight() / 2 - 5, env.getWidth() / 2 - 5,
                new Dimension(10, 10), 100, Color.BLUE);

        // 3
        env.addFoodSourceAt("fs-01", 100, 100, new Dimension(10, 10), Color.GREEN, 30.0);
        env.addFoodSourceAt("fs-02", 45, 245, new Dimension(10, 10), Color.GREEN, 30.0);
        env.addFoodSourceAt("fs-03", 100, 400, new Dimension(10, 10), Color.GREEN, 30.0);
        env.addFoodSourceAt("fs-04", 245, 445, new Dimension(10, 10), Color.GREEN, 30.0);
        env.addFoodSourceAt("fs-05", 400, 400, new Dimension(10, 10), Color.GREEN, 30.0);
        env.addFoodSourceAt("fs-06", 445, 245, new Dimension(10, 10), Color.GREEN, 30.0);
        env.addFoodSourceAt("fs-07", 400, 100, new Dimension(10, 10), Color.GREEN, 30.0);
        env.addFoodSourceAt("fs-08", 245, 45, new Dimension(10, 10), Color.GREEN, 30.0);

        // 4
        sim.scheduleEnvironmentElementRenderer("round-env.png", 0, TimeUnit.SECONDS);
        sim.scheduleEnvironmentExploredRenderer("round-exp-10s.png", envColour,
                new Color(118, 87, 131), 10, TimeUnit.SECONDS);
        sim.scheduleEnvironmentExploredRenderer("round-exp-20s.png", envColour,
                new Color(77, 175, 124), 20, TimeUnit.SECONDS);
        sim.scheduleEnvironmentExploredRenderer("round-exp-30s.png", envColour,
                new Color(235, 201, 94), 30, TimeUnit.SECONDS);
        sim.scheduleEnvironmentExploredRenderer("round-exp-40s.png", envColour,
                new Color(230, 93, 57), 40, TimeUnit.SECONDS);
        sim.scheduleEnvironmentExploredRenderer("round-exp-50s.png", envColour,
                new Color(91, 81, 71), 50, TimeUnit.SECONDS);

        sim.scheduleChemicalStimulusRenderer(ForageStimulusType.TYPE,
                "round-pheromone-50s.png", 50, TimeUnit.SECONDS);
        sim.scheduleChemicalStimulusRenderer(ForageStimulusType.TYPE,
                "round-pheromone-100s.png", 100, TimeUnit.SECONDS);
        sim.scheduleChemicalStimulusRenderer(ForageStimulusType.TYPE,
                "round-pheromone-180s.png", 180, TimeUnit.SECONDS);

        sim.composeImage("round-exp-final.png", new String[]{
                "round-exp-50s.png",
                "round-exp-40s.png",
                "round-exp-30s.png",
                "round-exp-20s.png",
                "round-exp-10s.png",
                "round-env.png"});

        sim.run(180, TimeUnit.SECONDS);
    }
}
```

### Results

#### Pheromone trail left by ants

<img src="http://luiz-filipe.github.io/ag-ants/img/round-experiment-ph-final.png" alt="Pheromone trail" width="350" height="350">

The agents used in this simulations use [path integration](http://en.wikipedia.org/wiki/Path_integration)
as the algorithm to calculate how to get back to their nest. That is why the pheromone
paths are always a straight line from the ant's current position to their nest. The
illustration above shows how the use of pheromone as a way to communicate is reinforced
by the feedback (deposit of more pheromone over time) given by the ants as a response on
the environment around them.

This is a clear example of emergence, the agents are not aware of the other agents, but
their contribution together with the other agents' created a trail of pheromone that is
used by the entire colony as the optimal path to the food source.

#### Nodes visited

<img src="http://luiz-filipe.github.io/ag-ants/img/round-experiment-final.png" alt="Visited nodes" width="350" height="350">
## More Results

The following is a very brief summary of the experiments and observations results described
in the MSc project. I thought it would be useful to have it here because they offer a different
insight into the framework details and show how it can actually be used.

For the full story on this experiments please refer to the [MSc project report](https://github.com/luiz-filipe/msc-project/raw/master/report/MSc-Report.pdf)

### Pheromone Concentration Sensitivity

This experiment investigates how different initial pheromone concentrations in the environment
and the amount of pheromone each agent is capable of depositing in each interaction affect the
agents navigation through the space.

Because the way the node selection was implemented, if we started with an environment with no
pheromone at all and then an ant deposited any some in any node, the probability of this node
being selected for the agent's next move was 100%, so it would effectively get stuck in a very
small area (this is explained in more detail from page 50 in the report).

So what if we initialised the environment with just enough pheromone so that the agents could
freely move and execute their tasks? But the question now is how much this initial concentration
should be an how big should be the agents increment to the pheromone concentration so that the
colony does not converges to a small area and at the same time it will not get too dispersed, so
that its agents would not be able to communicate indirectly to each other and the colony
behaviour would not emerge.

In this experiment two samples of the environment were taken, one close to the nest and one
far from it.

#### Close
<img src="http://luiz-filipe.github.io/ag-ants/img/initial-variations-close.png" alt="Close" width="350" height="210">

#### Far
<img src="http://luiz-filipe.github.io/ag-ants/img/initial-variations-far.png" alt="Far" width="350" height="210">

The trails left by the colonies can be compared in relation on how strong they are, how the agents
are able to ’scape’ them to explore the environment and how it shaped when agents get further
from the nest.

It is possible to observe from the figures that two very contrasting behaviours emerge, firstly
because the environment has so little pheromone and the update is so small, they weight assigned
to each of the neighbour nodes count considerably more than the pheromone deposited by the agents,
so the agents end up very dispersed, thus no chemical trail is formed at all.

When the amount of pheromone deposited by the agents is increased the behaviour of the colony could
not be more different than what was seen previously. The agents switch from exploring a large area
to be ’trapped’ into the pheromone trail. This impedes the agents of exploring the space, what is
not desirable for any colony.

Further investigation revealed that the probability of selection increases in a logarithmic fashion.
The following figure shows how the increase in probability progress when the amount of pheromone
deposited by the agents increases by multiples of the initial concentration in the environment.

<img src="http://luiz-filipe.github.io/ag-ants/img/probability-increase.png" alt="probability increase" width="350" height="220">

The complete trail pheromone trails left by simulation using 0.001,0.04 (a), 0.01,0.01 (b),
0.02,0.01 (c), 0.02,0.02 (d) and 0.4,04 (e) for initial pheromone concentration and update step
respectively:

<img src="http://luiz-filipe.github.io/ag-ants/img/full-trail-final.png" alt="probability increase" width="350" height="359">

### Forage Radius Investigation

In this experiment the radius of action of the forage pheromone is varied in order to check the
effects on the amount of food the colony is capable of forage.

At first, one would expect that the increase on the radius of the forage pheromone would have a
positive impact on the colony capacity of collecting food as trails that lead to food sources are
reinforced by agents carrying food back to the nest and with a wider spread of the pheromone more
workers would fall in these trails.

However the data from the simulations show a different picture. The colony collect less and less
food as the stimulus’ radius increase.

| Radius | Mean of food collected | Standard deviation |
| :----: | :--------------------: | :----------------: |
| 0 | 6.0 | 1.21 |
| 1 | 4.9 | 0.72 |
| 2 | 3.3 | 0.53 |

The experiment was run 100 times for every radius. The following figure shows the samples
distribution when variating the pheromone radius - 0 (a), 1 (b) and 2 (c) - and the samples’
probability density distribution (d)

<img src="http://luiz-filipe.github.io/ag-ants/img/radius-pdf.png" alt="PDFs" width="350" height="208">

The figure above helps us to visualise what the previous table had already shown - the samples
variance decreases when the radius increases.

The reason for this phenomena is that the bigger the pheromone radius, fewer agents are likely
to ’escape’ from the trail, thus they are not able to explore different areas of the environment.
This means that the agents will create virtually the same pheromone trail in each case to forage,
so that their outcomes are likely to be very close.

With a more diversified exploratory reach, agents in the simulations using radius 0 are likely to
have different degrees of success in finding food and taking it back to the nest in each simulation
run, justifying the greater variance in the colony outcome. In other words, random events play a
large role in determining the outcome.

## Overview

Like [AG Common](https://github.com/luiz-filipe/ag-common), this ant
specialisation can be divided in three main areas.

### Environment

#### Pheromone Node

A specialisation of [`BasicNode`](https://github.com/luiz-filipe/ag-common/blob/master/src/main/java/org/ag/common/env/BasicNode.java)
used to create environments for ant simulations. Pheromone nodes have a list of chemical
stimulus that are present in the node.

See: [`PheromoneNode`](src/main/java/org/ag/ants/env/PheromoneNode.java),
[`AntEnvironmentFactory`](src/main/java/org/ag/ants/env/impl/AntEnvironmentFactory.java),
[`ChemicalCommStimulus`](src/main/java/org/ag/ants/env/ChemicalCommStimulus.java),
[`ChemicalCommStimulusType`](src/main/java/org/ag/ants/env/ChemicalCommStimulusType.java)

#### Nest Node

A [`NestNode`](src/main/java/org/ag/ants/env/NestNode.java) is part of a
ant nest. It is extends the BasicNode adding a propriety that represents the amount
of food that particular node is capable of storing. NestNodes are assembled to
form ant nests.

See: [`NestNode`](src/main/java/org/ag/ants/env/NestNode.java),
[`AntNest`](src/main/java/org/ag/ants/env/AntNest.java)

#### Food Source Node

A [`FoodSourceNode`](src/main/java/org/ag/ants/env/FoodSourceNode.java) object
represents a node that contain some food. The amount of food is set at creation
time and ants that enter that node are able to collect food from it.

See: [`FoodSourceNode`](src/main/java/org/ag/ants/env/FoodSourceNode.java),
[`FoodSource`](src/main/java/org/ag/ants/env/FoodSource.java)

#### Chemical Communication Stimulus Type

An abstraction of communication stimulus that represent chemical substances.

See: [`ChemicalCommStimulusType`](src/main/java/org/ag/ants/env/ChemicalCommStimulusType.java)

#### Chemical Communication Stimulus

Represent chemical interactions between agents and the environment. Interaction might
not be the right word for it, but objects of this class are interactions that some
agents have with the environment and are possibly used by other agents to acquire
information.

See: [`ChemicalCommStimulus`](src/main/java/org/ag/ants/env/ChemicalCommStimulus.java),
[`ForageStimulusType`](src/main/java/org/ag/ants/env/impl/ForageStimulusType.java),
[`WarningStimulusType`](src/main/java/org/ag/ants/env/impl/WarningStimulusType.java)

### Agent

#### Ant

The [`Ant`](src/main/java/org/ag/ants/agent/Ant.java) interface defines the basic
API for all ant agents. Moving direction, food collection and communication stimulus
deposit are all handled by all ants and defined in this interface.

See: [`Ant`](src/main/java/org/ag/ants/agent/Ant.java),
[`AntAgent`](src/main/java/org/ag/ants/agent/impl/AntAgent.java),
[`WorkerAntAgent`](src/main/java/org/ag/ants/agent/impl/WorkerAntAgent.java),
[`AntAgentFactory`](src/main/java/org/ag/ants/agent/impl/AntAgentFactory.java)

#### Ant Type

Ants are implemented having limited memory size and limited capability of collecting food.
Different types of ants have different memory sizes and can collect different amounts of
food. Also the different types of ant interact differently with the environment so each
type of ants define a list of chemical stimulus and how much of each of them the agent is
able to lay in every interaction.

See: [`AntType`](src/main/java/org/ag/ants/agent/AntType.java),
[`WorkerAntType`](src/main/java/org/ag/ants/agent/impl/WorkerAntType.java)

### Task

#### Forage

The [`ForageTask`](src/main/java/org/ag/ants/task/ForageTask.java) implements a node selection
model to direct the ant towards a food source using pheromone trails. This model takes in
consideration the intensity of forage pheromone present in the 8 neighbour nodes around the agent.

See: [`ForageTask`](src/main/java/org/ag/ants/task/ForageTask.java)

#### Find Home Nest

The [`FindHomeNestTask`](src/main/java/org/ag/ants/task/FindHomeNestTask.java) implements the
path-integration algorithm to take the ant back to the nest were it was created.

See: [`FindHomeNestTask`](src/main/java/org/ag/ants/task/FindHomeNestTask.java)
