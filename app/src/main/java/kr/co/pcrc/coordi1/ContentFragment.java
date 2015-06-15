package kr.co.pcrc.coordi1;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ContentFragment extends Fragment {
    public static final String ARG_MENU_NUMBER = "menu_number";

    private ListView listView;
    private ArrayList<String> arrayList;
    private ArrayAdapter<String> adapter;

    public ContentFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_layout_list, container, false);

        arrayList = new ArrayList<String>();
        arrayList.add("안");
        arrayList.add("녕");
        arrayList.add("하");
        arrayList.add("십");
        arrayList.add("니");
        arrayList.add("까");
        arrayList.add("환");
        arrayList.add("영");
        arrayList.add("합");
        arrayList.add("니");
        arrayList.add("다");
        arrayList.add(".");
        arrayList.add("안");
        arrayList.add("녕");
        arrayList.add("하");
        arrayList.add("십");
        arrayList.add("니");
        arrayList.add("까");
        arrayList.add("환");
        arrayList.add("영");
        arrayList.add("합");
        arrayList.add("니");
        arrayList.add("다");
        arrayList.add(".");

        adapter = new ArrayAdapter<String>(super.getActivity(), android.R.layout.simple_list_item_1, arrayList);

        listView = (ListView) rootView.findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String str = (String) adapter.getItem(position);
                Toast.makeText(getView().getContext(), str, Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }
}
