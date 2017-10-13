package digiwin.smartdepott100.main.logic;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import digiwin.library.dialog.CustomDialog;
import digiwin.library.dialog.OnDialogTwoListener;
import digiwin.library.utils.ActivityManagerUtils;
import digiwin.library.utils.LogUtils;
import digiwin.library.utils.ViewUtils;
import digiwin.smartdepott100.R;
import digiwin.smartdepott100.main.activity.SettingActivity;

/**
 * @author xiemeng
 * @des 跳转到设置
 * @date 2017/3/22
 */
public class ToSettingLogic {

    private static CustomDialog dialog;
    private static final String TAG = "ToSettingLogic";


    /**
     * 弹出确定取消对话框
     */
    public static void showToSetdialog(final Activity activity, int title, final OnDialogTwoListener listener) {
        try {
            if (activity != null) {
                CustomDialog.Builder builder = new CustomDialog.Builder(activity);
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                    dialog = null;
                }
                dialog = builder.view(R.layout.dialog_tosetting)
                        .style(R.style.CustomDialog)
                        .cancelTouchout(false)
                        .widthpx((int) (ViewUtils.getScreenWidth(activity) * 0.6))
                        .heightpx(ViewGroup.LayoutParams.WRAP_CONTENT)
                        .setViewText(R.id.tv_title, title)
                        .addViewOnclick(R.id.tv_sure, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ActivityManagerUtils.startActivity(activity, SettingActivity.class);
                                if (null != listener) {
                                    listener.onCallback1();
                                }
                                dismissDialog();

                            }
                        })
                        .addViewOnclick(R.id.tv_cancle, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (null != listener) {
                                    listener.onCallback2();
                                }
                                dismissDialog();
                            }
                        })
                        .backCancelTouchout(false)
                        .build();
                dialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e(TAG, "showChooseAllotDailog-----Error" + e);
        }
    }

    /**
     * 隐藏等待dialog
     */
    public static void dismissDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
    }


}
