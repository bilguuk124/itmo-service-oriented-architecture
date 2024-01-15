package itmo.secondservice.config;

import itmo.library.ErrorBody;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.Serial;

@WebServlet("/AppExceptionHandler")
@Produces(MediaType.APPLICATION_XML)
public class AppExceptionHandler extends HttpServlet {
    @Serial
    private static final long serialVersionUID = 1L;

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
        String message = (String) request.getAttribute("jakarta.servlet.error.message");
        Integer statusCode = (Integer) request.getAttribute("jakarta.servlet.error.status_code");
        response.setContentType(MediaType.APPLICATION_XML);
        ErrorBody errorBody = ErrorBody.builder()
                .errorCode(statusCode)
                .message("Unknown error")
                .details(message)
                .build();
        PrintWriter out = response.getWriter();
        JAXBContext context = JAXBContext.newInstance(AppExceptionHandler.class, ErrorBody.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.marshal(errorBody, out);
        logger.info("Processed default error fallback");
    }
}
