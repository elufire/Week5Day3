package com.example.week5day3.managersandmodels;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ContactsManager {
    Context context;
    IContactsManager iContactsManager;


    public ContactsManager(Context context) {
        this.context = context;
        this.iContactsManager = (IContactsManager)context;
    }

    public void getContacts(){
        Uri contactsUri = ContactsContract.Contacts.CONTENT_URI;
        String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
        Cursor contactsCursor = context.getContentResolver().query(
                contactsUri, null, null, null, null);

        List<Contact> contactList = new ArrayList<>();
        while (contactsCursor.moveToNext()){
            String contactName = contactsCursor.getString(contactsCursor.getColumnIndex(DISPLAY_NAME));



                List<String> emails = new ArrayList<>();

                Uri phoneUri = ContactsContract.CommonDataKinds.Email.CONTENT_URI;
                String ADDRESS = ContactsContract.CommonDataKinds.Email.ADDRESS;


                String contactId = contactsCursor
                        .getString(contactsCursor
                                .getColumnIndex(ContactsContract.Contacts._ID));

                // Create query to use CommonDataKinds classes to fetch emails
                Cursor emailCursor = context.getContentResolver().query(
                        ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                        new String[]{ADDRESS},
                        DISPLAY_NAME + "=?",
                        new String[]{contactName},
                        ADDRESS +" ASC");

                while (emailCursor.moveToNext()){
                    String email = emailCursor.getString(emailCursor.getColumnIndex(ADDRESS));
                    emails.add(email);
                }
                if(!emails.isEmpty()){
                    Contact contact = new Contact(contactName, emails);
                    contactList.add(contact);
                }


        }
        iContactsManager.getContacts(contactList);
    }

    public interface IContactsManager{
        void getContacts(List<Contact> contact);
    }
}
