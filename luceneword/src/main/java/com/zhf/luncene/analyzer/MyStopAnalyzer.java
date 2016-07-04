package com.zhf.luncene.analyzer;

import java.io.Reader;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LengthFilter;
import org.apache.lucene.analysis.LetterTokenizer;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopAnalyzer;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.util.Version;

/**
 * 定义自己的过滤 分词器 
 * @author zhengfeng@chinasofti.com
 * @date 2016年6月11日 下午1:35:38 
 * @version com.zhf.luncene.analyzer.MyStopAnalyzer - V1.0   
 * @description (如果有详细说明)
 */
public class MyStopAnalyzer extends Analyzer{
	
	private Set stops; //过滤的词汇集合
	public MyStopAnalyzer(String[] strs) {
		//将字符数据数组转换set集合
		stops = StopFilter.makeStopSet(Version.LUCENE_35, strs, true);
		//添加本身stopAnalyzer 自定义的过滤词汇
		stops.addAll(StopAnalyzer.ENGLISH_STOP_WORDS_SET);
	}
	public MyStopAnalyzer() {
		stops = StopAnalyzer.ENGLISH_STOP_WORDS_SET;
	}

	@Override
	public TokenStream tokenStream(String fileName, Reader in) {
		return new StopFilter(Version.LUCENE_35, 
			   new LowerCaseFilter(Version.LUCENE_35,
			   new LetterTokenizer(Version.LUCENE_35, in)), stops);
	}

}
