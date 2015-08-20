package me.july.memory;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import me.july.dao.DaoMaster;
import me.july.dao.DaoSession;

/**
 * Created by Rc3 on 2015/8/4.
 */
public class NoteApplication extends Application {
    public SQLiteDatabase db;
    public DaoMaster mDaoMaster;
    public DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "note-db", null);
        db = helper.getWritableDatabase();
        mDaoMaster = new DaoMaster(db);
        daoSession = mDaoMaster.newSession();
    }

    public DaoSession getDaoSession() {

        return daoSession;
    }

    public SQLiteDatabase getdb() {

        return db;
    }



}
