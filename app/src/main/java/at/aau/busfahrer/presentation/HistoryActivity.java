package at.aau.busfahrer.presentation;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.viewpager.widget.ViewPager;
import at.aau.busfahrer.R;
import at.aau.busfahrer.service.LeaderboardService;
import at.aau.busfahrer.service.impl.LeaderboardServiceImpl;


public class HistoryActivity extends AppCompatActivity {

    private LeaderboardService leaderboardService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        leaderboardService=LeaderboardServiceImpl.getInstance();
        leaderboardService.updateScoreList();

        String[] scoreItems= new String[]{"Philipp: 10","Larissa: 9","Markus: 8","Elias: 7","Volte: 6","Gery: 10"};

        ListView listView= (ListView) decorView.findViewById(R.id.historyList);

        ArrayAdapter<String> listViewAdapter= new ArrayAdapter<String>(
                getApplication(),
                android.R.layout.simple_list_item_1,
                scoreItems
        );
        listView.setAdapter(listViewAdapter);
    }

}
