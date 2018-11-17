package ch.pa.oceanspolluters.app.adapter;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import ch.pa.oceanspolluters.app.R;
import ch.pa.oceanspolluters.app.database.entity.ContainerEntity;
import ch.pa.oceanspolluters.app.database.pojo.ContainerWithItem;
import ch.pa.oceanspolluters.app.database.pojo.ShipWithContainer;
import ch.pa.oceanspolluters.app.util.RecyclerViewItemClickListener;
import ch.pa.oceanspolluters.app.util.ViewType;

public class RecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private List<T> mData;
    private RecyclerViewItemClickListener mListener;
    private ViewType type;

    @NonNull
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //layout depend on object type
        Class oClass = mData.get(0).getClass();
        LinearLayout lineInView = null;
        ViewHolder viewHolder = null;
        ArrayList<View> texts = new ArrayList<View>();

        if (type == ViewType.Captain_Home) {

            //create item list object
            lineInView = (LinearLayout) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_captain_home_ship_line, parent, false);

            texts.add(lineInView.findViewById(R.id.svShipName));
            texts.add(lineInView.findViewById(R.id.svDestinationPort));
            texts.add(lineInView.findViewById(R.id.svDepartureDate));

        } else if (type == ViewType.Docker_Home) {

            //create item list object
            lineInView = (LinearLayout) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_docker_home_ship_line, parent, false);

            texts.add(lineInView.findViewById(R.id.svdShipName));
            texts.add(lineInView.findViewById(R.id.svdDepartureTime));
            texts.add(lineInView.findViewById(R.id.svdLoadingStatus));


        } else if (type == ViewType.LogMan_Home) {

            // create item list object
            lineInView = (LinearLayout) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_container_line, parent, false);

            texts.add(lineInView.findViewById(R.id.clContainerName));
            texts.add(lineInView.findViewById(R.id.clContainerWeight));

        } else if (type == ViewType.LogMan_Container_Content_View) {

            // create item list object
            lineInView = (LinearLayout) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_container_line_delete, parent, false);

            texts.add(lineInView.findViewById(R.id.clContainerName));
            texts.add(lineInView.findViewById(R.id.clContainerWeight));
            texts.add(lineInView.findViewById(R.id.clContainerDelete));

        } else if (type == ViewType.Docker_Ship_Container_list) {

            // create item list object
            lineInView = (LinearLayout) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_docker_container_line, parent, false);

            texts.add(lineInView.findViewById(R.id.clContainerName));
            texts.add(lineInView.findViewById(R.id.clDockPosition));

        }


        final ViewHolder viewholder = new ViewHolder(lineInView, texts);
        lineInView.setOnClickListener(view -> mListener.onItemClick(view, viewholder.getAdapterPosition()));
        lineInView.setOnLongClickListener(view -> {
            mListener.onItemLongClick(view, viewholder.getAdapterPosition());
            return true;
        });

        return viewholder;
    }

    public RecyclerAdapter(RecyclerViewItemClickListener listener, ViewType type) {
        this.type = type;
        mListener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.ViewHolder holder, int position) {
        T item = mData.get(position);

        if (type == ViewType.Docker_Home) {

            ShipWithContainer shipWithContainer = (ShipWithContainer) item;

            //calculate containers to load
            int countContainerToLoad = 0;
            for (int i = 0; i < shipWithContainer.containers.size(); i++) {
                if (!shipWithContainer.containers.get(i).container.getLoaded()) {
                    countContainerToLoad++;
                }
            }
            int totalContainer = shipWithContainer.containers.size();
            int loaded = totalContainer - countContainerToLoad;
            String loadingStatus = loaded + "/" + totalContainer;

            if (countContainerToLoad == 0)
                ((TextView) holder.mListText.get(2)).setTextColor(Color.GREEN);

            //container to load label
            ((TextView) holder.mListText.get(2)).setText(loadingStatus);

            //ship name label
            ((TextView) holder.mListText.get(0)).setText(shipWithContainer.ship.getName());

            //calculate time remaining before departure
            Date currentTime = Calendar.getInstance().getTime();
            long departure = shipWithContainer.ship.getDepartureDate().getTime() - currentTime.getTime();
            long diffHours = departure / (60 * 60 * 1000) % 24;
            long diffDays = departure / (60 * 60 * 1000 * 24);
            String textTimeToDeparture = "";

            //time in days
            if (diffDays > 0) {
                textTimeToDeparture = diffDays + " days";
            } else {
                //time in hours
                textTimeToDeparture = diffHours + " hours";

                //if short delay red text
                if (diffHours <= 6 && countContainerToLoad > 0) {
                    ((TextView) holder.mListText.get(1)).setTextColor(Color.RED);
                } else
                    ((TextView) holder.mListText.get(1)).setTextColor(Color.GRAY);
            }
            //departure date label
            ((TextView) holder.mListText.get(1)).setText(textTimeToDeparture);



        } else if (type == ViewType.Captain_Home) {

            ShipWithContainer shipWithContainer = (ShipWithContainer) item;

            ((TextView) holder.mListText.get(0)).setText(shipWithContainer.ship.getName());
            ((TextView) holder.mListText.get(1)).setText(shipWithContainer.port.getName());

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
            String departureDate = simpleDateFormat.format(shipWithContainer.ship.getDepartureDate());
            ((TextView) holder.mListText.get(2)).setText(departureDate);


        } else if (type == ViewType.Docker_Ship_Container_list) {
            ContainerWithItem containerWithItem = (ContainerWithItem) item;

            ((TextView) holder.mListText.get(0)).setText(containerWithItem.container.getName());
            ((TextView) holder.mListText.get(1)).setText(containerWithItem.container.getDockPosition());
        }
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    static class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout mListItem;
        ArrayList<View> mListText;

        ViewHolder(LinearLayout listItem, ArrayList<View> listText) {
            super(listItem);
            mListItem = listItem;
            mListText = listText;
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
                    else if (mData instanceof ContainerWithItem) {
                        ContainerEntity oldContainerEntity = ((ContainerWithItem) mData.get(oldItemPosition)).container;
                        ContainerEntity newContainerEntity = ((ContainerWithItem) mData.get(newItemPosition)).container;

                        return (oldContainerEntity.getId().equals(newContainerEntity.getId()));
                    }

                    return false;
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    if (mData instanceof ShipWithContainer) {
                        ShipWithContainer newShip = (ShipWithContainer) data.get(newItemPosition);
                        ShipWithContainer oldShip = (ShipWithContainer) mData.get(oldItemPosition);
                        return newShip.ship.getId().equals(oldShip.ship.getId())
                                && Objects.equals(newShip.containers, oldShip.containers)
                                && Objects.equals(newShip.port, oldShip.port)
                                && newShip.captain.equals(oldShip.captain);
                    }
                    else if (mData instanceof ContainerWithItem) {
                        ContainerWithItem newContainer = (ContainerWithItem) data.get(newItemPosition);
                        ContainerWithItem oldContainer = (ContainerWithItem) mData.get(oldItemPosition);
                        return newContainer.container.getId().equals(oldContainer.container.getId())
                                && Objects.equals(newContainer.container, oldContainer.container)
                                && Objects.equals(newContainer.items, oldContainer.items);
                    }

                    return false;
                }
            });
            mData = data;
            result.dispatchUpdatesTo(this);
        }
    }
}
