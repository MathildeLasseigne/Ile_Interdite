package modele;

public class Coord {

	private final int absc;
	private final int ord;
	
	/**
	 * Cree une coord (abscisse, ordonnee)
	 * @param absc
	 * @param ord
	 */
	public Coord(int absc, int ord) {
		this.absc = absc;
		this.ord = ord;
	}
	
	public int getAbsc() {
		return this.absc;
	}
	
	public int getOrd() {
		return this.ord;
	}
	
	public String toString() {
		return "("+absc+", "+ord+")";
	}
}
