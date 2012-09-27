package com.markatta.jee5unit.runners;

import org.junit.internal.runners.model.ReflectiveCallable;
import org.junit.internal.runners.statements.Fail;
import org.junit.internal.runners.statements.RunAfters;
import org.junit.internal.runners.statements.RunBefores;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

/**
 * A runner that handles lots of annotated testcase configurations for the tests.
 *
 * @author johan
 */
public class Jee5UnitRunner extends BlockJUnit4ClassRunner {

    public Jee5UnitRunner(Class klass) throws InitializationError {
        super(klass);
    }

    @Override
    protected Statement classBlock(RunNotifier notifier) {
        Statement topStatement = super.classBlock(notifier);

        return topStatement;
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
        //statement = withinTransaction(method, test, statement);

        // the BeforeTestTransaction and AfterTestTransaction annotated methods outside
        // of the transaction
        //   statement = withBeforeTestTransaction(method, test, statement);
        //   statement = withAfterTestTransaction(method, test, statement);

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
