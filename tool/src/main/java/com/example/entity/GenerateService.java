/**
 * 
 */
package com.example.entity;

import com.example.config.utils.CreateFile;
import com.example.config.utils.MySQLTableComment;

/**   
* @Description:生成service
* @author suicaijiao  
* @date 2019年4月25日  
*/
public class GenerateService {

	public static void getServiceFile(String tableName){
		//获得表名
		String name = tableName.substring(5);
		String entityClassName =MySQLTableComment.firstCharacterToUpper(name);
		String dirName = "D:/"+entityClassName+"/service";// 创建目录
        CreateFile.createDir(dirName);// 调用方法创建目录
        String fileName = dirName + "/"+entityClassName+"Service.java";// 创建文件
        CreateFile.createFile(fileName);// 调用方法创建文件
        
        StringBuilder builder = new StringBuilder();
        builder.append("package com.example.service;"+"\r\n\r\n");
        builder.append("import java.util.List;"+"\r\n\r\n");
        builder.append("import com.example.entity."+entityClassName+";\r\n\r\n");
        
        builder.append("public interface "+entityClassName+"Service {"+"\r\n\r\n");
        
        builder.append("	public int save("+entityClassName+" "+name+");"+"\r\n\r\n");
    	
        builder.append("	public int update("+entityClassName+" "+name+");"+"\r\n\r\n");
    	
        builder.append("	public int delet(Integer id);"+"\r\n\r\n");
    	
        builder.append("	public int deletByIds(String ids);"+"\r\n\r\n");
    	
        builder.append("	public "+entityClassName+" selectByPrimaryKey(Integer id);"+"\r\n\r\n");
    	
        builder.append("	public List<"+entityClassName+"> getAll("+entityClassName+" "+name+");"+"\r\n\r\n");
    	
        builder.append("	public int count("+entityClassName+" "+name+");"+"\r\n\r\n");
        
        builder.append("\r\n}");
        
        String str = CreateFile.fileLinesWrite(fileName, builder.toString(), false);
        System.out.println("创建成功："+fileName);
	}
	
	public static void main(String[] args) {
		getServiceFile("tb_e_product");
	}
}
