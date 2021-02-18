package com.cssrc.ibms.core.resources.io.bean;

import com.cssrc.ibms.core.resources.io.bean.ins.CheckResult;
import com.cssrc.ibms.core.resources.io.bean.ins.ConditionResult;
import com.cssrc.ibms.core.resources.io.bean.ins.SignResult;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * 靶场试验实例表单
 */
@XmlRootElement(name = "rangeTestInstance")
@XmlAccessorType(XmlAccessType.FIELD)
public class RangeTestInstance {
    private String ID;
    private String F_BGMC;
    private String F_NR;
    private String F_BGBH;
    private String F_JCBMB;
    private String F_SSGZXM;
    private String F_BB;
    private String F_KSJCSJ;
    private String F_JSJCSJ;
    private String F_BDZL;
    private String F_SSCH;
    private String F_BGZT;

    /**
     * 检查结果
     */
    private List<CheckResult> checkResultList;

    /**
     * 检查条件结果
     */
    private List<ConditionResult> conditionResultList;

    /**
     * 签署结果
     */
    private List<SignResult> signResultList;

    public List<CheckResult> getCheckResultList() {
        return checkResultList;
    }

    public void setCheckResultList(List<CheckResult> checkResultList) {
        this.checkResultList = checkResultList;
    }

    public List<ConditionResult> getConditionResultList() {
        return conditionResultList;
    }

    public void setConditionResultList(List<ConditionResult> conditionResultList) {
        this.conditionResultList = conditionResultList;
    }

    public List<SignResult> getSignResultList() {
        return signResultList;
    }

    public void setSignResultList(List<SignResult> signResultList) {
        this.signResultList = signResultList;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getF_BGMC() {
        return F_BGMC;
    }

    public void setF_BGMC(String f_BGMC) {
        F_BGMC = f_BGMC;
    }

    public String getF_NR() {
        return F_NR;
    }

    public void setF_NR(String f_NR) {
        F_NR = f_NR;
    }

    public String getF_BGBH() {
        return F_BGBH;
    }

    public void setF_BGBH(String f_BGBH) {
        F_BGBH = f_BGBH;
    }

    public String getF_JCBMB() {
        return F_JCBMB;
    }

    public void setF_JCBMB(String f_JCBMB) {
        F_JCBMB = f_JCBMB;
    }

    public String getF_SSGZXM() {
        return F_SSGZXM;
    }

    public void setF_SSGZXM(String f_SSGZXM) {
        F_SSGZXM = f_SSGZXM;
    }

    public String getF_BB() {
        return F_BB;
    }

    public void setF_BB(String f_BB) {
        F_BB = f_BB;
    }

    public String getF_KSJCSJ() {
        return F_KSJCSJ;
    }

    public void setF_KSJCSJ(String f_KSJCSJ) {
        F_KSJCSJ = f_KSJCSJ;
    }

    public String getF_JSJCSJ() {
        return F_JSJCSJ;
    }

    public void setF_JSJCSJ(String f_JSJCSJ) {
        F_JSJCSJ = f_JSJCSJ;
    }

    public String getF_BDZL() {
        return F_BDZL;
    }

    public void setF_BDZL(String f_BDZL) {
        F_BDZL = f_BDZL;
    }

    public String getF_SSCH() {
        return F_SSCH;
    }

    public void setF_SSCH(String f_SSCH) {
        F_SSCH = f_SSCH;
    }

    public String getF_BGZT() {
        return F_BGZT;
    }

    public void setF_BGZT(String f_BGZT) {
        F_BGZT = f_BGZT;
    }
}
