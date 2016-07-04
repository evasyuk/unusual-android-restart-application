package com.jv.example.restart;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class StarterActivity extends AppCompatActivity {

    Button makeCrash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starter);

        App.setIntent(getIntent());

        makeCrash = (Button) findViewById(R.id.button);

        makeCrash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                throw new RuntimeException("we've got it");
            }
        });

        checkCrash(getIntent());
    }

    private void checkCrash(Intent intent) {
        if (intent != null) {
            boolean wasItCrashed = intent.getBooleanExtra(App.CRASHED_BOOLEAN_FLAG_KEY, false);
            if (wasItCrashed) {
                Toast.makeText(StarterActivity.this, "Sorry, but app seems to be crashed", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
