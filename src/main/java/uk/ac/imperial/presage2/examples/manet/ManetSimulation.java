package uk.ac.imperial.presage2.examples.manet;

import uk.ac.imperial.presage2.core.simulator.Parameter;
import uk.ac.imperial.presage2.core.simulator.RunnableSimulation;
import uk.ac.imperial.presage2.core.simulator.Scenario;
import uk.ac.imperial.presage2.core.util.random.Random;
import uk.ac.imperial.presage2.util.environment.AbstractEnvironmentModule;
import uk.ac.imperial.presage2.util.location.Location;
import uk.ac.imperial.presage2.util.location.LocationStoragePlugin;
import uk.ac.imperial.presage2.util.location.MoveHandler;
import uk.ac.imperial.presage2.util.location.ParticipantLocationService;
import uk.ac.imperial.presage2.util.location.area.Area;
import uk.ac.imperial.presage2.util.network.BasicNetworkConnector;
import uk.ac.imperial.presage2.util.network.NetworkModule;

public class ManetSimulation extends RunnableSimulation {

	@Parameter
	public int size;

	@Parameter(optional = true)
	public int agents = 1;

	@Override
	public void initialiseScenario(Scenario scenario) {
		addModule(Area.Bind.area2D(size, size));
		addModule(new AbstractEnvironmentModule()
				.addActionHandler(MoveHandler.class)
				.addParticipantEnvironmentService(
						ParticipantLocationService.class)
				.addParticipantEnvironmentService(BasicNetworkConnector.class));
		scenario.addClass(LocationStoragePlugin.class);
		// create the network
		addModule(NetworkModule.fullyConnectedNetworkModule());

		for (int i = 0; i < agents; i++) {
			scenario.addAgent(new ManetAgent(
					Random.randomUUID(),
					"a" + i,
					new Location(Random.randomInt(size), Random.randomInt(size))));
		}
	}

}
