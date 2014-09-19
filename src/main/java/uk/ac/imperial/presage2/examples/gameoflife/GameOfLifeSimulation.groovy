package uk.ac.imperial.presage2.examples.gameoflife

import uk.ac.imperial.presage2.core.simulator.Parameter;
import uk.ac.imperial.presage2.core.simulator.RunnableSimulation
import uk.ac.imperial.presage2.core.simulator.Scenario;
import uk.ac.imperial.presage2.core.util.random.Random;
import uk.ac.imperial.presage2.util.environment.AbstractEnvironmentModule;
import uk.ac.imperial.presage2.util.location.Location;
import uk.ac.imperial.presage2.util.location.LocationStoragePlugin;
import uk.ac.imperial.presage2.util.location.MoveHandler;
import uk.ac.imperial.presage2.util.location.ParticipantLocationService;
import uk.ac.imperial.presage2.util.location.area.Area;
import uk.ac.imperial.presage2.util.location.area.AreaService;

class GameOfLifeSimulation extends RunnableSimulation {

	@Parameter(name="xsize", optional=true)
	public int xsize = 20
	@Parameter(name="ysize", optional=true)
	public int ysize = 20
	@Parameter(name="density", optional=true)
	public double initialDensity = 0.5

	@Override
	public void initialiseScenario(Scenario scenario) {
		addModule(new AbstractEnvironmentModule()
			.addActionHandler(GameOfLife.class)
			.addParticipantGlobalEnvironmentService(GameOfLife.class))
		addObjectClass(GameOfLife.class)
		
		Random rnd = new Random()
		for (x in 0..xsize-1) {
			for (y in 0..ysize-1) {
				scenario.addAgent(new GameOfLifeAgent(Random.randomUUID(),
						[x,y].toString(), x, y, (rnd.nextDouble() < initialDensity)))
			}
		}
	}
}
