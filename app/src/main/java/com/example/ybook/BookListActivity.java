package com.example.ybook;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;

public class BookListActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books_list);

        ImageButton add = findViewById(R.id.addItemIcon);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BookListActivity.this, BookActivity.class );
                startActivity(intent);
            }
        });

        BookListAdapter adapter = new BookListAdapter(BookListActivity.this,
                generateData());

        ListView list = findViewById(R.id.booksListView);
        list.setAdapter(adapter);
    }

    private ArrayList<BookListModel> generateData(){
        ArrayList<BookListModel> models = new ArrayList<BookListModel>();
        models.add(new BookListModel(R.drawable.ic_baseline_star_border_24px,"First Book"));
        models.add(new BookListModel(R.drawable.ic_baseline_star_border_24px,"Second Book"));
        models.add(new BookListModel(R.drawable.ic_baseline_star_border_24px,"Third Book"));

        return models;
    }
}
