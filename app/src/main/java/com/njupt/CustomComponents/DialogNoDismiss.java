package com.njupt.CustomComponents;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.njupt.R;


public class DialogNoDismiss extends Dialog {

    public DialogNoDismiss(Context context) {
        super(context);
    }

    public static class Builder {
        private Context context;
        private String title;  //标题
        DialogAutoDismiss dialog;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setTitle(String title) {
            if (title == null) {
                this.title = "提醒";
            }
            this.title = title;
            return this;
        }

        private TextView tv_title_custom_dialog;  //标题
        public DialogAutoDismiss create() {
            dialog = new DialogAutoDismiss(context);
            View view = LayoutInflater.from(context).inflate(R.layout.dialog_layout, null);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(view, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            tv_title_custom_dialog = (TextView) view.findViewById(R.id.tv_title_custom_dialog);
            tv_title_custom_dialog.setText(title);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
            return dialog;
        }

        public void cancel(){
            dialog.cancel();
        }
    }
}