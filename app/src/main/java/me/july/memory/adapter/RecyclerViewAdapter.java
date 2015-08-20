package me.july.memory.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import me.july.bean.Note;
import me.july.dao.NoteDao;
import me.july.memory.NoteApplication;
import me.july.memory.R;

/**
 * Created by Rc3 on 2015/8/4.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> implements Filterable {
    private Context mContext;
    private NoteDao mDaoMaster;
    private List<Note> mList;
    private NoteFilter mNoteFilter;

    public RecyclerViewAdapter(Context context, ArrayList<Note> list) {
        this.mContext = context;
        this.mList = list;
        this.mDaoMaster = getNoteDao();

    }

    private NoteDao getNoteDao() {

        return ((NoteApplication) mContext.getApplicationContext()).getDaoSession().getNoteDao();
    }


    public interface RecyclerViewListener {

        void itemClick(View view, int position);

        void itemLongClick(View view, int position);
    }

    public RecyclerViewListener listener;

    public void setRecyclerViewListener(RecyclerViewListener listener) {

        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.item_recyclerview, parent, false);
        MyViewHolder mHolder = new MyViewHolder(view);
        return mHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.mTitle.setText(mList.get(position).getTitle());
        Linkify.addLinks(holder.mTitle, Linkify.WEB_URLS);
        holder.mTitle.setMovementMethod(LinkMovementMethod.getInstance());
        holder.mContent.setText(mList.get(position).getContent());
        holder.mDate.setText(mList.get(position).getDate());
        setOnClick(holder);

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView mTitle;
        private TextView mContent;
        private TextView mDate;


        public MyViewHolder(View itemView) {
            super(itemView);

            mTitle = (TextView) itemView.findViewById(R.id.ed_title);
            mContent = (TextView) itemView.findViewById(R.id.tv_content);
            mDate = (TextView) itemView.findViewById(R.id.tv_date);

        }


    }

    protected void setOnClick(final MyViewHolder holder) {
        if (null != listener) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.itemClick(holder.itemView, holder.getLayoutPosition());
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.itemLongClick(holder.itemView, holder.getLayoutPosition());
                    return true;
                }
            });

        }


    }


    public void deleteItem(int pos) {


        mDaoMaster.deleteByKey(mList.get(pos).getId());
        mList.remove(pos);
        notifyItemRemoved(pos);


    }

    @Override
    public Filter getFilter() {
        if (mNoteFilter == null) {
            mNoteFilter = new NoteFilter(this, mList);

        }


        return mNoteFilter;

    }

    private static class NoteFilter extends Filter {

        private RecyclerViewAdapter mRecyclerViewAdapter;
        private List<Note> originalList;
        private List<Note> filterList;


        public NoteFilter(RecyclerViewAdapter adapter, List<Note> originalList) {
            super();
            this.mRecyclerViewAdapter = adapter;
            this.originalList = originalList;
            this.filterList = new ArrayList<Note>();

        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            filterList.clear();
            FilterResults results = new FilterResults();
            if (constraint.length() == 0) {
                filterList.addAll(originalList);

            } else {

                for (Note note : originalList) {

                    if (note.getContent().contains(constraint) || note.getTitle().contains(constraint)) {


                        filterList.add(note);
                    }
                }
            }

            results.values = filterList;
            results.count = filterList.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            mRecyclerViewAdapter.mList.clear();
            mRecyclerViewAdapter.mList.addAll((ArrayList<Note>) results.values);
            mRecyclerViewAdapter.notifyDataSetChanged();

        }
    }

}
