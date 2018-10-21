package ch.pa.oceanspolluters.app.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ch.pa.oceanspolluters.app.R;
import ch.pa.oceanspolluters.app.model.User;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove dummy users after connexion to DB
     */
    private final UserDummy[] test_users = new UserDummy[]{
        new UserDummy(0,"Pierre", 12345, 1),
        new UserDummy(1,"Jean", 12345, 2),
        new UserDummy(2,"Paul", 12345, 3)
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
    private View mLoginView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set up the list of users
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

        mLoginView = findViewById(R.id.login_form);
    }



    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mPassword.setError(null);

        // Store values at the time of the login attempt.
        String password = mPassword.getText().toString();

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPassword.setError(getString(R.string.error_incorrect_password));
        }
        else{
            mAuthTask = new UserLoginTask(mSpinner.getSelectedItem().toString(),mPassword.getText().toString());
        }
    }


    private boolean isPasswordValid(String password) {
        return password.length() == 4;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mUserName;
        private final String mUserPassword;

        UserLoginTask(String userName, String password) {
            mUserName = userName;
            mUserPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            for (UserDummy user: test_users) {

                if (user.name.equals(mUserName) && Integer.toString(user.password).equals(mUserPassword)) {

                    Context context = getApplicationContext();
                    CharSequence text = "Login Success";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                    return true;
                }
            }
            mPassword.setError(getString(R.string.error_incorrect_password));
            return true;
        }


    }
}

