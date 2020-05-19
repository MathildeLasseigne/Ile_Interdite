package modele;

public class Artefact extends Type {
	private int element;
	private boolean rempli;
	
	/**
	 * Cree une zone contenant un artefact selon l element donne en parametre
	 * </br>Ordre : 0: Eau, 1: Feu, 2: Air, 3: Terre
	 * @param element
	 */
	public Artefact(int element) {
		this.element = element;
		this.rempli = true;
	}
	
	
	@Override
	public boolean isSpecial() {
		return this.rempli;
	}

	@Override
	public boolean hasArtefact() {
		return this.rempli;
	}

	@Override
	public boolean isFinal() {
		return false;
	}
	
	/**
	 * Recupere l element de l artefact
	 * </br>Ordre : 0: Eau, 1: Feu, 2: Air, 3: Terre
	 * @return
	 */
	public int getElement() {
		return this.element;
	}
	
	/**
	 * Prend l artefact de la zone, celle ci deviens similaire a une zone de type Ordinaire
	 * </br>Ordre : 0: Eau, 1: Feu, 2: Air, 3: Terre
	 * @return l element de l artefact
	 */
	public int takeArtefact() {
		if(this.rempli == false) {
			System.out.println("Artefact deja recupere");
		}
		this.rempli = false;
		return this.element;
	}
	

}
