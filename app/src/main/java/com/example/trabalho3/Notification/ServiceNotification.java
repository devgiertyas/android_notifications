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
import com.example.trabalho3.Database.TarefaDAO;
import com.example.trabalho3.MainActivity;
import com.example.trabalho3.Models.Categoria;
import com.example.trabalho3.Models.Tarefa;
import com.example.trabalho3.R;
import com.example.trabalho3.TarefaActivity;

import java.util.List;

public class ServiceNotification extends Service {

    private static final String TAG = "ServiceNotification";
    private int NOTIFICATION_ID = 1;
    private static final String CHANNEL_ID = "ForegroundServiceChannel";
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

        startForeground(NOTIFICATION_ID, createNotification("Task Manager Executando.", "Notificação",0));

        // Exemplo: exibindo uma notificação a cada 5 segundos
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                TarefaDAO tarefaDAO = new TarefaDAO(getApplicationContext());

                List<Tarefa> listaTarefas = tarefaDAO.consultarTarefasVencidas();

                if(listaTarefas.size() > 0)
                {
                    for (Tarefa tarefa : listaTarefas) {
                        NOTIFICATION_ID = tarefa.getId();
                        Notification notification = createNotification("A tarefa: "
                                        + tarefa.getDescricao()+
                                        " está com o prazo atrasado.",
                                "Atenção!",tarefa.getId());
                        startForeground(NOTIFICATION_ID, notification);
                        tarefaDAO.alterarStatusNotificacao(tarefa.getId(), true);
                    }
                }
                handler.postDelayed(this, 10000); // repetir a cada 10 segundos
            }
        }, 10000);

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

    private Notification createNotification(String message, String title, int idTarefa) {
        Context context = getApplicationContext();

        if(idTarefa > 0)
        {
            Intent notificationIntent = new Intent(context, TarefaActivity.class);
            String id = String.valueOf(idTarefa);
            notificationIntent.putExtra("idTarefa",id);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setSmallIcon(R.drawable.baseline_notification_important_24)
                    .setContentIntent(pendingIntent)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_CALL);

            return builder.build();
        }
        else
        {
            Intent notificationIntent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setSmallIcon(R.drawable.baseline_notification_important_24)
                    .setContentIntent(pendingIntent)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_CALL);

            return builder.build();
        }

    }
}



