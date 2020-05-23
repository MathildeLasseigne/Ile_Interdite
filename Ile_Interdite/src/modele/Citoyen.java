package modele;

public class Citoyen extends Role {

	public Citoyen() {
		
	}
	
	@Override
	public String toString() {
		return "Joueur";
	}

	@Override
	public String getPower() {
		return "Pas de pouvoir spécial, citoyen ordinaire";
	}

	@Override
	public void utilisePower() {
	}

	@Override
	public boolean getFinPower() {
		return false;
	}

	@Override
	public void resetPower() {
	}

}
