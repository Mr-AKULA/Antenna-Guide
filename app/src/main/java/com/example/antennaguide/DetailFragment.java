package com.example.antennaguide;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class DetailFragment extends Fragment {
    private static final String ARG_ANTENNA_ID = "antenna_id";

    public static DetailFragment newInstance(String antennaId) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ANTENNA_ID, antennaId);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        String antennaId = getArguments().getString(ARG_ANTENNA_ID);
        Antenna antenna = AntennaRepository.getAntennaById(requireContext(), antennaId);

        if (antenna != null) {
            TextView nameView = view.findViewById(R.id.detail_name);
            TextView descView = view.findViewById(R.id.detail_description);
            TextView principleView = view.findViewById(R.id.detail_principle);
            TextView applicationView = view.findViewById(R.id.detail_application);

            nameView.setText(antenna.getName());
            descView.setText(antenna.getFullDesc());
            principleView.setText(antenna.getPrinciple());
            applicationView.setText(antenna.getApplication());
        }

        return view;
    }
}
