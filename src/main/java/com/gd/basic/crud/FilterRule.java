package com.gd.basic.crud;

import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @author ZhouHR
 */
@Data
@NoArgsConstructor
public class FilterRule {

    /**
     * 字段名
     */
    private String field;

    /**
     * 运算符，如like，equal等
     */
    private RuleOp op;

    /**
     * 值
     */
    private Object value;

    public FilterRule(String field, RuleOp op, Object value) {
        this.field = field;
        this.op = op;
        this.value = value;
    }

    public FilterRule(String field, Object value) {
        this.field = field;
        this.op = RuleOp.equal;
        this.value = value;
    }

    public enum RuleOp {

        equal(" = "), notequal(" != "), contains(" like "), beginwith(" like "), endwith(" like "), less(" < "), lessorequal(" <= "), greater(" > "), greaterorequal(" >= ");

        private String expression;

        private RuleOp(String expression) {
            this.expression = expression;
        }

        public String getExpression() {
            return expression;
        }
    }
}
