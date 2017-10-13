package digiwin.smartdepott100.main.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.Unbinder;
import digiwin.library.utils.ActivityManagerUtils;
import digiwin.library.utils.LogUtils;
import digiwin.library.utils.StringUtils;
import digiwin.smartdepott100.R;
import digiwin.smartdepott100.core.appcontants.ModuleCode;
import digiwin.smartdepott100.core.base.BaseFragment;
import digiwin.smartdepott100.login.loginlogic.LoginLogic;
import digiwin.smartdepott100.main.CustomRecyclerAdapter;
import digiwin.smartdepott100.main.bean.ModuleBean;

/**
 * @author xiemeng
 * @des 采购
 * @date 2017-10-12
 */
public class MainPurchaseFragment extends BaseMainFg {

    @BindView(R.id.ry_list1)
    RecyclerView ryList1;
    @BindView(R.id.ry_list2)
    RecyclerView ryList2;
    @BindView(R.id.ry_list3)
    RecyclerView ryList3;
    @BindView(R.id.ry_list4)
    RecyclerView ryList4;


    private List<ModuleBean> beanLists;
    private CustomRecyclerAdapter adapter1;
    private CustomRecyclerAdapter adapter2;
    private CustomRecyclerAdapter adapter3;
    private CustomRecyclerAdapter adapter4;

    private Map<String, List<ModuleBean>> maps;

    private static MainPurchaseFragment instance;

    public static MainPurchaseFragment getInstance(List<ModuleBean> beans, Map<String,List<ModuleBean>> maps) {
        instance=new MainPurchaseFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("beans", (Serializable) beans);
        bundle.putSerializable("details", (Serializable) maps);
        instance.setArguments(bundle);
        return instance;
    }



    @Override
    protected int bindLayoutId() {
        beanLists = (List<ModuleBean>) getArguments().getSerializable("beans");
        maps = (Map<String, List<ModuleBean>>) getArguments().getSerializable("details");
        return R.layout.fg_mainpurchase;
    }

    @Override
    protected void doBusiness() {
        adapter1 = new CustomRecyclerAdapter(maps.get("1"), context);
        adapter2 = new CustomRecyclerAdapter(maps.get("2"), context);
        adapter3 = new CustomRecyclerAdapter(maps.get("3"), context);
        adapter4 = new CustomRecyclerAdapter(maps.get("4"), context);


        GridLayoutManager manager1 = new GridLayoutManager(context, 4);
        ryList1.setLayoutManager(manager1);
        ryList1.setAdapter(adapter1);


        GridLayoutManager manager2 = new GridLayoutManager(context, 4);
        ryList2.setLayoutManager(manager2);
        ryList2.setAdapter(adapter2);


        GridLayoutManager manager3 = new GridLayoutManager(context, 4);
        ryList3.setLayoutManager(manager3);
        ryList3.setAdapter(adapter3);


        GridLayoutManager manager4 = new GridLayoutManager(context, 4);
        ryList4.setLayoutManager(manager4);
        ryList4.setAdapter(adapter4);

        initRecyclerViewListener();
    }


    private void initRecyclerViewListener() {
        adapter1.onItemClickListener(new CustomRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                try {
                    String ware = LoginLogic.getWare();
                    if (!StringUtils.isBlank(ware) ||
                            beanLists.get(position).getId().equals(ModuleCode.PROCESSREPORTING)
                            || beanLists.get(position).getId().equals(ModuleCode.PROCEDUCECHECK)
                            || beanLists.get(position).getId().equals(ModuleCode.PROCEDUCEMOVE)) {
                        ActivityManagerUtils.startActivity(activity, beanLists.get(position).getIntent());
                    } else {
                        showFailedDialog(R.string.system_warehouse_null);
                    }
                } catch (Exception e) {
                    showFailedDialog(R.string.toerror);
                }
            }
        });

        adapter2.onItemClickListener(new CustomRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                try {
                    String ware = LoginLogic.getWare();
                    if (!StringUtils.isBlank(ware) ||
                            beanLists.get(position).getId().equals(ModuleCode.PROCESSREPORTING)
                            || beanLists.get(position).getId().equals(ModuleCode.PROCEDUCECHECK)
                            || beanLists.get(position).getId().equals(ModuleCode.PROCEDUCEMOVE)) {
                        ActivityManagerUtils.startActivity(activity, beanLists.get(position).getIntent());
                    } else {
                        showFailedDialog(R.string.system_warehouse_null);
                    }
                } catch (Exception e) {
                    showFailedDialog(R.string.toerror);
                }
            }
        });

        adapter3.onItemClickListener(new CustomRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                try {
                    String ware = LoginLogic.getWare();
                    if (!StringUtils.isBlank(ware) ||
                            beanLists.get(position).getId().equals(ModuleCode.PROCESSREPORTING)
                            || beanLists.get(position).getId().equals(ModuleCode.PROCEDUCECHECK)
                            || beanLists.get(position).getId().equals(ModuleCode.PROCEDUCEMOVE)) {
                        ActivityManagerUtils.startActivity(activity, beanLists.get(position).getIntent());
                    } else {
                        showFailedDialog(R.string.system_warehouse_null);
                    }
                } catch (Exception e) {
                    showFailedDialog(R.string.toerror);
                }
            }
        });
        adapter4.onItemClickListener(new CustomRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                try {
                    String ware = LoginLogic.getWare();
                    if (!StringUtils.isBlank(ware) ||
                            beanLists.get(position).getId().equals(ModuleCode.PROCESSREPORTING)
                            || beanLists.get(position).getId().equals(ModuleCode.PROCEDUCECHECK)
                            || beanLists.get(position).getId().equals(ModuleCode.PROCEDUCEMOVE)) {
                        ActivityManagerUtils.startActivity(activity, beanLists.get(position).getIntent());
                    } else {
                        showFailedDialog(R.string.system_warehouse_null);
                    }
                } catch (Exception e) {
                    showFailedDialog(R.string.toerror);
                }
            }
        });
    }

}
