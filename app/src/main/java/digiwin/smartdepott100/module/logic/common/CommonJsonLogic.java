package digiwin.smartdepott100.module.logic.common;

import android.content.Context;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import digiwin.library.net.IUpdateCallBack;
import digiwin.library.utils.LogUtils;
import digiwin.smartdepott100.core.net.OkhttpRequestJson;
import digiwin.smartdepott100.login.bean.AccoutBean;
import digiwin.smartdepott100.login.loginlogic.LoginLogic;
import digiwin.smartdepott100.module.bean.common.ChoosePicBean;

/**
 * @des  json格式逻辑
 * @date 2017/4/20  
 * @author xiemeng
 */
public class CommonJsonLogic {

    private static final String TAG = "CommonJsonLogic";

    private Context mContext;
    /**
     * 模组名
     */
    private String mModule = "";
    /**
     * 设备号+模组+时间
     */
    private String mTimestamp = "";

    private static CommonJsonLogic logic;

    private CommonJsonLogic(Context context, String module, String timestamp) {
        mTimestamp = timestamp;
        mContext = context.getApplicationContext();
        mModule = module;

    }

    public static CommonJsonLogic getInstance(Context context, String module, String timestamp) {

        return logic = new CommonJsonLogic(context, module, timestamp);
    }



    /**
     * 上传图片
     */
    public interface UpdateImgListener{
        public void onProgressCallBack(long progress, long total);
        public void onSuccess(String msg);
        public void onFailed(long progress, long total);
    }

    /**
     * 上传图片
     * @param filepath
     * @param listener
     */
    public void update(List<ChoosePicBean> filepath, final UpdateImgListener listener){
        HashMap<String, Object> map = new HashMap<>();
        for (int i=0;i<filepath.size();i++){
            String picPath = filepath.get(i).getPicPath();
            File file = new File(picPath);
            map.put("file"+i,file);
        }
        AccoutBean info = LoginLogic.getUserInfo();
        if (null!=info) {
            map.put("username", info.getEmployee_no());
            map.put("plant", info.getEnterprise_no());
        }
        String url="http://180.167.0.42:9018/barcode/api/res/v1/upload/file";
        OkhttpRequestJson.getInstance(mContext).update(url, map, new IUpdateCallBack() {
            @Override
            public void onProgressCallBack(long progress, long total) {
                LogUtils.i(TAG, "onProgressCallBack--->" + progress+"--total"+total);
            }

            @Override
            public void onResponse(String msg) {
                LogUtils.i(TAG, "onResponse--->" + msg);
            }

            @Override
            public void onFailure(Context context, String throwable) {
                LogUtils.i(TAG, "throwable--->" + throwable);
            }
        });
    }
}