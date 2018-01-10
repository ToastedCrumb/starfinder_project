package starfinderproject.curl;

import android.animation.ValueAnimator;
import android.media.MediaPlayer;
import android.renderscript.Sampler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    protected int dmgsessiontotal = 0;
    protected int dmgtotal = 0;
    protected int toadd = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button addsessiondmg = (Button)findViewById(R.id.addsessiondmg);
        addsessiondmg.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                TextView text = (TextView) findViewById(R.id.dmgsessiontext);
                dmgsessiontotal = startAddAnimation(dmgsessiontotal, toadd, text);
            }
        });

        NumberPicker numberPicker = (NumberPicker) findViewById(R.id.numberpicker);
        numberPicker.setMaxValue(10);
        numberPicker.setMinValue(1);
        numberPicker.setWrapSelectorWheel(true);
        numberPicker.setOnValueChangedListener( new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                toadd = newVal;
            }
        });

        Button clearsessiondmg = (Button)findViewById(R.id.clearsessiondmg);
        clearsessiondmg.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                TextView text = (TextView)findViewById(R.id.dmglifetext);
                dmgtotal = startAddAnimation(dmgtotal,dmgsessiontotal,text);
                text = (TextView)findViewById(R.id.dmgsessiontext);
                dmgsessiontotal=startClearAnimation(dmgsessiontotal,text);
            }
        });

        Button cleartotaldmg = (Button)findViewById(R.id.cleartotaldmg);
        cleartotaldmg.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                TextView text = (TextView)findViewById(R.id.dmglifetext);
                dmgtotal = startClearAnimation(dmgtotal,text);
            }
        });

        Button confetti = (Button)findViewById(R.id.confettibutton);
        final MediaPlayer mp = MediaPlayer.create(this,R.raw.confetti_sound);
        confetti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mp.start();
            }
        });
    }
    private int startAddAnimation(int total, int value, TextView view) {
        int endvalue = value + total;
        final TextView text = view;
        ValueAnimator countup = ValueAnimator.ofInt(total, endvalue);
        countup.setDuration(1000);
        countup.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                text.setText(valueAnimator.getAnimatedValue().toString());
            }
        });
        countup.start();
        total += value;
        return total;
    }

    private int startClearAnimation(int total, TextView view){
        int value = 0;
        final TextView text = view;
        ValueAnimator clear = ValueAnimator.ofInt(total, value);
        clear.setDuration(1000);
        clear.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                text.setText(valueAnimator.getAnimatedValue().toString());
            }
        });
        clear.start();
        total = 0;
        return total;
    }

}
