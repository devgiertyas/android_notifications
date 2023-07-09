package com.example.trabalho3.Notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.example.trabalho3.Database.CategoriaDAO;
import com.example.trabalho3.MainActivity;
import com.example.trabalho3.Models.Categoria;
import com.example.trabalho3.R;

public class ServiceNotification extends Service {

    private static final String TAG = "ServiceNotification";
    private int NOTIFICATION_ID = 1;
    private static final String CHANNEL_ID = "ForegroundServiceChannel";
    private int counter = 0;
    private boolean isServiceRunning = false;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Serviço criado.");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (isServiceRunning) {
            return START_NOT_STICKY;
        }

        createNotificationChannel();

        // Exemplo: exibindo uma notificação a cada 5 segundos
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                CategoriaDAO categoriaDAO = new CategoriaDAO(getApplicationContext());

                Categoria categoria = categoriaDAO.getCategoriaById(counter);

                if(categoria != null)
                {
                    Notification notification = createNotification(categoria.getNome());
                    startForeground(NOTIFICATION_ID, notification);
                    NOTIFICATION_ID++;
                }
                counter++;
                handler.postDelayed(this, 5000); // repetir a cada 5 segundos
            }
        }, 5000);

        isServiceRunning = true;
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Serviço destruído.");
        isServiceRunning = false;

    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence channelName = "Foreground Service Channel";
            String channelDescription = "Channel for Foreground Service";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, channelName, importance);
            channel.setDescription(channelDescription);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private Notification createNotification(String message) {
        Context context = getApplicationContext();

        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Foreground Service")
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_menu_camera)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_CALL);

        return builder.build();
    }
}



