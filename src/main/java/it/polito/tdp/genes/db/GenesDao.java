package it.polito.tdp.genes.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.genes.model.Adiacenza;
import it.polito.tdp.genes.model.Genes;


public class GenesDao {
	
	public void getAllGenes(Map<String,Genes> idMap){
		String sql = "SELECT DISTINCT GeneID, Essential, Chromosome FROM Genes";
		
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
                if(!idMap.containsKey(res.getString("GeneID"))) {
                	Genes genes = new Genes(res.getString("GeneID"), 
    						res.getString("Essential"), 
    						res.getInt("Chromosome"));
    				idMap.put(res.getString("GeneID"), genes);
                }
			}
			res.close();
			st.close();
			conn.close();
		
			
		} catch (SQLException e) {
			e.printStackTrace();

		}
	}
	
	public List<Genes> getVertici(Map<String,Genes> idMap){
		String sql = "SELECT DISTINCT genes.GeneID "
				+ "FROM genes "
				+ "WHERE genes.Essential='Essential' ";
		List<Genes> result = new ArrayList<Genes>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
                if(idMap.containsKey(res.getString("genes.GeneID"))) {
                	Genes g= idMap.get(res.getString("genes.GeneID"));
                	result.add(g);
                }
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Adiacenza> getAdiacenze(Map<String,Genes> idMap){
		String sql = "SELECT i.GeneID1, i.GeneID2, i.Expression_Corr AS peso "
				+ "FROM interactions i, genes g1, genes g2 "
				+ "WHERE i.GeneID1= g1.GeneID AND i.GeneID2= g2.GeneID AND g2.Essential='Essential' AND "
				+ "g1.Essential= 'Essential' AND g1.GeneID<> g2.GeneID "
				+ "GROUP BY i.GeneID1, i.GeneID2 ";
		
		List<Adiacenza> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
                if(idMap.containsKey(res.getString("i.GeneID1")) && idMap.containsKey(res.getString("i.GeneID2")) ) {
                	Genes g1= idMap.get(res.getString("i.GeneID1"));
                	Genes g2= idMap.get(res.getString("i.GeneID2"));
                	Adiacenza a= new Adiacenza(g1,g2,res.getDouble("peso"));
                	result.add(a);
                }
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}


	
}
