package modele;

public class Plongeur extends Role {

	public Plongeur() {
		
	}
	
	@Override
	public String toString() {
		return "Plongeur";
	}

	@Override
	public String getPower() {
		return "Peut traverser une zone submerg�e";
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
