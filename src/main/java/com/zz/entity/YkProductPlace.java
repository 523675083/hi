package com.zz.entity;

public class YkProductPlace {
    private Long id;
    //产品id
    private Long pId;
    //目的地id
    private Long placeId;
    //大类id
    private Integer bigCategoryId;
    
    private Long createTime;

    private Integer isDel;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getpId() {
        return pId;
    }

    public void setpId(Long pId) {
        this.pId = pId;
    }

    public Long getPlaceId() {
        return placeId;
    }

    public void setPlaceId(Long placeId) {
        this.placeId = placeId;
    }

    public Integer getBigCategoryId() {
        return bigCategoryId;
    }

    public void setBigCategoryId(Integer bigCategoryId) {
        this.bigCategoryId = bigCategoryId;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

	public Integer getIsDel() {
		return isDel;
	}

	public void setIsDel(Integer isDel) {
		this.isDel = isDel;
	}


}