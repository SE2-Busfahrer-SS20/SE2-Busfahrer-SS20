package at.aau.busfahrer.presentation;

import android.content.Intent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import at.aau.busfahrer.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import at.aau.busfahrer.presentation.utils.CardUtility;
import at.aau.busfahrer.service.GamePlayService;
import at.aau.busfahrer.service.impl.GamePlayServiceImpl;
import shared.model.PlayerDTO;
import shared.model.impl.PlayersStorageImpl;



public class GameOverviewActivity extends AppCompatActivity {

    PlayersStorageImpl playersStorage;
    private GamePlayService gamePlayService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_overview);
        playersStorage= PlayersStorageImpl.getInstance();
        gamePlayService = GamePlayServiceImpl.getInstance();

        updateListScreen();

        //each client sends their own score
        sendDataToServer();
    }

    // click listener BackToMainMenu button
    public void onClickBackToMainMenu(View v){
        Intent i = new Intent(GameOverviewActivity.this, MainMenuActivity.class);
        startActivity(i);
    }
    private void sendDataToServer(){
        PlayerDTO myGameData= playersStorage.getPlayerList().get(playersStorage.getTempID());
        myGameData.setMAC(CardUtility.getMacAddr());
        gamePlayService.sendScoreDataToServer(myGameData);
    }
    private void updateListScreen(){

        String[] playerList= new String[playersStorage.getPlayerListAscending().size()];
        for(int i=0;i<playerList.length;i++){
            playerList[i]=(i+1)+". place "+ playersStorage.getPlayerListAscending().get(i).getName()+": "+playersStorage.getPlayerListAscending().get(i).getScore();
        }
        View decorView = getWindow().getDecorView();
        ListView listView= decorView.findViewById(R.id.overviewList);

        ArrayAdapter<String> listViewAdapter= new ArrayAdapter<>(
                getApplication(),
                android.R.layout.simple_list_item_1,
                playerList
        );
        listView.setAdapter(listViewAdapter);
    }
}
