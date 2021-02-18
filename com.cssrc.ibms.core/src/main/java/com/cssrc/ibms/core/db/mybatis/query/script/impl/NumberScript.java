package com.cssrc.ibms.core.db.mybatis.query.script.impl;

import com.cssrc.ibms.core.db.mybatis.query.entity.JudgeSingle;
import com.cssrc.ibms.core.db.mybatis.query.script.ISingleScript;

public class NumberScript implements ISingleScript {
	public String getSQL(JudgeSingle judgeSingle) {
		StringBuilder sb = new StringBuilder();
		if ("1".equals(judgeSingle.getCompare())) {
			sb.append(judgeSingle.getFixFieldName()).append("=")
					.append(judgeSingle.getValue());
		} else if ("2".equals(judgeSingle.getCompare())) {
			sb.append(judgeSingle.getFixFieldName()).append("!=")
					.append(judgeSingle.getValue());
		} else if ("3".equals(judgeSingle.getCompare())) {
			sb.append(judgeSingle.getFixFieldName()).append(">")
					.append(judgeSingle.getValue());
		} else if ("4".equals(judgeSingle.getCompare())) {
			sb.append(judgeSingle.getFixFieldName()).append(">=")
					.append(judgeSingle.getValue());
		} else if ("5".equals(judgeSingle.getCompare())) {
			sb.append(judgeSingle.getFixFieldName()).append("<")
					.append(judgeSingle.getValue());
		} else if ("6".equals(judgeSingle.getCompare())) {
			sb.append(judgeSingle.getFixFieldName()).append("<=")
					.append(judgeSingle.getValue());
		}

		return sb.toString();
	}
}
