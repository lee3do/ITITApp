package io.itit;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class AboutActivity extends SwipeBackActivity {

    @Bind(R.id.logo)
    ImageView logo;
    @Bind(R.id.versionText)
    TextView versionText;
    @Bind(R.id.centerText)
    TextView centerText;
    @Bind(R.id.textView)
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);

        float toY = logo.getY();

        ObjectAnimator animatorY = ObjectAnimator.ofFloat(logo, "translationY", 1000, 0);

        ObjectAnimator alpha1 = ObjectAnimator.ofFloat(versionText, "alpha", 0, 1);
        ObjectAnimator alpha2 = ObjectAnimator.ofFloat(centerText, "alpha", 0, 1);
        ObjectAnimator alpha3 = ObjectAnimator.ofFloat(textView, "alpha", 0, 1);

        AnimatorSet set3 = new AnimatorSet();
        set3.playSequentially(animatorY,alpha1,alpha2,alpha3);
        set3.setDuration(1000);
        set3.start();


    }
}
