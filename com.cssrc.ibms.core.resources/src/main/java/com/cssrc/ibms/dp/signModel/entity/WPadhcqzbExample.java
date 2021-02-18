package com.cssrc.ibms.dp.signModel.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class WPadhcqzbExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public WPadhcqzbExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("ID is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("ID is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(BigDecimal value) {
            addCriterion("ID =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(BigDecimal value) {
            addCriterion("ID <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(BigDecimal value) {
            addCriterion("ID >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("ID >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(BigDecimal value) {
            addCriterion("ID <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(BigDecimal value) {
            addCriterion("ID <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<BigDecimal> values) {
            addCriterion("ID in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<BigDecimal> values) {
            addCriterion("ID not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("ID between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("ID not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andFQzidIsNull() {
            addCriterion("F_QZID is null");
            return (Criteria) this;
        }

        public Criteria andFQzidIsNotNull() {
            addCriterion("F_QZID is not null");
            return (Criteria) this;
        }

        public Criteria andFQzidEqualTo(String value) {
            addCriterion("F_QZID =", value, "fQzid");
            return (Criteria) this;
        }

        public Criteria andFQzidNotEqualTo(String value) {
            addCriterion("F_QZID <>", value, "fQzid");
            return (Criteria) this;
        }

        public Criteria andFQzidGreaterThan(String value) {
            addCriterion("F_QZID >", value, "fQzid");
            return (Criteria) this;
        }

        public Criteria andFQzidGreaterThanOrEqualTo(String value) {
            addCriterion("F_QZID >=", value, "fQzid");
            return (Criteria) this;
        }

        public Criteria andFQzidLessThan(String value) {
            addCriterion("F_QZID <", value, "fQzid");
            return (Criteria) this;
        }

        public Criteria andFQzidLessThanOrEqualTo(String value) {
            addCriterion("F_QZID <=", value, "fQzid");
            return (Criteria) this;
        }

        public Criteria andFQzidLike(String value) {
            addCriterion("F_QZID like", value, "fQzid");
            return (Criteria) this;
        }

        public Criteria andFQzidNotLike(String value) {
            addCriterion("F_QZID not like", value, "fQzid");
            return (Criteria) this;
        }

        public Criteria andFQzidIn(List<String> values) {
            addCriterion("F_QZID in", values, "fQzid");
            return (Criteria) this;
        }

        public Criteria andFQzidNotIn(List<String> values) {
            addCriterion("F_QZID not in", values, "fQzid");
            return (Criteria) this;
        }

        public Criteria andFQzidBetween(String value1, String value2) {
            addCriterion("F_QZID between", value1, value2, "fQzid");
            return (Criteria) this;
        }

        public Criteria andFQzidNotBetween(String value1, String value2) {
            addCriterion("F_QZID not between", value1, value2, "fQzid");
            return (Criteria) this;
        }

        public Criteria andFYhIsNull() {
            addCriterion("F_YH is null");
            return (Criteria) this;
        }

        public Criteria andFYhIsNotNull() {
            addCriterion("F_YH is not null");
            return (Criteria) this;
        }

        public Criteria andFYhEqualTo(String value) {
            addCriterion("F_YH =", value, "fYh");
            return (Criteria) this;
        }

        public Criteria andFYhNotEqualTo(String value) {
            addCriterion("F_YH <>", value, "fYh");
            return (Criteria) this;
        }

        public Criteria andFYhGreaterThan(String value) {
            addCriterion("F_YH >", value, "fYh");
            return (Criteria) this;
        }

        public Criteria andFYhGreaterThanOrEqualTo(String value) {
            addCriterion("F_YH >=", value, "fYh");
            return (Criteria) this;
        }

        public Criteria andFYhLessThan(String value) {
            addCriterion("F_YH <", value, "fYh");
            return (Criteria) this;
        }

        public Criteria andFYhLessThanOrEqualTo(String value) {
            addCriterion("F_YH <=", value, "fYh");
            return (Criteria) this;
        }

        public Criteria andFYhLike(String value) {
            addCriterion("F_YH like", value, "fYh");
            return (Criteria) this;
        }

        public Criteria andFYhNotLike(String value) {
            addCriterion("F_YH not like", value, "fYh");
            return (Criteria) this;
        }

        public Criteria andFYhIn(List<String> values) {
            addCriterion("F_YH in", values, "fYh");
            return (Criteria) this;
        }

        public Criteria andFYhNotIn(List<String> values) {
            addCriterion("F_YH not in", values, "fYh");
            return (Criteria) this;
        }

        public Criteria andFYhBetween(String value1, String value2) {
            addCriterion("F_YH between", value1, value2, "fYh");
            return (Criteria) this;
        }

        public Criteria andFYhNotBetween(String value1, String value2) {
            addCriterion("F_YH not between", value1, value2, "fYh");
            return (Criteria) this;
        }

        public Criteria andFYhidIsNull() {
            addCriterion("F_YHID is null");
            return (Criteria) this;
        }

        public Criteria andFYhidIsNotNull() {
            addCriterion("F_YHID is not null");
            return (Criteria) this;
        }

        public Criteria andFYhidEqualTo(String value) {
            addCriterion("F_YHID =", value, "fYhid");
            return (Criteria) this;
        }

        public Criteria andFYhidNotEqualTo(String value) {
            addCriterion("F_YHID <>", value, "fYhid");
            return (Criteria) this;
        }

        public Criteria andFYhidGreaterThan(String value) {
            addCriterion("F_YHID >", value, "fYhid");
            return (Criteria) this;
        }

        public Criteria andFYhidGreaterThanOrEqualTo(String value) {
            addCriterion("F_YHID >=", value, "fYhid");
            return (Criteria) this;
        }

        public Criteria andFYhidLessThan(String value) {
            addCriterion("F_YHID <", value, "fYhid");
            return (Criteria) this;
        }

        public Criteria andFYhidLessThanOrEqualTo(String value) {
            addCriterion("F_YHID <=", value, "fYhid");
            return (Criteria) this;
        }

        public Criteria andFYhidLike(String value) {
            addCriterion("F_YHID like", value, "fYhid");
            return (Criteria) this;
        }

        public Criteria andFYhidNotLike(String value) {
            addCriterion("F_YHID not like", value, "fYhid");
            return (Criteria) this;
        }

        public Criteria andFYhidIn(List<String> values) {
            addCriterion("F_YHID in", values, "fYhid");
            return (Criteria) this;
        }

        public Criteria andFYhidNotIn(List<String> values) {
            addCriterion("F_YHID not in", values, "fYhid");
            return (Criteria) this;
        }

        public Criteria andFYhidBetween(String value1, String value2) {
            addCriterion("F_YHID between", value1, value2, "fYhid");
            return (Criteria) this;
        }

        public Criteria andFYhidNotBetween(String value1, String value2) {
            addCriterion("F_YHID not between", value1, value2, "fYhid");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}