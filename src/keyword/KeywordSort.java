package keyword;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
			List<Project> projects = GetDate.getProject(conn, startId, batchSize);
			int project_id = 0;
			for (Project project:projects) {
				System.out.println("匹配项目:" + project.getName() + " id:" + project.getId());
				//统计标签对应的词频
				Map<Integer,Float> wordFrequency = new HashMap<Integer,Float>();
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
					
				//查询词频中key对应的tagname
				Map<Integer,String> wordName = GetDate.getTagNames(wordFrequency, conn);
				//存储wordFrequency和wordName
				GetDate.insertHotWordsByBatch(conn2, wordName, wordFrequency, project_id);
				
				// 如果插入成功 修改pointers表中的指针
				GetDate.updatePointer(conn, SourceTableName,
						TargetTableName, project_id + 1);
				
			} 
				
		}//while循环结尾

	}

}