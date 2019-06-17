/**
 * 
 */
package com.example.tool.generate;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.example.config.utils.CreateFile;
import com.example.config.utils.MySQLTableComment;

/**   
* @Description: 生成实体类
* @author suicaijiao  
* @date 2019年4月24日  
*/
public class GenerateEntity {

	/**
	 * 生成实体类
	 * @param tableName 数据库表名
	 * @param subNum	截取前缀位数
	 * @param packageName 报名去掉package的包名称（com.example.demo）
	 */
	public static String getEntityFile(String tableName,int subNum,String packageName){
		//获得表名
		String entityName =MySQLTableComment.firstCharacterToUpper(tableName.substring(subNum));
		String dirName = "D:/"+entityName+"/entity";// 创建目录
        CreateFile.createDir(dirName);// 调用方法创建目录
        String fileName = dirName + "/"+entityName+".java";// 创建文件
        CreateFile.createFile(fileName);// 调用方法创建文件
        // 查询注释
        Map<String, String> commsInfo = MySQLTableComment.getColumnComments(tableName);
        StringBuilder builder = new StringBuilder();
        //写入文件内容
        Map<String, String> tablesTypeInfo =MySQLTableComment.getColumnTypes(tableName);
        builder.append("package "+packageName+".entity;"+"\r\n\r\n");
        builder.append("import java.io.Serializable;"+"\r\n");
        Set<String> columus = new HashSet<>();
        for (String key : tablesTypeInfo.keySet()) { 
    		String value = tablesTypeInfo.get(key);
    		value = MySQLTableComment.sqlType2JavaType(value);
    		columus.add(value);
		}
        // 生成导入java类库包
        for(String str:columus){
        	if("Date".equals(str)){
    			builder.append("import java.util.Date;"+"\r\n\r\n");
    		}
    		if("BigDecimal".equals(str)){
    			builder.append("import java.math.BigDecimal;"+"\r\n\r\n");
    		}
        }
        
        builder.append("public class "+entityName+" implements Serializable{"+"\r\n\r\n");
        builder.append("	private static final long serialVersionUID = 1L;"+"\r\n\r\n");
        // 生成私有实体类
        for (String key : tablesTypeInfo.keySet()) { 
    		String value = tablesTypeInfo.get(key);
    		String comms = commsInfo.get(key);
    		key = MySQLTableComment.replaceUnderlineAndfirstToUpper(key,"_");
    		value = MySQLTableComment.sqlType2JavaType(value);
    		builder.append("	/**\r\n	*\r\n"+"	* "+comms+"\r\n	*/\r\n"+"	private "+value+" "+key+";\r\n\r\n");	
		}
        // 生成get set方法
        for (String key : tablesTypeInfo.keySet()) { 
    		String value = tablesTypeInfo.get(key);
    		String comms = commsInfo.get(key);
    		key = MySQLTableComment.replaceUnderlineAndfirstToUpper(key,"_");
    		value = MySQLTableComment.sqlType2JavaType(value);
    		builder.append("	/**\r\n	 *\r\n"+"	 * "+comms+"\r\n	 * @return "+value+"\r\n	 */\r\n");
    		builder.append("	public "+value+" get"+MySQLTableComment.firstCharacterToUpper(key)+"() {\r\n"+"		return "+key+";\r\n	}\r\n");
    		
    		builder.append("	/**\r\n	 *\r\n"+"	 * "+comms+"\r\n	 * @param "+key+"\r\n	 */\r\n");
    		builder.append("	public void set"+MySQLTableComment.firstCharacterToUpper(key)+"("+value+" "+key+") {\r\n"+"		this."+key+" = "+key+";\r\n	}\r\n\r\n");
		}
        builder.append("\r\n}");
    	String str = CreateFile.fileLinesWrite(fileName, builder.toString(), false);
    	if(str.equals("write")){
    		System.out.println("创建成功"+fileName);
    	}
    	
    	return entityName;
	}
}
