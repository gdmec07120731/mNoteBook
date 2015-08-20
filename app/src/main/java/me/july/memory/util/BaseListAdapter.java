package me.july.memory.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by Rc3 on 2015/8/11.
 */
public class BaseListAdapter<T> extends BaseAdapter {

    private List<T> mList;
    private LayoutInflater mLayoutInflater;
    private IAdapterView itemView;

    public interface IAdapterView {
        public View getView(int position, View convertview, ViewGroup parent);
    }

    public BaseListAdapter(List<T> list, IAdapterView itemView) {
        super();
        this.mList = list;

        this.itemView = itemView;

    }



    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        return itemView.getView(position,convertView,parent);
    }
}
