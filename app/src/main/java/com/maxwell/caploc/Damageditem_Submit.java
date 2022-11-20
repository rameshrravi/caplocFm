package com.maxwell.caploc;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.maxwell.caploc.DatabaseHelper.ChecklistControler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.zelory.compressor.Compressor;

public class Damageditem_Submit extends AppCompatActivity {

    ImageView imageView;
    Uri path;
    TextView submit, title;
    String cat,roomtypeid;
    SharedPreferences.Editor editor;
    SharedPreferences preferences;
    String userid, roomnoid, assetid, token;
    byte[] carImageBytes, numberPlateImageBytes;
    Bitmap bitmap = null;
    File fileCar = null;
    private Bitmap carBitmap;
    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_damageditem_submit);

        preferences = getSharedPreferences(StringConstants.prefMySharedPreference, Context.MODE_PRIVATE);
        editor = preferences.edit();

        userid = preferences.getString(StringConstants.prefuserId, "");
        roomnoid = getIntent().getStringExtra("roomnoid");
        assetid = getIntent().getStringExtra("assetid");
        roomtypeid = getIntent().getStringExtra("roomtypeid");

        token = preferences.getString(StringConstants.token, "");

        System.out.println("assetid" + assetid + "FSDF" + roomnoid);

        imageView = (ImageView) findViewById(R.id.Damaged_img);
        submit = (TextView) findViewById(R.id.text_submit);
        title = (TextView) findViewById(R.id.title);

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.back_arrow);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new DownloadFilesTask().execute();
               // getSubmit();
            }
        });


        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                {
                    Intent intent = new Intent(getApplicationContext(), Dashboard.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
        cat = getIntent().getStringExtra("cat");

        title.setText(cat);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            path = (Uri) extras.get("img");
            System.out.println("getid" + path);
            try {
                carBitmap=handleSamplingAndRotationBitmap(Damageditem_Submit.this,path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (null != path) {
            // Get the path from the Uri
            String path1 = getPath(getApplicationContext(), path);
            Log.i("TAG", "Image Path : " + path1);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            carBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

            carImageBytes = baos.toByteArray();
            String encodedImage = Base64.encodeToString(carImageBytes, Base64.DEFAULT);

            // Set the image in ImageView
            if (path != null) {
                fileCar = new File(path1);



            }
            //  Toast.makeText(getApplicationContext(),filePath, Toast.LENGTH_SHORT).show();

        }
        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), path);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

            carImageBytes = baos.toByteArray();
            String encodedImage = Base64.encodeToString(carImageBytes, Base64.DEFAULT);
            //    System.out.println("encoded"+encodedImage);
        } catch (IOException e) {
            e.printStackTrace();
        }
        imageView.setImageBitmap(bitmap);


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation1);


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), Dashboard.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.About_Us:

                        startActivity(new Intent(getApplicationContext(), AboutUs.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.profile:

                        startActivity(new Intent(getApplicationContext(), Profile.class));
                        overridePendingTransition(0, 0);
                        return true;

                }
                return false;
            }
        });


    }

    public void getSubmit() {
        final ProgressDialog pDialog = new ProgressDialog(Damageditem_Submit.this);
        pDialog.setMessage("Loading..");
        pDialog.setCancelable(false);
        pDialog.setTitle("");
        pDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.getCache().clear();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        final String currentDate = sdf.format(new Date());
        DateFormat df = new SimpleDateFormat("HH:mm:ss");
        final String currentTime = df.format(Calendar.getInstance().getTime());

        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, StringConstants.AdminmainUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //This code is executed if the server responds, whether or not the response contains data.
                //The String 'response' contains the server's response.
                Log.d("Response", response);

                try {

                    JSONObject jsonObject = new JSONObject(response.trim());

                    if (jsonObject.has("response")) {
                        JSONArray responseArray = jsonObject.getJSONArray("response");
                        if (responseArray.length() > 0) {
                            JSONObject object = responseArray.getJSONObject(0);
                            if (object.has("status")) {
                                String status = object.getString("status");
                                if (status.equals("success")) {
                                    showAlertDialog("Submit Successful");
                                } else {
                                    showAlertDialog("Something went wrong");
                                }
                            }
                        }

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                pDialog.dismiss();

            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                //This code is executed if there is an error.
                pDialog.dismiss();
                String errorMessage = StringConstants.ErrorMessage(error);
                if (errorMessage.equals("Connection TimeOut! Please check your internet connection."))
                    getSubmit();
            }
        }) {
            protected Map<String, String> getParams() {


                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("image", String.valueOf(bitmap));
                MyData.put("method", "update_asset_damage");
                MyData.put("asset_id", assetid);
                MyData.put("room_no_id", roomnoid);
                MyData.put("entered_time", currentTime);
                MyData.put("entered_date", currentDate);
                MyData.put("enterby_id", userid);
                MyData.put("token", token);

                System.out.println(String.valueOf(path));
                System.out.println("19 " + token);
                System.out.println("18 " + assetid);
                System.out.println("17 " + userid);
                System.out.println("15 " + currentTime);
                System.out.println("14 " + currentDate);

                System.out.println("112 " + roomnoid);
                return MyData;
            }
        };

        requestQueue.add(MyStringRequest);

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), Dashboard.class);
        startActivity(intent);
        finish();
    }

    public void showAlertDialog(String message) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setPositiveButton("ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {
                        dialog.dismiss();

//                        finish();

                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    public void showAlertDialogintent(String message) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setPositiveButton("ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {

                        Intent i = new Intent(getApplicationContext(), Damageditem_Image.class);
                        i.putExtra("roomnoid", roomnoid);
                        i.putExtra("roomtypeid", roomtypeid);
                        startActivity(i);
                        finish();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    private class DownloadFilesTask extends AsyncTask<String, String, String> {
        String response1;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            final ProgressDialog pDialog = new ProgressDialog(Damageditem_Submit.this);
            pDialog.setMessage("Loading..");
            pDialog.setCancelable(false);
            pDialog.setIndeterminate(false);
            pDialog.show();

        }
        @Override
        protected String doInBackground(String... strings) {


            try {


                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                final String currentDate = sdf.format(new Date());
                DateFormat df = new SimpleDateFormat("HH:mm:ss");
                final String currentTime = df.format(Calendar.getInstance().getTime());
                String charset = "UTF-8";
                File uploadFile1 = new File("/sdcard/myvideo.mp4");
                String requestURL = StringConstants.AdminmainUrl;

                MultipartUtility multipart = new MultipartUtility(requestURL, charset);


                multipart.addFormField("method", "update_asset_damage");
                multipart.addFormField("asset_id", assetid);
                multipart.addFormField("room_no_id", roomnoid);
                multipart.addFormField("entered_time", currentTime);
                multipart.addFormField("entered_date", currentDate);
                multipart.addFormField("enterby_id", userid);
                multipart.addFormField("token", token);



                fileCar = new Compressor(Damageditem_Submit.this).compressToFile(fileCar);
                multipart.addFilePart("image", fileCar);

                List<String> response = multipart.finish();


                Log.v("rht", "SERVER REPLIED:");


                for (String line : response) {
                    Log.v("rht", "Line : " + line);


                    response1 = line;


                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            return response1;

        }


        @Override
        protected void onPostExecute(String response) {


            Log.d("Response", response);
            try {


                JSONObject jsonObject = new JSONObject(response.trim());
                if (jsonObject.has("response")) {

                    JSONArray responseArray = jsonObject.getJSONArray("response");

                    if (responseArray.length() > 0) {
                        JSONObject object = responseArray.getJSONObject(0);
                        if (object.has("status")) {

                            if (object.getString("status").equals("success")) {

                                showAlertDialogintent("Update Asset Damage Added successfully");

                            }


                        } else {
                            showAlertDialog(object.getString("message"));
                        }
                    }


                }

                // pDialog.dismiss();
            } catch (JSONException e) {
                e.printStackTrace();
            }


            super.onPostExecute(response);
        }

    }
    public static Bitmap handleSamplingAndRotationBitmap(Context context, Uri selectedImage)
            throws IOException {
        int MAX_HEIGHT = 1024;
        int MAX_WIDTH = 1024;

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        InputStream imageStream = context.getContentResolver().openInputStream(selectedImage);
        BitmapFactory.decodeStream(imageStream, null, options);
        imageStream.close();

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, MAX_WIDTH, MAX_HEIGHT);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        imageStream = context.getContentResolver().openInputStream(selectedImage);
        Bitmap img = BitmapFactory.decodeStream(imageStream, null, options);

        img = rotateImageIfRequired(img, selectedImage);
        return img;
    }

    private static int calculateInSampleSize(BitmapFactory.Options options,
                                             int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will guarantee a final image
            // with both dimensions larger than or equal to the requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;

            // This offers some additional logic in case the image has a strange
            // aspect ratio. For example, a panorama may have a much larger
            // width than height. In these cases the total pixels might still
            // end up being too large to fit comfortably in memory, so we should
            // be more aggressive with sample down the image (=larger inSampleSize).

            final float totalPixels = width * height;

            // Anything more than 2x the requested pixels we'll sample down further
            final float totalReqPixelsCap = reqWidth * reqHeight * 2;

            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                inSampleSize++;
            }
        }
        return inSampleSize;
    }

    private static Bitmap rotateImageIfRequired(Bitmap img, Uri selectedImage) throws IOException {

        ExifInterface ei = new ExifInterface(selectedImage.getPath());
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(img, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(img, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(img, 270);
            default:
                return img;
        }
    }
    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }
}