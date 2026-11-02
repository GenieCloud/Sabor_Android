// Gio Jones
// AID - C202605
// MainActivity.java

package com.example.jonesgio_ce04_aid;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Initialize review block
        setupReviewBlock(R.id.review1, R.string.user1_name, R.string.user1_date, R.string.user1_stars, R.string.user1_title, R.string.user1_body, R.drawable.avatar1);
        setupReviewBlock(R.id.review2, R.string.user2_name, R.string.user2_date, R.string.user2_stars, R.string.user2_title, R.string.user2_body, R.drawable.avatar2);
        setupReviewBlock(R.id.review3, R.string.user3_name, R.string.user3_date, R.string.user3_stars, R.string.user3_title, R.string.user3_body, R.drawable.avatar3);
        setupReviewBlock(R.id.review4, R.string.user4_name, R.string.user4_date, R.string.user4_stars, R.string.user4_title, R.string.user4_body, R.drawable.avatar4);
        setupReviewBlock(R.id.review5, R.string.user5_name, R.string.user5_date, R.string.user5_stars, R.string.user5_title, R.string.user5_body, R.drawable.avatar5);
    }

    /**
     * Helper method to isolate the included layout parent & inserts its specific child text.
     */
    private void setupReviewBlock(int includeId, int nameRes, int dateRes, int starRes, int titleRes, int bodyRes, int avatarRes) {
        // Finds a specific container item wrapper
        View reviewContainer = findViewById(includeId);
        if (reviewContainer != null) {
            // Find the subviews inside the reviewContainer by ID
            ImageView imgAvatar = reviewContainer.findViewById(R.id.reviewAvatar);
            TextView txtName = reviewContainer.findViewById(R.id.reviewAuthor);
            TextView txtDate = reviewContainer.findViewById(R.id.reviewDate);
            TextView txtStars = reviewContainer.findViewById(R.id.reviewRatingStars);
            TextView txtTitle = reviewContainer.findViewById(R.id.reviewTitle);
            TextView txtBody = reviewContainer.findViewById(R.id.reviewText);

            // Safely checks if views exist
            if (txtName != null) txtName.setText(getString(nameRes));
            if (txtDate != null) txtDate.setText(getString(dateRes));
            if (txtStars != null) txtStars.setText(getString(starRes));
            if (txtTitle != null) txtTitle.setText(getString(titleRes));
            if (txtBody != null) txtBody.setText(getString(bodyRes));
            if (imgAvatar != null) imgAvatar.setImageResource(avatarRes);
        }
    }
}