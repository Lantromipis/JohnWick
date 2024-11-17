package ru.ifmo.se.johnwick.rsql.visitor;

import cz.jirutka.rsql.parser.ast.*;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import ru.ifmo.se.johnwick.exception.UnknownEntityFieldException;
import ru.ifmo.se.johnwick.exception.UnsupportedRsqlOperatorException;
import ru.ifmo.se.johnwick.rsql.JpaRsqlVisitorParams;
import ru.ifmo.se.johnwick.rsql.field.JpaRsqlFieldProvider;

import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;

public abstract class AbstractJpaRsqlVisitor<T> implements RSQLVisitor<Predicate, JpaRsqlVisitorParams<T>> {
    private final Map<String, JpaRsqlFieldProvider> fieldProviders;

    public AbstractJpaRsqlVisitor(Map<String, JpaRsqlFieldProvider> fieldProviders) {
        this.fieldProviders = fieldProviders;
    }

    @Override
    public Predicate visit(AndNode node, JpaRsqlVisitorParams<T> param) {
        return andCondition(node, param);
    }

    @Override
    public Predicate visit(OrNode node, JpaRsqlVisitorParams<T> param) {
        return orCondition(node, param);
    }

    @Override
    public Predicate visit(ComparisonNode node, JpaRsqlVisitorParams<T> param) {
        String selector = node.getSelector();
        List<String> args = node.getArguments();
        JpaRsqlFieldProvider fieldProvider = fieldProviders.get(selector);

        if (fieldProvider == null) {
            throw new UnknownEntityFieldException(selector);
        }

        Path fieldPath = fieldProvider.getFieldPath(param);
        Comparable fieldValue = fieldProvider.getTypedValue(args.get(0));

        ComparisonOperator comparisonOperator = node.getOperator();
        if (RSQLOperators.EQUAL.equals(comparisonOperator)) {
            return param.getCriteriaBuilder().equal(fieldPath, fieldValue);
        } else if (RSQLOperators.NOT_EQUAL.equals(comparisonOperator)) {
            return param.getCriteriaBuilder().notEqual(fieldPath, fieldValue);
        } else if (RSQLOperators.GREATER_THAN.equals(comparisonOperator)) {
            return param.getCriteriaBuilder().greaterThan(fieldPath, fieldValue);
        } else if (RSQLOperators.GREATER_THAN_OR_EQUAL.equals(comparisonOperator)) {
            return param.getCriteriaBuilder().greaterThanOrEqualTo(fieldPath, fieldValue);
        } else if (RSQLOperators.LESS_THAN.equals(comparisonOperator)) {
            return param.getCriteriaBuilder().lessThan(fieldPath, fieldValue);
        } else if (RSQLOperators.LESS_THAN_OR_EQUAL.equals(comparisonOperator)) {
            return param.getCriteriaBuilder().lessThanOrEqualTo(fieldPath, fieldValue);
        } else {
            throw new UnsupportedRsqlOperatorException(comparisonOperator.getSymbol());
        }
    }

    protected Predicate andCondition(AndNode node, JpaRsqlVisitorParams<T> param) {
        return conditionFromNodes(node.getChildren(), (p1, p2) -> param.getCriteriaBuilder().and(p1, p2), param);
    }

    protected Predicate orCondition(OrNode node, JpaRsqlVisitorParams<T> param) {
        return conditionFromNodes(node.getChildren(), (p1, p2) -> param.getCriteriaBuilder().or(p1, p2), param);
    }

    protected Predicate conditionFromNodes(List<Node> nodes, BinaryOperator<Predicate> joiner, JpaRsqlVisitorParams<T> param) {
        Predicate predicate = null;
        for (Node node : nodes) {
            Predicate subPredicate = null;
            if (node instanceof AndNode andNode) {
                subPredicate = andCondition(andNode, param);
            } else if (node instanceof OrNode orNode) {
                subPredicate = orCondition(orNode, param);
            } else if (node instanceof ComparisonNode comparisonNode) {
                subPredicate = visit(comparisonNode, param);
            }

            if (subPredicate != null) {
                predicate = predicate == null ? subPredicate : joiner.apply(predicate, subPredicate);
            }
        }

        return predicate;
    }
}
