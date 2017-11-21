package com.qh.common.utils;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import com.qh.system.domain.UserDO;

public class ShiroUtils {
	public static Subject getSubjct() {
		return SecurityUtils.getSubject();
	}
	public static UserDO getUser() {
		return (UserDO)getSubjct().getPrincipal();
	}
	public static Integer getUserId() {
		return getUser().getUserId();
	}
	public static String getUsername() {
		return getUser().getUsername();
	}
	public static void logout() {
		getSubjct().logout();
	}
}
