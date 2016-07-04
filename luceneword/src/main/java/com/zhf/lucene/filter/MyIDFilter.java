package com.zhf.lucene.filter;

import java.io.IOException;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermDocs;
import org.apache.lucene.search.DocIdSet;
import org.apache.lucene.search.Filter;
import org.apache.lucene.util.OpenBitSet;

/**
 * 自定义ID过滤器
 * @author zhengfeng@chinasofti.com
 * @date 2016年6月16日 下午10:43:20 
 * @version com.zhf.lucene.filter.CustomFilter - V1.0   
 * @description (如果有详细说明)
 */
public class MyIDFilter extends Filter{
	
	private String[] ids = {"2", "4"};

	/**
	 * 重新该方法
	 * @author zhengfeng@chinasofti.com
	 * @date 2016年6月16日 下午10:43:38
	 * @param arg0
	 * @return
	 * @throws IOException 
	 * @description (如果有详细说明)
	 */
	@Override
	public DocIdSet getDocIdSet(IndexReader reader) throws IOException {
		//创建一个bit  默认元素都是0
		OpenBitSet obs = new OpenBitSet(reader.maxDoc());
		//先把元素都变成1
		obs.set(0, reader.maxDoc());
		int[] docs = new int[1];
		int[] freqs = new int[1];
		//获取id所在的位置，并将其设为0；
		for(String id : ids){
			//获取termQuery
			TermDocs tds = reader.termDocs(new Term("id", id));
			//查询出的对象位置存放在docs中。出现的频率存在fregs中,返回获取的条目
			int count = tds.read(docs, freqs);
			if(count == 1){
				//将元素删除
				obs.clear(docs[0]);
			}
		}
		return obs;
	}

}
