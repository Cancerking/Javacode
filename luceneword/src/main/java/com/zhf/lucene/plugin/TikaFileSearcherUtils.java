package com.zhf.lucene.plugin;

import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;
import org.apache.lucene.util.Version;

import com.chenlb.mmseg4j.analysis.MMSegAnalyzer;
import com.zhf.lucene.index.FileIndexUtils;

/**
 * Tika 高亮搜索
 * @author zhengfeng@chinasofti.com
 * @date 2016年6月19日 下午5:10:15 
 * @version com.zhf.lucene.plugin.TikaFileSearcherUtils - V1.0   
 * @description (如果有详细说明)
 */
public class TikaFileSearcherUtils {

	public void searcher(){
		try {
			IndexSearcher searcher = new IndexSearcher(IndexReader.open(TikaFileIndexUtils.getDirectory()));
			TermQuery query = new TermQuery(new Term("context", "person"));
			TopDocs tds = searcher.search(query, 100);
			for(ScoreDoc sd : tds.scoreDocs){
				Document document = searcher.doc(sd.doc);
				System.out.println(document.get("title"));
			}
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 高亮搜索
	 * @author zhengfeng@chinasofti.com
	 * @date 2016年6月19日 下午5:11:37 
	 * @description (如果有详细说明)
	 */
	public void lighter(){
		try {
			String txt = "才才相通，天天有才，我是天才，天才是我，";
			TermQuery query = new TermQuery(new Term("f", "天才"));
			QueryScorer scorer = new QueryScorer(query);
			Fragmenter fragmenter = new SimpleSpanFragmenter(scorer);
			Highlighter highlighter = new Highlighter(scorer);
			highlighter.setFragmentScorer(scorer);
			String str = highlighter.getBestFragment(new MMSegAnalyzer(), "f", txt);
			System.out.println(str);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidTokenOffsetsException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 高亮查询
	 * @author zhengfeng@chinasofti.com
	 * @date 2016年6月19日 下午7:07:11
	 * @param name 
	 * @description (如果有详细说明)
	 */
	public void searcherByLighter(String name){
		try {
			Analyzer analyzer = new MMSegAnalyzer();
			IndexSearcher searcher = new IndexSearcher(IndexReader.open(TikaFileIndexUtils.getDirectory()));
			QueryParser parser = new QueryParser(Version.LUCENE_35, "title", analyzer);
			Query query = parser.parse(name);
			TopDocs tds = searcher.search(query, 10);
			for(ScoreDoc sd : tds.scoreDocs){
				Document doc = searcher.doc(sd.doc);
				String title = doc.get("title");
				title = lighterString(analyzer, query, title, "title");
				System.out.println(title);
			}
			searcher.close();
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (InvalidTokenOffsetsException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 高亮显示样式
	 * @author zhengfeng@chinasofti.com
	 * @date 2016年6月19日 下午7:12:59
	 * @param analyzer
	 * @param query
	 * @param txt
	 * @param filename
	 * @return 
	 * @throws InvalidTokenOffsetsException 
	 * @throws IOException 
	 * @description (如果有详细说明)
	 */
	private String lighterString(Analyzer analyzer, Query query, String txt, String filename) throws IOException, InvalidTokenOffsetsException{
		String str = null;
		QueryScorer scorer = new QueryScorer(query);
		Fragmenter fragmenter = new SimpleSpanFragmenter(scorer);
		Formatter fmt = new SimpleHTMLFormatter("<b>", "</b>");
		Highlighter highlighter = new Highlighter(fmt, scorer);
		highlighter.setTextFragmenter(fragmenter);
		str = highlighter.getBestFragment(analyzer, filename, txt);
		return str;
		
	}
}
