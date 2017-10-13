package digiwin.smartdepott100.module.activity.purchase.purchaseinstore;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import digiwin.smartdepott100.R;
import digiwin.smartdepott100.core.appcontants.AddressContants;
import digiwin.smartdepott100.core.appcontants.ModuleCode;
import digiwin.smartdepott100.core.base.BaseTitleActivity;
import digiwin.smartdepott100.core.modulecommon.ModuleUtils;
import digiwin.smartdepott100.login.loginlogic.LoginLogic;
import digiwin.smartdepott100.module.adapter.purchase.PurchaseInStorageAdapter;
import digiwin.smartdepott100.module.bean.common.FilterBean;
import digiwin.smartdepott100.module.bean.common.FilterResultOrderBean;
import digiwin.smartdepott100.module.logic.purchase.PurchaseInStoreLogic;
import digiwin.smartdepott100.core.dialog.datepicker.DatePickerUtils;
import digiwin.library.utils.ActivityManagerUtils;
import digiwin.library.utils.LogUtils;
import digiwin.library.utils.StringUtils;
import digiwin.pulltorefreshlibrary.recyclerview.FullyLinearLayoutManager;
import digiwin.pulltorefreshlibrary.recyclerviewAdapter.OnItemClickListener;


/**
 * 扫码入库 筛选界面
 *
 * @author 唐孟宇
 */
public class PurchaseInStoreListActivity extends BaseTitleActivity {

    @BindViews({R.id.et_purchase_order, R.id.et_provider, R.id.et_date})
    List<EditText> editTexts;
    @BindViews({R.id.ll_purchase_order, R.id.ll_provider, R.id.ll_date})
    List<View> views;
    @BindViews({R.id.tv_purchase_order, R.id.tv_provider, R.id.tv_date})
    List<TextView> textViews;

    /**
     * 筛选框 供应商代码
     */
    @BindView(R.id.et_provider)
    EditText etProvider;

    /**
     * 筛选框 供应商代码
     */
    @BindView(R.id.ll_provider)
    LinearLayout llProvider;
    /**
     * 筛选框 供应商代码
     */
    @BindView(R.id.tv_provider)
    TextView tvProvider;
    @BindView(R.id.toolbar_title)
    Toolbar toolbarTitle;
    @BindView(R.id.tv_purchase_order)
    TextView tvPurchaseOrder;
    @BindView(R.id.et_purchase_order)
    EditText etPurchaseOrder;
    @BindView(R.id.ll_purchase_order)
    LinearLayout llPurchaseOrder;

    @OnFocusChange(R.id.et_purchase_order)
    void pruchaseFocusChanage() {
        ModuleUtils.viewChange(llPurchaseOrder, views);
        ModuleUtils.etChange(activity, etPurchaseOrder, editTexts);
        ModuleUtils.tvChange(activity, tvPurchaseOrder, textViews);
    }

    @OnFocusChange(R.id.et_provider)
    void prividerFocusChanage() {
        ModuleUtils.viewChange(llProvider, views);
        ModuleUtils.etChange(activity, etProvider, editTexts);
        ModuleUtils.tvChange(activity, tvProvider, textViews);
    }

    @OnFocusChange(R.id.et_date)
    void planDateFocusChanage() {
        ModuleUtils.viewChange(llDate, views);
        ModuleUtils.etChange(activity, etDate, editTexts);
        ModuleUtils.tvChange(activity, tvDate, textViews);
    }

    /**
     * 筛选框 日期
     */
    @BindView(R.id.et_date)
    EditText etDate;

    /**
     * 筛选框 日期
     */
    @BindView(R.id.ll_date)
    LinearLayout llDate;
    /**
     * 筛选框 日期
     */
    @BindView(R.id.tv_date)
    TextView tvDate;

    /**
     * 筛选框 日期
     */
    @BindView(R.id.iv_date)
    ImageView ivDate;

    String startDate = "";
    String endDate = "";

    @OnClick(R.id.iv_date)
    void dateClick() {
        DatePickerUtils.getDoubleDate(pactivity, new DatePickerUtils.GetDoubleDateListener() {
            @Override
            public void getTime(String mStartDate, String mEndDate, String showDate) {
                etDate.requestFocus();
                startDate = mStartDate;
                endDate = mEndDate;
                etDate.setText(showDate);
            }
        });
    }


    @BindView(R.id.ry_list)
    RecyclerView ryList;

    @BindView(R.id.ll_search_dialog)
    LinearLayout llSearchDialog;


    /**
     * 跳转扫描使用
     */
    public static final int SUMCODE = 1212;

    PurchaseInStoreLogic commonLogic;

    PurchaseInStoreListActivity pactivity;


    /**
     * 弹出筛选对话框
     */
    @OnClick(R.id.iv_title_setting)
    void searchDialog() {
        if (llSearchDialog.getVisibility() == View.VISIBLE) {
            if (null != sumShowBeanList && sumShowBeanList.size() > 0) {
                llSearchDialog.setVisibility(View.GONE);
                ryList.setVisibility(View.VISIBLE);
            }
        } else {
            llSearchDialog.setVisibility(View.VISIBLE);
            ryList.setVisibility(View.GONE);
        }
    }

    /**
     * 点击确定，筛选
     */
    @OnClick(R.id.btn_search_sure)
    void search() {
        upDateList();
    }

    @Override
    protected Toolbar toolbar() {
        return toolbarTitle;
    }

    @Override
    public String moduleCode() {
        module = ModuleCode.PURCHASEINSTORE;
        return module;
    }

    @Override
    protected int bindLayoutId() {
        return R.layout.activity_purchase_in_store_order;
    }

    @Override
    protected void doBusiness() {
        startDate = "";
        endDate = "";
        etDate.setKeyListener(null);
        pactivity = (PurchaseInStoreListActivity) activity;
        commonLogic = PurchaseInStoreLogic.getInstance(pactivity, module, mTimestamp.toString());
        FullyLinearLayoutManager linearLayoutManager = new FullyLinearLayoutManager(activity);
        ryList.setLayoutManager(linearLayoutManager);
        searchDialog();
    }


    /**
     * 只有一笔时自动跳入
     * 跳转到扫描页面
     */
    private void itemClick(List<FilterResultOrderBean> clickBeen, int position) {
        final FilterResultOrderBean orderData = clickBeen.get(position);
        Bundle bundle = new Bundle();
        bundle.putSerializable(AddressContants.ORDERDATA, orderData);
        ActivityManagerUtils.startActivityBundleForResult(pactivity,
                PurchaseInStoreActivity.class, bundle, SUMCODE);
    }

    @Override
    protected void initNavigationTitle() {
        super.initNavigationTitle();
        mName.setText(getString(R.string.purchase_in_store) + "" + getString(R.string.list));
        ivTitleSetting.setVisibility(View.VISIBLE);
        ivTitleSetting.setImageResource(R.drawable.search);
    }

    PurchaseInStorageAdapter adapter;

    List<FilterResultOrderBean> sumShowBeanList;

    /**
     * 汇总展示
     */
    public void upDateList() {
        FilterBean filterBean = new FilterBean();
        try {
            //仓库
            filterBean.setWarehouse_no(LoginLogic.getWare());
            filterBean.setDoc_no(etPurchaseOrder.getText().toString());
            //供应商
            if (!StringUtils.isBlank(etProvider.getText().toString())) {
                filterBean.setSupplier_no(etProvider.getText().toString());
            }
            //日期
            if (!StringUtils.isBlank(etDate.getText().toString())) {
                filterBean.setDate_begin(startDate);
                filterBean.setDate_end(endDate);
            }
            showLoadingDialog();
            commonLogic.getPISListData(filterBean, new PurchaseInStoreLogic.GetDataListListener() {
                @Override
                public void onSuccess(List<FilterResultOrderBean> list) {
                    if (null != list && list.size() > 0) {
                        dismissLoadingDialog();
                        //查询成功隐藏筛选界面，展示汇总信息
                        llSearchDialog.setVisibility(View.GONE);
                        ryList.setVisibility(View.VISIBLE);
                        ivTitleSetting.setVisibility(View.VISIBLE);
                        sumShowBeanList = new ArrayList<FilterResultOrderBean>();
                        sumShowBeanList = list;
                        adapter = new PurchaseInStorageAdapter(pactivity, sumShowBeanList);
                        ryList.setAdapter(adapter);
                        adapter.setOnItemClickListener(new OnItemClickListener() {
                            @Override
                            public void onItemClick(View itemView, int position) {
                                itemClick(sumShowBeanList, position);
                            }
                        });
                        if (autoSkip && sumShowBeanList.size() == 1) {
                            itemClick(sumShowBeanList, 0);
                        }
                        autoSkip = true;
                    }
                }

                @Override
                public void onFailed(String error) {
                    dismissLoadingDialog();
                    showFailedDialog(error);
                    sumShowBeanList = new ArrayList<FilterResultOrderBean>();
                    adapter = new PurchaseInStorageAdapter(pactivity, sumShowBeanList);
                    ryList.setAdapter(adapter);
                }
            });

        } catch (Exception e) {
            LogUtils.e(TAG, "updateList--getSum--Exception" + e);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SUMCODE) {
            adapter = new PurchaseInStorageAdapter(pactivity, new ArrayList<FilterResultOrderBean>());
            ryList.setAdapter(adapter);
            upDateList();
        }

    }

}
