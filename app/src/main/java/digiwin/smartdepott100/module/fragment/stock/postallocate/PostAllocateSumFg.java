package digiwin.smartdepott100.module.fragment.stock.postallocate;


import android.os.Bundle;
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
import digiwin.pulltorefreshlibrary.recyclerview.FullyLinearLayoutManager;
import digiwin.pulltorefreshlibrary.recyclerviewAdapter.OnItemClickListener;
import digiwin.smartdepott100.R;
import digiwin.smartdepott100.core.appcontants.AddressContants;
import digiwin.smartdepott100.core.base.BaseFragment;
import digiwin.smartdepott100.login.loginlogic.LoginLogic;
import digiwin.smartdepott100.module.activity.common.CommonDetailActivity;
import digiwin.smartdepott100.module.activity.stock.postallocate.PostAllocateScanActivity;
import digiwin.smartdepott100.module.adapter.stock.postallocate.PostAllocateSumAdapter;
import digiwin.smartdepott100.module.bean.common.ClickItemPutBean;
import digiwin.smartdepott100.module.bean.common.DetailShowBean;
import digiwin.smartdepott100.module.bean.common.FilterResultOrderBean;
import digiwin.smartdepott100.module.bean.common.ListSumBean;
import digiwin.smartdepott100.module.logic.common.CommonLogic;
import digiwin.smartdepott100.module.logic.stock.PostAllocateLogic;


/**
 * @author 唐孟宇
 * @des 调拨过账 数据汇总界面
 */
public class PostAllocateSumFg extends BaseFragment {
    @BindView(R.id.ry_list)
    RecyclerView ryList;

    /**
     * 单号
     */
    @BindView(R.id.tv_transfers_list_no)
    TextView tv_transfers_list_no;
    /**
     * 日期
     */
    @BindView(R.id.tv_head_plan_date)
    TextView tv_head_plan_date;
    /**
     * 部门
     */
    @BindView(R.id.tv_department)
    TextView tv_department;
    /**
     * 申请人
     */
    @BindView(R.id.tv_applicant)
    TextView tv_applicant;

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

    PostAllocateScanActivity pactivity;

    PostAllocateLogic commonLogic;

    private boolean upDateFlag;

    PostAllocateSumAdapter adapter;

    List<ListSumBean> sumShowBeanList;

    FilterResultOrderBean orderData;

//    private String doc_stus;
    @Override
    protected int bindLayoutId() {
        return R.layout.fg_post_allocate_sum;
    }

    @Override
    protected void doBusiness() {
        pactivity = (PostAllocateScanActivity) activity;
        commonLogic = PostAllocateLogic.getInstance(pactivity, pactivity.module, pactivity.mTimestamp.toString());
        FullyLinearLayoutManager linearLayoutManager = new FullyLinearLayoutManager(activity);
        ryList.setLayoutManager(linearLayoutManager);
        upDateFlag = false;
        Bundle bundle = getActivity().getIntent().getExtras();
        orderData = (FilterResultOrderBean) bundle.getSerializable(AddressContants.ORDERDATA);
        tv_transfers_list_no.setText(orderData.getDoc_no());
        tv_head_plan_date.setText(orderData.getCreate_date());
        tv_applicant.setText(orderData.getEmployee_name());
        tv_department.setText(orderData.getDepartment_name());
//        doc_stus=bundle.getString(AddressContants.DOC_NO);
    }

    /**
     * 汇总展示
     */
    public void upDateList() {
        sumShowBeanList = new ArrayList<ListSumBean>();
        adapter = new PostAllocateSumAdapter(pactivity, sumShowBeanList);
        ryList.setAdapter(adapter);
        try {
            ClickItemPutBean clickItemPutData = new ClickItemPutBean();
            clickItemPutData.setDoc_no(orderData.getDoc_no());
            clickItemPutData.setWarehouse_no(LoginLogic.getWare());
            showLoadingDialog();
            commonLogic.getPostAllocateSum(clickItemPutData, new CommonLogic.GetZSumListener() {
                @Override
                public void onSuccess(List<ListSumBean> list) {
                    dismissLoadingDialog();
                    sumShowBeanList = list;
                    if(list.size()>0){
                        adapter = new PostAllocateSumAdapter(pactivity, sumShowBeanList);
                        ryList.setAdapter(adapter);
                        upDateFlag = true;
                        toDetail();
                    }
                }

                @Override
                public void onFailed(String error) {
                   dismissLoadingDialog();
                    upDateFlag = false;
                    try {
                        showFailedDialog(error);
                        sumShowBeanList = new ArrayList<ListSumBean>();
                        adapter = new PostAllocateSumAdapter(pactivity, sumShowBeanList);
                        ryList.setAdapter(adapter);
                    } catch (Exception e) {
                        LogUtils.e(TAG, "updateList--getSum--onFailed" + e);
                    }
                }
            });
        } catch (Exception e) {
            LogUtils.e(TAG, "updateList--getSum--Exception" + e);
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
                    getDetail(sumShowBeanList.get(pos));
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
                ActivityManagerUtils.startActivityBundleForResult(activity, CommonDetailActivity.class, bundle, pactivity.DETAILCODE);
                dismissLoadingDialog();
            }

            @Override
            public void onFailed(String error) {
                dismissLoadingDialog();
                showFailedDialog(error);
            }
        });
    }

    private void sureCommit(){
        if (!upDateFlag) {
            showFailedDialog(R.string.nodate);
            return;
        }
        showLoadingDialog();
        HashMap<String, String> map = new HashMap<>();
//        map.put("doc_stus",doc_stus);
        commonLogic.commit(map, new CommonLogic.CommitListener() {
            @Override
            public void onSuccess(String msg) {
                dismissLoadingDialog();
                showCommitSuccessDialog(msg, new OnDialogClickListener() {
                    @Override
                    public void onCallback() {
                        pactivity.createNewModuleId(pactivity.module);
                        pactivity.mZXVp.setCurrentItem(0);
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
