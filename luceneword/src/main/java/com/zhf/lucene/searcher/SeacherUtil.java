package com.zhf.lucene.searcher;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

import com.zhf.lucene.index.FileIndexUtils;

/**
 * 索引的搜索
 * @author zhengfeng@chinasofti.com
 * @date 2016年5月24日 下午10:35:59 
 * @version com.zhf.lucene.searcher.SeacherUtil - V1.0   
 * @description (如果有详细说明)
 */
public class SeacherUtil {

	private Directory directory;
	
	private IndexReader reader;
	
	public SeacherUtil(){
		try {
			directory = FSDirectory.open(new File("/Users/mike/Public/index01"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 *  单例IndexSearcher
	 * @author zhengfeng@chinasofti.com
	 * @date 2016年5月24日 下午10:44:24
	 * @return 
	 * @description (如果有详细说明)
	 */
	public IndexSearcher getSeacher(){
		try {
			if(reader == null){
				reader = IndexReader.open(directory);
			} else {
				IndexReader ir = IndexReader.openIfChanged(reader);
				if(ir != null){
					reader.close();
					reader = ir;
				}
			}
			return new IndexSearcher(reader);
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * 精确搜索 
	 * @author zhengfeng@chinasofti.com
	 * @date 2016年5月24日 下午10:47:48
	 * @param field  搜索的域
	 * @param name 	 搜索的内容
	 * @param num 	 搜索显示几条
	 * @throws IOException 
	 * @description (如果有详细说明)
	 * searcherByTerm("content", "a", 10)
	 */
	public void searcherByTerm(String field, String name, int num) {
		try {
			IndexSearcher searcher = getSeacher();
			Query query = new TermQuery(new Term(field, name));
			TopDocs tds = searcher.search(query, num);
			for(ScoreDoc sd : tds.scoreDocs){
				Document doc = searcher.doc(sd.doc);
				System.out.println("{"+sd.doc +"}" + doc.get("name") + "------" + doc.get("email") + "-----" + doc.get("id"));
			}
			searcher.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 搜索区域  字符串类型
	 * @author zhengfeng@chinasofti.com
	 * @date 2016年5月25日 下午9:51:03
	 * @param field	  搜索的域
	 * @param start   搜索开始区域
	 * @param end	  搜索结束区域
	 * @param num 	  搜索显示几条
	 * @description (如果有详细说明)
	 * TermRangeQuery 五个参数分别表示
	 * 1.搜索的域
	 * 2.搜索开始区域
	 * 3.搜索结束区域
	 * 4.是否包含开始区域
	 * 5.是否包含结束区域
	 * 
	 * 搜索实例name域中以a开头f结尾之间数据 显示10条   不支持数字
	 * searcherByTermRange("name", "a", "f", 10)  
	 */
	public void searcherByTermRange(String field, String start, String end, int num){
		try {
			IndexSearcher searcher = getSeacher();
			Query query = new TermRangeQuery(field, start, end, true, true);
			TopDocs tds = searcher.search(query, num);
			for(ScoreDoc sd : tds.scoreDocs){
				Document doc = searcher.doc(sd.doc);
				System.out.println("{"+sd.doc +"}" + doc.get("name") + "------" + doc.get("email") + "-----" + doc.get("id"));
			}
			searcher.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 搜索区域 数字类型
	 * @author zhengfeng@chinasofti.com
	 * @date 2016年5月26日 下午9:51:03
	 * @param field	  搜索的域
	 * @param start   搜索开始区域
	 * @param end	  搜索结束区域
	 * @param num 	  搜索显示几条
	 * @description (如果有详细说明)
	 * TermRangeQuery 五个参数分别表示
	 * 1.搜索的域
	 * 2.搜索开始区域
	 * 3.搜索结束区域
	 * 4.是否包含开始区域
	 * 5.是否包含结束区域
	 * 
	 * 搜索实例name域中以a开头f结尾之间数据 显示10条
	 * searcherByTermRange("id", 1, 5, 10)  
	 */
	public void searcherByTermRange(String field, int start, int end, int num){
		try {
			IndexSearcher searcher = getSeacher();
			Query query = NumericRangeQuery.newIntRange(field, start, end, true, true);
			TopDocs tds = searcher.search(query, num);
			for(ScoreDoc sd : tds.scoreDocs){
				Document doc = searcher.doc(sd.doc);
				System.out.println("{"+sd.doc +"}" + doc.get("name") + "------" + doc.get("email") + "-----" + doc.get("id"));
			}
			searcher.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 前准搜索
	 * @author zhengfeng@chinasofti.com
	 * @date 2016年5月25日 下午10:51:53
	 * @param field 搜索的域
	 * @param value 搜索的前准
	 * @param num 搜索显示几条
	 * @description (如果有详细说明)
	 */
	public void searcherByPrefix(String field, String value, int num){
		try {
			IndexSearcher searcher = getSeacher();
			Query query = new PrefixQuery(new Term(field ,value));
			TopDocs tds = searcher.search(query, num);
			for(ScoreDoc sd : tds.scoreDocs){
				Document doc = searcher.doc(sd.doc);
				System.out.println("{"+sd.doc +"}" + doc.get("name") + "------" + doc.get("email") + "-----" + doc.get("id"));
			}
			searcher.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 通配符搜索 
	 * @author zhengfeng@chinasofti.com
	 * @date 2016年5月25日 下午10:57:12
	 * @param field 搜索的域
	 * @param value 搜索的通配符
	 * @param num 搜索显示几条
	 * @description (如果有详细说明)
	 * *表示通配多个字符	  ？表示通配一个字符
	 * searcherByWildcard("name", "*k", 10)
	 */
	public void searcherByWildcard(String field, String value, int num){
		try {
			IndexSearcher searcher = getSeacher();
			Query query = new WildcardQuery(new Term(field ,value));
			TopDocs tds = searcher.search(query, num);
			for(ScoreDoc sd : tds.scoreDocs){
				Document doc = searcher.doc(sd.doc);
				System.out.println("{"+sd.doc +"}" + doc.get("name") + "------" + doc.get("email") + "-----" + doc.get("id"));
			}
			searcher.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 搜索
	 * @author zhengfeng@chinasofti.com
	 * @date 2016年5月26日 下午8:53:01
	 * @param num 
	 * @description (如果有详细说明)
	 */
	public void searcherByBoolean(int num){
		try {
			IndexSearcher searcher = getSeacher();
			BooleanQuery query = new BooleanQuery();
			/**
			 * Occur.MUST  必须出现
			 * Occur.SHOULD  可以出现
			 * Occur.MUST_NOT 不出现
			 */
			query.add(new TermQuery(new Term("name", "a")), Occur.MUST); //必须name中有包含 a		Occur.MUST_NOT不包含
			query.add(new TermQuery(new Term("content", "like")), Occur.MUST);//必须content中包含 like
			TopDocs tds = searcher.search(query, num);
			for(ScoreDoc sd : tds.scoreDocs){
				Document doc = searcher.doc(sd.doc);
				System.out.println("{"+sd.doc +"}" + doc.get("name") + "------" + doc.get("email") + "-----" + doc.get("id"));
			}
			searcher.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * 不支持中午搜索  但是英文支持比较好
	 * @author zhengfeng@chinasofti.com
	 * @date 2016年5月26日 下午9:10:27
	 * @param num 
	 * @description (如果有详细说明)
	 * i like a
	 */
	public void searcherByPhrase(int num){
		try {
			IndexSearcher searcher = getSeacher();
			PhraseQuery query = new PhraseQuery();
			query.setSlop(1); //跳一次
			//第一个Term
			query.add(new Term("content", "I"));
			//产生距离之后第二个Term
			query.add(new Term("content", "like"));
			TopDocs tds = searcher.search(query, num);
			for(ScoreDoc sd : tds.scoreDocs){
				Document doc = searcher.doc(sd.doc);
				System.out.println("{"+sd.doc +"}" + doc.get("name") + "------" + doc.get("email") + "-----" + doc.get("id"));
			}
			searcher.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * 模糊搜索
	 * @author zhengfeng@chinasofti.com
	 * @date 2016年5月26日 下午9:19:54
	 * @param num 
	 * @description (如果有详细说明)
	 */
	public void searcherByFuzzyQuery(int num){
		try {
			IndexSearcher searcher = getSeacher();
			Query query = new FuzzyQuery(new Term("name", "like"));
			TopDocs tds = searcher.search(query, num);
			for(ScoreDoc sd : tds.scoreDocs){
				Document doc = searcher.doc(sd.doc);
				System.out.println("{"+sd.doc +"}" + doc.get("name") + "------" + doc.get("email") + "-----" + doc.get("id"));
			}
			searcher.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * QueryParserd对象
	 * @author zhengfeng@chinasofti.com
	 * @date 2016年5月26日 下午9:41:50
	 * @param query
	 * @param num 
	 * @description (如果有详细说明)
	 */
	public void searcherByQueryParser(Query query, int num){
		try {
			IndexSearcher searcher = getSeacher();
			TopDocs tds = searcher.search(query, num);
			for(ScoreDoc sd : tds.scoreDocs){
				Document doc = searcher.doc(sd.doc);
				System.out.println("{"+sd.doc +"}" + doc.get("name") + "------" + doc.get("email") + "-----" + doc.get("id"));
			}
			searcher.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 传入director获取searcher 
	 * @author zhengfeng@chinasofti.com
	 * @date 2016年5月27日 下午8:44:18
	 * @param directory
	 * @return 
	 * @description (如果有详细说明)
	 */
	public IndexSearcher getSeacher(Directory directory){
		try {
			if(reader == null){
				reader = IndexReader.open(directory);
			} else {
				IndexReader ir = IndexReader.openIfChanged(reader);
				if(ir != null){
					reader.close();
					reader = ir;
				}
			}
			return new IndexSearcher(reader);
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 * 根据query搜索 进行分页
	 * @author zhengfeng@chinasofti.com
	 * @date 2016年5月27日 下午9:04:10
	 * @param query	条件
	 * @param pageIndex 开始
	 * @param pageSize  大小
	 * @description (如果有详细说明)
	 */
	public void searcherByPage(String query, int pageIndex, int pageSize){
		try {
			Directory directory = FileIndexUtils.getDirectory();
			IndexSearcher searcher = getSeacher(directory);
			QueryParser parser = new QueryParser(Version.LUCENE_35, "context", new StandardAnalyzer(Version.LUCENE_35));
			Query q = parser.parse(query);
			TopDocs tds = searcher.search(q, 100);
			ScoreDoc[] sd = tds.scoreDocs;
			int start = (pageIndex - 1) * pageSize;
			int end = pageIndex * pageSize;
			for(int i=start; i<end; i++){
				Document doc = searcher.doc(sd[i].doc);
				System.out.println(sd[i].doc + " " + doc.get("path") + "---->" + doc.get("filename"));
			}
			searcher.close();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * 不进行分页
	 * @author zhengfeng@chinasofti.com
	 * @date 2016年5月27日 下午10:37:56
	 * @param query
	 * @param pageIndex
	 * @param pageSize 
	 * @description (如果有详细说明)
	 */
	public void searcherByNoPage(String query){
		try {
			Directory directory = FileIndexUtils.getDirectory();
			IndexSearcher searcher = getSeacher(directory);
			QueryParser parser = new QueryParser(Version.LUCENE_35, "context", new StandardAnalyzer(Version.LUCENE_35));
			Query q = parser.parse(query);
			TopDocs tds = searcher.search(q, 100);
			ScoreDoc[] sd = tds.scoreDocs;
			for(int i=0; i<sd.length; i++){
				Document doc = searcher.doc(sd[i].doc);
				System.out.println(sd[i].doc + " " + doc.get("path") + "---->" + doc.get("filename"));
			}
			searcher.close();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 返回最后一个scoredoc
	 * this method is used for....
	 * @author zhengfeng@chinasofti.com
	 * @date 2016年5月28日 上午11:42:50
	 * @param pageIndex 第几页
	 * @param pageSize 每页大小
	 * @param query	查询条件
	 * @param searcher	查询对象
	 * @return
	 * @throws IOException 
	 * @description (如果有详细说明)
	 */
	public ScoreDoc getLastScoreDoc(int pageIndex, int pageSize, Query query, IndexSearcher searcher) throws IOException{
		//如果是第一页则返回为空
		if(pageIndex == 1){
			return null;
		}
		//获取上一页的数量
		int num = pageSize * (pageIndex - 1);
		TopDocs tds = searcher.search(query, num);
		return tds.scoreDocs[num -1];
	}
	
	/**
	 * 
	 * 从上一页往下搜索
	 * @author zhengfeng@chinasofti.com
	 * @date 2016年5月27日 下午10:58:16
	 * @param query 
	 * @description (如果有详细说明)
	 */
	public void searcherPageByAfter(String query, int pageIndex, int pageSize){
		try {
			Directory directory = FileIndexUtils.getDirectory();
			IndexSearcher searcher = getSeacher(directory);
			QueryParser parser = new QueryParser(Version.LUCENE_35, "context", new StandardAnalyzer(Version.LUCENE_35));
			Query q = parser.parse(query);
			//获取上一页的最后一个元素
			ScoreDoc lastsd = getLastScoreDoc(pageIndex, pageSize, q, searcher);
			//通过最后的一个元素搜索下一页的pagesize个元素
			TopDocs tds = searcher.searchAfter(lastsd, q, 10);
			for(ScoreDoc sd : tds.scoreDocs){
				Document doc = searcher.doc(sd.doc);
				System.out.println(sd.doc + " " + doc.get("path") + "---->" + doc.get("filename"));
			}
			searcher.close();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
