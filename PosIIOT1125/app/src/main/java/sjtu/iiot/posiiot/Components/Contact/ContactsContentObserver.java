package sjtu.iiot.posiiot.Components.Contact;


import android.database.ContentObserver;
import android.os.Handler;
import android.util.Log;

public class ContactsContentObserver extends ContentObserver {

	public ContactsContentObserver(Handler handler) {
		super(handler);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onChange(boolean selfChange) {
		super.onChange(selfChange);
		Log.i("iBeacon contact", "联系人数据库发生了变化");
	}

}
