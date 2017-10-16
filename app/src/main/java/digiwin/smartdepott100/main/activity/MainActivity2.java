package digiwin.smartdepott100.main.activity;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import digiwin.library.constant.SharePreKey;
import digiwin.library.utils.ActivityManagerUtils;
import digiwin.library.utils.SharedPreferencesUtils;
import digiwin.library.utils.StringUtils;
import digiwin.library.utils.ToastUtils;
import digiwin.library.voiceutils.VoiceUtils;
import digiwin.smartdepott100.R;
import digiwin.smartdepott100.core.appcontants.ModuleCode;
import digiwin.smartdepott100.core.base.BaseApplication;
import digiwin.smartdepott100.core.base.BaseTitleActivity;
import digiwin.smartdepott100.main.bean.ModuleBean;
import digiwin.smartdepott100.main.bean.TotalMode;
import digiwin.smartdepott100.main.bean.VoiceWord;
import digiwin.smartdepott100.main.fragment.BaseMainFg;
import digiwin.smartdepott100.main.fragment.OtherFragment;
import digiwin.smartdepott100.main.fragment.MainPurchaseFragment;
import digiwin.smartdepott100.main.logic.MainLogic;
import digiwin.smartdepott100.main.logic.MainLogic2;


public class MainActivity2 extends BaseTitleActivity {
    @BindView(R.id.tv_title_person_name)
    TextView tvPersonName;
    @BindView(R.id.toolbar_title)
    Toolbar toolbar;
    @BindView(R.id.vp_main)
    ViewPager viewPager;
    @BindView(R.id.ll_point)
    LinearLayout linearLayoutPoints;
    @BindView(R.id.tb_title)
    TabLayout tablayout;
    //两秒内按返回键两次退出程序
    private long exitTime;
    /**
     * 指示点集合
     */
    private List<View> points;
    /**
     * 标题栏数组
     */
    private List<String> titles;

    @BindView(R.id.tv_title_operation)
    TextView tv_title_operation;

    String command = "";

    @BindView(R.id.voice_guide)
    ImageView voiceGuide;

    /**
     * 语音按钮
     */
    @OnClick(R.id.voice_guide)
    void voiceTest() {
        String voicer = (String) SharedPreferencesUtils.get(this, SharePreKey.VOICER_SELECTED, "voicer");
        final List<ModuleBean> list = MainLogic.ModuleList;
        VoiceUtils voiceUtils = VoiceUtils.getInstance(this, voicer);
        voiceUtils.voiceToText(new VoiceUtils.GetVoiceTextListener() {
            @Override
            public String getVoiceText(String str) {
                command = str.toUpperCase();
                Log.d(TAG, "command:" + command);
                if (list.size() > 0) {
                    for (int i = 0; i < list.size(); i++) {
                        String name = getResources().getString(list.get(i).getNameRes()).toUpperCase();
                        if (command.length() == name.length()) {
                            if (command.equals(name)) {
                                ActivityManagerUtils.startActivity(MainActivity2.this, list.get(i).getIntent());
                            } else {
                                if (StringUtils.getPingYin(command).equals(StringUtils.getPingYin(name))) {
                                    ActivityManagerUtils.startActivity(MainActivity2.this, list.get(i).getIntent());
                                }
                            }
                        }
                    }
                }
                return str;
            }
        });

    }

    private final int REQUESTCODE = 1002;

    @OnClick(R.id.iv_title_setting)
    public void goSetting() {
        ActivityManagerUtils.startActivityForResult(activity, SettingActivity.class, REQUESTCODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUESTCODE) {
            recreate();
        }
    }

    /**
     * 用户权限
     */
    private String access;
    /**
     * 采购管理
     */
    private List<ModuleBean> purchaseItems;
    /**
     * 采购管理
     */
    private Map<String,List<ModuleBean>> purchasemaps;
    /**
     * 生产管理
     */
    private List<ModuleBean> produceItems;
    /**
     * 生产管理
     */
    private Map<String,List<ModuleBean>> producemaps;
    /**
     * 库存管理
     */
    private List<ModuleBean> storageItems;
    /**
     * 库存管理
     */
    private Map<String,List<ModuleBean>> storagemaps;
    /**
     * 销售管理
     */
    private List<ModuleBean> salesItems;
    /**
     *销售管理
     */
    private Map<String,List<ModuleBean>> salesmaps;
    /**
     * 报工管理
     */
    private List<ModuleBean> dailyworkItems;
    /**
     * 看板管理
     */
    private List<ModuleBean> boardItems;


    private List<BaseMainFg> fgs;

    /**
     * 用户权限模块
     */
    private List<String> powerItems;

    /**
     * 点击选择模块
     */
    private List<TotalMode> totalModes;

    MainLogic2 mainLogic;

    @Override
    protected int bindLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initNavigationTitle() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayUseLogoEnabled(false);
        mBack.setVisibility(View.GONE);
        ivTitleSetting.setVisibility(View.VISIBLE);

        Toolbar.LayoutParams tl = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT);
        tl.gravity = Gravity.CENTER;
        mName.setLayoutParams(tl);
        mName.setEnabled(false);
        mName.setText(R.string.app_name);

    }

    @Override
    protected Toolbar toolbar() {
        return toolbar;
    }

    @Override
    protected void doBusiness() {
        fgs = new ArrayList<>();
        exitTime = 0;
        mainLogic = new MainLogic2(this);
        initModule();
        submitUserWords();
    }

    /**
     * 上传用户此表，用于精准识别语音录入
     */
    private void submitUserWords() {
        List<ModuleBean> modlueList = mainLogic.ModuleList;
        List<String> voiceWordList = new ArrayList<>();
        VoiceWord defaultWord = new VoiceWord();
        defaultWord.setName("default");
        defaultWord.setWord(getResources().getString(R.string.purchase_check));
        voiceWordList.add(defaultWord.toString());
        if (modlueList.size() > 0) {
            for (ModuleBean module : modlueList) {
                String moduleName = getResources().getString(module.getNameRes());
                VoiceWord voiceWord = new VoiceWord();
                voiceWord.setName(moduleName);
                voiceWord.setWord(moduleName);
                voiceWordList.add(voiceWord.toString());
            }
            StringBuffer userword = new StringBuffer();
            String head = "{" + "\"" + "userword" + "\"" + ":";
            userword.append(head);
            userword.append(voiceWordList.toString());
            userword.append("}");
            Log.d(TAG, "voiceWord.toArray():" + userword.toString());
            VoiceUtils.getInstance(getApplicationContext(), SharePreKey.VOICER_SELECTED).submitUserWords(userword.toString());
        }
    }

    //初始化各个模块
    private void initModule() {
        //正式发布版本时在解开
        access = getIntent().getExtras().getString("access");
        powerItems = StringUtils.split(access);
        //模拟测试
//        powerItems = new ArrayList<>();

        totalModes = new ArrayList<TotalMode>();

        titles = new ArrayList<>();
        purchaseItems = new ArrayList<>();
        produceItems = new ArrayList<>();
        storageItems = new ArrayList<>();
        salesItems = new ArrayList<>();
        dailyworkItems = new ArrayList<>();
        boardItems = new ArrayList<>();

        purchasemaps=new HashMap<>();
        producemaps=new HashMap<>();
        storagemaps=new HashMap<>();
        salesmaps=new HashMap<>();

        //初始化各个模块数据
        addMoudle();
    }


    private void addMoudle() {

//        ModuleBean materialreceipt = new ModuleBean(R.string.title_material_receipt, R.mipmap.material_receipt, ModuleCode.MATERIALRECEIPTCODE, "android.intent.actiont100.smartdepot.MaterialReceiptActivity");
//        ModuleBean purchaseCheck = new ModuleBean(R.string.purchase_check, R.mipmap.inspection_tests, ModuleCode.PURCHASECHECK, "android.intent.actiont100.smartdepot.PurchaseCheckActivity");
//        ModuleBean storeReturnMaterial = new ModuleBean(R.string.store_return_material, R.mipmap.warehouse_return, ModuleCode.STORERETURNMATERIAL, "android.intent.actiont100.smartdepot.StoreReturnMaterialListActivity");
//        ModuleBean quickstorage = new ModuleBean(R.string.title_quickstorage, R.mipmap.quickly_storage, ModuleCode.QUICKSTORAGE, "android.intent.actiont100.smartdepot.QuickStorageListActivity");
//        ModuleBean purchaseCheck = new ModuleBean(R.string.purchase_check, R.mipmap.inspection_tests, ModuleCode.PURCHASECHECK, "android.intent.actiont100.smartdepot.PurchaseCheckActivity");
//        ModuleBean quickstorage = new ModuleBean(R.string.title_quickstorage, R.mipmap.quickly_storage, ModuleCode.QUICKSTORAGE, "android.intent.actiont100.smartdepot.QuickStorageListActivity");
//        ModuleBean iqcCheck = new ModuleBean(R.string.iqc_check, R.mipmap.inspection_tests, ModuleCode.PURCHASECHECK, "android.intent.actiont100.smartdepot.IQCListActivity");
        ModuleBean materialreceipt = new ModuleBean(R.string.title_material_receipt, R.mipmap.material_receipt, ModuleCode.MATERIALRECEIPTCODE, "android.intent.actiont100.smartdepot.MaterialReceiptActivity");
        ModuleBean purchaseGoodsScan = new ModuleBean(R.string.title_purchase_goods_scan, R.mipmap.quickly_storage, ModuleCode.PURCHASEGOODSSCAN, "android.intent.actiont100.smartdepot.PurchaseGoodsScanListActivity");
        ModuleBean purchasesupplier = new ModuleBean(R.string.purchase_scan_supplier, R.mipmap.scan_inapection, ModuleCode.PURCHASESUPPLIERSCAN, "android.intent.actiont100.smartdepot.PurhcaseSupplierListActivity");
        ModuleBean purchaseReceiving = new ModuleBean(R.string.purchase_receiving, R.mipmap.purchase_receipt, ModuleCode.PURCHASERECEIVING, "android.intent.actiont100.smartdepot.PurchaseReceivingActivity");
        ModuleBean purchaseInStore = new ModuleBean(R.string.purchase_in_store, R.mipmap.saomaruku, ModuleCode.PURCHASEINSTORE, "android.intent.actiont100.smartdepot.PurchaseInStoreListActivity");
        ModuleBean quickstorage = new ModuleBean(R.string.title_quickstorage, R.mipmap.migo, ModuleCode.QUICKSTORAGE, "android.intent.actiont100.smartdepot.QuickStorageListActivity");
        ModuleBean iqcInspect = new ModuleBean(R.string.iqc_check_pad, R.mipmap.iqc_check_pad, ModuleCode.IQCINSPECT, "android.intent.actiont100.smartdepot.IQCInspectListActivity");
        ModuleBean purchasestore = new ModuleBean(R.string.purchase_store, R.mipmap.warehouse_return, ModuleCode.STORERETURNMATERIAL, "android.intent.actiont100.smartdepot.PurchaseStoreListActivity");

        ArrayList<ModuleBean> purchaseItems1 = new ArrayList<>();
        purchaseItems.add(materialreceipt);
        purchaseItems.add(purchaseGoodsScan);
        purchaseItems.add(purchasesupplier);
        purchaseItems.add(purchaseReceiving);

        purchaseItems1.add(materialreceipt);
        purchaseItems1.add(purchaseGoodsScan);
        purchaseItems1.add(purchasesupplier);
        purchaseItems1.add(purchaseReceiving);

        ArrayList<ModuleBean> purchaseItems2 = new ArrayList<>();
        purchaseItems.add(purchaseInStore);
        purchaseItems.add(quickstorage);

        purchaseItems2.add(purchaseInStore);
        purchaseItems2.add(quickstorage);

        ArrayList<ModuleBean> purchaseItems3 = new ArrayList<>();
        purchaseItems.add(iqcInspect);

        purchaseItems3.add(iqcInspect);

        ArrayList<ModuleBean> purchaseItems4 = new ArrayList<>();
        purchaseItems.add(purchasestore);

        purchaseItems4.add(purchasestore);

        purchasemaps.put("1",purchaseItems1);
        purchasemaps.put("2",purchaseItems2);
        purchasemaps.put("3",purchaseItems3);
        purchasemaps.put("4",purchaseItems4);
//        ModuleBean transfersToReviewActivity = new ModuleBean(R.string.transfers_to_review, R.mipmap.diaobofuhe, ModuleCode.TRANSFERS_TO_REVIEW, "android.intent.actiont100.smartdepot.TransfersToReviewActivity");
//        ModuleBean distribute = new ModuleBean(R.string.distribute, R.mipmap.shengchanpeihuo, ModuleCode.DISTRIBUTE, "android.intent.actiont100.smartdepot.DistributeActivity");
//        ModuleBean postMaterial = new ModuleBean(R.string.post_material, R.mipmap.lingliaoguozahng, ModuleCode.POSTMATERIAL, "android.intent.actiont100.smartdepot.PostMaterialActivity");
//        ModuleBean putInStore = new ModuleBean(R.string.put_in_store, R.mipmap.putaway, ModuleCode.PUTINSTORE, "android.intent.actiont100.smartdepot.PutInStoreActivity");
//        ModuleBean completingstore = new ModuleBean(R.string.title_completing_store, R.mipmap.complete_storage, ModuleCode.COMPLETINGSTORE, "android.intent.actiont100.smartdepot.CompletingStoreActivity");
//        ModuleBean enchaseprint = new ModuleBean(R.string.enchaseprint, R.mipmap.enchaseprint, ModuleCode.ENCHASEPRINT, "android.intent.actiont100.smartdepot.EnchasePrintActivity");
//        ModuleBean linesend = new ModuleBean(R.string.line_send, R.mipmap.xianbianfaliao, ModuleCode.LINESEND, "android.intent.actiont100.smartdepot.LineSendActivity");
        ModuleBean workorder = new ModuleBean(R.string.title_work_order, R.mipmap.work_order, ModuleCode.WORKORDERCODE, "android.intent.actiont100.smartdepot.WorkOrderActivity");//TODO 暂时没有加产品特征码
        ModuleBean accordingMaterialActivity = new ModuleBean(R.string.according_material, R.mipmap.accordingmaterial, ModuleCode.ACCORDINGMATERIALCODE, "android.intent.actiont100.smartdepot.AccordingMaterialActivity");
        ModuleBean productionleaderlist = new ModuleBean(R.string.title_production_leader, R.mipmap.production_receive, ModuleCode.PRODUCTIONLEADER, "android.intent.actiont100.smartdepot.ProductionLeaderListActivity");
        ModuleBean suitpicking = new ModuleBean(R.string.suitpicking, R.mipmap.lingliaoguozahng, ModuleCode.SUITPICKING, "android.intent.actiont100.smartdepot.SuitPickingListAcitivity");
        ModuleBean orderputaway = new ModuleBean(R.string.order_putaway, R.mipmap.order_putaway, ModuleCode.ORDERPUTAWAY, "android.intent.actiont100.smartdepot.OrderPutawayListAcitivity");
        ModuleBean suitpickinghalf = new ModuleBean(R.string.suitpickinghalf, R.mipmap.lingliaoguozahng, ModuleCode.SUITPICKINGHALF, "android.intent.actiont100.smartdepot.SuitPickingHalfListAcitivity");
        ModuleBean worksupplementlist = new ModuleBean(R.string.title_worksupplement, R.mipmap.ordermaterial, ModuleCode.WORKSUPPLEMENT, "android.intent.actiont100.smartdepot.WorkSupplementListActivity");
        ModuleBean materialReturning = new ModuleBean(R.string.mataerial_returning, R.mipmap.return_of_material, ModuleCode.MATERIALRETURNING, "android.intent.actiont100.smartdepot.MaterialReturnListActivity");
        ModuleBean workorderreturnlistactivity = new ModuleBean(R.string.work_order_return, R.mipmap.work_order_return, ModuleCode.WORKORDERRETURN, "android.intent.actiont100.smartdepot.WorkOrderReturnListActivity");
        ModuleBean driectStorage = new ModuleBean(R.string.direct_storage, R.mipmap.direct_storage, ModuleCode.DIRECTSTORAGE, "android.intent.actiont100.smartdepot.DirectStorageActivity");
        ModuleBean wipstorage = new ModuleBean(R.string.title_completing_store, R.mipmap.complete_storage, ModuleCode.COMPLETINGSTORE, "android.intent.actiont100.smartdepot.WipStorageActivity");
        ModuleBean zputInStoreft = new ModuleBean(R.string.put_in_store, R.mipmap.putaway, ModuleCode.PUTINSTORE, "android.intent.actiont100.smartdepot.ZPutInStoreActivity");
        ModuleBean finishedStorageActivity = new ModuleBean(R.string.finishedstorage, R.mipmap.finishedstorage, ModuleCode.FINISHEDSTORAGE, "android.intent.actiont100.smartdepot.FinishedStorageActivity");
        ModuleBean endproductAllot = new ModuleBean(R.string.endproduct_allot, R.mipmap.endproduct_allot, ModuleCode.ENDPRODUCTALLOT, "android.intent.actiont100.smartdepot.EndProductAllotActivity");
        ModuleBean inbinninglist = new ModuleBean(R.string.title_in_binning, R.mipmap.inbox, ModuleCode.INBINNING, "android.intent.actiont100.smartdepot.InBinningListActivity");
        ModuleBean fqcCheck = new ModuleBean(R.string.fqc_check_pad, R.mipmap.fqc_check_pad, ModuleCode.FQC, "android.intent.actiont100.smartdepot.FQCInspectListActivity");

        produceItems.add(workorder);
        produceItems.add(accordingMaterialActivity);
        produceItems.add(productionleaderlist);
        produceItems.add(suitpicking);
        produceItems.add(orderputaway);
        produceItems.add(worksupplementlist);
        produceItems.add(materialReturning);
        produceItems.add(workorderreturnlistactivity);
        produceItems.add(driectStorage);
        produceItems.add(wipstorage);
        produceItems.add(zputInStoreft);
        produceItems.add(finishedStorageActivity);
        produceItems.add(endproductAllot);
//        produceItems.add(inbinninglist);
        produceItems.add(fqcCheck);

//        ModuleBean printLabelActivity = new ModuleBean(R.string.print_label_flow, R.mipmap.bar_code, ModuleCode.PRINTLABEL, "android.intent.actiont100.smartdepot.PrintLabelFlowActivity");
//        ModuleBean printLabelFinishActivity = new ModuleBean(R.string.print_label_finish, R.mipmap.finished_product_print, ModuleCode.PRINTFINISHLABEL, "android.intent.actiont100.smartdepot.PrintLabelFinishActivity");
//        ModuleBean storetranslockactivity = new ModuleBean(R.string.store_trans_lock, R.mipmap.inventory_lock, ModuleCode.STORETRANSLOCK, "android.intent.actiont100.smartdepot.StoreTransLockActivity");
//        ModuleBean storetransunlockactivity = new ModuleBean(R.string.store_trans_unlock, R.mipmap.inventory_deblocking, ModuleCode.STORETRANSUNLOCK, "android.intent.actiont100.smartdepot.StoreTransUnlockActivity");
        ModuleBean storeallotactivity = new ModuleBean(R.string.nocome_allot, R.mipmap.nocome_alllot, ModuleCode.NOCOMESTOREALLOT, "android.intent.actiont100.smartdepot.StoreAllotActivity");
        ModuleBean miscellaneousoutactivity = new ModuleBean(R.string.miscellaneous_issues_out, R.mipmap.youyuanfaliao, ModuleCode.MISCELLANEOUSISSUESOUT, "android.intent.actiont100.smartdepot.MiscellaneousissuesOutListActivity");
        ModuleBean miscellaneousainctivity = new ModuleBean(R.string.miscellaneous_issues_in, R.mipmap.youyuanshouliao, ModuleCode.MISCELLANEOUSISSUESIN, "android.intent.actiont100.smartdepot.MiscellaneousissuesInListActivity");
        ModuleBean miscellaneousNocomeoutactivity = new ModuleBean(R.string.miscellaneous_nocome_out, R.mipmap.receiptout, ModuleCode.MISCELLANEOUSNOCOMEOUT, "android.intent.actiont100.smartdepot.MiscellaneousNocomeOutActivity");
        ModuleBean miscellaneousNocomeinctivity = new ModuleBean(R.string.miscellaneous_nocome_in, R.mipmap.shouliao, ModuleCode.MISCELLANEOUSNOCOMEIN, "android.intent.actiont100.smartdepot.MiscellaneousNocomeInActivity");
        ModuleBean storeQueryActivity = new ModuleBean(R.string.store_query, R.mipmap.store_query, ModuleCode.STOREQUERY, "android.intent.actiont100.smartdepot.StoreQueryActivity");
        ModuleBean movestoreactivity = new ModuleBean(R.string.movestore, R.mipmap.movestore, ModuleCode.MOVESTORE, "android.intent.actiont100.smartdepot.MoveStoreActivity");
        ModuleBean stockcheck = new ModuleBean(R.string.store_check, R.mipmap.kucunpandian, ModuleCode.STORECHECK, "android.intent.actiont100.smartdepot.StockCheckListActivity");
        ModuleBean postallocateactivity = new ModuleBean(R.string.title_post_allocate, R.mipmap.allot_post, ModuleCode.POSTALLOCATE, "android.intent.actiont100.smartdepot.PostAllocateActivity");
        ModuleBean printLabelFillActivity = new ModuleBean(R.string.print_label, R.mipmap.finished_product_print, ModuleCode.LABLEPRINTING, "android.intent.actiont100.smartdepot.PrintLabelFillActivity");
        ModuleBean productbinning = new ModuleBean(R.string.product_binning, R.mipmap.encasement, ModuleCode.PRODUCTBINNING, "android.intent.actiont100.smartdepot.ProductBinningActivity");
        ModuleBean productoutbox = new ModuleBean(R.string.product_outbox, R.mipmap.outbox, ModuleCode.PRODUCTOUTBOX, "android.intent.actiont100.smartdepot.ProductOutBoxActivity");
        storageItems.add(storeallotactivity);
        storageItems.add(miscellaneousoutactivity);
        storageItems.add(miscellaneousainctivity);
        storageItems.add(miscellaneousNocomeoutactivity);
        storageItems.add(miscellaneousNocomeinctivity);
        storageItems.add(storeQueryActivity);
        storageItems.add(movestoreactivity);
        storageItems.add(stockcheck);
        storageItems.add(postallocateactivity);
        storageItems.add(printLabelFillActivity);
//        storageItems.add(productbinning);
//        storageItems.add(productoutbox);

        ModuleBean scanOutStoreActivity = new ModuleBean(R.string.scan_out_store, R.mipmap.scan_shipment, ModuleCode.SCANOUTSTORE, "android.intent.actiont100.smartdepot.ScanOutStoreListActivity");
//        ModuleBean tranceProductActivity = new ModuleBean(R.string.trace_product_quality, R.mipmap.quality_retrospect, ModuleCode.TRANSPRODUCTQUALITY, "android.intent.actiont100.smartdepot.TraceProductActivity");
        ModuleBean saleoutletactivity = new ModuleBean(R.string.saleoutlet, R.mipmap.saleoutlet, ModuleCode.SALEOUTLET, "android.intent.actiont100.smartdepot.SaleOutletListActivity");
        ModuleBean pickupshipment = new ModuleBean(R.string.title_pickupshipment, R.mipmap.pickup_shipment, ModuleCode.PICKUPSHIPMENT, "android.intent.actiont100.smartdepot.PickUpShipmentListActivity");
        ModuleBean saleReturnActivity = new ModuleBean(R.string.title_sale_return, R.mipmap.ntsale_return, ModuleCode.SALERETURN, "android.intent.actiont100.smartdepot.SaleReturnActivity");
        ModuleBean oqc = new ModuleBean(R.string.oqc_check_pad, R.mipmap.ntsale_return, ModuleCode.OQC, "android.intent.actiont100.smartdepot.OQCListActivity");


//        salesItems.add(scanOutStoreActivity);
        salesItems.add(saleoutletactivity);
        salesItems.add(pickupshipment);
        salesItems.add(saleReturnActivity);
        salesItems.add(oqc);

        ModuleBean palletreport = new ModuleBean(R.string.title_pallet_report, R.mipmap.pallet_report, ModuleCode.ORDERDAILYWORK, "android.intent.actiont100.smartdepot.ProcessReportActivity");
        dailyworkItems.add(palletreport);

        mainLogic.initModule(activity, powerItems, purchaseItems, produceItems, storageItems, salesItems, dailyworkItems, boardItems);
        showTitle();

    }


    /**
     * 显示标题栏
     */
    public void showTitle() {
        mainLogic.showTitle(totalModes, titles);
        initDatas();
        mainLogic.setCustomTab(context, tablayout, titles);
        mainLogic.initListener(context, points, viewPager, tablayout);
    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean speechInput = (boolean) SharedPreferencesUtils.get(activity, SharePreKey.SPEECH_INPUT, true);
        if (speechInput) {
            voiceGuide.setVisibility(View.VISIBLE);
        } else {
            voiceGuide.setVisibility(View.GONE);
        }
    }

    /**
     * 用户信息界面跳转
     */
    @OnClick(R.id.tv_title_person_name)
    public void showUserInfo() {
        ActivityManagerUtils.startActivity(activity, UserInfoActivity.class);
    }

    private void initDatas() {
        points = new ArrayList<>();
        mainLogic.setPonit(context, titles, points, linearLayoutPoints);

        if (purchaseItems.size() > 0) {
            fgs.add(MainPurchaseFragment.getInstance(purchaseItems,purchasemaps));
        }
        if (produceItems.size() > 0) {
            fgs.add(OtherFragment.getInstance(produceItems,producemaps));
        }
        if (storageItems.size() > 0) {
            fgs.add(OtherFragment.getInstance(storageItems,storagemaps));
        }
        if (salesItems.size() > 0) {
            fgs.add(OtherFragment.getInstance(salesItems,salesmaps));
        }
        if (dailyworkItems.size() > 0) {
            fgs.add(OtherFragment.getInstance(dailyworkItems,salesmaps));
        }

        viewPager.setAdapter(new MyFragmentAdapter(getSupportFragmentManager()));
        tablayout.setupWithViewPager(viewPager);
    }

    @Override
    public String moduleCode() {
        module = ModuleCode.OTHER;
        return module;
    }

    class MyFragmentAdapter extends FragmentPagerAdapter {

        public MyFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fgs.get(position);
        }

        @Override
        public int getCount() {
            return titles.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                ToastUtils.showToastByString(context, this.getResources().getString(R.string.app_exit));
                exitTime = System.currentTimeMillis();
            } else {
                BaseApplication.getInstance().destroyReceiver();
                ActivityManagerUtils.finishAllActivity();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
