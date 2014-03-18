package nfc.mytools.no;

import java.util.Date;



public class ToolLog2 {
	@com.google.gson.annotations.SerializedName("id")
	public int id;
	
	@com.google.gson.annotations.SerializedName("toolid")
	public String toolid;
	
	@com.google.gson.annotations.SerializedName("outtime")
	public Date outtime;
	
	@com.google.gson.annotations.SerializedName("intime")
	public Date intime;
	
	@com.google.gson.annotations.SerializedName("userid")
	public int userid;
	
	@com.google.gson.annotations.SerializedName("locationid")
	public int locationid;
	
	@com.google.gson.annotations.SerializedName("companyid")
	public int companyid;
	
	@com.google.gson.annotations.SerializedName("statusid")
	public int statusid;
	
	public ToolLog2(){}
	
	public ToolLog2(int _id, String _toolid, Date _outtime, Date _intime, int _userid, int _locationid, int _companyid){
		id = _id;
		toolid = _toolid;
		outtime = _outtime;
		intime = _intime;
		userid = _userid;
		locationid = _locationid;
		companyid = _companyid;
	}

	
	
}
