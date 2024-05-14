package com.example.github2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
//import android.widget.EditText;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    private ProgressBar mProgressBar;
    private TextView mTextView;
    private EditText et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mTextView = (TextView) findViewById(R.id.textView);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.INVISIBLE);
        et = findViewById(R.id.editText);
    }


    public void onClick(View view) {
        mProgressBar.setVisibility(View.VISIBLE);

        mTextView = findViewById(R.id.textView);
       // mTextView.setText("В процессе...");
        GitHubService gitHubService = GitHubService.retrofit.create(GitHubService.class);
        final Call<User> call =
                gitHubService.getUser(et.getText().toString());
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful())
                {
                    User user = response.body();
                    mTextView.setText(
                            "Аккаунт:" + user.getName()+
                            "\nСайт: " + user.getBlog() +
                            "\nКомпания: " + user.getCompany()
                    );
                }
                else
                {
                    int statusCode = response.code();

                    ResponseBody errorBody = response.errorBody();
                    try {
                        mTextView.setText(errorBody.string());
                        mProgressBar.setVisibility(View.VISIBLE);
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                mTextView.setText("Что-то пошло не так" + t.getMessage());
            }
        });
    }
}