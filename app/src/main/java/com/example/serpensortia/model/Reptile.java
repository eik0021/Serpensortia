package com.example.serpensortia.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

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

    public static Reptile findByQR(String qrCode){
        return new Select().from(Reptile.class).where("reptile_qrcode = ?", qrCode).executeSingle();
    }

    public static Reptile findById(long id){
        return new Select().from(Reptile.class).where("id = ?", id).executeSingle();
    }

    public static List<Reptile> findAll() {
        return new Select().from(Reptile.class).orderBy("id DESC").execute();
    }

    public static List<Reptile> findByGroup(long group_id) {
        return new Select().from(Reptile.class).where("GroupAnimal = ?", group_id).execute();
    }

    public List<Action> actions() {
        return getMany(Action.class, "action_reptile");
    }

    public List<Feeding> feedings() {
        return getMany(Feeding.class, "feeding_reptile");
    }
}
