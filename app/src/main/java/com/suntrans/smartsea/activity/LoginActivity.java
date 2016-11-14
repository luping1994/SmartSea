package com.suntrans.smartsea.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.suntrans.smartsea.R;
import com.suntrans.smartsea.widget.EditView;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private Button login ;
    private EditView ed_account;
    private EditView ed_password;
    private String account;
    private String password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login = (Button) findViewById(R.id.login);
        ed_account = (EditView) findViewById(R.id.account);
        ed_password = (EditView) findViewById(R.id.password);
        String zhanghao = getSharedPreferences("config", Context.MODE_PRIVATE).getString("account","");
        String mima = getSharedPreferences("config", Context.MODE_PRIVATE).getString("password","");
        ed_account.setText(zhanghao);
        ed_password.setText(mima);
        login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login:{
                account = ed_account.getText().toString();
                password = ed_password.getText().toString();
                if (account.equals("admin")&&password.equals("suntrans123456")){
                    getSharedPreferences("config", Context.MODE_PRIVATE).edit().putString("account",account).commit();
                    getSharedPreferences("config", Context.MODE_PRIVATE).edit().putString("password",password).commit();
                    startActivity(new Intent(LoginActivity.this, Main_activity.class));
                    finish();
                }else {
                    Toast.makeText(LoginActivity.this,"账号或密码错误",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
