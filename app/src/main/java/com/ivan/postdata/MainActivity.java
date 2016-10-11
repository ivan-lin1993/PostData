package com.ivan.postdata;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    private Context thisContext;
    private PostData postData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        thisContext=this;

        Button addParm=(Button)findViewById(R.id.add_btn);
        addParm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddParm();
            }
        });
        Button send=(Button)findViewById(R.id.send_btn);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Send();
            }
        });
        Button clear=(Button)findViewById(R.id.clear_btn);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Clear();
            }
        });
    }
    public void AddParm(){
        EditText value_ed=(EditText)findViewById(R.id.value);
        EditText key_ed=(EditText)findViewById(R.id.key);
        if(value_ed.getText().toString().equals("")||value_ed.getText().toString().equals("")){
            Toast.makeText(thisContext,"Empty Key & Value",Toast.LENGTH_SHORT).show();
            return;
        }
        TextView param=(TextView)findViewById(R.id.param);
        PostData.addParms(postData.getParam(),key_ed.getText().toString(),value_ed.getText().toString());
        value_ed.setText("");
        key_ed.setText("");
        param.setText(postData.getParam());
    }
    public void Clear(){
        TextView param=(TextView)findViewById(R.id.param);
        TextView result=(TextView)findViewById(R.id.result);
        param.setText("");
        result.setText("");
        postData.ClearParam();
    }
    public void Send(){
        postData=new PostData(thisContext, "Loading...", new PostData.AsyncResponse() {
            @Override
            public void processFinish(String output) {
                TextView result=(TextView)findViewById(R.id.result);
                result.setText(output);
            }
        });
        TextView url=(TextView)findViewById(R.id.url);
        postData.execute(url.getText().toString());
    }


}
