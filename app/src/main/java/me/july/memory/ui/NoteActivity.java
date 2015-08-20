package me.july.memory.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import me.july.bean.Note;
import me.july.dao.NoteDao;
import me.july.memory.NoteApplication;
import me.july.memory.R;

/**
 * Created by Rc3 on 2015/8/5.
 */
public class NoteActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private EditText mTitle;
    private EditText mContent;
    private NoteDao mNoteDao;
    private Cursor mCursor;
    private int type = 0;
    private Note mNote;
    private boolean isShowDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);


        initView();


    }

    private NoteDao getNoteDao() {

        return ((NoteApplication) this.getApplicationContext()).getDaoSession().getNoteDao();
    }


    private void initView() {
        mNoteDao = getNoteDao();
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mTitle = (EditText) findViewById(R.id.title);
        mContent = (EditText) findViewById(R.id.content);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();

        switch (intent.getExtras().getInt("qige")) {


            case 1:
                type = 1;
                mToolbar.setTitle("查看内容");
                mNote = (Note) (intent.getExtras().getParcelable("note"));
                mTitle.setText(mNote.getTitle());
                mContent.setText(mNote.getContent());


                isShowDialog = false;
                mTitle.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (mToolbar != null && hasFocus) {
                            mToolbar.setTitle("修改内容");
                            isShowDialog = true;
                        }
                    }
                });
                break;
            case 2:
                mToolbar.setTitle("编辑内容");
                isShowDialog=true;
                break;
        }


        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mToolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShowDialog ) {
                    toFinish();
                } else {
                    finish();
                }
            }
        });


        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_add) {
                    if (!"".equals(mTitle.getText().toString())) {


                        save();
                        finish();

                    }

                }


                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_note, menu);


        return super.onCreateOptionsMenu(menu);
    }


    private void toFinish() {
        if (!"".equals(mTitle.getText().toString())||! "".equals(mContent.getText().toString())) {
            new AlertDialog.Builder(this).setMessage(getString(R.string.savenote))
                   .setPositiveButton(getString(R.string.save), new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialog, int which) {

                           save();
                           dialog.dismiss();
                           finish();

                       }
                   }).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    finish();
                }
            }).show();


        } else {

            finish();
        }


    }

    private void save() {
        Calendar mCalendar = Calendar.getInstance();

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd HH:mm");


        if (type == 1) {
            Note note = new Note(mNote.getId(), mTitle.getText().toString(), mContent.getText().toString(), sdf.format(mCalendar.getTime()));
            mNoteDao.update(note);
        } else {
            Note note = new Note(null, mTitle.getText().toString(), mContent.getText().toString(), sdf.format(mCalendar.getTime()));
            mNoteDao.insert(note);
        }


    }


    @Override
    public void onBackPressed() {

        if (isShowDialog) {
            toFinish();
        } else {
            finish();
        }

    }


}
