package com.techinvest.pcplrealestate;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.jar.*;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.darsh.multipleimageselect.activities.AlbumSelectActivity;
import com.darsh.multipleimageselect.helpers.Constants;
import com.darsh.multipleimageselect.models.Image;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.techinvest.pcpl.adapter.LayoutdetailsPendingImagesGridViewAdapter;
import com.techinvest.pcpl.commonutil.AppCommonDialog;
import com.techinvest.pcpl.commonutil.AppConstants;
import com.techinvest.pcpl.commonutil.AppSetting;
import com.techinvest.pcpl.model.CommonResponse;
import com.techinvest.pcpl.model.Jj;
import com.techinvest.pcpl.model.SetOfflinedata;
import com.techinvest.pcpl.model.imagesResponse;
import com.techinvest.pcpl.parserhelper.DataBaseHelper;
import com.techinvest.pcpl.parserhelper.GetWebServiceData;
import com.techinvest.pcplrealestate.fragment.Buildingdetails.SetBuildingAsyncTask;

import android.*;
import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import androidx.exifinterface.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
//import android.support.annotation.NonNull;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.app.Fragment;
//import android.support.v4.widget.SwipeRefreshLayout;
import android.provider.OpenableColumns;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import static android.app.Activity.RESULT_OK;

import static com.techinvest.pcpl.commonutil.AppDetails.getActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class Layoutdetails extends Fragment implements OnClickListener {
    private GridView mGridView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LayoutdetailsPendingImagesGridViewAdapter mGridAdapter;
    private ArrayList<SetOfflinedata> mGridData;
    public List<SetOfflinedata> userImgsList;

    //public DataBaseHelper dataBaseHelper;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    Button addphoto;
    public TextView textViewImageUploadStatus;
    private ProgressDialog loadingDialog;
    ImageView mImageView;
    Activity activity;
    View view;
    String tempPath;
    int REQUEST_CAMERA = 0, SELECT_LIBRARY_IMAGE = 1, CROP_PIC=2;
    private String imagePath = "";
    Bitmap bitmap;
    public String encodedImgString;

    String url;
    private Context context;
    private static final int PERMISSION_REQUEST_CODE = 1;

    ArrayList<Uri> filePathUriArrayList;
    private StorageReference mStorageRef;
    private FirebaseStorage storage;
    ArrayList<Image> images;
    public File[] files;
    String pictureFilePath = "";

    public ProgressBar androidProgressBar;
    int progressStatusCounter = 0;
    ArrayList<String> imagePathList;
    int imageCount = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layoutdetails, container, false);
        initview();
        url = AppSetting.getapiURL();

        mImageView = (ImageView) view.findViewById(R.id.ximgPhoto);
        mGridView = (GridView) view.findViewById(R.id.gridView);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh1);

        addphoto = (Button) view.findViewById(R.id.button1);
        textViewImageUploadStatus = (TextView) view.findViewById(R.id.textViewImageUploadStatus);
        androidProgressBar = (ProgressBar) view.findViewById(R.id.infoProgressBar);
        androidProgressBar.setMax(100);
        //androidProgressBar.setVisibility(View.GONE);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mImageView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
                //dispatchSelectPictureIntent();

            }
        });

        ((CheckBox) view.findViewById(R.id.checkmarkascompleted)).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (((CheckBox) view.findViewById(R.id.checkmarkascompleted)).isChecked()) {
                    if (GetWebServiceData.isNetworkAvailable(getActivity())) {
                        new MarkAsCompleteTask().execute();
                    } else {
                        Toast.makeText(getActivity(), getResources().getString(R.string.check_network), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        userImgsList = SecondScreenActivity.mypcplData.getImagesAllvaluesByUser(AppSetting.getUserId(), AppSetting.getRequestId());
        mGridAdapter = new LayoutdetailsPendingImagesGridViewAdapter(getContext(), R.layout.item_image, userImgsList);
        mGridView.setAdapter(mGridAdapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                userImgsList = SecondScreenActivity.mypcplData.getImagesAllvaluesByUser(AppSetting.getUserId(), AppSetting.getRequestId());
                mGridAdapter.setGridData(userImgsList);
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        if (PcplApplication.getInstance().isNetworkConnected()) {
            textViewImageUploadStatus.setText("Image Uploading to server is going on background");
            uploadImgsToServer();
        } else {
            textViewImageUploadStatus.setText("Image Uploading to server is offline");//07
            //userImgsList = dataBaseHelper.getImagesAllvaluesByUser(AppSetting.getUserId());
        }
        return view;
    }

    private void uploadImgsToServer() {
        progressStatusCounter = 0;
        androidProgressBar.setProgress(0);
        try {


            File tempfolder = new File(Environment.getExternalStorageDirectory().toString() + "/PCPL/" + AppSetting.getUserId());
            if (!tempfolder.isDirectory()) {
                tempfolder.mkdirs();
            }

            try {
                userImgsList = SecondScreenActivity.mypcplData.getImagesAllvaluesByUser(AppSetting.getUserId(), AppSetting.getRequestId());
                files = new File[userImgsList.size()];
                for (int i = 0; i < userImgsList.size(); i++) {
                    files[i] = new File(userImgsList.get(i).getSaveuserdata().toString());
                    Log.e(Layoutdetails.class.getName(), files[i].getPath());
                }
                Log.e(Layoutdetails.class.getName(), "Size: " + files.length);
            } catch (Exception ex) {
                Log.e(Layoutdetails.class.getName(), "Exp : " + ex.getMessage().toString());
                Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_LONG).show();
            }

            //files = directory.listFiles();
            if (files == null) {
                return;
            }
            //if(files.length<0){return;}

            Uri[] uri = new Uri[files.length];
            for (int i = 0; i < files.length; i++) {
                if (files[i].exists()) {
                    //uri[i] = Uri.parse("file://" + files[i].getPath());
                    uri[i] = Uri.fromFile(new File(files[i].getAbsolutePath()));
                    final String localFilePath = files[i].getPath();
                    final StorageReference ref = mStorageRef.child("/PCPL/" + AppSetting.getUserId() + "/" + files[i].getName());
                    final UploadTask uploadToBucketTask = ref.putFile(uri[i]);
                    uploadToBucketTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Uri downloadUri = taskSnapshot.getUploadSessionUri();
                            String generatedFilePath = downloadUri.toString();
                            Log.i(Layoutdetails.class.getName(), generatedFilePath);
                            new UploadImagesToServerFromBucketTask(localFilePath).execute(generatedFilePath);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "Uploading Failed : " + e.getMessage().toString(), Toast.LENGTH_LONG).show();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            Log.d(Layoutdetails.class.getName(), String.format("onProgress: bytes=%d total=%d", taskSnapshot.getBytesTransferred(), taskSnapshot.getTotalByteCount()));
                            //Toast.makeText(getContext(), "Progress : "+String.format("onProgress: bytes=%d total=%d",taskSnapshot.getBytesTransferred(), taskSnapshot.getTotalByteCount()),Toast.LENGTH_LONG).show();
                            /*double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            progressStatusCounter = (int)progress;
                            androidProgressBar.incrementProgressBy(progressStatusCounter);*/
                            //double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            long total = task.getResult().getTotalByteCount();
                            long trans = task.getResult().getBytesTransferred();
                            Log.d(Layoutdetails.class.getName(), String.format("onComplete: bytes=%d total=%d", trans, total));
                            if (task.isSuccessful()) {
                                Log.d(Layoutdetails.class.getName(), "onComplete: SUCC");
                                /*progressStatusCounter = 100;
                                androidProgressBar.setProgress(progressStatusCounter);*/
                                //Toast.makeText(getContext(),"Task Complete",Toast.LENGTH_LONG).show();
                            } else {
                                Log.d(Layoutdetails.class.getName(), "onComplete: FAIL " + task.getException().getMessage());
                                //Toast.makeText(getContext(),"Task Inomplete",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } else {
                    //do nothing
                }
            }
        }
        catch (Exception e){
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void dispatchSelectPictureIntent() {
        Intent intent = new Intent(getContext(), AlbumSelectActivity.class);
        intent.putExtra(Constants.INTENT_EXTRA_LIMIT, 5);
        startActivityForResult(intent, Constants.REQUEST_CODE);
    }

    private void initview() {
        view.findViewById(R.id.xbtnSubmit).setOnClickListener(this);
        view.findViewById(R.id.xbtnView).setOnClickListener(this);
        view.findViewById(R.id.xbtnmapView).setOnClickListener(this);

        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    // taking photo from camera
    private void dispatchTakePictureIntent() {


        //ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.WRITE_INTERNAL_STORAGE},PERMISSION_REQUEST_CODE);
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE);

        final CharSequence[] items = {
                getResources().getString(R.string.camera),
                getResources().getString(R.string.gallery),
                getResources().getString(R.string.cancel)};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getResources().getString(R.string.add_photo));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals(getResources()
                        .getString(R.string.camera))) {

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    // ComponentName test = intent.resolveActivity(activity.getApplicationContext().getPackageManager());
                    //if(intent.resolveActivity(activity.getApplicationContext().getPackageManager()) != null){
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        System.out.println(ex);
                    }
                    if (photoFile != null) {
                        Uri photoURI = FileProvider.getUriForFile(activity.getApplicationContext(), BuildConfig.APPLICATION_ID +".provider",photoFile);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                                photoURI);
                        //  startActivityForResult(intent,
                        //       REQUEST_CAMERA_CODE);
                    }
                    // }
                    //File f = new File(Environment.getExternalStorageDirectory(), "temp.jpg");
                    //intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(activity.getApplicationContext(), BuildConfig.APPLICATION_ID +".provider", new File(Environment.getExternalStorageDirectory(), "temp.jpg")));
                    startActivityForResult(intent, REQUEST_CAMERA);
                    getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                } else if (items[item].equals(getResources().getString(
                        R.string.gallery))) {

                    Intent intent = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    //intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            SELECT_LIBRARY_IMAGE);
                } else if (items[item].equals(getResources().getString(
                        R.string.cancel))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private File createImageFile() throws IOException {
        File storageDir;
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        } else {
            storageDir = new File(Environment.getExternalStorageDirectory().toString(), Environment.DIRECTORY_PICTURES);
        }

        storageDir.mkdirs(); // make sure you call mkdirs() and not mkdir()
        File image = File.createTempFile(
                imageFileName,  // prefix
                ".jpg",         // suffix
                storageDir      // directory
        );

        pictureFilePath = image.getAbsolutePath();
        Log.e("our file", image.toString());
        return image;
    }
//        String timeStamp =
//                new SimpleDateFormat("yyyyMMdd_HHmmss",
//                        Locale.getDefault()).format(new Date());
//        String imageFileName = "IMG_" + timeStamp + "_";
//        File storageDir = new File(Environment.getExternalStorageDirectory(),
//                Environment.DIRECTORY_PICTURES);
//        storageDir.mkdirs();
//        File image = File.createTempFile(
//                imageFileName,  /* prefix */
//                ".jpg",         /* suffix */
//                storageDir      /* directory */
//        );
//
//        pictureFilePath = image.getAbsolutePath();
//        return image;
//        // return image;
//    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode ==  Constants.REQUEST_CODE && resultCode == RESULT_OK && data != null) {
                try {
                    loadingDialog = new ProgressDialog(getActivity());
                    loadingDialog.setCancelable(false);
                    loadingDialog.setMessage("Copying...");
                    loadingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

                    loadingDialog.show();

                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }

                ArrayList<Image> images = data.getParcelableArrayListExtra(Constants.INTENT_EXTRA_IMAGES);
                File tempfolder = new File(Environment.getExternalStorageDirectory().toString() + "/PCPL/" + AppSetting.getUserId());
                if (!tempfolder.isDirectory()) {
                    tempfolder.mkdirs();
                }
                else {
                    for (int i = 0; i < images.size(); i++) {
                        File srcFile = new File(images.get(i).path);
                        File destFile;
                        int file_size = Integer.parseInt(String.valueOf(srcFile.length() / 1024));
                        if (file_size > 1024) {
                            Bitmap bitmap = BitmapFactory.decodeFile(srcFile.getPath());
                            destFile = new File(Environment.getExternalStorageDirectory().toString() + "/PCPL/" + AppSetting.getUserId() + "/" + srcFile.getName());
                            OutputStream os;
                            try {
                                os = new FileOutputStream(destFile);
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, os);
                                os.flush();
                                os.close();
                                SecondScreenActivity.mypcplData.insertImagedata(destFile.getPath().toString(), AppSetting.getRequestId(), AppSetting.getUserId());
                                Log.e(Layoutdetails.class.getName(), "ins 1 " + destFile.getName().toString() + "  --  " + file_size);
                                copyImgs(srcFile, destFile);


                            } catch (IOException e) {
                                e.printStackTrace();
                                Toast.makeText(getContext(), "Internal Error !!", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            destFile = new File(Environment.getExternalStorageDirectory().toString() + "/PCPL/" + AppSetting.getUserId() + "/" + images.get(i).name);
                            try {
                                SecondScreenActivity.mypcplData.insertImagedata(destFile.getPath().toString(), AppSetting.getRequestId(), AppSetting.getUserId());
                                Log.e(Layoutdetails.class.getName(), "ins 2 " + destFile.getName().toString() + "  --  " + file_size);
                                copyImgs(srcFile, destFile);


                            } catch (IOException e) {
                                e.printStackTrace();
                                Toast.makeText(getContext(), "Internal Error !!", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                    //}
                    loadingDialog.dismiss();
                /*if(PcplApplication.getInstance().isNetworkConnected()) {
                    uploadImgsToServer();
				}*/
                    if (PcplApplication.getInstance().isNetworkConnected()) {
                        textViewImageUploadStatus.setText("Image Uploading to server is going on background");
                        uploadImgsToServer();
                    } else {
                        textViewImageUploadStatus.setText("Image Uploading to server is offline");//07
                    }

                }
            }
            else if (resultCode == getActivity().RESULT_CANCELED) {
                Toast.makeText(getActivity(),
                        "Google Play Services must be installed.",
                        Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
            else if (requestCode == REQUEST_CAMERA) {
                //imagePathList = new ArrayList<String>();
                //Uri selectedImageUri = Uri.fromFile(new File(pictureFilePath));
                try {


                    File f = new File(pictureFilePath);
//                Uri selectImageUri = FileProvider.getUriForFile(getContext(), BuildConfig.APPLICATION_ID + ".provider",f);
//                performCrop(selectImageUri);
                    //for (File temp : f.listFiles()) {
                    //if (f.getName().equals("temp.jpg")) {
                    //   f = temp;
                    //  break;
                    //  }
                    //}
                    if (f.exists()) {
                        try {
                            long fileSizeInBytes = f.length();

                            long fileSizeInKB = fileSizeInBytes / 1024;

                            long fileSizeInMB = fileSizeInKB / 1024;

//                        Matrix matrix = new Matrix();
//                        matrix.postRotate(0);

                            if (fileSizeInKB > 100) {

                                Bitmap bm;
                                BitmapFactory.Options btmapOptions = new BitmapFactory.Options();
                                btmapOptions.inSampleSize = 2;
                                bm = BitmapFactory.decodeFile(f.getAbsolutePath(), btmapOptions);
                                bm = Bitmap.createScaledBitmap(bm, 500, 500, true);
                                ExifInterface ei = new ExifInterface(pictureFilePath);
                                int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                                        ExifInterface.ORIENTATION_UNDEFINED);

                                Bitmap rotatedBitmap = null;
                                switch (orientation) {

                                    case ExifInterface.ORIENTATION_ROTATE_90:
                                        rotatedBitmap = rotateImage(bm, 90);
                                        break;

                                    case ExifInterface.ORIENTATION_ROTATE_180:
                                        rotatedBitmap = rotateImage(bm, 180);
                                        break;

                                    case ExifInterface.ORIENTATION_ROTATE_270:
                                        rotatedBitmap = rotateImage(bm, 270);
                                        break;

                                    case ExifInterface.ORIENTATION_NORMAL:
                                    default:
                                        rotatedBitmap = bm;
                                }
                                //Bitmap rotatedBitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
                                mImageView.setImageBitmap(rotatedBitmap);
                                // imageView.setBackgroundResource(R.drawable.image_border);
                                FileOutputStream fOut;// = new ByteArrayOutputStream();
                     /*   String path = Environment
                                .getExternalStorageDirectory()
                                + File.separator
                                + "PCPL";

                        File folderPath = new File(path);
                        boolean success = true;
                        if (!folderPath.exists()) {
                            success = folderPath.mkdir();
                        }
                        if (success) {
                            // Do something on success
                            f.delete();
                            OutputStream fOut = null;
                            String filepath = path + File.separator
                                    + String.valueOf(System.currentTimeMillis())
                                    + ".jpg";
*/
                                // File file = new File(filepath);
                                File file = null;
                                try {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                        file = new File(activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES), f.getName());
                                    } else {
                                        file = new File(f.getAbsolutePath());
                                    }
                                } catch (Exception e) {
                                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                                }


                                try {
                                    fOut = new FileOutputStream(file);
                                    rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 90, fOut);
                                    fOut.flush();
                                    fOut.close();
                                    imagePath = file.getPath();
                                    //imagePathList.add(imagePath);

                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Bitmap bm;
                                BitmapFactory.Options btmapOptions = new BitmapFactory.Options();
                                btmapOptions.inSampleSize = 2;
                                bm = BitmapFactory.decodeFile(f.getAbsolutePath(), btmapOptions);
                                bm = Bitmap.createScaledBitmap(bm, 500, 500, true);
                                ExifInterface ei = new ExifInterface(pictureFilePath);
                                int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                                        ExifInterface.ORIENTATION_UNDEFINED);

                                Bitmap rotatedBitmap = null;
                                switch (orientation) {

                                    case ExifInterface.ORIENTATION_ROTATE_90:
                                        rotatedBitmap = rotateImage(bm, 90);
                                        break;

                                    case ExifInterface.ORIENTATION_ROTATE_180:
                                        rotatedBitmap = rotateImage(bm, 180);
                                        break;

                                    case ExifInterface.ORIENTATION_ROTATE_270:
                                        rotatedBitmap = rotateImage(bm, 270);
                                        break;

                                    case ExifInterface.ORIENTATION_NORMAL:
                                    default:
                                        rotatedBitmap = bm;
                                }

                                //Bitmap rotatedBitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
                                mImageView.setImageBitmap(rotatedBitmap);
                                // imageView.setBackgroundResource(R.drawable.image_border);
                                FileOutputStream fOut;// = new ByteArrayOutputStream();
                     /*   String path = Environment
                                .getExternalStorageDirectory()
                                + File.separator
                                + "PCPL";

                        File folderPath = new File(path);
                        boolean success = true;
                        if (!folderPath.exists()) {
                            success = folderPath.mkdir();
                        }
                        if (success) {
                            // Do something on success
                            f.delete();
                            OutputStream fOut = null;
                            String filepath = path + File.separator
                                    + String.valueOf(System.currentTimeMillis())
                                    + ".jpg";
*/
                                // File file = new File(filepath);
                                File file = null;
                                try {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                        file = new File(activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES), f.getName());
                                    } else {
                                        file = new File(f.getAbsolutePath());
                                    }
                                } catch (Exception e) {
                                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                                }


                                try {
                                    fOut = new FileOutputStream(file);
                                    // bm.compress(Bitmap.CompressFormat.JPEG, 50, fOut);
                                    fOut.flush();
                                    fOut.close();
                                    imagePath = file.getPath();
                                    //imagePathList.add(imagePath);

                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }
                catch (Exception e)
                {
                    Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }

            else if (requestCode == SELECT_LIBRARY_IMAGE) {
                //imagePathList = new ArrayList<String>();
//                if (data.getClipData() != null) {
//
//                    int count = data.getClipData().getItemCount();
//                    for (int i=0; i<count; i++) {
//                        Uri selectedImageUri = data.getClipData().getItemAt(i).getUri();
//                        tempPath = getPath(getActivity(), selectedImageUri);
//
//                        File f = new File(tempPath);
//                        if(f.exists()) {
//                            try {
//                                long fileSizeInBytes = f.length();
//
//                                long fileSizeInKB = fileSizeInBytes / 1024;
//
//                                long fileSizeInMB = fileSizeInKB / 1024;
//
//                                if (fileSizeInKB > 100) {
//
//                                    Bitmap bm;
//                                    BitmapFactory.Options btmapOptions = new BitmapFactory.Options();
//                                    btmapOptions.inSampleSize = 2;
//                                    bm = BitmapFactory.decodeFile(f.getAbsolutePath(), btmapOptions);
//                                    bm = Bitmap.createScaledBitmap(bm, 500, 500, true);
//                                    ExifInterface ei = new ExifInterface(tempPath);
//                                    int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
//                                            ExifInterface.ORIENTATION_UNDEFINED);
//
//                                    Bitmap rotatedBitmap = null;
//                                    switch(orientation) {
//
//                                        case ExifInterface.ORIENTATION_ROTATE_90:
//                                            rotatedBitmap = rotateImage(bm, 90);
//                                            break;
//
//                                        case ExifInterface.ORIENTATION_ROTATE_180:
//                                            rotatedBitmap = rotateImage(bm, 180);
//                                            break;
//
//                                        case ExifInterface.ORIENTATION_ROTATE_270:
//                                            rotatedBitmap = rotateImage(bm, 270);
//                                            break;
//
//                                        case ExifInterface.ORIENTATION_NORMAL:
//                                        default:
//                                            rotatedBitmap = bm;
//                                    }
//
//                                    //Bitmap rotatedBitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
//                                    mImageView.setImageBitmap(rotatedBitmap);
//                                    // imageView.setBackgroundResource(R.drawable.image_border);
//                                    FileOutputStream fOut;// = new ByteArrayOutputStream();
//                     /*   String path = Environment
//                                .getExternalStorageDirectory()
//                                + File.separator
//                                + "PCPL";
//
//                        File folderPath = new File(path);
//                        boolean success = true;
//                        if (!folderPath.exists()) {
//                            success = folderPath.mkdir();
//                        }
//                        if (success) {
//                            // Do something on success
//                            f.delete();
//                            OutputStream fOut = null;
//                            String filepath = path + File.separator
//                                    + String.valueOf(System.currentTimeMillis())
//                                    + ".jpg";
//*/
//                                    // File file = new File(filepath);
//                                    File file = null;
//                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//                                        //activity.getExternalFilesDir(null).mkdirs();
//                                        file = new File(activity.getExternalFilesDir(null), f.getName());
//                                    } else {
//                                        file = new File(f.getAbsolutePath());
//                                    }
//
//                                    try {
//                                        fOut = new FileOutputStream(file);
//                                        rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 90, fOut);
//                                        fOut.flush();
//                                        fOut.close();
//                                        imagePath = file.getPath();
//                                        //imagePathList.add(imagePath);
//
//                                    } catch (FileNotFoundException e) {
//                                        e.printStackTrace();
//                                    } catch (IOException e) {
//                                        e.printStackTrace();
//
//                                    } catch (Exception e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//                                else{
//
//                                    Bitmap bm;
//                                    BitmapFactory.Options btmapOptions = new BitmapFactory.Options();
//                                    btmapOptions.inSampleSize = 2;
//                                    bm = BitmapFactory.decodeFile(f.getAbsolutePath(), btmapOptions);
//                                    bm = Bitmap.createScaledBitmap(bm, 500, 500, true);
//                                    ExifInterface ei = new ExifInterface(tempPath);
//                                    int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
//                                            ExifInterface.ORIENTATION_UNDEFINED);
//
//                                    Bitmap rotatedBitmap = null;
//                                    switch(orientation) {
//
//                                        case ExifInterface.ORIENTATION_ROTATE_90:
//                                            rotatedBitmap = rotateImage(bm, 90);
//                                            break;
//
//                                        case ExifInterface.ORIENTATION_ROTATE_180:
//                                            rotatedBitmap = rotateImage(bm, 180);
//                                            break;
//
//                                        case ExifInterface.ORIENTATION_ROTATE_270:
//                                            rotatedBitmap = rotateImage(bm, 270);
//                                            break;
//
//                                        case ExifInterface.ORIENTATION_NORMAL:
//                                        default:
//                                            rotatedBitmap = bm;
//                                    }
//
//                                    //Bitmap rotatedBitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
//                                    mImageView.setImageBitmap(rotatedBitmap);
//                                    // imageView.setBackgroundResource(R.drawable.image_border);
//                                    FileOutputStream fOut;// = new ByteArrayOutputStream();
//                     /*   String path = Environment
//                                .getExternalStorageDirectory()
//                                + File.separator
//                                + "PCPL";
//
//                        File folderPath = new File(path);
//                        boolean success = true;
//                        if (!folderPath.exists()) {
//                            success = folderPath.mkdir();
//                        }
//                        if (success) {
//                            // Do something on success
//                            f.delete();
//                            OutputStream fOut = null;
//                            String filepath = path + File.separator
//                                    + String.valueOf(System.currentTimeMillis())
//                                    + ".jpg";
//*/
//                                    // File file = new File(filepath);
//                                    File file = null;
//                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//                                        //activity.getExternalFilesDir(null).mkdirs();
//                                        file = new File(activity.getExternalFilesDir(null), f.getName());
//                                    } else {
//                                        file = new File(f.getAbsolutePath());
//                                    }
//
//                                    try {
//                                        fOut = new FileOutputStream(file);
//                                        //bm.compress(Bitmap.CompressFormat.JPEG, 50, fOut);
//                                        fOut.flush();
//                                        fOut.close();
//                                        imagePath = file.getPath();
//                                        //imagePathList.add(imagePath);
//
//                                    } catch (FileNotFoundException e) {
//                                        e.printStackTrace();
//                                    } catch (IOException e) {
//                                        e.printStackTrace();
//
//                                    } catch (Exception e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//                }
               // else
                try {


                    if (data.getData() != null) {
                        Uri selectedImageUri = data.getData();
                        tempPath = getPath(getActivity(), selectedImageUri);

                        File f = new File(tempPath);
                        if (f.exists()) {
                            try {
                                long fileSizeInBytes = f.length();

                                long fileSizeInKB = fileSizeInBytes / 1024;

                                long fileSizeInMB = fileSizeInKB / 1024;

                                if (fileSizeInKB > 100) {

                                    Bitmap bm;
                                    BitmapFactory.Options btmapOptions = new BitmapFactory.Options();
                                    btmapOptions.inSampleSize = 2;
                                    bm = BitmapFactory.decodeFile(f.getAbsolutePath(), btmapOptions);
                                    bm = Bitmap.createScaledBitmap(bm, 500, 500, true);
                                    ExifInterface ei = new ExifInterface(tempPath);
                                    int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                                            ExifInterface.ORIENTATION_UNDEFINED);

                                    Bitmap rotatedBitmap = null;
                                    switch (orientation) {

                                        case ExifInterface.ORIENTATION_ROTATE_90:
                                            rotatedBitmap = rotateImage(bm, 90);
                                            break;

                                        case ExifInterface.ORIENTATION_ROTATE_180:
                                            rotatedBitmap = rotateImage(bm, 180);
                                            break;

                                        case ExifInterface.ORIENTATION_ROTATE_270:
                                            rotatedBitmap = rotateImage(bm, 270);
                                            break;

                                        case ExifInterface.ORIENTATION_NORMAL:
                                        default:
                                            rotatedBitmap = bm;
                                    }

                                    //Bitmap rotatedBitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
                                    mImageView.setImageBitmap(rotatedBitmap);
                                    // imageView.setBackgroundResource(R.drawable.image_border);
                                    FileOutputStream fOut;// = new ByteArrayOutputStream();
                     /*   String path = Environment
                                .getExternalStorageDirectory()
                                + File.separator
                                + "PCPL";

                        File folderPath = new File(path);
                        boolean success = true;
                        if (!folderPath.exists()) {
                            success = folderPath.mkdir();
                        }
                        if (success) {
                            // Do something on success
                            f.delete();
                            OutputStream fOut = null;
                            String filepath = path + File.separator
                                    + String.valueOf(System.currentTimeMillis())
                                    + ".jpg";
*/
                                    // File file = new File(filepath);
                                    File file = null;
                                    try {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                                            //activity.getExternalFilesDir(null).mkdirs();
                                            file = new File(activity.getExternalFilesDir(null), f.getName());
                                        } else {
                                            file = new File(f.getAbsolutePath());
                                        }
                                    } catch (Exception e) {
                                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                                    }


                                    try {
                                        fOut = new FileOutputStream(file);
                                        rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 90, fOut);
                                        fOut.flush();
                                        fOut.close();
                                        imagePath = file.getPath();
                                        //imagePathList.add(imagePath);

                                    } catch (FileNotFoundException e) {
                                        e.printStackTrace();
                                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                } else {

                                    Bitmap bm;
                                    BitmapFactory.Options btmapOptions = new BitmapFactory.Options();
                                    btmapOptions.inSampleSize = 2;
                                    bm = BitmapFactory.decodeFile(f.getAbsolutePath(), btmapOptions);
                                    bm = Bitmap.createScaledBitmap(bm, 500, 500, true);
                                    ExifInterface ei = new ExifInterface(tempPath);
                                    int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                                            ExifInterface.ORIENTATION_UNDEFINED);

                                    Bitmap rotatedBitmap = null;
                                    switch (orientation) {

                                        case ExifInterface.ORIENTATION_ROTATE_90:
                                            rotatedBitmap = rotateImage(bm, 90);
                                            break;

                                        case ExifInterface.ORIENTATION_ROTATE_180:
                                            rotatedBitmap = rotateImage(bm, 180);
                                            break;

                                        case ExifInterface.ORIENTATION_ROTATE_270:
                                            rotatedBitmap = rotateImage(bm, 270);
                                            break;

                                        case ExifInterface.ORIENTATION_NORMAL:
                                        default:
                                            rotatedBitmap = bm;
                                    }

                                    //Bitmap rotatedBitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
                                    mImageView.setImageBitmap(rotatedBitmap);
                                    // imageView.setBackgroundResource(R.drawable.image_border);
                                    FileOutputStream fOut;// = new ByteArrayOutputStream();
                     /*   String path = Environment
                                .getExternalStorageDirectory()
                                + File.separator
                                + "PCPL";

                        File folderPath = new File(path);
                        boolean success = true;
                        if (!folderPath.exists()) {
                            success = folderPath.mkdir();
                        }
                        if (success) {
                            // Do something on success
                            f.delete();
                            OutputStream fOut = null;
                            String filepath = path + File.separator
                                    + String.valueOf(System.currentTimeMillis())
                                    + ".jpg";
*/
                                    // File file = new File(filepath);
                                    File file = null;
                                    try {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                                            //activity.getExternalFilesDir(null).mkdirs();
                                            file = new File(activity.getExternalFilesDir(null), f.getName());
                                        } else {
                                            file = new File(f.getAbsolutePath());
                                        }
                                    } catch (Exception e) {
                                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                                    }


                                    try {
                                        fOut = new FileOutputStream(file);
                                        //bm.compress(Bitmap.CompressFormat.JPEG, 50, fOut);
                                        fOut.flush();
                                        fOut.close();
                                        imagePath = file.getPath();
                                        //imagePathList.add(imagePath);

                                    } catch (FileNotFoundException e) {
                                        e.printStackTrace();
                                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }catch (Exception e)
                {
                    Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_LONG).show();
                }

//                Uri selectedImageUri = data.getData();
//                //performCrop(selectedImageUri);
//                tempPath = getPath(getActivity(), selectedImageUri);
//
//                File f = new File(tempPath);
//                if(f.exists()) {
//                    try {
//                        long fileSizeInBytes = f.length();
//
//                        long fileSizeInKB = fileSizeInBytes / 1024;
//
//                        long fileSizeInMB = fileSizeInKB / 1024;
//
//                        if (fileSizeInKB > 100) {
//
//                            Bitmap bm;
//                            BitmapFactory.Options btmapOptions = new BitmapFactory.Options();
//                            btmapOptions.inSampleSize = 2;
//                            bm = BitmapFactory.decodeFile(f.getAbsolutePath(), btmapOptions);
//                            bm = Bitmap.createScaledBitmap(bm, 500, 500, true);
//                            ExifInterface ei = new ExifInterface(tempPath);
//                            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
//                                    ExifInterface.ORIENTATION_UNDEFINED);
//
//                            Bitmap rotatedBitmap = null;
//                            switch(orientation) {
//
//                                case ExifInterface.ORIENTATION_ROTATE_90:
//                                    rotatedBitmap = rotateImage(bm, 90);
//                                    break;
//
//                                case ExifInterface.ORIENTATION_ROTATE_180:
//                                    rotatedBitmap = rotateImage(bm, 180);
//                                    break;
//
//                                case ExifInterface.ORIENTATION_ROTATE_270:
//                                    rotatedBitmap = rotateImage(bm, 270);
//                                    break;
//
//                                case ExifInterface.ORIENTATION_NORMAL:
//                                default:
//                                    rotatedBitmap = bm;
//                            }
//
//                            //Bitmap rotatedBitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
//                            mImageView.setImageBitmap(rotatedBitmap);
//                            // imageView.setBackgroundResource(R.drawable.image_border);
//                            FileOutputStream fOut;// = new ByteArrayOutputStream();
//                     /*   String path = Environment
//                                .getExternalStorageDirectory()
//                                + File.separator
//                                + "PCPL";
//
//                        File folderPath = new File(path);
//                        boolean success = true;
//                        if (!folderPath.exists()) {
//                            success = folderPath.mkdir();
//                        }
//                        if (success) {
//                            // Do something on success
//                            f.delete();
//                            OutputStream fOut = null;
//                            String filepath = path + File.separator
//                                    + String.valueOf(System.currentTimeMillis())
//                                    + ".jpg";
//*/
//                            // File file = new File(filepath);
//                            File file = null;
//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//                                //activity.getExternalFilesDir(null).mkdirs();
//                                file = new File(activity.getExternalFilesDir(null), f.getName());
//                            } else {
//                                file = new File(f.getAbsolutePath());
//                            }
//
//                            try {
//                                fOut = new FileOutputStream(file);
//                                rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 90, fOut);
//                                fOut.flush();
//                                fOut.close();
//                                imagePath = file.getPath();
//
//                            } catch (FileNotFoundException e) {
//                                e.printStackTrace();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//                        else{
//
//                            Bitmap bm;
//                            BitmapFactory.Options btmapOptions = new BitmapFactory.Options();
//                            btmapOptions.inSampleSize = 2;
//                            bm = BitmapFactory.decodeFile(f.getAbsolutePath(), btmapOptions);
//                            bm = Bitmap.createScaledBitmap(bm, 500, 500, true);
//                            ExifInterface ei = new ExifInterface(tempPath);
//                            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
//                                    ExifInterface.ORIENTATION_UNDEFINED);
//
//                            Bitmap rotatedBitmap = null;
//                            switch(orientation) {
//
//                                case ExifInterface.ORIENTATION_ROTATE_90:
//                                    rotatedBitmap = rotateImage(bm, 90);
//                                    break;
//
//                                case ExifInterface.ORIENTATION_ROTATE_180:
//                                    rotatedBitmap = rotateImage(bm, 180);
//                                    break;
//
//                                case ExifInterface.ORIENTATION_ROTATE_270:
//                                    rotatedBitmap = rotateImage(bm, 270);
//                                    break;
//
//                                case ExifInterface.ORIENTATION_NORMAL:
//                                default:
//                                    rotatedBitmap = bm;
//                            }
//
//                            //Bitmap rotatedBitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
//                            mImageView.setImageBitmap(rotatedBitmap);
//                            // imageView.setBackgroundResource(R.drawable.image_border);
//                            FileOutputStream fOut;// = new ByteArrayOutputStream();
//                     /*   String path = Environment
//                                .getExternalStorageDirectory()
//                                + File.separator
//                                + "PCPL";
//
//                        File folderPath = new File(path);
//                        boolean success = true;
//                        if (!folderPath.exists()) {
//                            success = folderPath.mkdir();
//                        }
//                        if (success) {
//                            // Do something on success
//                            f.delete();
//                            OutputStream fOut = null;
//                            String filepath = path + File.separator
//                                    + String.valueOf(System.currentTimeMillis())
//                                    + ".jpg";
//*/
//                            // File file = new File(filepath);
//                            File file = null;
//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//                                //activity.getExternalFilesDir(null).mkdirs();
//                                file = new File(activity.getExternalFilesDir(null), f.getName());
//                            } else {
//                                file = new File(f.getAbsolutePath());
//                            }
//
//                            try {
//                                fOut = new FileOutputStream(file);
//                                //bm.compress(Bitmap.CompressFormat.JPEG, 50, fOut);
//                                fOut.flush();
//                                fOut.close();
//                                imagePath = file.getPath();
//
//                            } catch (FileNotFoundException e) {
//                                e.printStackTrace();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }

            }

//            else if (requestCode == CROP_PIC) {
//                // get the returned data
//                Bundle extras = data.getExtras();
//                // get the cropped bitmap
//                Bitmap thePic = extras.getParcelable("data");
//                //ImageView picView = (ImageView) findViewById(R.id.picture);
//                mImageView.setImageBitmap(thePic);
//            File f = new File(context.getCacheDir(), "file");
//            try {
//                f.createNewFile();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
////Convert bitmap to byte array
//            Bitmap bitmap = thePic;
//            ByteArrayOutputStream bos = new ByteArrayOutputStream();
//            bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
//            byte[] bitmapdata = bos.toByteArray();
//
////write the bytes in file
//            FileOutputStream fos = null;
//            try {
//                fos = new FileOutputStream(f);
//                fos.write(bitmapdata);
//                fos.flush();
//                fos.close();
//
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//                if(f.exists()) {
//                    try {
//                        long fileSizeInBytes = f.length();
//
//                        long fileSizeInKB = fileSizeInBytes / 1024;
//
//                        long fileSizeInMB = fileSizeInKB / 1024;
//
//                        if (fileSizeInKB > 100) {
//
//                            Bitmap bm;
//                            BitmapFactory.Options btmapOptions = new BitmapFactory.Options();
//                            btmapOptions.inSampleSize = 2;
//                            bm = BitmapFactory.decodeFile(f.getAbsolutePath(), btmapOptions);
//                            bm = Bitmap.createScaledBitmap(bm, 500, 500, true);
//                            ExifInterface ei = new ExifInterface(f.getPath());
//                            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
//                                    ExifInterface.ORIENTATION_UNDEFINED);
//
//                            Bitmap rotatedBitmap = null;
//                            switch(orientation) {
//
//                                case ExifInterface.ORIENTATION_ROTATE_90:
//                                    rotatedBitmap = rotateImage(bm, 90);
//                                    break;
//
//                                case ExifInterface.ORIENTATION_ROTATE_180:
//                                    rotatedBitmap = rotateImage(bm, 180);
//                                    break;
//
//                                case ExifInterface.ORIENTATION_ROTATE_270:
//                                    rotatedBitmap = rotateImage(bm, 270);
//                                    break;
//
//                                case ExifInterface.ORIENTATION_NORMAL:
//                                default:
//                                    rotatedBitmap = bm;
//                            }
//
//                            //Bitmap rotatedBitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
//                            mImageView.setImageBitmap(rotatedBitmap);
//                            // imageView.setBackgroundResource(R.drawable.image_border);
//                            FileOutputStream fOut;// = new ByteArrayOutputStream();
//                     /*   String path = Environment
//                                .getExternalStorageDirectory()
//                                + File.separator
//                                + "PCPL";
//
//                        File folderPath = new File(path);
//                        boolean success = true;
//                        if (!folderPath.exists()) {
//                            success = folderPath.mkdir();
//                        }
//                        if (success) {
//                            // Do something on success
//                            f.delete();
//                            OutputStream fOut = null;
//                            String filepath = path + File.separator
//                                    + String.valueOf(System.currentTimeMillis())
//                                    + ".jpg";
//*/
//                            // File file = new File(filepath);
//                            File file = null;
//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//                                //activity.getExternalFilesDir(null).mkdirs();
//                                file = new File(activity.getExternalFilesDir(null), f.getName());
//                            } else {
//                                file = new File(f.getAbsolutePath());
//                            }
//
//                            try {
//                                fOut = new FileOutputStream(file);
//                                rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 90, fOut);
//                                fOut.flush();
//                                fOut.close();
//                                imagePath = file.getPath();
//
//                            } catch (FileNotFoundException e) {
//                                e.printStackTrace();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//                        else{
//
//                            Bitmap bm;
//                            BitmapFactory.Options btmapOptions = new BitmapFactory.Options();
//                            btmapOptions.inSampleSize = 2;
//                            bm = BitmapFactory.decodeFile(f.getAbsolutePath(), btmapOptions);
//                            bm = Bitmap.createScaledBitmap(bm, 500, 500, true);
//                            ExifInterface ei = new ExifInterface(tempPath);
//                            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
//                                    ExifInterface.ORIENTATION_UNDEFINED);
//
//                            Bitmap rotatedBitmap = null;
//                            switch(orientation) {
//
//                                case ExifInterface.ORIENTATION_ROTATE_90:
//                                    rotatedBitmap = rotateImage(bm, 90);
//                                    break;
//
//                                case ExifInterface.ORIENTATION_ROTATE_180:
//                                    rotatedBitmap = rotateImage(bm, 180);
//                                    break;
//
//                                case ExifInterface.ORIENTATION_ROTATE_270:
//                                    rotatedBitmap = rotateImage(bm, 270);
//                                    break;
//
//                                case ExifInterface.ORIENTATION_NORMAL:
//                                default:
//                                    rotatedBitmap = bm;
//                            }
//
//                            //Bitmap rotatedBitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
//                            mImageView.setImageBitmap(rotatedBitmap);
//                            // imageView.setBackgroundResource(R.drawable.image_border);
//                            FileOutputStream fOut;// = new ByteArrayOutputStream();
//                     /*   String path = Environment
//                                .getExternalStorageDirectory()
//                                + File.separator
//                                + "PCPL";
//
//                        File folderPath = new File(path);
//                        boolean success = true;
//                        if (!folderPath.exists()) {
//                            success = folderPath.mkdir();
//                        }
//                        if (success) {
//                            // Do something on success
//                            f.delete();
//                            OutputStream fOut = null;
//                            String filepath = path + File.separator
//                                    + String.valueOf(System.currentTimeMillis())
//                                    + ".jpg";
//*/
//                            // File file = new File(filepath);
//                            File file = null;
//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//                                //activity.getExternalFilesDir(null).mkdirs();
//                                file = new File(activity.getExternalFilesDir(null), f.getName());
//                            } else {
//                                file = new File(f.getAbsolutePath());
//                            }
//
//                            try {
//                                fOut = new FileOutputStream(file);
//                                //bm.compress(Bitmap.CompressFormat.JPEG, 50, fOut);
//                                fOut.flush();
//                                fOut.close();
//                                imagePath = file.getPath();
//
//                            } catch (FileNotFoundException e) {
//                                e.printStackTrace();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//
//            }
        }
    }

//    private void performCrop(Uri uri) {
//        // take care of exceptions
//        try {
//            // call the standard crop action intent (the user device may not
//            // support it)
//            Intent cropIntent = new Intent("com.android.camera.action.CROP");
//            // indicate image type and Uri
//            cropIntent.setDataAndType(uri, "image/*");
//            // set crop properties
//            cropIntent.putExtra("crop", "true");
//            // indicate aspect of desired crop
//            cropIntent.putExtra("aspectX", 1);
//            cropIntent.putExtra("aspectY", 1);
//            // indicate output X and Y
//            cropIntent.putExtra("outputX", 256);
//            cropIntent.putExtra("outputY", 256);
//            // retrieve data on return
//            cropIntent.putExtra("return-data", true);
//            // start the activity - we handle returning in onActivityResult
//            startActivityForResult(cropIntent, CROP_PIC);
//        }
//        // respond to users whose devices do not support the crop action
//        catch (ActivityNotFoundException anfe) {
//        Toast.makeText(getContext(), "This device doesn't support the crop action!", Toast.LENGTH_SHORT).show();
//        }
//    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    public static String getPath(final Context context, final Uri uri) {

        try {
            final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
            // DocumentProvider
            if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
                // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    if ("primary".equalsIgnoreCase(type)) {
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
//                       return Environment.getStorageDirectory().getAbsolutePath()+ "/" + split[1];
//                    else
                        return Environment.getExternalStorageDirectory() + "/" + split[1];
                    }
                    // TODO handle non-primary volumes
                }
                else if (isDownloadsDocument(uri)) {

                    final String id = DocumentsContract.getDocumentId(uri);

                    if (id != null && id.startsWith("raw:")) {
                        return id.substring(4);
                    }

                    String otherId = null;
                    if (id != null && id.startsWith("msf:")) {
                        String[] split = id.split(":");
                        otherId = split[1];
                    }

                    String[] contentUriPrefixesToTry = new String[]{
                            "content://downloads/public_downloads",
                            "content://downloads/my_downloads",
                            "content://downloads/all_downloads"
                    };

                    for (String contentUriPrefix : contentUriPrefixesToTry) {
                        Uri contentUri = null;

                        if (otherId == null) {
                            contentUri = ContentUris.withAppendedId(Uri.parse(contentUriPrefix), Long.valueOf(id));
                        } else {
                            contentUri = ContentUris.withAppendedId(Uri.parse(contentUriPrefix), Long.parseLong(otherId));
                        }
                        try {
                            String path = getDataColumn(context, contentUri, null, null);
                            if (path != null) {
                                return path;
                            }
                        } catch (Exception e) {
                        }
                    }

                    // path could not be retrieved using ContentResolver, therefore copy file to accessible cache using streams
                    String fileName = getFileName(context, uri);
                    File cacheDir = context.getExternalCacheDir();
                    File file = generateFileName(fileName, cacheDir);
                    String destinationPath = null;
                    if (file != null) {
                        destinationPath = file.getAbsolutePath();
                        saveFileFromUri(context, uri, destinationPath);
                    }

                    return destinationPath;
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
                    final String[] selectionArgs = new String[]{
                            split[1]
                    };

                    return getDataColumn(context, contentUri, selection, selectionArgs);
                }
            }
            // MediaStore (and general)
            else if ("content".equalsIgnoreCase(uri.getScheme())) {
                // Return the remote address
                if (isGooglePhotosUri(uri)) {
                    return uri.getLastPathSegment();
                }

                return getDataColumn(context, uri, null, null);
            }
            // File
            else if ("file".equalsIgnoreCase(uri.getScheme())) {
                return uri.getPath();
            }
        }catch (Exception e)
        {

        }

        return null;
    }

    public static File generateFileName(@Nullable String name, File directory) {
        if (name == null) {
            return null;
        }

        File file = new File(directory, name);

        if (file.exists()) {
            String fileName = name;
            String extension = "";
            int dotIndex = name.lastIndexOf('.');
            if (dotIndex > 0) {
                fileName = name.substring(0, dotIndex);
                extension = name.substring(dotIndex);
            }

            int index = 0;

            while (file.exists()) {
                index++;
                name = fileName + '(' + index + ')' + extension;
                file = new File(directory, name);
            }
        }

        try {
            if (!file.createNewFile()) {
                return null;
            }
        } catch (IOException e) {

            return null;
        }
        return file;
    }

    private static void saveFileFromUri(Context context, Uri uri, String destinationPath) {
        InputStream is = null;
        BufferedOutputStream bos = null;
        try {
            is = context.getContentResolver().openInputStream(uri);
            bos = new BufferedOutputStream(new FileOutputStream(destinationPath, false));
            byte[] buf = new byte[1024];
            is.read(buf);
            do {
                bos.write(buf);
            } while (is.read(buf) != -1);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) is.close();
                if (bos != null) bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static String getName(String filename) {
        if (filename == null) {
            return null;
        }
        int index = filename.lastIndexOf('/');
        return filename.substring(index + 1);
    }

    public static String getFileName(@NonNull Context context, Uri uri) {
        String mimeType = context.getContentResolver().getType(uri);
        String filename = null;

        if (mimeType == null && context != null) {
            String path = getPath(context, uri);
            if (path == null) {
                filename = getName(uri.toString());
            } else {
                File file = new File(path);
                filename = file.getName();
            }
        } else {
            Cursor returnCursor = context.getContentResolver().query(uri, null,
                    null, null, null);
            if (returnCursor != null) {
                int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                returnCursor.moveToFirst();
                filename = returnCursor.getString(nameIndex);
                returnCursor.close();
            }
        }

        return filename;
    }


    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
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

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
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

//    public String getPath(Uri uri, Activity activity) {
//        String[] projection = {MediaStore.MediaColumns.DATA};
//        String result;
//        @SuppressWarnings("deprecation")
//        Cursor cursor = activity.getContentResolver().query(uri, projection, null, null, null);
//        if (cursor == null) {
//            result = uri.getPath();
//        } else {
//            cursor.moveToFirst();
//            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
//            result = cursor.getString(column_index);
//            cursor.close();
//        }
//        return result;
//    }


    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.xbtnSubmit:

                if (GetWebServiceData.isNetworkAvailable(getActivity())) {
                    try{
                        if (imagePath != null && imagePath.length() > 0) {
                            new UploadImagesAsyncTask().execute();
                        } else {
                            Toast.makeText(getActivity(), "Please capture images or select from gallery", Toast.LENGTH_LONG).show();
                        }
                    }catch (Exception e){
                        Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
                    }

                } else {
                    AppCommonDialog.showSimpleDialog(getActivity(),
                            getResources().getString(R.string.app_name),
                            getResources().getString(R.string.check_network),
                            getResources().getString(R.string.ok), "OK");
                }

                break;
            case R.id.xbtnmapView:
                if(AppSetting.getUserLatitude().equals(null) && AppSetting.getUserLongitude().equals(null) || AppSetting.getUserLatitude().equalsIgnoreCase("") && AppSetting.getUserLongitude().equalsIgnoreCase("")) {
                    AppCommonDialog.showSimpleDialog(getActivity(),
                            getResources().getString(R.string.app_name),
                            "Please enter latitude and longitude in locationDetail Screen",
                            getResources().getString(R.string.ok), "OK");
                }
                else {
                    Intent intentmap = new Intent(getActivity(), MapviewActivity.class);
                    startActivity(intentmap);
                }
                break;


            case R.id.xbtnView:
                Intent intent = new Intent(getActivity(), PcplImagesDetail.class);
                startActivity(intent);
                break;

            default:
                break;
        }
    }

    //  Task
    public class MarkAsCompleteTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            try {
                loadingDialog = new ProgressDialog(getActivity());
                loadingDialog.setCancelable(false);
                loadingDialog.setMessage("Loading..");
                loadingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

                loadingDialog.show();

            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            String data = null;
            List<NameValuePair> nameValuePairs = null;

            if (AppSetting.getRequestId() != null) {
                nameValuePairs = new ArrayList<NameValuePair>(1);
                nameValuePairs.add(new BasicNameValuePair("RequestId", AppSetting.getRequestId()));
                System.out.println("Request str: " + "post_data" + nameValuePairs.toString());
            }
            return GetWebServiceData.getServerResponse(url+ "/SetAsComplete", nameValuePairs, data);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                if (loadingDialog != null && loadingDialog.isShowing()) {
                    loadingDialog.dismiss();
                }
            } catch (IllegalArgumentException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            if (result != null) {
                Log.i("Response", result);
                try {
                    JSONObject serverResponsere = new JSONObject(result);

                    CommonResponse serverResponseRequest = new Gson().fromJson(
                            result, CommonResponse.class);

                    String loginResult = serverResponsere.getString("status");
                    String message = serverResponsere.getString("Remarks");
                    if (loginResult.equals("OK")) {
                        //Log.d("reuest detail", "get data");
                        if (message != null)
                            Toast.makeText(getActivity(), "Case Submitted Successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getActivity(), SecondScreenActivity.class);
                            startActivity(intent);
                    } else {
                        // Toast.makeText(getApplicationContext(),
                        // faultObject.getString("faultstring"),
                        // Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Log.d("LoginActivity", "JSON Result parse error");
                    e.printStackTrace();
                }

            }
        }

    }

    //  Task
    public class UploadImagesAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            try {
                loadingDialog = new ProgressDialog(getActivity());
                loadingDialog.setCancelable(false);
                loadingDialog.setMessage("Loading..");
                loadingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

                loadingDialog.show();

            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
                Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            String resultdata = null;
            String data = "mumbai";
            List<NameValuePair> nameValuePairs = null;
try {
    if (AppSetting.getRequestId() != null && imagePath != null && imagePath.length() > 0) {
               /* BitmapFactory.Options options = null;
                options = new BitmapFactory.Options();
                options.inSampleSize = 3;
                bitmap = BitmapFactory.decodeFile(imagePath,options);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
                byte[] byte_arr = stream.toByteArray();
                encodedString = Base64.encodeToString(byte_arr, 0);*/

        //encodedString = Base64.encodeBytes(byte_arr);
        //System.out.println("Base 64 String: " + "file path" + encodedString);
        Log.e("1111", "Path \n" + imagePath + "\nBase 64 String : " + encodeImage(imagePath).trim());
        //System.out.println("Path \n"+imagePath+"\nBase 64 String : "+encodeImage(imagePath).trim());

        //for (int i=0;i<imagePathList.size();i++){
            encodedImgString = encodeImage(imagePath).trim();
            nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("myBase64String", encodedImgString));
            nameValuePairs.add(new BasicNameValuePair("fileName", "image"));
            nameValuePairs.add(new BasicNameValuePair("requestID", AppSetting.getRequestId()));
            nameValuePairs.add(new BasicNameValuePair("filepath", imagePath));

        //}

        System.out.println("Request multipart final: " + "post_data" + nameValuePairs.toString());
    }
}catch(Exception e){
    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
}
            return GetWebServiceData.getServerResponse(url + "/UploadFile", nameValuePairs, data);
            //return String.valueOf(imageCount);

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                if (loadingDialog != null && loadingDialog.isShowing()) {
                    loadingDialog.dismiss();
                }
            } catch (IllegalArgumentException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

//            if (result != null) {
//                try {
//                    JSONObject serverResponsere = new JSONObject(result);
//                    CommonResponse serverResponseRequest = new Gson().fromJson(result, CommonResponse.class);
//
//                    String loginResult = serverResponsere.getString("status");
//                    String message = serverResponsere.getString("Remarks");
//                    if (loginResult.equals("OK")) {
//                        imageCount++;
//                        //Toast.makeText(getActivity(), "Uploading image "+ imageCount + " out of ")
//                    } else {
//                        // Toast.makeText(getApplicationContext(),
//                        // faultObject.getString("faultstring"),
//                        // Toast.LENGTH_SHORT).show();
//                    }
//
//                } catch (JSONException e) {
//                    // TODO Auto-generated catch block
//                    Log.d("LoginActivity", "JSON Result parse error");
//                    e.printStackTrace();
//                }
//
//            }

            if (result != null) {
                try {
                    JSONObject serverResponsere = new JSONObject(result);
                    CommonResponse serverResponseRequest = new Gson().fromJson(result, CommonResponse.class);

                    String loginResult = serverResponsere.getString("status");
                    String message = serverResponsere.getString("Remarks");
                    if (loginResult.equals("OK")) {
                        Log.d("request detail", "get data");
                        if (message != null) {
                            Toast.makeText(getActivity(), "Successfully uploaded all Images", Toast.LENGTH_LONG).show();
                            mImageView.setImageDrawable(getResources().getDrawable(R.drawable.pcpl_icon));
                            encodedImgString = "";
                            imagePath = "";
                            //imagePathList.removeAll(imagePathList);
                            //imageCount = 0;
                        }

                        /*
                         * if (serverResponseRequest != null&&
                         * serverResponseRequest.getStatus()!=null) {
                         * AppCommonDialog.showSimpleDialog(getActivity(),
                         * getResources
                         * ().getString(R.string.app_name),"Data Update Sucessfully"
                         * , getResources().getString(R.string.ok), "OK"); }
                         */

                    } else {
                        // Toast.makeText(getApplicationContext(),
                        // faultObject.getString("faultstring"),
                        // Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Log.d("LoginActivity", "JSON Result parse error");
                    e.printStackTrace();
                    Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
                }

            }
        }

    }

    private String encodeImage(String path) {
        File imagefile = new File(path);
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(imagefile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Bitmap bm = BitmapFactory.decodeStream(fis);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);
        //Base64.de
        return encImage;

    }

    public class UploadImagesToServerFromBucketTask extends AsyncTask<String, Void, String> {
        String localFilePath;

        public UploadImagesToServerFromBucketTask(String path) {
            this.localFilePath = path;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            /*try {
				loadingDialog = new ProgressDialog(getActivity());
				loadingDialog.setCancelable(false);
				loadingDialog.setMessage("Loading..");
				loadingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

				loadingDialog.show();

			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}*/
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            String imgUrl = params[0];
            String data = null;
            List<NameValuePair> nameValuePairs = null;

            if (AppSetting.getRequestId() != null) {
                nameValuePairs = new ArrayList<NameValuePair>(1);
                nameValuePairs.add(new BasicNameValuePair("RequestID", AppSetting.getRequestId()));
                nameValuePairs.add(new BasicNameValuePair("ImageURl", imgUrl));
                nameValuePairs.add(new BasicNameValuePair("CreatedBy", AppSetting.getUserId()));

                System.out.println("Request str: " + "post_data" + nameValuePairs.toString());
            }
            return GetWebServiceData.getServerResponse(url
                    + "/StoreMobileImages", nameValuePairs, data);
        }

        @TargetApi(Build.VERSION_CODES.KITKAT)
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
			/*try
			{
				if (loadingDialog != null && loadingDialog.isShowing()) {
					loadingDialog.dismiss();
				}
			} catch (IllegalArgumentException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}*/
            //loadingDialog.dismiss();
            if (result != null) {
                //Log.i(Layoutdetails.class.getName(), result);


//				loadingDialog.dismiss();
                try {
                    JSONObject serverResponsere = new JSONObject(result);
                    JSONArray temp = serverResponsere.getJSONArray("jj");

                    JSONObject t1 = temp.getJSONObject(0);
                    String tmp = t1.getString("Column1");
                    Log.i(Layoutdetails.class.getName(), tmp);
                    if (tmp.equals("Images Details Save Successfully")) {
                        boolean isImageUploaded = SecondScreenActivity.mypcplData.updateImagedata(AppSetting.getRequestId(), localFilePath, localFilePath, AppSetting.getUserId(), 1);
                        //List<SetOfflinedata> tempPendingUploadImages = SecondScreenActivity.mypcplData.getPendingUploadImages(AppSetting.getUserId(),AppSetting.getRequestId());

                        if (isImageUploaded)
                        {
                            File fdelete = new File(localFilePath);
                            if (fdelete.exists()) {
                                if (fdelete.delete()) {
                                    System.out.println("file Deleted :" + localFilePath);
                                    Log.i(Layoutdetails.class.getName(), "file Deleted :" + localFilePath);
                                    //boolean recorddeleteresult = SecondScreenActivity.mypcplData.deleteImageRecord(AppSetting.getRequestId(), 1);
                                    progressStatusCounter = progressStatusCounter + 100 / files.length;
                                    Log.e("123", "Counter: " + progressStatusCounter);
                                    androidProgressBar.setProgress(progressStatusCounter);
                                    textViewImageUploadStatus.setText("Image Uploading to server is going on background" + progressStatusCounter + "% Complete");
                                } else {
                                    System.out.println("file not Deleted :" + localFilePath);
                                    Log.i(Layoutdetails.class.getName(), "file not Deleted :" + localFilePath);
                                }
                                userImgsList = SecondScreenActivity.mypcplData.getImagesAllvaluesByUser(AppSetting.getUserId(), AppSetting.getRequestId());
                                // mGridAdapter.setGridData(userImgsList);
                            }
                        }
                    }

					/*JSONArray serverResponse = new JSONArray(result);
					Log.e(Layoutdetails.class.getName(), "jj:" + serverResponse);
					for (int j = 0; j < serverResponse.length(); j++)
					{
						JSONObject jsonObject = serverResponse.getJSONObject(0);
						//String msg = jsonObject.getJSONObject("Column1");
						Log.e(Layoutdetails.class.getName(), "msg :" + jsonObject);
						if (jsonObject.equals("Images Details Save Successfully"))
						{
							//009
							File fdelete = new File(localFilePath);
							if (fdelete.exists()) {
								if (fdelete.delete()) {
									System.out.println("file Deleted :" + localFilePath);
								} else {
									System.out.println("file not Deleted :" + localFilePath);
								}
							}
						}
						else {
							Toast.makeText(getContext(),"Fail To Upload Image ",Toast.LENGTH_LONG).show();
						}
					}*/
					/*JSONObject serverResponsere = new JSONObject(result);
					CommonResponse serverResponseRequest = new Gson().fromJson(
							result, CommonResponse.class);

					String loginResult =serverResponsere.getString("status");
					String message =serverResponsere.getString("Remarks");
					if (loginResult.equals("OK")) {
						//Log.d("reuest detail", "get data");
						if(message!=null)
							Toast.makeText(getActivity(),"sucessfully "  +message,Toast.LENGTH_SHORT).show();

					} else {
						// Toast.makeText(getApplicationContext(),
						// faultObject.getString("faultstring"),
						// Toast.LENGTH_SHORT).show();
					}*/

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Log.d("LoginActivity", "JSON Result parse error");
                    e.printStackTrace();
                }

            }
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static void copyImgs(File src, File dst) throws IOException {
        try (InputStream in = new FileInputStream(src)) {
            try (OutputStream out = new FileOutputStream(dst)) {
                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            }
        }
    }

}