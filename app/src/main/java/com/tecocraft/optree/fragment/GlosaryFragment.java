package com.tecocraft.optree.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tecocraft.optree.R;
import com.tecocraft.optree.adapter.GrosaryListAdapter;
import com.tecocraft.optree.model.GlosaryModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class GlosaryFragment extends Fragment {

    List<GlosaryModel> dataList = new ArrayList<>();
    @BindView(R.id.rvList)
    RecyclerView rvList;
    @BindView(R.id.tvError)
    TextView tvError;
    private GrosaryListAdapter adapter;

    public GlosaryFragment() {
        // Required empty public constructor
    }


    String validateString = "[\n" +
            "                           { \"title\" : \"AE\",\n" +
            "                              \"content\" : \"Above elbow amputation\"},\n" +
            "                           { \"title\" : \"AFO\",\n" +
            "                              \"content\" : \"Ankle foot orthosis\"},\n" +
            "                           { \"title\" : \"AK\",\n" +
            "                              \"content\" : \"Above the Knee.  Usually refers to an above the knee amputation or above the knee compression hose\"},\n" +
            "                           { \"title\" : \"ASIS\",\n" +
            "                              \"content\" : \"Anterior superior iliac spine of the pelvic bone\"},\n" +
            "                           { \"title\" : \"BE\",\n" +
            "                              \"content\" : \"Below Elbow amputation\"},\n" +
            "                           { \"title\" : \"BK\",\n" +
            "                              \"content\" : \"Below the knee amputation\"},\n" +
            "                           { \"title\" : \"CO\",\n" +
            "                              \"content\" : \"Cervical orthosis\"},\n" +
            "                           { \"title\" : \"CTLSO\",\n" +
            "                              \"content\" : \"Cervical thoracic lumbar sacral orthosis\"},\n" +
            "                           { \"title\" : \"Custom fit\",\n" +
            "                              \"content\" : \"Devices that are prefabricated.  They may or may not be supplied as a kit that requires some assembly. Assembly of the item and/or installation of add-on components and/or the use of some basic materials in preparation of the item does not change classification from OTS to custom fitted.  Classification as custom fitted requires substantial modification for fitting at the time of delivery in order to provide an individualized fit, i.e., the item must be trimmed, bent, molded (with or without heat), or otherwise modified resulting in alterations beyond minimal self-adjustment.  This fitting at delivery does require expertise of a certified orthotist or an individual who has equivalent specialized training in the provision of orthosis to fit the item to the individual beneficiary.  Substantial modification is defined as changes made to achieve an individualized fit of the item that requires the expertise of a certified orthotist or an individual who has equivalent specialized training in the provision of orthotics such as a physician, treating practitioner, an occupational therapist, or physical therapist in compliance with all applicable Federal and State licensure and regulatory requirements\"},\n" +
            "                           { \"title\" : \"DIP\",\n" +
            "                              \"content\" : \"Distal Inter phalange\"},\n" +
            "                           { \"title\" : \"DME\",\n" +
            "                              \"content\" : \"Durable medical equipment\"},\n" +
            "                           { \"title\" : \"ED\",\n" +
            "                              \"content\" : \"Elbow disarticulation amputation\"},\n" +
            "                           { \"title\" : \"ENDO\",\n" +
            "                              \"content\" : \"Endoskeletal\"},\n" +
            "                           { \"title\" : \"EO\",\n" +
            "                              \"content\" : \"Elbow orthosis\"},\n" +
            "                           { \"title\" : \"EWHO\",\n" +
            "                              \"content\" : \"Elbow wrist hand orthosis\"},\n" +
            "                           { \"title\" : \"EXO\",\n" +
            "                              \"content\" : \"Exoskeletal\"},\n" +
            "                           { \"title\" : \"FFO\",\n" +
            "                              \"content\" : \"Functional foot orthotics\"},\n" +
            "                           { \"title\" : \"FO\",\n" +
            "                              \"content\" : \"Finger orthosis\"},\n" +
            "                           { \"title\" : \"HD\",\n" +
            "                              \"content\" : \"Hip disarticulation\"},\n" +
            "                           { \"title\" : \"HEMI\",\n" +
            "                              \"content\" : \"Hemi pelvectomy\"},\n" +
            "                           { \"title\" : \"HFO\",\n" +
            "                              \"content\" : \"Hand finger orthosis\"},\n" +
            "                           { \"title\" : \"HKAFO\",\n" +
            "                              \"content\" : \"Hip knee ankle foot orthosis\"},\n" +
            "                           { \"title\" : \"HO\",\n" +
            "                              \"content\" : \"Refers to hand orthosis when referring to upper extremity, or hip orthosis for lower extremity\"},\n" +
            "                           { \"title\" : \"IT\",\n" +
            "                              \"content\" : \"Interscapular thoracic amputation\"},\n" +
            "                           { \"title\" : \"KAFO\",\n" +
            "                              \"content\" : \"Knee ankle foot orthosis\"},\n" +
            "                           { \"title\" : \"KO\",\n" +
            "                              \"content\" : \"Knee orthosis\"},\n" +
            "                           { \"title\" : \"LO\",\n" +
            "                              \"content\" : \"Lumbar orthosis\"},\n" +
            "                           { \"title\" : \"LSO\",\n" +
            "                              \"content\" : \"Lumbar sacral orthosis\"},\n" +
            "                           { \"title\" : \"OTS\",\n" +
            "                              \"content\" : \"Off-the-shelf\"},\n" +
            "                           { \"title\" : \"PIP\",\n" +
            "                              \"content\" : \"Proximal inter phalange\"},\n" +
            "                           { \"title\" : \"Prefab\",\n" +
            "                              \"content\" : \"Pre fabricated device\"},\n" +
            "                           { \"title\" : \"PTB\",\n" +
            "                              \"content\" : \"Patellar tendon bearing\"},\n" +
            "                           { \"title\" : \"SACH\",\n" +
            "                              \"content\" : \"Solid ankle cushion heel\"},\n" +
            "                           { \"title\" : \"SD\",\n" +
            "                              \"content\" : \"Shoulder disarticulation\"},\n" +
            "                           { \"title\" : \"SEWHFO\",\n" +
            "                              \"content\" : \"shoulder elbow wrist hand finger orthosis\"},\n" +
            "                           { \"title\" : \"SEWHO\",\n" +
            "                              \"content\" : \"Shoulder elbow wrist hand orthosis\"},\n" +
            "                           { \"title\" : \"SMO\",\n" +
            "                              \"content\" : \"Supra malleolar orthosis\"},\n" +
            "                           { \"title\" : \"SO\",\n" +
            "                              \"content\" : \"shoulder orthosis when referring to upper extremity, sacral orthosis when referring to spine\"},\n" +
            "                           { \"title\" : \"TLSO\",\n" +
            "                              \"content\" : \"Thoraco lumbar sacral orthosis\"},\n" +
            "                           { \"title\" : \"UCBL\",\n" +
            "                              \"content\" : \"High profile foot orthotics\"},\n" +
            "                           { \"title\" : \"VC\",\n" +
            "                              \"content\" : \"voluntary closing terminal device\"},\n" +
            "                           { \"title\" : \"VO\",\n" +
            "                              \"content\" : \"voluntary opening terminal device\"},\n" +
            "                           { \"title\" : \"WD\",\n" +
            "                              \"content\" : \"wrist disarticulation\"},\n" +
            "                           { \"title\" : \"WHFO\",\n" +
            "                              \"content\" : \"wrist hand finger orthosis\"},\n" +
            "                           { \"title\" : \"WHO\",\n" +
            "                              \"content\" : \"wrist hand orthosis\"}]\n";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_glosary, container, false);

        dataList = new Gson().fromJson(validateString, new TypeToken<List<GlosaryModel>>() {
        }.getType());


        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rvList.setLayoutManager(mLayoutManager);
        rvList.setItemAnimator(new DefaultItemAnimator());
        adapter = new GrosaryListAdapter(getActivity(), dataList, GlosaryFragment.this);
        rvList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


}
