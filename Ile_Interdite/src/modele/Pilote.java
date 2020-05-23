package modele;

public class Pilote extends Role {
	
	private int nbMax = 1;
	private int utilisation = nbMax;
	
	public Pilote() {
		
	}
	
	@Override
	public String toString() {
		return "Pilote";
	}

	@Override
	public String getPower() {
		return "Peut se d�placer vers une zone non submerg�e arbitraire (co�te une action)";
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
