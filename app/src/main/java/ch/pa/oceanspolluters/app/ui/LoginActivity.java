package ch.pa.oceanspolluters.app.ui;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ch.pa.oceanspolluters.app.BaseApp;
import ch.pa.oceanspolluters.app.R;
import ch.pa.oceanspolluters.app.database.AppDatabase;
import ch.pa.oceanspolluters.app.database.entity.UserEntity;
import ch.pa.oceanspolluters.app.database.repository.UserRepository;
import ch.pa.oceanspolluters.app.util.Roles;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity{


    private UserLoginTask mAuthTask = null;

    // UI references.
    private EditText mPassword;
    private Spinner mSpinner;
    private List<UserEntity> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        mPassword = (EditText)findViewById(R.id.password);

        new LoadUsersTask().execute();

        ImageButton mLoginButton = (ImageButton) findViewById(R.id.login_button);
        mLoginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

    }
    @Override
    public void onResume(){
        super.onResume();
        //we disconnect user if connected
        ((BaseApp)getApplication()).disconnectUser();
    }

    private class LoadUsersTask extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground(Void... empty) {

            users = UserRepository.getInstance(AppDatabase.getInstance(getApplicationContext())).getUsers();
            String[] userNames = new  String[users.size()+1];
            userNames[0] = "- Select User -";
            for(int i = 1; i<userNames.length; i++){
                userNames[i] = users.get(i-1).getName();
            }

            return userNames;
        }

        @Override
        protected void onPostExecute(String[] userNames) {
            mSpinner = findViewById(R.id.users_spinner);
            ArrayAdapter<String> usersAdapter =
                    new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, userNames);
            usersAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mSpinner.setAdapter(usersAdapter);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.parameters:
                startActivity(new Intent(this, ParameterActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }
        // Reset errors.
         mPassword.setError(null);

        // Store values at the time of the login attempt.
        String password = mPassword.getText().toString();
        String userName = mSpinner.getSelectedItem().toString();

        if (TextUtils.isEmpty(password) ) {
            mPassword.setError(getString(R.string.error_empty_password));
        }
        else{
            if(userName.indexOf('-') < 0)
                new UserLoginTask().execute(userName,password);
        }
    }

    private class UserLoginTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... credentials) {
            if(credentials.length == 2){

                for(int i = 0; i<users.size(); i++){

                    if (users.get(i).getName().equals(credentials[0]) && Integer.toString(users.get(i).getPassword()).equals(credentials[1])) {
                        ((BaseApp)getApplication()).connectUser(users.get(i));
                        return users.get(i).getRoleId();
                    }
                }
            }
            return -1;
        }

        @Override
        protected void onPostExecute(Integer userRoleId) {
            if(userRoleId >= 0){

                if(userRoleId == Roles.Administrator.id()){
                     startActivity(new Intent(getApplicationContext(), AdministratorHomeActivity.class));
                }
                else if(userRoleId == Roles.Docker.id()){
                    startActivity(new Intent(getApplicationContext(), DockerHomeActivity.class));
                }
                else if(userRoleId == Roles.LogisticManager.id()){
                    startActivity(new Intent(getApplicationContext(), LogisticManagerHomeActivity.class));
                }
                else{
                    startActivity(new Intent(getApplicationContext(), CaptainHomeActivity.class));
                }
            }
        }
    }
}

