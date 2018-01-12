package starfinderproject.curl;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.renderscript.Sampler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

        //Setup text colour and font
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/mechsuit/Mechsuit.otf");
        final TextView dmglifetext = (TextView) findViewById(R.id.dmglifetext);
        dmglifetext.setTextColor(Color.parseColor("#18CAE6"));
        dmglifetext.setTypeface(typeface);

        final TextView dmgsessiontext = (TextView) findViewById(R.id.dmgsessiontext);
        dmgsessiontext.setTextColor(Color.parseColor("#18CAE6"));
        dmgsessiontext.setTypeface(typeface);

        //Add session damage on button press
        Button addsessiondmg = (Button) findViewById(R.id.addsessiondmg);
        addsessiondmg.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //Open the number selector modal
                numberSelector();
            }
        });

        //Clear session damage on button press
        Button clearsessiondmg = (Button) findViewById(R.id.clearsessiondmg);
        clearsessiondmg.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //Animate the value transition for both total and session damage. Transfer damage from session to total
                dmgtotal = startAddAnimation(dmgtotal, dmgsessiontotal, dmglifetext);
                dmgsessiontotal = startClearAnimation(dmgsessiontotal, dmgsessiontext);
            }
        });

        //Clear total damage on button press
        Button cleartotaldmg = (Button) findViewById(R.id.cleartotaldmg);
        cleartotaldmg.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //Animate the value transition for total. Modify total damage back to zero
                dmgtotal = startClearAnimation(dmgtotal, dmglifetext);
            }
        });

        //Play grunt headshot sound on button press
        Button confetti = (Button) findViewById(R.id.confettibutton);
        final MediaPlayer mp = MediaPlayer.create(this, R.raw.confetti_sound);
        confetti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mp.start();
            }
        });
    }

    //Animation of text from one value to another
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

    //Animation of text from some value to zero
    private int startClearAnimation(int total, TextView view) {
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

    //Number selector modal
    private void numberSelector() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Get the layout inflater
        LayoutInflater inflater = this.getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        // NOTE: dlgedititem is a layout xml file.
        View v = inflater.inflate(R.layout.number_modal, null);

        // Optionally set some values in the view...
        final NumberPicker guiQty = (NumberPicker) v.findViewById(R.id.numberpicker);
        guiQty.setMinValue(1);
        guiQty.setMaxValue(20000);
        guiQty.setValue(1);

        // Add the view to the builder.
        builder.setView(v)
                // Add action buttons
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    //Add value and do transition animation on "OK" press
                    public void onClick(DialogInterface dialog, int id) {
                        toadd = guiQty.getValue();
                        TextView text = (TextView) findViewById(R.id.dmgsessiontext);
                        dmgsessiontotal = startAddAnimation(dmgsessiontotal, toadd, text);
                        Log.d("click",String.valueOf(toadd));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Do something when Cancel clicked.
                    }
                });

        // Create the actual AlertDialog
        AlertDialog dlg2 = builder.create();

        // Display the modal.
        dlg2.show();
    }
}
