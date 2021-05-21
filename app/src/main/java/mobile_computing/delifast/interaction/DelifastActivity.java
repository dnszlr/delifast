package mobile_computing.delifast.interaction;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

import mobile_computing.delifast.R;
import mobile_computing.delifast.authentication.AuthenticationActivity;

public class DelifastActivity extends AppCompatActivity {

    private Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delifast);
        btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent homepage = new Intent(DelifastActivity.this, AuthenticationActivity.class);
                startActivity(homepage);
                finish();
            }
        });
    }
}