package digiwin.smartdepott100.module.fragment.produce.materialreturn;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import digiwin.library.dialog.OnDialogClickListener;
import digiwin.library.dialog.OnDialogTwoListener;
import digiwin.library.utils.ActivityManagerUtils;
import digiwin.library.utils.LogUtils;
import digiwin.pulltorefreshlibrary.recyclerviewAdapter.BaseRecyclerAdapter;
import digiwin.pulltorefreshlibrary.recyclerviewAdapter.OnItemClickListener;
import digiwin.smartdepott100.R;
import digiwin.smartdepott100.core.appcontants.AddressContants;
import digiwin.smartdepott100.core.base.BaseFragment;
import digiwin.smartdepott100.login.loginlogic.LoginLogic;
import digiwin.smartdepott100.module.activity.common.CommonDetailActivity;
import digiwin.smartdepott100.module.activity.produce.materialreturn.MaterialReturnActivity;
import digiwin.smartdepott100.module.adapter.produce.MaterialReturnSumAdapter;
import digiwin.smartdepott100.module.bean.common.ClickItemPutBean;
import digiwin.smartdepott100.module.bean.common.DetailShowBean;
import digiwin.smartdepott100.module.bean.common.FilterResultOrderBean;
import digiwin.smartdepott100.module.bean.common.ListSumBean;
import digiwin.smartdepott100.module.logic.common.CommonLogic;
import digiwin.smartdepott100.module.logic.produce.materialreturn.MaterialReturnLogic;


/**
 * @author xiemeng
 * @des 退料过账汇总页面
 * @date 2017/3/30
 */
public class MaterialReturnSumFg extends BaseFragment {
    @BindView(R.id.tv_material_return_number)
    TextView tvMaterialReturnNumber;
    @BindView(R.id.tv_item_date)
    TextView tvItemDate;
    @BindView(R.id.ry_list)
    RecyclerView ryList;
    @BindView(R.id.tv_depart)
    TextView tvDepart;
    @BindView(R.id.tv_applicant)
    TextView tvApplicant;

    @OnClick(R.id.commit)
    void commit() {
        showCommitSureDialog(new OnDialogTwoListener() {
            @Override
            public void onCallback1() {
                sureCommit();
            }

            @Override
            public void onCallback2() {

            }
        });
    }


    MaterialReturnActivity pactivity;
    MaterialReturnLogic commonLogic;

    boolean upDateFlag;

    List<ListSumBean> sumBeanList;
    private BaseRecyclerAdapter adapter;

    @Override
    protected int bindLayoutId() {
        return R.layout.fg_material_return_sum;
    }

    @Override
    protected void doBusiness() {
        upDateFlag = false;
        sumBeanList = new ArrayList<>();
        pactivity = (MaterialReturnActivity) this.activity;
        commonLogic = MaterialReturnLogic.getInstance(context, pactivity.module, pactivity.mTimestamp.toString());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(pactivity);
        ryList.setLayoutManager(linearLayoutManager);
    }

    public void upDateList() {
        try {
            sumBeanList.clear();
            adapter = new MaterialReturnSumAdapter(context, sumBeanList);
            ryList.setAdapter(adapter);
            Bundle bundle = activity.getIntent().getExtras();
            FilterResultOrderBean filterBean = (FilterResultOrderBean) bundle.getSerializable(MaterialReturnActivity.filterBean);
            ClickItemPutBean itemPutBean = new ClickItemPutBean();
            itemPutBean.setDoc_no(filterBean.getDoc_no());
            itemPutBean.setWarehouse_no(LoginLogic.getWare());
            showLoadingDialog();
            commonLogic.getMRSumData(itemPutBean, new MaterialReturnLogic.GetZSumListener() {
                @Override
                public void onSuccess(List<ListSumBean> list) {
                    dismissLoadingDialog();
                    sumBeanList = list;
                    adapter = new MaterialReturnSumAdapter(context, sumBeanList);
                    ryList.setAdapter(adapter);
                    if (null != list && list.size() > 0) {
                        upDateFlag = true;
                        toDetail();
                        tvMaterialReturnNumber.setText(list.get(0).getDoc_no());
                        tvItemDate.setText(list.get(0).getCreate_date());
                        tvDepart.setText(list.get(0).getDepartment_name());
                        tvApplicant.setText(list.get(0).getEmployee_name());

                    }
                }

                @Override
                public void onFailed(String error) {
                    dismissLoadingDialog();
                    upDateFlag = false;
                    sumBeanList.clear();
                    adapter = new MaterialReturnSumAdapter(context, sumBeanList);
                    ryList.setAdapter(adapter);
                    showFailedDialog(error);
                }
            });
        }catch (Exception e){
            LogUtils.e(TAG,"updatelist"+e);
        }


    }

    /**
     * 查看单笔料明细
     */
    public void toDetail() {
        try {
            adapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(View itemView, int pos) {
                    getDetail(sumBeanList.get(pos));
                }
            });
        } catch (Exception e) {
            LogUtils.e(TAG, "toDetail-->" + e);
        }
    }


    /**
     * 查看明细
     */
    private void getDetail(final ListSumBean orderSumData) {
        showLoadingDialog();
        orderSumData.setAvailable_in_qty(orderSumData.getApply_qty());
        commonLogic.getDetail(orderSumData, new CommonLogic.GetDetailListener() {
            @Override
            public void onSuccess(List<DetailShowBean> detailShowBeen) {
                Bundle bundle = new Bundle();
                bundle.putString(AddressContants.MODULEID_INTENT, pactivity.mTimestamp.toString());
                bundle.putString(CommonDetailActivity.MODULECODE, pactivity.module);
                bundle.putSerializable(CommonDetailActivity.ONESUM, orderSumData);
                bundle.putSerializable(CommonDetailActivity.DETAIL, (Serializable) detailShowBeen);
                dismissLoadingDialog();
                ActivityManagerUtils.startActivityBundleForResult(activity, CommonDetailActivity.class, bundle, pactivity.DETAILCODE);
            }

            @Override
            public void onFailed(String error) {
                dismissLoadingDialog();
                showFailedDialog(error);
            }
        });
    }

    private void sureCommit() {
        if (!upDateFlag) {
            showFailedDialog(R.string.nodate);
            return;
        }
        showLoadingDialog();
        HashMap<String, String> map = new HashMap<>();
        commonLogic.commitMRData(map, new CommonLogic.CommitListener() {
            @Override
            public void onSuccess(String msg) {
                dismissLoadingDialog();
                showCommitSuccessDialog(msg, new OnDialogClickListener() {
                    @Override
                    public void onCallback() {
                        pactivity.scanFg.initData();
                        pactivity.finish();
                    }
                });
            }

            @Override
            public void onFailed(String error) {
                dismissLoadingDialog();
                showCommitFailDialog(error);
            }
        });

    }


}
