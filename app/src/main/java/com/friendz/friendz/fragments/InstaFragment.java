package com.friendz.friendz.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.friendz.friendz.InstagramActivity;
import com.friendz.friendz.R;
import com.friendz.friendz.adapters.InstaAdapter;
import com.friendz.friendz.api.ApiHelper;
import com.friendz.friendz.db.InstaDataItem;
import com.friendz.friendz.db.InstagramMediaResponse;
import com.friendz.friendz.db.PostsDataItem;
import com.steelkiwi.instagramhelper.utils.SharedPrefUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InstaFragment extends Fragment {

    @BindView(R.id.instaList)
    ListView instaList;
    Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_insta, container, false);
        unbinder = ButterKnife.bind(this, view);
        if(SharedPrefUtils.getToken(getActivity())!=null &&!SharedPrefUtils.getToken(getActivity()).isEmpty())
        getInstaData();
        else
            startActivity(new Intent(getActivity(), InstagramActivity.class));
        return view;
    }

    private void getInstaData() {
        ApiHelper helper = new ApiHelper();
        helper.getInstagramData(SharedPrefUtils.getToken(getActivity())).enqueue(new Callback<InstagramMediaResponse>() {
            @Override
            public void onResponse(Call<InstagramMediaResponse> call, Response<InstagramMediaResponse> response) {
                InstagramMediaResponse resp = response.body();
                InstaDataItem data = resp.getData().get(0);

                InstaAdapter adapter=new InstaAdapter(getActivity(),resp.getData());
                instaList.setAdapter(adapter);
                System.out.println(response.body());
            }

            @Override
            public void onFailure(Call<InstagramMediaResponse> call, Throwable t) {
                System.out.println(t);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}