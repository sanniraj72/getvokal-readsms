package com.getvokal.readsms.views;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.getvokal.readsms.R;
import com.getvokal.readsms.model.Sms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by sanni on 27/02/18.
 */
public class SmsListActivity extends AppCompatActivity {

    private static final int SMS_READ_PERMISSION_REQUEST = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_list);

        boolean smsPermission = checkSmsPermission();
        if (smsPermission) {
            showSms();
        }
    }

    /**
     * Get All Message of Given folder - Like inbox, sent etc.
     *
     * @return List of Sms
     */
    private List<Sms> getAllSms() {
        List<Sms> smsList = new ArrayList<>();
        Sms sms;
        Uri uri = Uri.parse("content://sms/inbox");
        ContentResolver resolver = this.getContentResolver();
        Cursor cursor = resolver.query(uri, null, null, null, null);
        // this.startManagingCursor(c);
        assert cursor != null;
        int totalSMS = cursor.getCount();
        if (cursor.moveToFirst()) {
            for (int i = 0; i < totalSMS; i++) {
                // Get Data
                int id = cursor.getColumnIndexOrThrow("_id");
                int message = cursor.getColumnIndexOrThrow("body");
                int date = cursor.getColumnIndexOrThrow("date");
                // Set data to sms model
                sms = new Sms();
                sms.setId(cursor.getString(id));
                sms.setMsg(cursor.getString(message));
                sms.setTime(cursor.getString(date));
                smsList.add(sms);
                cursor.moveToNext();
            }
        }
        cursor.close();
        return smsList;
    }

    private boolean checkSmsPermission() {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_SMS)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("Read SMS permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(SmsListActivity.this, new String[]{Manifest.permission.READ_SMS}, SMS_READ_PERMISSION_REQUEST);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS}, SMS_READ_PERMISSION_REQUEST);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 100:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (permissions[0].equals(Manifest.permission.READ_SMS)) {
                        showSms();
                    }
                }
                break;
            default:
                break;
        }
    }

    private void showSms() {
        RecyclerView recyclerView = findViewById(R.id.sms_list);
        List<Sms> smsList = getAllSms();
        Collections.sort(smsList, new Comparator<Sms>() {
            @Override
            public int compare(Sms s1, Sms s2) {
                Long t1 = Long.parseLong(s1.getTime());
                Long t2 = Long.parseLong(s2.getTime());
                return t2.compareTo(t1);
            }
        });
        SmsListAdapter smsListAdapter = new SmsListAdapter(this, smsList);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(smsListAdapter);
    }
}
