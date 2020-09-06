package com.example.covidnews.ui.notifications;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.example.covidnews.R;
import com.example.covidnews.ShowDataActivity;
import com.example.covidnews.VirusSearchActivity;
import com.example.covidnews.globalviews.GlobalActivity;
import com.example.covidnews.listviews.NewsAdapter;
import com.example.covidnews.listviews.NewsItem;
import com.example.covidnews.newsviews.NewsDetailActivity;
import com.example.covidnews.provinceviews.ProvinceActivity;

public class NotificationsFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        Button btn1 = (Button)root.findViewById(R.id.rect_btn1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ProvinceActivity.class);
                startActivity(intent);
            }
        });
        Button btn2 = (Button)root.findViewById(R.id.rect_btn2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), GlobalActivity.class);
                startActivity(intent);
            }
        });
        Button btn3 = (Button)root.findViewById(R.id.rect_btn3);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), VirusSearchActivity.class);
                startActivity(intent);
            }
        });
        return root;
    }
}