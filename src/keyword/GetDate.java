package keyword;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class GetDate {
	private static Connection con = null;

	public static List<Integer> getMemoIds(int project_id,
			Connection conn) {
		List<Integer> relativeMemoIds = new ArrayList<Integer>();
		ResultSet rs_memoIds = null;
		try {
			String sql = "select relative_memo_id from relative_memo_to_open_source_projects where osp_id = " + project_id ; // 鏌ヨ鏁版嵁鐨剆ql璇彞
			Statement st = (Statement) conn.createStatement(); // 鍒涘缓鐢ㄤ簬鎵ц闈欐�乻ql璇彞鐨凷tatement瀵硅薄锛宻t灞炲眬閮ㄥ彉閲�

			rs_memoIds = st.executeQuery(sql); // 鎵цsql鏌ヨ璇彞锛岃繑鍥炴煡璇㈡暟鎹殑缁撴灉闆�
			while(rs_memoIds.next()){
				relativeMemoIds.add(rs_memoIds.getInt("relative_memo_id"));
			}
			rs_memoIds.close();
			st.close();
			// conn.close(); // 鍏抽棴鏁版嵁搴撹繛鎺�
		} catch (SQLException e) {
			System.out.println("读取数据错误！memo ");
			e.printStackTrace();
		}
		return relativeMemoIds;
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
			System.out.println("读取数据错误！memo ");
			e.printStackTrace();
		}
		return rs_memo;
	}
	
	
	public static List<String> getMemoTags(String ids, Connection conn){
		ResultSet rs = null;
		List<String> tagNames = new ArrayList<String>();
		String sql = "select name from tags where id in (?)";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, ids);
			rs = ps.executeQuery();
			while(rs.next()){
				tagNames.add(rs.getString("name"));
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tagNames;
	}
	
	
	public static List<Integer> getTagIds(String memo_ids_str, Connection conn){
		ResultSet rs = null;
		List<Integer> tagIds = new ArrayList<Integer>();
		String sql = "select tag_id from taggings where taggable_id in (?) and taggable_type=?";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, memo_ids_str);
			ps.setString(2, "RelativeMemo");
			rs = ps.executeQuery();
			while(rs.next()){
				tagIds.add(rs.getInt("tag_id"));
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return tagIds;
	}
	
	
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
				projects.add(project);
			}
			rs_project.close();
			st.close();
			// conn.close(); // 鍏抽棴鏁版嵁搴撹繛鎺�
		} catch (SQLException e) {
			System.out.println("读取数据错误！project");
			e.printStackTrace();
		}
		
		return projects;

	}

	public static void insertByBatch(Connection conn) throws SQLException {
		Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
				ResultSet.CONCUR_READ_ONLY);
		PreparedStatement pst = conn.prepareStatement("insert into hot_words(osp_id,name,created_at,updated_at) values (?,?,now(),now())");
	}
	
	
	public static Connection getConnection() {
		if (con == null) {
			try {
				Class.forName("com.mysql.jdbc.Driver");// 鍔犺浇Mysql鏁版嵁椹卞姩

				con = DriverManager.getConnection(
						"jdbc:mysql://localhost:3306/ossean_show?useUnicode=true&amp;characterEncoding=utf-8", "root", "123456");// 鍒涘缓鏁版嵁杩炴帴

			} catch (Exception e) {
				System.out.println("连接错误" + e.getMessage());
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
	public static Boolean insertHotWordsByBatch(Connection conn, List<String> nameList, List<Float> weightList, int osp_id){
		String sql = "insert into hot_words(osp_id,name,weight,created_at,updated_at) values (?,?,?,now(),now())";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			for(int i = 0; i < nameList.size(); i++){
				ps.setInt(1, osp_id);
				ps.setString(2, nameList.get(i));
				ps.setFloat(3, weightList.get(i));
				ps.addBatch();
			}
			ps.executeBatch();
			conn.commit();
			ps.close();
			return true;
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return false;//事务回滚 插入失败
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
				System.out.println(ps2.toString());
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
			e.printStackTrace();
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

}
