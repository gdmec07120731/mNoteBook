package com.example;

import java.io.IOException;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class GreenDaoGenerator {


    public static void main(String[] args) throws Exception {

        Schema schema = new Schema(1, "me.july.bean");
        schema.setDefaultJavaPackageDao("me.july.dao");


        addNote(schema);
        new DaoGenerator().generateAll(schema, "D:\\Git\\Memory\\app\\src\\main\\java-gen");
    }

    private static void addNote(Schema schema) {

        Entity note=schema.addEntity("Note");
        note.addIdProperty();
        note.addStringProperty("title").notNull();
        note.addStringProperty("content");
        note.addStringProperty("date");

    }
}
