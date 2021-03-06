package com.example.tool.generate;

import com.example.config.utils.CreateFile;
import com.example.config.utils.MySQLTableComment;

/**   
* @Description:
* @author suicaijiao  
* @date 2019年6月14日  
*/
public class GenerateService {

	/**
	 * 生成service
	 * @param tableName 数据库表名
	 * @param subNum	截取前缀位数
	 * @param packageName 报名去掉package的包名称（com.example.demo）
	 */
	public static void getServiceFile(String tableName,int subNum,String packageName){
		//获得表名
		String name = tableName.substring(subNum);
		String entityClassName =MySQLTableComment.firstCharacterToUpper(name);
		String dirName = "D:/"+entityClassName+"/service";// 创建目录
        CreateFile.createDir(dirName);// 调用方法创建目录
        String fileName = dirName + "/"+entityClassName+"Service.java";// 创建文件
        CreateFile.createFile(fileName);// 调用方法创建文件
        
		// 查询主键名称(数据库名)
		String keyNameDb =MySQLTableComment.getPrimaryKeyName(tableName).split(",")[0];
		String keyNameEntity = MySQLTableComment.replaceUnderlineAndfirstTo(keyNameDb,"_");
		
		// 查询主键类型(数据库名)
		String keyType = MySQLTableComment.getPrimaryKeyName(tableName).split(",")[1];
        
        StringBuilder builder = new StringBuilder();
        builder.append("package "+packageName+".service;"+"\r\n\r\n");
        builder.append("import java.util.List;"+"\r\n\r\n");
        builder.append("import "+packageName+".entity."+entityClassName+";\r\n\r\n");
        
        builder.append("public interface "+entityClassName+"Service {"+"\r\n\r\n");
        
        builder.append("	public int save("+entityClassName+" "+name+");"+"\r\n\r\n");
    	
        builder.append("	public int update("+entityClassName+" "+name+");"+"\r\n\r\n");
    	if("varchar(32)".equalsIgnoreCase(keyType)){
    		builder.append("	public int delete(String "+keyNameEntity+");"+"\r\n\r\n");
    		builder.append("	public "+entityClassName+" selectByPrimaryKey(String "+keyNameEntity+");"+"\r\n\r\n");
    	}
    	else{
    		builder.append("	public int delete(Integer "+keyNameEntity+");"+"\r\n\r\n");
    		builder.append("	public "+entityClassName+" selectByPrimaryKey(Integer "+keyNameEntity+");"+"\r\n\r\n");
    	}
    	
        builder.append("	public int deleteByIds(String ids);"+"\r\n\r\n");
    	
        builder.append("	public List<"+entityClassName+"> getAll("+entityClassName+" "+name+");"+"\r\n\r\n");
    	
        builder.append("	public int count("+entityClassName+" "+name+");"+"\r\n\r\n");
        
        builder.append("\r\n}");
        
        String str = CreateFile.fileLinesWrite(fileName, builder.toString(), false);
        
        System.out.println("创建成功："+str+fileName);
	}

}
