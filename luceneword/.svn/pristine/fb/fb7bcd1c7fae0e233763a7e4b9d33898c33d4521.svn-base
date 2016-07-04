package com.zhf.lucene.query;

import java.text.SimpleDateFormat;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.NumericField;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.Version;

/**
 * 自定义Query
 * @author zhengfeng@chinasofti.com
 * @date 2016年6月16日 下午9:39:19 
 * @version com.zhf.lucene.query.CustomParser - V1.0   
 * @description (如果有详细说明)
 */
public class CustomParser extends QueryParser{

	public CustomParser(Version matchVersion, String f, Analyzer a) {
		super(matchVersion, f, a);
	}
	
	@Override
	protected Query getWildcardQuery(String field, String termStr)
			throws ParseException {
		throw new ParseException("禁用");
	}

	@Override
	protected Query getFuzzyQuery(String field,
			String termStr, float minSimilarity) throws ParseException {
		throw new ParseException("禁用");
	}

	/**
	 * 设定查询规则
	 * @author zhengfeng@chinasofti.com
	 * @date 2016年6月16日 下午10:41:16
	 * @param field 查询域
	 * @param part1
	 * @param part2
	 * @param inclusive
	 * @return
	 * @throws ParseException 
	 * @description (如果有详细说明)
	 */
	@Override
	protected Query getRangeQuery(String field,
			String part1, String part2, boolean inclusive) throws ParseException {
		if(field.equals("size")){
			return NumericRangeQuery.newIntRange(field, Integer.parseInt(part1), Integer.parseInt(part2), inclusive, inclusive);
		}else if(field.equals("data")){
//			STRING DATATYPE = "YYYY-MM-DD";
//			SIMPLEDATEFORMAT SDF = NEW SIMPLEDATEFORMAT();
		}
		return null;
	}

}
