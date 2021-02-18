package com.cssrc.ibms.report.model;


public class SignItem {
    private Long id;

    private Long code;

    private Long signId;

    private Long formId;

    private Long templateId;

    private Long activitiId;

    private String signData;

    private String password;
    
    private String image;

    private String data;

    private String encryData;

    private String position;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public Long getSignId() {
        return signId;
    }

    public void setSignId(Long signId) {
        this.signId = signId;
    }

    public Long getFormId() {
        return formId;
    }

    public void setFormId(Long formId) {
        this.formId = formId;
    }

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    public Long getActivitiId() {
        return activitiId;
    }

    public void setActivitiId(Long activitiId) {
        this.activitiId = activitiId;
    }

    public String getSignData() {
        return signData;
    }

    public void setSignData(String signData) {
        this.signData = signData == null ? null : signData.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getImage()
    {
        return image;
    }

    public void setImage(String image)
    {
        this.image = image;
    }

    public String getData()
    {
        return data;
    }

    public void setData(String data)
    {
        this.data = data;
    }

    public String getEncryData()
    {
        return encryData;
    }

    public void setEncryData(String encryData)
    {
        this.encryData = encryData;
    }

    public String getPosition()
    {
        return position;
    }

    public void setPosition(String position)
    {
        this.position = position;
    }
    
    
    
}