package ch.pa.oceanspolluters.app.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.bumptech.glide.Glide;

import java.util.List;

import ch.pa.oceanspolluters.app.BaseApp;
import ch.pa.oceanspolluters.app.R;
import ch.pa.oceanspolluters.app.database.entity.UserEntity;
import ch.pa.oceanspolluters.app.util.Roles;
import ch.pa.oceanspolluters.app.viewmodel.UserListViewModel;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity{


    private UserLoginTask mAuthTask = null;

    // UI references.
    private EditText mPassword;
    private Spinner mSpinner;
    private List<UserEntity> users;
    private ArrayAdapter<String> usersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.setTitle(getString(R.string.home));

        Glide.with(this)
                .load(R.drawable.loading)
                .into((ImageView)findViewById(R.id.loadingGif));

        // we set a bit of delay to see the loading screen

        UserListViewModel.FactoryUsers factoryUsers = new UserListViewModel.FactoryUsers(getApplication());
        UserListViewModel mUsers = ViewModelProviders.of(this, factoryUsers).get(UserListViewModel.class);
        mUsers.getUsers().observe(this, usersList -> {
            if (usersList != null) {
                users = usersList;
                generateLoginPage();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                    }
                }, 2500);
            }
        });
    }
    @Override
    public void onResume(){
        super.onResume();
        //we disconnect user if connected
        ((BaseApp)getApplication()).disconnectUser();
        if(mPassword != null)
            mPassword.setText(null);
        if(mSpinner != null)
            mSpinner.setSelection(0);

    }

    private void generateLoginPage(){
        String[] userNames = new  String[users.size()+1];
        if(userNames.length == 1){
            userNames[0] = "- No user found -";
        }
        else{
            userNames[0] = "- Select User -";
        }
        for(int i = 1; i<userNames.length; i++){
            userNames[i] = users.get(i-1).getName();
        }

        //setup loggin form
        LinearLayout loggingPage = findViewById(R.id.loginFormLoading);
        View loggingForm = getLayoutInflater().inflate(R.layout.login_form,null);

        loggingPage.removeAllViews();
        loggingPage.addView(loggingForm);
        loggingPage.setBackground(getDrawable(R.drawable.texture_eau));

        //setup spinner
        mSpinner = findViewById(R.id.users_spinner);
        usersAdapter =
                new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, userNames);
        usersAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(usersAdapter);

        //setup password
        mPassword = (EditText)findViewById(R.id.password);

        //setup login listener
        ImageButton mLoginButton = (ImageButton) findViewById(R.id.login_button);
        mLoginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
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
            else
                mPassword.setError(getString(R.string.error_user_not_selected));
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
            else
                mPassword.setError(getString(R.string.error_bad_password));
        }
    }
}

