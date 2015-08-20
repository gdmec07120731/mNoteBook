package me.july.memory.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;

import java.util.ArrayList;
import java.util.List;

import me.july.bean.Note;
import me.july.memory.R;
import me.july.memory.util.BaseListAdapter;

/**
 * Created by Rc3 on 2015/8/11.
 */
public class View extends Activity implements BaseListAdapter.IAdapterView {
    List<Note> list;
    Context mContext ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view);
         mContext = this;
        list = new ArrayList<Note>();
        for(int i=0;i<20;i++) {
            Note a = new Note(0L, "qigesb", "a", "a");
            list.add(a);
        }
        ListView lv = (ListView) findViewById(R.id.lv);
      //  BaseListAdapter<Note> adapter = new BaseListAdapter<Note>(list, this);

        QuickAdapter<Note> adapter=new QuickAdapter<Note>(this,android.R.layout.simple_list_item_1) {
            @Override
            protected void convert(BaseAdapterHelper helper, Note item) {
                             TextView title=helper.getView(android.R.id.text1);
                title.setText(item.getTitle());
            }
        };
        adapter.addAll(list);
        lv.setAdapter(adapter);


    }

    @Override
    public android.view.View getView(int position, android.view.View convertview, ViewGroup parent) {
        ViewHolder holder;

        if (convertview == null) {
            holder = new ViewHolder();
            convertview = LayoutInflater.from(mContext).inflate(android.R.layout.simple_list_item_1, parent, false);
            holder.title = (TextView) convertview.findViewById(android.R.id.text1);
            convertview.setTag(holder);
        } else {
            holder = (ViewHolder) convertview.getTag();


        }

        holder.title.setText(list.get(position).getTitle());

        return convertview;
    }

    public class ViewHolder {
        TextView title;
    }
}
