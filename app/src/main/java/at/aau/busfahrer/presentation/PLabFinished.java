package at.aau.busfahrer.presentation;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

import at.aau.busfahrer.R;
import at.aau.busfahrer.service.PLabService;
import at.aau.busfahrer.service.impl.PLabServiceImpl;

public class PLabFinished extends AppCompatActivity {

    private PLabService pLabService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p_lap_finished);
        pLabService = PLabServiceImpl.getInstance();
        updateList(pLabService.getPlayerNames());
        pLabService.registerFinishedLabCallback(lostGame -> {
            runOnUiThread(() -> {
                // Switch to Busfaher Lab and pass boolean attribute
            });
        });
        findViewById(R.id.progressBar_pfinished).setVisibility(View.INVISIBLE);
    }

    public void dealButtonClick(View v) {
        String selectedPlayer = ((Spinner)findViewById(R.id.sP_name_spinner)).getSelectedItem().toString();
        pLabService.dealPoints(selectedPlayer);
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
