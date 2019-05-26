package com.xz.app.pojo;

public class ReadcardKey {
    private Integer fromid;

    private Integer toid;

    public ReadcardKey() {
        // Auto-generated constructor stub
    }
    public ReadcardKey(Integer fromid, Integer toid) {
		super();
		this.fromid = fromid;
		this.toid = toid;
	}

	public Integer getFromid() {
        return fromid;
    }

    public void setFromid(Integer fromid) {
        this.fromid = fromid;
    }

    public Integer getToid() {
        return toid;
    }

    public void setToid(Integer toid) {
        this.toid = toid;
    }
}