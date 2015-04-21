package keyword;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GetDate {

	public static List<RelativeMemoToOpenSourceProjects> getMemoIds(int project_id,
			Connection conn) {
		List<RelativeMemoToOpenSourceProjects> results = new ArrayList<RelativeMemoToOpenSourceProjects>();
		ResultSet rs = null;
		try {
			String sql = "select * from relative_memo_to_open_source_projects where osp_id = " + project_id ; // 鏌ヨ鏁版嵁鐨剆ql璇彞
			Statement st = (Statement) conn.createStatement(); // 鍒涘缓鐢ㄤ簬鎵ц闈欐�乻ql璇彞鐨凷tatement瀵硅薄锛宻t灞炲眬閮ㄥ彉閲�

			rs = st.executeQuery(sql); // 鎵цsql鏌ヨ璇彞锛岃繑鍥炴煡璇㈡暟鎹殑缁撴灉闆�
			while(rs.next()){
				RelativeMemoToOpenSourceProjects mtp = new RelativeMemoToOpenSourceProjects();
				mtp.setId(rs.getInt("id"));
				mtp.setOsp_id(rs.getInt("osp_id"));
				mtp.setRelative_memo_id(rs.getInt("relative_memo_id"));
				mtp.setMatch_weight(rs.getFloat("match_weight"));
				results.add(mtp);
			}
			rs.close();
			st.close();
			// conn.close(); // 鍏抽棴鏁版嵁搴撹繛鎺�
		} catch (SQLException e) {
			System.out.println("读取数据错误！getMemoIds ");
			System.out.println("出错时间: " + new Date().toString());
			e.printStackTrace();
		}
		return results;
	}
	
	public static ResultSet getMemo(String memo_ids_str,
			Connection conn) {
		ResultSet rs_memo = null;
		try {
			String sql = "select * from relative_memos where id in (" + memo_ids_str + ")"; // 鏌ヨ鏁版嵁鐨剆ql璇彞
			Statement st = (Statement) conn.createStatement(); // 鍒涘缓鐢ㄤ簬鎵ц闈欐�乻ql璇彞鐨凷tatement瀵硅薄锛宻t灞炲眬閮ㄥ彉閲�

			rs_memo = st.executeQuery(sql); // 鎵цsql鏌ヨ璇彞锛岃繑鍥炴煡璇㈡暟鎹殑缁撴灉闆�
			st.close();
			// conn.close(); // 鍏抽棴鏁版嵁搴撹繛鎺�
		} catch (SQLException e) {
			System.out.println("读取数据错误！getMemo ");
			System.out.println("出错时间: " + new Date().toString());
			e.printStackTrace();
		}
		return rs_memo;
	}
	
	
	//根据tagId的map查找tagName
	public static Map<Integer,String> getTagNames(Map<Integer,Float> wordFrequency, Connection conn){
		Map<Integer,String> tagNames = new HashMap<Integer,String>();
		Set<Integer> keyset = wordFrequency.keySet();
		Iterator<Integer> it = keyset.iterator();
		while(it.hasNext()){
			Integer tagId = it.next();
			String sql = "select name from tags where id=?";
			try {
				PreparedStatement ps = conn.prepareStatement(sql);
				ps.setInt(1, tagId);
				ResultSet rs = ps.executeQuery();
				if(rs.next())
					tagNames.put(tagId, rs.getString("name"));
				rs.close();
				ps.close();
			} catch (SQLException e) {
				System.out.println("读取标签出错：getTagNames");
				System.out.println("出错时间: " + new Date().toString());
				e.printStackTrace();
			}
			
		}
		return tagNames;
	}
	
	
	
	public static List<Integer> findTagIds(int memo_id, Connection conn){
		List<Integer> tagIds = new ArrayList<Integer>();
		String sql = "select tag_id from taggings where taggable_id=? and taggable_type=?";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, memo_id);
			ps.setString(2, "RelativeMemo");
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				tagIds.add(rs.getInt("tag_id"));
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			System.out.println("读取标签id出错：findTagIds");
			System.out.println("出错时间: " + new Date().toString());
			e.printStackTrace();
		}
		
		return tagIds;
	}
	
	
	
//	public static List<Integer> getTagIds(String memo_ids_str, Connection conn){
//		ResultSet rs = null;
//		List<Integer> tagIds = new ArrayList<Integer>();
//		String sql = "select tag_id from taggings where taggable_id in (" + memo_ids_str +") and taggable_type=?";
//		try {
//			PreparedStatement ps = conn.prepareStatement(sql);
//			ps.setString(1, "RelativeMemo");
//			rs = ps.executeQuery();
//			while(rs.next()){
//				tagIds.add(rs.getInt("tag_id"));
//			}
//			rs.close();
//			ps.close();
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		return tagIds;
//	}
	
	
	public static List<Project> getProject(Connection conn, int startId, int batchSize) {
		List<Project> projects = new ArrayList<Project>();
		ResultSet rs_project = null;
		try {
			String sql = "select * from open_source_projects where id>=? limit " + batchSize; // 鏌ヨ鏁版嵁鐨剆ql璇彞
			PreparedStatement st = conn.prepareStatement(sql); // 鍒涘缓鐢ㄤ簬鎵ц闈欐�乻ql璇彞鐨凷tatement瀵硅薄锛宻t灞炲眬閮ㄥ彉閲�
			st.setInt(1, startId);
			rs_project = st.executeQuery(); // 鎵цsql鏌ヨ璇彞锛岃繑鍥炴煡璇㈡暟鎹殑缁撴灉闆�
			while(rs_project.next()){
				Project project = new Project();
				project.setId(rs_project.getInt("id"));
				project.setName(rs_project.getString("name"));
				project.setTags(rs_project.getString("tags"));
				projects.add(project);
			}
			rs_project.close();
			st.close();
			// conn.close(); // 鍏抽棴鏁版嵁搴撹繛鎺�
		} catch (SQLException e) {
			System.out.println("读取数据错误！project");
			System.out.println("出错时间: " + new Date().toString());
			e.printStackTrace();
		}
		
		return projects;

	}
	
	@SuppressWarnings("unused")
	public static Connection getConnection() {
		Connection con = null;
		if (con == null) {
			try {
				Class.forName("com.mysql.jdbc.Driver");// 鍔犺浇Mysql鏁版嵁椹卞姩

				con = DriverManager.getConnection(
						"jdbc:mysql://192.168.80.130:3306/ossean_production?useUnicode=true&amp;characterEncoding=utf-8", "trustie", "1234");// 鍒涘缓鏁版嵁杩炴帴

			} catch (Exception e) {
				System.out.println("连接错误" + e.getMessage());
				System.out.println("出错时间: " + new Date().toString());
				e.printStackTrace();
			}
			return con; 
		}
		return con;
	}
	
	
	@SuppressWarnings("unused")
	public static Connection getConnectionDest(){
		Connection con = null;
		if (con == null) {
			try {
				Class.forName("com.mysql.jdbc.Driver");// 鍔犺浇Mysql鏁版嵁椹卞姩

				con = DriverManager.getConnection(
						"jdbc:mysql://192.168.80.130:3306/ossean_production?useUnicode=true&amp;characterEncoding=utf-8", "trustie", "1234");// 鍒涘缓鏁版嵁杩炴帴

			} catch (Exception e) {
				System.out.println("连接错误" + e.getMessage());
				System.out.println("出错时间: " + new Date().toString());
				e.printStackTrace();
			}
			return con; 
		}
		return con;
	}
	
	
	/**
	 * 向hot_words表中插入热词
	 * @param nameList
	 * @param weightList
	 * @param osp_id
	 */
	public static Boolean insertHotWordsByBatch(Connection conn, Map<Integer,String> wordName, Map<Integer,Float> wordFrequency, int osp_id){
		String sql = "insert into hot_words(osp_id,name,weight,created_at,updated_at) values (?,?,?,now(),now())";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			Set<Integer> keyset = wordFrequency.keySet();
			Iterator<Integer> it = keyset.iterator();
			int num = 0;
			while(it.hasNext() && num < 20){
				Integer tagId = it.next();
				ps.setInt(1, osp_id);
				ps.setString(2, wordName.get(tagId));
				ps.setFloat(3, wordFrequency.get(tagId));
				ps.addBatch();
				num++;
			}
			ps.executeBatch();
			conn.commit();
			ps.close();
			return true;
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			System.out.println("出错时间: " + new Date().toString());
			return false;//事务回滚 插入失败
		}
	}
	
	
	public static void updateTagsOfOsp(Connection conn, Project project){
		System.out.println("更新项目id: " + project.getId() + "对应的标签");
		String sql = "update open_source_projects set tags=? where id=?";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, project.getTags());
			ps.setInt(2, project.getId());
			ps.executeUpdate();
			conn.commit();
			ps.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("更新OSP tags字段失败");
			System.out.println("出错时间: " + new Date().toString());
			e.printStackTrace();
		}
	}
	
	
	
	public static int readPointer(Connection conn, String SourceTableName, String TargetTableName){
		int result = 1;
		String sql = "select Pointer from pointers where SourceTableName=? and TargetTableName=?";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, SourceTableName);
			ps.setString(2, TargetTableName);
			ResultSet rs = ps.executeQuery();
			if(!rs.next()){
				//没有指针 插入指向1的指针
				String sql2 = "insert into pointers(SourceTableName,TargetTableName,Pointer) values (?,?,?)";
				PreparedStatement ps2 = conn.prepareStatement(sql2);
				ps2.setString(1, SourceTableName);
				ps2.setString(2, TargetTableName);
				ps2.setString(3, "1");
				ps2.execute();
				conn.commit();
				ps2.close();
			}else{
				result = Integer.parseInt(rs.getString("Pointer"));
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("the pointer table doesn't exist");
			String sqlBody = ReadSql.Read("./files/pointers.sql");
			try {
				PreparedStatement ps3 = conn.prepareStatement(sqlBody);
				ps3.execute();
				conn.commit();
				ps3.close();
				return -1;
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return result;
	}
	
	
	public static void updatePointer(Connection conn, String SourceTableName, String TargetTableName, int value){
		String sql = "update pointers set Pointer=? where SourceTableName=? and TargetTableName=?";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, value+"");
			ps.setString(2, SourceTableName);
			ps.setString(3, TargetTableName);
			ps.executeUpdate();
			conn.commit();
			ps.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static Boolean isTagExists(Connection conn, int tagId){
		Boolean result = false;
		String sql = "select * from tags where id=?";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, tagId);
			ResultSet rs = ps.executeQuery();
			if(rs.next()){
				result = true;
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

}
