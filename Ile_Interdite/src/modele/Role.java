package modele;

public abstract class Role {

	public Role() {
		
	}
	
	public abstract String toString();
	
	public abstract String getPower();
	
	public abstract void utilisePower();
	
	public abstract boolean getFinPower();
	
	public abstract void resetPower();
	
	
}
