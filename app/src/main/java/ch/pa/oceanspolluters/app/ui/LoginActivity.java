package ch.pa.oceanspolluters.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ch.pa.oceanspolluters.app.BaseApp;
import ch.pa.oceanspolluters.app.R;
import ch.pa.oceanspolluters.app.database.entity.UserEntity;
import ch.pa.oceanspolluters.app.util.Roles;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity{

    // UI references.
    private EditText mPassword;
    private Spinner mSpinner;
    private List<UserEntity> users;
    private static FirebaseDatabase fireBaseDB = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.setTitle(getString(R.string.home));

        Glide.with(this)
                .load(R.drawable.loading)
                .into((ImageView)findViewById(R.id.loadingGif));

        //as user dont change we only load them once
        fireBaseDB.getReference("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                users = new ArrayList<UserEntity>();

                for (DataSnapshot userSnapshot: snapshot.getChildren()) {
                    UserEntity user = userSnapshot.getValue(UserEntity.class);
                    users.add(user);
                }
                generateLoginPage();
                new Handler().postDelayed(() -> {

                }, 2500);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("PA_DEBUG : error init users");
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

        if (BaseApp.NeedHomeRefresh()) {
            BaseApp.setHomeNeedRefresh(false);
            recreate();
        }

    }

    private void generateLoginPage(){
        String[] userNames = new  String[users.size()+1];
        if(userNames.length == 1){
            userNames[0] = "- No user found -";
        }
        else{
            userNames[0] = getString(R.string.spinnerUserSelect);
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

        // setup spinner for user selection
        mSpinner = findViewById(R.id.users_spinner);
        ArrayAdapter<String> usersAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, userNames);
        usersAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(usersAdapter);


        //setup password
        mPassword = findViewById(R.id.password);

        //setup login listener
        ImageButton mLoginButton = findViewById(R.id.login_button);
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
         // Reset errors.
         mPassword.setError(null);

        // Store values at the time of the login attempt.
        String password = mPassword.getText().toString();
        String userName = mSpinner.getSelectedItem().toString();

        if (TextUtils.isEmpty(password) ) {
            mPassword.setError(getString(R.string.error_empty_password));
        }
        else{
            if(userName.indexOf('-') < 0){
                    int userRoleId = -1;

                    for(int i = 0; i<users.size(); i++){
                        if (users.get(i).getName().equals(userName) && Integer.toString(users.get(i).getPassword()).equals(password)) {
                            ((BaseApp)getApplication()).connectUser(users.get(i));
                            userRoleId = users.get(i).getRoleId();
                        }
                    }

                if(userRoleId >= 0){

                    if (userRoleId == Roles.Docker.id()) {
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

            else
                mPassword.setError(getString(R.string.error_user_not_selected));
        }
    }
}

