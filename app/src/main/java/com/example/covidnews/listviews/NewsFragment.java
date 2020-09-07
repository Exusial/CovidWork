package com.example.covidnews.listviews;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.example.covidnews.Fresher.LoadMore;
import com.example.covidnews.Fresher.LoadNew;
import com.example.covidnews.MainActivity;
import com.example.covidnews.NewsDataBase.NewsInit;
import com.example.covidnews.R;
import com.example.covidnews.newsviews.NewsDetailActivity;
import com.example.covidnews.newsviews.NewsItemActivity;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private final static String ARG_PARAM1 = "param1";
    private String kind;
    private RefreshLayout refreshLayout;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private NewsAdapter adapter;

    public NewsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment NewsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewsFragment newInstance(String kind) {
        NewsFragment fragment = new NewsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, kind);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            kind = getArguments().getString(ARG_PARAM1);
        }
        adapter = new NewsAdapter(R.layout.news_item_layout,null);
        adapter.addChildClickViewIds(R.id.main_lay);
        adapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                NewsItem item = (NewsItem) adapter.getData().get(position);
                String to_send = item.getId();
                TextView textView = view.findViewById(R.id.ntitle1);
                textView.setTextColor(Color.parseColor("#708090"));
                Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("id",to_send);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        NewsInit init = new NewsInit(adapter,kind);
        init.InitPage();
        RecyclerView myview = view.findViewById(R.id.newsview);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        myview.setLayoutManager(manager);
        myview.setAdapter(adapter);
        refreshLayout = view.findViewById(R.id.refreshLayout);
        refreshLayout.setRefreshHeader(new ClassicsHeader(getContext()));
        refreshLayout.setRefreshFooter(new ClassicsFooter(getContext()));
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                Thread ts = new Thread(new LoadNew(adapter,NewsFragment.this,kind));
                refreshlayout.setDisableContentWhenRefresh(true);
                ts.start();
            }
        });

        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                Thread ts = new Thread(new LoadMore(adapter,NewsFragment.this,kind));
                refreshlayout.setDisableContentWhenLoading(true);
                ts.start();
            }
        });
        return view;
    }

    public void freshNews(NewsAdapter adapter, NewsItem ni, int position){
        if(position == -1) {
            adapter.addData(ni);
        }
        else{
            adapter.addData(position, ni);
        }
        adapter.notifyDataSetChanged();
    }

    public void RefreshReCall(int newState){
        switch(newState){
            case 1:
                refreshLayout.finishRefresh(true);
                break;
            case 2:
                refreshLayout.finishRefresh(false);
                break;
        }
    }

    public void LoadReCall(int newState){
        switch (newState){
            case 1:
                refreshLayout.finishLoadMore(true);
                break;
            case 2:
                refreshLayout.finishLoadMore(false);
                break;
        }
    }

}