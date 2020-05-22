package modele;

public class Heliport extends Type {
	private int[] artefacts;
	private int nbPlayers;
	
	
	public Heliport() {
		this.artefacts = new int[4];
		for(int i = 0; i<artefacts.length; i++) {
			artefacts[i] = 0;
		}
		this.nbPlayers = 0;
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
			int nb = artefacts[i];
			this.artefacts[i] -= nb;
			if(this.artefacts[i] < 0) {
				System.out.println("Nb artefacts heliport negatif !");
			}
		}
	}
	
	/**
	 * Retourne le nombre de players presents sur l heliport
	 * @return
	 */
	public int getNbPlayers() {
		return this.nbPlayers;
	}
	
	/**
	 * Ajoute 1 au nombre de players presents sur l heliport
	 */
	public void addPlayer() {
		this.nbPlayers++;
	}
	
	/**
	 * Enleve 1 au nombre de players presents sur l heliport
	 */
	public void removePlayer() {
		this.nbPlayers--;
	}

}
