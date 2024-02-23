package com.student.pojo.vo;

import java.util.List;

/**
 * 多级树节点构建
 * @author RJD
 */
public class TreeNodeVo<H>{
    private Object id;
    private Object parentId;
    private List<H> children;
    public void add(H child){
    children.add(child);
    }

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public Object getParentId() {
        return parentId;
    }

    public void setParentId(Object parentId) {
        this.parentId = parentId;
    }

    public List<H> getChildren() {
        return children;
    }

    public void setChildren(List<H> children) {
        this.children = children;
    }
}
