package sjtu.iiot.posiiot.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import sjtu.iiot.posiiot.App;
import sjtu.iiot.posiiot.Components.Communications.Communications;
import sjtu.iiot.posiiot.Components.Util.HttpUtil;
import sjtu.iiot.posiiot.DataStructure.BLConstants;
import sjtu.iiot.posiiot.DataStructure.DBHelper;
import sjtu.iiot.posiiot.R;

/**登录界面activity*/
public class LoginActivity extends BaseActivity implements OnClickListener {
    private static final String DEBUG_TAG = "LoginActivity";
    private Button btn_login;
	private Button btn_regist;
	private Button btn_forget_pwd;
	private EditText username_edit;
	private EditText password_edit;
	private CheckBox rem_pwd;
	private DBHelper dbHelper;
	private String username = "";
	private String password = "";

	public HashMap<String,String>SendData;
	HashMap<String,String> UploadUserLocation;
	public HashMap<String,Integer>send_test= new HashMap<String, Integer>();
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login);
        
		initView();
	}
    
	private void initView(){
		dbHelper = new DBHelper(this);
		rem_pwd = (CheckBox)findViewById(R.id.rem_checkbox);
		btn_login = (Button)findViewById(R.id.signin_button);
		btn_login.setOnClickListener(this);

		btn_regist = (Button)findViewById(R.id.btn_login_regist);
		btn_regist.setOnClickListener(this);
		
		btn_forget_pwd = (Button)findViewById(R.id.btn_forget_pwd);
		btn_forget_pwd.setOnClickListener(this);

		username_edit = (EditText)findViewById(R.id.username_edit);
		password_edit = (EditText)findViewById(R.id.password_edit);

		initLogin();
	}

	public void initLogin() {

		String[] usernames = dbHelper.queryUserOrMapInfo("user_table");

		if (usernames.length > 0) {
			final String tempName = usernames[usernames.length - 1];
			username_edit.setText(tempName);
			username_edit.setSelection(tempName.length());
			final String tempPwd = dbHelper.queryPasswordByName(tempName);
			password_edit.setText(tempPwd);
            int checkFlag = dbHelper.queryIsSavedByName(tempName);

            if (checkFlag == 0) {
                rem_pwd.setChecked(false);
            } else if (checkFlag == 1) {

                rem_pwd.setChecked(true);
                doLogin(tempName, tempPwd);
            }
		}
	}
	
	@Override
	public void onClick(View v) {
		final Intent intent_register;
		switch (v.getId()) {
		case R.id.signin_button:

			username = username_edit.getText().toString().trim();
			password = password_edit.getText().toString().trim();
			btn_login.setEnabled(false);
            doLogin(username, password);

            final Intent intent_location;
            intent_location = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent_location);
            finish();
	        break;

		case R.id.btn_login_regist:
			intent_register = new Intent(LoginActivity.this, RegisterActivity.class);
			startActivity(intent_register);
			break;

		default:
			break;
		}
	}

    public void doLogin(String username, String password){
        Map<String, String> login_map = new HashMap<String, String>();
        login_map.put(BLConstants.ARG_USER_ID, username);
        login_map.put(BLConstants.ARG_USER_PWD, password);
		Log.e("Login",username);
		//LocationActivity.UserID=username;
        mComm.doVolleyLogin(login_map, Communications.TAG_LOGIN);
    }
        
    @Override
    public void onSuccess(String tag, String response) {
        if (tag.equals(Communications.TAG_LOGIN)) {
            String sessionID = HttpUtil.parseJson(response, "success");
            App.setCookie(BLConstants.KEY_SESSION + "=" + sessionID);
            //final Intent intent_location;
            if (!username.equals("") && !password.equals("")) {
                dbHelper.insertOrUpdate(username, password, 1);
            }
            //intent_location = new Intent(LoginActivity.this, MainActivity.class);
            //startActivity(intent_location);
            finish();
        }
    }

    @Override
    public void refreshUI() {
        btn_login.setEnabled(true);
    }
}
