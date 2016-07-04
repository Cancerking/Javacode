package com.zhf.luncene.analyzer;

import java.io.Reader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;

import com.chenlb.mmseg4j.Dictionary;
import com.chenlb.mmseg4j.MaxWordSeg;
import com.chenlb.mmseg4j.analysis.MMSegTokenizer;

/**
 * 自定义 中文分词
 * @author zhengfeng@chinasofti.com
 * @date 2016年6月11日 下午6:21:23 
 * @version com.zhf.luncene.analyzer.MySameAnalyzer - V1.0   
 * @description (如果有详细说明)
 */
public class MySameAnalyzer extends Analyzer {

	@Override
	public TokenStream tokenStream(String fileName, Reader in) {
		Dictionary dic = Dictionary.getInstance("/Users/mike/Public/lucene/data");
		return new MySameTokenFilter(new MMSegTokenizer(new MaxWordSeg(dic), in));
	}

}
