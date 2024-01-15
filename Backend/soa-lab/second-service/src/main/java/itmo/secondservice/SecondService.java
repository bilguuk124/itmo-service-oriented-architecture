package itmo.secondservice;

import jakarta.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;


@ApplicationPath("/")
public class SecondService extends ResourceConfig {
    public SecondService(){
        packages("itmo.secondservice.config",
                "itmo.secondservice.controller",
                "itmo.secondservice.exception.handler");
    }
}