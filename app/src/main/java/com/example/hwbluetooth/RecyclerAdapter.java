package com.example.hwbluetooth;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    interface onButtonClickHandler {
        // 提供onItemClick方法作為點擊事件，括號內為接受的參數
        void onButtonClick(String key,BLEdevice blEdevice);
        // 提供onItemRemove做為移除項目的事件
    }

    private HashMap<String, BLEdevice> hashMap;
    private onButtonClickHandler mButtonClickHandler;


    public RecyclerAdapter(HashMap<String, BLEdevice> hashMap, onButtonClickHandler mButtonClickHandler) {

        this.hashMap= hashMap;
        this.mButtonClickHandler = mButtonClickHandler;

    }




    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.datalist;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view1 = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        ViewHolder viewHolder = new ViewHolder(view1);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.getDevicemac().setOnClickListener(holder);
        String key=getKeyInHashMap(hashMap,position);
        BLEdevice value=getValueInHashMap(hashMap,position);
        holder.getDevicemac().setText(key);
        holder.getDeviceRssi().setText(value.getRSSI());
    }




    @Override
    public int getItemCount() {
        return 0;
    }

    private String getKeyInHashMap(HashMap<String, BLEdevice> hashMap, int position) {
        int index=0;
        for(Map.Entry<String,BLEdevice>BTH:hashMap.entrySet()){
            if(index==position){
                return BTH.getKey();
            }
            index++;
        }
        return null;
    }

    private BLEdevice getValueInHashMap(HashMap<String, BLEdevice> hashMap, int position) {
        int index=0;
        for(Map.Entry<String,BLEdevice>BTH:hashMap.entrySet()){
            if(index==position) {return BTH.getValue();}
            index++;
        }
        return null;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView deviceNameView;
        public TextView RssiView;
        public TextView contentView;
        public Button DataButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            deviceNameView=(TextView)itemView.findViewById(R.id.mac1);
            RssiView=(TextView)itemView.findViewById(R.id.rssi1);
            contentView=(TextView)itemView.findViewById(R.id.content);
            DataButton =(Button) itemView.findViewById(R.id.data);

        }

        @Override
        public void onClick(View v) {
            int wrapperPosition=getAdapterPosition();
            String key=getKeyInHashMap(hashMap,wrapperPosition);
            BLEdevice value=getValueInHashMap(hashMap,wrapperPosition);
            mButtonClickHandler.onButtonClick(key,value);
        }
        private TextView  getDevicemac() {
            return deviceNameView;
        }

        private TextView getDeviceRssi() {
            return RssiView;

        }

        private Button getBtn_detail() {
            return DataButton ;
        }
    }



}





