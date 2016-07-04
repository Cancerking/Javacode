package com.zhf.test;

import org.junit.Test;

import com.zhf.lucene.plugin.TikaFileIndexUtils;
import com.zhf.lucene.plugin.TikaFileSearcherUtils;

public class TikaTest {

	@Test
	public void test01(){
		TikaFileIndexUtils tfu = new TikaFileIndexUtils();
		tfu.index(true);
	}
	
	@Test
	public void test02(){
		TikaFileSearcherUtils tfsu = new TikaFileSearcherUtils();
		//tfsu.searcher();
		tfsu.lighter();
	}
	
	@Test
	public void test03(){
		TikaFileSearcherUtils tfsu = new TikaFileSearcherUtils();
		tfsu.searcherByLighter("郑峰");
	}
}
