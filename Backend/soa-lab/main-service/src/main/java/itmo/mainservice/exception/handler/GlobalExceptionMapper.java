package itmo.mainservice.exception.handler;

import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.GenericEntity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {

    @XmlRootElement
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @XmlAccessorType(XmlAccessType.FIELD)
    private static class StackTraceElement{
        private String className;
        private String message;
    }

    @XmlRootElement
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @XmlAccessorType(XmlAccessType.FIELD)
    private static class StackTrace{
        private String throwableName;
        private StackTraceElement element;
    }

    @Override
    public Response toResponse(Throwable throwable) {
        List<StackTrace> data = Arrays.stream(throwable.getStackTrace()).map(v -> new StackTrace(throwable.getClass().getName(),new StackTraceElement(v.getClassName(),v.toString()))).collect(Collectors.toList());
        GenericEntity<List<StackTrace>> entity = new GenericEntity<>(data){};
        return Response.ok().entity(entity).build();
    }
}
