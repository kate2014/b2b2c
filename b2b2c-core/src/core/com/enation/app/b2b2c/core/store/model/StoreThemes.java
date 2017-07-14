package com.enation.app.b2b2c.core.store.model;

/**
 * 店铺模板
 * @author Kanon
 *
 */
public class StoreThemes {

	private Integer id;		//id
	private String name;	//名称
	private String path;	//目录
	private Integer is_default;	//是否为默认
	
	//get set
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public Integer getIs_default() {
		return is_default;
	}
	public void setIs_default(Integer is_default) {
		this.is_default = is_default;
	}
}
