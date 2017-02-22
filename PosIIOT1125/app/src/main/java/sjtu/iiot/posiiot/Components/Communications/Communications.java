package sjtu.iiot.posiiot.Components.Communications;

import android.graphics.Bitmap;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;

import android.util.Log;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

import sjtu.iiot.posiiot.App;
import sjtu.iiot.posiiot.DataStructure.BLConstants;

/**
 * Created by TongXinyu on 16/7/5.
 */
public class Communications {


    private final String DEBUG_TAG = "Communications";
    private final String CONTENT_KEY = "Content-Type";
    private final String SET_COOKIE_KEY = "Set-Cookie";
    private final String COOKIE_KEY = "Cookie";
    private final String APP_JSON = "application/json";
    private final String APP_URL = "application/x-www-form-urlencoded";

    public static final String TAG_LOGIN = "Login";
    public static final String TAG_LOGOUT = "Logout";
    public static final String TAG_REGISTER = "Register";
    public static final String TAG_QUERY_MAP = "Query Map";
    public static final String TAG_QUERY_POSITION = "Query Position";
    public static final String TAG_QUERY_USER_INFO = "Query User Information";
    public static final String TAG_CHANGE_USER_INFO = "Change User Information";
    public static final String TAG_CHANGE_PASSWORD = "Change Password";
    public static final String TAG_DOWNLOAD_MAP = "Download Map";
    public static final String TAG_SINGLE_TRACK = "Single Track";
    public static final String TAG_BATCH_TRACK = "Batch Track";
    public static final String TAG_TEST_CONNECT = "test_connect";

    private String mCookie;
    private Object mtag;


    public interface ResponseListener {
        public void onImageResponse(String tag, Bitmap response);
        public void onResponse(String tag, String response);
        public void onSuccess(String tag, String response);
        public void onFail(String tag, String response);
        public void refreshUI();
    }
    private ResponseListener mListener;

    public interface ErrorResponseListener {
        public void onErrorResponse(String tag, VolleyError volleyError);
    }
    private ErrorResponseListener mErrorListener;

    public Communications(Object tag) {
        mtag = tag;
    }

    private void updateCookie() {
        mCookie = App.getCookie();
    }

    public void setOnResponseListener(ResponseListener listener) {
        mListener = listener;
    }

    public void setOnErrorResponseListener(ErrorResponseListener listener) {
        mErrorListener = listener;
    }

    /**
     * Login
     * @param params API params
     * @param tag tag what you have set, otherwise will be default
     */
    public void doVolleyLogin(final Map<String, String> params, final String tag) {
        StringRequest request = new StringRequest(Request.Method.POST,
                BLConstants.API_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (mListener != null) {
                            mListener.onResponse(tag == null ? TAG_LOGIN : tag, s);
                        }
                        Log.i("Login", s);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (mListener != null) {
                    mErrorListener.onErrorResponse(tag == null ? TAG_LOGIN : tag, volleyError);
                }
                //Log.i("Loginerror","ppp" + volleyError.networkResponse.statusCode +"ddd" +  volleyError.networkResponse.data.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
             /*
             // we parse out durty string "path=/" in the cookie
             @Override
             protected Response<String> parseNetworkResponse(NetworkResponse response) {
                 for (String s : response.headers.keySet()) {
                     if (s.contains(SET_COOKIE_KEY)) {
                         mCookie = response.headers.get(s);
                         App.setCookie(mCookie);
                         break;
                     }
                 }
                 return super.parseNetworkResponse(response);
             }
             */
        };
        RequestManager.addRequest(request, mtag);
    }

    public void doVolleyDelete(String url, final String tag) {
        doVolleyRequest(Request.Method.DELETE, url, null, tag);
    }

    public void doVolleyGet(String url, final String tag) {
        doVolleyRequest(Request.Method.GET, url, null, tag);
    }


    /**
     * Upload user's location
     * @param UserID the ID of User
     * @param Pos_X the Coodinate X
     * @param Pos_Y the Coodinate Y
     */
    public void UploadUserLocation(String UserID,float Pos_X,float Pos_Y){
        HashMap<String,String> Upload=new HashMap<String,String>();
        Upload.put("userid",UserID);
        Upload.put("position", "1," + Pos_X + "," + Pos_Y);
        doVolleyPost("http://202.120.36.137:5000/update_position/",Upload,"update_position");
    }

    public void doVolleyPost(String url, final Map<String, String> params, final String tag) {
        doVolleyRequest(Request.Method.POST, url, params, tag);
    }

    /**
     * doVolleyRequest
     * @param method Request method
     * @param url API URL
     * @param params API params
     * @param tag tag what you have set for response
     */
    public void doVolleyRequest(int method, String url, final Map<String, String> params, final String tag) {
        updateCookie();
        StringRequest request = new StringRequest(
                method,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                if (mListener != null) {
                    mErrorListener.onErrorResponse(tag, volleyError);
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put(COOKIE_KEY, mCookie);
                return map;
            }
        };
        RequestManager.addRequest(request, mtag);
    }

    public void doVolleyImageRequest(String url, final String tag) {
        updateCookie();
        ImageRequest request = new ImageRequest(
                url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap s) {
                        if (mListener != null) {
                            mListener.onImageResponse(tag, s);
                        }
                    }
                },
                0, 0, Bitmap.Config.ARGB_8888,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        if (mListener != null) {
                            mErrorListener.onErrorResponse(tag, volleyError);
                        }
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put(COOKIE_KEY, mCookie);
                return map;
            }
        };
        RequestManager.addRequest(request, mtag);
    }
    public void doJsonRequest(String url, final Map<String, String> params, final String tag) {
//        Map<String, String> map = new HashMap<String, String>();
//        map.put("name1", "value1");
//        map.put("name2", "value2");

        JSONObject jsonObject = new JSONObject(params);
        String result=jsonObject.toString();
        Log.i("result",result);
        // String result = HttpUtil.parseJson(jsonObject, BLConstants.ARG_REQ_RESULT);
        JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(Request.Method.POST,url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(tag, "response -> " + response.toString());

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(tag, error.getMessage(), error);
            }
        })


        {
            //ע��˴�override��getParams()����,�ڴ˴�����post��Ҫ�ύ�Ĳ����������
            //��������������,����JSONObject����ʵ�δ���JsonObjectRequest������
            //������������ڴ˴��ǲ���Ҫ��
//    @Override
//    protected Map<String, String> getParams() {
//          Map<String, String> map = new HashMap<String, String>();
//            map.put("name1", "value1");
//            map.put("name2", "value2");

//        return params;
//    }

            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/json; charset=UTF-8");

                return headers;
            }
        };
        // requestQueue.add(jsonRequest);
        RequestManager.addRequest(jsonRequest, mtag);
    }

}
