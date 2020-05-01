package modele;

public class Zone {
	public Etat etat;
	public Type type;
	
	private Coord coord;
	
	/**
	 * Cree une zone d'etat initial normal et de type initial ordinaire
	 * @param c Les coordonnes de la zone
	 */
	public Zone(Coord c) {
		this.coord = c;
		this.type = new Ordinaire();
		etat = Etat.normale;
	}
	
	
	/**
	 * Cree une zone d'etat initial normal
	 * @param c Les coordonnes de la zone
	 * @param type Le type de zone
	 */
	public Zone(Coord c, Type type) {
		this.coord = c;
		this.type = type;
		etat = Etat.normale;
	}
	
	public Zone(Coord c, Type type, Etat e) {
		this.coord = c;
		this.type = type;
		etat = e;
	}
	
	/**
	 * Verifie si la zone est assechable
	 * @return
	 */
	public boolean estAssechable() {
		if (etat == Etat.inondee) {
			return true;
		}
		return false;
	}
	
	/**
	 * Asseche la zone si elle est assechable
	 */
	public void asseche() {
		if (estAssechable()) {
			etat = Etat.normale;
		}
	}
	
	/**
	 * Inonde une zone
	 */
	public void inonde() {
		if (etat == Etat.normale) {
			etat = Etat.inondee;
		} else if (etat == Etat.inondee) {
			etat = Etat.submergee;
		}
	}
	
	/**
	 * Verifie si la zone est accessible
	 * @return
	 */
	public boolean estAccessible() {
		if (etat == Etat.submergee) {
			return false;
		}
		return true;
	}
	
	/**
	 * Renvoie les coordonees de la zone
	 * @return
	 */
	public Coord getCoord() {
		return coord;
	}
	
}
