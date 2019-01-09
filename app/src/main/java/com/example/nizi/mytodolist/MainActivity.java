package com.example.nizi.mytodolist;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity{

    private Button button;
    private ListView listView;
    private EditText editTitle;
    private EditText editDescription;
    private CheckBox cb;

    ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.add);
        listView = (ListView) findViewById(R.id.list);
        editTitle = (EditText) findViewById(R.id.editTitle);
        editDescription = (EditText) findViewById(R.id.editDescription);
        cb = (CheckBox) findViewById(R.id.delete);

        final MyAdapter myAdapter = new MyAdapter(MainActivity.this,list, R.id.delete, R.layout.list_item,
                new String[]{"title", "description"},
                new int[] {R.id.line_a, R.id.line_b});
        listView.setAdapter(myAdapter);
        myAdapter.notifyDataSetChanged();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.add:
                        HashMap<String, String> taskMap = new HashMap<String, String>();
                        if(TextUtils.isEmpty(editTitle.getText())||TextUtils.isEmpty(editDescription.getText())){
                            Toast.makeText(MainActivity.this, "Must input something!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        taskMap.put("title", editTitle.getText().toString());
                        taskMap.put("description", editDescription.getText().toString());

                        if (list.add(taskMap)) {
                            Toast.makeText(MainActivity.this, "Task Added", Toast.LENGTH_SHORT).show();
                        }
                        myAdapter.notifyDataSetChanged();
                        editTitle.setText("");
                        editDescription.setText("");
                        FileHelper.writeData(list, MainActivity.this);
                        break;
                }
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                list.remove(position);
                myAdapter.notifyDataSetChanged();
                Toast.makeText(MainActivity.this, "Task Deleted", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

    }

    public class MyAdapter extends SimpleAdapter{
        ArrayList<HashMap<String, String>> list;
        private int cb_ID;

        public MyAdapter(Context context, ArrayList<HashMap<String, String>> list, int cb_ID, int resource, String[] from, int[] to){
            super(context, list, resource, from, to);
            this.list = list;
            this.cb_ID = cb_ID;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent){
            View view = super.getView(position, convertView, parent);
            final CheckBox del = (CheckBox) view.findViewById(R.id.delete);
            del.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    list.remove(position);
                    del.setChecked(false);
                    notifyDataSetChanged();
                }
            });
            return view;
        }
    }
}
