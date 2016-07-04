package com.zhf.lucene.index;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.NumericField;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.util.Version;

/**
 * lucene 索引 
 * @author zhengfeng@chinasofti.com
 * @date 2016年5月23日 下午9:24:52 
 * @version com.zhf.lucene.index.IndexUtil - V1.0   
 * @description (如果有详细说明)
 */
public class IndexUtil {

	private String[] ids = {"1", "2", "3", "4", "5", "6"};
	
	private String[] emails = {"aaa@.com", "bbb@.com", "ccc@.com", "ddd@.com", "eee@.com", "fff@.com"};
	
	private String[] contents = {"i like a", "i like b", "i like c", "i like d", "i like e", "i like f"};
	
	private String[] names = {"a", "b", "c", "d", "e", "f"};

	private Directory directory = null;
	//加权操作
	private Map<String, Float> scores = new HashMap<String, Float>();
	
	public IndexUtil() {
		try {
			scores.put("aaa", 2.0f);
			scores.put("bbb", 2.0f);
			directory = FSDirectory.open(new File("/Users/mike/Public/index01"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 创建索引 
	 * @author zhengfeng@chinasofti.com
	 * @date 2016年5月23日 下午9:37:05 
	 * @description (如果有详细说明)
	 */
	public void index(){
		IndexWriter writer = null;
		try {
			 writer = new IndexWriter(directory, new IndexWriterConfig(Version.LUCENE_35, new StandardAnalyzer(Version.LUCENE_35)));
			 Document document = null;
			 for(int i=0; i<ids.length; i++){
				 document = new Document();
				 document.add(new Field("id", ids[i], Field.Store.YES, Index.NOT_ANALYZED_NO_NORMS));
				 document.add(new Field("email", emails[i], Field.Store.YES, Index.NOT_ANALYZED));
				 document.add(new Field("content", contents[i], Field.Store.NO, Index.ANALYZED));
				 document.add(new Field("name", names[i], Field.Store.YES, Index.NOT_ANALYZED_NO_NORMS));
				 //数字添加索引
				 //document.add(new NumericField("int", Field.Store.YES, true).setIntValue(i));
				 //日期添加索引
				 //document.add(new NumericField("date", Field.Store.YES, true).setLongValue(new Date().getTime());
				 //加权操作
				 String emailKey = emails[i].substring(emails[i].indexOf("@") + 1);
				 if(scores.containsKey(emailKey)){
					 document.setBoost(scores.get(emailKey));
				 }else{
					 document.setBoost(0.5f);
				 }
				 writer.addDocument(document);
			 }
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (LockObtainFailedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(writer != null)
					writer.close();
			} catch (CorruptIndexException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 查询索引 
	 * @author zhengfeng@chinasofti.com
	 * @date 2016年5月23日 下午10:21:51 
	 * @description (如果有详细说明)
	 */
	public void query(){
		try {
			IndexReader reader = IndexReader.open(directory);
			//h获取索引文件信息
			System.out.println(reader.numDocs());
			System.out.println(reader.maxDoc());
			
			reader.close();
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 删除索引 
	 * @author zhengfeng@chinasofti.com
	 * @date 2016年5月23日 下午10:30:11 
	 * @description (如果有详细说明)
	 */
	public void delete(){
		 IndexWriter writer = null;
		 try {
			writer = new IndexWriter(directory, new IndexWriterConfig(Version.LUCENE_35, new StandardAnalyzer(Version.LUCENE_35)));
			/**
			 * 参数 query对象   Term对象(精确对象) 
			 * 此时的删除不是完全删除 而是存储在一个回收站中, 是可恢复
			 */
			writer.deleteDocuments(new Term("id", "1"));
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (LockObtainFailedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(writer != null)
					writer.close();
			} catch (CorruptIndexException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 恢复索引
	 * @author zhengfeng@chinasofti.com
	 * @date 2016年5月23日 下午10:34:48 
	 * @description (如果有详细说明)
	 */
	public void undelete(){
		try {
			IndexReader reader = IndexReader.open(directory, false);
			reader.undeleteAll();
			reader.close();
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 强制删除索引 
	 * @author zhengfeng@chinasofti.com
	 * @date 2016年5月23日 下午10:41:20 
	 * @description (如果有详细说明)
	 */
	public void forceDelete(){
		 IndexWriter writer = null;
		 try {
			writer = new IndexWriter(directory, new IndexWriterConfig(Version.LUCENE_35, new StandardAnalyzer(Version.LUCENE_35)));
			writer.forceMergeDeletes();
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (LockObtainFailedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(writer != null)
					writer.close();
			} catch (CorruptIndexException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 合并索引 
	 * @author zhengfeng@chinasofti.com
	 * @date 2016年5月23日 下午10:46:56 
	 * @description (如果有详细说明)
	 */
	public void merge(){
		 IndexWriter writer = null;
		 try {
			writer = new IndexWriter(directory, new IndexWriterConfig(Version.LUCENE_35, new StandardAnalyzer(Version.LUCENE_35)));
			//将索引合并为2段  这两段中删除的数据会被清空3.5之后不建议使用 消耗资源
			writer.forceMerge(2);
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (LockObtainFailedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(writer != null)
					writer.close();
			} catch (CorruptIndexException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 更新索引 
	 * @author zhengfeng@chinasofti.com
	 * @date 2016年5月23日 下午10:47:41 
	 * @description (如果有详细说明)
	 */
	public void update(){
		 IndexWriter writer = null;
		 try {
			writer = new IndexWriter(directory, new IndexWriterConfig(Version.LUCENE_35, new StandardAnalyzer(Version.LUCENE_35)));
			//lucene并没有更新   其实更新是先删除在添加
			Document document = new Document();
			document.add(new Field("id", ids[0], Field.Store.YES, Index.NOT_ANALYZED_NO_NORMS));
			document.add(new Field("email", emails[0], Field.Store.YES, Index.NOT_ANALYZED));
			document.add(new Field("content", contents[0], Field.Store.NO, Index.ANALYZED));
			document.add(new Field("name", names[0], Field.Store.YES, Index.NOT_ANALYZED_NO_NORMS));
			writer.updateDocument(new Term("id", "1"), document);
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (LockObtainFailedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(writer != null)
					writer.close();
			} catch (CorruptIndexException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
 	 * 搜索 
	 * @author zhengfeng@chinasofti.com
	 * @date 2016年5月24日 下午8:54:38 
	 * @description (如果有详细说明)
	 */
	public void seacher(){
		try {
			IndexReader indexReader = IndexReader.open(directory);
			IndexSearcher indexSearcher = new IndexSearcher(indexReader);
			TermQuery termQuery = new TermQuery(new Term("content", "like"));
			TopDocs topDocs = indexSearcher.search(termQuery, 10);
			for(ScoreDoc scoreDoc : topDocs.scoreDocs){
				Document document = indexSearcher.doc(scoreDoc.doc);
				System.out.println("{"+scoreDoc.doc +"}" + document.get("name") + "####" + document.get("email") + "####" + document.get("id"));
			}
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
