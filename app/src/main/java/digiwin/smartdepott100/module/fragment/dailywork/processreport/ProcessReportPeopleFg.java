package digiwin.smartdepott100.module.fragment.dailywork.processreport;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.text.method.TextKeyListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yanzhenjie.recyclerview.swipe.Closeable;
import com.yanzhenjie.recyclerview.swipe.OnSwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnFocusChange;
import butterknife.OnTextChanged;
import digiwin.library.dialog.OnDialogClickListener;
import digiwin.library.utils.StringUtils;
import digiwin.library.utils.WeakRefHandler;
import digiwin.pulltorefreshlibrary.recyclerview.DividerItemDecoration;
import digiwin.pulltorefreshlibrary.recyclerviewAdapter.BaseSwipeMenuAdapter;
import digiwin.pulltorefreshlibrary.recyclerviewAdapter.RecyclerViewHolder;
import digiwin.smartdepott100.R;
import digiwin.smartdepott100.core.appcontants.AddressContants;
import digiwin.smartdepott100.core.base.BaseFragment;
import digiwin.smartdepott100.core.modulecommon.ModuleUtils;
import digiwin.smartdepott100.module.activity.dailywork.processreporting.ProcessReportActivity;
import digiwin.smartdepott100.module.bean.dailywork.ProcedureEmployeeBean;
import digiwin.smartdepott100.module.bean.dailywork.WorkerPerson;
import digiwin.smartdepott100.module.logic.dailywok.ProcessReportLogic;

/**
 * @author xiemeng
 * @des 扫描人员
 * @date 2017-10-16 09:51
 */

public class ProcessReportPeopleFg extends BaseFragment {


    @BindView(R.id.ry_list)
    SwipeMenuRecyclerView ryList;
    @BindView(R.id.tv_scan_employee)
    TextView tvScanEmployee;
    @BindView(R.id.et_scan_employee)
    EditText etScanEmployee;
    @BindView(R.id.ll_scan_employee)
    LinearLayout llScanBarcode;
    public List<WorkerPerson> employeeBeen;

    @BindViews({R.id.tv_scan_employee})
    List<TextView> textviews;
    @BindViews({R.id.et_scan_employee})
    List<EditText> editTexts;
    @BindViews({R.id.ll_scan_employee})
    List<View> views;

    @OnFocusChange(R.id.et_scan_employee)
    void etScanBarcodeFocusChange() {
        ModuleUtils.tvChange(activity, tvScanEmployee, textviews);
        ModuleUtils.viewChange(llScanBarcode, views);
        ModuleUtils.etChange(activity, etScanEmployee, editTexts);
    }

    @OnTextChanged(value = R.id.et_scan_employee, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void etScanBarcodeChange(CharSequence s) {
        if (!StringUtils.isBlank(s.toString())) {
            for (int j = 0; j < employeeBeen.size(); j++) {
                if (s.toString().equals(employeeBeen.get(j).getEmployee_no())) {
                    showFailedDialog(R.string.employee_scaned, new OnDialogClickListener() {
                        @Override
                        public void onCallback() {
                            etScanEmployee.setText("");
                        }
                    });
                    return;
                }
            }
            mHandler.removeMessages(EMPLOYEEWHAT);
            mHandler.sendMessageDelayed(mHandler.obtainMessage(EMPLOYEEWHAT, s.toString()), AddressContants.DELAYTIME);
        }
    }

    /**
     * 扫描员工
     */
    private final int EMPLOYEEWHAT = 1001;

    private Handler.Callback mCallback = new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case EMPLOYEEWHAT:
                    HashMap<String, String> map = new HashMap<>();
                    map.put(AddressContants.EMPLOYEENO, etScanEmployee.getText().toString());
                    etScanEmployee.setKeyListener(null);
                    processReportLogic.scanPerson(map, new ProcessReportLogic.ScanPersonListener() {
                        @Override
                        public void onSuccess(WorkerPerson employeebackBean) {
                            etScanEmployee.setKeyListener(new TextKeyListener(TextKeyListener.Capitalize.CHARACTERS, true));
                            ProcedureEmployeeBean employeeBean = new ProcedureEmployeeBean();
                            employeeBean.setEmployee_no(etScanEmployee.getText().toString());
                            employeeBean.setEmployee_name(employeebackBean.getEmployee_name());
                            etScanEmployee.setText("");
                            employeeBeen.add(employeebackBean);
                            showList();
                        }

                        @Override
                        public void onFailed(String error) {
                            etScanEmployee.setKeyListener(new TextKeyListener(TextKeyListener.Capitalize.CHARACTERS, true));
                            showFailedDialog(error, new OnDialogClickListener() {
                                @Override
                                public void onCallback() {
                                    etScanEmployee.setText("");
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

    private BaseSwipeMenuAdapter<WorkerPerson> adapter;
    private ProcessReportActivity pactivity;
    ProcessReportLogic processReportLogic;

    @Override
    protected int bindLayoutId() {
        return R.layout.fg_processreport_people;
    }

    @Override
    protected void doBusiness() {
        init();
    }

    public void init(){
        employeeBeen = new ArrayList<>();
        pactivity = (ProcessReportActivity) activity;
        processReportLogic = ProcessReportLogic.getInstance(context, pactivity.module, pactivity.mTimestamp.toString());
        showList();

    }


    /**
     * 显示数据
     */
    private void showList() {
        adapter = new BaseSwipeMenuAdapter<WorkerPerson>(context, employeeBeen) {
            @Override
            protected int getItemLayout(int viewType) {
                return R.layout.ryitem_processreport_people;
            }

            @Override
            protected void bindData(RecyclerViewHolder holder, int position, WorkerPerson item) {
                holder.setText(R.id.tv_employee_no, item.getEmployee_no());
                holder.setText(R.id.tv_employee_name, item.getEmployee_name());
            }
        };
        ryList.setLayoutManager(new LinearLayoutManager(context));// 布局管理器。
        ryList.setHasFixedSize(true);// 如果Item够简单，高度是确定的，打开FixSize将提高性能。
        ryList.setItemAnimator(new DefaultItemAnimator());// 设置Item默认动画，加也行，不加也行。
        // 为SwipeRecyclerView的Item创建菜单就两句话，不错就是这么简单：
        // 设置菜单Item点击监听。
        ryList.setSwipeMenuItemClickListener(menuItemClickListener);
        // 设置菜单创建器。
        ryList.setSwipeMenuCreator(swipeMenuCreator);
        ryList.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL_LIST));
        ryList.setAdapter(adapter);
    }


    /**
     * 菜单点击监听。
     */
    private OnSwipeMenuItemClickListener menuItemClickListener = new OnSwipeMenuItemClickListener() {
        @Override
        public void onItemClick(Closeable closeable, final int adapterPosition, int menuPosition, int direction) {
            closeable.smoothCloseMenu();// 关闭被点击的菜单。
            // TODO 推荐调用Adapter.notifyItemRemoved(position)，也可以Adapter.notifyDataSetChanged();
            if (menuPosition == 0) {// 删除按钮被点击。
                WorkerPerson employeeBean = employeeBeen.get(adapterPosition);
                employeeBeen.remove(adapterPosition);
                adapter.notifyDataSetChanged();
            }
        }
    };
    /**
     * 菜单创建器。在Item要创建菜单的时候调用。
     */
    private SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
        @Override
        public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
            int width = getResources().getDimensionPixelSize(R.dimen.item_width);
            // MATCH_PARENT 自适应高度，保持和内容一样高；也可以指定菜单具体高度，也可以用WRAP_CONTENT。
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
//            int height = getResources().getDimensionPixelSize(R.dimen.item_height);

            SwipeMenuItem deleteItem = new SwipeMenuItem(activity)
                    .setBackgroundDrawable(R.drawable.swipe_delete)
                    .setText(R.string.delete)// 文字，还可以设置文字颜色，大小等
                    .setTextColor(Color.WHITE)
                    .setWidth(width)
                    .setHeight(height);
            swipeRightMenu.addMenuItem(deleteItem);// 添加一个按钮到右侧侧菜单。
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }
}
