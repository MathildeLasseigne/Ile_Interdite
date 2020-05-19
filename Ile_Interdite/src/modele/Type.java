package modele;

public abstract class Type {
	public Type() {
		
	}
	
	/**
	 * Verifie si la zone a une fonction speciale (Comme heliport ou Artefact)
	 * @return
	 */
	public abstract boolean isSpecial();
	
	/**
	 * Verifie si la zone possede un artefact
	 * @return
	 */
	public abstract boolean hasArtefact(); 
	
	/**
	 * Verifie si la zone influence la fin du jeu
	 * @return
	 */
	public abstract boolean isExit();
	
	public abstract boolean isFull();
}
