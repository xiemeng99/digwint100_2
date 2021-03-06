package digiwin.smartdepott100.main.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
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
 * Created by ChangquanSun
 * 2017/1/5
 */

public class OtherFragment extends BaseMainFg {


    @BindView(R.id.main_recyclerview)
    RecyclerView recyclerView;

    private List<ModuleBean> beanLists;
    private CustomRecyclerAdapter adapter;

    private static OtherFragment instance;

    public static OtherFragment getInstance(List<ModuleBean> beans, Map<String,List<ModuleBean>> maps) {
        instance=new OtherFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("beans", (Serializable) beans);
        bundle.putSerializable("details", (Serializable) maps);
        instance.setArguments(bundle);
        return instance;
    }

    @Override
    protected int bindLayoutId() {
        beanLists= (List<ModuleBean>) getArguments().getSerializable("beans");
        return R.layout.base_fragment;
    }

    @Override
    protected void doBusiness() {
        adapter=new CustomRecyclerAdapter(beanLists,context);
        GridLayoutManager manager=new GridLayoutManager(context,3);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        initRecyclerViewListener();
    }

    private void initRecyclerViewListener() {
        adapter.onItemClickListener(new CustomRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                try {
                    String ware = LoginLogic.getWare();
                    if(!StringUtils.isBlank(ware) ||
                            beanLists.get(position).getId().equals(ModuleCode.PROCESSREPORTING)
                            ||beanLists.get(position).getId().equals(ModuleCode.PROCEDUCECHECK)
                            ||beanLists.get(position).getId().equals(ModuleCode.PROCEDUCEMOVE)){
                        ActivityManagerUtils.startActivity(activity,beanLists.get(position).getIntent());
                    }else{
                        showFailedDialog(R.string.system_warehouse_null);
                    }
                }catch (Exception e){
                    showFailedDialog(R.string.toerror);
                }
            }
        });
    }


}
