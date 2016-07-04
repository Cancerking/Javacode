package com.zhf.luncene.analyzer;

import java.io.IOException;
import java.io.StringReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;

/**
 * 分词工具
 * @author zhengfeng@chinasofti.com
 * @date 2016年6月11日 下午12:10:37 
 * @version com.zhf.luncene.analyzer.AnalyzerUtils - V1.0   
 * @description (如果有详细说明)
 */
public class AnalyzerUtils {

	/**
	 * 显示分词
	 * @author zhengfeng@chinasofti.com
	 * @date 2016年6月11日 下午12:30:00
	 * @param str 需要分词的字符串
	 * @param analyzer 分词类型
	 * @description (如果有详细说明)
	 */
	public static void displayToken(String str, Analyzer analyzer){
		try {
			TokenStream stream = analyzer.tokenStream("content", new StringReader(str));
			//创建一个属性，这个属性会添加到流中，随着tokenStream增加
			CharTermAttribute cta = stream.addAttribute(CharTermAttribute.class);
			while (stream.incrementToken()) {
				System.out.print("{"+ cta+"}");
			}
			System.out.println();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 显示分词所有信息
	 * @author zhengfeng@chinasofti.com
	 * @date 2016年6月11日 下午1:05:51
	 * @param str
	 * @param analyzer 
	 * @description (如果有详细说明)
	 */
	public static void displayTokenInfo(String str, Analyzer analyzer){
		try {
			TokenStream stream = analyzer.tokenStream("content", new StringReader(str));
			//保存相应词汇
			CharTermAttribute cta = stream.addAttribute(CharTermAttribute.class);
			//保存词与词之间的位置增量
			PositionIncrementAttribute pia = stream.addAttribute(PositionIncrementAttribute.class);
			//保存各个词汇之间的偏移量
			OffsetAttribute oa = stream.addAttribute(OffsetAttribute.class);
			//分词的类型
			TypeAttribute ta = stream.addAttribute(TypeAttribute.class);
			for(;stream.incrementToken();){
				System.out.print(pia.getPositionIncrement() + ":");
				System.out.print(cta + "{"+oa.startOffset()+"-"+oa.endOffset()+"}" + "--->" + ta.type() + "\n");
			}
			System.out.println();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}