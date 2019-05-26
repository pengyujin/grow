package com.xz.app.pojo;

public class Inform_infor extends Inform_inforKey {
    private Byte informed_time;

    private Byte write_time;

    private Byte is_banned;

    private Integer userid;

    public Inform_infor() {
    }

    public Inform_infor(long cyid, Byte type, Byte informed_time, Byte write_time, Byte is_banned, Integer userid) {
		super();
		this.setCyid(cyid);
		this.setType(type);
		this.informed_time = informed_time;
		this.write_time = write_time;
		this.is_banned = is_banned;
		this.userid = userid;
	}

	public Byte getInformed_time() {
        return informed_time;
    }

    public void setInformed_time(Byte informed_time) {
        this.informed_time = informed_time;
    }

    public Byte getWrite_time() {
        return write_time;
    }

    public void setWrite_time(Byte write_time) {
        this.write_time = write_time;
    }

    public Byte getIs_banned() {
        return is_banned;
    }

    public void setIs_banned(Byte is_banned) {
        this.is_banned = is_banned;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }
}