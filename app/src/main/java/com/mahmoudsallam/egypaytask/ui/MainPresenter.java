package com.mahmoudsallam.egypaytask.ui;

import com.mahmoudsallam.egypaytask.data.Api;
import com.mahmoudsallam.egypaytask.data.RetrofitInstance;

import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainPresenter implements MainMvpPresenter {
    MainMvpView mainView;
    Api api;

    public MainPresenter(MainMvpView mainView) {
        this.mainView = mainView;
        api = RetrofitInstance.getInstance().create(Api.class);
    }

    @Override
    public void submitDataToApi(Map<String, String> map) {
        api.submitData(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                               @Override
                               public void accept(String s) throws Exception {
                                   mainView.showDialog(s);
                               }
                           }
                        , new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                mainView.showDialog(throwable.getMessage());
                            }
                        }
                );
    }
}
