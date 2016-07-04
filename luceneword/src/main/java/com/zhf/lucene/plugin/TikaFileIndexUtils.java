package com.zhf.lucene.plugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

import org.apache.commons.io.FilenameUtils;
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
import org.apache.tika.Tika;
import org.apache.tika.metadata.Metadata;

import com.chenlb.mmseg4j.analysis.MMSegAnalyzer;

/**
 * Tika生产索引
 * @author zhengfeng@chinasofti.com
 * @date 2016年6月19日 下午3:31:25 
 * @version com.zhf.lucene.plugin.TikaFileIndexUtils - V1.0   
 * @description (如果有详细说明)
 */
public class TikaFileIndexUtils {

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
	 * 生成索引
	 * @author zhengfeng@chinasofti.com
	 * @date 2016年6月19日 下午3:32:12
	 * @param hasNew 
	 * @description (如果有详细说明)
	 */
	public static void index(boolean hasNew){
		IndexWriter writer = null;
		try {
			writer = new IndexWriter(directory, new IndexWriterConfig(Version.LUCENE_35, new MMSegAnalyzer()));
			if(hasNew){
				writer.deleteAll();//删除重新建立索引
			}
			File files = new File("/Users/mike/Public/lucene/");
			Document document = null;
			for(File file : files.listFiles()){
				//Tika建立索引
				document = generatorDoc(file);
				if(null != document) writer.addDocument(document);
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
	
	/**
	 * Tika建立索引
	 * @author zhengfeng@chinasofti.com
	 * @date 2016年6月19日 下午4:08:14
	 * @param file
	 * @return
	 * @throws IOException 
	 * @description (如果有详细说明)
	 */
	public static Document generatorDoc(File file) throws IOException{
		if(file.isDirectory()) return null;
		Document document = new Document();
		Metadata metadata = new Metadata();
		document.add(new Field("context", new Tika().parse(new FileInputStream(file), metadata)));
		document.add(new Field("title", FilenameUtils.getBaseName(file.getName()), Field.Store.YES, Field.Index.ANALYZED));
		document.add(new Field("filename", file.getName(), Field.Store.YES, Field.Index.NOT_ANALYZED));
		document.add(new Field("path", file.getAbsolutePath(), Field.Store.YES, Field.Index.NOT_ANALYZED));
		document.add(new Field("type", FilenameUtils.getExtension(file.getName()), Field.Store.YES, Field.Index.NOT_ANALYZED_NO_NORMS));
		document.add(new NumericField("date", Field.Store.YES, true).setLongValue(file.lastModified()));
		document.add(new NumericField("size", Field.Store.YES, true).setIntValue((int) file.length()/1024));
		int page = 0;
		try {
			page = Integer.parseInt(metadata.get("xmpTPg:NPages"));
		} catch (NumberFormatException e) {
		}
		document.add(new NumericField("page", Field.Store.YES, true).setIntValue(page));
		return document;
		
	}
}
