package com.pengpeng.elifelistenapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pengpeng.elifelistenapp.ELifeModel.Audio;
import com.pengpeng.elifelistenapp.utils.ImageLoaderUtils;

import java.util.List;

/**
 * Created by pengpeng on 16-3-27.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;
    private Context mContext;
    private List<Audio> mData;
    private boolean mShowFooter = true;
    private OnItemClickListener mOnItemClickListener;

    public RecyclerViewAdapter(Context context) {
        this.mContext = context;
    }

    public void setmData(List<Audio> data) {
        this.mData = data;
        this.notifyDataSetChanged();
    }


    public void setIsShowFooter(boolean showFooter){
        this.mShowFooter = showFooter;
    }

    public boolean isShowFooter(){
        return this.mShowFooter;
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mOnItemClickListener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        if (!mShowFooter) {
            return TYPE_ITEM;
        }
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if(viewType == TYPE_ITEM){
            View v = LayoutInflater.from(mContext).inflate(R.layout.fragment_item_list_audio, null);
            ItemViewHolder viewHolder = new ItemViewHolder(v);
            return viewHolder;
        }else{
            View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_item_list_footer, null);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            return new FooterViewHolder(view);
        }


    }


    /**
     * 所谓绑定viewholder实际上就是将data中的数据取出并设置到对应的控件上
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ItemViewHolder){//因为这个类中存在FooterViewHolder 和 ItemViewHolder，所以要判断这个holder所属的类是哪个
            Audio audio = mData.get(position);
            if(audio == null){
                return;
            }
            ((ItemViewHolder) holder).tvTitle.setText(audio.getAudioTitle());
            ((ItemViewHolder)holder).tvDesc.setText(audio.getAudioDate());
            ImageLoaderUtils.display(mContext, ((ItemViewHolder)holder).ivAudioImg, audio.getAudioImageUrl());

        }
    }


    /**
     * 因为加上了脚部Footer，所以此处的操作不能像ListView那样直接返回data.size()
     * 要做一些判断，以判断返回的是Footer的布局还是Item的布局
     * @return
     */
    @Override
    public int getItemCount() {
        int type;
        if(mShowFooter){
            type = TYPE_FOOTER;
        }else{
            type = TYPE_ITEM;
        }

        if(mData == null){
            return type;
        }
        return mData.size() + type;
    }


    public class FooterViewHolder extends RecyclerView.ViewHolder {

        public FooterViewHolder(View view) {
            super(view);
        }

    }


    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView tvTitle;
        public TextView tvDesc;
        public ImageView ivAudioImg;


        public ItemViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_audio_title);
            tvDesc = (TextView) itemView.findViewById(R.id.tv_audio_desc);
            ivAudioImg = (ImageView) itemView.findViewById(R.id.iv_audio_img);
            itemView.setOnClickListener(this);
        }

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            Toast.makeText(mContext, "click", Toast.LENGTH_SHORT).show();
            //之所以不像ListView那样设置onItemClickListener监听器，是因为recyclerview不提供这样的接口。。
            //所以只能用这样的方法模拟实现onItemClick接口方法了
            if(mOnItemClickListener!=null){
                mOnItemClickListener.onItemClick(v, this.getPosition());
            }
        }


    }
    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }
}
