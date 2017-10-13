package digiwin.smartdepott100.module.activity.produce.worksupplement;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
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
import digiwin.smartdepott100.core.dialog.datepicker.DatePickerUtils;
import digiwin.library.utils.ActivityManagerUtils;
import digiwin.library.utils.StringUtils;
import digiwin.pulltorefreshlibrary.recyclerviewAdapter.OnItemClickListener;
import digiwin.smartdepott100.R;
import digiwin.smartdepott100.core.appcontants.ModuleCode;
import digiwin.smartdepott100.core.base.BaseTitleActivity;
import digiwin.smartdepott100.core.modulecommon.ModuleUtils;
import digiwin.smartdepott100.login.loginlogic.LoginLogic;
import digiwin.smartdepott100.module.adapter.produce.WorkSupplementListAdapter;
import digiwin.smartdepott100.module.bean.common.FilterBean;
import digiwin.smartdepott100.module.bean.common.FilterResultOrderBean;
import digiwin.smartdepott100.module.logic.common.CommonLogic;
import digiwin.smartdepott100.module.logic.produce.WorkSupplementLogic;

import static digiwin.smartdepott100.R.id.scrollview;

/**
 * @author 赵浩然
 * @module 依工单补料-补料清单
 * @date 2017/3/27
 */

public class WorkSupplementListActivity extends BaseTitleActivity {
    private WorkSupplementListActivity activity;

    @BindView(R.id.toolbar_title)
    Toolbar toolbarTitle;

    @BindView(R.id.ry_list)
    RecyclerView ryList;

    /**
     * 筛选布局
     */
    @BindView(R.id.ll_search_dialog)
    LinearLayout llSearchDialog;

    WorkSupplementListAdapter adapter;

    WorkSupplementLogic commonLogic;

    @BindViews({R.id.ll_material_returning_number, R.id.ll_returning_item_no, R.id.ll_applicant,
            R.id.ll_apply_branch, R.id.ll_plan_date})
    List<View> views;
    @BindViews({R.id.tv_material_returning_number, R.id.tv_returning_item_no, R.id.tv_applicant,
            R.id.tv_apply_branch, R.id.tv_plan_date})
    List<TextView> textViews;
    @BindViews({R.id.et_material_returning_number, R.id.et_returning_item_no, R.id.et_applicant,
            R.id.et_apply_branch, R.id.et_plan_date})
    List<EditText> editTexts;

    /**
     * 退料单号
     */
    @BindView(R.id.ll_material_returning_number)
    LinearLayout ll_material_returning_number;
    @BindView(R.id.tv_material_returning_number)
    TextView tv_material_returning_number;
    @BindView(R.id.et_material_returning_number)
    EditText et_material_returning_number;

    @OnFocusChange(R.id.et_material_returning_number)
    void shipping_orderFocusChanage() {
        ModuleUtils.viewChange(ll_material_returning_number, views);
        ModuleUtils.tvChange(activity, tv_material_returning_number, textViews);
        ModuleUtils.etChange(activity, et_material_returning_number, editTexts);
    }

    /**
     * 退料料号
     */
    @BindView(R.id.ll_returning_item_no)
    LinearLayout ll_returning_item_no;
    @BindView(R.id.tv_returning_item_no)
    TextView tv_returning_item_no;
    @BindView(R.id.et_returning_item_no)
    EditText et_returning_item_no;

    @OnFocusChange(R.id.et_returning_item_no)
    void item_noFocusChanage() {
        ModuleUtils.viewChange(ll_returning_item_no, views);
        ModuleUtils.tvChange(activity, tv_returning_item_no, textViews);
        ModuleUtils.etChange(activity, et_returning_item_no, editTexts);
    }

    /**
     * 申请人
     */
    @BindView(R.id.ll_applicant)
    LinearLayout ll_applicant;
    @BindView(R.id.tv_applicant)
    TextView tv_applicant;
    @BindView(R.id.et_applicant)
    EditText et_applicant;

    @OnFocusChange(R.id.et_applicant)
    void customFocusChanage() {
        ModuleUtils.viewChange(ll_applicant, views);
        ModuleUtils.tvChange(activity, tv_applicant, textViews);
        ModuleUtils.etChange(activity, et_applicant, editTexts);
    }

    /**
     * 申请部门
     */
    @BindView(R.id.ll_apply_branch)
    LinearLayout ll_apply_branch;
    @BindView(R.id.tv_apply_branch)
    TextView tv_apply_branch;
    @BindView(R.id.et_apply_branch)
    EditText et_apply_branch;

    @OnFocusChange(R.id.et_apply_branch)
    void salesmanFocusChanage() {
        ModuleUtils.viewChange(ll_apply_branch, views);
        ModuleUtils.tvChange(activity, tv_apply_branch, textViews);
        ModuleUtils.etChange(activity, et_apply_branch, editTexts);
    }

    /**
     * 筛选框 计划日
     */
    @BindView(R.id.iv_plan_date)
    ImageView iv_plan_date;
    @BindView(R.id.ll_plan_date)
    LinearLayout ll_plan_date;
    @BindView(R.id.tv_plan_date)
    TextView tv_plan_date;
    @BindView(R.id.et_plan_date)
    EditText etPlanDate;

    @OnFocusChange(R.id.et_plan_date)
    void plan_dateFocusChanage() {
        ModuleUtils.viewChange(ll_plan_date, views);
        ModuleUtils.tvChange(activity, tv_plan_date, textViews);
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

    private List<FilterResultOrderBean> dataList;

    @BindView(R.id.btn_search_sure)
    Button btn_search_sure;

    @OnClick(R.id.btn_search_sure)
    void search() {
        //待办事项展示
        FilterBean filterBean = new FilterBean();
        showLoadingDialog();
        filterBean.setWarehouse_no(LoginLogic.getWare());
        if (!StringUtils.isBlank(et_material_returning_number.getText().toString().trim())) {
            filterBean.setDoc_no(et_material_returning_number.getText().toString().trim());
        }

        if (!StringUtils.isBlank(et_returning_item_no.getText().toString().trim())) {
            filterBean.setBarcode_no(et_returning_item_no.getText().toString().trim());
        }

        if (!StringUtils.isBlank(et_applicant.getText().toString().trim())) {
            filterBean.setEmployee_no(et_applicant.getText().toString().trim());
        }

        if (!StringUtils.isBlank(et_apply_branch.getText().toString().trim())) {
            filterBean.setDepartment_no(et_apply_branch.getText().toString().trim());
        }

        if (!StringUtils.isBlank(etPlanDate.getText().toString())) {
            filterBean.setDate_begin(startDate);
            filterBean.setDate_end(endDate);
        }

        commonLogic.getWSList(filterBean, new CommonLogic.GetDataListListener() {
            @Override
            public void onSuccess(final List<FilterResultOrderBean> list) {
                dismissLoadingDialog();
                if (list.size() > 0) {
                    llSearchDialog.setVisibility(View.GONE);
                    ryList.setVisibility(View.VISIBLE);
                    dataList = new ArrayList<FilterResultOrderBean>();
                    dataList = list;
                    adapter = new WorkSupplementListAdapter(activity, list);
                    ryList.setAdapter(adapter);
                    adapter.setOnItemClickListener(new OnItemClickListener() {
                        @Override
                        public void onItemClick(View itemView, int position) {
                            itemClick(list, position);
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
                adapter = new WorkSupplementListAdapter(activity, dataList);
                ryList.setAdapter(adapter);
            }
        });
    }

    private void itemClick(List<FilterResultOrderBean> clickBeen, int position) {
        Bundle bundle = new Bundle();
        FilterResultOrderBean data = clickBeen.get(position);
        bundle.putSerializable("data", data);
        ActivityManagerUtils.startActivityBundleForResult(activity, WorkSupplementActivity.class, bundle, SCANCODE);
    }

    @Override
    protected void doBusiness() {
        startDate="";
        endDate="";
        etPlanDate.setKeyListener(null);
        commonLogic = WorkSupplementLogic.getInstance(activity, module, mTimestamp.toString());
        ryList.setLayoutManager(new LinearLayoutManager(this));
    }

    /**
     * 弹出筛选对话框
     */
    @OnClick(R.id.iv_title_setting)
    void searchDialog() {
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
    protected int bindLayoutId() {
        return R.layout.activity_worksupplementlist;
    }

    @Override
    protected Toolbar toolbar() {
        return toolbarTitle;
    }

    @Override
    public String moduleCode() {
        module = ModuleCode.WORKSUPPLEMENT;
        return module;
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
    }

    @Override
    protected void initNavigationTitle() {
        super.initNavigationTitle();
        activity = this;
        mName.setText(getString(R.string.title_worksupplement) + "" + getString(R.string.list));
        ivTitleSetting.setVisibility(View.VISIBLE);
        ivTitleSetting.setImageResource(R.drawable.search);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SCANCODE) {
            dataList.clear();
            adapter = new WorkSupplementListAdapter(activity, dataList);
            ryList.setAdapter(adapter);
            search();
        }
    }
}