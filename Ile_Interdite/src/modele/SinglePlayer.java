package modele;

public class SinglePlayer {

	private int id;
	private Coord coord;
	
	public SinglePlayer(int id, Coord c) {
		this.id = id;
		this.coord = c;
	}

	public Coord getCoord() {
		return this.coord;
	}
	
	
	public int getId() {
		return this.id;
	}
	
	/**
	 * Modifie la coord du player
	 * @param c
	 */
	public void move(Coord c) {
		this.coord = c;
	}
	
	public String toString() {
		return "Joueur "+this.id;
	}
	
	
}
