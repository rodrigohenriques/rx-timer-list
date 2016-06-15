package com.github.rodrigohenriques.timerlist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(buildAdapter());
    }

    private RecyclerView.Adapter buildAdapter() {
        List<String> names = new ArrayList<>();

        names.add("Rodrigo");
        names.add("Magno");
        names.add("Lucas");
        names.add("Henrique");
        names.add("Marcelo");
        names.add("Nadilson");
        names.add("Rafael");
        names.add("Walmyr");
        names.add("Haroldo");
        names.add("Samir");
        names.add("Eugênio");
        names.add("Leonardo");
        names.add("Maurício");
        names.add("Júlio");

        return new Adapter(this, names);
    }
}
