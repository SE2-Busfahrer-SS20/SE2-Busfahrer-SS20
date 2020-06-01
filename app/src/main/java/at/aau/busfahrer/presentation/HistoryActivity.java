package at.aau.busfahrer.presentation;

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
    private List<PlayerDTO> playerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        leaderboardService=LeaderboardServiceImpl.getInstance();
        leaderboardService.registerPlayerListCallback(playerList->{

            runOnUiThread(() -> {
                System.out.println("\n\n\nCALLBACK FUNCTION!!!!");
                updateList(playerList);
            });
        });
        leaderboardService.updateScoreList();
        leaderboardService.connect();

    }
    private void updateList(List<PlayerDTO> playerList){

        //String[] scoreItems= new String[]{"Philipp: 10","Larissa: 9","Markus: 8","Elias: 7","Volte: 6","Gery: 10"};
        String[] scoreItems= new String[playerList.size()];
        for(int i=0;i<scoreItems.length;i++){
            scoreItems[i]=playerList.get(i).getName()+": "+playerList.get(i).getScore();
        }
        View decorView = getWindow().getDecorView();
        ListView listView= (ListView) decorView.findViewById(R.id.historyList);

        ArrayAdapter<String> listViewAdapter= new ArrayAdapter<String>(
                getApplication(),
                android.R.layout.simple_list_item_1,
                scoreItems
        );
        listView.setAdapter(listViewAdapter);
    }
}
