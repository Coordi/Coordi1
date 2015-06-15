package kr.co.pcrc.coordi1;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class MainActivity extends Activity {
    private final long	FINSH_INTERVAL_TIME    = 2000;
    private long		backPressedTime        = 0;

    private DrawerLayout mainDrawerLayout;
    private ListView mainDrawerList;
    private ActionBarDrawerToggle mainDrawerToggle;

    private CharSequence mainDrawerTitle;
    private CharSequence currentTitle;
    private String[] mainMenuTitles;

    View profileModify;
    View mainFrame;

    Button proModifyBt;

    public boolean profile_view = false;

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
        setContentView(R.layout.activity_main);

        mHandler = new Handler();
        checkUpdate.start();

        profileModify = (View) findViewById(R.id.modify_profile);
        mainFrame = (View) findViewById(R.id.content_frame);
        proModifyBt = (Button) findViewById(R.id.Profile_Modify_Bt);

        currentTitle = mainDrawerTitle = getTitle();
        mainMenuTitles = getResources().getStringArray(R.array.menu_array);
        mainDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mainDrawerList = (ListView) findViewById(R.id.left_drawer);
        mainDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        mainDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mainMenuTitles));
        mainDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        mainDrawerToggle = new ActionBarDrawerToggle(this, mainDrawerLayout, R.drawable.ic_drawer,
                R.string.drawer_open, R.string.drawer_close) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(currentTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mainDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mainDrawerLayout.setDrawerListener(mainDrawerToggle);

        proModifyBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onProModifyBtClicked();
            }
        });

        if (savedInstanceState == null) {
            selectItem(0);
        }
    }
    public void onProModifyBtClicked() {

    }
    @Override
    public void onBackPressed() {
        long tempTime        = System.currentTimeMillis();
        long intervalTime    = tempTime - backPressedTime;

        if(profile_view) {
            profileModify.setVisibility(View.INVISIBLE);
            mainFrame.setVisibility(View.VISIBLE);

            profile_view = false;
        }else {
            if (0 <= intervalTime && FINSH_INTERVAL_TIME >= intervalTime) {
                moveTaskToBack(true);
                finish();
                android.os.Process.killProcess(android.os.Process.myPid());
            } else {
                backPressedTime = tempTime;
                Toast.makeText(getApplicationContext(), "'뒤로'버튼을 한번 더 누르시면 종료됩니다.",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mainDrawerLayout.isDrawerOpen(mainDrawerList);
        menu.findItem(R.id.action_profile).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (mainDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action buttons
        switch(item.getItemId()) {
            case R.id.profile_settings:
                Intent login_intent = getIntent();
                String Email = login_intent.getStringExtra("login_email");
//                Toast.makeText(getApplicationContext(), Email, Toast.LENGTH_LONG).show();

                profileModify.setVisibility(View.VISIBLE);
                mainFrame.setVisibility(View.INVISIBLE);
                profile_view = true;

//                if (Email != null) {
//                    PrintWriter out = new PrintWriter(networkWriter, true);
//                    String message = Email;
//                    out.println(message);
//                }
                break;
            case R.id.action_logout:
                Toast.makeText(getApplicationContext(), "Log out..", Toast.LENGTH_LONG).show();
                Intent intentLogout = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intentLogout);
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void setTitle(CharSequence title) {
        currentTitle = title;
        getActionBar().setTitle(currentTitle);
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mainDrawerToggle.syncState();
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mainDrawerToggle.onConfigurationChanged(newConfig);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }
    private void selectItem(int position) {
        Fragment fragment = new ContentFragment();
        Bundle args = new Bundle();
        switch (position) {
            case 1:
                args.putInt(ContentFragment.ARG_MENU_NUMBER, position);
                break;
            case 2:
                args.putInt(ContentFragment.ARG_MENU_NUMBER, position);
                break;
            case 3:
                args.putInt(ContentFragment.ARG_MENU_NUMBER, position);
                break;
            case 4:
                args.putInt(ContentFragment.ARG_MENU_NUMBER, position);
                break;
            default:
                break;
        }

        fragment.setArguments(args);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

        mainDrawerList.setItemChecked(position, true);
        setTitle(mainMenuTitles[position]);
        mainDrawerLayout.closeDrawer(mainDrawerList);
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
                Log.w("Start", "Start Thread");
                while (true) {
                    Log.w("running", "chatting is running");
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
//            Toast.makeText(MainActivity.this, "Coming word: " + html, Toast.LENGTH_SHORT).show();

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