package edu.pjwstk.tests.expression.auxname;

import edu.pjwstk.jps.ast.IExpression;
import edu.pjwstk.jps.result.IAbstractQueryResult;
import edu.pjwstk.jps.result.IBagResult;
import edu.pjwstk.jps.result.IBinderResult;
import edu.pjwstk.jps.result.ICollectionResult;
import edu.pjwstk.tests.expression.AbstractExpressionTest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AbstractAuxiliaryNameExpressionTest extends AbstractExpressionTest {

    protected IBinderResult[] getBinders(IExpression e) {

        List<IBinderResult> binders;

        e.accept(visitor);

        IAbstractQueryResult result = qres.pop();

        if (result instanceof ICollectionResult) {
            binders = ((IBagResult) result).getElements()
                    .stream()
                    .filter(x -> x instanceof IBinderResult)
                    .map(x -> (IBinderResult) x)
                    .collect(Collectors.toList());
        } else {
            binders = new ArrayList<>();
            binders.add((IBinderResult)result);
        }

        return binders.toArray(new IBinderResult[binders.size()]);
    }
}
