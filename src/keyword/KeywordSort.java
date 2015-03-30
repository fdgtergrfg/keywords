package keyword;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

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

	private static int keyword_count = 20;
	private static int titleword_length = 2;
	private static int word_length = 2;
	private static int batchSize = 500;// 一次处理项目数量
	private static int startId = 1;// 读取数据库中上次处理到的项目id
	private static String SourceTableName = "open_source_projects";
	private static String TargetTableName = "hot_words";
	private static Boolean insertResult = false;

	public static void main(String[] args) throws SQLException {

		Connection conn;
		conn = (Connection) GetDate.getConnection();
		conn.setAutoCommit(false);
		while (true) {
			startId = GetDate.readPointer(conn, SourceTableName,
					TargetTableName);
			List<Project> projects = GetDate.getProject(conn, startId, batchSize);
			int project_id = 0;
			String memo_ids_str = "";
			String tags = "";
			String out = "";
			for (Project project:projects) {
				project_id = project.getId();
				List<Integer> relativeMemoIds = GetDate.getMemoIds(project_id, conn);
				for (Integer id:relativeMemoIds) {
					memo_ids_str += id + ",";
				}
				if (memo_ids_str.length() > 0) {
					memo_ids_str = memo_ids_str.substring(0,
							memo_ids_str.length() - 1);
					// rs_memo = GetDate.getMemo(memo_ids_str.substring(0,
					// memo_ids_str.length()-1), conn);
					List<Integer> tagIdList = GetDate.getTagIds(memo_ids_str, conn);
					String tagIds = "";
					for (Integer id:tagIdList) {
						tagIds += id + ",";
					}
					if (tagIds.length() > 0)
						tagIds = tagIds.substring(0, tagIds.length() - 1);
					else{
						//没有找到任何tag
						GetDate.updatePointer(conn, SourceTableName,
								TargetTableName, project_id + 1);
						continue;
					}
					List<String> tagNames = GetDate.getMemoTags(tagIds, conn);
					for (String tagName:tagNames) {
						// title += rs_memo.getString("subject");
						tags += tagName + "|,|";
					}
					if (tags.length() >= 3)
						tags = tags.substring(0, tags.length() - 3);
				} else {
					GetDate.updatePointer(conn, SourceTableName,
							TargetTableName, project_id + 1);
					continue;
				}
				if (tags.length() > 0) {
					out = getsubkeywordtitle(tags);

					// 将热词存入数据库
					// 将out处理为两个List
					String[] string = out.split("\\|;\\|");
					List<String> nameList = new ArrayList<String>();
					List<Float> weightList = new ArrayList<Float>();
					for (String str : string) {
						String[] tmp = str.split("\\|,\\|");
						nameList.add(tmp[0]);
						weightList.add(Float.parseFloat(tmp[1]));
					}
					insertResult = GetDate.insertHotWordsByBatch(conn,
							nameList, weightList, project_id);
					if (insertResult) {
						// 如果插入成功 修改pointers表中的指针
						GetDate.updatePointer(conn, SourceTableName,
								TargetTableName, project_id + 1);
					}
					memo_ids_str = "";
					tags = "";
				} else{
					//没有标签
					GetDate.updatePointer(conn, SourceTableName,
							TargetTableName, project_id + 1);
				}
			}//while循环结尾

		}

	}

	/**
	 * @param args
	 */
	public static String getsubkeywordtitle(String testString) {
		// Analyzer ika = null;
		// ika = new PaodingAnalyzer();
		// ika = new IKAnalyzer();
		List<String> keys = null;
		keys = new ArrayList();

		String[] strings = testString.split("\\|,\\|");
		for (String str : strings)
			keys.add(str);

		/*
		 * try { Reader r = null; r = new StringReader(testString); IKSegmenter
		 * ika = new IKSegmenter(r, true);
		 * 
		 * Lexeme term = null; while ((term = ika.next()) != null) { if
		 * (term.getLexemeText().length() >= titleword_length &&
		 * !term.equals("nbsp")) keys.add(term.getLexemeText()); //
		 * System.out.print(term.getLexemeText() + "|"); }
		 * 
		 * ika = null; r.close(); } catch (IOException e) { e.printStackTrace();
		 * }
		 */

		Map<String, Integer> keyMap = new HashMap<String, Integer>();
		Integer $ = null;
		for (String key : keys) {
			keyMap.put(key, ($ = keyMap.get(key)) == null ? 1 : $ + 1);
		}

		List<Map.Entry<String, Integer>> keyList = new ArrayList<Map.Entry<String, Integer>>(
				keyMap.entrySet());
		// ÅÅÐòÇ°
		// for (int i = 0; i < infoIds.size(); i++) {
		// String id = infoIds.get(i).toString();
		// System.out.println(id);
		// }
		// ÅÅÐò
		Collections.sort(keyList, new Comparator<Map.Entry<String, Integer>>() {
			public int compare(Map.Entry<String, Integer> o1,
					Map.Entry<String, Integer> o2) {
				return (o2.getValue() - o1.getValue());
			}
		});
		// ÅÅÐòºó
		testString = "";
		String cc = "";
		int weight = 0;
		for (int i = 0; i < keyList.size() && i < keyword_count; i++) {
			cc = keyList.get(i).toString();
			weight = keyList.get(i).getValue();

			cc = cc.substring(0, cc.lastIndexOf("=")) + "|,|" + weight + "|;|";
			testString = testString + cc;
		}
		if (testString.length() > 6) {
			testString = testString.substring(0, testString.length() - 3);
		}

		keyList.clear();
		keyMap.clear();
		return testString;
	}

	public static String getsubkeyword(String testString) {
		// Analyzer ika = null;
		// ika = new PaodingAnalyzer();
		// ika = new IKAnalyzer();
		List<String> keys = null;
		keys = new ArrayList();
		try {
			Reader r = null;
			r = new StringReader(testString);

			IKSegmenter ika = new IKSegmenter(r, true);

			Lexeme term = null;
			while ((term = ika.next()) != null) {
				if (term.getLexemeText().length() >= titleword_length
						&& !term.equals("nbsp"))
					keys.add(term.getLexemeText());
				// System.out.print(term.getLexemeText() + "|");
			}
			//
			// TokenStream ts = ika.tokenStream("TestField", r);
			//
			// CharTermAttribute term =
			// ts.addAttribute(CharTermAttribute.class);
			// ts.reset();
			// while (ts.incrementToken()) {
			// if (term.length() >= word_length && !term.equals("nbsp"))
			// keys.add(term.toString());
			// }
			//
			// ts.close();

			r.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Map<String, Integer> keyMap = new HashMap<String, Integer>();
		Integer $ = null;
		for (String key : keys) {
			keyMap.put(key, ($ = keyMap.get(key)) == null ? 1 : $ + 1);
		}

		List<Map.Entry<String, Integer>> keyList = new ArrayList<Map.Entry<String, Integer>>(
				keyMap.entrySet());
		// ÅÅÐòÇ°
		// for (int i = 0; i < infoIds.size(); i++) {
		// String id = infoIds.get(i).toString();
		// System.out.println(id);
		// }
		// ÅÅÐò
		Collections.sort(keyList, new Comparator<Map.Entry<String, Integer>>() {
			public int compare(Map.Entry<String, Integer> o1,
					Map.Entry<String, Integer> o2) {
				return (o2.getValue() - o1.getValue());
			}
		});
		// ÅÅÐòºó
		testString = "";
		String cc = "";
		for (int i = 0; i < keyList.size() && i < keyword_count; i++) {
			cc = keyList.get(i).toString();
			cc = cc.substring(0, cc.lastIndexOf("=")) + ",";
			testString = testString + cc;
		}
		if (testString.length() > 2) {
			testString = testString.substring(0, testString.length() - 1);
		}

		return testString;
	}
}