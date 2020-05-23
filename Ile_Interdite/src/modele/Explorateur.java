package modele;

public class Explorateur extends Role {

	public Explorateur() {
		
	}
	
	@Override
	public String toString() {
		return "Explorateur";
	}

	@Override
	public String getPower() {
		return "Peut se déplacer et assécher diagonalement";
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
