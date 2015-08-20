package me.july.memory.ui;

import android.app.Activity;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import net.yazeed44.imagepicker.model.ImageEntry;
import net.yazeed44.imagepicker.util.Picker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.dao.query.Query;
import me.july.bean.Note;
import me.july.dao.NoteDao;
import me.july.memory.NoteApplication;
import me.july.memory.R;
import me.july.memory.adapter.RecyclerViewAdapter;


public class MainActivity extends AppCompatActivity {
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.drawerlayout)
    DrawerLayout mDrawerLayout;
    @Bind(R.id.recyclerview)
    RecyclerView mRecyclerView;
    private ActionBarDrawerToggle toggle;
    private ArrayList<Note> mList;
    private RecyclerViewAdapter mAdapter;
    private Context mContext;
    private SearchView mSearchView;
    @Bind(R.id.coordinator)
    CoordinatorLayout mCoordinatorLayout;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    private NoteDao mNoteDao;
    @Bind(R.id.layout_bg)
    LinearLayout mLinearLayout;
    private SharedPreferences spf;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        ButterKnife.bind(this);


        setSupportActionBar(mToolbar);


        toggle = new ActionBarDrawerToggle(MainActivity.this, mDrawerLayout, mToolbar, 0, 0);

        mDrawerLayout.setDrawerListener(toggle);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }

    private void initViews() {
        mContext = this;
        spf = mContext.getSharedPreferences("img", Activity.MODE_PRIVATE);
        mList = new ArrayList<Note>();
        //  mLinearLayout = (LinearLayout) findViewById(R.id.layout_bg);
        //  mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator);
        //  fab = (FloatingActionButton) findViewById(R.id.fab);
        //  mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        //  mToolbar = (Toolbar) findViewById(R.id.toolbar);
        //  mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);


    }

    public NoteDao getNoteDao() {
        return ((NoteApplication) this.getApplicationContext()).getDaoSession().getNoteDao();

    }

    private void initDatas() {
        mNoteDao = getNoteDao();
        Query query = mNoteDao.queryBuilder().build();

        List notes = query.list();
        Iterator it = notes.iterator();
        while (it.hasNext()) {

            Note a = (Note) it.next();

            Note note = new Note();
            note.setId(a.getId());
            note.setTitle(a.getTitle());
            note.setContent(a.getContent());
            note.setDate(a.getDate());
            mList.add(note);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        String imgName = spf.getString("img", "");
        if (null != imgName && !"".equals(imgName)) {
            mLinearLayout.setBackgroundDrawable(new BitmapDrawable(getDrawableFromBitmap(spf.getString("img", ""))));
        } else {

            mLinearLayout.setBackgroundResource(R.drawable.bg);
        }
        mList.clear();
        initDatas();
        Collections.reverse(mList);
        mAdapter = new RecyclerViewAdapter(mContext, mList);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NoteActivity.class);


                intent.putExtra("qige", 2);

                startActivity(intent);
            }
        });
        mAdapter.setRecyclerViewListener(new RecyclerViewAdapter.RecyclerViewListener() {
            @Override
            public void itemClick(View view, int position) {
                Intent intent = new Intent(MainActivity.this, NoteActivity.class);

                intent.putExtra("note", mList.get(position));
                intent.putExtra("qige", 1);

                startActivity(intent);
            }

            @Override
            public void itemLongClick(View view, int position) {
                mAdapter.deleteItem(position);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        ComponentName componentName = getComponentName();
        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(componentName));
        mSearchView.setQueryHint("find");
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mAdapter.getFilter().filter(newText);
                return false;
            }
        });

        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {

                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                mList.clear();
                initDatas();
                Collections.reverse(mList);

                mAdapter = new RecyclerViewAdapter(mContext, mList);
                mRecyclerView.setAdapter(mAdapter);
                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Snackbar.make(mCoordinatorLayout, "设置", Snackbar.LENGTH_SHORT).show();
            new Picker.Builder(MainActivity.this, new MyPickerListener(), R.style.noActionbar).setPickMode(Picker.PickMode.MULTIPLE_IMAGES).build().startActivity();


            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class MyPickerListener implements Picker.PickListener {
        @Override
        public void onPickedSuccessfully(ArrayList<ImageEntry> arrayList) {


            mLinearLayout.setBackgroundDrawable(new BitmapDrawable(getDrawableFromBitmap(((ImageEntry) arrayList.get(0)).path)));


            SharedPreferences.Editor editor = spf.edit();
            editor.putString("img", ((ImageEntry) arrayList.get(0)).path);
            editor.commit();
        }

        @Override
        public void onCancel() {

        }
    }


    private Bitmap getDrawableFromBitmap(String file) {
        BitmapFactory.Options option = new BitmapFactory.Options();

        option.inSampleSize = 1;

        Bitmap bitmap = BitmapFactory.decodeFile(file, option);

        return bitmap;
    }
}
