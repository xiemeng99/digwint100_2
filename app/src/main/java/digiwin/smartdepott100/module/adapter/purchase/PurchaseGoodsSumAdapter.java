package digiwin.smartdepott100.module.adapter.purchase;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import java.util.List;

import digiwin.library.utils.StringUtils;
import digiwin.pulltorefreshlibrary.recyclerviewAdapter.BaseRecyclerAdapter;
import digiwin.pulltorefreshlibrary.recyclerviewAdapter.RecyclerViewHolder;
import digiwin.smartdepott100.R;
import digiwin.smartdepott100.module.bean.common.ListSumBean;

/**
 * @author 唐孟宇
 * @des 采购收货扫描 汇总adapter
 */

public class PurchaseGoodsSumAdapter extends BaseRecyclerAdapter<ListSumBean> {
    float numb2;

    public PurchaseGoodsSumAdapter(Context ctx, List<ListSumBean> list) {
        super(ctx, list);
    }

    @Override
    protected int getItemLayout(int viewType) {
        return R.layout.ryitem_purchase_goods_sum;
    }

    @Override
    protected void bindData(final RecyclerViewHolder holder, int position, final ListSumBean item) {
        final float numb1 = StringUtils.string2Float(item.getApply_qty());
        numb2 = StringUtils.string2Float(item.getScan_sumqty());

        holder.setText(R.id.tv_item_seq,item.getSeq());
        holder.setText(R.id.tv_item_no, item.getItem_no());
        holder.setText(R.id.tv_item_danwei, item.getUnit_no());
        holder.setText(R.id.tv_item_name, item.getItem_name());
        holder.setText(R.id.tv_item_model, item.getItem_spec());
        holder.setText(R.id.tv_product_code, item.getProduct_no());
        holder.setText(R.id.tv_in_storage_number, StringUtils.deleteZero(String.valueOf(numb1)));
        holder.setText(R.id.tv_match_number, StringUtils.deleteZero(String.valueOf(numb2)));

        final EditText etNum = (EditText) holder.getView(R.id.tv_match_number);
        etNum.setTag(position);
        etNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (etNum.hasFocus()) {
                    numb2 = StringUtils.string2Float(s.toString());
                    changeColor(holder, numb1, numb2);
                    int pos= (int) etNum.getTag();
                    mItems.get(pos).setScan_sumqty(s.toString());
                }

            }
        });
        changeColor(holder, numb1, numb2);
    }
    @SuppressWarnings("ResourceType")
    private void changeColor(RecyclerViewHolder holder, float numb1, float numb2) {
        TypedArray a = mContext.obtainStyledAttributes(new int[]{R.attr.sumColor_1, R.attr.sumColor_2, R.attr.sumColor_3});
        if (numb2 == 0) {
            holder.setBackground(R.id.item_ll, R.drawable.red_scandetail_bg);
            holder.setTextColor(R.id.tv_item_seq, a.getColor(0, mContext.getResources().getColor(R.color.Base_color)));
            holder.setTextColor(R.id.tv_item_no, a.getColor(0, mContext.getResources().getColor(R.color.Base_color)));
            holder.setTextColor(R.id.tv_item_danwei, a.getColor(0, mContext.getResources().getColor(R.color.Base_color)));
            holder.setTextColor(R.id.tv_item_name, a.getColor(0, mContext.getResources().getColor(R.color.Base_color)));
            holder.setTextColor(R.id.tv_item_model, a.getColor(0, mContext.getResources().getColor(R.color.Base_color)));
            holder.setTextColor(R.id.tv_product_code, a.getColor(0, mContext.getResources().getColor(R.color.Base_color)));
            holder.setTextColor(R.id.tv_match_number, a.getColor(0, mContext.getResources().getColor(R.color.Base_color)));
            holder.setTextColor(R.id.tv_in_storage_number, a.getColor(0, mContext.getResources().getColor(R.color.Base_color)));
        } else if (numb1 > numb2) {
            holder.setBackground(R.id.item_ll, R.drawable.yellow_scandetail_bg);
            holder.setTextColor(R.id.tv_item_seq, a.getColor(1, mContext.getResources().getColor(R.color.outside_yellow)));
            holder.setTextColor(R.id.tv_item_no, a.getColor(1, mContext.getResources().getColor(R.color.outside_yellow)));
            holder.setTextColor(R.id.tv_item_danwei, a.getColor(1, mContext.getResources().getColor(R.color.outside_yellow)));
            holder.setTextColor(R.id.tv_item_name, a.getColor(1, mContext.getResources().getColor(R.color.outside_yellow)));
            holder.setTextColor(R.id.tv_item_model, a.getColor(1, mContext.getResources().getColor(R.color.outside_yellow)));
            holder.setTextColor(R.id.tv_product_code, a.getColor(1, mContext.getResources().getColor(R.color.outside_yellow)));
            holder.setTextColor(R.id.tv_match_number, a.getColor(1, mContext.getResources().getColor(R.color.outside_yellow)));
            holder.setTextColor(R.id.tv_in_storage_number, a.getColor(1, mContext.getResources().getColor(R.color.outside_yellow)));
        } else if (numb1 == numb2) {
            holder.setBackground(R.id.item_ll, R.drawable.green_scandetail_bg);
            holder.setTextColor(R.id.tv_item_seq, a.getColor(2, mContext.getResources().getColor(R.color.green1b)));
            holder.setTextColor(R.id.tv_item_no, a.getColor(2, mContext.getResources().getColor(R.color.green1b)));
            holder.setTextColor(R.id.tv_item_danwei, a.getColor(2, mContext.getResources().getColor(R.color.green1b)));
            holder.setTextColor(R.id.tv_item_name, a.getColor(2, mContext.getResources().getColor(R.color.green1b)));
            holder.setTextColor(R.id.tv_item_model, a.getColor(2, mContext.getResources().getColor(R.color.green1b)));
            holder.setTextColor(R.id.tv_product_code, a.getColor(2, mContext.getResources().getColor(R.color.green1b)));
            holder.setTextColor(R.id.tv_match_number, a.getColor(2, mContext.getResources().getColor(R.color.green1b)));
            holder.setTextColor(R.id.tv_in_storage_number, a.getColor(2, mContext.getResources().getColor(R.color.green1b)));
        }
    }
}
