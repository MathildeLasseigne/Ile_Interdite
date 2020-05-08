package modele;

public class Zone {
	private Etat etat;
	private Type type;
	
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
	public boolean asseche() {
		if (estAssechable()) {
			etat = Etat.normale;
			return true;
		}
		return false;
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
	 * Verifie si la zone est accessible (cad normale ou inondee)
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
	
	public Etat getEtat() {
		return this.etat;
	}
	
	public Type getType() {
		return this.type;
	}
	
	
}
