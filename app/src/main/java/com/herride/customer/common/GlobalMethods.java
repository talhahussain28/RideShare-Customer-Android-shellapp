package com.herride.customer.common;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.herride.customer.R;
import com.herride.customer.utils.RepoModel;


public class GlobalMethods {

    public static final String yyyy_MM_dd = "yyyy-MM-dd";
    public static final String dd_MMM_YYYY = "dd MMM yyyy";
    public static final String dd_MM_YYYY = "dd/MM/yyyy";
    public static final String yyyy_MM_dd_T_HH_mm_ss_sss = "MM-dd-yyyy hh:mm:a";
    public static final String EEEE_MMM_dd_HH_mm_a = "EEEE, MMM.dd hh:mm a";
    public static final String yyyy_mm_dd_HH_mm_ss_SSSZ = "yyyy MM dd HH:mm:ss";


    public static final String EEE_MMM_dd_yyyy = "EEE MMM dd, yyyy";
    public static final String HH_mm = "HH:mm";

    public static final String MM_dd_yyyy_T_HH_mm_ss_a = "MM-dd-yyyy hh:mm:a";
    public static final String dd_mm_yyyy_T_HH_mm_ss_a = "dd-MM-yyyy hh:mm:a";
    public static final String yyyy_MM_dd_T_HH_mm_ss = "yyyy-MM-dd hh:mm:ss";

    public static final String MMM_dd_yyyy = "MMM dd, yyyy";
    public static final String yyyy_MM_dd_T_HH_mm_ss_SSS_Z = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final String HH_mm_a = "hh:mm a";
    public static final String yyyy_mm_dd_T_HH_mm_a = "yyyy-MM-dd hh:mm a";

    public static final String HH_mm_ss_a = "hh:mm:ss a";
    public static final String dd_EEE_yyyy = "dd EEE, yyyy";
    public static final String mm_dd_yyyy = "MM/dd/yyyy";


    public DialogListener listener;
    private static Dialog dialog;

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }

    private static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    static RepoModel repoModel;
    public static void Dialog(Context context, String msg) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

        repoModel = new RepoModel(context);
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_message);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        TextView txt_dialog_msg = dialog.findViewById(R.id.txt_dialog_msg);
        TextView txt_dialog_ok = dialog.findViewById(R.id.txt_dialog_ok);

        txt_dialog_msg.setText(msg);
        //txt_dialog_ok.setText(repoModel.getLabelPref().getValue(PrefKeys.BTN_DIALOG_OK));

//        txt_dialog_msg.setTypeface(SetFontTypeFace.setSFProDisplayRegular(context));
//        txt_dialog_ok.setTypeface(SetFontTypeFace.setSFProDisplaySemibold(context));

        txt_dialog_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        wlp.windowAnimations = R.style.DialogAnimation;
        wlp.gravity = Gravity.CENTER;
        window.setAttributes(wlp);

        if (!((AppCompatActivity) context).isFinishing())
            dialog.show();

    }

    public void DialogOkCancel(Context context, String msg) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_message);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        TextView txt_dialog_msg = dialog.findViewById(R.id.txt_dialog_msg);
        TextView txt_dialog_ok = dialog.findViewById(R.id.txt_dialog_ok);
        TextView txt_dialog_cancel = dialog.findViewById(R.id.txt_dialog_cancel);
        txt_dialog_cancel.setVisibility(View.VISIBLE);

        txt_dialog_msg.setText(msg);

//        txt_dialog_msg.setTypeface(SetFontTypeFace.setSFProDisplayRegular(context));
//        txt_dialog_ok.setTypeface(SetFontTypeFace.setSFProDisplaySemibold(context));
//        txt_dialog_cancel.setTypeface(SetFontTypeFace.setSFProDisplaySemibold(context));

        txt_dialog_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();

                if (listener != null) {
                    listener.setOnOkClick();
                }

            }
        });

        txt_dialog_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        wlp.windowAnimations = R.style.DialogAnimation;
        wlp.gravity = Gravity.CENTER;
        window.setAttributes(wlp);
        if (!((AppCompatActivity) context).isFinishing())
            dialog.show();

    }

    public void DialogYesNo(Context context, String msg, String yesLabel, String noLabel) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_message);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        TextView txt_dialog_msg = dialog.findViewById(R.id.txt_dialog_msg);
        TextView txt_dialog_ok = dialog.findViewById(R.id.txt_dialog_ok);
        txt_dialog_ok.setText(yesLabel);
        TextView txt_dialog_cancel = dialog.findViewById(R.id.txt_dialog_cancel);
        txt_dialog_cancel.setText(noLabel);
        txt_dialog_cancel.setVisibility(View.VISIBLE);

        if (noLabel.equals("hide")) {
            txt_dialog_cancel.setVisibility(View.GONE);
        } else {
            txt_dialog_cancel.setVisibility(View.VISIBLE);
        }


        txt_dialog_msg.setText(msg);

//        txt_dialog_msg.setTypeface(SetFontTypeFace.setSFProDisplayRegular(context));
//        txt_dialog_ok.setTypeface(SetFontTypeFace.setSFProDisplaySemibold(context));
//        txt_dialog_cancel.setTypeface(SetFontTypeFace.setSFProDisplaySemibold(context));

        txt_dialog_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();

                if (listener != null) {
                    listener.setOnOkClick();
                }

            }
        });

        txt_dialog_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        wlp.windowAnimations = R.style.DialogAnimation;
        wlp.gravity = Gravity.CENTER;
        window.setAttributes(wlp);
        if (!((AppCompatActivity) context).isFinishing())
            dialog.show();

    }

    public void setDialogListener(DialogListener listener1) {
        listener = listener1;
    }

    public static boolean validiate(String pass) {
        if (pass.length() < 8) {
            System.out.println("pass too short or too long");
            return false;
        }
        if (!pass.matches(".*\\d.*")) {
            System.out.println("no digits found");
            return false;
        }

        if (!pass.matches(".*[a-z].*")) {
            System.out.println("no lowercase letters found");
            return false;
        }
        if (!pass.matches(".*[A-Z].*")) {
            System.out.println("no upper letters found");
            return false;
        }
        if (!pass.matches(".*[!@#$%^&*+=?-].*")) {
            System.out.println("no special chars found");
            return false;
        }

        return true;
    }

    // interface
    public interface DialogListener {
        void setOnOkClick();
    }

    public void setDialog(Context context, String msg) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

        dialog = new Dialog(context);
        dialog.setCancelable(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_message);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        TextView txt_dialog_msg = dialog.findViewById(R.id.txt_dialog_msg);
        TextView txt_dialog_ok = dialog.findViewById(R.id.txt_dialog_ok);

        txt_dialog_msg.setText(msg);

//        txt_dialog_msg.setTypeface(SetFontTypeFace.setSFProDisplayMedium(context));
//        txt_dialog_ok.setTypeface(SetFontTypeFace.setSFProDisplaySemibold(context));

        txt_dialog_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (listener != null) {
                    listener.setOnOkClick();
                }
            }
        });

        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        wlp.windowAnimations = R.style.DialogAnimation;
        wlp.gravity = Gravity.CENTER;
        window.setAttributes(wlp);

        if (!((AppCompatActivity) context).isFinishing())
            dialog.show();

    }


    public void loadImages(
            String url,
            ImageView imageView,
            int placeHolder
    ) {
        if (!TextUtils.isEmpty(url)) {
            Log.e("loadImages", "1 loadImages url=" + url);
            Glide
                    .with(imageView.getContext())
                    .load(url)
                    .placeholder(placeHolder)
                    .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    //.dontTransform()
                    //.centerCrop()
                    //.override(512, 512)
//                    .diskCacheStrategy(DiskCacheStrategy.NONE)
//                    .skipMemoryCache(true)
                    .into(imageView);

//.centerCrop()

//            Picasso.get()
//                .load(placeHolder)
//                .placeholder(placeHolder)
//                .error(placeHolder)
//                .into(imageView)
        } else {
//            Picasso.get()
//                .load(url)
//                .placeholder(placeHolder)
//                .error(placeHolder)
//                .into(imageView)
            //-------------------------------------------

//            var options = BitmapFactory.Options();
//            options.inSampleSize = 8;
//
//            var bm = BitmapFactory.decodeFile(url,options);
//            imageView.setImageBitmap(bm);
            Log.e("loadImages", "2 loadImages url=" + url);

            Glide
                    .with(imageView.getContext())
                    .load(placeHolder)
                    .placeholder(placeHolder)
                    .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    //.dontTransform()
                    // .centerCrop()
                    //.override(512, 512)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(imageView);
            //.centerCrop()
            //-------------------------------------------
//            GlideApp.with(context)
////            Glide.with(context)
//                .load(url)
//                .placeholder(placeHolder) // .override(512, 512)
//                .error(placeHolder)
//                .listener(object : RequestListener<Drawable?> {
//
//                    override fun onLoadFailed(
//                        e: GlideException?,
//                        model: Any?,
//                        target: com.bumptech.glide.request.target.Target<Drawable?>?,
//                        isFirstResource: Boolean
//                    ): Boolean {
//                        Log.e(
//                            "ErrrorGlide",
//                            "ErrrorGlide==" + e!!.causes.toString()
//                        )
//                        return false // important to return false so the error placeholder can be placed
//                    }
//
//                    override fun onResourceReady(
//                        resource: Drawable?,
//                        model: Any?,
//                        target: Target<Drawable?>?,
//                        dataSource: DataSource?,
//                        isFirstResource: Boolean
//                    ): Boolean {
//                        return false //To change body of created functions use File | Settings | File Templates.
//                    }
//                })
//                .into(imageView)

        }


    }

    public void loadImagesWithProgressbar(
            String url,
            ImageView imageView,
            int placeHolder,
            ProgressBar progressBar,
            int height,
            int width
    ) {

        Glide.with(imageView.getContext())
                .load(!TextUtils.isEmpty(url) ? url : placeHolder)
                .placeholder(placeHolder)
                .override(height, width)
//                    .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
//                    .diskCacheStrategy(DiskCacheStrategy.NONE)
//                    .skipMemoryCache(true)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        if (progressBar != null) {
                            progressBar.setVisibility(View.GONE);
                        }
                        //progressBar.setVisibility(progressBar != null ? View.GONE : View.VISIBLE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        if (progressBar != null) {
                            progressBar.setVisibility(View.GONE);
                        }
                        return false;
                    }
                }).into(imageView);


//        /////////////////////////////////////////////////////////////
//        if (!TextUtils.isEmpty(url)) {
//            Log.e("loadImages","1 loadImages url="+url);
//            Glide.with(imageView.getContext())
//                    .load(url)
//                    .placeholder(placeHolder)
//                    .override(height, width)
////                    .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
////                    .diskCacheStrategy(DiskCacheStrategy.NONE)
////                    .skipMemoryCache(true)
//                    .listener(new RequestListener<Drawable>() {
//                        @Override
//                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                            if (progressBar!=null)
//                            {
//                                progressBar.setVisibility(View.GONE);
//                            }
//                            return false;
//                        }
//
//                        @Override
//                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                            if (progressBar!=null)
//                            {
//                                progressBar.setVisibility(View.GONE);
//                            }
//                            return false;
//                        }
//                    }).into(imageView);
//
//
//        } else {
//
//            Log.e("loadImages","2 loadImages url="+url);
//
//
//            Glide.with(imageView.getContext())
//                    .load(placeHolder)
//                    .placeholder(placeHolder)
//                    .override(height, width)
////                    .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
////                    .diskCacheStrategy(DiskCacheStrategy.NONE)
////                    .skipMemoryCache(true)
//                    .listener(new RequestListener<Drawable>() {
//                        @Override
//                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                            if (progressBar!=null)
//                            {
//                                progressBar.setVisibility(View.GONE);
//                            }
//                            return false;
//                        }
//
//                        @Override
//                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                            if (progressBar!=null)
//                            {
//                                progressBar.setVisibility(View.GONE);
//                            }
//                            return false;
//                        }
//                    }).into(imageView);
//
//
//
//        }
//        /////////////////////////////////////////////////////////////////////


    }


    public void loadImagesWithNoProgressbar(
            String url,
            ImageView imageView,
            int placeHolder,
            int height,
            int width
    ) {

        Glide.with(imageView.getContext())
                .load(!TextUtils.isEmpty(url) ? url : placeHolder)
                .placeholder(placeHolder)
                .override(height, width)
//                    .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
//                    .diskCacheStrategy(DiskCacheStrategy.NONE)
//                    .skipMemoryCache(true)
               /* .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        if (progressBar != null) {
                            progressBar.setVisibility(View.GONE);
                        }
                        //progressBar.setVisibility(progressBar != null ? View.GONE : View.VISIBLE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        if (progressBar != null) {
                            progressBar.setVisibility(View.GONE);
                        }
                        return false;
                    }
                })*/
                .into(imageView);



    }
}
