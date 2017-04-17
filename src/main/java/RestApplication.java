import javax.ws.rs.ApplicationPath;

import com.sun.jersey.api.core.PackagesResourceConfig;

@ApplicationPath("/test")
public class RestApplication extends PackagesResourceConfig {
	
    public RestApplication() {
        super("components");
    }
    
}