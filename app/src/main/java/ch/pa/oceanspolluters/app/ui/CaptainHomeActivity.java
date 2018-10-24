package ch.pa.oceanspolluters.app.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
    private List<ShipWithContainer> shipsFromCaptain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_captain_home);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mScrollView = (ScrollView) findViewById(R.id.captain_ship_list_scroll_view);

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

            View shipListItem = getLayoutInflater().inflate(R.layout.captain_ship_list,null);
            TextView shipListName = (TextView)findViewById(R.id.svShipName);
            for(ShipWithContainer shipWithContainer: shipsFromCaptain){
                shipListName.setText(shipWithContainer.ship.getName() + " - "+shipWithContainer.containers.size());
                mScrollView.addView(shipListItem);
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
