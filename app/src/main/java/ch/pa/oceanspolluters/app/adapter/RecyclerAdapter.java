package ch.pa.oceanspolluters.app.adapter;

import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ch.pa.oceanspolluters.app.R;
import ch.pa.oceanspolluters.app.database.pojo.ShipWithContainer;
import ch.pa.oceanspolluters.app.util.RecyclerViewItemClickListener;

public class RecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private List<T> mData;
    private RecyclerViewItemClickListener mListener;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    static class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout mListItem;
        ArrayList<TextView> mListText;

        ViewHolder(LinearLayout listItem, ArrayList<TextView> listText) {
            super(listItem);
            mListItem = listItem;
            mListText = listText;
        }
    }

    public RecyclerAdapter(RecyclerViewItemClickListener listener) {
        mListener = listener;
    }

    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //layout depend on object type
        Class oClass = mData.get(0).getClass();

        if(oClass.equals(ShipWithContainer.class)){

            //create item list object
            LinearLayout shipListItem = (LinearLayout) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.ship_list_item, parent, false);

            ArrayList<TextView> texts = new ArrayList<TextView>();

            texts.add(shipListItem.findViewById(R.id.svShipName));
            texts.add(shipListItem.findViewById(R.id.svDestinationPort));
            texts.add(shipListItem.findViewById(R.id.svDepartureDate));

            final ViewHolder viewHolderShipList = new ViewHolder(shipListItem,texts);

            shipListItem.setOnClickListener(view -> mListener.onItemClick(view, viewHolderShipList.getAdapterPosition()));
            shipListItem.setOnLongClickListener(view -> {
                mListener.onItemLongClick(view, viewHolderShipList.getAdapterPosition());
                return true;
            });
            return viewHolderShipList;
        }

        return new ViewHolder(null,null);
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter.ViewHolder holder, int position) {
        T item = mData.get(position);
        if (item.getClass().equals(ShipWithContainer.class)){
            holder.mListText.get(0).setText(((ShipWithContainer) item).ship.getName());
            holder.mListText.get(1).setText(((ShipWithContainer) item).port.getName());

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
            String departureDate = simpleDateFormat.format(((ShipWithContainer) item).ship.getDepartureDate());
            holder.mListText.get(2).setText(departureDate);
        }
    }

    @Override
    public int getItemCount() {
        if (mData != null) {
            return mData.size();
        } else {
            return 0;
        }
    }

    public void setData(final List<T> data) {
        if (mData == null) {
            mData = data;
            notifyItemRangeInserted(0, data.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return mData.size();
                }

                @Override
                public int getNewListSize() {
                    return data.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    if (mData instanceof ShipWithContainer) {

                        return ((ShipWithContainer) mData.get(oldItemPosition)).ship.getId().equals(((ShipWithContainer) mData.get(newItemPosition)).ship.getId());
                    }
                    return false;
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    if (mData instanceof ShipWithContainer) {
                        ShipWithContainer newAccount = (ShipWithContainer) data.get(newItemPosition);
                        ShipWithContainer oldAccount = (ShipWithContainer) mData.get(newItemPosition);
                        return newAccount.ship.getId().equals(oldAccount.ship.getId())
                                && Objects.equals(newAccount.containers, oldAccount.containers)
                                && Objects.equals(newAccount.port, oldAccount.port)
                                && newAccount.captain.equals(oldAccount.captain);
                    }
                    return false;
                }
            });
            mData = data;
            result.dispatchUpdatesTo(this);
        }
    }
}
