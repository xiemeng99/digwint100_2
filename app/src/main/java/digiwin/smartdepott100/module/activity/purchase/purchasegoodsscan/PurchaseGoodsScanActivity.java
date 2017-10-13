package digiwin.smartdepott100.module.activity.purchase.purchasegoodsscan;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import digiwin.library.dialog.OnDialogClickListener;
import digiwin.library.dialog.OnDialogTwoListener;
import digiwin.library.utils.StringUtils;
import digiwin.pulltorefreshlibrary.recyclerview.FullyLinearLayoutManager;
import digiwin.smartdepott100.R;
import digiwin.smartdepott100.core.appcontants.AddressContants;
import digiwin.smartdepott100.core.appcontants.ModuleCode;
import digiwin.smartdepott100.core.base.BaseFirstModuldeActivity;
import digiwin.smartdepott100.core.printer.BlueToothManager;
import digiwin.smartdepott100.login.loginlogic.LoginLogic;
import digiwin.smartdepott100.main.logic.ToSettingLogic;
import digiwin.smartdepott100.module.adapter.purchase.PurchaseGoodsSumAdapter;
import digiwin.smartdepott100.module.bean.common.ClickItemPutBean;
import digiwin.smartdepott100.module.bean.common.FilterResultOrderBean;
import digiwin.smartdepott100.module.bean.common.ListSumBean;
import digiwin.smartdepott100.module.bean.purchase.PurchaseGoodsScanBackBean;
import digiwin.smartdepott100.module.bean.stock.PrintBarcodeBean;
import digiwin.smartdepott100.module.logic.common.CommonLogic;
import digiwin.smartdepott100.module.logic.purchase.PurchaseGoodScanLogic;

/**
 * @author 唐孟宇
 * @des 扫码收货 扫描/汇总页面
 */
public class PurchaseGoodsScanActivity extends BaseFirstModuldeActivity {

    @BindView(R.id.toolbar_title)
    Toolbar toolbarTitle;

    @BindView(R.id.ry_list)
    RecyclerView ryList;

    /**
     * 送货单号
     */
    @BindView(R.id.tv_head_post_order)
    TextView tvHeadPostOrder;
    /**
     * 日期
     */
    @BindView(R.id.tv_head_plan_date)
    TextView tvHeadPlanDate;
    /**
     * 供应商
     */
    @BindView(R.id.tv_head_provider)
    TextView tvHeadProvider;

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


    PurchaseGoodScanLogic commonLogic;

    private boolean upDateFlag;

    PurchaseGoodsSumAdapter adapter;

    List<ListSumBean> sumShowBeanList;

    FilterResultOrderBean orderData;

    @Override
    protected void initNavigationTitle() {
        super.initNavigationTitle();
        mName.setText(R.string.title_purchase_goods_scan);
    }

    @Override
    protected int bindLayoutId() {
        return R.layout.activity_purchase_goods_sum;
    }

    @Override
    protected void doBusiness() {
        commonLogic = PurchaseGoodScanLogic.getInstance(activity, module, mTimestamp.toString());
        FullyLinearLayoutManager linearLayoutManager = new FullyLinearLayoutManager(activity);
        ryList.setLayoutManager(linearLayoutManager);
        upDateFlag = false;
        Bundle bundle = getIntent().getExtras();
        orderData = (FilterResultOrderBean) bundle.getSerializable(AddressContants.ORDERDATA);
        tvHeadPlanDate.setText(orderData.getCreate_date());
        tvHeadPostOrder.setText(orderData.getDoc_no());
        tvHeadProvider.setText(orderData.getSupplier_name());
        upDateList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean isOpen = BlueToothManager.getManager(activity).isOpen();
        if (!isOpen){
            ToSettingLogic.showToSetdialog(activity, R.string.title_set_bluttooth, new OnDialogTwoListener() {
                @Override
                public void onCallback1() {

                }
                @Override
                public void onCallback2() {
                    activity.finish();
                }
            });
        }
    }

    /**
     * 汇总展示
     */
    public void upDateList() {
        ClickItemPutBean clickItemPutData = new ClickItemPutBean();
        clickItemPutData.setDoc_no(orderData.getDoc_no());
        clickItemPutData.setWarehouse_no(LoginLogic.getWare());
        showLoadingDialog();
        commonLogic.getPGSSumData(clickItemPutData, new PurchaseGoodScanLogic.GetZSumListener() {
            @Override
            public void onSuccess(List<ListSumBean> list) {
                dismissLoadingDialog();
                sumShowBeanList = list;
                if (list.size() > 0) {
                    adapter = new PurchaseGoodsSumAdapter(activity, sumShowBeanList);
                    ryList.setAdapter(adapter);
                    upDateFlag = true;
                }
            }

            @Override
            public void onFailed(String error) {
                dismissLoadingDialog();
                upDateFlag = false;
                showFailedDialog(error);
                sumShowBeanList = new ArrayList<ListSumBean>();
                adapter = new PurchaseGoodsSumAdapter(activity, sumShowBeanList);
                ryList.setAdapter(adapter);
            }
        });
    }


    private void sureCommit() {
        if (!upDateFlag) {
            showFailedDialog(R.string.nodate);
            return;
        }
        float sum=0;
        for (int i=0;i<sumShowBeanList.size();i++){
            if (StringUtils.isBlank(sumShowBeanList.get(i).getScan_sumqty())){
                showFailedDialog(sumShowBeanList.get(i).getItem_no()+getString(R.string.num_empty));
                return;
            }
            float sub = StringUtils.sub(sumShowBeanList.get(i).getApply_qty(), sumShowBeanList.get(i).getScan_sumqty());
            if (sub<0){
                showFailedDialog(sumShowBeanList.get(i).getItem_no()+getString(R.string.num_big));
                return;
            }
            sum+=StringUtils.string2Float(sumShowBeanList.get(i).getScan_sumqty());
        }
        if (sum==0){
            showFailedDialog(R.string.num_all_zero);
            return;
        }
        showLoadingDialog();
        commonLogic.commitPGSData(sumShowBeanList, new PurchaseGoodScanLogic.CommitPurchaseListener() {
            @Override
            public void onSuccess(String docNo,final List<PurchaseGoodsScanBackBean> scanBackBeen) {
                dismissLoadingDialog();
                showCommitSuccessDialog(docNo, new OnDialogClickListener() {
                    @Override
                    public void onCallback() {
                        showPrint(scanBackBeen);
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

    /**
     * 打印
     * @param scanBackBeen
     */
    private void showPrint(final  List<PurchaseGoodsScanBackBean> scanBackBeen){
        PrintDialog.showRePrinterDailog(this, new PrintDialog.PrinterFlowListener() {
            @Override
            public void bindByDevice(String lableNumber) {
                boolean isOpen = BlueToothManager.getManager(activity).isOpen();
                if (!isOpen){
                    ToSettingLogic.showToSetdialog(activity,R.string.title_set_bluttooth,null);
                    return;
                }else {
                    BlueToothManager.getManager(activity).printPurchaseScan(scanBackBeen,lableNumber);
                }
            }
        });


    }






    @Override
    public ExitMode exitOrDel() {
        return ExitMode.EXITD;
    }

    @Override
    public String moduleCode() {
        module=ModuleCode.PURCHASEGOODSSCAN;
        return module;
    }

    @Override
    protected Toolbar toolbar() {
        return toolbarTitle;
    }
}
