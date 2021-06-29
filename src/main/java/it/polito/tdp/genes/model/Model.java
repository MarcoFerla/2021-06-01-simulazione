package it.polito.tdp.genes.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.genes.db.GenesDao;
import static java.util.Comparator.*;

public class Model {
	
	private GenesDao dao;
	private SimpleWeightedGraph<Genes,DefaultWeightedEdge> grafo;
	private Map<String,Genes> idMap;
	
	public Model() {
		this.dao= new GenesDao();
		idMap= new HashMap<String,Genes>();
		dao.getAllGenes(idMap);
		
	}
	
	public void creaGrafo() {
		this.grafo= new SimpleWeightedGraph<Genes,DefaultWeightedEdge>(DefaultWeightedEdge.class);
		Graphs.addAllVertices(grafo, dao.getVertici(idMap));
		
		for(Adiacenza a: this.dao.getAdiacenze(idMap)) {
			DefaultWeightedEdge e= grafo.getEdge(a.getG1(), a.getG2());
			if(e==null) {
				if(a.getG1().getChromosome()!=a.getG2().getChromosome()) {
					Graphs.addEdgeWithVertices(grafo, a.getG1(), a.getG2(), Math.abs(a.getPeso()));
				}
				if(a.getG1().getChromosome()==a.getG2().getChromosome()) {
					Graphs.addEdgeWithVertices(grafo, a.getG1(), a.getG2(), 2*Math.abs(a.getPeso()));
				}
			}
		}
	}
	
	public int getNumArchi() {
		return this.grafo.edgeSet().size();
		
	}
	public int getNumVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public Set<Genes> getVertici(){
		return this.grafo.vertexSet();
	}
	
	public List<GenePeso> getGeniAdiacenti(Genes partenza){
		
		List<GenePeso> result= new ArrayList<GenePeso>();
		for(Genes gene: Graphs.neighborListOf(grafo, partenza)) {
			
			Double peso= grafo.getEdgeWeight(grafo.getEdge(partenza, gene));
			GenePeso gp= new GenePeso(gene,peso);
			result.add(gp);
		}
		Comparator<GenePeso> comp= comparing(GenePeso::getPeso, reverseOrder());
		Collections.sort(result,comp);
		return result;
	}
	
	
}
