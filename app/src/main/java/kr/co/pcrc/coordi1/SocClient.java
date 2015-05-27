package kr.co.pcrc.coordi1;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by Jeon SooMin on 2015-05-21.
 */


//public class SocClient extends LoginActivity {
//
//    private String html = "";
//    private Handler mHandler;
//
//    private Socket socket;
//
//    private BufferedReader networkReader;
//    private BufferedWriter networkWriter;
//
//    private String ip = "192.168.123.1"; // IP
//    private int port = 4444; // PORT번호
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        try {
//            socket.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.main);
//        mHandler = new Handler();
//
//        try {
//            setSocket(ip, port);
//        } catch (IOException e1) {
//            e1.printStackTrace();
//        }
//
//        checkUpdate.start();
//
//
//        final EditText et = (EditText) findViewById(R.id.EditText01);
//        Button btn = (Button) findViewById(R.id.Button01);
//        final TextView tv = (TextView) findViewById(R.id.TextView01);
//
//
//
//        btn.setOnClickListener(new Button.OnClickListener() {
//
//            public void onClick(View v) {
//                if (et.getText().toString() != null || !et.getText().toString().equals("")) {
//                    PrintWriter out = new PrintWriter(networkWriter, true);
//                    String return_msg = et.getText().toString();
//                    out.println(return_msg);
//                }
//            }
//        });
//    }
//
//    private Thread checkUpdate = new Thread() {
//
//        public void run() {
//            try {
//                String line;
//                Log.w("ChattingStart", "Start Thread");
//                while (true) {
//                    Log.w("Chatting is running", "chatting is running");
//                    line = networkReader.readLine();
//                    html = line;
//                    mHandler.post(showUpdate);
//                }
//            } catch (Exception e) {
//
//            }
//        }
//    };
//
//    private Runnable showUpdate = new Runnable() {
//
//        public void run() {
//            Toast.makeText(SocClient.this, "Coming word: " + html, Toast.LENGTH_SHORT).show();
//        }
//
//    };
//
//    public void setSocket(String ip, int port) throws IOException {
//
//        try {
//            socket = new Socket(ip, port);
//            networkWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
//            networkReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//        } catch (IOException e) {
//            System.out.println(e);
//            e.printStackTrace();
//        }
//
//    }
//
//}

public class SocClient implements Runnable {
    private Socket socket;
    private String ServerIP = "192.168.123.1";
    PrintWriter outToServer = null;
    BufferedReader in = null;
    public void run()
    {
        try
        {
            socket = new Socket("192.168.123.1", 4444);
            outToServer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }
        catch(Exception e)
        {
            System.out.print("Whoops! It didn't work!:");
            System.out.print(e.getLocalizedMessage());
            System.out.print("\n");
        }
    }

    public void send(String s)
    {
         outToServer.print(s + "\n");
         outToServer.flush();
        //Create BufferedReader object for receiving messages from server.
        try {
            String input = in.readLine();
            System.out.println("Input: " + input);
            Log.d("Server Send", in.readLine());
        }
        catch (Exception e) {
            System.out.println(e.toString());
        }
    }

}