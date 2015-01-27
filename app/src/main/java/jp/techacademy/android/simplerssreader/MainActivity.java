package jp.techacademy.android.simplerssreader;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {
    private String mURL = "http://www.atmarkit.co.jp/rss/rss.xml";
    private String TAG = "RSS Reader";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // URL入力ダイアログ
        ((Button)findViewById(R.id.button)).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {

                        //URL入力用のビューを生成
                        final EditText editView = new EditText(MainActivity.this);
                        if(mURL.equals("")){
                            // 現在値が内場合、入力を簡略化するために「http://」だけセットしておく
                            editView.setText("http://", TextView.BufferType.NORMAL);
                        }else {
                            // 現在値をセット
                            editView.setText(mURL, TextView.BufferType.NORMAL);
                        }
                        // 入力ダイアログの表示
                        new AlertDialog.Builder(MainActivity.this)
                                .setIcon(android.R.drawable.ic_dialog_info)
                                .setTitle("URLをセットしてください")
                                .setView(editView)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        //入力したURLをセット
                                        mURL = editView.getText().toString();
                                        LoadRSS();
                                    }
                                })
                                .setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        //do nothing
                                    }
                                })
                                .show();
                    }
                });

        // 再読み込みボタン
        ((Button)findViewById(R.id.button2)).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        LoadRSS();

                    }
                });
    }

    // onStartでRSS読み込み
    @Override
    protected void onStart() {
        super.onStart();
        LoadRSS();
    }
    /**
     * RSSを読み込む
     * ※非同期処理として実行
     */
    private void LoadRSS(){
        AsyncGetRSS task = new AsyncGetRSS(this);
        task.ownerActivity = this;
        task.execute(mURL);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
