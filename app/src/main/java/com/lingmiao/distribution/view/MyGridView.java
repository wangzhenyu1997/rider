package com.lingmiao.distribution.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 
 * @Autor 阎道成
 *  * @Content 自定义gridview，解决ScrollView中嵌套gridview显示不正常的问题（1行半）
 */
public class MyGridView extends GridView{
	  public MyGridView(Context context, AttributeSet attrs) { 
	        super(context, attrs); 
	    } 
	 
	    public MyGridView(Context context) { 
	        super(context); 
	    } 
	 
	    public MyGridView(Context context, AttributeSet attrs, int defStyle) { 
	        super(context, attrs, defStyle); 
	    } 
	 
	    @Override 
	    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) { 
	 
	        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, 
	                MeasureSpec.AT_MOST); 
	        super.onMeasure(widthMeasureSpec, expandSpec); 
	    } 
}
