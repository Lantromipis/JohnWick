package ru.ifmo.se.johnwick.utils;

import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.RSQLParserException;
import cz.jirutka.rsql.parser.UnknownOperatorException;
import cz.jirutka.rsql.parser.ast.Node;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import ru.ifmo.se.johnwick.exception.UnsupportedRsqlOperatorException;

@ApplicationScoped
public class RsqlParserUtils {
    private RSQLParser rsqlParser;

    @PostConstruct
    public void init() {
        rsqlParser = new RSQLParser();
    }

    public Node parsePredicate(String rsqlPredicate) throws UnsupportedRsqlOperatorException, RSQLParserException {
        RSQLParser rsqlParser = new RSQLParser();
        try {
            return rsqlParser.parse(rsqlPredicate);
        } catch (RSQLParserException e) {
            Throwable cause = e.getCause();
            if (cause instanceof UnknownOperatorException unknownOperatorException) {
                throw new UnsupportedRsqlOperatorException(unknownOperatorException.getOperator());
            }
            throw e;
        }
    }
}
