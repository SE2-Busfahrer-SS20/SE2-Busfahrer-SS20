package at.aau.busfahrer.presentation;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

import at.aau.busfahrer.R;
import at.aau.busfahrer.service.PLapClientService;
import at.aau.busfahrer.service.impl.PLapClientServiceImpl;

public class PLabFinished extends AppCompatActivity {

    private PLapClientService pLapClientService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p_lap_finished);
        pLapClientService = PLapClientServiceImpl.getInstance();
        updateList(pLapClientService.getPlayerNames());
        pLapClientService.registerFinishedLabCallback(lostGame -> runOnUiThread(() -> {
            Intent bushmenIntent = new Intent(this, BushmenActivity.class);
            bushmenIntent.putExtra("LOST_GAME", lostGame);
            startActivity(bushmenIntent);
        }));
        findViewById(R.id.progressBar_pfinished).setVisibility(View.INVISIBLE);
    }

    public void dealButtonClick(View v) {
        String selectedPlayer = ((Spinner)findViewById(R.id.sP_name_spinner)).getSelectedItem().toString();
        pLapClientService.dealPoints(selectedPlayer);
        setWait();
        v.setEnabled(false);
    }

    private void updateList(List<String> list) {
        ArrayAdapter<String> listViewAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        ((Spinner)findViewById(R.id.sP_name_spinner)).setAdapter(listViewAdapter);
    }

    private void setWait() {
        findViewById(R.id.tV_plab_finished_info).setVisibility(View.INVISIBLE);
        ((TextView)findViewById(R.id.tV_points_todeal)).setText("Waiting for other Players...");
        findViewById(R.id.bT_deal_button).setVisibility(View.INVISIBLE);
        findViewById(R.id.sP_name_spinner).setVisibility(View.INVISIBLE);
        findViewById(R.id.progressBar_pfinished).setVisibility(View.VISIBLE);
    }

}
