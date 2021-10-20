package in.imsaf.app;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import android.webkit.MimeTypeMap;

//import com.github.ybq.android.spinkit.style.Circle;

//import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;



//import class for Uploading part start
//import class for Uploading part End

public class MainActivity extends AppCompatActivity  {


    //lateinit var webview : WebView
    //val ref = FirebaseDatabase.getInstance().getReference("URL")

    //ProgressBar progressBar;
    WebView webView;
    private String webUrl = ("https://www.imsaf.in/");
    String waUrl = ("IMSAF APP DOWNLOAD LINK: " +
            "https://apkpure.com/imsaf/in.imsaf.app/");
    //String wUrl = ("https://www.imsaf.in/");



    //SwipeRefreshLayout swipeRefreshLayout;



    ProgressDialog progressDialog;
    private WebSettings webSettings;
    String myCurrentUrl;

    RelativeLayout relativeLayout;
    Button btnNoInternetConnection;


    //public Context context;

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int FILECHOOSER_RESULTCODE = 1;
    private ValueCallback<Uri> mUploadMessage;
    private Uri mCapturedImageURI = null;

    // the same for Android 5.0 methods only
    private ValueCallback<Uri[]> mFilePathCallback;
    private String mCameraPhotoPath;
    private ProgressBar progressBarWeb;
    //private boolean desktop;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);





        FirebaseMessaging.getInstance().subscribeToTopic("imsaf")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Done";
                        if (!task.isSuccessful()) {
                            msg = "Failed";
                        }
                    }
                });

        Window window = getWindow();



        //window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN) //
        // *desible for not entresting full screen


        webView = (WebView) findViewById(R.id.myWebView);
        progressBarWeb = (ProgressBar) findViewById(R.id.progressBar);



        //progressBar = (ProgressBar) findViewById(R.id.spin_kit);  //loading bar implement
        //Circle circle = new Circle();
        //progressBar.setIndeterminateDrawable(circle);  //loading bar end
        //swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        btnNoInternetConnection = (Button) findViewById(R.id.btnNoConnection);
        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
        progressDialog = new ProgressDialog(this);
        //progressDialog.setInverseBackgroundForced(true);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setTitle("⏳ Loading IMSAF App");



        myCurrentUrl = webUrl;


        webView.getSettings().setJavaScriptEnabled(true);

        if (savedInstanceState != null) {
            webView.restoreState(savedInstanceState);
            webView.getSettings().setJavaScriptEnabled(true);

        } else {

            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setSupportZoom(false);
            webView.getSettings().setLoadsImagesAutomatically(true);
            webView.getSettings().setLoadWithOverviewMode(true);
            webView.getSettings().setUseWideViewPort(true);
            webView.getSettings().setDomStorageEnabled(true);
            webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
            webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            webView.getSettings().setAppCacheEnabled(true);
            webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
            webView.getSettings().setDatabaseEnabled(true);
            webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setSavePassword(true);
            webView.getSettings().setSaveFormData(true);
            webView.getSettings().setEnableSmoothTransition(true);
            webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);



            webView.getSettings().setAppCachePath(getApplicationContext().getFilesDir().getAbsolutePath() + "/cache");
            webView.getSettings().setDatabasePath(getApplicationContext().getFilesDir().getAbsolutePath() + "/databases");
            webView.loadUrl(webUrl);

        /*
            Swipe Rfresh coding starting

           swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
             public void onRefresh() {
             swipeRefreshLayout.setRefreshing(true);


            new Handler().postDelayed(new Runnable() {
              @Override
           public void run() {
           swipeRefreshLayout.setRefreshing(false);
           webView.reload();
               }
                 }, 1300);
             }

             });

            swipeRefreshLayout.setColorSchemeColors(
            getResources().getColor(android.R.color.holo_orange_dark),
            getResources().getColor(R.color.colorPrimary),
            getResources().getColor(android.R.color.holo_red_light),
            getResources().getColor(android.R.color.holo_blue_bright)

            );



            //Swipe Code End.
        */

            checkConnection();


        }


        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                //url pase
                if(url.contains("mailto:")  || url.contains("apkpure") || url.contains("whatsapp:")  ||
                url.contains("maps") ||  url.contains(".nic")  ||  url.contains("razorpay") ||
                url.contains("facebook") || url.contains(".pk") || url.contains(".sa") || url.contains(".us") ||
                url.contains(".ae") || url.contains(".bd") || url.contains(".unv") || url.contains(".ac") ||
                url.contains("instagram") || url.contains("instamojo")  || url.contains("wblpp")   ||
                url.contains("india") || url.contains(".online") || url.contains(".net") ||  url.contains(".gov") ||
                url.contains(".org")  || url.contains(".uk") || url.contains(".tk") || url.contains(".cn") ||
                url.contains(".de") || url.contains(".ua") || url.contains(".au") || url.contains(".ir") ||
                url.contains(".ru") || url.contains("jiocloud") || url.contains("ikhlasbd")  || url.contains("youtube") ||
                url.contains("al-itisam") || url.contains("at-tahreek") || url.contains("epaper.puberkalom") ||
                url.contains("epaper.sangbadpratidin") || url.contains("ekdin-epaper") || url.contains("epaper.eisamay") ||
                url.contains("bangla.ganashakti") || url.contains("epaper.anandabazar") || url.contains("epaper.thestatesman") ||
                url.contains("bartamanpatrika") || url.contains("aajkaal") || url.contains("blogspot") || url.contains("wa.me") ||
                url.contains("twitter") || url.contains("play") || url.contains("paytm") || url.contains("bit.ly") ||
                url.contains("cutt.ly") || url.contains("shorturl.at") || url.contains("tinyurl") || url.contains("classroom") ||
                url.contains("rotf.lol") || url.contains("tiny") || url.contains(".un") || url.contains("meet") || url.contains("m.me") ||
                url.contains("t.me") || url.contains("fb.me") || url.contains("fb.com") )

                {
                    webView.stopLoading();
                    Intent i= new Intent();
                    i.setAction(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                    return true;
                }
                return false;

            }


        });






        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {

                progressBarWeb.setVisibility(View.VISIBLE);
                progressBarWeb.setProgress(newProgress);



                //progressBar.setVisibility(View.VISIBLE);
                //progressBar.setProgress(newProgress);

                //setTitle("Loading...");
                progressDialog.show();
                progressDialog.setCancelable(false);
                if (newProgress == 100) {

                    //progressBar.setVisibility(View.GONE);

                    progressBarWeb.setVisibility(View.GONE);
                    setTitle(view.getTitle());
                    progressDialog.dismiss();
                }

                super.onProgressChanged(view, newProgress);


            }

            // for Lollipop, all in one uploading code
            public boolean onShowFileChooser(
                    WebView webView, ValueCallback<Uri[]> filePathCallback,
                    FileChooserParams fileChooserParams) {
                if (mFilePathCallback != null) {
                    mFilePathCallback.onReceiveValue(null);
                }
                mFilePathCallback = filePathCallback;

                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

                    // create the file where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                        takePictureIntent.putExtra("PhotoPath", mCameraPhotoPath);
                    } catch (IOException ex) {
                        // Error occurred while creating the File
                        Log.e(TAG, "Unable to create Image File", ex);
                    }

                    // continue only if the file was successfully created
                    if (photoFile != null) {
                        mCameraPhotoPath = "file:" + photoFile.getAbsolutePath();
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                                Uri.fromFile(photoFile));
                    } else {
                        takePictureIntent = null;
                    }
                }

                Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
                contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
                contentSelectionIntent.setType("image/*");

                Intent[] intentArray;
                if (takePictureIntent != null) {
                    intentArray = new Intent[]{takePictureIntent};
                } else {
                    intentArray = new Intent[0];
                }

                Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
                chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
                chooserIntent.putExtra(Intent.EXTRA_TITLE, getString(R.string.image_chooser));
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);

                startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE);

                return true;
            }

            // creating image files (Lollipop only)
            private File createImageFile() throws IOException {

                File imageStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "DirectoryNameHere");

                if (!imageStorageDir.exists()) {
                    imageStorageDir.mkdirs();
                }

                // create an image file name
                imageStorageDir = new File(imageStorageDir + File.separator + "IMG_" + String.valueOf(System.currentTimeMillis()) + ".jpg");
                return imageStorageDir;
            }

            // openFileChooser for Android 3.0+
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
                mUploadMessage = uploadMsg;

                try {
                    File imageStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "DirectoryNameHere");

                    if (!imageStorageDir.exists()) {
                        imageStorageDir.mkdirs();
                    }

                    File file = new File(imageStorageDir + File.separator + "IMG_" + String.valueOf(System.currentTimeMillis()) + ".jpg");

                    mCapturedImageURI = Uri.fromFile(file); // save to the private variable

                    final Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);
                    // captureIntent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                    Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                    i.addCategory(Intent.CATEGORY_OPENABLE);
                    i.setType("image/*");

                    Intent chooserIntent = Intent.createChooser(i, getString(R.string.image_chooser));
                    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Parcelable[]{captureIntent});

                    startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE);
                } catch (Exception e) {
                    Toast.makeText(getBaseContext(), "Camera Exception:" + e, Toast.LENGTH_LONG).show();
                }

            }

            // openFileChooser for Android < 3.0
            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                openFileChooser(uploadMsg, "");
            }


            // above code for uploading


        });

        btnNoInternetConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkConnection();
            }
        });

//download code starting...

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {

                Log.d("permission", "permission denied to WRITE_EXTERNAL_STORAGE - requesting it");
                String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permissions, 1);

            }
        }

        webView.setDownloadListener(new DownloadListener() {

            @Override
            public void onDownloadStart(final String url, final String userAgent, String contentDisposition, String mimetype, long contentLength) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        DowloadDialog(url, userAgent, contentDisposition, mimetype);
                    } else {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                    }
                }
                else
                {
                    DowloadDialog(url, userAgent, contentDisposition, mimetype);
                }


            }
        });
    }

    public void DowloadDialog(final String url, final String userAgent, String contentDisposition, String mimetype)
    {
        String FileName = URLUtil.guessFileName(url,contentDisposition,mimetype);

        FileName = contentDisposition.replace("inline; FileName=", "");
        FileName = FileName.replaceAll(".+UTF-8''", "");
        FileName = FileName.replaceAll("\"", "");
        try {
            FileName = URLDecoder.decode(FileName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("\u26A0 Confirmation:")



                .setMessage("Do you want to Download?\uD83D\uDCE5\n\n"+FileName+" "+' ' )
                .setPositiveButton("Yes ☑", new DialogInterface.OnClickListener() {


                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                        String cookie = CookieManager.getInstance().getCookie(url);
                        request.addRequestHeader("Cookie",cookie);
                        request.addRequestHeader("User-Agent", userAgent);
                        request.allowScanningByMediaScanner();
                        String FileName = contentDisposition.replace("inline; FileName=", "");
                        FileName = FileName.replaceAll(".+UTF-8''", "");
                        FileName = FileName.replaceAll("\"", "");
                        try {
                            FileName = URLDecoder.decode(FileName, "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        request.setTitle(FileName);
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                        FileName = URLUtil.guessFileName(url, null, MimeTypeMap.getFileExtensionFromUrl(url));

                        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,URLUtil.guessFileName(url, contentDisposition,mimetype));
                        DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                        dm.enqueue(request);
                        Toast.makeText(getApplicationContext(), "Downloading File \uD83D\uDCE5" ,Toast.LENGTH_SHORT).show();
                    }

                })


                .setNegativeButton("No ☒", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        dialogInterface.cancel();
                    }
                })
                .show();
        progressBarWeb.setVisibility(View.GONE);
        progressDialog.dismiss();



        //download code end.



    }



    // return here when file selected from camera or from SD Card
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        // code for all versions except of Lollipop
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {

            if (requestCode == FILECHOOSER_RESULTCODE) {
                if (null == this.mUploadMessage) {
                    return;
                }

                Uri result = null;

                try {
                    if (resultCode != RESULT_OK) {
                        result = null;
                    } else {
                        // retrieve from the private variable if the intent is null
                        result = data == null ? mCapturedImageURI : data.getData();
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "activity :" + e, Toast.LENGTH_LONG).show();
                }

                mUploadMessage.onReceiveValue((Uri) result);
                mUploadMessage = null;
            }

        } // end of code for all versions except of Lollipop

        // start of code for Lollipop only
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            if (requestCode != FILECHOOSER_RESULTCODE || mFilePathCallback == null) {
                super.onActivityResult(requestCode, resultCode, data);
                return;
            }

            Uri[] results = null;

            // check that the response is a good one
            if (resultCode == Activity.RESULT_OK) {
                if (data == null || data.getData() == null) {
                    // if there is not data, then we may have taken a photo
                    if (mCameraPhotoPath != null) {
                        results = new Uri[]{Uri.parse(mCameraPhotoPath)};
                    }
                } else {
                    String dataString = data.getDataString();
                    if (dataString != null) {
                        results = new Uri[]{Uri.parse(dataString)};
                    }
                }
            }

            mFilePathCallback.onReceiveValue(results);
            mFilePathCallback = null;

        } // end of code for Lollipop only
    }  //CODE FOR UPLAOD



    @Override
    public void onBackPressed() {
        if(webView.canGoBack()){
            webView.goBack();

        }
        else
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("\u26A0 Confirmation:");
            builder.setMessage("Are you sure you want to exit IMSAF?")
                    .setNegativeButton("Cancel ⌫", null)
                    .setPositiveButton("Exit ⎋", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {


                            finishAffinity();
                        }
                    }).show();

        }
    }

    public void checkConnection(){

        ConnectivityManager connectivityManager = (ConnectivityManager)
                this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileNetwork = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if(wifi.isConnected()){
            webView.loadUrl(webUrl);
            webView.setVisibility(View.VISIBLE);
            relativeLayout.setVisibility(View.GONE);


        }
        else if (mobileNetwork.isConnected()){
            webView.loadUrl(webUrl);
            webView.setVisibility(View.VISIBLE);
            relativeLayout.setVisibility(View.GONE);


        }
        else {
            webView.setVisibility(View.GONE);
            relativeLayout.setVisibility(View.VISIBLE);
            setTitle("IMSAF- No Internet!");

        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.contact_us) {
            Intent intent = new Intent(MainActivity.this, ContactUs.class);
            startActivity(intent);
            return true;
            //} else if (id == R.id.privacy) {
            //  Intent intent = new Intent(MainActivity.this, PrivacyPolicy.class);
            // startActivity(intent);
            // return true;
        } else

            switch (item.getItemId()) {
                case R.id.nav_previous:
                    onBackPressed();
                    //Toast.makeText(this, "Previous Page", Toast.LENGTH_SHORT).show();
                    break;

                case R.id.nav_reload:
                    webView.reload();
                    //Toast.makeText(this, "Refresh", Toast.LENGTH_SHORT).show();
                    break;

                case R.id.nav_next:
                    if (webView.canGoForward()) {
                        webView.goForward();

                      //  Toast.makeText(this, "Next Page", Toast.LENGTH_SHORT).show();
                    }
                    break;

                case R.id.nav_home:
                    checkConnection();

                    //Toast.makeText(this, "Home Page", Toast.LENGTH_SHORT).show();
                    break;


                // case R.id.contact_us:
                //Toast.makeText(this, "Desktop Site", Toast.LENGTH_SHORT).show();


                // if (item.isChecked()) {
                //   item.setChecked(false);
                // desktop = false;
                //} else {
                //  item.setChecked(true);
                //desktop = true;
                //}
                // break;


                case R.id.menu_share:
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/palin");
                    shareIntent.putExtra(Intent.EXTRA_TEXT, waUrl);
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "App Link");
                    startActivity(Intent.createChooser(shareIntent, "Share URL With Friends"));
                    break;

                case R.id.exit:
                    //Toast.makeText(this, "Exit", Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("\u26A0 Confirmation:");
                    builder.setMessage("Are you sure you want to exit IMSAF?")
                            .setNegativeButton("Cancel ⌫", null)
                            .setPositiveButton("Exit ⎋", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int i) {


                                    finishAffinity();
                                }
                            }).show();

                    break;


            }


        return super.onOptionsItemSelected(item);


    }



    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        webView.saveState(outState);

    }


}
