package keyword;

import java.util.ArrayList;
import java.util.List;

public class Project {
	
	private int id;
	private String name;
	private String description;
	private int followers_num;
	private String url;
	private String language;
	private int download_num;
	private int view_new_crawled;
	private String category;
	private String crawled_time;
	private String source;
	private int view_num_local;
	private String created_at;
	private String updated_at;
	private int composite_score;
	private int relative_memos_num;
	private String created_time;
	private String updated_time;
	private int view_num;
	private int activeness;
	private String tags;
	
	
	public List<String> getTagList(){
		List<String> result = new ArrayList<String>();
		String tmp = this.tags;
		if(tmp != null && !"".equals(tmp)){
			String[] strings = tmp.split(",");
			for(String str:strings){
				int index1 = str.indexOf("<");
				int index2 = str.indexOf(">");
				if(index2 > index1 && index1 >= 0){
					//读取当前的标签
					String tag = str.substring(index1 + 1, index2);
					if(!"".equals(tag) )
						result.add(tag);
				}
				
			}
		}
		return result;
	}
	
	
	public String changeTagListToStr(List<String> tags){
		String result = "";
		for(String tag:tags){
			result += "<"+tag+">,";
		}
		if(!"".equals(result)){
			result = result.substring(0, result.length()-1);
		}else{
			result = null;
		}
		return result;
	}
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getFollowers_num() {
		return followers_num;
	}
	public void setFollowers_num(int followers_num) {
		this.followers_num = followers_num;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public int getDownload_num() {
		return download_num;
	}
	public void setDownload_num(int download_num) {
		this.download_num = download_num;
	}
	public int getView_new_crawled() {
		return view_new_crawled;
	}
	public void setView_new_crawled(int view_new_crawled) {
		this.view_new_crawled = view_new_crawled;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getCrawled_time() {
		return crawled_time;
	}
	public void setCrawled_time(String crawled_time) {
		this.crawled_time = crawled_time;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public int getView_num_local() {
		return view_num_local;
	}
	public void setView_num_local(int view_num_local) {
		this.view_num_local = view_num_local;
	}
	public String getCreated_at() {
		return created_at;
	}
	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}
	public String getUpdated_at() {
		return updated_at;
	}
	public void setUpdated_at(String updated_at) {
		this.updated_at = updated_at;
	}
	public int getRelative_memos_num() {
		return relative_memos_num;
	}
	public void setRelative_memos_num(int relative_memos_num) {
		this.relative_memos_num = relative_memos_num;
	}
	public String getCreated_time() {
		return created_time;
	}
	public void setCreated_time(String created_time) {
		this.created_time = created_time;
	}
	public String getUpdated_time() {
		return updated_time;
	}
	public void setUpdated_time(String updated_time) {
		this.updated_time = updated_time;
	}
	public int getComposite_score() {
		return composite_score;
	}
	public void setComposite_score(int composite_score) {
		this.composite_score = composite_score;
	}
	public int getView_num() {
		return view_num;
	}
	public void setView_num(int view_num) {
		this.view_num = view_num;
	}
	public int getActiveness() {
		return activeness;
	}
	public void setActiveness(int activeness) {
		this.activeness = activeness;
	}
	public String getTags() {
		return tags;
	}
	public void setTags(String tags) {
		this.tags = tags;
	}
}
