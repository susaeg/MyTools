package nfc.mytools.no;

public class Tool {
	@com.google.gson.annotations.SerializedName("id")
	public int id;
	
	@com.google.gson.annotations.SerializedName("toolid")
	public String toolid;
	
	@com.google.gson.annotations.SerializedName("name")
	public String name;
	
	@com.google.gson.annotations.SerializedName("tooltypeid")
	public int tooltypeid;
	
	@com.google.gson.annotations.SerializedName("statusid")
	public int statusid;
	
	@com.google.gson.annotations.SerializedName("description")
	public String description;
	
	@com.google.gson.annotations.SerializedName("image")
	public String image;
	
	@com.google.gson.annotations.SerializedName("locationid")
	public int locationid;

	@com.google.gson.annotations.SerializedName("username")
	public String username;
	
	public Tool(){}
	
	public Tool(String _id, String _name, int _tooltypeid, String _desc){
		toolid = _id;
		name = _name;
		tooltypeid = _tooltypeid;
		description = _desc;
	}

	public String getTitle() {
		return name;
	}
}


