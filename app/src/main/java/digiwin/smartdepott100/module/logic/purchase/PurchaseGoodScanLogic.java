package digiwin.smartdepott100.module.logic.purchase;

import android.content.Context;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import digiwin.library.utils.ObjectAndMapUtils;
import digiwin.smartdepott100.R;
import digiwin.smartdepott100.core.appcontants.AddressContants;
import digiwin.smartdepott100.core.appcontants.ReqTypeName;
import digiwin.smartdepott100.core.json.JsonReqForERP;
import digiwin.smartdepott100.core.net.IRequestCallbackImp;
import digiwin.smartdepott100.core.net.OkhttpRequest;
import digiwin.smartdepott100.module.bean.common.ClickItemPutBean;
import digiwin.smartdepott100.module.bean.common.FilterBean;
import digiwin.smartdepott100.module.bean.common.FilterResultOrderBean;
import digiwin.smartdepott100.module.bean.common.ListSumBean;
import digiwin.smartdepott100.module.bean.purchase.PurchaseGoodsScanBackBean;
import digiwin.smartdepott100.module.logic.common.CommonLogic;
import digiwin.library.json.JsonResp;
import digiwin.library.utils.LogUtils;
import digiwin.library.utils.ThreadPoolManager;

/**
 * @author xiemeng
 * @des 快速收货
 * @date 2017-10-11
 */
public class PurchaseGoodScanLogic extends CommonLogic {
    private static PurchaseGoodScanLogic logic;

    private String TAG = "PurchaseGoodScanLogic";

    protected PurchaseGoodScanLogic(Context context, String module, String timestamp) {
        super(context, module, timestamp);
    }

    public static PurchaseGoodScanLogic getInstance(Context context, String module, String timestamp) {
        logic = new PurchaseGoodScanLogic(context, module, timestamp);
        return logic;
    }

    /**
     * 采购收货扫描获取列表数据
     *
     * @param filterBean
     */
    public void getPGSListData(final FilterBean filterBean, final GetDataListListener listener) {
        ThreadPoolManager.getInstance().executeTask(new Runnable() {
            @Override
            public void run() {
                try {
                    String createJson = JsonReqForERP.objCreateJson(mModule, "als.a001.list.get", mTimestamp, filterBean);
                    OkhttpRequest.getInstance(mContext).post(createJson, new IRequestCallbackImp() {
                        @Override
                        public void onResponse(String s) {
                            String error = mContext.getString(R.string.unknow_error);
                            if (null != s) {
                                if (ReqTypeName.SUCCCESSCODE.equals(JsonResp.getCode(s))) {
                                    List<FilterResultOrderBean> showBeanList = JsonResp.getParaDatas(s, "list", FilterResultOrderBean.class);
                                    listener.onSuccess(showBeanList);
                                    return;
                                } else {
                                    error = JsonResp.getDescription(s);
                                }
                            }
                            listener.onFailed(error);
                        }
                    });
                } catch (Exception e) {
                    listener.onFailed(mContext.getString(R.string.unknow_error));
                    LogUtils.e(TAG, "getSum--->" + e);
                }
            }
        }, null);
    }

    /**
     * 采购收货扫描 获取汇总数据
     *
     * @param clickItemPutBean
     * @param listener
     */
    public void getPGSSumData(final ClickItemPutBean clickItemPutBean, final GetZSumListener listener) {
        ThreadPoolManager.getInstance().executeTask(new Runnable() {
            @Override
            public void run() {
                try {
                    String createJson = JsonReqForERP.objCreateJson(mModule, "als.a001.list.detail.get", mTimestamp, clickItemPutBean);
                    OkhttpRequest.getInstance(mContext).post(createJson, new IRequestCallbackImp() {
                        @Override
                        public void onResponse(String string) {
                            String error = mContext.getString(R.string.unknow_error);
                            if (null != string) {
                                if (ReqTypeName.SUCCCESSCODE.equals(JsonResp.getCode(string))) {
                                    List<ListSumBean> showBeanList = JsonResp.getParaDatas(string, "list_detail", ListSumBean.class);
                                    listener.onSuccess(showBeanList);
                                    return;
                                } else {
                                    error = JsonResp.getDescription(string);
                                }
                            }
                            listener.onFailed(error);
                        }
                    });
                } catch (Exception e) {
                    listener.onFailed(mContext.getString(R.string.unknow_error));
                    LogUtils.e(TAG, "getSum--->" + e);
                }
            }
        }, null);
    }

    /**
     * 提交
     */
    public interface CommitPurchaseListener {
        void onSuccess(String docNo,List<PurchaseGoodsScanBackBean> scanBackBeen);

        void onFailed(String msg);
    }


    /**
     * 提交
     *
     * @param map map可以直接为空
     */
    public void commitPGSData(final List<ListSumBean> sumShowBeanList, final CommitPurchaseListener listener) {
        ThreadPoolManager.getInstance().executeTask(new Runnable() {
            @Override
            public void run() {
                try {
                    final List<Map<String, String>> listMap = ObjectAndMapUtils.getListMap(sumShowBeanList);
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("data", listMap);
                    String createJson = JsonReqForERP.dataCreateJson(mModule, "als.a001.submit", mTimestamp, map);
                    OkhttpRequest.getInstance(mContext).post(createJson, new IRequestCallbackImp() {
                        @Override
                        public void onResponse(String string) {
                            String error = mContext.getString(R.string.unknow_error);
                            if (null != string) {
                                if (ReqTypeName.SUCCCESSCODE.equals(JsonResp.getCode(string))) {
                                    String docNo = JsonResp.getParaString(string, AddressContants.DOC_NO);
                                    List<PurchaseGoodsScanBackBean> scanBackBeen = JsonResp.getParaDatas(string, "data", PurchaseGoodsScanBackBean.class);
                                    listener.onSuccess(docNo,scanBackBeen);
                                    return;
                                } else {
                                    error = JsonResp.getDescription(string);
                                }
                            }
                            listener.onFailed(error);
                        }
                    });
                } catch (Exception e) {
                    listener.onFailed(mContext.getString(R.string.unknow_error));
                    LogUtils.e(TAG, "commitPGSData--->" + e);
                }
            }
        }, null);
    }
}
