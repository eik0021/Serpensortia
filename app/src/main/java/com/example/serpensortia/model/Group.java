package com.example.serpensortia.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

@Table(name = "GroupTable")
public class Group extends Model {
    @Column(name = "group_name", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    public String name;

    public Group() {
        super();
    }

    public static Group findByName(String name){
        return new Select().from(Group.class).where("group_name = ?", name).executeSingle();
    }

    public static List<Group> getAllGroups() {
        return new Select().from(Group.class).orderBy("id DESC").execute();
    }

}
