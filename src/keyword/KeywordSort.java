package keyword;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.mysql.jdbc.Connection;

/**
 * �ؼ�����ȡ������
 * 
 * ��Ҫ����IKAnalyzer.jar
 * 
 * @author fq
 * @email
 */
public class KeywordSort {
	
	private static int batchSize = 500;// 一次处理项目数量
	private static int startId = 1;// 读取数据库中上次处理到的项目id
	private static String SourceTableName = "open_source_projects";
	private static String TargetTableName = "hot_words";

	public static void main(String[] args) throws SQLException {

		Connection conn;
		conn = (Connection) GetDate.getConnection();
		conn.setAutoCommit(false);
		Connection conn2;
		conn2 = (Connection) GetDate.getConnectionDest();
		conn2.setAutoCommit(false);
		
		while (true) {
			startId = GetDate.readPointer(conn, SourceTableName,
					TargetTableName);
			if(startId == -1){
				continue;//刚刚创建了表
			}
			List<Project> projects = GetDate.getProject(conn, startId, batchSize);
			if(projects.size() == 0){
				System.out.println("no projects! Sleep 3600s");
				try {
					Thread.sleep(3600*1000L);
					continue;
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			int project_id = 0;
			for (Project project:projects) {
				System.out.println("匹配项目:" + project.getName() + " id:" + project.getId());
				//统计标签对应的词频
				Map<Integer,Float> wordFrequency = new LinkedHashMap<Integer,Float>();
				project_id = project.getId();
				List<RelativeMemoToOpenSourceProjects> mtps = GetDate.getMemoIds(project_id, conn);
				for(int i = 0; i < mtps.size(); i++){
					RelativeMemoToOpenSourceProjects mtp = mtps.get(i);
					List<Integer> tagIdList = GetDate.findTagIds(mtp.getRelative_memo_id(), conn);
					mtp.setTagIds(tagIdList);
					
					
					for(Integer tagId:tagIdList){
						if(wordFrequency.containsKey(tagId)){
							wordFrequency.put(tagId, wordFrequency.get(tagId)+mtp.getMatch_weight());
						}else{
							wordFrequency.put(tagId, mtp.getMatch_weight());
						}
					}
					
				}
				
				wordFrequency = sortMap(wordFrequency);
					
				//查询词频中key对应的tagname
				Map<Integer,String> wordName = GetDate.getTagNames(wordFrequency, conn);
				//存储wordFrequency和wordName
				GetDate.insertHotWordsByBatch(conn2, wordName, wordFrequency, project_id);
				
				//存储热词到open_source_projects表中
				List<String> tags = project.getTagList();
				if(tags.size() < 10){
					//小于10 添加5个
					Set<Integer> keys = wordName.keySet();
					Iterator<Integer> it = keys.iterator();
					for(int i = 0; it.hasNext() && i < keys.size() && i<=5; i++){
						int key = it.next();
						tags.add(wordName.get(key));
					}
					
					project.setTags(project.changeTagListToStr(tags));
					GetDate.updateTagsOfOsp(conn2, project);
				}
				
				// 如果插入成功 修改pointers表中的指针
				GetDate.updatePointer(conn, SourceTableName,
						TargetTableName, project_id + 1);
				
			} 
				
		}//while循环结尾

	}
	
	
	public static Map<Integer,Float> sortMap(Map<Integer,Float> oldMap) {  
        ArrayList<Map.Entry<Integer, Float>> list = new ArrayList<Map.Entry<Integer, Float>>(oldMap.entrySet());  
        Collections.sort(list, new Comparator<Map.Entry<Integer, Float>>() {  
  
            public int compare(Entry<Integer, Float> arg0,  
                    Entry<Integer, Float> arg1) {  
                if(arg0.getValue() - arg1.getValue() > 0)
                	return -1;
                else if(arg0.getValue() - arg1.getValue() < 0)
                	return 1;
                else
                	return 0;
            }  
        });  
        Map<Integer,Float> newMap = new LinkedHashMap<Integer,Float>();  
        for (int i = 0; i < list.size(); i++) {  
            newMap.put(list.get(i).getKey(), list.get(i).getValue());  
        }  
        return newMap;  
    }  

}