package com.kubista.alek.virtualnurse;

import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import de.greenrobot.event.EventBus;


public class ServerActivity extends AppCompatActivity {
    TextView tv_adress;
    private static final String TAG = "MyApplication";
    private static final int SERVER_PORT = 12345;

    private MySocketServer mServer;
    private MediaPlayer player;
    private ImageButton btnOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);
        tv_adress = findViewById(R.id.textViewAdress);
        startServer();
        EventBus.getDefault().register(this);
        btnOk = findViewById(R.id.btn_alert_ok);
    }

    private void startServer() {
        InetAddress inetAddress = getInetAddress();
        if (inetAddress == null) {
            Log.e(TAG, "Unable to lookup IP address");
            return;
        }

        mServer = new MySocketServer(new InetSocketAddress(inetAddress.getHostAddress(), SERVER_PORT));
        mServer.start();

        tv_adress.setText(mServer.getAddress().toString());
    }

    private static InetAddress getInetAddress() {
        try {
            for (Enumeration en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface networkInterface = (NetworkInterface) en.nextElement();

                for (Enumeration enumIpAddr = networkInterface.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = (InetAddress) enumIpAddr.nextElement();

                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress;
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
            Log.e(TAG, "Error getting the network interface information");
        }

        return null;
    }

    @SuppressWarnings("UnusedDeclaration")
    public void onEvent(SocketMessageEvent event) {
       final String message = event.getMessage();
        mServer.sendMessage("echo: " + message);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                player = MediaPlayer.create(getApplicationContext(),
                        Settings.System.DEFAULT_ALARM_ALERT_URI);
                player.setLooping(false);
                player.start();
                btnOk.setImageDrawable(getImageFromMessage(message));
              btnOk.setVisibility(View.VISIBLE);
            }
        });

    }

    private Drawable getImageFromMessage(String message) {
        switch (message){
            case "pee":
                return ContextCompat.getDrawable(getApplicationContext(),R.drawable.toilet);
            case "drink":
                return ContextCompat.getDrawable(getApplicationContext(),R.drawable.drink);
            case "alert":
                return ContextCompat.getDrawable(getApplicationContext(),R.drawable.warning);
        }
        return ContextCompat.getDrawable(getApplicationContext(),R.drawable.warning);
    }

    public void alertOk(View view) {
        view.setVisibility(View.INVISIBLE);
        player.stop();
    }
}
