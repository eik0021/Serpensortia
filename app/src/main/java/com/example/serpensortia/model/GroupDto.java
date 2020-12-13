package com.example.serpensortia.model;

public class GroupDto implements DtoSaveable{
    private String name;
    private long groupId;

    public GroupDto(Group group) {
        this.name = group.name;
        this.groupId = group.getId();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getGroup_id() {
        return groupId;
    }

    public void setGroup_id(long group_id) {
        this.groupId = group_id;
    }

    @Override
    public void saveModel() {
        Group group = new Group();
        group.name = this.name;
        group.save();
    }
}
