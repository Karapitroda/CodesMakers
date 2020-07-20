package com.app.codesmakers.ui.onboarding.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.app.codesmakers.R;
import com.app.codesmakers.api.pojo.config.HowToUse;
import com.app.codesmakers.databinding.FragmentLayoutOnboardingBinding;
import com.app.codesmakers.ui.base.BaseFragment;
import com.app.codesmakers.utils.session.SessionManager;
import com.squareup.picasso.Picasso;

/**
 * Created by nmillward on 7/12/16.
 */
public class BoardingFragment extends BaseFragment {

    private static final String PAGE = "page";
    private int page;
    FragmentLayoutOnboardingBinding binding;

    MutableLiveData<HowToUse> howtoUse = new MutableLiveData<>();

    public static BoardingFragment newInstance(int page) {
        BoardingFragment fragment = new BoardingFragment();
        Bundle b = new Bundle();
        b.putInt(PAGE, page);
        fragment.setArguments(b);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt(PAGE);
        howtoUse.postValue(SessionManager.getInstance().getAppHowToUseConfigurations().get(page));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_layout_onboarding, container, false);
        binding = FragmentLayoutOnboardingBinding.bind(root);
        binding.setLifecycleOwner(this);
        root.setTag(page);
        setupData();

        howtoUse.observe(getActivity(), howToUse -> {
            binding.titleTv.setText(howtoUse.getValue().getText());
            String url = howtoUse.getValue().getImg();
            Picasso.get().load(url).into(binding.logoImageview);
        });

        //ViewColorsUtils.changeTextColorHeading(binding.titleTv);
        //ViewColorsUtils.changeTextColorSubHeading(binding.subTitleTv);
        return root;
    }

    private void setupData() {

    }


}
