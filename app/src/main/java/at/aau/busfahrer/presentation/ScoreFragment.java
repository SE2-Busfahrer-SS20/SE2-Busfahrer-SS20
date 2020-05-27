package at.aau.busfahrer.presentation;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import at.aau.busfahrer.R;
import shared.model.impl.PlayersStorageImpl;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScoreFragment extends Fragment {

    private PlayersStorageImpl playersStorage = PlayersStorageImpl.getInstance();

    public ScoreFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_score, container, false);
        String[] scoreItems= new String[playersStorage.getPlayerNamesList().size()];
        for(int i=0;i<scoreItems.length;i++){
            scoreItems[i]=playersStorage.getPlayerName(i)+": "+playersStorage.getScoreList().get(i);
        }

        ListView listView= (ListView) view.findViewById(R.id.scoreList);

        ArrayAdapter<String> listViewAdapter= new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                scoreItems
        );
        listView.setAdapter(listViewAdapter);
        // Inflate the layout for this fragment
        return view;
    }
}
