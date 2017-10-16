package digiwin.smartdepott100.module.fragment.dailywork.processreport;

import android.os.Handler;
import android.os.Message;
import android.text.method.TextKeyListener;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import butterknife.OnTextChanged;
import digiwin.library.dialog.OnDialogClickListener;
import digiwin.library.dialog.OnDialogTwoListener;
import digiwin.library.utils.StringUtils;
import digiwin.library.utils.WeakRefHandler;
import digiwin.smartdepott100.R;
import digiwin.smartdepott100.core.appcontants.AddressContants;
import digiwin.smartdepott100.core.base.BaseFragment;
import digiwin.smartdepott100.core.modulecommon.ModuleUtils;
import digiwin.smartdepott100.module.activity.dailywork.processreporting.ProcessReportActivity;
import digiwin.smartdepott100.module.bean.dailywork.ProcessReportBean;
import digiwin.smartdepott100.module.logic.dailywok.ProcessReportLogic;

/**
 * @author xiemeng
 * @des
 * @date 2017-10-16 09:30
 */

public class ProcessReportCommitFg extends BaseFragment {


    @BindView(R.id.tv_report_min)
    TextView tvReportMin;
    @BindView(R.id.et_report_min)
    EditText etReportMin;
    @BindView(R.id.ll_report_min)
    LinearLayout llReportMin;
    private ProcessReportActivity pactivity;

    private final int WO_NO = 1001;

    private final int PROCESS_NO = 1002;


    ProcessReportLogic logic;

    /**
     * 工单单号
     */
    @BindView(R.id.et_gongDan_no)
    EditText etGongDanNo;
    @BindView(R.id.tv_gongDan_no_code)
    TextView tvGongDanNoCode;
    @BindView(R.id.ll_gongDan_no)
    LinearLayout llGongDanNo;
    /**
     * 作业号
     */
    @BindView(R.id.et_job_num)
    EditText etJobNum;
    @BindView(R.id.tv_job_num)
    TextView tvJobNum;
    @BindView(R.id.ll_job_num)
    LinearLayout llJobNum;


    /**
     * 良品量
     */
    @BindView(R.id.et_good_amount)
    EditText etGoodAmount;
    @BindView(R.id.tv_good_amount)
    TextView tvGoodAmount;
    @BindView(R.id.ll_good_amount)
    LinearLayout llGoodAmount;

    /**
     * 不良品量
     */
    @BindView(R.id.et_not_good_amount)
    EditText etNotGoodAmount;
    @BindView(R.id.tv_not_good_amount)
    TextView tvNotGoodAmount;
    @BindView(R.id.ll_not_good_amount)
    LinearLayout llNotGoodAmount;


    @BindView(R.id.tv_detail_show)
    TextView tvDetailShow;
    @BindView(R.id.includedetail)
    View includeDetail;

    private String woNoShowing;

    private String jobShowing;

    private ProcessReportBean commitBean;


    @BindViews({R.id.et_gongDan_no, R.id.et_job_num,
            R.id.et_good_amount, R.id.et_not_good_amount, R.id.et_report_min})
    List<EditText> editTexts;
    @BindViews({R.id.tv_gongDan_no_code, R.id.tv_job_num,
            R.id.tv_good_amount, R.id.tv_not_good_amount, R.id.tv_report_min})
    List<TextView> textViews;
    @BindViews({R.id.ll_gongDan_no, R.id.ll_job_num,
            R.id.ll_good_amount, R.id.ll_not_good_amount, R.id.ll_report_min})
    List<View> views;

    @OnFocusChange(R.id.et_gongDan_no)
    void barcodeFocusChanage() {
        ModuleUtils.viewChange(llGongDanNo, views);
        ModuleUtils.etChange(activity, etGongDanNo, editTexts);
        ModuleUtils.tvChange(activity, tvGongDanNoCode, textViews);
    }

    @OnFocusChange(R.id.et_job_num)
    void jobNumFocusChanage() {
        ModuleUtils.viewChange(llJobNum, views);
        ModuleUtils.etChange(activity, etJobNum, editTexts);
        ModuleUtils.tvChange(activity, tvJobNum, textViews);
    }


    @OnFocusChange(R.id.et_good_amount)
    void numFocusChanage() {
        ModuleUtils.viewChange(llGoodAmount, views);
        ModuleUtils.etChange(activity, etGoodAmount, editTexts);
        ModuleUtils.tvChange(activity, tvGoodAmount, textViews);
    }

    @OnFocusChange(R.id.et_not_good_amount)
    void notNumFocusChanage() {
        ModuleUtils.viewChange(llNotGoodAmount, views);
        ModuleUtils.etChange(activity, etNotGoodAmount, editTexts);
        ModuleUtils.tvChange(activity, tvNotGoodAmount, textViews);
    }

    @OnFocusChange(R.id.et_report_min)
    void reprotMinFocusChanage() {
        ModuleUtils.viewChange(llReportMin, views);
        ModuleUtils.etChange(activity, etReportMin, editTexts);
        ModuleUtils.tvChange(activity, tvReportMin, textViews);
    }

    @OnTextChanged(value = R.id.et_gongDan_no, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void gongDanChange(CharSequence s) {
        if (!StringUtils.isBlank(s.toString())) {
            mHandler.removeMessages(WO_NO);
            mHandler.sendMessageDelayed(mHandler.obtainMessage(WO_NO, s.toString().trim()),
                    AddressContants.DELAYTIME);
        }
    }

    @OnTextChanged(value = R.id.et_job_num, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void jobChange(CharSequence s) {
        if (!StringUtils.isBlank(s.toString())) {
            mHandler.removeMessages(PROCESS_NO);
            mHandler.sendMessageDelayed(mHandler.obtainMessage(PROCESS_NO, s.toString().trim()),
                    AddressContants.DELAYTIME);
        }
    }


    @OnClick(R.id.commit)
    void commit() {
        showCommitSureDialog(new OnDialogTwoListener() {
            @Override
            public void onCallback1() {
                if (StringUtils.isBlank(etGongDanNo.getText().toString())) {
                    showFailedDialog(R.string.scan_work_order);
                    return;
                }
                if (StringUtils.isBlank(etJobNum.getText().toString())) {
                    showFailedDialog(R.string.job_num_scan);
                    return;
                }
                if (StringUtils.isBlank(etGoodAmount.getText().toString())) {
                    showFailedDialog(R.string.input_goodnum);
                    return;
                }
                if (StringUtils.isBlank(etNotGoodAmount.getText().toString())) {
                    showFailedDialog(R.string.input_badnum);
                    return;
                }
                if (StringUtils.isBlank(etReportMin.getText().toString())) {
                    showFailedDialog(R.string.input_time);
                    return;
                }
                commitBean.setUndefect_qty(etGoodAmount.getText().toString().trim());
                commitBean.setDefect_qty(etNotGoodAmount.getText().toString().trim());
                float sum = StringUtils.sum(commitBean.getUndefect_qty(), commitBean.getDefect_qty());
                if (StringUtils.sub(String.valueOf(sum), commitBean.getApply_qty()) > 0) {
                    showFailedDialog(getString(R.string.sum_small_apply) + commitBean.getApply_qty());
                    return;
                }
                commitBean.setReport_min(etReportMin.getText().toString().trim());
                showLoadingDialog();
                commitBean.setPeople(pactivity.peopleFg.employeeBeen);
                logic.commitList(commitBean, new ProcessReportLogic.CommitListListener() {
                    @Override
                    public void onSuccess(final String msg) {
                        dismissLoadingDialog();
                        showCommitSuccessDialog(msg, new OnDialogClickListener() {
                            @Override
                            public void onCallback() {
                                pactivity.createNewModuleId(pactivity.module);
                                initData();
                                pactivity.peopleFg.init();

                            }
                        });
                    }

                    @Override
                    public void onFailed(String error) {
                        dismissLoadingDialog();
                        showFailedDialog(error);
                    }
                });
            }

            @Override
            public void onCallback2() {

            }
        });
    }

    private Handler.Callback mCallback = new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case WO_NO:
                    Map<String, String> map = new HashMap<String, String>();
                    map.put(AddressContants.WO_NO, String.valueOf(msg.obj));
                    etGongDanNo.setKeyListener(null);
                    logic.scanCode(map, new ProcessReportLogic.ScanCodeListener() {
                        @Override
                        public void onSuccess(ProcessReportBean data) {
                            etGongDanNo.setKeyListener(new TextKeyListener(TextKeyListener.Capitalize.CHARACTERS, true));
                            etJobNum.setText("");
                            etJobNum.requestFocus();
                            woNoShowing = data.getShowing();
                            commitBean = data;
                            commitBean.setWo_no(etGongDanNo.getText().toString().trim());
                            show();
                        }

                        @Override
                        public void onFailed(String error) {
                            etGongDanNo.setKeyListener(new TextKeyListener(TextKeyListener.Capitalize.CHARACTERS, true));
                            showFailedDialog(error, new OnDialogClickListener() {
                                @Override
                                public void onCallback() {
                                    etGongDanNo.setText("");
                                }
                            });
                        }
                    });
                    break;
                case PROCESS_NO:
                    Map<String, String> map2 = new HashMap<String, String>();
                    if (StringUtils.isBlank(etGongDanNo.getText().toString().trim())) {
                        showFailedDialog(R.string.scan_work_order, new OnDialogClickListener() {
                            @Override
                            public void onCallback() {
                                return;
                            }
                        });
                    }
                    map2.put(AddressContants.WO_NO, commitBean.getWo_no());
                    map2.put(AddressContants.ITEM_NO, commitBean.getItem_no());
                    map2.put("op_no", commitBean.getOp_no());
                    map2.put(AddressContants.PROCESSNO, String.valueOf(msg.obj));
                    etJobNum.setKeyListener(null);
                    logic.scanJob(map2, new ProcessReportLogic.ScanCodeListener() {
                        @Override
                        public void onSuccess(ProcessReportBean data) {
                            etJobNum.setKeyListener(new TextKeyListener(TextKeyListener.Capitalize.CHARACTERS, true));
                            jobShowing = data.getShowing();
                            etGoodAmount.requestFocus();
                            commitBean.setOp_seq(data.getOp_seq());
                            commitBean.setSubop_no(data.getSubop_no());
                            commitBean.setUnit_no(data.getUnit_no());
                            commitBean.setApply_qty(data.getApply_qty());
                            show();

                        }

                        @Override
                        public void onFailed(String error) {
                            etJobNum.setKeyListener(new TextKeyListener(TextKeyListener.Capitalize.CHARACTERS, true));
                            dismissLoadingDialog();
                            showFailedDialog(error, new OnDialogClickListener() {
                                @Override
                                public void onCallback() {
                                    etJobNum.setText("");
                                }
                            });
                        }
                    });
                    break;
            }

            return false;
        }
    };

    private Handler mHandler = new WeakRefHandler(mCallback);


    /**
     * 公共区域展示
     */
    private void show() {
        tvDetailShow.setText(StringUtils.lineChange(woNoShowing + "\\n" + jobShowing));
        if (!StringUtils.isBlank(tvDetailShow.getText().toString())) {
            includeDetail.setVisibility(View.VISIBLE);
        } else {
            includeDetail.setVisibility(View.GONE);
        }
    }

    @Override
    protected void doBusiness() {
        pactivity = (ProcessReportActivity) activity;
        initData();
    }

    private void initData() {
        commitBean = new ProcessReportBean();
        logic = ProcessReportLogic
                .getInstance(activity, pactivity.module, pactivity.mTimestamp.toString());
        etGongDanNo.setText("");
        etJobNum.setText("");
        etGoodAmount.setText("");
        etNotGoodAmount.setText("");
        etReportMin.setText("");
        woNoShowing = "";
        jobShowing = "";
        etGongDanNo.requestFocus();
        show();
    }


    @Override
    protected int bindLayoutId() {
        return R.layout.fg_processreportcommit;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }

}
