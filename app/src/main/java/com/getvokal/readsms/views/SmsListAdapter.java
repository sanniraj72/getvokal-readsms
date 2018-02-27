package com.getvokal.readsms.views;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.getvokal.readsms.R;
import com.getvokal.readsms.model.Sms;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by sanni on 27/02/18.
 */
public class SmsListAdapter extends RecyclerView.Adapter<SmsListAdapter.SmsViewHolder> {

    private List<Sms> smsList;
    private Context context;

    SmsListAdapter(Context context, List<Sms> smsList) {
        this.context = context;
        this.smsList = smsList;
    }

    /**
     * Called when RecyclerView needs a new {@link android.support.v7.widget.RecyclerView.ViewHolder} of the given type to represent
     * an item.
     * <p>
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     * <p>
     * The new ViewHolder will be used to display items of the adapter using
     * {@link #onBindViewHolder(android.support.v7.widget.RecyclerView.ViewHolder, int, java.util.List)}. Since it will be re-used to display
     * different items in the data set, it is a good idea to cache references to sub views of
     * the View to avoid unnecessary {@link android.view.View#findViewById(int)} calls.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     * @see #getItemViewType(int)
     * @see #onBindViewHolder(android.support.v7.widget.RecyclerView.ViewHolder, int)
     */
    @Override
    public SmsListAdapter.SmsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.sms_item_layout, parent, false);
        return new SmsViewHolder(itemView);
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the {@link android.support.v7.widget.RecyclerView.ViewHolder#itemView} to reflect the item at the given
     * position.
     * <p>
     * Note that unlike {@link ListView}, RecyclerView will not call this method
     * again if the position of the item changes in the data set unless the item itself is
     * invalidated or the new position cannot be determined. For this reason, you should only
     * use the <code>position</code> parameter while acquiring the related data item inside
     * this method and should not keep a copy of it. If you need the position of an item later
     * on (e.g. in a click listener), use {@link android.support.v7.widget.RecyclerView.ViewHolder#getAdapterPosition()} which will
     * have the updated adapter position.
     * <p>
     * Override {@link #onBindViewHolder(android.support.v7.widget.RecyclerView.ViewHolder, int, java.util.List)} instead if Adapter can
     * handle efficient partial bind.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(SmsListAdapter.SmsViewHolder holder, int position) {
        Sms sms = smsList.get(position);

        Date date = new Date(Long.parseLong(sms.getTime()));
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm", Locale.getDefault());
        String time = dateFormat.format(date);

        holder.messageView.setText(sms.getMsg());
        holder.dateView.setText(time);
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return smsList.size();
    }

    /**
     * View Holder for SMS
     */
    class SmsViewHolder extends RecyclerView.ViewHolder {

        TextView messageView;
        TextView dateView;

        SmsViewHolder(View itemView) {
            super(itemView);
            messageView = itemView.findViewById(R.id.msg_text_view);
            dateView = itemView.findViewById(R.id.date_view);
        }
    }
}
