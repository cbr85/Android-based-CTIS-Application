package com.example.ctisadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.Serializable;

public class MenuActivity extends AppCompatActivity {

    Button recordTester;
    Button manageKit;
    CentreOfficer centreOfficer = new CentreOfficer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Intent i = getIntent();
        centreOfficer = (CentreOfficer) i.getSerializableExtra("officer");

        recordTester = findViewById(R.id.bottom_record_tester);
        manageKit = findViewById(R.id.bottom_manage_test_kit_stock);

        recordTester.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), RecordTesterActivity.class);
                intent.putExtra("officer", (Serializable) centreOfficer);
                startActivity(intent);
            }
        });

        manageKit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), ManageTestKitStockActivity.class);
                intent.putExtra("officer", (Serializable) centreOfficer);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(), "Logout from app",
                Toast.LENGTH_SHORT).show();
        super.onBackPressed();
    }
}