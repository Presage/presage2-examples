package uk.ac.imperial.presage2.examples.gameoflife

import uk.ac.imperial.presage2.core.simulator.Initialisor;
import uk.ac.imperial.presage2.core.simulator.Step;
import uk.ac.imperial.presage2.util.participant.AbstractParticipant
import uk.ac.imperial.presage2.util.participant.AbstractParticipant.State

class GameOfLifeAgent extends AbstractParticipant {

	public State cell
	public State alive
	public State range

	def GameOfLife game

	GameOfLifeAgent(id, name, x, y, alive) {
		super(id, name)
		this.cell = createState("cell", [x,y])
		this.alive = createState("alive", alive)
		this.range = createState("range", 1)
	}
	
	
	@Initialisor
	@Override
	public void initialise() {
		super.initialise()
		game = getEnvironmentService(GameOfLife.class)
	}



	@Step
	public void step(int t) {
		int nb = game.getAliveNeighbours(getID())
		boolean alive = alive.get()
		if(alive) {
			switch(nb) {
				case 0..1:
					// die
					act(game.getStateChangeAction(false))
					break
				case 2..3:
					// stay living, no action
					break
				default:
					// more than 3, die
					act(game.getStateChangeAction(false))
			}
		} else if(nb == 3) {
			// born
			act(game.getStateChangeAction(true))
		}
	}

}
