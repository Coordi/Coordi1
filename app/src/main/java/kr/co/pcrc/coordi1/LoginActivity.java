package kr.co.pcrc.coordi1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends ActionBarActivity {
    View loginLayout;
    View enrollLayout;

    EditText id;
    EditText pw;
    EditText newEmail;
    EditText newPW;
    EditText rePW;
    EditText newName;
    EditText newBirth;

    Button loginBt;
    Button enrollBt;
    Button compBt;

    public boolean enroll_view = false;

    private final long	FINSH_INTERVAL_TIME    = 2000;
    private long		backPressedTime        = 0;

    private String return_msg;

    private SocClient mClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        Intent temp = new Intent(getApplicationContext(), SocClient.class);
//        startActivity(temp);

        mClient = new SocClient();
        Thread myThread = new Thread(mClient);

        //final Context context = this;

        loginLayout = (View) findViewById(R.id.loginLayout);
        enrollLayout = (View) findViewById(R.id.enrollLayout);
        id = (EditText) findViewById(R.id.Email);
        pw = (EditText) findViewById(R.id.PW);
        newEmail = (EditText) findViewById(R.id.new_Email);
        newPW = (EditText) findViewById(R.id.new_PW);
        rePW = (EditText) findViewById(R.id.re_PW);
        newName = (EditText) findViewById(R.id.new_Name);
        newBirth = (EditText) findViewById(R.id.new_Birth);
        loginBt = (Button) findViewById(R.id.LoginBt);
        enrollBt = (Button) findViewById(R.id.EnrollBt);
        compBt=(Button) findViewById(R.id.Comp_Bt);

        loginBt.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                LoginBtClicked();
            }
        });
        enrollBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EnrollBtClicked();
            }
        });
    }

    public void LoginBtClicked() {
        String Email = id.getText().toString();
        String Pw = pw.getText().toString();
        if (Email.equals("admin@naver.com")) {
            if (Pw.equals("1q2w3e")) {
                String message = Email + "/" + Pw;
                mClient.send(message);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }

            else {
                Toast.makeText(getApplicationContext(), "비밀번호가 틀렸습니다. ", Toast.LENGTH_LONG).show();
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(), "이메일이 틀렸습니다. ", Toast.LENGTH_LONG).show();
        } // 서버 전

    }

    public void EnrollBtClicked(){
        loginLayout.setVisibility(View.INVISIBLE);
        enrollLayout.setVisibility(View.VISIBLE);

        enroll_view = true;
    }

    public void CompBtClicked(){

    }

    @Override
    public void onBackPressed() {
        long tempTime        = System.currentTimeMillis();
        long intervalTime    = tempTime - backPressedTime;

        if (enroll_view)
        {
            loginLayout.setVisibility(View.VISIBLE);
            enrollLayout.setVisibility(View.INVISIBLE);

            enroll_view = false;
        }else {
            if (0 <= intervalTime && FINSH_INTERVAL_TIME >= intervalTime) {
                super.onBackPressed();
            } else {
                backPressedTime = tempTime;
                Toast.makeText(getApplicationContext(), "'뒤로'버튼을 한번 더 누르시면 종료됩니다.",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}
