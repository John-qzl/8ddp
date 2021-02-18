package com.cssrc.ibms.index.model;

import java.util.List;

/**
 * 评论信息
 * @author YangBo
 *
 */
public class Comment {

	protected Long cmId;
	protected String name;
	protected String time;
	protected String content;
	protected Long userId;
	protected List<ReplyComment> rpcomment;

	public Comment() {
	}

	public Comment(Long cmId, String name, String time, String content,
			List<ReplyComment> rpcomment,Long userId) {
		this.cmId = cmId;
		this.name = name;
		this.time = time;
		this.content = content;
		this.rpcomment = rpcomment;
		this.userId = userId;
	}

	public String getName() {
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

	public List<ReplyComment> getRpcomment() {
		return this.rpcomment;
	}

	public void setRpcomment(List<ReplyComment> rpcomment) {
		this.rpcomment = rpcomment;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
}
