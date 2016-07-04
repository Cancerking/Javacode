package com.zhf.lucene;

import java.io.File;
import java.io.FileReader;
import java.io.IOException; 

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

/**
 * 初学lucene 
 * @author mike 
 *
 */
public class LuceneWorld {

	/**
	 * 建立索引
	 */
	public void index(){
		IndexWriter indexWriter = null;
		try {
			//1.创建directory
			//Directory directory = new RAMDirectory();//索引建立在内存中
			Directory directory = FSDirectory.open(new File("/Users/mike/Public/luceneIndex"));//索引建立在磁盘上
			//2.创建indexWriter
			IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LUCENE_35, new StandardAnalyzer(Version.LUCENE_35)); 
			indexWriter = new IndexWriter(directory, indexWriterConfig);
			//3.创建document
			Document document = null;
			//4.document添加file
			File files = new File("/Users/mike/Public/lucene");
			for(File file : files.listFiles()){
				document = new Document();
				document.add(new Field("context", new FileReader(file)));
				document.add(new Field("filename", file.getName(), Field.Store.YES, Field.Index.NOT_ANALYZED));
				document.add(new Field("path", file.getAbsolutePath(), Field.Store.YES, Field.Index.NOT_ANALYZED));
				//5.添加document到indexWriter索引中
				indexWriter.addDocument(document);
			}
				
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (LockObtainFailedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(indexWriter != null) indexWriter.close();
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
	 * @date 2016年5月23日 下午8:48:57 
	 * @description (如果有详细说明)
	 */
	public void searcher(){
		try {
			//1.创建directory
			Directory directory = FSDirectory.open(new File("/Users/mike/Public/luceneIndex"));//索引建立在磁盘上
			//2.创建indexReader
			IndexReader indexReader = IndexReader.open(directory);
			//3.根据indexReader创建indexSearcher
			IndexSearcher indexSearcher = new IndexSearcher(indexReader);
			/**
			 * 4.创建搜索的Query
			 * 		4.1 创建QueryParser来确定搜索的文件内容	第二个参数表示搜索的域
			 * 		4.2 创建query表示搜索域为context中包含“Lucene”的文字
			 */
			QueryParser queryParser = new QueryParser(Version.LUCENE_35, "context", new StandardAnalyzer(Version.LUCENE_35));
			Query query = queryParser.parse("Lucene");
			//5.根据searcher搜索并且返回TopDocs
			TopDocs topDocs = indexSearcher.search(query, 10);
			//6.根据TopDocs获取ScopeDocs对象
			ScoreDoc[] scoreDocs = topDocs.scoreDocs;
			for(ScoreDoc scoreDoc : scoreDocs){
				//7.根据searcher和ScopeDocs对象获取具体的Document对象
				Document document = indexSearcher.doc(scoreDoc.doc);
				//8.根据Document对象获取需要的值
				System.out.println(document.get("filename") + "(" + document.get("path") + ")");
			}
			
			//9.关闭indexReader
			indexReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}
