package sjtu.iiot.posiiot.Activity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.WindowManager;

/**
 * Created by sunjiachen on 16/11/23.
 */
public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive (Context context, Intent intent)
    {
        // TODO Auto-generated method stub
        int event = intent.getIntExtra("msg", -1);
        //Log.d("MyTag", "onclock......................");
        switch (event) {
            case 1:
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("ALARM")
                        .setMessage("No.1 Meeting is going to begin in 10 mins")
                        .setPositiveButton("Start Navigation", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                            }
                        })
                         .setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                            }
                         });
                AlertDialog ad = builder.create();
                ad.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                ad.setCanceledOnTouchOutside(true);
                ad.show();
                break;
            case 2:
                AlertDialog.Builder builder2 = new AlertDialog.Builder(context);
                builder2.setTitle("ALARM")
                        .setMessage("No.2 Meeting is going to begin in 10 mins")
                        .setPositiveButton("Start Navigation", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                            }
                        })
                        .setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                            }
                        });
                AlertDialog ad1 = builder2.create();
                ad1.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                ad1.setCanceledOnTouchOutside(true);
                ad1.show();
                break;
            case 3:
                AlertDialog.Builder builder3 = new AlertDialog.Builder(context);
                builder3.setTitle("ALARM")
                        .setMessage("No.3 Meeting is going to begin in 10 mins")
                        .setPositiveButton("Start Navigation", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                            }
                        })
                        .setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                            }
                        });
                AlertDialog ad2 = builder3.create();
                ad2.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                ad2.setCanceledOnTouchOutside(true);
                ad2.show();
                break;
            case 4:
                AlertDialog.Builder builder4 = new AlertDialog.Builder(context);
                builder4.setTitle("ALARM")
                        .setMessage("No.4 Meeting is going to begin in 10 mins")
                        .setPositiveButton("Start Navigation", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                            }
                        })
                        .setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                            }
                        });
                AlertDialog ad3 = builder4.create();
                ad3.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                ad3.setCanceledOnTouchOutside(true);
                ad3.show();
                break;
            case 5:
                AlertDialog.Builder builder5 = new AlertDialog.Builder(context);
                builder5.setTitle("ALARM")
                        .setMessage("No.5 Meeting is going to begin in 10 mins")
                        .setPositiveButton("Start Navigation", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                            }
                        })
                        .setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                            }
                        });
                AlertDialog ad4 = builder5.create();
                ad4.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                ad4.setCanceledOnTouchOutside(true);
                ad4.show();
                break;
            default:
                break;
            //Toast.makeText(context,event, Toast.LENGTH_SHORT).show();
        }
        getDebugUnregister();
    }
}
