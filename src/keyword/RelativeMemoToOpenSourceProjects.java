package keyword;

import java.util.List;

public class RelativeMemoToOpenSourceProjects {
	
	private int id;
	private int osp_id;
	private int relative_memo_id;
	private float match_weight;
	private String created_time;
	private List<Integer> tagIds;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getOsp_id() {
		return osp_id;
	}
	public void setOsp_id(int osp_id) {
		this.osp_id = osp_id;
	}
	public int getRelative_memo_id() {
		return relative_memo_id;
	}
	public void setRelative_memo_id(int relative_memo_id) {
		this.relative_memo_id = relative_memo_id;
	}
	public float getMatch_weight() {
		return match_weight;
	}
	public void setMatch_weight(float match_weight) {
		this.match_weight = match_weight;
	}
	public String getCreated_time() {
		return created_time;
	}
	public void setCreated_time(String created_time) {
		this.created_time = created_time;
	}
	public List<Integer> getTagIds() {
		return tagIds;
	}
	public void setTagIds(List<Integer> tagIds) {
		this.tagIds = tagIds;
	}
	

}
