package khoiviet24.recfit;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.List;

public class Register extends AppCompatActivity {

    protected ImageView mRegisterLogo;

    protected EditText mFirst;
    protected EditText mLast;
    protected EditText mUsername;
    protected EditText mUserEmail;
    protected EditText mUserEmailConfirm;
    protected EditText mUserPassword;
    protected EditText mUserPasswordConfirm;
    protected RadioButton mMale;
    protected RadioButton mFemale;
    protected Spinner mAge;
    protected Spinner mGymMembership;
    protected EditText mGym;
    protected boolean gym;
    protected Button mRegisterBtn;
    String gender;


    ArrayAdapter<CharSequence> adapter;
    ArrayAdapter<CharSequence> adapterGym;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //initialize
        mRegisterLogo = (ImageView)findViewById(R.id.registerLogo);
        Bitmap mLogo = BitmapFactory.decodeResource(getResources(), R.drawable.register_bar);
        mRegisterLogo.setImageBitmap(mLogo);
        mFirst = (EditText)findViewById(R.id.firstRegister_txt);
        mLast = (EditText)findViewById(R.id.lastRegister_txt);
        mUsername = (EditText)findViewById(R.id.usernameRegister_txt);
        mUserEmail = (EditText)findViewById(R.id.emailRegister_txt);
        mUserEmailConfirm = (EditText)findViewById(R.id.confirmEmailRegister_txt);
        mUserPassword = (EditText)findViewById(R.id.passwordRegister_txt);
        mUserPasswordConfirm = (EditText)findViewById(R.id.confirmPasswordRegister_txt);
        mMale = (RadioButton)findViewById(R.id.male_btn);
        mFemale = (RadioButton)findViewById(R.id.female_btn);
        mAge = (Spinner)findViewById(R.id.spinner);
        mGymMembership = (Spinner)findViewById(R.id.spinnerGym);
        adapter = ArrayAdapter.createFromResource(this, R.array.age_group, R.layout.register_spinner_item);
        adapterGym = ArrayAdapter.createFromResource(this, R.array.gym_membership, R.layout.register_spinner_item);
        mGym = (EditText)findViewById(R.id.gym_membership_edit);
        mGym.setVisibility(View.INVISIBLE);
        gym = false;
        mRegisterBtn = (Button)findViewById(R.id.register_btn);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mAge.setAdapter(adapter);
        mAge.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        adapterGym.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mGymMembership.setAdapter(adapterGym);
        mGymMembership.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i == 8){
                    mGym.setVisibility(View.VISIBLE);
                    gym = true;
                }else{
                    mGym.setVisibility(View.INVISIBLE);
                    gym = false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //listen to register button click
        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if((isSame(mUserEmail.getText().toString().trim(),mUserEmailConfirm.getText().toString().trim()))&&(isSame(mUserPassword.getText().toString().trim(), mUserPasswordConfirm.getText().toString().trim())) && (!isEmpty(mUserEmail.getText().toString().trim())) && (!isEmpty(mUserPassword.getText().toString().trim()))) {

                    ParseQuery<ParseUser> query = ParseUser.getQuery();
                    query.findInBackground(new FindCallback<ParseUser>() {
                        @Override
                        public void done(List<ParseUser> objects, ParseException e) {
                            if(e == null){
                                for(int i = 0; i < objects.size(); i++){
                                    if(isSame(mUsername.getText().toString().trim(), objects.get(i).getUsername())){
                                        AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                                        builder.setMessage("Username already taken");
                                        builder.setTitle("Please select different username");
                                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                //close the dialog
                                                dialogInterface.dismiss();
                                            }
                                        });
                                        AlertDialog dialog = builder.create();
                                        dialog.show();
                                    }else{

                                    }
                                }

                            }
                        }
                    });

                    String first = mFirst.getText().toString().trim();
                    String last = mLast.getText().toString().trim();
                    String username = mUsername.getText().toString().trim();
                    String email = mUserEmail.getText().toString().trim();
                    String password = mUserPassword.getText().toString().trim();
                    String age = mAge.getSelectedItem().toString().trim();
                    String tempGym = "";
                    if(gym == true){
                        tempGym = mGym.getText().toString().trim();
                    }else{
                        tempGym = mGymMembership.getSelectedItem().toString().trim();
                    }


                    //store user info
                    ParseUser user = new ParseUser();
                    user.put("Name", first + " " + last);
                    user.setUsername(username);
                    user.setEmail(email);
                    user.setPassword(password);
                    user.put("Gender", gender);
                    user.put("Age", age);
                    user.put("Gym", tempGym);


                    user.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            //String message = e.getMessage();
                            if (e == null) {
                                //Toast.makeText(Register.this, "Hi", Toast.LENGTH_LONG).show();

                                Intent RegisterToLogin = new Intent(Register.this, NavDrawer.class);
                                startActivity(RegisterToLogin);
                            } else {
                                //Toast.makeText(Register.this, message, Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                    builder.setMessage("Email or Password fields do not match");
                    builder.setTitle("Incorrect Register Info");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //close the dialog
                            dialogInterface.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });

    }

    private boolean isEmpty(String myEditText){
        return myEditText.toString().trim().length() == 0;
    }

    private boolean isSame(String text1, String text2){
        return text1.toString().trim().equals(text2.toString().trim());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void selectGender(View view){
        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId())
        {
            case R.id.male_btn:
                if(checked)
                {
                    gender = "Male";
                    mFemale.setChecked(false);
                }
                break;
            case R.id.female_btn:
                if(checked)
                {
                    gender = "Female";
                    mMale.setChecked(false      );
                }
                break;
        }
    }
}
