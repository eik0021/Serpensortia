package com.example.serpensortia.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

@Table(name = "Reptile")
public class Reptile extends Model {

    @Column(name = "reptile_name")
    public String name = null;

    @Column(name = "reptile_species")
    public String species = null;

    @Column(name = "reptile_sex_type")
    public String sexType = null;

    @Column(name = "reptile_birth_day")
    public String birthDay = null;

    @Column(name = "reptile_image")
    public String image = null;

    @Column(name = "reptile_qrcode")
    public String qrcode = null;

    @Column(name = "reptile_nfc_code")
    public String nfcCode = null;

    @Column(name = "GroupAnimal", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    public Group group = null;

    public Reptile() {
        super();
    }

    public static Reptile findByName(String name){
        return new Select().from(Reptile.class).where("reptile_name = ?", name).executeSingle();
    }

}
