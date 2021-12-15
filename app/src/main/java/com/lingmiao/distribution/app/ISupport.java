package com.lingmiao.distribution.app;

import android.app.Application;
import android.content.Context;
/**
 *  IActivitySupport
 *  @author yandaocheng <br/>
 *	Activity帮助支持类接
 *	2018-04-23
 *	修改者，修改日期，修改内容
 */
public interface ISupport {
	/**
	 * 
	 * 获取MyApplication.
	 */
	public abstract Application getMyApplication();
	/**
	 * 
	 * 返回当前Activity上下
	 * 
	 * @return
	 */
	public abstract Context getContext();

}
