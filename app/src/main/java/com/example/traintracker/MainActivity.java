package com.example.traintracker;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button trackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        trackButton = findViewById(R.id.buttonTrack);
        trackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editTextTrainNumber = findViewById(R.id.editTextTrainNumber);
                EditText editTextDate = findViewById(R.id.editTextDate);
                String trainNumber = editTextTrainNumber.getText().toString();
                String date = editTextDate.getText().toString();

                Intent intent = new Intent(MainActivity.this, DisplayLocationActivity.class);
                intent.putExtra("trainNumber", trainNumber);
                intent.putExtra("date", date);
                startActivity(intent);
            }
        });
    }
}
