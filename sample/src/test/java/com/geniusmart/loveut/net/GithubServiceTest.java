package com.geniusmart.loveut.net;

import android.util.Log;

import com.geniusmart.loveut.BuildConfig;
import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLog;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.junit.Assert.assertTrue;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class GithubServiceTest {

    private static final String TAG = "GithubServiceTest";
    GithubService githubService;

    @Before
    public void setUp() throws URISyntaxException {
        //输出日志
        ShadowLog.stream = System.out;
        githubService = GithubService.Factory.create();
    }

    @Test
    public void publicRepositories() throws IOException {
        Call<List<Repository>> call = githubService.publicRepositories("geniusmart");
        Response<List<Repository>> execute = call.execute();

        List<Repository> list = execute.body();
        Log.i(TAG,new Gson().toJson(list));
        assertTrue(list.size()>0);
    }

    @Test
    public void publicRepositoriesEnqueue() throws IOException {

        Call<List<Repository>> call = githubService.publicRepositories("geniusmart");
        //callback如何测试？？
        call.enqueue(new Callback<List<Repository>>() {
            @Override
            public void onResponse(Call<List<Repository>> call, Response<List<Repository>> response) {
                ShadowLog.v(TAG,"onResponse");
                List<Repository> body = response.body();
                assertTrue(body.size() > 0);
            }

            @Override
            public void onFailure(Call<List<Repository>> call, Throwable t) {
                ShadowLog.v(TAG, "onFailure");
            }
        });
    }
}