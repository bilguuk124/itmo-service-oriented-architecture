package itmo.mainservice.config;

import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;
import jakarta.inject.Inject;
import jakarta.transaction.TransactionManager;
import jakarta.transaction.UserTransaction;
import jakarta.ws.rs.ext.Provider;
import lombok.Getter;

import java.util.logging.Logger;

@Provider
public class TransactionProvider {
    private static final int MAX_ACTIVES = 50;
    private static final int MAX_TIMEOUT = 300000;
    private static final int DEFAULT_JTA_TIMEOUT = 300000;

    @Getter
    private static TransactionManager transactionManager;
    @Getter
    private static UserTransaction userTransaction;

    @Inject
    private static Logger logger = Logger.getLogger(TransactionProvider.class.getName());

    static {
        initAtomikos();
    }
    private static void initAtomikos() {
        try {
            com.atomikos.icatch.jta.UserTransactionManager utm = new UserTransactionManager();
            utm.setForceShutdown(false);
            utm.init();

            transactionManager = utm;
            userTransaction = new UserTransactionImp();
        } catch (Exception e) {
            logger.severe(e.getMessage());
            throw new RuntimeException("Failed to initialize Atomikos", e);
        }
    }

}
