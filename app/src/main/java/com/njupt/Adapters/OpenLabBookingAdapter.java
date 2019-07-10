package com.njupt.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.njupt.POJO.OpenLabInfo;
import com.njupt.R;
import com.njupt.Utils.TimeUtil;

import java.util.List;

public class OpenLabBookingAdapter extends BaseAdapter {

    private Context context;
    private List<OpenLabInfo> lists;

    public OpenLabBookingAdapter(Context context, List<OpenLabInfo> lists) {
        super();
        this.context = context;
        this.lists = lists;
    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int position) {
        return lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint({"ViewHolder", "SetTextI18n"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        OpenLabInfo openLabInfo = lists.get(position);
        convertView = View.inflate(context, R.layout.layout_item_booking, null);
        convertView.getBackground().setAlpha(100);
        TimeUtil timeUtil = new TimeUtil();
        TextView date = (TextView) convertView.findViewById(R.id.booking_item_text_date);
        date.setText("日期：" + timeUtil.FormatData(openLabInfo.getOpenDate()));
        TextView time = (TextView) convertView.findViewById(R.id.booking_item_text_time);
        time.setText("时间：" + openLabInfo.getOpenTime());
        TextView week = (TextView) convertView.findViewById(R.id.booking_item_text_week);
        week.setText("周次：第" + openLabInfo.getOpenWeek() + "周");
        TextView teacher = (TextView) convertView.findViewById(R.id.booking_item_text_teacher);
        teacher.setText("老师：" + openLabInfo.getOpenTeacherName());
        TextView hasBook = (TextView) convertView.findViewById(R.id.booking_item_text_hasbook);
        hasBook.setText("已预约：" + openLabInfo.getBookingNum() + "人");
        TextView canBook = (TextView) convertView.findViewById(R.id.booking_item_text_canbook);
        canBook.setText("可预约：" + openLabInfo.getNotBookingNum() + "人");
        return convertView;
    }
}
