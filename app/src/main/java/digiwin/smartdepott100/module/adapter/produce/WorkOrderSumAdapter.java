package digiwin.smartdepott100.module.adapter.produce;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.View;

import java.util.List;

import digiwin.library.utils.StringUtils;
import digiwin.pulltorefreshlibrary.recyclerviewAdapter.BaseRecyclerAdapter;
import digiwin.pulltorefreshlibrary.recyclerviewAdapter.RecyclerViewHolder;
import digiwin.smartdepott100.R;
import digiwin.smartdepott100.module.activity.produce.workorder.WorkOrderActivity;
import digiwin.smartdepott100.module.bean.common.ListSumBean;

/**
 * @author songjie
 * @module 依工单发料 汇总数据
 * @date 2017/3/10
 */

public class WorkOrderSumAdapter extends BaseRecyclerAdapter<ListSumBean>{

    public WorkOrderSumAdapter(Context ctx, List<ListSumBean> list) {
        super(ctx, list);
    }

    @Override
    protected int getItemLayout(int viewType) {
        return R.layout.ryitem_workorder;
    }

    @SuppressWarnings("ResourceType")
    @Override
    protected void bindData(RecyclerViewHolder holder, int position, final ListSumBean item) {
       //申请、库存、匹配
        final float numb1 = StringUtils.string2Float(item.getApply_qty());
        final float numb2 = StringUtils.string2Float(item.getStock_qty());
        final float numb3 = StringUtils.string2Float(item.getScan_sumqty());

        holder.setText(R.id.tv_item_name, item.getLow_order_item_name());
        holder.setText(R.id.tv_unit,item.getUnit_no());
        holder.setText(R.id.tv_item_format, item.getLow_order_item_spec());
        holder.setText(R.id.tv_item_no, item.getLow_order_item_no());
        holder.setText(R.id.tv_product_code, item.getProduct_no());
        holder.setText(R.id.tv_match_number, StringUtils.deleteZero(item.getStock_qty()));
        holder.setText(R.id.tv_apply_number, StringUtils.deleteZero(item.getApply_qty()));
        holder.setText(R.id.tv_locator_num, StringUtils.deleteZero(item.getScan_sumqty()));

        //跳转到明细
        holder.setClickListener(R.id.img_detail, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListSumBean bean = new ListSumBean();
                bean.setItem_no(item.getLow_order_item_no());
                bean.setItem_name(item.getLow_order_item_name());
                bean.setAvailable_in_qty(item.getApply_qty());
                WorkOrderActivity activity = (WorkOrderActivity) mContext;
                activity.ToDetailAct(bean);
            }
        });

        TypedArray a = mContext.obtainStyledAttributes(new int[]{R.attr.sumColor_1, R.attr.sumColor_2, R.attr.sumColor_3});

        if (numb3 == 0) {
            holder.setBackground(R.id.item_ll,R.drawable.red_scandetail_bg);
            holder.setTextColor(R.id.tv_item_no, a.getColor(0,mContext.getResources().getColor(R.color.Base_color)));
            holder.setTextColor(R.id.tv_unit, a.getColor(0,mContext.getResources().getColor(R.color.Base_color)));
            holder.setTextColor(R.id.tv_item_name, a.getColor(0,mContext.getResources().getColor(R.color.Base_color)));
            holder.setTextColor(R.id.tv_item_format, a.getColor(0,mContext.getResources().getColor(R.color.Base_color)));
            holder.setTextColor(R.id.tv_locator_num, a.getColor(0,mContext.getResources().getColor(R.color.Base_color)));
            holder.setTextColor(R.id.tv_apply_number, a.getColor(0,mContext.getResources().getColor(R.color.Base_color)));
            holder.setTextColor(R.id.tv_match_number, a.getColor(0,mContext.getResources().getColor(R.color.Base_color)));
            holder.setTextColor(R.id.tv_product_code, a.getColor(0,mContext.getResources().getColor(R.color.Base_color)));

        } else if (numb1 > numb3) {
            holder.setBackground(R.id.item_ll,R.drawable.yellow_scandetail_bg);
            holder.setTextColor(R.id.tv_item_no,a.getColor(1,mContext.getResources().getColor(R.color.outside_yellow)));
            holder.setTextColor(R.id.tv_unit,a.getColor(1,mContext.getResources().getColor(R.color.outside_yellow)));
            holder.setTextColor(R.id.tv_item_name,a.getColor(1,mContext.getResources().getColor(R.color.outside_yellow)));
            holder.setTextColor(R.id.tv_locator_num,a.getColor(1,mContext.getResources().getColor(R.color.outside_yellow)));
            holder.setTextColor(R.id.tv_item_format, a.getColor(1,mContext.getResources().getColor(R.color.outside_yellow)));
            holder.setTextColor(R.id.tv_apply_number,a.getColor(1,mContext.getResources().getColor(R.color.outside_yellow)));
            holder.setTextColor(R.id.tv_match_number,a.getColor(1,mContext.getResources().getColor(R.color.outside_yellow)));
            holder.setTextColor(R.id.tv_product_code,a.getColor(1,mContext.getResources().getColor(R.color.outside_yellow)));
        } else if (numb1 ==numb3) {
            holder.setBackground(R.id.item_ll,R.drawable.green_scandetail_bg);
            holder.setTextColor(R.id.tv_item_no,a.getColor(2,mContext.getResources().getColor(R.color.green1b)));
            holder.setTextColor(R.id.tv_unit,a.getColor(2,mContext.getResources().getColor(R.color.green1b)));
            holder.setTextColor(R.id.tv_item_name,a.getColor(2,mContext.getResources().getColor(R.color.green1b)));
            holder.setTextColor(R.id.tv_item_format, a.getColor(2,mContext.getResources().getColor(R.color.green1b)));
            holder.setTextColor(R.id.tv_locator_num, a.getColor(2,mContext.getResources().getColor(R.color.green1b)));
            holder.setTextColor(R.id.tv_apply_number,a.getColor(2,mContext.getResources().getColor(R.color.green1b)));
            holder.setTextColor(R.id.tv_match_number,a.getColor(2,mContext.getResources().getColor(R.color.green1b)));
            holder.setTextColor(R.id.tv_product_code,a.getColor(2,mContext.getResources().getColor(R.color.green1b)));
        }
    }
}
