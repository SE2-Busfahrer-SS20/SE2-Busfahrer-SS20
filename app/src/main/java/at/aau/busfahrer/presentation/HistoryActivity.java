package at.aau.busfahrer.presentation;

import android.content.SharedPreferences;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import at.aau.busfahrer.R;
import at.aau.busfahrer.service.LeaderboardService;
import at.aau.busfahrer.service.impl.LeaderboardServiceImpl;
import shared.model.PlayerDTO;

import java.util.List;


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

        SharedPreferences sharedPreferences = getSharedPreferences("shared_preferences",MODE_PRIVATE);

        leaderboardService=LeaderboardServiceImpl.getInstance();
        leaderboardService.setHostname(sharedPreferences.getString("HostName","127.0.0.1"));
        leaderboardService.registerPlayerListCallback(playerList-> runOnUiThread(() -> updateList(playerList)));
        leaderboardService.connect();
        leaderboardService.updateScoreList();


    }
    @Override
    public void onBackPressed() {
        leaderboardService.disconnect();
        super.onBackPressed();
    }
    private void updateList(List<PlayerDTO> playerList){

        String[] scoreItems= new String[playerList.size()];
        for(int i=0;i<scoreItems.length;i++){
            scoreItems[i]=playerList.get(i).getName()+": "+playerList.get(i).getScore();
        }
        View decorView = getWindow().getDecorView();
        ListView listView= decorView.findViewById(R.id.historyList);

        ArrayAdapter<String> listViewAdapter= new ArrayAdapter<>(
                getApplication(),
                android.R.layout.simple_list_item_1,
                scoreItems
        );
        listView.setAdapter(listViewAdapter);
    }
}
