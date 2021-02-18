package com.cssrc.ibms.index.model;

/**
 * 回复评论Model
 * @author YangBo
 *
 */
public class ReplyComment {

	protected Long cmId;
	protected String name;
	protected String time;
	protected String content;
	protected String replyName;
	protected Long replyId;

	public ReplyComment()
	{
	}

	public ReplyComment(Long cmId, String name, String time, String content, String replyName, Long replyId)
	{
		this.cmId = cmId;
		this.name = name;
		this.time = time;
		this.content = content;
		this.replyName = replyName;
		this.replyId = replyId;
	}

	public String getName()
	{
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTime() {
		return this.time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getContent() {
		return this.content;
	}
	public void setContent(String content) {
		this.content = content;
	}

	public Long getCmId() {
		return this.cmId;
	}

	public void setCmId(Long cmId) {
		this.cmId = cmId;
	}
	public String getReplyName() {
		return this.replyName;
	}
	public void setReplyName(String replyName) {
		this.replyName = replyName;
	}
	public Long getReplyId() {
		return this.replyId;
	}
	public void setReplyId(Long replyId) {
		this.replyId = replyId;
	}

}
