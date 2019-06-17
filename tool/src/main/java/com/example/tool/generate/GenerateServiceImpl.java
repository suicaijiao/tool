package com.example.tool.generate;

import com.example.config.utils.CreateFile;
import com.example.config.utils.MySQLTableComment;

/**   
* @Description:
* @author suicaijiao  
* @date 2019年4月25日  
*/
public class GenerateServiceImpl {

	/**
	 * 生成serviceImp
	 * @param tableName 数据库表名
	 * @param subNum	截取前缀位数
	 * @param packageName 报名去掉package的包名称（com.example.demo）
	 */
	public static void getServiceImpFile(String tableName,int subNum,String packageName){
		//获得表名
		String name = tableName.substring(subNum);
		String entityClassName =MySQLTableComment.firstCharacterToUpper(name);
		String entityClassNameToLower = MySQLTableComment.firstCharacterToLower(name);
		
		// 查询主键名称(数据库名)
		String keyNameDb =MySQLTableComment.getPrimaryKeyName(tableName).split(",")[0];
		String keyNameEntity = MySQLTableComment.replaceUnderlineAndfirstTo(keyNameDb,"_");
		
		// 查询主键类型(数据库名)
		String keyType = MySQLTableComment.getPrimaryKeyName(tableName).split(",")[1];
		
		String daoName = entityClassNameToLower+"Dao";
		
		String dirName = "D:/"+entityClassName+"/service/impl";// 创建目录
        CreateFile.createDir(dirName);// 调用方法创建目录
        String fileName = dirName + "/"+entityClassName+"ServiceImpl.java";// 创建文件
        CreateFile.createFile(fileName);// 调用方法创建文件
        
        StringBuilder builder = new StringBuilder();
        builder.append("package "+packageName+".service.impl;"+"\r\n\r\n");

        builder.append("import "+packageName+".dao."+entityClassName+"Dao;\r\n");
        builder.append("import org.springframework.beans.factory.annotation.Autowired;\r\n");
        builder.append("import org.springframework.stereotype.Service;\r\n");
        builder.append("import java.util.List;"+"\r\n");
        builder.append("import "+packageName+".entity."+entityClassName+";\r\n");
        builder.append("import "+packageName+".service."+entityClassName+"Service;\r\n");
        
        builder.append("@Service(\""+entityClassNameToLower+"Service\")\r\n");
        builder.append("public class "+entityClassName+"ServiceImpl implements "+entityClassName+"Service  {"+"\r\n\r\n");
        builder.append("	@Autowired\r\n");
        builder.append("	private "+entityClassName+"Dao "+entityClassNameToLower+"Dao;\r\n\r\n");
        
        builder.append("	@Override\r\n");
        builder.append("	public int save("+entityClassName+" "+entityClassNameToLower+") {\r\n");
        builder.append("		return "+daoName+".save("+entityClassNameToLower+");\r\n");
        builder.append("	}\r\n\r\n");
        
        builder.append("	@Override\r\n");
        builder.append("	public int update("+entityClassName+" "+entityClassNameToLower+") {\r\n");
        builder.append("		return "+daoName+".update("+entityClassNameToLower+");\r\n");
        builder.append("	}\r\n\r\n");

        builder.append("	@Override\r\n");
        if("varchar(32)".equalsIgnoreCase(keyType)){
        	builder.append("	public int delete(String "+keyNameEntity+") {\r\n");
        }
        else{
        	builder.append("	public int delete(Integer "+keyNameEntity+") {\r\n");
        }
        builder.append("		return "+daoName+".delete("+keyNameEntity+");\r\n");
        builder.append("	}\r\n\r\n");

        builder.append("	@Override\r\n");
        builder.append("	public int deleteByIds(String ids) {\r\n");
        builder.append("		return "+daoName+".deleteByIds(ids);\r\n");
        builder.append("	}\r\n\r\n");

        builder.append("	@Override\r\n");
        
        if("varchar(32)".equalsIgnoreCase(keyType)){
        	builder.append("	public "+entityClassName+" selectByPrimaryKey(String "+keyNameEntity+") {\r\n");
        }
        else{
        	builder.append("	public "+entityClassName+" selectByPrimaryKey(Integer "+keyNameEntity+") {\r\n");
        }
        
        builder.append("		return "+daoName+".selectByPrimaryKey("+keyNameEntity+");\r\n");
        builder.append("	}\r\n\r\n");

        builder.append("	@Override\r\n");
        builder.append("	public List<"+entityClassName+"> getAll("+entityClassName+" "+entityClassNameToLower+") {\r\n");
        builder.append("		return "+daoName+".getAll("+entityClassNameToLower+");\r\n");
        builder.append("	}\r\n\r\n");
        
        builder.append("	@Override\r\n");
        builder.append("	public int count("+entityClassName+" "+entityClassNameToLower+") {\r\n");
        builder.append("		return "+daoName+".count("+entityClassNameToLower+");\r\n");
        builder.append("	}\r\n\r\n");
        
        builder.append("}");
        String str = CreateFile.fileLinesWrite(fileName, builder.toString(), false);
        if("write".equals(str)){
        	System.out.println("创建成功："+fileName);
        }
	}
}
