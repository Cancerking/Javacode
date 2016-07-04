package com.zhf.lucene.filter;

import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;

import com.zhf.lucene.index.FileIndexUtils;

/**
 * 使用自定义ID过滤器
 * @author zhengfeng@chinasofti.com
 * @date 2016年6月17日 下午9:55:31 
 * @version com.zhf.lucene.filter.CustomFilter - V1.0   
 * @description (如果有详细说明)
 */
public class CustomFilter {

	/**
	 * 通过Filter进行搜索
	 * @author zhengfeng@chinasofti.com
	 * @date 2016年6月17日 下午9:56:09 
	 * @description (如果有详细说明)
	 */
	public void searcherByFilter(){
		try {
			IndexSearcher searcher = new IndexSearcher(IndexReader.open(FileIndexUtils.getDirectory()));
			Query query = new TermQuery(new Term("context", "person"));
			TopDocs tds = searcher.search(query, new MyIDFilter(), 100);
			for(ScoreDoc sd : tds.scoreDocs){
				Document doc = searcher.doc(sd.doc);
				System.out.println(sd.doc + "--"+doc.get("id")+"-----"+doc.get("filename")+"----"+doc.get("path")+"-----"+doc.get("size")+"-----"+sd.score);
			}
			searcher.close();
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
