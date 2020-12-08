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

    @Override
    public String toString() {
        return name;
    }

    public void deleteGroup(){
        List<Reptile> reptiles = reptiles();
        for (Reptile r : reptiles) {
            Feeding.deleteByReptileId(r.getId());
            Action.deleteByReptileId(r.getId());
            r.delete();
        }
        super.delete();
    }

    public List<Reptile> reptiles() {
        return getMany(Reptile.class, "GroupAnimal");
    }

    public static Group findByName(String name){
        return new Select().from(Group.class).where("group_name = ?", name).executeSingle();
    }

    public static Group findById(long id){
        return new Select().from(Group.class).where("id = ?", id).executeSingle();
    }

    public static List<Group> getAllGroups() {
        return new Select().from(Group.class).orderBy("id DESC").execute();
    }

    public static List<Group> findAll() {
        return new Select().from(Group.class).orderBy("group_name DESC").execute();
    }

}
