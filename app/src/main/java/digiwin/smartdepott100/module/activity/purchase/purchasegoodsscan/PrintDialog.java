package digiwin.smartdepott100.module.activity.purchase.purchasegoodsscan;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import digiwin.library.dialog.CustomDialog;
import digiwin.library.utils.LogUtils;
import digiwin.library.utils.ViewUtils;
import digiwin.smartdepott100.R;

/**
 * @author xiemeng
 * @des
 * @date 2017-10-11 21:09
 */

public class PrintDialog {
    /**
     * 回调接口
     */
    public interface PrinterFlowListener {
        public void bindByDevice(String lableNumber);
    }

    private static CustomDialog dialog;
    private static final String TAG = "PrintDialog";

    /**
     * 标签补打
     */
    public static void showRePrinterDailog(Context context, final PrinterFlowListener listener) {
        try {
            if (context != null) {
                if (dialog != null) {
                    dialog.dismiss();
                    dialog = null;
                }
                final CustomDialog.Builder builder = new CustomDialog.Builder(context)
                        .view(R.layout.dialog_printer_reprint)
                        .style(R.style.CustomDialog);
                builder.addViewOnclick(R.id.tv_sure, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (listener != null) {
//                            dialog.dismiss();
                            //打印张数
                            final String printNumber = builder.getViewText(R.id.et_print_number);
                            listener.bindByDevice(printNumber);
                        }
                    }
                })
                        .cancelTouchout(true).backCancelTouchout(true)
                        .widthpx((int) (ViewUtils.getScreenWidth(context) * 0.8))
                        .heightpx(ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog = builder.build();
                dialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e(TAG, "PrintLableFlowDialog-----Error" + e);
        }
    }

}
