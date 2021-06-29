package it.polito.tdp.genes.model;

public class GenePeso {

	private Genes g;
	private double peso;
	public GenePeso(Genes g, double peso) {
		super();
		this.g = g;
		this.peso = peso;
	}
	public Genes getG() {
		return g;
	}
	public void setG(Genes g) {
		this.g = g;
	}
	public double getPeso() {
		return peso;
	}
	public void setPeso(double peso) {
		this.peso = peso;
	}
	@Override
	public String toString() {
		return g + "  " + peso + "\n";
	}
	
	
	
}
