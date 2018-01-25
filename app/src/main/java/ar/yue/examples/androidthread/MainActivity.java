package ar.yue.examples.androidthread;

import android.app.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class MainActivity extends Activity {

    FutureTask<Number> futureTask;
    CountCallable countCallable;

    ExecutorService es;
    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        countCallable = new CountCallable();

        // done 定義 結束的動作
        futureTask = new FutureTask<Number>(countCallable){
            @Override
            protected void done() {
                try {
                    Number number = futureTask.get();
                    Log.i(TAG, "任务结束于" + new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()) + "  result=" + number.getNum());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (CancellationException e) {
                    Log.i(TAG, "任务被取消于" + new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
                }
            }
        };

        es = Executors.newFixedThreadPool(2);

        es.execute(futureTask);

        Log.d(TAG,"Task start" + new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));


    }


    public void cancel(View view){
        futureTask.cancel(true);
    }

    // 呼叫 的時候做的事情
    public static class CountCallable implements Callable<Number>{
        @Override
        public Number call() throws Exception {
                Number number = new  Number();
            Log.i(TAG,"运行在"+Thread.currentThread().getName());
            Thread.sleep(5000);
            number.setNum(10);
            return number;

        }
    }

    static class Number {
        private int num;

        private int getNum() {
            return num;
        }

        private void setNum(int num) {
            this.num = num;
        }
    }
}
