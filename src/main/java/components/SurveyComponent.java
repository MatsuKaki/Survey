package components;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/survey")
public class SurveyComponent {
	@GET
	@Produces({ "text/plain", MediaType.APPLICATION_JSON })
	public String get() {
		// return Response.ok().build();
		return "Hello RestService!";
	}
}
