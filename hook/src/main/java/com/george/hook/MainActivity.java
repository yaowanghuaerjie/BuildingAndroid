package com.george.hook;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private Button button;
    private Map<Integer, ViewState> resMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.btn);
        contextHookMethod(this);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "点击了我", Toast.LENGTH_SHORT).show();


            }
        });
        hookOnClickListener(button);
        getApplicationContext().startActivity(new Intent(MainActivity.this, SecondActivity.class));

    }


    @Override
    protected void onResume() {
        super.onResume();
        resMap.remove(R.id.btn);
    }

    private void contextHookMethod(Activity activity) {
        try {
            Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
            Method currentActivityThreadMethod = activityThreadClass.getDeclaredMethod("currentActivityThread");
            currentActivityThreadMethod.setAccessible(true);
            Object currentActivityThread = currentActivityThreadMethod.invoke(null);

            Field mInstrumentationField = activityThreadClass.getDeclaredField("mInstrumentation");
            mInstrumentationField.setAccessible(true);
            Instrumentation mInstrumentation = (Instrumentation) mInstrumentationField.get(currentActivityThread);
            Instrumentation georgeInstrumentation = new GeorgeInstrumentation(mInstrumentation);
            mInstrumentationField.set(currentActivityThread, georgeInstrumentation);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void activityStartHook(Activity activity) {
        // 拿到原始的 mInstrumentation字段
        Field mInstrumentationField = null;
        try {
            mInstrumentationField = Activity.class.getDeclaredField("mInstrumentation");
            mInstrumentationField.setAccessible(true);
            Instrumentation mInstrumentation = (Instrumentation) mInstrumentationField.get(activity);
            Instrumentation evilInstrumentation = new GeorgeInstrumentation(mInstrumentation);
            mInstrumentationField.set(activity, evilInstrumentation);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    private void hookOnClickListener(View view) {
        try {
            Method getListenerInfo = View.class.getDeclaredMethod("getListenerInfo");
            getListenerInfo.setAccessible(true);
            Object listener = getListenerInfo.invoke(view);

            Class<?> listenerInfoClz = Class.forName("android.view.View$ListenerInfo");
            Field mOnClicklistener = listenerInfoClz.getDeclaredField("mOnClickListener");
            mOnClicklistener.setAccessible(true);
            View.OnClickListener originOnClickListener = (View.OnClickListener) mOnClicklistener.get(listener);

            View.OnClickListener hookOnclickListener = new HookedOnClickListener(originOnClickListener);
            mOnClicklistener.set(listener, hookOnclickListener);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    private class HookedOnClickListener implements View.OnClickListener {
        private View.OnClickListener origin;

        public HookedOnClickListener(View.OnClickListener originOnClickListener) {
            this.origin = originOnClickListener;
        }

        @Override
        public void onClick(View v) {
            Log.d("hook", "hook onclick");
            if (null == origin) {
                return;
            }
            if (resMap.containsKey(v.getId())) {
                if (resMap.get(v.getId()).isReqest()) {
                    return;
                }
                long time = System.currentTimeMillis() - resMap.get(v.getId()).getOnclickTime();
                if (time < 1000) {
                    Log.d("hook", "您点击的过于频繁了，请稍后再点击");
                } else {
                    resMap.get(v.getId()).setOnclickTime(System.currentTimeMillis());
                    origin.onClick(v);
                }
            } else {
                resMap.put(v.getId(), new ViewState(System.currentTimeMillis(), true));
                origin.onClick(v);
            }
        }
    }
}
