package at.aau.busfahrer.presentation;

import android.os.Bundle;
import android.webkit.WebView;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import at.aau.busfahrer.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutFragment extends Fragment {

    public AboutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about, container, false);

        WebView webView= (WebView) view.findViewById(R.id.webViewAbout);
        webView.loadUrl("file:///android_asset/about.html");
        return view;
    }
}
