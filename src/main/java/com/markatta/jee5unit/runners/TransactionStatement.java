package com.markatta.jee5unit.runners;

import com.markatta.jee5unit.framework.EntityTransactional;
import javax.persistence.EntityTransaction;
import org.junit.runners.model.Statement;

/**
 * Statement that starts and does rollback on an exception. Used in the
 * {@link TxRollbackRunner}
 * 
 * @author johan
 */
class TransactionStatement extends Statement {

    private Object target;

    private Statement statement;

    /**
     * @param target TestCase target
     * @param statement The statement to evaluate inside the transaction
     */
    public TransactionStatement(Object target, Statement statement) {
        this.target = target;
        this.statement = statement;
    }

    @Override
    public void evaluate() throws Throwable {
        if (!(target instanceof EntityTransactional)) {
            throw new Jee5UnitRunnerException("Testcase annotated with the TxRollbackRunner must implement the Transactional interface. " +
                    "Perhaps you have forgot to extend EntityTestCase or EJBTestCase when creating your testcase?");
        }

        EntityTransactional transactional = (EntityTransactional) target;

        EntityTransaction transaction = transactional.getTransaction();
        transaction.begin();
        try {
            statement.evaluate();
        } catch (Throwable ex) {
            // rollback database on exception to
            if (transaction.isActive()) {
                transaction.rollback();
            }
            // re-throw
            throw ex;
        }

        if (transaction.isActive()) {
            transaction.rollback();
        }

    }
}
