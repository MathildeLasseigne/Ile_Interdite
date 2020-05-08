package modele;

public class Ordinaire extends Type{
	
	public Ordinaire() {
		
	}

	@Override
	public boolean isFinal() {
		return false;
	}

	@Override
	public boolean hasArtefact() {
		return false;
	}
}
