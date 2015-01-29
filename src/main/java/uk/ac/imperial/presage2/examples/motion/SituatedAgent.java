package uk.ac.imperial.presage2.examples.motion;

import java.util.UUID;

import uk.ac.imperial.presage2.core.environment.ActionHandlingException;
import uk.ac.imperial.presage2.core.simulator.Initialisor;
import uk.ac.imperial.presage2.core.simulator.Step;
import uk.ac.imperial.presage2.core.util.random.Random;
import uk.ac.imperial.presage2.util.location.Location;
import uk.ac.imperial.presage2.util.location.Move;
import uk.ac.imperial.presage2.util.participant.AbstractParticipant;

import com.google.inject.Inject;
import com.google.inject.name.Named;

public class SituatedAgent extends AbstractParticipant {

	public State<Location> myLocation;

	@Inject
	@Named("params.size")
	public int size;

	SituatedAgent(UUID id, String name, Location myLocation) {
		super(id, name);
		this.myLocation = new State<Location>("util.location", myLocation);
	}

	@Initialisor
	public void init() {

	}

	@Step
	public void step(int t) throws ActionHandlingException {
		Location loc = myLocation.get();
		logger.info("My location is: " + loc);

		Move m = new Move(Random.randomInt(3) - 1, Random
				.randomInt(3) - 1);
		logger.info("Move "+ m);
		act(m);
	}

}
