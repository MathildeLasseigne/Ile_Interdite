package modele;

public class Heliport extends Type {
	private int[] artefacts;
	
	
	public Heliport() {
		this.artefacts = new int[4];
		for(int i = 0; i<artefacts.length; i++) {
			artefacts[i] = 0;
		}
	}
	
	@Override
	public boolean isSpecial() {
		return true;
	}

	@Override
	public boolean hasArtefact() {
		return false;
	}

	@Override
	public boolean isExit() {
		return true;
	}

	@Override
	public boolean isFull() {
		boolean full = true;
		for(int i = 0; i<artefacts.length; i++) {
			if(artefacts[i]<1) {
				full = false;
			}
		}
		return full;
	}
	
	/**
	 * Ajoute les artefacts en parametre a la liste d artefacts presents dans l heliport
	 * @param artefacts
	 */
	public void addArtefact(int[] artefacts) {
		for(int i = 0; i<this.artefacts.length; i++) {
			this.artefacts[i] += artefacts[i];
		}
	}
	
	/**
	 * Enleve les artefacts en parametre a la liste d artefacts presents dans l heliport
	 * @param artefacts
	 */
	public void removeArtefact(int[] artefacts) {
		for(int i = 0; i<this.artefacts.length; i++) {
			this.artefacts[i] -= artefacts[i];
			if(this.artefacts[i] < 0) {
				System.out.println("Nb artefacts heliport negatif !");
			}
		}
	}

}