/**
 * Common Functions
 * Author 	: Jitesh Rana <jhrana@gmail.com>
 * Company 	: Differenz Systems
 * Date		: 7-May-2014
 */
package com.example.administrator.swipe_fragment.utility;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.swipe_fragment.R;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class CommonFunctions {

    static String tag = "CommonFunctions :";
    public static String errMessage = "";

    /**
     * Check Internet connection available or not
     *
     * @return
     */

    public static String getJSONe(String url, JSONObject jsonObj, String accessToken) {

        Logger.debugE(url);
        InputStream inputStream = null;
        String result = "";
        try {

            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpGet httpGet = new HttpGet(url);


            String json = jsonObj.toString();
            //StringEntity se = new StringEntity(json, "UTF-8");

            // 6. set httpPost Entity
            //		httpGet.setEntity(se);

            // 7. Set some headers to inform server about the type of the
            // content
            httpGet.setHeader("Accept", "application/json");
            httpGet.setHeader("Content-type", "application/json");
            httpGet.addHeader("AccessToken", accessToken);
            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpGet);

            // 9. receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // 10. convert inputstream to string
            if (inputStream != null) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                        inputStream, "iso-8859-1"), 8);
                StringBuilder stringBuilder = new StringBuilder();

                String bufferedStrChunk = null;

                while ((bufferedStrChunk = bufferedReader.readLine()) != null) {
                    stringBuilder.append(bufferedStrChunk);
                }

                return stringBuilder.toString();
            } else
                result = "";

        } catch (Exception e) {
            Log.d("InputStream", e + "");
        }

        // 11. return result
        return result;

    }

    public static String getJSON(String url, String msg) {

        Logger.debugE(url);
        InputStream inputStream = null;
        String result = "";
        try {

            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpGet httpGet = new HttpGet(url);


            // String json = jsonObj.toString();
            StringEntity se = new StringEntity(msg, "UTF-8");

            // 6. set httpPost Entity
            //httpGet.setEntity(se);

            // 7. Set some headers to inform server about the type of the
            // content
            httpGet.setHeader("Accept", "application/json");
            httpGet.setHeader("Content-type", "application/json");
            // httpGet.addHeader("AccessToken", msg);
            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpGet);

            // 9. receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // 10. convert inputstream to string
            if (inputStream != null) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                        inputStream, "iso-8859-1"), 8);
                StringBuilder stringBuilder = new StringBuilder();

                String bufferedStrChunk = null;

                while ((bufferedStrChunk = bufferedReader.readLine()) != null) {
                    stringBuilder.append(bufferedStrChunk);
                }

                return stringBuilder.toString();
            } else
                result = "";

        } catch (Exception e) {
            Log.d("InputStream", e + "");
        }

        // 11. return result
        return result;

    }


    public static String postJSON(String url,
                                  List<NameValuePair> nameValuePairList) {

        HttpClient httpClient = new DefaultHttpClient();

        // In a POST request, we don't pass the values in the URL.
        // Therefore we use only the web page URL as the parameter of the
        // HttpPost argument
        HttpPost httpPost = new HttpPost(url);

        try {
            // UrlEncodedFormEntity is an entity composed of a list of
            // url-encoded pairs.
            // This is typically useful while sending an HTTP POST request.
            // UrlEncodedFormEntity urlEncodedFormEntity = new
            // UrlEncodedFormEntity(nameValuePairList);
            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(
                    nameValuePairList, "UTF-8");
            // setEntity() hands the entity (here it is urlEncodedFormEntity) to
            // the request.
            httpPost.setEntity(urlEncodedFormEntity);

            try {
                // HttpResponse is an interface just like HttpPost.
                // Therefore we can't initialize them
                HttpResponse httpResponse = httpClient.execute(httpPost);

                // According to the JAVA API, InputStream constructor do
                // nothing.
                // So we can't initialize InputStream although it is not an
                // interface
                InputStream inputStream = httpResponse.getEntity().getContent();

                InputStreamReader inputStreamReader = new InputStreamReader(
                        inputStream);

                // BufferedReader bufferedReader = new
                // BufferedReader(inputStreamReader);
                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(inputStream, "iso-8859-1"), 8);
                StringBuilder stringBuilder = new StringBuilder();

                String bufferedStrChunk = null;

                while ((bufferedStrChunk = bufferedReader.readLine()) != null) {
                    stringBuilder.append(bufferedStrChunk);
                }

                return stringBuilder.toString();

            } catch (ClientProtocolException cpe) {
                System.out.println("Firstption caz of HttpResponese :" + cpe);
                cpe.printStackTrace();
            } catch (IOException ioe) {
                System.out.println("Secondption caz of HttpResponse :" + ioe);
                ioe.printStackTrace();
            }

        } catch (UnsupportedEncodingException uee) {
            System.out
                    .println("Anption given because of UrlEncodedFormEntity argument :"
                            + uee);
            uee.printStackTrace();
        }

        return "";
    }


    public static String postJSONs(String url, String accessToken) {
        Logger.debugE(tag + " Request  :" + url);
        InputStream inputStream = null;
        String result = "";
        try {

            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);

            // String json = jsonObj.toString();
            // StringEntity se = new StringEntity(json, "UTF-8");

            // 6. set httpPost Entity
            // httpPost.setEntity(se);

            // 7. Set some headers to inform server about the type of the
            // content
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            httpPost.addHeader("AccessToken", accessToken);

            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);

            // 9. receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();
            int status = httpResponse.getStatusLine().getStatusCode();
            // 10. convert inputstream to string
            if (inputStream != null)
                result = convertStreamToString(inputStream);
            else
                result = "";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        return result;
    }

    public static String postJSON(String url, JSONObject jsonObj, String accessToken) {
        Logger.debugE(tag + " Request  :" + url + " JSON :" + jsonObj.toString());
        InputStream inputStream = null;
        String result = "";
        try {

            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);
            String json = jsonObj.toString();
            StringEntity se = new StringEntity(json, "UTF-8");
            httpPost.setEntity(se);
            // String json = jsonObj.toString();
            // StringEntity se = new StringEntity(json, "UTF-8");

            // 6. set httpPost Entity
            // httpPost.setEntity(se);

            // 7. Set some headers to inform server about the type of the
            // content
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            httpPost.addHeader("AccessToken", accessToken);
            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);

            // 9. receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // 10. convert inputstream to string
            if (inputStream != null)
                result = convertStreamToString(inputStream);
            else
                result = "";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        return result;
    }


    /* public static String postJSONe(String url, String accessToken) {
     InputStream inputStream = null;
     String result = "";
     try {

     HttpClient httpclient = new DefaultHttpClient();

    // 2. make POST request to the given URL
     HttpPost httpPost = new HttpPost(url);
    //
//	 String json = jsonObj.toString();
    // StringEntity se = new StringEntity(msg, "UTF-8");
    //
    // // 6. set httpPost Entity
    // httpPost.setEntity(se);
    //
    // // 7. Set some headers to inform server about the type of the
     // content
     httpPost.setHeader("Accept", "application/json");
     httpPost.setHeader("Content-type", "application/json");
    //
     httpPost.addHeader("AccessToken", accessToken);
    // // 8. Execute POST request to the given URL
     HttpResponse httpResponse = httpclient.execute(httpPost);
    //
    // // 9. receive response as inputStream
     inputStream = httpResponse.getEntity().getContent();
    //
    // // 10. convert inputstream to string
     if (inputStream != null)
     result = convertStreamToString(inputStream);
     else
     result = "";

     } catch (Exception e) {
     Log.d("InputStream", e.getLocalizedMessage());
     }

     return result;
     }
*/
    public static String postJSON(String url, String msg) {

        InputStream inputStream = null;
        String result = "";
        try {

            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);

            // String json = jsonObj.toString();
            StringEntity se = new StringEntity(msg, "UTF-8");

            // 6. set httpPost Entity
            httpPost.setEntity(se);

            // 7. Set some headers to inform server about the type of the
            // content

            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            httpPost.addHeader(Constants.accesstoken, msg);
            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);

            // 9. receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // 10. convert inputstream to string
            if (inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        // 11. return result
        return result;
        /*
         * URL Url = new URL(url); HttpURLConnection connection =
		 * (HttpURLConnection) Url .openConnection();
		 * connection.setDoOutput(true); connection.setDoInput(true);
		 * connection.setInstanceFollowRedirects(false);
		 * connection.setRequestMethod("POST");
		 * connection.setRequestProperty("Content-Type",
		 * "application/x-www-form-urlencoded");
		 * connection.setRequestProperty("charset", "iso-8859-1");
		 * connection.setUseCaches(false);
		 * 
		 * DataOutputStream wr = new DataOutputStream(
		 * connection.getOutputStream());
		 * 
		 * wr.writeBytes(msg);
		 * 
		 * wr.flush(); BufferedReader reader = new BufferedReader(new
		 * InputStreamReader( connection.getInputStream(), "iso-8859-1"), 8);
		 * BufferedReader reader = new BufferedReader(new InputStreamReader(
		 * connection.getInputStream()));
		 * 
		 * StringBuffer res = new StringBuffer(); char[] chBuff = new
		 * char[1000]; int len = 0; while ((len = reader.read(chBuff)) > 0)
		 * res.append(new String(chBuff, 0, len)); wr.close(); reader.close();
		 * Logger.debugE(tag + " Responce  :" + res.toString()); return
		 * res.toString();
		 * 
		 * } catch (SocketTimeoutException e) { errMessage = e.toString(); }
		 * catch (Exception e) { Logger.debugE(tag + " Error  :", e.toString());
		 * errMessage = e.toString(); } return "";
		 */
    }


    public static String postJSON(String url, JSONObject jsonObj) {
        Logger.debugE(tag + " Request  :" + url);
        InputStream inputStream = null;
        String result = "";
        try {
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);

            String json = jsonObj.toString();
            StringEntity se = new StringEntity(json, "UTF-8");

            // 6. set httpPost Entity
            httpPost.setEntity(se);

            // 7. Set some headers to inform server about the type of the
            // content
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);

            // 9. receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // 10. convert inputstream to string
            if (inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        // 11. return result
        return result;
    }

    private static String convertInputStreamToString(InputStream inputStream)
            throws IOException {
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    public static boolean isNetworkAvailable(Context mContext) {

        ConnectivityManager connec = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        // ARE WE CONNECTED TO THE NET
        if (connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED
                || connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTING
                || connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTING
                || connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED) {

            // MESSAGE TO SCREEN FOR TESTING (IF REQ)
            // Toast.makeText(this, connectionType + " connected",
            // Toast.LENGTH_SHORT).show();
            return true;

        } else if (connec.getNetworkInfo(0).getState() == NetworkInfo.State.DISCONNECTED
                || connec.getNetworkInfo(1).getState() == NetworkInfo.State.DISCONNECTED) {

            // System.out.println("Not Connected");
            return false;
        }
        return false;
    }

    /**
     * Convert Stream to String
     *
     * @param is
     * @return
     * @throws Exception
     */
    public static String convertStreamToString(InputStream is) throws Exception {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is,
                "UTF-8"));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line + "\n");
        }
        is.close();
        return sb.toString();
    }

    /**
     * Create Directories
     *
     * @param filePath
     * @return
     */
    public static boolean createDirs(String filePath) {
        Logger.debugE(filePath.substring(0, filePath.lastIndexOf("/")));
        File f = new File(filePath.substring(0, filePath.lastIndexOf("/")));
        Logger.debugE(f.mkdirs() + "");
        return f.mkdirs();
    }

    public static void showAlert(Context c, String title, String msg) {
        new AlertDialog.Builder(c)
                .setMessage(msg)
                .setTitle(title)
                .setPositiveButton(c.getString(android.R.string.ok),
                        new OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                            }
                        }).show();
    }

    @SuppressLint("NewApi")
    public static void showAlert(Context c, String title, String msg, int icon) {
        new AlertDialog.Builder(c, AlertDialog.THEME_HOLO_LIGHT)
                .setMessage(msg)
                .setIcon(icon)
                .setTitle(title)
                .setPositiveButton(c.getString(android.R.string.ok),
                        new OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                            }
                        }).show();
    }

    public static void showAlert(Context c, String title, String msg, int icon,
                                 OnClickListener listener) {
        new AlertDialog.Builder(c).setMessage(msg).setIcon(icon)
                .setTitle(title).setCancelable(false)
                .setPositiveButton(c.getString(android.R.string.ok), listener).show();
    }

    public static void showAlert(Context c, String title, String msg, int icon,
                                 OnClickListener listener,
                                 OnClickListener cancelListener) {
        new AlertDialog.Builder(c)
                .setMessage(msg)
                .setIcon(icon)
                .setTitle(title)
                .setPositiveButton(c.getString(android.R.string.ok), listener)
                .setNegativeButton(c.getString(android.R.string.cancel), cancelListener)
                .show();
    }

    public static void showAlert(Context c, String title, String msg, int icon,
                                 OnClickListener listener,
                                 OnClickListener cancelListener, boolean cancalable) {
        new AlertDialog.Builder(c)
                .setMessage(msg)
                .setIcon(icon)
                .setTitle(title)
                .setPositiveButton(c.getString(android.R.string.ok), listener)
                .setCancelable(cancalable)
                .setNegativeButton(c.getString(android.R.string.cancel), cancelListener)
                .show();
    }

    public static void showAlert(Context c, String title, String msg, int icon,
                                 OnClickListener listener, boolean cancalable) {
        new AlertDialog.Builder(c).setMessage(msg).setIcon(icon)
                .setTitle(title)
                .setPositiveButton(c.getString(android.R.string.ok), listener)
                .setCancelable(cancalable).show();
    }

    public static void setPreference(Context c, String pref, boolean val) {
        Editor e = PreferenceManager.getDefaultSharedPreferences(c).edit();
        e.putBoolean(pref, val);
        e.commit();
    }

    public static boolean getPreference(Context context, String pref,
                                        boolean def) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(pref, def);
    }

    public static void setPreference(Context c, String pref, int val) {
        Editor e = PreferenceManager.getDefaultSharedPreferences(c).edit();
        e.putInt(pref, val);
        e.commit();
    }

    public static int getPreference(Context context, String pref, int def) {
        return PreferenceManager.getDefaultSharedPreferences(context).getInt(
                pref, def);
    }

    public static void setPreference(Context c, String pref, float val) {
        Editor e = PreferenceManager.getDefaultSharedPreferences(c).edit();
        e.putFloat(pref, val);
        e.commit();
    }

    public static float getPreference(Context context, String pref, float def) {
        return PreferenceManager.getDefaultSharedPreferences(context).getFloat(
                pref, def);
    }

    public static void setPreference(Context c, String pref, long val) {
        Editor e = PreferenceManager.getDefaultSharedPreferences(c).edit();
        e.putLong(pref, val);
        e.commit();
    }

    public static long getPreference(Context context, String pref, long def) {
        return PreferenceManager.getDefaultSharedPreferences(context).getLong(
                pref, def);
    }

    public static void setPreference(Context c, String pref, String val) {
        Editor e = PreferenceManager.getDefaultSharedPreferences(c).edit();
        e.putString(pref, val);
        e.commit();
    }

    public static String getPreference(Context context, String pref, String def) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(pref, def);
    }

    public static String title(String string) {
        String ret = "";
        StringBuffer sb = new StringBuffer();
        Matcher match = Pattern.compile("([a-z])([a-z]*)",
                Pattern.CASE_INSENSITIVE).matcher(string);
        while (match.find()) {
            match.appendReplacement(sb, match.group(1).toUpperCase()
                    + match.group(2).toLowerCase());
        }
        ret = match.appendTail(sb).toString();
        return ret;
    }

    public static String toTitleCase(String input) {
        StringBuilder titleCase = new StringBuilder();
        boolean nextTitleCase = true;

        for (char c : input.toCharArray()) {
            if (Character.isSpaceChar(c)) {
                nextTitleCase = true;
            } else if (nextTitleCase) {
                c = Character.toTitleCase(c);
                nextTitleCase = false;
            }

            titleCase.append(c);
        }

        return titleCase.toString();
    }

    /**
     * Enables/Disables all child views in a view group.
     *
     * @param viewGroup the view group
     * @param enabled   <code>true</code> to enable, <code>false</code> to disable the
     *                  views.
     */
    public static void enableDisableViewGroup(ViewGroup viewGroup,
                                              boolean enabled, View[] exceptionalViews) {
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = viewGroup.getChildAt(i);
            view.setEnabled(enabled);
            for (int j = 0; j < exceptionalViews.length; j++)
                if (view.getId() == exceptionalViews[j].getId()) {
                    view.setEnabled(true);
                    break;
                }

            if (view instanceof ViewGroup) {
                enableDisableViewGroup((ViewGroup) view, enabled,
                        exceptionalViews);
            }
        }
    }

    /**
     * Create Progress bar
     */

    static ProgressDialog pd;

    public static ProgressDialog createProgressBar(Context context,
                                                   String strMsg) {
        pd = new ProgressDialog(context, R.style.Base_Theme_AppCompat_Dialog_MinWidth);
        pd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pd.setMessage(strMsg);
        pd.setCancelable(true);
        pd.show();
        return pd;
    }

    /**
     * Destroy progress bar
     */
    public static void destroyProgressBar() {
        pd.dismiss();
    }

    // Region startactivity
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static void changeactivity(Context context, String Act_des) {
        Intent i = new Intent();
        i.setClassName(context.getPackageName(), context.getPackageName() + "."
                + Act_des);
        ((Activity) context).startActivityForResult(i, 0);
        ((Activity) context).finish();

    }

    public static ProgressDialog createProgressBar(Context context, String strMsg, int theme) {
        pd = new ProgressDialog(context, theme);
        pd.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pd.setMessage(strMsg);
        pd.setCancelable(true);
        pd.setCanceledOnTouchOutside(true);
        pd.show();
        return pd;
    }


    // EndRegion

    // Region hidekeyboad
    public static void hideSoftKeyboard(Activity activity, EditText e) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity
                .getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(e.getWindowToken(), 0);
    }

    // EndRegion
    // Region Toast function
    public static void ToastMessage(Activity a, String message) {
        try {
            Toast.makeText(a, message, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    // EndRegion
    //Region exit from application
    @SuppressLint("NewApi")
    public static void alertboxshow(final Activity activity) {
        // TODO Auto-generated method stub
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(
                    activity, AlertDialog.THEME_DEVICE_DEFAULT_DARK);
            builder.setTitle(activity.getResources().getString(R.string.app_name));

            //	builder.setIcon(R.drawable.iconlogo);
            builder.setMessage("Do you want to exit?");

            builder.setPositiveButton("YES",
                    new OnClickListener() {
                        @SuppressLint("InlinedApi")
                        public void onClick(DialogInterface dialog, int which) {
                            // System.exit(0);
                            // Home.this.finish();

                            final Intent relaunch = new Intent(activity, Exiter.class)
                                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                            | Intent.FLAG_ACTIVITY_CLEAR_TASK
                                            | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);

                            activity.startActivity(relaunch);
                            activity.finish();

                        }
                    });
            builder.setNegativeButton("NO",
                    new OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

            AlertDialog dialog = builder.create();
            dialog.show();
        } catch (Exception e) {
            // TODO: handle exception
            Logger.debugE("Login", e + "");
        }
    }

    //EndRegion
    public static Map jsonToMap(JSONObject json) throws JSONException {
        Map<String, Object> retMap = new HashMap<String, Object>();

        if (json != JSONObject.NULL) {
            retMap = toMap(json);
        }
        return retMap;
    }

    public static Map toMap(JSONObject object) throws JSONException {
        Map<String, Object> map = new HashMap<String, Object>();

        Iterator<String> keysItr = object.keys();
        while (keysItr.hasNext()) {
            String key = keysItr.next();
            Object value = object.get(key);

            if (value instanceof JSONArray) {
                value = toList((JSONArray) value);
            } else if (value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            map.put(key, value);
        }
        return map;
    }

    public static List toList(JSONArray array) throws JSONException {
        List<Object> list = new ArrayList<Object>();
        for (int i = 0; i < array.length(); i++) {
            Object value = array.get(i);
            if (value instanceof JSONArray) {
                value = toList((JSONArray) value);
            } else if (value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            list.add(value);
        }
        return list;
    }


}
