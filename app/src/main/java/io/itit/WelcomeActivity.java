package io.itit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.orhanobut.logger.Logger;

import java.util.UUID;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.itit.db.DBHelper;
import io.itit.db.Data;
import io.itit.http.HttpUtils;

import static io.itit.ITITApplication.uuid;

public class WelcomeActivity extends AppCompatActivity {

    @Bind(R.id.welcome)
    ImageView welcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager
                .LayoutParams.FLAG_FULLSCREEN);
        initStartPage();
    }


    private void initStartPage() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager
                .LayoutParams.FLAG_FULLSCREEN);
        AlphaAnimation start_anima = new AlphaAnimation(0.5f, 1.0f);
        start_anima.setDuration(getDuaration());

        start_anima.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                Data user = DBHelper.getDataByKey("USER");
                if (user == null) {
                    uuid = UUID.randomUUID().toString();
                    DBHelper.insertValue("USER", uuid);
                } else {
                    uuid = user.getValue();
                }
                Logger.d("reg user :"+uuid);
                HttpUtils.appApis.register(uuid);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                getWindow().setFlags(~WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager
                        .LayoutParams.FLAG_FULLSCREEN);

                startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                finish();
            }
        });
        welcome.startAnimation(start_anima);
    }

    protected long getDuaration() {
        return 1000;
    }
}
