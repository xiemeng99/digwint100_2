package digiwin.smartdepott100.main.voicer;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import digiwin.smartdepott100.R;


/**
 * 声音选择适配器
 * @Author 毛衡
 * Created by Administrator on 2017/1/11 0011.
 */

public class VoicerChooseAdapter extends RecyclerView.Adapter<VoicerChooseAdapter.OCViewHolder> {

    private final String TAG = "StorageAdapter";
    private List<String> list;
    private Context context;
    private LayoutInflater inflater;
    private VoicerChooseOnItemClickListener listener;

    public VoicerChooseAdapter(List<String> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public interface VoicerChooseOnItemClickListener{
        public void onClick(View view, int position);
    }
    public void setClick(VoicerChooseOnItemClickListener click) {
        this.listener = click;
    }
    @Override
    public OCViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.ryitem_dialog_entidcompany, null);
        return new OCViewHolder(view);
    }

    @SuppressWarnings("ResourceType")
    @Override
    public void onBindViewHolder(OCViewHolder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener!=null){
                    listener.onClick(view,position);
                }else {
                }
            }
        });
        TypedArray typedArray = context.obtainStyledAttributes(new int[]{R.attr.Base_color,R.attr.text_click_bg});
        if (position == 0){
            holder.tv_item_operatingCenter.setTextColor(Color.WHITE);
            holder.itemView.setBackgroundColor(typedArray.getColor(0,context.getResources().getColor(R.color.Base_color)));
        }else {
            holder.tv_item_operatingCenter.setTextColor(context.getResources().getColor(R.color.black_32));
            holder.itemView.setBackgroundDrawable(typedArray.getDrawable(1));
        }
        if(list.get(position)!=null){
            holder.tv_item_operatingCenter.setText(list.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    /**
     * 声音选择
     * RecyclerView.ViewHolder
     * @Author 毛衡
     */
    class OCViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tv_item_operatingCenter)
        public TextView tv_item_operatingCenter;
        public OCViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
