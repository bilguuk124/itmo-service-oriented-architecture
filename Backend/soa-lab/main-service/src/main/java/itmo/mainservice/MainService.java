package itmo.mainservice;

import jakarta.ws.rs.ApplicationPath;
import org.glassfish.jersey.moxy.xml.MoxyXmlFeature;
import org.glassfish.jersey.server.ResourceConfig;


@ApplicationPath("/api")
public class MainService extends ResourceConfig {
    public MainService() {
        packages("itmo.mainservice.config"
                , "itmo.mainservice.controller",
                "itmo.mainservice.repository",
                "itmo.mainservice.exception.handler");
        register(MoxyXmlFeature.class);
    }


}