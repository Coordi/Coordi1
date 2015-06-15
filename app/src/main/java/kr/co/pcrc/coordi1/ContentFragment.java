package kr.co.pcrc.coordi1;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class ContentFragment extends Fragment {
    public static final String ARG_MENU_NUMBER = "menu_number";

    ImageView ivIcon;
    TextView tvItemName;

    public ContentFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_layout_one, container, false);

        int i = getArguments().getInt(ARG_MENU_NUMBER);
        String menu = getResources().getStringArray(R.array.menu_array)[i];

        ivIcon = (ImageView) rootView.findViewById(R.id.frag1_icon);
        tvItemName = (TextView) rootView.findViewById(R.id.frag1_text);
        tvItemName.setText(menu);
        ivIcon.setImageDrawable(rootView.getResources().getDrawable(R.drawable.action_profile));

        getActivity().setTitle(menu);
        return rootView;
    }
}
