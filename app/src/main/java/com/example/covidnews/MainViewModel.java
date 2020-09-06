package com.example.covidnews;
import android.view.View;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {
    private MutableLiveData<View> mDashboardView;
    private MutableLiveData<View> mHomeView;
    private MutableLiveData<View> mNotificationsView;
    public MainViewModel() {
        mDashboardView = new MutableLiveData<>();
        mHomeView = new MutableLiveData<>();
        mNotificationsView = new MutableLiveData<>();
    }
    public void setDashboardView(View v) {
        mDashboardView.setValue(v);
    }
    public void setHomeView(View v) {
        mHomeView.setValue(v);
    }
    public void setNotificationsView(View v) {
        mNotificationsView.setValue(v);
    }
    public LiveData<View> getDashboardView() {
        return mDashboardView;
    }
    public LiveData<View> getHomeView() {
        return mHomeView;
    }
    public LiveData<View> getNotificationsView() {
        return mNotificationsView;
    }
}
