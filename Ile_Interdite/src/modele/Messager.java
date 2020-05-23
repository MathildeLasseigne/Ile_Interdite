package modele;

public class Messager extends Role {

	private int nbMax = 1;
	private int utilisation = nbMax;
	
	public Messager() {
		
	}
	
	@Override
	public String toString() {
		return "Messager";
	}

	@Override
	public String getPower() {
		return "Peut donner une clé qu’il possède à un joueur distant (coûte une action)";
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
