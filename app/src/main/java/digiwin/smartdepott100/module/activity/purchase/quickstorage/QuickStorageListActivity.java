package digiwin.smartdepott100.module.activity.purchase.quickstorage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
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
import digiwin.library.constant.SharePreKey;
import digiwin.smartdepott100.core.dialog.datepicker.DatePickerUtils;
import digiwin.library.utils.ActivityManagerUtils;
import digiwin.library.utils.SharedPreferencesUtils;
import digiwin.library.utils.StringUtils;
import digiwin.pulltorefreshlibrary.recyclerview.FullyLinearLayoutManager;
import digiwin.pulltorefreshlibrary.recyclerviewAdapter.OnItemClickListener;
import digiwin.smartdepott100.R;
import digiwin.smartdepott100.core.appcontants.ModuleCode;
import digiwin.smartdepott100.core.base.BaseTitleActivity;
import digiwin.smartdepott100.core.modulecommon.ModuleUtils;
import digiwin.smartdepott100.module.adapter.stock.QuickStorageListAdapter;
import digiwin.smartdepott100.module.bean.common.FilterBean;
import digiwin.smartdepott100.module.bean.common.FilterResultOrderBean;
import digiwin.smartdepott100.module.logic.common.CommonLogic;
import digiwin.smartdepott100.module.logic.produce.QuickStorageLogic;


/**
 * @author 孙长权
 * @module 快速入库 -- 清单
 * @date 2017/6/15
 */

public class QuickStorageListActivity extends BaseTitleActivity {
    private QuickStorageListActivity activity;

    QuickStorageListAdapter adapter;

    QuickStorageLogic quickStorageLogic;

    @BindView(R.id.toolbar_title)
    Toolbar toolbarTitle;

    @BindView(R.id.ry_list)
    RecyclerView ryList;

    /**
     * 筛选布局
     */
    @BindView(R.id.ll_search_dialog)
    LinearLayout llSearchDialog;

    @BindViews({R.id.ll_provider_code, R.id.ll_barcode_no, R.id.ll_item_name, R.id.ll_plan_date})
    List<View> views;
    @BindViews({R.id.tv_provider_code, R.id.tv_barcode_no, R.id.tv_item_name, R.id.tv_plan_date})
    List<TextView> textViews;
    @BindViews({R.id.et_provider_code, R.id.et_barcode_no, R.id.et_item_name, R.id.et_plan_date})
    List<EditText> editTexts;

    /**
     * 供应商代码
     */
    @BindView(R.id.ll_provider_code)
    LinearLayout llProviderCode;
    @BindView(R.id.tv_provider_code)
    TextView tvProviderCode;
    @BindView(R.id.et_provider_code)
    EditText etProviderCode;

    @OnFocusChange(R.id.et_provider_code)
    void provider_codeFocusChanage() {
        ModuleUtils.viewChange(llProviderCode, views);
        ModuleUtils.tvChange(activity, tvProviderCode, textViews);
        ModuleUtils.etChange(activity, etProviderCode, editTexts);
    }

    /**
     * 物料条码
     */
    @BindView(R.id.ll_barcode_no)
    LinearLayout llBarcodeNo;
    @BindView(R.id.tv_barcode_no)
    TextView tvBarcodeNo;
    @BindView(R.id.et_barcode_no)
    EditText etBarcodeNo;

    @OnFocusChange(R.id.et_barcode_no)
    void barcode_noFocusChanage() {
        ModuleUtils.viewChange(llBarcodeNo, views);
        ModuleUtils.tvChange(activity, tvBarcodeNo, textViews);
        ModuleUtils.etChange(activity, etBarcodeNo, editTexts);
    }

    /**
     * 品名
     */
    @BindView(R.id.ll_item_name)
    LinearLayout llItemName;
    @BindView(R.id.tv_item_name)
    TextView tvItemName;
    @BindView(R.id.et_item_name)
    EditText etItemName;

    @OnFocusChange(R.id.et_item_name)
    void customFocusChanage() {
        ModuleUtils.viewChange(llItemName, views);
        ModuleUtils.tvChange(activity, tvItemName, textViews);
        ModuleUtils.etChange(activity, etItemName, editTexts);
    }

    /**
     * 筛选框 计划日
     */
    @BindView(R.id.ll_plan_date)
    LinearLayout llPlanDate;

    @BindView(R.id.iv_plan_date)
    ImageView ivPlanDate;
    @BindView(R.id.tv_plan_date)
    TextView tvPlanDate;
    @BindView(R.id.et_plan_date)
    EditText etPlanDate;

    @OnFocusChange(R.id.et_plan_date)
    void plan_dateFocusChanage() {
        ModuleUtils.viewChange(llPlanDate, views);
        ModuleUtils.tvChange(activity, tvPlanDate, textViews);
        ModuleUtils.etChange(activity, etPlanDate, editTexts);
    }

    String startDate = "";
    String endDate = "";

    @OnClick(R.id.iv_plan_date)
    void dateClick() {
        DatePickerUtils.getDoubleDate(activity, new DatePickerUtils.GetDoubleDateListener() {
            @Override
            public void getTime(String mStartDate, String mEndDate, String showDate) {
                etPlanDate.requestFocus();
                startDate = mStartDate;
                endDate = mEndDate;
                etPlanDate.setText(showDate);
            }
        });
    }

    private final int SCANCODE = 1234;

    private final String DATA = "data";

    private List<FilterResultOrderBean> dataList;

    @BindView(R.id.btn_search_sure)
    Button btnSearchSure;

    @OnClick(R.id.btn_search_sure)
    void search() {
        FilterBean filterBean = new FilterBean();
        try {
            showLoadingDialog();
            if (!StringUtils.isBlank(etProviderCode.getText().toString().trim())) {
                filterBean.setSupplier_no(etProviderCode.getText().toString().trim());//供应商
            }

            if (!StringUtils.isBlank(etBarcodeNo.getText().toString().trim())) {
                filterBean.setItem_no(etBarcodeNo.getText().toString().trim());//料号
            }

            if (!StringUtils.isBlank(etItemName.getText().toString().trim())) {
                filterBean.setItem_name(etItemName.getText().toString().trim());
            }

            if (!StringUtils.isBlank(etPlanDate.getText().toString())) {
                filterBean.setDate_begin(startDate);
                filterBean.setDate_end(endDate);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        quickStorageLogic.getQuickStorageOrderData(filterBean, new CommonLogic.GetOrderListener() {
            @Override
            public void onSuccess(final List<FilterResultOrderBean> list) {
                dismissLoadingDialog();
                if (list.size() > 0) {
                    llSearchDialog.setVisibility(View.GONE);
                    ryList.setVisibility(View.VISIBLE);
                    dataList = new ArrayList<FilterResultOrderBean>();
                    dataList = list;
                    adapter = new QuickStorageListAdapter(activity, list);
                    ryList.setAdapter(adapter);
                    adapter.setOnItemClickListener(new OnItemClickListener() {
                        @Override
                        public void onItemClick(View itemView, int position) {
                            itemClick(dataList, position);
                        }
                    });
                    if (autoSkip && list.size() == 1) {
                        itemClick(dataList, 0);
                    }
                    autoSkip = true;
                } else {
                    showFailedDialog(getResources().getString(R.string.nodate));
                }
            }

            @Override
            public void onFailed(String error) {
                dismissLoadingDialog();
                showFailedDialog(error);
                ArrayList dataList = new ArrayList<FilterResultOrderBean>();
                adapter = new QuickStorageListAdapter(activity, dataList);
                ryList.setAdapter(adapter);
            }
        });
    }

    /**
     * 只有一笔时自动跳入
     * 跳转到扫描页面
     */
    private void itemClick(List<FilterResultOrderBean> clickBeen, int position) {
        Bundle bundle = new Bundle();
        FilterResultOrderBean data = clickBeen.get(position);
        bundle.putSerializable(DATA, data);
        ActivityManagerUtils.startActivityBundleForResult(activity, QuickStorageActivity.class, bundle, SCANCODE);
    }

    @Override
    protected void doBusiness() {
        startDate = "";
        endDate = "";
        etPlanDate.setKeyListener(null);
        quickStorageLogic = QuickStorageLogic.getInstance(activity, module, mTimestamp.toString());
        FullyLinearLayoutManager linearLayoutManager = new FullyLinearLayoutManager(activity);
        ryList.setLayoutManager(linearLayoutManager);

    }

    /**
     * 弹出筛选对话框
     */
    @OnClick(R.id.iv_title_setting)
    void SearchDialog() {
        if (llSearchDialog.getVisibility() == View.VISIBLE) {
            if (null != dataList && dataList.size() > 0) {
                llSearchDialog.setVisibility(View.GONE);
                ryList.setVisibility(View.VISIBLE);
            }
        } else {
            llSearchDialog.setVisibility(View.VISIBLE);
            ryList.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == SCANCODE) {
                dataList.clear();
                adapter = new QuickStorageListAdapter(activity, dataList);
                ryList.setAdapter(adapter);
                search();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected int bindLayoutId() {
        return R.layout.activity_quickstoragelist;
    }

    @Override
    protected Toolbar toolbar() {
        return toolbarTitle;
    }

    @Override
    public String moduleCode() {
        module = ModuleCode.QUICKSTORAGE;
        return module;
    }

    @Override
    protected void initNavigationTitle() {
        super.initNavigationTitle();
        mName.setText(getString(R.string.title_quickstorage) + getString(R.string.list));
        activity = this;
        ivTitleSetting.setVisibility(View.VISIBLE);
        ivTitleSetting.setImageResource(R.drawable.search);
    }
}
