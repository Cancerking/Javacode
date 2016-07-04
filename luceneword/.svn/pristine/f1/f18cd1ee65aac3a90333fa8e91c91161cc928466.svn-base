package com.zhf.luncene.analyzer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.util.AttributeSource;

/**
 * 自定义 中文分词 过滤器
 * @author zhengfeng@chinasofti.com
 * @date 2016年6月11日 下午6:24:08 
 * @version com.zhf.luncene.analyzer.MySameTokenFilter - V1.0   
 * @description (如果有详细说明)
 */
public class MySameTokenFilter extends TokenFilter {
	
	private CharTermAttribute cta = null;
	private PositionIncrementAttribute pia = null;
	private AttributeSource.State current; //获取当前状态
	private Stack<String> sames = null;

	protected MySameTokenFilter(TokenStream input) {
		super(input);
		cta = this.addAttribute(CharTermAttribute.class);
		pia = this.addAttribute(PositionIncrementAttribute.class);
		sames = new Stack<String>();
	}

	@Override
	public boolean incrementToken() throws IOException {
		while(sames.size() > 0){
			//将元素出栈 并且捕获这个同义词
			String str = sames.pop();
			//还原状态
			restoreState(current);
			cta.setEmpty();
			cta.append(str);
			//设置位置0
			pia.setPositionIncrement(0);
			return true;
		}
		if(!input.incrementToken()) return false;
		//进行同义词的相关处理
		if(getSameWord(cta.toString())){
			current = captureState();//捕获当前状态
		}
		return true;
	}

	/**
	 * 获取同义词 放入栈中
	 * @author zhengfeng@chinasofti.com
	 * @date 2016年6月12日 下午10:43:21
	 * @param name
	 * @return 
	 * @description (如果有详细说明)
	 */
	private boolean getSameWord(String name){
		Map<String, String[]> maps = new HashMap<String, String[]>();
		maps.put("我", new String[]{"俺", "咱"});
		maps.put("安", new String[]{"北", "上"});
		String[] sts = maps.get(name);
		if(sts != null){
			for(String s : sts){
				sames.push(s);
			}
			return true;
		}
		return false;
	}
}
