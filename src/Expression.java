package src;

import src.parser.Token;

public abstract class Expression {

    abstract <T> T accept(Visitor<T> visitor);

    private interface Visitor<T> {
        T visit(Binary expression);
        T visit(Grouping expression);
        T visit(Literal expression);
        T visit(Unary expression);
    }

    private static class Binary extends Expression {
        final Expression left;
        final Token operator;
        final Expression right;

        Binary(Expression left, Token operator, Expression right) {
            this.left = left;
            this.operator = operator;
            this.right = right;
        }

        @Override
        <T> T accept(Visitor<T> visitor) {
            return visitor.visit(this);
        }
    }

    private static class Grouping extends Expression {
        final Expression expression;

        Grouping(Expression expression) {
            this.expression = expression;
        }

        @Override
        <T> T accept(Visitor<T> visitor) {
            return visitor.visit(this);
        }
    }

    private static class Literal extends Expression {
        final Object value;

        Literal(Object value) {
            this.value = value;
        }

        @Override
        <T> T accept(Visitor<T> visitor) {
            return visitor.visit(this);
        }
    }

    private static class Unary extends Expression {
        final Token operator;
        final Expression right;

        Unary(Token operator, Expression right) {
            this.operator = operator;
            this.right = right;
        }

        @Override
        <T> T accept(Visitor<T> visitor) {
            return visitor.visit(this);
        }
    }
}
