package com.lingmiao.distribution.dialog;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lingmiao.distribution.R;
import com.lingmiao.distribution.util.GlideUtil;

import uk.co.senab.photoview.PhotoView;

public class BigImageFragment extends Fragment {


    private static final String IMAGE_URL = "image";
    PhotoView image;
    private String imageUrl;
    private static DialogCallBack mCallBack;

    public static BigImageFragment newInstance(String param1, DialogCallBack callBack) {
        mCallBack = callBack;
        BigImageFragment fragment = new BigImageFragment();
        Bundle args = new Bundle();
        args.putString(IMAGE_URL, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            imageUrl = getArguments().getString(IMAGE_URL);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image, container, false);
        image = view.findViewById(R.id.image);
        GlideUtil.load(getActivity(), imageUrl, image, GlideUtil.getOption());
        image.setOnPhotoTapListener((view1, x, y) -> mCallBack.imageCallBack());
        return view;
    }

    public interface DialogCallBack {
        void imageCallBack();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}