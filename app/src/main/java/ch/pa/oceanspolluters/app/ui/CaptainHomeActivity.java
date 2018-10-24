package ch.pa.oceanspolluters.app.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.List;

import ch.pa.oceanspolluters.app.BaseApp;
import ch.pa.oceanspolluters.app.R;
import ch.pa.oceanspolluters.app.database.AppDatabase;
import ch.pa.oceanspolluters.app.database.entity.UserEntity;
import ch.pa.oceanspolluters.app.database.pojo.ShipWithContainer;

public class CaptainHomeActivity extends AppCompatActivity {

    private ScrollView mScrollView;
    private LinearLayout mShipList;
    private List<ShipWithContainer> shipsFromCaptain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_captain_home);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mScrollView = (ScrollView) findViewById(R.id.captain_ship_list_scroll_view);
        mShipList = (LinearLayout) findViewById(R.id.captain_ship_list);

        new LoadShipsFromCaptain().execute();
    }

    private class LoadShipsFromCaptain extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... empty) {

            int captainId = ((BaseApp)getApplication()).getCurrentUser().getId();
            shipsFromCaptain =  AppDatabase.getInstance(getApplicationContext()).shipDao().getShipsFromCaptain(captainId);

            return true;
        }

        @Override
        protected void onPostExecute(Boolean success) {

            LayoutInflater inflater = (LayoutInflater)getApplicationContext().getSystemService
                    (getApplicationContext().LAYOUT_INFLATER_SERVICE);

            //we generate for each ship a line with data
            for(ShipWithContainer shipWithContainer: shipsFromCaptain){
                //get empty layout for ship item
                View shipListItem = inflater.inflate(R.layout.ship_list_item,null);
                mShipList.addView(shipListItem);

                //edit ship data
                TextView shipName = shipListItem.findViewById(R.id.svShipName);
                shipName.setText(shipWithContainer.ship.getName());

                TextView destinationPort = shipListItem.findViewById(R.id.svDestinationPort);
                destinationPort.setText(shipWithContainer.port.getName());

                TextView departureDate = shipListItem.findViewById(R.id.svDepartureDate);
                departureDate.setText(shipWithContainer.ship.getDepartureDate());
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
