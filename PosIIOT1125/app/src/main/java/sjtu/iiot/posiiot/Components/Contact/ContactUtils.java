package sjtu.iiot.posiiot.Components.Contact;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;

import sjtu.iiot.posiiot.Components.Contact.SortCursor.SortEntry;
import sjtu.iiot.posiiot.DataStructure.DBUser.Contact;

public class ContactUtils {
	public static Context m_context;

	public static ArrayList<SortEntry> mPersons = new ArrayList<SortEntry>();
	//SQLiteDatabase db;
	public static final String[] CONTACT_COLS = {Contact.CONTACTNAME, Contact.CONTACTNO};

	public static void init(Context context)
	{
		m_context = context;
	}


	public static void AddContact(String name, String account)
	{
		ContentValues values = new ContentValues();

		/*if(mPersonsName.contains(name)||mPersonsAccount.contains(account))
			return ;
		else{
			mPersonsName.add(name);
			mPersonsAccount.add(account);*/
		boolean flag=false;
		Cursor mCursor=QueryContact();
			values.put(Contact.CONTACTNAME, name);
			values.put(Contact.CONTACTNO, account);

		while(mCursor.moveToNext())
			if(mCursor.getString(mCursor.getColumnIndex(Contact.CONTACTNAME)).equals(name))
				flag=true;
		if(flag==false)
				m_context.getContentResolver().insert(ContactProvider.CONTENT_URI, values);


	}


	public static Cursor QueryContact() {
		Cursor cursor = m_context.getContentResolver().query(ContactProvider.CONTENT_URI , null, null, null, null);
		return cursor;
	}


	public static void ChangeContact(String name, String number, String ContactId)
	{
		ContentValues values = new ContentValues();

        values.put(Contact.CONTACTNAME, name);
        values.put(Contact.CONTACTNO, number);
        m_context.getContentResolver().update(ContactProvider.CONTENT_URI,
                        values,
                        "_ID=?",
                        new String[] { ContactId });

	}


	public static void DeleteContact(String ContactId)
	{
		ContentValues values = new ContentValues();
      Uri uri = Uri.withAppendedPath(ContactProvider.CONTENT_URI, ContactId);
		m_context.getContentResolver().delete(uri, null, null);
	}



}

