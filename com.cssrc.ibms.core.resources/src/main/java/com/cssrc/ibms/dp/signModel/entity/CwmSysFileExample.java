package com.cssrc.ibms.dp.signModel.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CwmSysFileExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public CwmSysFileExample() {
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

        public Criteria andFileidIsNull() {
            addCriterion("FILEID is null");
            return (Criteria) this;
        }

        public Criteria andFileidIsNotNull() {
            addCriterion("FILEID is not null");
            return (Criteria) this;
        }

        public Criteria andFileidEqualTo(Long value) {
            addCriterion("FILEID =", value, "fileid");
            return (Criteria) this;
        }

        public Criteria andFileidNotEqualTo(Long value) {
            addCriterion("FILEID <>", value, "fileid");
            return (Criteria) this;
        }

        public Criteria andFileidGreaterThan(Long value) {
            addCriterion("FILEID >", value, "fileid");
            return (Criteria) this;
        }

        public Criteria andFileidGreaterThanOrEqualTo(Long value) {
            addCriterion("FILEID >=", value, "fileid");
            return (Criteria) this;
        }

        public Criteria andFileidLessThan(Long value) {
            addCriterion("FILEID <", value, "fileid");
            return (Criteria) this;
        }

        public Criteria andFileidLessThanOrEqualTo(Long value) {
            addCriterion("FILEID <=", value, "fileid");
            return (Criteria) this;
        }

        public Criteria andFileidIn(List<Long> values) {
            addCriterion("FILEID in", values, "fileid");
            return (Criteria) this;
        }

        public Criteria andFileidNotIn(List<Long> values) {
            addCriterion("FILEID not in", values, "fileid");
            return (Criteria) this;
        }

        public Criteria andFileidBetween(Long value1, Long value2) {
            addCriterion("FILEID between", value1, value2, "fileid");
            return (Criteria) this;
        }

        public Criteria andFileidNotBetween(Long value1, Long value2) {
            addCriterion("FILEID not between", value1, value2, "fileid");
            return (Criteria) this;
        }

        public Criteria andFilenameIsNull() {
            addCriterion("FILENAME is null");
            return (Criteria) this;
        }

        public Criteria andFilenameIsNotNull() {
            addCriterion("FILENAME is not null");
            return (Criteria) this;
        }

        public Criteria andFilenameEqualTo(String value) {
            addCriterion("FILENAME =", value, "filename");
            return (Criteria) this;
        }

        public Criteria andFilenameNotEqualTo(String value) {
            addCriterion("FILENAME <>", value, "filename");
            return (Criteria) this;
        }

        public Criteria andFilenameGreaterThan(String value) {
            addCriterion("FILENAME >", value, "filename");
            return (Criteria) this;
        }

        public Criteria andFilenameGreaterThanOrEqualTo(String value) {
            addCriterion("FILENAME >=", value, "filename");
            return (Criteria) this;
        }

        public Criteria andFilenameLessThan(String value) {
            addCriterion("FILENAME <", value, "filename");
            return (Criteria) this;
        }

        public Criteria andFilenameLessThanOrEqualTo(String value) {
            addCriterion("FILENAME <=", value, "filename");
            return (Criteria) this;
        }

        public Criteria andFilenameLike(String value) {
            addCriterion("FILENAME like", value, "filename");
            return (Criteria) this;
        }

        public Criteria andFilenameNotLike(String value) {
            addCriterion("FILENAME not like", value, "filename");
            return (Criteria) this;
        }

        public Criteria andFilenameIn(List<String> values) {
            addCriterion("FILENAME in", values, "filename");
            return (Criteria) this;
        }

        public Criteria andFilenameNotIn(List<String> values) {
            addCriterion("FILENAME not in", values, "filename");
            return (Criteria) this;
        }

        public Criteria andFilenameBetween(String value1, String value2) {
            addCriterion("FILENAME between", value1, value2, "filename");
            return (Criteria) this;
        }

        public Criteria andFilenameNotBetween(String value1, String value2) {
            addCriterion("FILENAME not between", value1, value2, "filename");
            return (Criteria) this;
        }

        public Criteria andFilepathIsNull() {
            addCriterion("FILEPATH is null");
            return (Criteria) this;
        }

        public Criteria andFilepathIsNotNull() {
            addCriterion("FILEPATH is not null");
            return (Criteria) this;
        }

        public Criteria andFilepathEqualTo(String value) {
            addCriterion("FILEPATH =", value, "filepath");
            return (Criteria) this;
        }

        public Criteria andFilepathNotEqualTo(String value) {
            addCriterion("FILEPATH <>", value, "filepath");
            return (Criteria) this;
        }

        public Criteria andFilepathGreaterThan(String value) {
            addCriterion("FILEPATH >", value, "filepath");
            return (Criteria) this;
        }

        public Criteria andFilepathGreaterThanOrEqualTo(String value) {
            addCriterion("FILEPATH >=", value, "filepath");
            return (Criteria) this;
        }

        public Criteria andFilepathLessThan(String value) {
            addCriterion("FILEPATH <", value, "filepath");
            return (Criteria) this;
        }

        public Criteria andFilepathLessThanOrEqualTo(String value) {
            addCriterion("FILEPATH <=", value, "filepath");
            return (Criteria) this;
        }

        public Criteria andFilepathLike(String value) {
            addCriterion("FILEPATH like", value, "filepath");
            return (Criteria) this;
        }

        public Criteria andFilepathNotLike(String value) {
            addCriterion("FILEPATH not like", value, "filepath");
            return (Criteria) this;
        }

        public Criteria andFilepathIn(List<String> values) {
            addCriterion("FILEPATH in", values, "filepath");
            return (Criteria) this;
        }

        public Criteria andFilepathNotIn(List<String> values) {
            addCriterion("FILEPATH not in", values, "filepath");
            return (Criteria) this;
        }

        public Criteria andFilepathBetween(String value1, String value2) {
            addCriterion("FILEPATH between", value1, value2, "filepath");
            return (Criteria) this;
        }

        public Criteria andFilepathNotBetween(String value1, String value2) {
            addCriterion("FILEPATH not between", value1, value2, "filepath");
            return (Criteria) this;
        }

        public Criteria andCreatetimeIsNull() {
            addCriterion("CREATETIME is null");
            return (Criteria) this;
        }

        public Criteria andCreatetimeIsNotNull() {
            addCriterion("CREATETIME is not null");
            return (Criteria) this;
        }

        public Criteria andCreatetimeEqualTo(Date value) {
            addCriterion("CREATETIME =", value, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeNotEqualTo(Date value) {
            addCriterion("CREATETIME <>", value, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeGreaterThan(Date value) {
            addCriterion("CREATETIME >", value, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeGreaterThanOrEqualTo(Date value) {
            addCriterion("CREATETIME >=", value, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeLessThan(Date value) {
            addCriterion("CREATETIME <", value, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeLessThanOrEqualTo(Date value) {
            addCriterion("CREATETIME <=", value, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeIn(List<Date> values) {
            addCriterion("CREATETIME in", values, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeNotIn(List<Date> values) {
            addCriterion("CREATETIME not in", values, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeBetween(Date value1, Date value2) {
            addCriterion("CREATETIME between", value1, value2, "createtime");
            return (Criteria) this;
        }

        public Criteria andCreatetimeNotBetween(Date value1, Date value2) {
            addCriterion("CREATETIME not between", value1, value2, "createtime");
            return (Criteria) this;
        }

        public Criteria andExtIsNull() {
            addCriterion("EXT is null");
            return (Criteria) this;
        }

        public Criteria andExtIsNotNull() {
            addCriterion("EXT is not null");
            return (Criteria) this;
        }

        public Criteria andExtEqualTo(String value) {
            addCriterion("EXT =", value, "ext");
            return (Criteria) this;
        }

        public Criteria andExtNotEqualTo(String value) {
            addCriterion("EXT <>", value, "ext");
            return (Criteria) this;
        }

        public Criteria andExtGreaterThan(String value) {
            addCriterion("EXT >", value, "ext");
            return (Criteria) this;
        }

        public Criteria andExtGreaterThanOrEqualTo(String value) {
            addCriterion("EXT >=", value, "ext");
            return (Criteria) this;
        }

        public Criteria andExtLessThan(String value) {
            addCriterion("EXT <", value, "ext");
            return (Criteria) this;
        }

        public Criteria andExtLessThanOrEqualTo(String value) {
            addCriterion("EXT <=", value, "ext");
            return (Criteria) this;
        }

        public Criteria andExtLike(String value) {
            addCriterion("EXT like", value, "ext");
            return (Criteria) this;
        }

        public Criteria andExtNotLike(String value) {
            addCriterion("EXT not like", value, "ext");
            return (Criteria) this;
        }

        public Criteria andExtIn(List<String> values) {
            addCriterion("EXT in", values, "ext");
            return (Criteria) this;
        }

        public Criteria andExtNotIn(List<String> values) {
            addCriterion("EXT not in", values, "ext");
            return (Criteria) this;
        }

        public Criteria andExtBetween(String value1, String value2) {
            addCriterion("EXT between", value1, value2, "ext");
            return (Criteria) this;
        }

        public Criteria andExtNotBetween(String value1, String value2) {
            addCriterion("EXT not between", value1, value2, "ext");
            return (Criteria) this;
        }

        public Criteria andFiletypeIsNull() {
            addCriterion("FILETYPE is null");
            return (Criteria) this;
        }

        public Criteria andFiletypeIsNotNull() {
            addCriterion("FILETYPE is not null");
            return (Criteria) this;
        }

        public Criteria andFiletypeEqualTo(String value) {
            addCriterion("FILETYPE =", value, "filetype");
            return (Criteria) this;
        }

        public Criteria andFiletypeNotEqualTo(String value) {
            addCriterion("FILETYPE <>", value, "filetype");
            return (Criteria) this;
        }

        public Criteria andFiletypeGreaterThan(String value) {
            addCriterion("FILETYPE >", value, "filetype");
            return (Criteria) this;
        }

        public Criteria andFiletypeGreaterThanOrEqualTo(String value) {
            addCriterion("FILETYPE >=", value, "filetype");
            return (Criteria) this;
        }

        public Criteria andFiletypeLessThan(String value) {
            addCriterion("FILETYPE <", value, "filetype");
            return (Criteria) this;
        }

        public Criteria andFiletypeLessThanOrEqualTo(String value) {
            addCriterion("FILETYPE <=", value, "filetype");
            return (Criteria) this;
        }

        public Criteria andFiletypeLike(String value) {
            addCriterion("FILETYPE like", value, "filetype");
            return (Criteria) this;
        }

        public Criteria andFiletypeNotLike(String value) {
            addCriterion("FILETYPE not like", value, "filetype");
            return (Criteria) this;
        }

        public Criteria andFiletypeIn(List<String> values) {
            addCriterion("FILETYPE in", values, "filetype");
            return (Criteria) this;
        }

        public Criteria andFiletypeNotIn(List<String> values) {
            addCriterion("FILETYPE not in", values, "filetype");
            return (Criteria) this;
        }

        public Criteria andFiletypeBetween(String value1, String value2) {
            addCriterion("FILETYPE between", value1, value2, "filetype");
            return (Criteria) this;
        }

        public Criteria andFiletypeNotBetween(String value1, String value2) {
            addCriterion("FILETYPE not between", value1, value2, "filetype");
            return (Criteria) this;
        }

        public Criteria andNoteIsNull() {
            addCriterion("NOTE is null");
            return (Criteria) this;
        }

        public Criteria andNoteIsNotNull() {
            addCriterion("NOTE is not null");
            return (Criteria) this;
        }

        public Criteria andNoteEqualTo(String value) {
            addCriterion("NOTE =", value, "note");
            return (Criteria) this;
        }

        public Criteria andNoteNotEqualTo(String value) {
            addCriterion("NOTE <>", value, "note");
            return (Criteria) this;
        }

        public Criteria andNoteGreaterThan(String value) {
            addCriterion("NOTE >", value, "note");
            return (Criteria) this;
        }

        public Criteria andNoteGreaterThanOrEqualTo(String value) {
            addCriterion("NOTE >=", value, "note");
            return (Criteria) this;
        }

        public Criteria andNoteLessThan(String value) {
            addCriterion("NOTE <", value, "note");
            return (Criteria) this;
        }

        public Criteria andNoteLessThanOrEqualTo(String value) {
            addCriterion("NOTE <=", value, "note");
            return (Criteria) this;
        }

        public Criteria andNoteLike(String value) {
            addCriterion("NOTE like", value, "note");
            return (Criteria) this;
        }

        public Criteria andNoteNotLike(String value) {
            addCriterion("NOTE not like", value, "note");
            return (Criteria) this;
        }

        public Criteria andNoteIn(List<String> values) {
            addCriterion("NOTE in", values, "note");
            return (Criteria) this;
        }

        public Criteria andNoteNotIn(List<String> values) {
            addCriterion("NOTE not in", values, "note");
            return (Criteria) this;
        }

        public Criteria andNoteBetween(String value1, String value2) {
            addCriterion("NOTE between", value1, value2, "note");
            return (Criteria) this;
        }

        public Criteria andNoteNotBetween(String value1, String value2) {
            addCriterion("NOTE not between", value1, value2, "note");
            return (Criteria) this;
        }

        public Criteria andCreatoridIsNull() {
            addCriterion("CREATORID is null");
            return (Criteria) this;
        }

        public Criteria andCreatoridIsNotNull() {
            addCriterion("CREATORID is not null");
            return (Criteria) this;
        }

        public Criteria andCreatoridEqualTo(Long value) {
            addCriterion("CREATORID =", value, "creatorid");
            return (Criteria) this;
        }

        public Criteria andCreatoridNotEqualTo(Long value) {
            addCriterion("CREATORID <>", value, "creatorid");
            return (Criteria) this;
        }

        public Criteria andCreatoridGreaterThan(Long value) {
            addCriterion("CREATORID >", value, "creatorid");
            return (Criteria) this;
        }

        public Criteria andCreatoridGreaterThanOrEqualTo(Long value) {
            addCriterion("CREATORID >=", value, "creatorid");
            return (Criteria) this;
        }

        public Criteria andCreatoridLessThan(Long value) {
            addCriterion("CREATORID <", value, "creatorid");
            return (Criteria) this;
        }

        public Criteria andCreatoridLessThanOrEqualTo(Long value) {
            addCriterion("CREATORID <=", value, "creatorid");
            return (Criteria) this;
        }

        public Criteria andCreatoridIn(List<Long> values) {
            addCriterion("CREATORID in", values, "creatorid");
            return (Criteria) this;
        }

        public Criteria andCreatoridNotIn(List<Long> values) {
            addCriterion("CREATORID not in", values, "creatorid");
            return (Criteria) this;
        }

        public Criteria andCreatoridBetween(Long value1, Long value2) {
            addCriterion("CREATORID between", value1, value2, "creatorid");
            return (Criteria) this;
        }

        public Criteria andCreatoridNotBetween(Long value1, Long value2) {
            addCriterion("CREATORID not between", value1, value2, "creatorid");
            return (Criteria) this;
        }

        public Criteria andCreatorIsNull() {
            addCriterion("CREATOR is null");
            return (Criteria) this;
        }

        public Criteria andCreatorIsNotNull() {
            addCriterion("CREATOR is not null");
            return (Criteria) this;
        }

        public Criteria andCreatorEqualTo(String value) {
            addCriterion("CREATOR =", value, "creator");
            return (Criteria) this;
        }

        public Criteria andCreatorNotEqualTo(String value) {
            addCriterion("CREATOR <>", value, "creator");
            return (Criteria) this;
        }

        public Criteria andCreatorGreaterThan(String value) {
            addCriterion("CREATOR >", value, "creator");
            return (Criteria) this;
        }

        public Criteria andCreatorGreaterThanOrEqualTo(String value) {
            addCriterion("CREATOR >=", value, "creator");
            return (Criteria) this;
        }

        public Criteria andCreatorLessThan(String value) {
            addCriterion("CREATOR <", value, "creator");
            return (Criteria) this;
        }

        public Criteria andCreatorLessThanOrEqualTo(String value) {
            addCriterion("CREATOR <=", value, "creator");
            return (Criteria) this;
        }

        public Criteria andCreatorLike(String value) {
            addCriterion("CREATOR like", value, "creator");
            return (Criteria) this;
        }

        public Criteria andCreatorNotLike(String value) {
            addCriterion("CREATOR not like", value, "creator");
            return (Criteria) this;
        }

        public Criteria andCreatorIn(List<String> values) {
            addCriterion("CREATOR in", values, "creator");
            return (Criteria) this;
        }

        public Criteria andCreatorNotIn(List<String> values) {
            addCriterion("CREATOR not in", values, "creator");
            return (Criteria) this;
        }

        public Criteria andCreatorBetween(String value1, String value2) {
            addCriterion("CREATOR between", value1, value2, "creator");
            return (Criteria) this;
        }

        public Criteria andCreatorNotBetween(String value1, String value2) {
            addCriterion("CREATOR not between", value1, value2, "creator");
            return (Criteria) this;
        }

        public Criteria andTotalbytesIsNull() {
            addCriterion("TOTALBYTES is null");
            return (Criteria) this;
        }

        public Criteria andTotalbytesIsNotNull() {
            addCriterion("TOTALBYTES is not null");
            return (Criteria) this;
        }

        public Criteria andTotalbytesEqualTo(Long value) {
            addCriterion("TOTALBYTES =", value, "totalbytes");
            return (Criteria) this;
        }

        public Criteria andTotalbytesNotEqualTo(Long value) {
            addCriterion("TOTALBYTES <>", value, "totalbytes");
            return (Criteria) this;
        }

        public Criteria andTotalbytesGreaterThan(Long value) {
            addCriterion("TOTALBYTES >", value, "totalbytes");
            return (Criteria) this;
        }

        public Criteria andTotalbytesGreaterThanOrEqualTo(Long value) {
            addCriterion("TOTALBYTES >=", value, "totalbytes");
            return (Criteria) this;
        }

        public Criteria andTotalbytesLessThan(Long value) {
            addCriterion("TOTALBYTES <", value, "totalbytes");
            return (Criteria) this;
        }

        public Criteria andTotalbytesLessThanOrEqualTo(Long value) {
            addCriterion("TOTALBYTES <=", value, "totalbytes");
            return (Criteria) this;
        }

        public Criteria andTotalbytesIn(List<Long> values) {
            addCriterion("TOTALBYTES in", values, "totalbytes");
            return (Criteria) this;
        }

        public Criteria andTotalbytesNotIn(List<Long> values) {
            addCriterion("TOTALBYTES not in", values, "totalbytes");
            return (Criteria) this;
        }

        public Criteria andTotalbytesBetween(Long value1, Long value2) {
            addCriterion("TOTALBYTES between", value1, value2, "totalbytes");
            return (Criteria) this;
        }

        public Criteria andTotalbytesNotBetween(Long value1, Long value2) {
            addCriterion("TOTALBYTES not between", value1, value2, "totalbytes");
            return (Criteria) this;
        }

        public Criteria andDelflagIsNull() {
            addCriterion("DELFLAG is null");
            return (Criteria) this;
        }

        public Criteria andDelflagIsNotNull() {
            addCriterion("DELFLAG is not null");
            return (Criteria) this;
        }

        public Criteria andDelflagEqualTo(BigDecimal value) {
            addCriterion("DELFLAG =", value, "delflag");
            return (Criteria) this;
        }

        public Criteria andDelflagNotEqualTo(BigDecimal value) {
            addCriterion("DELFLAG <>", value, "delflag");
            return (Criteria) this;
        }

        public Criteria andDelflagGreaterThan(BigDecimal value) {
            addCriterion("DELFLAG >", value, "delflag");
            return (Criteria) this;
        }

        public Criteria andDelflagGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("DELFLAG >=", value, "delflag");
            return (Criteria) this;
        }

        public Criteria andDelflagLessThan(BigDecimal value) {
            addCriterion("DELFLAG <", value, "delflag");
            return (Criteria) this;
        }

        public Criteria andDelflagLessThanOrEqualTo(BigDecimal value) {
            addCriterion("DELFLAG <=", value, "delflag");
            return (Criteria) this;
        }

        public Criteria andDelflagIn(List<BigDecimal> values) {
            addCriterion("DELFLAG in", values, "delflag");
            return (Criteria) this;
        }

        public Criteria andDelflagNotIn(List<BigDecimal> values) {
            addCriterion("DELFLAG not in", values, "delflag");
            return (Criteria) this;
        }

        public Criteria andDelflagBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("DELFLAG between", value1, value2, "delflag");
            return (Criteria) this;
        }

        public Criteria andDelflagNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("DELFLAG not between", value1, value2, "delflag");
            return (Criteria) this;
        }

        public Criteria andProtypeidIsNull() {
            addCriterion("PROTYPEID is null");
            return (Criteria) this;
        }

        public Criteria andProtypeidIsNotNull() {
            addCriterion("PROTYPEID is not null");
            return (Criteria) this;
        }

        public Criteria andProtypeidEqualTo(Long value) {
            addCriterion("PROTYPEID =", value, "protypeid");
            return (Criteria) this;
        }

        public Criteria andProtypeidNotEqualTo(Long value) {
            addCriterion("PROTYPEID <>", value, "protypeid");
            return (Criteria) this;
        }

        public Criteria andProtypeidGreaterThan(Long value) {
            addCriterion("PROTYPEID >", value, "protypeid");
            return (Criteria) this;
        }

        public Criteria andProtypeidGreaterThanOrEqualTo(Long value) {
            addCriterion("PROTYPEID >=", value, "protypeid");
            return (Criteria) this;
        }

        public Criteria andProtypeidLessThan(Long value) {
            addCriterion("PROTYPEID <", value, "protypeid");
            return (Criteria) this;
        }

        public Criteria andProtypeidLessThanOrEqualTo(Long value) {
            addCriterion("PROTYPEID <=", value, "protypeid");
            return (Criteria) this;
        }

        public Criteria andProtypeidIn(List<Long> values) {
            addCriterion("PROTYPEID in", values, "protypeid");
            return (Criteria) this;
        }

        public Criteria andProtypeidNotIn(List<Long> values) {
            addCriterion("PROTYPEID not in", values, "protypeid");
            return (Criteria) this;
        }

        public Criteria andProtypeidBetween(Long value1, Long value2) {
            addCriterion("PROTYPEID between", value1, value2, "protypeid");
            return (Criteria) this;
        }

        public Criteria andProtypeidNotBetween(Long value1, Long value2) {
            addCriterion("PROTYPEID not between", value1, value2, "protypeid");
            return (Criteria) this;
        }

        public Criteria andTableidIsNull() {
            addCriterion("TABLEID is null");
            return (Criteria) this;
        }

        public Criteria andTableidIsNotNull() {
            addCriterion("TABLEID is not null");
            return (Criteria) this;
        }

        public Criteria andTableidEqualTo(String value) {
            addCriterion("TABLEID =", value, "tableid");
            return (Criteria) this;
        }

        public Criteria andTableidNotEqualTo(String value) {
            addCriterion("TABLEID <>", value, "tableid");
            return (Criteria) this;
        }

        public Criteria andTableidGreaterThan(String value) {
            addCriterion("TABLEID >", value, "tableid");
            return (Criteria) this;
        }

        public Criteria andTableidGreaterThanOrEqualTo(String value) {
            addCriterion("TABLEID >=", value, "tableid");
            return (Criteria) this;
        }

        public Criteria andTableidLessThan(String value) {
            addCriterion("TABLEID <", value, "tableid");
            return (Criteria) this;
        }

        public Criteria andTableidLessThanOrEqualTo(String value) {
            addCriterion("TABLEID <=", value, "tableid");
            return (Criteria) this;
        }

        public Criteria andTableidLike(String value) {
            addCriterion("TABLEID like", value, "tableid");
            return (Criteria) this;
        }

        public Criteria andTableidNotLike(String value) {
            addCriterion("TABLEID not like", value, "tableid");
            return (Criteria) this;
        }

        public Criteria andTableidIn(List<String> values) {
            addCriterion("TABLEID in", values, "tableid");
            return (Criteria) this;
        }

        public Criteria andTableidNotIn(List<String> values) {
            addCriterion("TABLEID not in", values, "tableid");
            return (Criteria) this;
        }

        public Criteria andTableidBetween(String value1, String value2) {
            addCriterion("TABLEID between", value1, value2, "tableid");
            return (Criteria) this;
        }

        public Criteria andTableidNotBetween(String value1, String value2) {
            addCriterion("TABLEID not between", value1, value2, "tableid");
            return (Criteria) this;
        }

        public Criteria andDataidIsNull() {
            addCriterion("DATAID is null");
            return (Criteria) this;
        }

        public Criteria andDataidIsNotNull() {
            addCriterion("DATAID is not null");
            return (Criteria) this;
        }

        public Criteria andDataidEqualTo(String value) {
            addCriterion("DATAID =", value, "dataid");
            return (Criteria) this;
        }

        public Criteria andDataidNotEqualTo(String value) {
            addCriterion("DATAID <>", value, "dataid");
            return (Criteria) this;
        }

        public Criteria andDataidGreaterThan(String value) {
            addCriterion("DATAID >", value, "dataid");
            return (Criteria) this;
        }

        public Criteria andDataidGreaterThanOrEqualTo(String value) {
            addCriterion("DATAID >=", value, "dataid");
            return (Criteria) this;
        }

        public Criteria andDataidLessThan(String value) {
            addCriterion("DATAID <", value, "dataid");
            return (Criteria) this;
        }

        public Criteria andDataidLessThanOrEqualTo(String value) {
            addCriterion("DATAID <=", value, "dataid");
            return (Criteria) this;
        }

        public Criteria andDataidLike(String value) {
            addCriterion("DATAID like", value, "dataid");
            return (Criteria) this;
        }

        public Criteria andDataidNotLike(String value) {
            addCriterion("DATAID not like", value, "dataid");
            return (Criteria) this;
        }

        public Criteria andDataidIn(List<String> values) {
            addCriterion("DATAID in", values, "dataid");
            return (Criteria) this;
        }

        public Criteria andDataidNotIn(List<String> values) {
            addCriterion("DATAID not in", values, "dataid");
            return (Criteria) this;
        }

        public Criteria andDataidBetween(String value1, String value2) {
            addCriterion("DATAID between", value1, value2, "dataid");
            return (Criteria) this;
        }

        public Criteria andDataidNotBetween(String value1, String value2) {
            addCriterion("DATAID not between", value1, value2, "dataid");
            return (Criteria) this;
        }

        public Criteria andSharedIsNull() {
            addCriterion("SHARED is null");
            return (Criteria) this;
        }

        public Criteria andSharedIsNotNull() {
            addCriterion("SHARED is not null");
            return (Criteria) this;
        }

        public Criteria andSharedEqualTo(Short value) {
            addCriterion("SHARED =", value, "shared");
            return (Criteria) this;
        }

        public Criteria andSharedNotEqualTo(Short value) {
            addCriterion("SHARED <>", value, "shared");
            return (Criteria) this;
        }

        public Criteria andSharedGreaterThan(Short value) {
            addCriterion("SHARED >", value, "shared");
            return (Criteria) this;
        }

        public Criteria andSharedGreaterThanOrEqualTo(Short value) {
            addCriterion("SHARED >=", value, "shared");
            return (Criteria) this;
        }

        public Criteria andSharedLessThan(Short value) {
            addCriterion("SHARED <", value, "shared");
            return (Criteria) this;
        }

        public Criteria andSharedLessThanOrEqualTo(Short value) {
            addCriterion("SHARED <=", value, "shared");
            return (Criteria) this;
        }

        public Criteria andSharedIn(List<Short> values) {
            addCriterion("SHARED in", values, "shared");
            return (Criteria) this;
        }

        public Criteria andSharedNotIn(List<Short> values) {
            addCriterion("SHARED not in", values, "shared");
            return (Criteria) this;
        }

        public Criteria andSharedBetween(Short value1, Short value2) {
            addCriterion("SHARED between", value1, value2, "shared");
            return (Criteria) this;
        }

        public Criteria andSharedNotBetween(Short value1, Short value2) {
            addCriterion("SHARED not between", value1, value2, "shared");
            return (Criteria) this;
        }

        public Criteria andFolderidIsNull() {
            addCriterion("FOLDERID is null");
            return (Criteria) this;
        }

        public Criteria andFolderidIsNotNull() {
            addCriterion("FOLDERID is not null");
            return (Criteria) this;
        }

        public Criteria andFolderidEqualTo(Long value) {
            addCriterion("FOLDERID =", value, "folderid");
            return (Criteria) this;
        }

        public Criteria andFolderidNotEqualTo(Long value) {
            addCriterion("FOLDERID <>", value, "folderid");
            return (Criteria) this;
        }

        public Criteria andFolderidGreaterThan(Long value) {
            addCriterion("FOLDERID >", value, "folderid");
            return (Criteria) this;
        }

        public Criteria andFolderidGreaterThanOrEqualTo(Long value) {
            addCriterion("FOLDERID >=", value, "folderid");
            return (Criteria) this;
        }

        public Criteria andFolderidLessThan(Long value) {
            addCriterion("FOLDERID <", value, "folderid");
            return (Criteria) this;
        }

        public Criteria andFolderidLessThanOrEqualTo(Long value) {
            addCriterion("FOLDERID <=", value, "folderid");
            return (Criteria) this;
        }

        public Criteria andFolderidIn(List<Long> values) {
            addCriterion("FOLDERID in", values, "folderid");
            return (Criteria) this;
        }

        public Criteria andFolderidNotIn(List<Long> values) {
            addCriterion("FOLDERID not in", values, "folderid");
            return (Criteria) this;
        }

        public Criteria andFolderidBetween(Long value1, Long value2) {
            addCriterion("FOLDERID between", value1, value2, "folderid");
            return (Criteria) this;
        }

        public Criteria andFolderidNotBetween(Long value1, Long value2) {
            addCriterion("FOLDERID not between", value1, value2, "folderid");
            return (Criteria) this;
        }

        public Criteria andFileattIsNull() {
            addCriterion("FILEATT is null");
            return (Criteria) this;
        }

        public Criteria andFileattIsNotNull() {
            addCriterion("FILEATT is not null");
            return (Criteria) this;
        }

        public Criteria andFileattEqualTo(Short value) {
            addCriterion("FILEATT =", value, "fileatt");
            return (Criteria) this;
        }

        public Criteria andFileattNotEqualTo(Short value) {
            addCriterion("FILEATT <>", value, "fileatt");
            return (Criteria) this;
        }

        public Criteria andFileattGreaterThan(Short value) {
            addCriterion("FILEATT >", value, "fileatt");
            return (Criteria) this;
        }

        public Criteria andFileattGreaterThanOrEqualTo(Short value) {
            addCriterion("FILEATT >=", value, "fileatt");
            return (Criteria) this;
        }

        public Criteria andFileattLessThan(Short value) {
            addCriterion("FILEATT <", value, "fileatt");
            return (Criteria) this;
        }

        public Criteria andFileattLessThanOrEqualTo(Short value) {
            addCriterion("FILEATT <=", value, "fileatt");
            return (Criteria) this;
        }

        public Criteria andFileattIn(List<Short> values) {
            addCriterion("FILEATT in", values, "fileatt");
            return (Criteria) this;
        }

        public Criteria andFileattNotIn(List<Short> values) {
            addCriterion("FILEATT not in", values, "fileatt");
            return (Criteria) this;
        }

        public Criteria andFileattBetween(Short value1, Short value2) {
            addCriterion("FILEATT between", value1, value2, "fileatt");
            return (Criteria) this;
        }

        public Criteria andFileattNotBetween(Short value1, Short value2) {
            addCriterion("FILEATT not between", value1, value2, "fileatt");
            return (Criteria) this;
        }

        public Criteria andFolderpathIsNull() {
            addCriterion("FOLDERPATH is null");
            return (Criteria) this;
        }

        public Criteria andFolderpathIsNotNull() {
            addCriterion("FOLDERPATH is not null");
            return (Criteria) this;
        }

        public Criteria andFolderpathEqualTo(String value) {
            addCriterion("FOLDERPATH =", value, "folderpath");
            return (Criteria) this;
        }

        public Criteria andFolderpathNotEqualTo(String value) {
            addCriterion("FOLDERPATH <>", value, "folderpath");
            return (Criteria) this;
        }

        public Criteria andFolderpathGreaterThan(String value) {
            addCriterion("FOLDERPATH >", value, "folderpath");
            return (Criteria) this;
        }

        public Criteria andFolderpathGreaterThanOrEqualTo(String value) {
            addCriterion("FOLDERPATH >=", value, "folderpath");
            return (Criteria) this;
        }

        public Criteria andFolderpathLessThan(String value) {
            addCriterion("FOLDERPATH <", value, "folderpath");
            return (Criteria) this;
        }

        public Criteria andFolderpathLessThanOrEqualTo(String value) {
            addCriterion("FOLDERPATH <=", value, "folderpath");
            return (Criteria) this;
        }

        public Criteria andFolderpathLike(String value) {
            addCriterion("FOLDERPATH like", value, "folderpath");
            return (Criteria) this;
        }

        public Criteria andFolderpathNotLike(String value) {
            addCriterion("FOLDERPATH not like", value, "folderpath");
            return (Criteria) this;
        }

        public Criteria andFolderpathIn(List<String> values) {
            addCriterion("FOLDERPATH in", values, "folderpath");
            return (Criteria) this;
        }

        public Criteria andFolderpathNotIn(List<String> values) {
            addCriterion("FOLDERPATH not in", values, "folderpath");
            return (Criteria) this;
        }

        public Criteria andFolderpathBetween(String value1, String value2) {
            addCriterion("FOLDERPATH between", value1, value2, "folderpath");
            return (Criteria) this;
        }

        public Criteria andFolderpathNotBetween(String value1, String value2) {
            addCriterion("FOLDERPATH not between", value1, value2, "folderpath");
            return (Criteria) this;
        }




        public Criteria andSecurityIsNull() {
            addCriterion("SECURITY is null");
            return (Criteria) this;
        }

        public Criteria andSecurityIsNotNull() {
            addCriterion("SECURITY is not null");
            return (Criteria) this;
        }

        public Criteria andSecurityEqualTo(String value) {
            addCriterion("SECURITY =", value, "security");
            return (Criteria) this;
        }

        public Criteria andSecurityNotEqualTo(String value) {
            addCriterion("SECURITY <>", value, "security");
            return (Criteria) this;
        }

        public Criteria andSecurityGreaterThan(String value) {
            addCriterion("SECURITY >", value, "security");
            return (Criteria) this;
        }

        public Criteria andSecurityGreaterThanOrEqualTo(String value) {
            addCriterion("SECURITY >=", value, "security");
            return (Criteria) this;
        }

        public Criteria andSecurityLessThan(String value) {
            addCriterion("SECURITY <", value, "security");
            return (Criteria) this;
        }

        public Criteria andSecurityLessThanOrEqualTo(String value) {
            addCriterion("SECURITY <=", value, "security");
            return (Criteria) this;
        }

        public Criteria andSecurityLike(String value) {
            addCriterion("SECURITY like", value, "security");
            return (Criteria) this;
        }

        public Criteria andSecurityNotLike(String value) {
            addCriterion("SECURITY not like", value, "security");
            return (Criteria) this;
        }

        public Criteria andSecurityIn(List<String> values) {
            addCriterion("SECURITY in", values, "security");
            return (Criteria) this;
        }

        public Criteria andSecurityNotIn(List<String> values) {
            addCriterion("SECURITY not in", values, "security");
            return (Criteria) this;
        }

        public Criteria andSecurityBetween(String value1, String value2) {
            addCriterion("SECURITY between", value1, value2, "security");
            return (Criteria) this;
        }

        public Criteria andSecurityNotBetween(String value1, String value2) {
            addCriterion("SECURITY not between", value1, value2, "security");
            return (Criteria) this;
        }

        public Criteria andDescribeIsNull() {
            addCriterion("DESCRIBE is null");
            return (Criteria) this;
        }

        public Criteria andDescribeIsNotNull() {
            addCriterion("DESCRIBE is not null");
            return (Criteria) this;
        }

        public Criteria andDescribeEqualTo(String value) {
            addCriterion("DESCRIBE =", value, "describe");
            return (Criteria) this;
        }

        public Criteria andDescribeNotEqualTo(String value) {
            addCriterion("DESCRIBE <>", value, "describe");
            return (Criteria) this;
        }

        public Criteria andDescribeGreaterThan(String value) {
            addCriterion("DESCRIBE >", value, "describe");
            return (Criteria) this;
        }

        public Criteria andDescribeGreaterThanOrEqualTo(String value) {
            addCriterion("DESCRIBE >=", value, "describe");
            return (Criteria) this;
        }

        public Criteria andDescribeLessThan(String value) {
            addCriterion("DESCRIBE <", value, "describe");
            return (Criteria) this;
        }

        public Criteria andDescribeLessThanOrEqualTo(String value) {
            addCriterion("DESCRIBE <=", value, "describe");
            return (Criteria) this;
        }

        public Criteria andDescribeLike(String value) {
            addCriterion("DESCRIBE like", value, "describe");
            return (Criteria) this;
        }

        public Criteria andDescribeNotLike(String value) {
            addCriterion("DESCRIBE not like", value, "describe");
            return (Criteria) this;
        }

        public Criteria andDescribeIn(List<String> values) {
            addCriterion("DESCRIBE in", values, "describe");
            return (Criteria) this;
        }

        public Criteria andDescribeNotIn(List<String> values) {
            addCriterion("DESCRIBE not in", values, "describe");
            return (Criteria) this;
        }

        public Criteria andDescribeBetween(String value1, String value2) {
            addCriterion("DESCRIBE between", value1, value2, "describe");
            return (Criteria) this;
        }

        public Criteria andDescribeNotBetween(String value1, String value2) {
            addCriterion("DESCRIBE not between", value1, value2, "describe");
            return (Criteria) this;
        }

        public Criteria andFilingIsNull() {
            addCriterion("FILING is null");
            return (Criteria) this;
        }

        public Criteria andFilingIsNotNull() {
            addCriterion("FILING is not null");
            return (Criteria) this;
        }

        public Criteria andFilingEqualTo(BigDecimal value) {
            addCriterion("FILING =", value, "filing");
            return (Criteria) this;
        }

        public Criteria andFilingNotEqualTo(BigDecimal value) {
            addCriterion("FILING <>", value, "filing");
            return (Criteria) this;
        }

        public Criteria andFilingGreaterThan(BigDecimal value) {
            addCriterion("FILING >", value, "filing");
            return (Criteria) this;
        }

        public Criteria andFilingGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("FILING >=", value, "filing");
            return (Criteria) this;
        }

        public Criteria andFilingLessThan(BigDecimal value) {
            addCriterion("FILING <", value, "filing");
            return (Criteria) this;
        }

        public Criteria andFilingLessThanOrEqualTo(BigDecimal value) {
            addCriterion("FILING <=", value, "filing");
            return (Criteria) this;
        }

        public Criteria andFilingIn(List<BigDecimal> values) {
            addCriterion("FILING in", values, "filing");
            return (Criteria) this;
        }

        public Criteria andFilingNotIn(List<BigDecimal> values) {
            addCriterion("FILING not in", values, "filing");
            return (Criteria) this;
        }

        public Criteria andFilingBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("FILING between", value1, value2, "filing");
            return (Criteria) this;
        }

        public Criteria andFilingNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("FILING not between", value1, value2, "filing");
            return (Criteria) this;
        }

        public Criteria andParentidIsNull() {
            addCriterion("PARENTID is null");
            return (Criteria) this;
        }

        public Criteria andParentidIsNotNull() {
            addCriterion("PARENTID is not null");
            return (Criteria) this;
        }

        public Criteria andParentidEqualTo(BigDecimal value) {
            addCriterion("PARENTID =", value, "parentid");
            return (Criteria) this;
        }

        public Criteria andParentidNotEqualTo(BigDecimal value) {
            addCriterion("PARENTID <>", value, "parentid");
            return (Criteria) this;
        }

        public Criteria andParentidGreaterThan(BigDecimal value) {
            addCriterion("PARENTID >", value, "parentid");
            return (Criteria) this;
        }

        public Criteria andParentidGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("PARENTID >=", value, "parentid");
            return (Criteria) this;
        }

        public Criteria andParentidLessThan(BigDecimal value) {
            addCriterion("PARENTID <", value, "parentid");
            return (Criteria) this;
        }

        public Criteria andParentidLessThanOrEqualTo(BigDecimal value) {
            addCriterion("PARENTID <=", value, "parentid");
            return (Criteria) this;
        }

        public Criteria andParentidIn(List<BigDecimal> values) {
            addCriterion("PARENTID in", values, "parentid");
            return (Criteria) this;
        }

        public Criteria andParentidNotIn(List<BigDecimal> values) {
            addCriterion("PARENTID not in", values, "parentid");
            return (Criteria) this;
        }

        public Criteria andParentidBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("PARENTID between", value1, value2, "parentid");
            return (Criteria) this;
        }

        public Criteria andParentidNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("PARENTID not between", value1, value2, "parentid");
            return (Criteria) this;
        }

        public Criteria andIsnewIsNull() {
            addCriterion("ISNEW is null");
            return (Criteria) this;
        }

        public Criteria andIsnewIsNotNull() {
            addCriterion("ISNEW is not null");
            return (Criteria) this;
        }

        public Criteria andIsnewEqualTo(BigDecimal value) {
            addCriterion("ISNEW =", value, "isnew");
            return (Criteria) this;
        }

        public Criteria andIsnewNotEqualTo(BigDecimal value) {
            addCriterion("ISNEW <>", value, "isnew");
            return (Criteria) this;
        }

        public Criteria andIsnewGreaterThan(BigDecimal value) {
            addCriterion("ISNEW >", value, "isnew");
            return (Criteria) this;
        }

        public Criteria andIsnewGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("ISNEW >=", value, "isnew");
            return (Criteria) this;
        }

        public Criteria andIsnewLessThan(BigDecimal value) {
            addCriterion("ISNEW <", value, "isnew");
            return (Criteria) this;
        }

        public Criteria andIsnewLessThanOrEqualTo(BigDecimal value) {
            addCriterion("ISNEW <=", value, "isnew");
            return (Criteria) this;
        }

        public Criteria andIsnewIn(List<BigDecimal> values) {
            addCriterion("ISNEW in", values, "isnew");
            return (Criteria) this;
        }

        public Criteria andIsnewNotIn(List<BigDecimal> values) {
            addCriterion("ISNEW not in", values, "isnew");
            return (Criteria) this;
        }

        public Criteria andIsnewBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("ISNEW between", value1, value2, "isnew");
            return (Criteria) this;
        }

        public Criteria andIsnewNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("ISNEW not between", value1, value2, "isnew");
            return (Criteria) this;
        }

        public Criteria andVersionIsNull() {
            addCriterion("VERSION is null");
            return (Criteria) this;
        }

        public Criteria andVersionIsNotNull() {
            addCriterion("VERSION is not null");
            return (Criteria) this;
        }

        public Criteria andVersionEqualTo(String value) {
            addCriterion("VERSION =", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionNotEqualTo(String value) {
            addCriterion("VERSION <>", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionGreaterThan(String value) {
            addCriterion("VERSION >", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionGreaterThanOrEqualTo(String value) {
            addCriterion("VERSION >=", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionLessThan(String value) {
            addCriterion("VERSION <", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionLessThanOrEqualTo(String value) {
            addCriterion("VERSION <=", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionLike(String value) {
            addCriterion("VERSION like", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionNotLike(String value) {
            addCriterion("VERSION not like", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionIn(List<String> values) {
            addCriterion("VERSION in", values, "version");
            return (Criteria) this;
        }

        public Criteria andVersionNotIn(List<String> values) {
            addCriterion("VERSION not in", values, "version");
            return (Criteria) this;
        }

        public Criteria andVersionBetween(String value1, String value2) {
            addCriterion("VERSION between", value1, value2, "version");
            return (Criteria) this;
        }

        public Criteria andVersionNotBetween(String value1, String value2) {
            addCriterion("VERSION not between", value1, value2, "version");
            return (Criteria) this;
        }

        public Criteria andStorewayIsNull() {
            addCriterion("STOREWAY is null");
            return (Criteria) this;
        }

        public Criteria andStorewayIsNotNull() {
            addCriterion("STOREWAY is not null");
            return (Criteria) this;
        }

        public Criteria andStorewayEqualTo(BigDecimal value) {
            addCriterion("STOREWAY =", value, "storeway");
            return (Criteria) this;
        }

        public Criteria andStorewayNotEqualTo(BigDecimal value) {
            addCriterion("STOREWAY <>", value, "storeway");
            return (Criteria) this;
        }

        public Criteria andStorewayGreaterThan(BigDecimal value) {
            addCriterion("STOREWAY >", value, "storeway");
            return (Criteria) this;
        }

        public Criteria andStorewayGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("STOREWAY >=", value, "storeway");
            return (Criteria) this;
        }

        public Criteria andStorewayLessThan(BigDecimal value) {
            addCriterion("STOREWAY <", value, "storeway");
            return (Criteria) this;
        }

        public Criteria andStorewayLessThanOrEqualTo(BigDecimal value) {
            addCriterion("STOREWAY <=", value, "storeway");
            return (Criteria) this;
        }

        public Criteria andStorewayIn(List<BigDecimal> values) {
            addCriterion("STOREWAY in", values, "storeway");
            return (Criteria) this;
        }

        public Criteria andStorewayNotIn(List<BigDecimal> values) {
            addCriterion("STOREWAY not in", values, "storeway");
            return (Criteria) this;
        }

        public Criteria andStorewayBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("STOREWAY between", value1, value2, "storeway");
            return (Criteria) this;
        }

        public Criteria andStorewayNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("STOREWAY not between", value1, value2, "storeway");
            return (Criteria) this;
        }

        public Criteria andDimensionIsNull() {
            addCriterion("DIMENSION is null");
            return (Criteria) this;
        }

        public Criteria andDimensionIsNotNull() {
            addCriterion("DIMENSION is not null");
            return (Criteria) this;
        }

        public Criteria andDimensionEqualTo(String value) {
            addCriterion("DIMENSION =", value, "dimension");
            return (Criteria) this;
        }

        public Criteria andDimensionNotEqualTo(String value) {
            addCriterion("DIMENSION <>", value, "dimension");
            return (Criteria) this;
        }

        public Criteria andDimensionGreaterThan(String value) {
            addCriterion("DIMENSION >", value, "dimension");
            return (Criteria) this;
        }

        public Criteria andDimensionGreaterThanOrEqualTo(String value) {
            addCriterion("DIMENSION >=", value, "dimension");
            return (Criteria) this;
        }

        public Criteria andDimensionLessThan(String value) {
            addCriterion("DIMENSION <", value, "dimension");
            return (Criteria) this;
        }

        public Criteria andDimensionLessThanOrEqualTo(String value) {
            addCriterion("DIMENSION <=", value, "dimension");
            return (Criteria) this;
        }

        public Criteria andDimensionLike(String value) {
            addCriterion("DIMENSION like", value, "dimension");
            return (Criteria) this;
        }

        public Criteria andDimensionNotLike(String value) {
            addCriterion("DIMENSION not like", value, "dimension");
            return (Criteria) this;
        }

        public Criteria andDimensionIn(List<String> values) {
            addCriterion("DIMENSION in", values, "dimension");
            return (Criteria) this;
        }

        public Criteria andDimensionNotIn(List<String> values) {
            addCriterion("DIMENSION not in", values, "dimension");
            return (Criteria) this;
        }

        public Criteria andDimensionBetween(String value1, String value2) {
            addCriterion("DIMENSION between", value1, value2, "dimension");
            return (Criteria) this;
        }

        public Criteria andDimensionNotBetween(String value1, String value2) {
            addCriterion("DIMENSION not between", value1, value2, "dimension");
            return (Criteria) this;
        }

        public Criteria andIsencryptIsNull() {
            addCriterion("ISENCRYPT is null");
            return (Criteria) this;
        }

        public Criteria andIsencryptIsNotNull() {
            addCriterion("ISENCRYPT is not null");
            return (Criteria) this;
        }

        public Criteria andIsencryptEqualTo(BigDecimal value) {
            addCriterion("ISENCRYPT =", value, "isencrypt");
            return (Criteria) this;
        }

        public Criteria andIsencryptNotEqualTo(BigDecimal value) {
            addCriterion("ISENCRYPT <>", value, "isencrypt");
            return (Criteria) this;
        }

        public Criteria andIsencryptGreaterThan(BigDecimal value) {
            addCriterion("ISENCRYPT >", value, "isencrypt");
            return (Criteria) this;
        }

        public Criteria andIsencryptGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("ISENCRYPT >=", value, "isencrypt");
            return (Criteria) this;
        }

        public Criteria andIsencryptLessThan(BigDecimal value) {
            addCriterion("ISENCRYPT <", value, "isencrypt");
            return (Criteria) this;
        }

        public Criteria andIsencryptLessThanOrEqualTo(BigDecimal value) {
            addCriterion("ISENCRYPT <=", value, "isencrypt");
            return (Criteria) this;
        }

        public Criteria andIsencryptIn(List<BigDecimal> values) {
            addCriterion("ISENCRYPT in", values, "isencrypt");
            return (Criteria) this;
        }

        public Criteria andIsencryptNotIn(List<BigDecimal> values) {
            addCriterion("ISENCRYPT not in", values, "isencrypt");
            return (Criteria) this;
        }

        public Criteria andIsencryptBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("ISENCRYPT between", value1, value2, "isencrypt");
            return (Criteria) this;
        }

        public Criteria andIsencryptNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("ISENCRYPT not between", value1, value2, "isencrypt");
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