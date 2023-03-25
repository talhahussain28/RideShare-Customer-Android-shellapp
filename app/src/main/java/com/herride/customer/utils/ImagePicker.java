package com.herride.customer.utils;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.herride.customer.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * Created by  on 3/5/19.
 */
public class ImagePicker extends DialogFragment implements View.OnClickListener {

    private static final int CAMERA_REQUEST = 1;
    private static final int RESULT_LOAD_IMAGE = 2;
    private static final String TAG = "ImagePicker";
    private static final int CHECK_CAMERA = 3;
    private static final int CHECK_STORAGE = 4;
    private LinearLayout llCamera;
    private TextView txtCamera;
    private LinearLayout llStorage;
    private TextView txtStorage;
    private TextView txtCancel;
    onUpdate listener;
    Uri imageUri;
    String imgPath = null;
    RepoModel repo;

    public ImagePicker(RepoModel repo) {
        this.repo = repo;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NORMAL, R.style.MaterialDialogSheet);
    }

    //
//
    @Override
    public void setupDialog(Dialog dialog, int style) {
        //super.setupDialog(dialog, style);

        dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_choose_source, container, false);

        return view;

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        llCamera = (LinearLayout) view.findViewById(R.id.llCamera);
        txtCamera = (TextView) view.findViewById(R.id.txtCamera);
        llStorage = (LinearLayout) view.findViewById(R.id.llGallery);
        txtStorage = (TextView) view.findViewById(R.id.txtStorage);
        txtCancel = (TextView) view.findViewById(R.id.txtCancel);
        txtCamera.setText("Camera");
        txtCancel.setText("Cancel");
        txtStorage.setText("Gallery");
        llCamera.setOnClickListener(this);
        llStorage.setOnClickListener(this);
        txtCancel.setOnClickListener(this);
        setLabel();

    }

    private void setLabel() {

        //  Log.e(TAG, "On Create: " + new Gson().toJson(lngList));
//        txtCamera.setText(repo.getLabelPref().getValue(PrefKeys.LBL_CAMERA));
//        txtStorage.setText(repo.getLabelPref().getValue(PrefKeys.LBL_GALLARY));
//        txtCancel.setText(repo.getLabelPref().getValue(PrefKeys.LBL_CANCEL));

    }


    private boolean chkAllPermissionForStorage() {

        int result;

        String[] permissions = new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};

        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(getContext(), p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }


        if (!listPermissionsNeeded.isEmpty()) {
            requestPermissions(listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), CHECK_STORAGE);

            return false;
        } else {
            return true;
        }


    }

    private boolean chkAllPermissionForCamera() {
        int result;
        String[] permissions = new String[]{
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};

        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(getContext(), p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }


        if (!listPermissionsNeeded.isEmpty()) {
            requestPermissions(listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), CHECK_CAMERA);

            return false;
        } else {
            return true;
        }


    }


    private boolean chkStoragePermission() {


        int result;

        String[] permissions = new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};

        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(getContext(), p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }


        if (!listPermissionsNeeded.isEmpty()) {
            requestPermissions(listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), CHECK_STORAGE);

            return false;
        } else {
            return true;
        }


    }

    private boolean chkCameraPermission() {


        int result;

        String[] permissions = new String[]{Manifest.permission.CAMERA};

        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(getContext(), p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            requestPermissions(listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), CHECK_CAMERA);
            return false;
        } else {
            return true;
        }

    }

   /* @OnClick({R.id.llCamera, R.id.llGallery, R.id.tvCancel})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.llCamera: {

                if (chkAllPermission())
                    callCamera();
                break;
            }

            case R.id.llGallery: {
                if (chkAllPermission())
                    callGallery();
                break;
            }
            case R.id.tvCancel: {
                dismiss();
                break;
            }

        }

    }*/


    public void setListener(onUpdate listener) {
        this.listener = listener;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    private void callGallery() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(intent, "Select Source"), RESULT_LOAD_IMAGE);
    }

//    private void callCamera() {
//
//        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        File file = getOutputMediaFile(1);
//
//        try {
//            Log.e(TAG, "call Camera" + getActivity().getPackageName());
//            imageUri = FileProvider.getUriForFile(getActivity(), getActivity().getPackageName() + ".Utils.Provide", createTemporaryFile("picture", ".jpg"));
//            Log.e(TAG, "imageUri ==>>>" + il̥mageUri);
//        } catch (Exception e) {
//
//            e.getLocalizedMessage();
//            e.printStackTrace();
//
//            Log.e("TAG", "Can't create file to take picture!  " + e);
//
//            Toast.makeText(getActivity(), "Please check storage! Image shot is impossible!", Toast.LENGTH_SHORT);
//
//        }
//
//        Log.e(TAG, "Start Camera");
//        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//        startActivityForResult(cameraIntent, CAMERA_REQUEST);
//
//    }


    private void callCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {

         /*   File photoFile = null;
            try {
                photoFile = createImageFile();
                Log.e(TAG, "Create Image File");

            } catch (IOException ex) {

                Log.e(TAG, "Error File" + ex);

                // Error occurred while creating the File
            }*/
            // Continue only if the File was successfully created
            /*if (photoFile != null) {
                imageUri = FileProvider.getUriForFile(getContext(),
                        "com.togoexpress.customer.provider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            }*/
            startActivityForResult(takePictureIntent, CAMERA_REQUEST);
        }
    }


    private File getOutputMediaFile(int type) {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyApplication");

        /**Create the storage directory if it does not exist*/
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        /**Create a media file name*/
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == 1) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_" + timeStamp + ".png");
        } else {
            return null;
        }

        return mediaFile;
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        imgPath = image.getAbsolutePath();
        return image;
    }

    private File createTemporaryFile(String part, String ext) throws Exception {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        Log.e(TAG, "onActivityResult: ");

        if (resultCode == Activity.RESULT_OK) {

            Uri uri;
            if (requestCode == RESULT_LOAD_IMAGE) {

                if (data != null) {

                    uri = data.getData();
                    if (uri != null) {
                        File myFile = new File(uri.getPath());
                        Log.e("uriname=>", "" + uri);
                        imgPath = getRealPathFromUri(getActivity(), uri);
                        Log.e(TAG, "Gallery Path: " + imgPath);

                        if (listener != null) {
                            listener.set(imgPath);
                        }

                        dismiss();

                    } else {
                        Log.e(TAG, "uri null: ");
                    }

                }
            } else if (requestCode == CAMERA_REQUEST) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                Uri urione = getImageUri(getActivity(), photo);
                String filepath = getRealPathFromUri(getActivity(), urione);
                listener.set(filepath);
                dismiss();
            }


            //getActivity().getContentResolver().notifyChange(imageUri, null);
                /*Log.e("CAMERA_REQUEST", "load 1");
                ContentResolver cr = getActivity().getContentResolver();
                Bitmap bitmap;
                Log.e("CAMERA_REQUEST", "load 2");

                try {
                    bitmap = MediaStore.Images.Media.getBitmap(cr, imageUri);

                    uri = getImageUri(getActivity(), bitmap);
                    if (uri != null) {
                        imgPath = getRealPathFromUri(getActivity(), uri);
                        Log.e(TAG, "Camera Path: " + imgPath);

                        if (listener != null) {
                            listener.set(imgPath);
                        }

                        dismiss();

                    } else {
                        Log.e(TAG, "Uri Null");

                    }

                } catch (Exception e) {

                    e.printStackTrace();
                    e.getLocalizedMessage();

                    Log.e("CAMERA_REQUEST", "Failed to load", e);
                }

            }*/


        } else {
            dismiss();
        }

    }


    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
//        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "IMG_" + Calendar.getInstance().getTime(), null);
        return Uri.parse(path);
    }

    public static String getRealPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {

            case CHECK_CAMERA:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    //GlobalMethods.Dialog(getContext(), "" + Utility.getLanguageString(getContext(), ConstantLanguage.MSG_CAMERA_PERMISSION_REQUIRE), Utility.getLanguageString(getContext(), ConstantLanguage.LBL_OK));
                    //Toast.makeText(getContext(), "Camera permission require", Toast.LENGTH_SHORT).show();
                } else {
                    callCamera();
                }

                break;


            case CHECK_STORAGE:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    //GlobalMethods.Dialog(getContext(), "" + Utility.getLanguageString(getContext(), ConstantLanguage.MSG_STRORAGE_PERMISSION_REQUIRE), Utility.getLanguageString(getContext(), ConstantLanguage.LBL_OK));
                    //Toast.makeText(getContext(), "Storgae permission require", Toast.LENGTH_SHORT).show();
                } else {
                    callGallery();
                }
                break;


        }

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llCamera:
                if (chkAllPermissionForCamera())
                    callCamera();
                break;


            case R.id.llGallery:
                if (chkAllPermissionForStorage())
                    callGallery();
                break;

            case R.id.txtCancel:
                dismiss();
                break;

        }
    }

    public interface onUpdate {
        public void set(String imagePath);
    }


}