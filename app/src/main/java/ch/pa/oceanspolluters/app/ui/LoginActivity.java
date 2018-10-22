package ch.pa.oceanspolluters.app.ui;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import ch.pa.oceanspolluters.app.R;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove dummy users after connexion to DB
     */
    private final UserDummy[] test_users = new UserDummy[]{
        new UserDummy(0,"Pierre", 1234, 1),
        new UserDummy(1,"Jean", 1234, 2),
        new UserDummy(2,"Paul", 1234, 3)
    };

    private String[] usersTest = new String[]{
            "Pierre",  "Jean" ,"Paul"
    };

    public class UserDummy{

        public int id;
        public String name;
        public int password;
        public int roleId;

        public UserDummy(int id, String name, int password, int roleId){
            this.id = id;
            this.name = name;
            this.password = password;
            this.roleId = roleId;
        }
    }

    private UserLoginTask mAuthTask = null;

    // UI references.
    private EditText mPassword;
    private Spinner mSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mPassword = (EditText)findViewById(R.id.password);

        // Set up the list of users
        mSpinner = (Spinner) findViewById(R.id.users_spinner);
        ArrayAdapter<String> usersAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, usersTest);
        usersAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(usersAdapter);

        Button mLoginButton = (Button) findViewById(R.id.login_button);
        mLoginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

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

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) || !isPasswordValid(password)) {
            mPassword.setError(getString(R.string.error_invalid_password));
        }
        else{
            new UserLoginTask().execute(userName,password);
        }
    }


    private boolean isPasswordValid(String password) {
        return password.length() == 4;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    private class UserLoginTask extends AsyncTask<String, Void, Boolean> {


        @Override
        protected Boolean doInBackground(String... credentials) {
            System.out.println("Debugpa - "+ credentials.length);
            if(credentials.length == 2){
                System.out.println("Debugpa - "+credentials[0]+" "+credentials[1]);
                for (UserDummy user: test_users) {

                    if (user.name.equals(credentials[0]) && Integer.toString(user.password).equals(credentials[1])) {
                        return true;
                    }
                }
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean loginSuccess) {
            if(loginSuccess){
                Context context = getApplicationContext();
                CharSequence text = "Login Success";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

                Intent intent = new Intent(context, CaptainHomeActivity.class);
                startActivity(intent);
                finish();

            }else{
                mPassword.setError(getString(R.string.error_incorrect_password));
            }
        }
    }
}

