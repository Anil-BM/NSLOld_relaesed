package com.nsl.beejtantra.commonutils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.nsl.beejtantra.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Home on 19-09-2016.
 */
public class CustomerAlertDialog3 extends Dialog {
    @BindView(R.id.cancel)
    Button cancel;
    @BindView(R.id.ok)
    Button ok;
    @BindView(R.id.title)
    TextView tvTitle;
   @BindView(R.id.tv_header)
   TextView tvHeader;
    String title;
    String header;
    String buttonTitle;
    DialogClickListener dialogClickListener;

    public CustomerAlertDialog3(Context context, String header, String title, String buttonTitle, DialogClickListener dialogClickListener) {
        super(context);
        this.dialogClickListener=dialogClickListener;
        this.title=title;
        this.header=header;
        this.buttonTitle=buttonTitle;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_alert_dialog3);
        ButterKnife.bind(this);
        tvTitle.setText(title);
        tvHeader.setText(header);
        ok.setText(buttonTitle);
        init();


    }
    @OnClick(R.id.cancel)
    void cancelClicked(){
        dialogClickListener.onCancelClick();
    }
    @OnClick(R.id.ok)
    void okClicked(){
        dialogClickListener.onOkClick();
    }

    public void init(){
        //getWindow().setBackgroundDrawableResource(R.drawable.search_way_dialog_background);
        getWindow().setBackgroundDrawable(new ColorDrawable(0));
        Window window = getWindow();
        WindowManager.LayoutParams param = window.getAttributes();
        // set the layout at right bottom
        param.gravity = Gravity.CENTER | Gravity.CENTER;
        setCancelable(false);
        setCanceledOnTouchOutside(false);

    }
}
