package com.zhf.lucene.index;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.NumericField;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.util.Version;

/**
 * 生成索引的公共类 
 * @author zhengfeng@chinasofti.com
 * @date 2016年5月26日 下午10:54:51 
 * @version com.zhf.lucene.index.FileIndexUtils - V1.0   
 * @description (如果有详细说明)
 */
public class FileIndexUtils {

	private static Directory directory = null;
	
	static{
		try {
			directory = FSDirectory.open(new File("/Users/mike/Public/FileIndex/"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Directory getDirectory(){
		return directory;
	}
	
	/**
	 * 生成索引的方法
	 * @author zhengfeng@chinasofti.com
	 * @date 2016年5月26日 下午10:55:47 
	 * @description (如果有详细说明)
	 */
	public static void index(boolean hasNew){
		IndexWriter writer = null;
		try {
			writer = new IndexWriter(directory, new IndexWriterConfig(Version.LUCENE_35, new StandardAnalyzer(Version.LUCENE_35)));
			if(hasNew){
				writer.deleteAll();//删除重新建立索引
			}
			File files = new File("/Users/mike/Public/lucene/");
			Document document = null;
			Random rd = new Random();
			int index = 0;
			for(File file : files.listFiles()){
				int score = rd.nextInt(800);
				document = new Document();
				document.add(new Field("context", new FileReader(file)));
				document.add(new Field("id", String.valueOf(index++), Field.Store.YES, Field.Index.NOT_ANALYZED));
				document.add(new Field("filename", file.getName(), Field.Store.YES, Field.Index.NOT_ANALYZED));
				document.add(new Field("path", file.getAbsolutePath(), Field.Store.YES, Field.Index.NOT_ANALYZED));
				document.add(new NumericField("date", Field.Store.YES, true).setLongValue(file.lastModified()));
				document.add(new NumericField("size", Field.Store.YES, true).setIntValue((int) file.length()));
				document.add(new NumericField("score", Field.Store.NO, true).setIntValue(score));
				//5.添加document到indexWriter索引中
				writer.addDocument(document);
			}
				
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(writer != null) writer.close();
			} catch (CorruptIndexException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
