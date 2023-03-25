package com.herride.customer.utils;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.herride.customer.R;
import com.herride.customer.common.Constants;
import com.herride.customer.preference.PrefKeys;
import com.herride.customer.ui.base.BaseActivity;

public class MessageDialog extends DialogFragment {
    private static final int CAMERA_REQUEST = 1;
    private static final int RESULT_LOAD_IMAGE = 2;
    private static final String TAG = "ImagePicker";
    private static final int CHECK_CAMERA = 3;
    private static final int CHECK_STORAGE = 4;
    OnClick listener;
    OnClickLogout onClickLogout;

    public TextView tvMsg;
    public TextView tvMsgInfo;
    public ImageView imgClose;
    public Button btCancel;
    public Button btOk;
    public LinearLayout llMain;
    String msgType = "";
    RepoModel repoModel;


    String tvMsgText = "", tvMsgInfoText = "", cancelTxt = "", okTxt = "";
    static MessageDialog msgDialog;

    @SuppressLint("WrongConstant")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        repoModel= new RepoModel(getContext());
        setStyle(0, R.style.MaterialDialogSheet);
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setGravity(Gravity.TOP);
        dialog.getWindow().setWindowAnimations(R.style.DialogMessageAnimation);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_ok, container, false);

        btOk = view.findViewById(R.id.btOk);
        btCancel = view.findViewById(R.id.btCancel);
        tvMsg = view.findViewById(R.id.tvMsg);
        tvMsgInfo = view.findViewById(R.id.tvMsgInfo);
        imgClose = view.findViewById(R.id.imgClose);
        llMain = view.findViewById(R.id.llMain);
        btOk.setText("Ok");

        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.set(true);
                }

                if (onClickLogout != null) {
                    onClickLogout.logout(true);
                }

            }
        });

        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btCancel.performClick();
            }
        });

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.set(false);
                }
                if (onClickLogout != null) {
                    onClickLogout.cancelBtn(false);
                }

            }
        });
        return view;
    }

    public static MessageDialog getInstance() {
        if (msgDialog == null)
            msgDialog = new MessageDialog();
        return msgDialog;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            tvMsgText = getArguments().getString("tvMsgText", null);
            tvMsgInfoText = getArguments().getString("tvMsgInfoText", null);

            cancelTxt = getArguments().getString("cancelTxt", "");
            okTxt = getArguments().getString("okTxt", "");
            msgType = getArguments().getString("msgType", "");


            if (TextUtils.isEmpty(cancelTxt)) {
                btCancel.setVisibility(View.GONE);
            } else {
                btCancel.setVisibility(View.GONE);
            }

            if (msgType.equals(Constants.SUCCESS)) {
                llMain.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
//                tvMsg.setText(""+ repoModel.getPref().getLabel(PrefKeys.MSG_GM_SUCCESS));
                tvMsg.setText(""+ repoModel.getPref().getLabel(PrefKeys.MSG_GM_SUCCESS));
            } else {
                llMain.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                tvMsg.setText(""+ repoModel.getPref().getLabel(PrefKeys.MSG_GM_SUCCESS));
            }

            btCancel.setText(cancelTxt);
            btOk.setText(okTxt);

            tvMsgInfo.setText(tvMsgText);

        }

        setLabel();
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        //No call for super(). Bug on API Level > 11.
    }


    private void setLabel() {
       /* if (lngList.size() == 0) {
            return;
        }
*/
        //lblTakePicture.setText(lngList.get(Label.LBL_FROM_CAMERA));
        //lblFromGallery.setText(lngList.get(Label.LBL_FROM_GALLERY));
        //tvCancel.setText(lngList.get(Label.LBL_CANCEL));
    }


    public void setListener(OnClick listener) {
        this.listener = listener;
    }

    public interface OnClick {
        public void set(boolean ok);
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        ((BaseActivity) getContext()).setStatusBar(ContextCompat.getColor(getContext(), R.color.colorPrimary), false);
    }

    ///////////////////////////////////////////////////////////////

    public void setListenerLogout(OnClickLogout onClickLogout) {
        this.onClickLogout = onClickLogout;
    }

    public interface OnClickLogout {
        public void logout(boolean yes);

        public void cancelBtn(boolean cancel);
    }


}