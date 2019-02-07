package com.example.week5day3;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;


import com.example.week5day3.managersandmodels.Contact;
import com.example.week5day3.managersandmodels.ContactsManager;
import com.example.week5day3.managersandmodels.PermissionsManager;

import java.util.List;

public class MainActivity extends AppCompatActivity implements PermissionsManager.IPermissionManager, ContactsManager.IContactsManager {
    PermissionsManager permissionsManager;
    EditText etEmail;
    List<Contact> contactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        permissionsManager = new PermissionsManager(this, this);
        permissionsManager.checkPermission();
        etEmail = findViewById(R.id.etEmail);
    }

    private void refreshData() {
        String emaildata = "";

        try {

            /**************************************************/

            ContentResolver cr = getBaseContext()
                    .getContentResolver();
            Cursor cur = cr
                    .query(ContactsContract.Contacts.CONTENT_URI,
                            null,
                            null,
                            null,
                            null);

            if (cur.getCount() > 0) {

                Log.i("Content provider", "Reading contact  emails");

                while (cur
                        .moveToNext()) {

                    String contactId = cur
                            .getString(cur
                                    .getColumnIndex(ContactsContract.Contacts._ID));

                    // Create query to use CommonDataKinds classes to fetch emails
                    Cursor emails = cr.query(
                            ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Email.CONTACT_ID
                                    + " = " + contactId, null, null);

                            /*
                            //You can use all columns defined for ContactsContract.Data
                            // Query to get phone numbers by directly call data table column

                            Cursor c = getContentResolver().query(Data.CONTENT_URI,
                                      new String[] {Data._ID, Phone.NUMBER, Phone.TYPE, Phone.LABEL},
                                      Data.CONTACT_ID + "=?" + " AND "
                                              + Data.MIMETYPE + "= + Phone.CONTENT_ITEM_TYPE + ",
                                      new String[] {String.valueOf(contactId)}, null);
                            */

                    while (emails.moveToNext()) {

                        // This would allow you get several email addresses
                        String emailAddress = emails
                                .getString(emails
                                        .getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));

                        //Log.e("email==>", emailAddress);

                        emaildata +=" "+emailAddress+" " +"--------------------------------------";
                    }

                    emails.close();
                }

            }
            else
            {
                emaildata +=" Data not found. ";

            }
            cur.close();


        } catch (Exception e) {

            emaildata +=" Exception : "+e+" ";
        }

        //return emaildata;
        Log.d("TAG", "Emails: " + emaildata);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.checkResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onPerissionResult(boolean isGranted) {
        if(isGranted){
            getContacts();
            //refreshData();
        }else{
            Log.d("TAG", "onPerissionResult: " + (isGranted ? "Granted" : "Denied!!!"));
        }

    }

    public  void getContacts(){
        ContactsManager contactsManager = new ContactsManager(this);
        contactsManager.getContacts();
    }

    @Override
    public void getContacts(List<Contact> contacts) {
        contactList = contacts;
        for (Contact contact : contacts){
            Log.d("TAG", "OnCOntactsReceived: " + contact.toString());
        }
    }

    public void onClick(View view) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        int counter = 0;
        for(Contact contact : contactList){
            if(contact.getName().equals(etEmail.getText().toString())){
                break;
            }
            counter++;
        }
        String email = contactList.get(counter).getEmail().get(0);
        Log.d("TAG", "Email is: " + email);
        emailIntent.setData(Uri.parse("mailto:" + email));
        startActivity(emailIntent);
    }
}
