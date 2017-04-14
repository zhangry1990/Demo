package com.zhangry.demo.data.hibernate;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by zhangry on 2017/3/16.
 */
@MappedSuperclass
public abstract class BaseEntity implements Serializable {
    @GenericGenerator(
            name = "generator",
            strategy = "uuid.hex"
    )
    @Id
    @GeneratedValue(
            generator = "generator"
    )
    @Column(
            name = "ID",
            unique = true,
            nullable = false,
            length = 32
    )
    protected String id;
    @Column(
            name = "CREATED_USER_ID"
    )
    protected String createdUserId;
    @Column(
            name = "CREATED_USER_NAME"
    )
    protected String createdUserName;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(
            name = "CREATED_TIME",
            nullable = false
    )
    protected Date createdTime;
    @Column(
            name = "MODIFIED_USER_ID"
    )
    protected String modifiedUserId;
    @Column(
            name = "MODIFIED_USER_NAME"
    )
    protected String modifiedUserName;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(
            name = "MODIFIED_TIME"
    )
    protected Date modifiedTime;
    @Column(
            name = "DELETED_USER_ID"
    )
    protected String deletedUserId;
    @Column(
            name = "DELETED_USER_NAME"
    )
    protected String deletedUserName;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(
            name = "DELETED_TIME"
    )
    protected Date deletedTime;
    @Column(
            name = "DELETED_FLAG",
            nullable = false,
            length = 1
    )
    protected Integer deletedFlag = Integer.valueOf(0);

    public BaseEntity() {
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreatedTime() {
        return this.createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getModifiedTime() {
        return this.modifiedTime;
    }

    public void setModifiedTime(Date modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    public Date getDeletedTime() {
        return this.deletedTime;
    }

    public void setDeletedTime(Date deletedTime) {
        this.deletedTime = deletedTime;
    }

    public Integer getDeletedFlag() {
        return this.deletedFlag;
    }

    public void setDeletedFlag(Integer deletedFlag) {
        this.deletedFlag = deletedFlag;
    }

    public String getCreatedUserId() {
        return this.createdUserId;
    }

    public void setCreatedUserId(String createdUserId) {
        this.createdUserId = createdUserId;
    }

    public String getCreatedUserName() {
        return this.createdUserName;
    }

    public void setCreatedUserName(String createdUserName) {
        this.createdUserName = createdUserName;
    }

    public String getModifiedUserId() {
        return this.modifiedUserId;
    }

    public void setModifiedUserId(String modifiedUserId) {
        this.modifiedUserId = modifiedUserId;
    }

    public String getModifiedUserName() {
        return this.modifiedUserName;
    }

    public void setModifiedUserName(String modifiedUserName) {
        this.modifiedUserName = modifiedUserName;
    }

    public String getDeletedUserId() {
        return this.deletedUserId;
    }

    public void setDeletedUserId(String deletedUserId) {
        this.deletedUserId = deletedUserId;
    }

    public String getDeletedUserName() {
        return this.deletedUserName;
    }

    public void setDeletedUserName(String deletedUserName) {
        this.deletedUserName = deletedUserName;
    }

    public String toString() {
        return "BaseEntity{id='" + this.id + '\'' + ", createdUserId='" + this.createdUserId + '\'' + ", createdUserName='" + this.createdUserName + '\'' + ", createdTime=" + this.createdTime + ", modifiedUserId='" + this.modifiedUserId + '\'' + ", modifiedUserName='" + this.modifiedUserName + '\'' + ", modifiedTime=" + this.modifiedTime + ", deletedUserId='" + this.deletedUserId + '\'' + ", deletedUserName='" + this.deletedUserName + '\'' + ", deletedTime=" + this.deletedTime + ", deletedFlag=" + this.deletedFlag + '}';
    }
}

