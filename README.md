# AG Ants

A specialisation of [AG Common](https://github.com/luizfilipeabrahao/ag-common) that 
describes a colony of ants and its main components (Ants and Nests) and the 
environment (Chemical communication and food sources) necessary to simulate the 
behaviour of an ant colony.

This is work is part of my final MSc project that can be [downloaded here](http://luizfilipe.com/ag/msc-luiz-filipe.pdf).

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

![alt tag](http://luizfilipe.com/ag/intro-pheromone.jpg)

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

![alt tag](http://luizfilipe.com/ag/ant-explored.jpg)

## Overview

Like [AG Common](https://github.com/luizfilipeabrahao/ag-common), this ant 
specialisation can be divided in three main areas.

### Environment

#### Pheromone Node

A specialisation of [`BasicNode`](https://github.com/luizfilipeabrahao/ag-common/blob/master/src/main/java/org/ag/common/env/BasicNode.java)
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
[`WarningStimulusType`](src/main/java/org/ag/ants/env/impl/WarningStimulusType.java),
