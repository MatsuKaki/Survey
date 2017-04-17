package survey;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.Test;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

/**
 * No real unit test : only here to test various ways to reach eleastic search.
 * Will be removed in due time... 
 * @author Kaki
 *
 */
public class TestElasticSearch {

	@Test
	public void testGet() {
		try {

			URL url = new URL("http://localhost:9200/surveys/user_rating/_search");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			String output;
			System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				System.out.println(output);
			}

			conn.disconnect();

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testGetQueryWithJerseyClientAPI() {
		Client client = Client.create();

		try {
			WebResource webResource = client.resource("http://localhost:9200/surveys/_search?").queryParam("q",
					"user_id=\"kdang060116\"");

			ClientResponse response = webResource.accept("application/json").get(ClientResponse.class);
			if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
			}

			String output = response.getEntity(String.class);

			System.out.println("Output from Server .... \n");
			System.out.println(output);

		} catch (Exception e) {

			e.printStackTrace();

		}

	}

	@Test
	public void testGetQueryWithESClientAPI() throws Exception {
		TransportClient client = null;
		try {
			client = new PreBuiltTransportClient(Settings.EMPTY)
					.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));

			SearchResponse response = client.prepareSearch("surveys").setTypes("user")
					.setQuery(QueryBuilders.termQuery("user_id", "kdang060116")).get();
			System.out.println("Output from Server .... \n");
			System.out.println(response.getHits().hits()[0].sourceAsString());
		} catch (Exception exc) {
			exc.printStackTrace();
		} finally {
			// on shutdown
			if (client != null) {
				client.close();
			}
		}
	}

	@Test
	public void testPostWithESClientAPI() {
		TransportClient client = null;
		try {
			
			Calendar cal = GregorianCalendar.getInstance();
			cal.clear();;
			cal.set(1980,7,23);
			
			client = new PreBuiltTransportClient(Settings.EMPTY)
					.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));
			
			Map<String, Object> docSource = new HashMap<String, Object>();
			docSource.put("user_id","kdang060116");
			docSource.put("application_id","CCD");
			docSource.put("question_id",1);
			docSource.put("rating",5);
			docSource.put("rating_date", cal);
			IndexResponse response = client.prepareIndex("surveys","user_rating").setSource(docSource).get();
			System.out.println("Output from Server .... \n");
			System.out.println(response.getResult());
		} catch(Exception exc){
			exc.printStackTrace();
		}
		finally {
			// on shutdown
			if (client != null) {
				client.close();
			}
		}
	}
}
