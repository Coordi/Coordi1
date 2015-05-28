package kr.co.pcrc.coordi1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class LoginActivity extends Activity {
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

    private String html = "";
    private Handler mHandler;

    private Socket socket;

    private BufferedReader networkReader;
    private BufferedWriter networkWriter;
    private String ip = "192.168.123.1"; // IP
    private int port = 4444; // PORT번호

    @Override
    protected void onStop() {
        super.onStop();
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mHandler = new Handler();

        checkUpdate.start();

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
            public void onClick(View v) {
                EnrollBtClicked();
            }
        });
        compBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CompBtClicked();
            }
        });
    }

    public void LoginBtClicked() {
        String Email = id.getText().toString();
        String Pw = pw.getText().toString();
        if (Email != null || Pw != null) {
            PrintWriter out = new PrintWriter(networkWriter, true);
            String message = Email + "/" + Pw;
            out.println(message);
        }

        /*
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
        */

    }

    public void EnrollBtClicked(){
        loginLayout.setVisibility(View.INVISIBLE);
        enrollLayout.setVisibility(View.VISIBLE);

        enroll_view = true;
    }
    public void CompBtClicked(){
        String NewEmail = newEmail.getText().toString();
        String NewPw = newPW.getText().toString();
        String NewName = newName.getText().toString();
        String NewBirth = newBirth.getText().toString();

        if(NewEmail.equals("") || NewPw.equals("") || NewName.equals("")||NewBirth.equals("")) {
            Toast.makeText(getApplicationContext(), "입력란을 모두 채워주세요. ", Toast.LENGTH_LONG).show();
        }else {
            PrintWriter out = new PrintWriter(networkWriter, true);
            String message = NewEmail + "/" + NewPw + "/" + NewName + "/" + NewBirth;
            out.println(message);
        }

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


    private Thread checkUpdate = new Thread() {
        public  int init = 0;
        public void run() {
            try {
                if(init == 0)
                {
                    setSocket(ip, port);
                    init  = 1;
                }
               // setSocket(ip, port);
                String line;
                Log.w("ChattingStart", "Start Thread");
                while (true) {
                    Log.w("Chatting is running", "chatting is running");
                    line = networkReader.readLine();
                    html = line;
                    mHandler.post(showUpdate);
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    };

    private Runnable showUpdate = new Runnable() {

        public void run() {
            Toast.makeText(LoginActivity.this, "Coming word: " + html, Toast.LENGTH_SHORT).show();

            if (html.equals("Success")) {
                Toast.makeText(getApplicationContext(),
                        "로그인에 성공하였습니다.", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }else {
                Toast.makeText(getApplicationContext(),
                        "이메일 또는 비밀번호가 옳바르지 않습니다.", Toast.LENGTH_LONG).show();
            }
        }

    };

    public void setSocket(String ip, int port) throws Exception {

        try {
            socket = new Socket(ip, port);
            networkWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            networkReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }

    }
}
