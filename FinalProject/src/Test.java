import java.io.IOException;
import java.io.PrintWriter;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;


public class Test {
	public static void main(String[] args) throws SolrServerException, IOException{
		search();
		
	}
	
	public static void search() throws SolrServerException, IOException {
		   HttpSolrServer solr = new HttpSolrServer("http://localhost:8983/solr/Mar25Test");

		   SolrQuery query = new SolrQuery();
		   query.setQuery("Pho");
		   //query.addFilterQuery("cat:electronics","store:amazon.com");
		   //query.setFields("id","price","merchant","cat","store");
		   query.setStart(0);    
		   //query.set("defType", "edismax");

		   QueryResponse response = solr.query(query);
		   SolrDocumentList results = response.getResults();
		   String str = results.get(0).toString();
		   System.out.println(str);
		   // get location
		   // str = str.substring(str.indexOf("location=[") + 10, str.indexOf("]"));
		   // get review 
		   str = str.substring(str.indexOf("review=[") + 8, str.indexOf(", _version") - 1);
		   System.out.println(str);
		 }
}
