package itmo.mainservice.config;

import itmo.library.ErrorBody;
import itmo.mainservice.service.impl.ErrorBodyGenerator;
import jakarta.inject.Inject;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.core.MediaType;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.Serial;
import java.util.Enumeration;

@WebServlet("/AppExceptionHandler")
public class AppExceptionHandler extends HttpServlet {
    @Serial
    private static final long serialVersionUID = 1L;

    @Inject
    private ErrorBodyGenerator errorBodyGenerator;
    private final Logger logger = LoggerFactory.getLogger(AppExceptionHandler.class);

    protected void doGet(HttpServletRequest request, HttpServletResponse response){
        processError(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response){
        processError(request, response);
    }

    @SneakyThrows
    private void processError(HttpServletRequest request, HttpServletResponse response) {
        logger.info("Processing default error fallback");
        Enumeration<String> enumerationP = request.getAttributeNames();

        int i = 1;
        while(enumerationP.hasMoreElements()){
            logger.info(i++ + " " + enumerationP.nextElement());
        }

        String message = (String) request.getAttribute("jakarta.servlet.error.message");
        Integer statusCode = (Integer) request.getAttribute("jakarta.servlet.error.status_code");
        String servletName = (String) request.getAttribute("jakarta.servlet.error.servlet_name");
        if (servletName == null) servletName = "Unknown";

        ErrorBody errorBody = errorBodyGenerator.generateServletError(servletName, statusCode, message);
        response.setContentType(MediaType.APPLICATION_XML);
        PrintWriter out = response.getWriter();
        JAXBContext context = JAXBContext.newInstance(AppExceptionHandler.class, ErrorBody.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.marshal(errorBody, out);
        logger.info("Processed default error fallback");
    }
}