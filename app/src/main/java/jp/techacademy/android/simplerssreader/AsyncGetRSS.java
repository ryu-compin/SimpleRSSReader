package jp.techacademy.android.simplerssreader;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import nl.matshofman.saxrssreader.RssFeed;
import nl.matshofman.saxrssreader.RssItem;
import nl.matshofman.saxrssreader.RssReader;

/**
 * RSSを非同期で読み込んでリストビューに表示する
 */
public class AsyncGetRSS extends AsyncTask<String, Integer, ArrayList<RssItem>> {
    // Logcatのタグ
    private String TAG = "RSS Reader";
    // 表示する記事の最大数
    public int maxTopicsNum = 20;
    // 親アクティビティを格納する
    public Activity ownerActivity;
    // 親アクティビティのlistViewを格納する
    private ListView lv = null;

    // コンストラクタ（初期処理）
    public AsyncGetRSS(Activity activity){
        // 親アクティビティのセット
        ownerActivity = activity;
        // listViewをセット
        this.lv = (ListView) ownerActivity.findViewById(R.id.listView);
    }

    @Override
    protected ArrayList<RssItem> doInBackground(String... mURL) {
        // 取得するRSSのURLを格納
        URL targetURL;
        if(mURL.equals("")){
            //URLが指定されてなければ何もしない
            return null;
        }else{
            try {
                // URLをセット
                targetURL = new URL(mURL[0]);
            } catch (MalformedURLException e) {
                e.printStackTrace();
                Log.e(TAG,e.toString());
                return null;
            }
        }
        RssFeed feed = null;
        Log.d(TAG,"Load start [" + targetURL + "]");
        try {
            // RSS読み込み＆Parse
            feed = RssReader.read(targetURL);
        } catch (SAXException| IOException e) {
            e.printStackTrace();
            Log.d(TAG, e.toString());
            Log.d(TAG,"load failed");
            return null;
        }

        // ParseしたRSSの内容をリストに格納
        ArrayList<RssItem> rssItems = feed.getRssItems();
        return rssItems;
    }
    @Override
    protected void onPostExecute(ArrayList<RssItem> itm){
        // listViewにタイトルを挿入するためのアダプタ
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this.ownerActivity, android.R.layout.simple_list_item_1);
        this.lv.setAdapter(adapter);
        // RSSの記事を配列に保存
        final ArrayList<RssItem> rssItems = itm;
        int i = 0;
        String title="";
        //String link="";
        //String description="";
        //String pubdate = "";
        // セットされた最大数または記事数ぶんだけlistViewに追加
        for(RssItem rssItem : itm) {
            // カウントして表示最大数を超えたら繰り返し処理を抜ける
            i++;
            if(i > maxTopicsNum){
                break;
            }
            // タイトルを取得
            title = rssItem.getTitle();
            // 以下ここでは扱わないが、URL、記事詳細、公開日付などを取得する例
            //link = rssItem.getLink();
            //description = rssItem.getDescription();
            //pubdate  = rssItem.getPubDate().toString();
            Log.d(TAG, title);
            // listビューに表示
            adapter.add(title);
        }
        /*
        記事タイトルをクリックしたらブラウザを起動
         */
        this.lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // listviewのIDから該当する記事のURLを取得
                // ※文字列型からUri型へ変換
                Uri uri = Uri.parse(rssItems.get((int) id).getLink());
                Log.d(TAG, String.valueOf(id) + ":" + uri.toString());
                // インテントによるブラウザの起動
                Intent i = new Intent(Intent.ACTION_VIEW,uri);
                ownerActivity.startActivity(i);
            }
        });

    }
    /*
    以下ここでは扱わないが、表示する記事数を制御する場合に使用する最大表示数の変更処理
     */
    public void setMaxTopicsNum(int maxTopicsNum) {
        this.maxTopicsNum = maxTopicsNum;
    }
    /*
    以下ここでは扱わないが、表示する記事数を制御する場合に使用する最大表示数の現在値の取得処理
     */
    public int getMaxTopicsNum() {
        return maxTopicsNum;
    }
}
