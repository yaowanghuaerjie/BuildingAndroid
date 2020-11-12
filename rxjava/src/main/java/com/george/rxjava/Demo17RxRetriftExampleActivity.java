package com.george.rxjava;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.george.rxjava.demo16.GetReqest_Interface;
import com.george.rxjava.demo16.Translation;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Demo17RxRetriftExampleActivity extends AppCompatActivity {
    private static final String TAG = "RxJavaRetrift";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo16_rx_retrift_example);
        reqestData();
    }

    private void reqestData() {
        /**
         * 步骤1：采用interval()延迟发送
         *
         */
        Observable.intervalRange(0,5,2, 1, TimeUnit.SECONDS)
                /**
                 * 参数说明：
                 * 参数1： 第一次延迟时间
                 * 参数2： 间隔时间
                 * 参数3： 时间单位
                 * 延迟2秒后，发送改事件，每隔1秒产生一个数字（从0开始递增1，无限个）
                 */


                /**
                 * 步骤2：每次发送数字前发送1次网络请求（doOnNext在Next事件前调用）
                 */
                .doOnNext(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        Log.d(TAG, "第" + aLong + "次轮询");
                        /**
                         * 步骤3： 通过Retrofit发送网络请求
                         */
                        // a. 创建Retrofit对象
                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl("http://fy.iciba.com/")//设置请求Url
                                .addConverterFactory(GsonConverterFactory.create())//设置使用Gson解析
                                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//支持Rxjava
                                .build();
                        //b.创建 网络请求的示例
                        GetReqest_Interface request = retrofit.create(GetReqest_Interface.class);
                        //c. 采用Observable<...>形式对网络请求进行封装
                        Observable<Translation> observable = request.getCall();
                        //d. 通过线程切换发送网络请求
                        observable.subscribeOn(Schedulers.io())// 切换到IO线程进行网络请求
                                .observeOn(AndroidSchedulers.mainThread())//切换到主线程处理请求网络
                                .subscribe(new Observer<Translation>() {
                                    @Override
                                    public void onSubscribe(Disposable d) {

                                    }

                                    @Override
                                    public void onNext(Translation translation) {
                                        translation.show();
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        Log.d(TAG, "请求失败");
                                    }

                                    @Override
                                    public void onComplete() {
                                        Log.d(TAG, "请求完成");
                                    }
                                });
                    }
                }).subscribe(new Observer<Long>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Long aLong) {
                Log.d(TAG, "long：" + aLong);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "对Error事件作出响应");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "对Complete事件作出响应");
            }
        });
    }
}
