package itmo.mainservice.exception.handler;

import itmo.mainservice.service.impl.ErrorBodyGenerator;
import jakarta.ejb.EJBTransactionRolledbackException;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class EJBTransactionRolledbackMapper implements ExceptionMapper<EJBTransactionRolledbackException> {

    @Inject
    private ErrorBodyGenerator errorBodyGenerator;

    @Override
    public Response toResponse(EJBTransactionRolledbackException e) {
        return Response
                .status(500)
                .entity(errorBodyGenerator.generateValidationError("Coercion error! Wrong type for parameter"))
                .build();
    }
}
