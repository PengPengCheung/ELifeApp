package com.pengpeng.elifelistenapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.pengpeng.elifelistenapp.ELifeModel.Audio;
import com.pengpeng.elifelistenapp.ELifePresenter.AudioListPresenter;
import com.pengpeng.elifelistenapp.ELifePresenter.AudioListPresenterImpl;
import com.pengpeng.elifelistenapp.ELifeView.AudioListIView;
import com.pengpeng.elifelistenapp.utils.Resource;

import java.util.ArrayList;
import java.util.List;


public class ItemFragment extends Fragment implements AudioListIView, SwipeRefreshLayout.OnRefreshListener {

    // TODO: Customize parameters
    private int mType = Resource.Type.TYPE_MOVIE;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private SwipeRefreshLayout mSwipeRefreshWidget;
    private RecyclerViewAdapter mAdapter;
    private List<Audio> mData;

    private AudioListPresenter mPresenter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ItemFragment newInstance(int type) {
        ItemFragment fragment = new ItemFragment();
        Bundle args = new Bundle();
        args.putInt("type", type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mType = getArguments().getInt("type");
        }

        mPresenter = new AudioListPresenterImpl(this);
        if(mAdapter==null){
            mAdapter = new RecyclerViewAdapter(getActivity().getApplicationContext());
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        mSwipeRefreshWidget = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_widget);
//        mSwipeRefreshWidget.setColorSchemeResources(R.color.primary,
//                R.color.primary_dark, R.color.primary_light,
//                R.color.accent);
        mSwipeRefreshWidget.setOnRefreshListener(this);//实现onRefresh方法，进行刷新

        mRecyclerView = (RecyclerView)view.findViewById(R.id.recycle_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);//为RecyclerView指定布局管理对象


        mRecyclerView.setItemAnimator(new DefaultItemAnimator());//设置动画

        mRecyclerView.addItemDecoration(new DividerItemDecoration(
                getActivity(), DividerItemDecoration.VERTICAL_LIST));

        mAdapter.setOnItemClickListener(mOnItemClickListener);
        mRecyclerView.setAdapter(mAdapter);

        //设置上拉加载的监听器
        mRecyclerView.addOnScrollListener(mOnScrollListener);
        onRefresh();


        return view;
    }

    private RecyclerViewAdapter.OnItemClickListener mOnItemClickListener = new RecyclerViewAdapter.OnItemClickListener(){

        @Override
        public void onItemClick(View view, int position) {
            ((MainActivity)getActivity()).setPlayingAudio(mData.get(position), position);
        }
    };

    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {

        private int lastVisibleItem;

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == RecyclerView.SCROLL_STATE_IDLE
                    && lastVisibleItem + 1 == mAdapter.getItemCount()
                    && mAdapter.isShowFooter()) {
                //加载更多
                Log.d(Resource.Debug.TAG, "loading more data");
                mPresenter.loadAudioList("123", mType);
            }
        }
    };


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnListFragmentInteractionListener) {
//            mListener = (OnListFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnListFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        mListener = null;
    }

    @Override
    public void onRefresh() {

//        if(mData !=null){
//            mData.clear();
//        }
        mPresenter.loadAudioList("123", mType);

    }

    @Override
    public void showProgress() {
        mSwipeRefreshWidget.setRefreshing(true);
    }

    @Override
    public void addAudio(List<Audio> audioList) {

        mAdapter.setIsShowFooter(true);

        if(mData == null){
            mData = new ArrayList<>();
        }

        Log.i(Resource.Debug.TAG, "before add audio");
        mData.addAll(audioList);
        ((ELifeApplication)(getActivity().getApplication())).getManager().addAll(audioList);

        Log.i(Resource.Debug.TAG, "before new Adapter");
        if(mAdapter==null){
            mAdapter = new RecyclerViewAdapter(getActivity().getApplicationContext());
        }

        Log.i(Resource.Debug.TAG, "before add data");

        if(mAdapter.getItemCount() == 1){
            Log.i(Resource.Debug.TAG, "add data");

            //当item只有1项时，此时已经绑定了数据，mData这个对象引用指向了列表数据集，所以前面的addAll()方法加进了数据集中，后面就不需要再append了，只需要通知数据集发生改变即可
            mAdapter.setmData(mData);
        }else{
            //如果没有更多数据了,则隐藏footer布局
            if(audioList == null || audioList.size() == 0){
                mAdapter.setIsShowFooter(false);
            }
            Log.i(Resource.Debug.TAG, "append data");
            mAdapter.notifyDataSetChanged();
//            mAdapter.appendData(audioList);
        }
    }

    @Override
    public void hideProgress() {
        mSwipeRefreshWidget.setRefreshing(false);

    }

    @Override
    public void showLoadFailMsg() {
        Toast.makeText(getActivity(), "load Fail", Toast.LENGTH_SHORT).show();
    }
}
