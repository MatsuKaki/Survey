package components;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/survey")
public class SurveyComponent {
    @GET
    @Produces("text/plain")
    public String get() {
    	//return Response.ok().build();
    	return "Hello RestService!";
    }
 
    /*@POST
    public Response post(Product product) {
        Products.put(product);
        return Response.ok(product).build();
    }
 
    @GET
    public Response get() {
        List<Product> products = Products.get();
        GenericEntity<List<Product>> entity = new GenericEntity<List<Product>>(products) {};
        return Response.ok(entity).build();
    }*/
}
