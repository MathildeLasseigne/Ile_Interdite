package modele;

public class Ingenieur extends Role {
	
	private int nbMax = 2;
	private int utilisation = nbMax;
	
	public Ingenieur() {
		
	}
	
	@Override
	public String toString() {
		return "Ingénieur";
	}

	@Override
	public String getPower() {
		return "Peut assécher deux zones pour une seule action";
	}
	
	/**
	 * Diminue le nb d utilisation du pouvoir de 1
	 */
	public void utilisePower() {
		this.utilisation--;
	}
	
	/**
	 * Verifie si le nb d utilisations restantes du pouvoir est 0
	 * @return
	 */
	public boolean getFinPower() {
		return this.utilisation == 0;
	}
	
	/**
	 * Remet a 0 le nombre d utilisation du pouvoir
	 */
	public void resetPower() {
		this.utilisation = nbMax;
	}
}
