package modele;

public class Ordinaire extends Type{
	
	public Ordinaire() {
		
	}

	@Override
	public boolean isExit() {
		return false;
	}

	@Override
	public boolean hasArtefact() {
		return false;
	}

	@Override
	public boolean isSpecial() {
		return false;
	}

	@Override
	public boolean isFull() {
		return false;
	}
}
