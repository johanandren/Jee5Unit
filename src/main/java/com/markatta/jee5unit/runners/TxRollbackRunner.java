package com.markatta.jee5unit.runners;

import com.markatta.jee5unit.framework.EJBTestCase;
import com.markatta.jee5unit.framework.EntityTestCase;
import org.junit.internal.runners.model.ReflectiveCallable;
import org.junit.internal.runners.statements.Fail;
import org.junit.internal.runners.statements.RunAfters;
import org.junit.internal.runners.statements.RunBefores;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

/**
 * <p>
 * A runner for {@link EJBTestCase} and {@link EntityTestCase} testcases that
 * will start a transaction before each test and then roll the transaction back
 * after each test.
 * </p>
 * <p>
 * Use it by annotating the test case with the Junit annotation like this:
 * <code>@RunWith(TxRollBackRunner.class)</code>
 * </p>
 * <p>
 * Adds two annotations that makes it possible to do operations before and
 * after the transaction by annotating them with {@link BeforeTestTransaction}
 * and {@link AfterTestTransaction}.
 * </p>
 * @author johan
 * @since 1.2
 */
public final class TxRollbackRunner extends BlockJUnit4ClassRunner {

    public TxRollbackRunner(Class klass) throws InitializationError {
        super(klass);
    }



    @Override
    protected Statement methodBlock(FrameworkMethod method) {
        Object test;
        try {
            test = new ReflectiveCallable() {

                @Override
                protected Object runReflectiveCall() throws Throwable {
                    return createTest();
                }
            }.run();
        } catch (Throwable e) {
            return new Fail(e);
        }

        Statement statement = methodInvoker(method, test);
        statement = possiblyExpectingExceptions(method, test, statement);
        statement = withPotentialTimeout(method, test, statement);
        statement = withBefores(method, test, statement);
        statement = withAfters(method, test, statement);

        // befores, afters and actual test evaluation inside our transaction statement
        statement = withinTransaction(method, test, statement);

        // the BeforeTestTransaction and AfterTestTransaction annotated methods outside
        // of the transaction
        statement = withBeforeTestTransaction(method, test, statement);
        statement = withAfterTestTransaction(method, test, statement);

        return statement;
    }

    private Statement withinTransaction(FrameworkMethod method, Object target, Statement statement) {
        return new TransactionStatement(target, statement);
    }

    private Statement withBeforeTestTransaction(FrameworkMethod method, Object target, Statement statement) {
        return new RunBefores(statement, getTestClass().getAnnotatedMethods(BeforeTestTransaction.class), target);
    }

    protected Statement withAfterTestTransaction(FrameworkMethod method, Object target, Statement statement) {
        return new RunAfters(statement, getTestClass().getAnnotatedMethods(AfterTestTransaction.class), target);
    }
}
