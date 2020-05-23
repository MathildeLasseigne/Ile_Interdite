package modele;

public class Navigateur extends Role {
	
	private int nbMax = 1;
	private int utilisation = nbMax;
	
	public Navigateur() {
		
	}
	
	@Override
	public String toString() {
		return "Navigateur";
	}

	@Override
	public String getPower() {
		return "Peut déplacer un autre joueur (coûte une action)";
	}

	@Override
	public void utilisePower() {
		this.utilisation--;
		
	}

	@Override
	public boolean getFinPower() {
		return this.utilisation == 0;
	}

	@Override
	public void resetPower() {
		this.utilisation = nbMax;
	}

}
