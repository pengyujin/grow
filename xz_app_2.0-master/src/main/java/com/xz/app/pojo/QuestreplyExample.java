package com.xz.app.pojo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class QuestreplyExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public QuestreplyExample() {
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

        public Criteria andReplyidIsNull() {
            addCriterion("replyid is null");
            return (Criteria) this;
        }

        public Criteria andReplyidIsNotNull() {
            addCriterion("replyid is not null");
            return (Criteria) this;
        }

        public Criteria andReplyidEqualTo(Long value) {
            addCriterion("replyid =", value, "replyid");
            return (Criteria) this;
        }

        public Criteria andReplyidNotEqualTo(Long value) {
            addCriterion("replyid <>", value, "replyid");
            return (Criteria) this;
        }

        public Criteria andReplyidGreaterThan(Long value) {
            addCriterion("replyid >", value, "replyid");
            return (Criteria) this;
        }

        public Criteria andReplyidGreaterThanOrEqualTo(Long value) {
            addCriterion("replyid >=", value, "replyid");
            return (Criteria) this;
        }

        public Criteria andReplyidLessThan(Long value) {
            addCriterion("replyid <", value, "replyid");
            return (Criteria) this;
        }

        public Criteria andReplyidLessThanOrEqualTo(Long value) {
            addCriterion("replyid <=", value, "replyid");
            return (Criteria) this;
        }

        public Criteria andReplyidIn(List<Long> values) {
            addCriterion("replyid in", values, "replyid");
            return (Criteria) this;
        }

        public Criteria andReplyidNotIn(List<Long> values) {
            addCriterion("replyid not in", values, "replyid");
            return (Criteria) this;
        }

        public Criteria andReplyidBetween(Long value1, Long value2) {
            addCriterion("replyid between", value1, value2, "replyid");
            return (Criteria) this;
        }

        public Criteria andReplyidNotBetween(Long value1, Long value2) {
            addCriterion("replyid not between", value1, value2, "replyid");
            return (Criteria) this;
        }

        public Criteria andQuestidIsNull() {
            addCriterion("questid is null");
            return (Criteria) this;
        }

        public Criteria andQuestidIsNotNull() {
            addCriterion("questid is not null");
            return (Criteria) this;
        }

        public Criteria andQuestidEqualTo(Long value) {
            addCriterion("questid =", value, "questid");
            return (Criteria) this;
        }

        public Criteria andQuestidNotEqualTo(Long value) {
            addCriterion("questid <>", value, "questid");
            return (Criteria) this;
        }

        public Criteria andQuestidGreaterThan(Long value) {
            addCriterion("questid >", value, "questid");
            return (Criteria) this;
        }

        public Criteria andQuestidGreaterThanOrEqualTo(Long value) {
            addCriterion("questid >=", value, "questid");
            return (Criteria) this;
        }

        public Criteria andQuestidLessThan(Long value) {
            addCriterion("questid <", value, "questid");
            return (Criteria) this;
        }

        public Criteria andQuestidLessThanOrEqualTo(Long value) {
            addCriterion("questid <=", value, "questid");
            return (Criteria) this;
        }

        public Criteria andQuestidIn(List<Long> values) {
            addCriterion("questid in", values, "questid");
            return (Criteria) this;
        }

        public Criteria andQuestidNotIn(List<Long> values) {
            addCriterion("questid not in", values, "questid");
            return (Criteria) this;
        }

        public Criteria andQuestidBetween(Long value1, Long value2) {
            addCriterion("questid between", value1, value2, "questid");
            return (Criteria) this;
        }

        public Criteria andQuestidNotBetween(Long value1, Long value2) {
            addCriterion("questid not between", value1, value2, "questid");
            return (Criteria) this;
        }

        public Criteria andTimeIsNull() {
            addCriterion("time is null");
            return (Criteria) this;
        }

        public Criteria andTimeIsNotNull() {
            addCriterion("time is not null");
            return (Criteria) this;
        }

        public Criteria andTimeEqualTo(Date value) {
            addCriterion("time =", value, "time");
            return (Criteria) this;
        }

        public Criteria andTimeNotEqualTo(Date value) {
            addCriterion("time <>", value, "time");
            return (Criteria) this;
        }

        public Criteria andTimeGreaterThan(Date value) {
            addCriterion("time >", value, "time");
            return (Criteria) this;
        }

        public Criteria andTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("time >=", value, "time");
            return (Criteria) this;
        }

        public Criteria andTimeLessThan(Date value) {
            addCriterion("time <", value, "time");
            return (Criteria) this;
        }

        public Criteria andTimeLessThanOrEqualTo(Date value) {
            addCriterion("time <=", value, "time");
            return (Criteria) this;
        }

        public Criteria andTimeIn(List<Date> values) {
            addCriterion("time in", values, "time");
            return (Criteria) this;
        }

        public Criteria andTimeNotIn(List<Date> values) {
            addCriterion("time not in", values, "time");
            return (Criteria) this;
        }

        public Criteria andTimeBetween(Date value1, Date value2) {
            addCriterion("time between", value1, value2, "time");
            return (Criteria) this;
        }

        public Criteria andTimeNotBetween(Date value1, Date value2) {
            addCriterion("time not between", value1, value2, "time");
            return (Criteria) this;
        }

        public Criteria andImageIsNull() {
            addCriterion("image is null");
            return (Criteria) this;
        }

        public Criteria andImageIsNotNull() {
            addCriterion("image is not null");
            return (Criteria) this;
        }

        public Criteria andImageEqualTo(String value) {
            addCriterion("image =", value, "image");
            return (Criteria) this;
        }

        public Criteria andImageNotEqualTo(String value) {
            addCriterion("image <>", value, "image");
            return (Criteria) this;
        }

        public Criteria andImageGreaterThan(String value) {
            addCriterion("image >", value, "image");
            return (Criteria) this;
        }

        public Criteria andImageGreaterThanOrEqualTo(String value) {
            addCriterion("image >=", value, "image");
            return (Criteria) this;
        }

        public Criteria andImageLessThan(String value) {
            addCriterion("image <", value, "image");
            return (Criteria) this;
        }

        public Criteria andImageLessThanOrEqualTo(String value) {
            addCriterion("image <=", value, "image");
            return (Criteria) this;
        }

        public Criteria andImageLike(String value) {
            addCriterion("image like", value, "image");
            return (Criteria) this;
        }

        public Criteria andImageNotLike(String value) {
            addCriterion("image not like", value, "image");
            return (Criteria) this;
        }

        public Criteria andImageIn(List<String> values) {
            addCriterion("image in", values, "image");
            return (Criteria) this;
        }

        public Criteria andImageNotIn(List<String> values) {
            addCriterion("image not in", values, "image");
            return (Criteria) this;
        }

        public Criteria andImageBetween(String value1, String value2) {
            addCriterion("image between", value1, value2, "image");
            return (Criteria) this;
        }

        public Criteria andImageNotBetween(String value1, String value2) {
            addCriterion("image not between", value1, value2, "image");
            return (Criteria) this;
        }

        public Criteria andLiketimeIsNull() {
            addCriterion("liketime is null");
            return (Criteria) this;
        }

        public Criteria andLiketimeIsNotNull() {
            addCriterion("liketime is not null");
            return (Criteria) this;
        }

        public Criteria andLiketimeEqualTo(Integer value) {
            addCriterion("liketime =", value, "liketime");
            return (Criteria) this;
        }

        public Criteria andLiketimeNotEqualTo(Integer value) {
            addCriterion("liketime <>", value, "liketime");
            return (Criteria) this;
        }

        public Criteria andLiketimeGreaterThan(Integer value) {
            addCriterion("liketime >", value, "liketime");
            return (Criteria) this;
        }

        public Criteria andLiketimeGreaterThanOrEqualTo(Integer value) {
            addCriterion("liketime >=", value, "liketime");
            return (Criteria) this;
        }

        public Criteria andLiketimeLessThan(Integer value) {
            addCriterion("liketime <", value, "liketime");
            return (Criteria) this;
        }

        public Criteria andLiketimeLessThanOrEqualTo(Integer value) {
            addCriterion("liketime <=", value, "liketime");
            return (Criteria) this;
        }

        public Criteria andLiketimeIn(List<Integer> values) {
            addCriterion("liketime in", values, "liketime");
            return (Criteria) this;
        }

        public Criteria andLiketimeNotIn(List<Integer> values) {
            addCriterion("liketime not in", values, "liketime");
            return (Criteria) this;
        }

        public Criteria andLiketimeBetween(Integer value1, Integer value2) {
            addCriterion("liketime between", value1, value2, "liketime");
            return (Criteria) this;
        }

        public Criteria andLiketimeNotBetween(Integer value1, Integer value2) {
            addCriterion("liketime not between", value1, value2, "liketime");
            return (Criteria) this;
        }

        public Criteria andReplyuserIsNull() {
            addCriterion("replyuser is null");
            return (Criteria) this;
        }

        public Criteria andReplyuserIsNotNull() {
            addCriterion("replyuser is not null");
            return (Criteria) this;
        }

        public Criteria andReplyuserEqualTo(Long value) {
            addCriterion("replyuser =", value, "replyuser");
            return (Criteria) this;
        }

        public Criteria andReplyuserNotEqualTo(Long value) {
            addCriterion("replyuser <>", value, "replyuser");
            return (Criteria) this;
        }

        public Criteria andReplyuserGreaterThan(Long value) {
            addCriterion("replyuser >", value, "replyuser");
            return (Criteria) this;
        }

        public Criteria andReplyuserGreaterThanOrEqualTo(Long value) {
            addCriterion("replyuser >=", value, "replyuser");
            return (Criteria) this;
        }

        public Criteria andReplyuserLessThan(Long value) {
            addCriterion("replyuser <", value, "replyuser");
            return (Criteria) this;
        }

        public Criteria andReplyuserLessThanOrEqualTo(Long value) {
            addCriterion("replyuser <=", value, "replyuser");
            return (Criteria) this;
        }

        public Criteria andReplyuserIn(List<Long> values) {
            addCriterion("replyuser in", values, "replyuser");
            return (Criteria) this;
        }

        public Criteria andReplyuserNotIn(List<Long> values) {
            addCriterion("replyuser not in", values, "replyuser");
            return (Criteria) this;
        }

        public Criteria andReplyuserBetween(Long value1, Long value2) {
            addCriterion("replyuser between", value1, value2, "replyuser");
            return (Criteria) this;
        }

        public Criteria andReplyuserNotBetween(Long value1, Long value2) {
            addCriterion("replyuser not between", value1, value2, "replyuser");
            return (Criteria) this;
        }

        public Criteria andUseridIsNull() {
            addCriterion("userid is null");
            return (Criteria) this;
        }

        public Criteria andUseridIsNotNull() {
            addCriterion("userid is not null");
            return (Criteria) this;
        }

        public Criteria andUseridEqualTo(Integer value) {
            addCriterion("userid =", value, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridNotEqualTo(Integer value) {
            addCriterion("userid <>", value, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridGreaterThan(Integer value) {
            addCriterion("userid >", value, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridGreaterThanOrEqualTo(Integer value) {
            addCriterion("userid >=", value, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridLessThan(Integer value) {
            addCriterion("userid <", value, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridLessThanOrEqualTo(Integer value) {
            addCriterion("userid <=", value, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridIn(List<Integer> values) {
            addCriterion("userid in", values, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridNotIn(List<Integer> values) {
            addCriterion("userid not in", values, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridBetween(Integer value1, Integer value2) {
            addCriterion("userid between", value1, value2, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridNotBetween(Integer value1, Integer value2) {
            addCriterion("userid not between", value1, value2, "userid");
            return (Criteria) this;
        }

        public Criteria andIs_deleteIsNull() {
            addCriterion("is_delete is null");
            return (Criteria) this;
        }

        public Criteria andIs_deleteIsNotNull() {
            addCriterion("is_delete is not null");
            return (Criteria) this;
        }

        public Criteria andIs_deleteEqualTo(Byte value) {
            addCriterion("is_delete =", value, "is_delete");
            return (Criteria) this;
        }

        public Criteria andIs_deleteNotEqualTo(Byte value) {
            addCriterion("is_delete <>", value, "is_delete");
            return (Criteria) this;
        }

        public Criteria andIs_deleteGreaterThan(Byte value) {
            addCriterion("is_delete >", value, "is_delete");
            return (Criteria) this;
        }

        public Criteria andIs_deleteGreaterThanOrEqualTo(Byte value) {
            addCriterion("is_delete >=", value, "is_delete");
            return (Criteria) this;
        }

        public Criteria andIs_deleteLessThan(Byte value) {
            addCriterion("is_delete <", value, "is_delete");
            return (Criteria) this;
        }

        public Criteria andIs_deleteLessThanOrEqualTo(Byte value) {
            addCriterion("is_delete <=", value, "is_delete");
            return (Criteria) this;
        }

        public Criteria andIs_deleteIn(List<Byte> values) {
            addCriterion("is_delete in", values, "is_delete");
            return (Criteria) this;
        }

        public Criteria andIs_deleteNotIn(List<Byte> values) {
            addCriterion("is_delete not in", values, "is_delete");
            return (Criteria) this;
        }

        public Criteria andIs_deleteBetween(Byte value1, Byte value2) {
            addCriterion("is_delete between", value1, value2, "is_delete");
            return (Criteria) this;
        }

        public Criteria andIs_deleteNotBetween(Byte value1, Byte value2) {
            addCriterion("is_delete not between", value1, value2, "is_delete");
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