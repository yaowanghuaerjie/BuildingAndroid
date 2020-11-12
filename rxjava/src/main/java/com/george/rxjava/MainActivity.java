package com.george.rxjava;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String RXJAVA_TAG = "RXJAVA";
    @BindView(R.id.btn1)
    AppCompatButton btn1;
    @BindView(R.id.btn2)
    AppCompatButton btn2;
    @BindView(R.id.btn3)
    AppCompatButton btn3;
    @BindView(R.id.btn4)
    AppCompatButton btn4;
    @BindView(R.id.btn5)
    AppCompatButton btn5;
    @BindView(R.id.btn6_create)
    AppCompatButton btn6Create;
    @BindView(R.id.btn7_just)
    AppCompatButton btn7Just;
    @BindView(R.id.btn8_fromArray)
    AppCompatButton btn8FromArray;
    @BindView(R.id.btn_fromIterable)
    AppCompatButton btnFromIterable;
    @BindView(R.id.btn10_other)
    AppCompatButton btn10Other;
    @BindView(R.id.btn11_defer)
    AppCompatButton btn11Defer;
    @BindView(R.id.btn12_timer)
    AppCompatButton btn12Timer;
    @BindView(R.id.btn13_interval)
    AppCompatButton btn13Interval;
    @BindView(R.id.btn14_intervalRange)
    AppCompatButton btn14IntervalRange;
    @BindView(R.id.btn15_range)
    AppCompatButton btn15Range;
    @BindView(R.id.btn16_rangeLong)
    AppCompatButton btn16RangeLong;
    @BindView(R.id.btn17_request_example)
    AppCompatButton btn17RequestExample;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initOnclickListener(btn1, btn2, btn3, btn4, btn5, btn6Create, btn7Just, btn8FromArray, btn10Other,
                btn11Defer, btn12Timer, btn13Interval, btn14IntervalRange, btn15Range, btn16RangeLong,
                btn17RequestExample);
    }

    private void initOnclickListener(View... views) {
        for (View view :
                views) {
            view.setOnClickListener(this);
        }
    }

    private void demo1() {
        //创建观察者Observable
        Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {
            //create 是Rxjava最基本的创建时间序列的方法
            //此处传入一个OnSubscribe对象参数，当observable被订阅后，OnSubscribe的call()方法会自动被调用，即事件序列就会依照设定一次被触发
            //即观察者会依次调用对应时间的复写方法从而响应事件
            //从而实现被观察者调用了观察者的回调方法 & 由被观察者向观察者的事件传递，即观察者模式
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                //通过ObservableEmitter类对象产生事件并通知观察者
                //ObservableEmitter : 1.定义：事件发射器   2.作用：定义需要发送的事件 & 向观察者发送事件
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
                emitter.onNext(4);
                emitter.onNext(5);
                emitter.onComplete();
            }
        });
        /**
         * Subscriber 抽象类与Observer接口的区别：
         *      相同点：二者基本使用方式完全一致（实质上，在rxjava的subscribe过程中，Observer总是会先被转换为Subscriber再使用）
         *      不同点： Subscriber抽象类对Observer接口进行了扩展，新增了两个方法：
         *          1. onStart():在还未响应事情前调用，用于进一步初始化工作
         *          2. unSubscribe():用于取消订阅，在该方法被调用后，观察者不在接收 & 响应事件
         *          在调用该方法前，先使用isUnsubscribed()判断状态
         */
        //创建观察者Observer对象
        Observer<Integer> observer = new Observer<Integer>() {
            //创建对象时通过复写对应的事件方法，从而响应对应的事件
            private Disposable disposable;

            @Override
            public void onSubscribe(Disposable d) {
                Log.e(RXJAVA_TAG, "开始采用subscribe链接" + d.isDisposed());
                disposable = d;
            }

            //当被观察者生产next事件 & 观察者接收到时，会调用改复写方法  进行响应
            @Override
            public void onNext(Integer integer) {
                Log.e(RXJAVA_TAG, "对Next事件作出响应 value:" + integer);
                if (integer == 2) {
                    //设置切断观察者和被观察者的连接
                    disposable.dispose();
                }
            }

            //当被观察者生产Error事件 & 观察者接收到时，会调用改复写方法  进行响应
            @Override
            public void onError(Throwable e) {
                Log.e(RXJAVA_TAG, "对Error事件作出响应 Throwable:" + e.getMessage());
            }

            //当被观察者生产Complete事件 & 观察者接收到时，会调用改复写方法  进行响应
            @Override
            public void onComplete() {
                Log.e(RXJAVA_TAG, "对Complete事件作出响应");
            }
        };
        observable.subscribe(observer);


    }

    private void demo2() {
        //Rxjava提供其他方法用于创建Observable
        //方法一：just(T...) 直接将传入的参数衣服发送出来
        Observable<String> observable1 = Observable.just("A", "B", "C");
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.e(RXJAVA_TAG, "开始采用subscribe链接" + d.isDisposed());
            }

            @Override
            public void onNext(String s) {
                Log.e(RXJAVA_TAG, "对Next事件作出响应 value:" + s);
            }

            @Override
            public void onError(Throwable e) {
                Log.e(RXJAVA_TAG, "对Error事件作出响应 Throwable:" + e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.e(RXJAVA_TAG, "对Complete事件作出响应");
            }
        };
        observable1.subscribe(observer);
    }

    private void demo3() {
        //方法二：from()
        String[] words = {"E", "F", "G"};
        Observable<String> observable2 = Observable.fromArray(words);
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.e(RXJAVA_TAG, "开始采用subscribe链接" + d.isDisposed());
            }

            @Override
            public void onNext(String s) {
                Log.e(RXJAVA_TAG, "对Next事件作出响应 value:" + s);
            }

            @Override
            public void onError(Throwable e) {
                Log.e(RXJAVA_TAG, "对Error事件作出响应 Throwable:" + e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.e(RXJAVA_TAG, "对Complete事件作出响应");
            }
        };
        observable2.subscribe(observer);
    }

    private void demo4() {
        //Rxjava 链式调用
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
                emitter.onNext(4);
                emitter.onNext(5);
                emitter.onComplete();
            }
        }).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.e(RXJAVA_TAG, "开始采用subscribe链接" + d.isDisposed());
            }

            @Override
            public void onNext(Integer s) {
                Log.e(RXJAVA_TAG, "对Next事件作出响应 value:" + s);
            }

            @Override
            public void onError(Throwable e) {
                Log.e(RXJAVA_TAG, "对Error事件作出响应 Throwable:" + e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.e(RXJAVA_TAG, "对Complete事件作出响应");
            }
        });
    }

    private void demo5() {
        Observable.just("hello").subscribe(new Consumer<String>() {
            @SuppressLint("CheckResult")
            @Override
            public void accept(String s) throws Exception {
                System.out.println("value:" + s);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn1:
                demo1();
                break;
            case R.id.btn2:
                demo2();
                break;
            case R.id.btn3:
                demo3();
                break;
            case R.id.btn4:
                demo4();
                break;
            case R.id.btn5:
                demo5();
                break;
            case R.id.btn17_request_example:
                startActivity(new Intent(MainActivity.this, Demo17RxRetriftExampleActivity.class));
                break;
        }
    }
}
