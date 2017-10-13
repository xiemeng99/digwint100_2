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

public abstract  class BaseMainFg extends BaseFragment {

    private static BaseMainFg instance;

    public static BaseMainFg getInstance(List<ModuleBean> beans, Map<String,List<ModuleBean>> maps) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("beans", (Serializable) beans);
        bundle.putSerializable("details", (Serializable) maps);
        instance.setArguments(bundle);
        return instance;
    }


}
