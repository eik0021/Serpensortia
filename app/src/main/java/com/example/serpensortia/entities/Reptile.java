package com.example.serpensortia.entities;

import com.orm.SugarRecord;
import com.orm.dsl.Column;
import com.orm.dsl.Unique;

public class Reptile extends SugarRecord {
    @Unique
    @Column(name = "reptile_id")
    private Long reptileId;

    @Column(name = "reptile_name")
    private String name=null;

    @Column(name = "reptile_species")
    private String species=null;

    @Column(name = "reptile_sex_type")
    private String sexType=null;

    @Column(name = "reptile_birth_day")
    private Long birthDay=null;

    @Column(name = "reptile_image")
    private String image=null;

    @Column(name = "reptile_qrcode")
    private String qrcode=null;

    @Column(name = "reptile_nfc_code")
    private String nfcCode=null;

    private Group group=null;

    public Reptile() {
    }

    public Reptile(String name, String species, String sexType, Long birthDay, String image, String qrcode, String nfcCode, Group group) {
        this.name = name;
        this.species = species;
        this.sexType = sexType;
        this.birthDay = birthDay;
        this.image = image;
        this.qrcode = qrcode;
        this.nfcCode = nfcCode;
        this.group = group;
    }

    public Long getReptileId() {
        return reptileId;
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

    public Long getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Long birthDay) {
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

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
}
