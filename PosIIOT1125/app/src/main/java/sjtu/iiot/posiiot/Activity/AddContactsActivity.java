package sjtu.iiot.posiiot.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import sjtu.iiot.posiiot.Components.Contact.ContactUtils;
import sjtu.iiot.posiiot.R;

public class AddContactsActivity extends Activity {
	private Button m_SaveBtn;
	private  Button mAddAll;
	private EditText m_EditName;
	private EditText m_EditNum;
	private TextView m_TextTitle;
	private Context mContext=this;
	private String m_ContactId;
	private int m_Type;
	private String NAME;
	private String NUM;
	ProgressDialog m_dialogLoading;



	private static final String[] PHONES_PROJECTION = new String[] {
			Phone.DISPLAY_NAME, Phone.NUMBER };

	/**联系人显示名称**/
	private static final int PHONES_DISPLAY_NAME_INDEX = 0;

	/**电话号码**/
	private static final int PHONES_NUMBER_INDEX = 1;

	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.add_contacts);

		Intent intent = getIntent();
		Bundle bundle = intent.getBundleExtra("person");

		m_TextTitle = (TextView)findViewById(R.id.text_title);
		m_EditName = (EditText)findViewById(R.id.edit_name);
		m_EditNum = (EditText)findViewById(R.id.edit_num);


		m_Type = bundle.getInt("tpye");
		m_EditName.setText(bundle.getString("name"));
		m_EditNum.setText(bundle.getString("number"));

		if(m_Type == 0)
		{
			m_TextTitle.setText(R.string.addContact);
		}
		else if(m_Type == 1)
		{
			m_ContactId = bundle.getString("id");
			m_TextTitle.setText(R.string.editContact);
		}


		mAddAll=(Button)findViewById(R.id.AddAll);
		mAddAll.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				getPhoneContacts();
			}
		});


		m_SaveBtn = (Button)findViewById(R.id.btn_save_contact);
		m_SaveBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if("".equals(m_EditName.getText().toString()))
				{
					Toast.makeText(AddContactsActivity.this, R.string.inputName, Toast.LENGTH_SHORT).show();
				}
				else if("".equals(m_EditNum.getText().toString()))
				{
					Toast.makeText(AddContactsActivity.this, R.string.inputAccount, Toast.LENGTH_SHORT).show();
				}
				else if(m_Type == 0)
				{
					//新增联系人操作,放在线程中处理
					//Message message=new Message();
					//message.what=4;
					//LocationActivity.FriendID= m_EditNum.getText().toString();
					//Log.i("accountid", LocationActivity.FriendID);
					//LocationActivity.mHandler.sendMessage(message);
					new SaveContactTask().execute();
				}
				else if(m_Type == 1)
				{
					//更新联系人操作,放在线程中处理
					new ChangeContactTask().execute();
				}
				NAME=m_EditName.getText().toString();
				NUM=m_EditNum.getText().toString();
			}
		});
	}

	 class  SaveContactTask extends AsyncTask<Void, Integer, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			ContactUtils.AddContact(NAME, NUM);

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if(m_dialogLoading!= null)
			{
				m_dialogLoading.dismiss();
				finish();
			}
		}

		@Override
		protected void onPreExecute() {
			m_dialogLoading = new ProgressDialog(AddContactsActivity.this);
            m_dialogLoading.setProgressStyle(ProgressDialog.STYLE_SPINNER);//设置风格为圆形进度条
	        m_dialogLoading.setMessage(getString(R.string.saveContact));
            m_dialogLoading.setCancelable(false);
            m_dialogLoading.show();
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
		}
	}

	 class  ChangeContactTask extends AsyncTask<Void, Integer, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			ContactUtils.ChangeContact(NAME, NUM,m_ContactId);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if(m_dialogLoading!= null)
			{
				m_dialogLoading.dismiss();
				finish();
			}
		}

		@Override
		protected void onPreExecute() {
			m_dialogLoading = new ProgressDialog(AddContactsActivity.this);
	        m_dialogLoading.setProgressStyle(ProgressDialog.STYLE_SPINNER);//设置风格为圆形进度条
	        m_dialogLoading.setMessage(getString(R.string.saveContact));  
	        m_dialogLoading.setCancelable(false);
            m_dialogLoading.show();  
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
		}
		
	}



	/**得到手机通讯录联系人信息**/
	private void getPhoneContacts() {
		ContentResolver resolver = mContext.getContentResolver();

		// 获取手机联系人
		Cursor phoneCursor = resolver.query(Phone.CONTENT_URI, PHONES_PROJECTION, null, null, null);


		if (phoneCursor != null) {
			while (phoneCursor.moveToNext()) {

				//得到手机号码
				NUM = phoneCursor.getString(PHONES_NUMBER_INDEX);
				//当手机号码为空的或者为空字段 跳过当前循环
				if (TextUtils.isEmpty(NUM))
					continue;

				//得到联系人名称
				NAME = phoneCursor.getString(PHONES_DISPLAY_NAME_INDEX);
				ContactUtils.AddContact(NAME, NUM);
			}

			phoneCursor.close();
			finish();
		}
	}

}