package com.example.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.entity.HotListEntity;
import com.example.entity.ProductTest;


@RestController
public class ReactNtiveTestResource {
	@GetMapping("/get-product-test")
    public List<ProductTest> searchProductBinds2() {
    	String path = "f:/json/index.json";
    	String jsonStr = "";
		BufferedReader brname;
		try {
			FileReader file = new FileReader(new File(path));
			brname = new BufferedReader(file);

			String sname = null;
			while ((sname = brname.readLine()) != null) {
				jsonStr+=sname.trim();
			}
			brname.close();
			file.close();
			return getProdutList(jsonStr);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
    }
	
	@GetMapping("/get-hot-list-test")
    public List<HotListEntity> getHotListTest() {
    	String path = "f:/json/hotlist.json";
    	String jsonStr = "";
		BufferedReader brname;
		try {
			FileReader file = new FileReader(new File(path));
			brname = new BufferedReader(file);

			String sname = null;
			while ((sname = brname.readLine()) != null) {
				jsonStr+=sname.trim();
			}
			brname.close();
			file.close();
			return getHotListJson(jsonStr);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
    }
    
    private List<HotListEntity> getHotListJson(String jsonStr) {
    	List<HotListEntity> list = new LinkedList<>();
    	try {
			JSONObject json = JSONObject.parseObject(jsonStr);
			JSONObject dataJson =JSONObject.parseObject(json.getString("data"));
			JSONArray array = JSONArray.parseArray(dataJson.getString("list"));
			HotListEntity hotListEntity;
			 for (int i = 0; i < array.size(); i++) {
				 hotListEntity = new HotListEntity();
				 JSONObject itemJson = array.getJSONObject(i);
				 hotListEntity.setId(itemJson.getString("id"));
				 hotListEntity.setTitle(itemJson.getString("title"));
				 hotListEntity.setDesc(itemJson.getString("desc"));
				 hotListEntity.setImgUrl(itemJson.getString("imgUrl"));
				 list.add(hotListEntity);
			 }
			 return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
    
    @GetMapping("/get-category-list")
    public List<HotListEntity> getCategorylistTest() {
    	String path = "f:/json/categorylist.json";
    	String jsonStr = "";
		BufferedReader brname;
		try {
			FileReader file = new FileReader(new File(path));
			brname = new BufferedReader(file);

			String sname = null;
			while ((sname = brname.readLine()) != null) {
				jsonStr+=sname.trim();
			}
			brname.close();
			file.close();
			return getHotListJson(jsonStr);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
    }
	
	private  List<ProductTest> getProdutList(String jsonStr){
		List<ProductTest> list = new LinkedList<>();
		try {
			JSONObject json = JSONObject.parseObject(jsonStr);
			JSONObject dataJson =JSONObject.parseObject(json.getString("data"));
			JSONArray array = JSONArray.parseArray(dataJson.getString("categories"));
			ProductTest productTest;
			 for (int i = 0; i < array.size(); i++) {
				 productTest = new ProductTest();
				 JSONObject itemJson = array.getJSONObject(i);
				 productTest.setId(itemJson.getString("id"));
				 productTest.setTitle(itemJson.getString("title")); 
				 productTest.setImgUrl(itemJson.getString("imgUrl"));
				 list.add(productTest);
			 }
			 return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
