package com.duff.ImitateQQSwipeDelete;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.duff.ImitateQQSlideMenu.R;

import java.util.ArrayList;

// ┏┓　　　┏┓
// ┏┛┻━━━┛┻┓
// ┃　　　　　　　┃ 　
// ┃　　　━　　　┃
// ┃　┳┛　┗┳　┃
// ┃　　　　　　　┃
// ┃　　　┻　　　┃
// ┃　　　　　　　┃
// ┗━┓　　　┏━┛
// ┃　　　┃ 神兽保佑　　　　　　　　
// ┃　　　┃ 永不crush
// ┃　　　┗━━━┓
// ┃　　　　　　　┣┓
// ┃　　　　　　　┏┛
// ┗┓┓┏━┳┓┏┛
// ┃┫┫　┃┫┫
// ┗┻┛　┗┻┛
public class MainActivity extends Activity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private Cursor cursor;
    private ListView listview;
    private ArrayList<Message> mMessagesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //1.准备数据
        initData();
        initView();
    }

    private void initView() {
        listview = (ListView) findViewById(R.id.listview);
        listview.setAdapter(new MyAdapter());

        listview.setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    //如果垂直滑动，则需要关闭已经打开的layout
                    SwipeLayoutManager.getInstance().closeCurrentLayout();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
            }
        });
    }

    private void initData() {
        try {
            Uri uri = Uri.parse("content://sms/");// 所有短信
            cursor = this.getContentResolver().query(uri,
                    new String[]{"address", "date", "type", "body","_id"}, null, null,
                    null);
            //type =>  ALL    = 0;
//            INBOX  = 1;
//            SENT   = 2;
//            DRAFT  = 3;
//            OUTBOX = 4;
//            FAILED = 5;
//            QUEUED = 6;
            while (cursor != null && !cursor.isClosed() && cursor.moveToNext()) {
                DateUtilx dateUtilx = new DateUtilx();
                Message mMessage = new Message();
                mMessage.setId(cursor.getString(4));
                mMessage.setAddress(cursor.getString(0));
                mMessage.setBody(cursor.getString(3));
                mMessage.setType(cursor.getString(2));

                String date = cursor.getString(1);
                CharSequence relativeTime = DateUtils.getRelativeDateTimeString(MainActivity.this, Long.parseLong(date),
                        DateUtils.MINUTE_IN_MILLIS, DateUtils.WEEK_IN_MILLIS, DateUtils.FORMAT_NUMERIC_DATE);
                mMessage.setDate(relativeTime.toString());
                mMessagesList.add(mMessage);

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    class MyAdapter extends BaseAdapter implements SwipeLayout.OnSwipeStateChangeListener {
        @Override
        public int getCount() {
            return mMessagesList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(MainActivity.this, R.layout.adapter_list, null);
            }
            ViewHolder holder = ViewHolder.getHolder(convertView);

            holder.tv_name.setText(mMessagesList.get(position).getBody());
            holder.tv_date.setText(mMessagesList.get(position).getDate());
            holder.tv_title.setText(mMessagesList.get(position).getAddress());
            holder.tv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String id = mMessagesList.get(position).getId();
                    Log.e(TAG,"删除的Id为---"+id);
                    MainActivity.this.getContentResolver().delete(Uri.parse("content://sms"), "_id=?",
                            new String[]{id});
                    mMessagesList.remove(position);
                    notifyDataSetChanged();
                }
            });

            holder.tv_call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //用intent启动拨打电话
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mMessagesList.get(position).getAddress()));
                    if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        return;
                    }
                    startActivity(intent);
                }
            });
			
			holder.swipeLayout.setTag(position);
			holder.swipeLayout.setOnSwipeStateChangeListener(this);
			
			return convertView;
		}
		@Override
		public void onOpen(Object tag) {
            // TODO: 2017/3/3 开启
        }
		@Override
		public void onClose(Object tag) {
            // TODO: 2017/3/3 关闭
        }
		
	}
	
	static class ViewHolder{
		TextView tv_name,tv_call,tv_delete,tv_title,tv_date;
		SwipeLayout swipeLayout;
		public ViewHolder(View convertView){
            tv_call = (TextView) convertView.findViewById(R.id.tv_call);
            tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            tv_date = (TextView) convertView.findViewById(R.id.tv_date);
			tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			tv_delete = (TextView) convertView.findViewById(R.id.tv_delete);
			swipeLayout = (SwipeLayout) convertView.findViewById(R.id.swipeLayout);
		}
		public static ViewHolder getHolder(View convertView){
			ViewHolder holder = (ViewHolder) convertView.getTag();
			if(holder==null){
				holder = new ViewHolder(convertView);
				convertView.setTag(holder);
			}
			return holder;
		}
	}
}
