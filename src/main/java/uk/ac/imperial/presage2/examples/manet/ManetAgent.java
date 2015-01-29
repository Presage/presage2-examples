package uk.ac.imperial.presage2.examples.manet;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import uk.ac.imperial.presage2.core.environment.ActionHandlingException;
import uk.ac.imperial.presage2.core.environment.UnavailableServiceException;
import uk.ac.imperial.presage2.core.messaging.Performative;
import uk.ac.imperial.presage2.core.simulator.Initialisor;
import uk.ac.imperial.presage2.core.simulator.Step;
import uk.ac.imperial.presage2.examples.motion.SituatedAgent;
import uk.ac.imperial.presage2.util.location.Location;
import uk.ac.imperial.presage2.util.network.BasicNetworkConnector;
import uk.ac.imperial.presage2.util.network.Message;
import uk.ac.imperial.presage2.util.network.NetworkAddress;
import uk.ac.imperial.presage2.util.network.NetworkConnector;
import uk.ac.imperial.presage2.util.network.UnicastMessage;

public class ManetAgent extends SituatedAgent {

	NetworkConnector network;

	ManetAgent(UUID id, String name, Location myLocation) {
		super(id, name, myLocation);
	}

	@Initialisor
	@Override
	public void initialise() {
		super.initialise();
		try {
			network = getEnvironmentService(BasicNetworkConnector.class);
		} catch (UnavailableServiceException e) {
			logger.warn(e);
		}
	}

	/**
	 * Message counters
	 */
	int saidHello = 0;
	int heardHello = 0;

	@Step
	public synchronized void talk(int t) throws ActionHandlingException {
		// find out who's in my communication range
		Set<NetworkAddress> connected = network.getConnectedNodes();
		logger.info("Connected to " + connected.size() + " agents.");

		// send them messages
		for (NetworkAddress addr : connected) {
			network.sendMessage(new UnicastMessage(Performative.INFORM,
					"hello", t, network.getAddress(), addr));
		}
		saidHello += connected.size();
		logger.info("Said hello " + saidHello + " times.");

		// check incoming messages
		List<Message> inbox = network.getMessages();
		for (Message m : inbox) {
			if (m.getType() == "hello") {
				heardHello++;
			}
		}
		logger.info("Heard hello " + heardHello + " times.");
	}

}
