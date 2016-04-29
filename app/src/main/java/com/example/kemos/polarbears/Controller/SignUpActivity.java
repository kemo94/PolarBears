package com.example.kemos.polarbears.Controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kemos.polarbears.Model.ParseOperation;
import com.example.kemos.polarbears.R;
import com.example.kemos.polarbears.View.User;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class SignUpActivity extends Activity {

    ParseOperation userOperation ;

    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;
    ParseOperation parseOperation ;
    ArrayList<User> arrayUsers = new ArrayList<User>();

    EditText userName , userPassword ;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
         parseOperation = new ParseOperation(getApplicationContext()) ;
         getUsers();
		setContentView(R.layout.signup_activity);
        Button signUpBtn = (Button) findViewById(R.id.signupBtn);
        Button loginBtn = (Button) findViewById(R.id.loginBtn);
        userName = (EditText) findViewById(R.id.userName);
        userPassword = (EditText) findViewById(R.id.password);

        userOperation = new ParseOperation(this);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);


        signUpBtn.setOnClickListener(onButtonClick);
        loginBtn.setOnClickListener(onButtonClick);
   }


    public boolean checkUser(String userName ,String userPassword ) {
        for ( int i = 0 ;i < arrayUsers.size() ; i++ )
            if ( userName.equals(arrayUsers.get(i).getName()) && userPassword.equals(arrayUsers.get(i).getPassword()) )
                 return true ;

        return false ;
    }
    boolean check(ArrayList<User>users , String id){
        for ( int i = 0 ;i < users.size() ;i ++ )
            if ( users.get(i).getId().equals(id))
                return true ;
        return false ;
    }
    public void getUsers( ){
        Firebase firebaseUsers = new Firebase("https://polarbears.firebaseio.com/users");

        firebaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Map<String, Object> td = (HashMap<String, Object>) snapshot.getValue();
                if (td != null)
                    for (final Map.Entry<String, Object> mapEntry : td.entrySet()) {
                        Firebase firebase = new Firebase("https://polarbears.firebaseio.com/users/" + mapEntry.getKey());
                        firebase.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                User user = dataSnapshot.getValue(User.class);
                                user.setId( mapEntry.getKey());
                                if ( !check(arrayUsers , mapEntry.getKey() ) )
                                    arrayUsers.add(user);

                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {}
                        });

                    }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {}
        });
    }


            private View.OnClickListener onButtonClick = new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    String name = userName.getText().toString();
                    String password = userPassword.getText().toString();

                    switch (v.getId()) {

                        case R.id.signupBtn: {

                            if (name.length() > 0 && password.length() > 0) {
                                if (checkUser(name, password) == true)

                                    Toast.makeText(getApplicationContext(), R.string.already_exist, Toast.LENGTH_LONG).show();
                                else {
                                    SharedPreferences.Editor editor = sharedpreferences.edit();
                                    parseOperation.addUser(name, password);
                                    editor.putString("UserName", name);
                                    editor.commit();
                                    startActivity(new Intent(SignUpActivity.this, SocialActivity.class));
                                }
                            } else {

                                Toast.makeText(getApplicationContext(), R.string.fill_field, Toast.LENGTH_LONG).show();
                            }
                            break;
                        }

                        case R.id.loginBtn: {

                            if (name.length() > 0 && password.length() > 0) {
                                if (checkUser(name, password) == true) {

                                    SharedPreferences.Editor editor = sharedpreferences.edit();
                                    editor.putString("UserName", name);
                                    editor.commit();
                                    startActivity(new Intent(SignUpActivity.this, SocialActivity.class));
                                } else
                                    Toast.makeText(getApplicationContext(), R.string.wrong_login, Toast.LENGTH_LONG).show();

                            } else {

                                Toast.makeText(getApplicationContext(), R.string.fill_field, Toast.LENGTH_LONG).show();
                            }
                            break;
                        }
                    }
                }
        };

}