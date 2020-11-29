package com.example.serpensortia.entities;

import com.orm.SugarRecord;
import com.orm.dsl.Column;
import com.orm.dsl.Unique;

public class Group extends SugarRecord {
  /*  @Unique
    @Column(name = "group_id")
    private Long groupId;
*/
    @Column(name = "group_name")
    private String name;


    public Group() {
    }

    public Group(String name) {
        this.name = name;
    }
/*
    public Long getGroupId() {
        return groupId;
    }*/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
