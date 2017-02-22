package sjtu.iiot.posiiot.Components.Contact;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;

public class SortCursorLoader extends CursorLoader {

public SortCursorLoader(Context context, Uri uri, String[] projection,
						String selection, String[] selectionArgs, String sortOrder) {
		super(context, uri, projection, selection, selectionArgs, sortOrder);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public Cursor loadInBackground() {
		Cursor cursor = super.loadInBackground();
		return new SortCursor(cursor);
	}

}
