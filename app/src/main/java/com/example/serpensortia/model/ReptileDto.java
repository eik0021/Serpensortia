package com.example.serpensortia.model;

import com.activeandroid.annotation.Column;

public class ReptileDto {
    private long reptileId;

    private String name = null;

    private String species = null;

    private String sexType = null;

    private String birthDay = null;

    private String image = null;

    private String qrcode = null;

    private String nfcCode = null;

    private GroupDto group = null;

    public ReptileDto(Reptile reptile) {
        this.reptileId = reptile.getId();
        this.name = reptile.name;
        this.species = reptile.species;
        this.sexType = reptile.sexType;
        this.birthDay = reptile.birthDay;
        this.image = reptile.image;
        this.qrcode = reptile.qrcode;
        this.nfcCode = reptile.nfcCode;
        this.group = reptile.group.getDto();
    }

    public long getReptileId() {
        return reptileId;
    }

    public void setReptileId(long reptileId) {
        this.reptileId = reptileId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getSexType() {
        return sexType;
    }

    public void setSexType(String sexType) {
        this.sexType = sexType;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public String getNfcCode() {
        return nfcCode;
    }

    public void setNfcCode(String nfcCode) {
        this.nfcCode = nfcCode;
    }

    public GroupDto getGroup() {
        return group;
    }

    public void setGroup(GroupDto group) {
        this.group = group;
    }
}
