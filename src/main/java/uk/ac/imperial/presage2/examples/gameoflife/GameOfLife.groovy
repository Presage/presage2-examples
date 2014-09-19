package uk.ac.imperial.presage2.examples.gameoflife

import java.util.UUID;

import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import uk.ac.imperial.presage2.core.Action
import uk.ac.imperial.presage2.core.environment.ActionHandler;
import uk.ac.imperial.presage2.core.environment.ActionHandlingException;
import uk.ac.imperial.presage2.core.environment.EnvironmentRegistrationRequest;
import uk.ac.imperial.presage2.core.environment.EnvironmentService
import uk.ac.imperial.presage2.core.environment.EnvironmentSharedStateAccess;
import uk.ac.imperial.presage2.core.simulator.FinishCondition;
import uk.ac.imperial.presage2.core.simulator.Step;
import uk.ac.imperial.presage2.util.participant.StateAccessor;

@Singleton
class GameOfLife extends EnvironmentService implements ActionHandler {

	Logger logger = Logger.getLogger("GOL")
	def xmax
	def ymax

	@Inject
	GameOfLife(EnvironmentSharedStateAccess sharedState,
	@Named("params.xsize") int xsize,
	@Named("params.ysize") int ysize) {
		super(sharedState);
		xmax = xsize
		ymax = ysize
	}

	@Override
	public synchronized void registerParticipant(EnvironmentRegistrationRequest req) {
		def id = req.getParticipantID()
		def pos = sharedState.get("cell",id)
		sharedState.createGlobal(pos.toString(), id)
	}


	def getAliveNeighbours(id) {
		def pos = sharedState.get("cell", id)
		int x = pos[0]; int y = pos[1]
		def r = sharedState.get("range", id)
		def nb = []
		for(dx in x-r..x+r) {
			for(dy in y-r..y+r) {
				if(dx == x && dy == y)
					continue
				nb.add(sharedState.get("alive", sharedState.getGlobal([dx%xmax,dy%ymax].toString())))
			}
		}
		return nb.findAll{ it }.size()
	}

	@StateAccessor("alive")
	Boolean getAlive(UUID id) {
		return sharedState.get("alive", id)
	}

	def getStateChangeAction(boolean alive) {
		return new ChangeState(alive)
	}

	class ChangeState extends Action {
		boolean alive

		ChangeState(boolean alive) {
			super();
			this.alive = alive;
		}
	}

	public boolean canHandle(Action action) {
		return action instanceof ChangeState;
	}

	public Object handle(Action action, UUID actor) throws ActionHandlingException {
		sharedState.change("alive", actor, action.alive)
		return null;
	}

	@Step(nice=-20)
	def logGameState() {
		for(y in 0..ymax-1) {
			String line = ""
			for(x in 0..xmax-1) {
				def pos = [x,y]
				def alive = sharedState.get("alive", sharedState.getGlobal(pos.toString()))
				line += (alive ? "X" : " ")
			}
			logger.info(line)
		}
	}
	
	@FinishCondition
	boolean stopIfAllDead() {
		for(x in 0..xmax-1) {
			for(y in 0..ymax-1) {
				def alive = sharedState.get("alive", sharedState.getGlobal([x,y].toString()))
				if(alive)
					return false
			}
		}
		return true
	}
	
}
