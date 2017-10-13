package digiwin.smartdepott100.main.logic;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import digiwin.library.utils.LogUtils;
import digiwin.library.utils.ViewUtils;
import digiwin.smartdepott100.R;
import digiwin.smartdepott100.main.activity.MainActivity2;
import digiwin.smartdepott100.main.bean.ModuleBean;
import digiwin.smartdepott100.main.bean.TotalMode;

import static android.util.TypedValue.applyDimension;


/**
 * Created by ChangquanSun
 * 2017/2/22
 */

public class MainLogic2 {
    private final String TAG = "MainLogic";
    private int screenWidth;
    private int titleValue;
    private int oldIndex;
    private MainActivity2 activity;
    /**
     * 采购管理
     */
    private List<ModuleBean> purchaseItems;
    /**
     * 生产管理
     */
    private List<ModuleBean> produceItems;
    /**
     * 库存管理
     */
    private List<ModuleBean> storageItems;
    /**
     * 销售管理
     */
    private List<ModuleBean> salesItems;

    /**
     * 报工管理
     */
    private List<ModuleBean> dailyworkItems;
    /**
     * 看板管理
     */
    private List<ModuleBean> boardItems;
    /**
     * 用户权限模块
     */
    private List<String> powerItems;

    public MainLogic2(Context context) {
        activity = (MainActivity2) context;
    }

    public static List<ModuleBean> ModuleList = new ArrayList<>();

    /**
     * 初始化各个模块
     *
     * @param powerItems     权限数据源
     * @param purchaseItems  采购管理数据源
     * @param produceItems   生产管理数据源
     * @param storageItems   库存管理数据源
     * @param salesItems     销售管理数据源
     * @param dailyworkItems 报工管理数据源
     */
    public void initModule(Context context, List<String> powerItems, List<ModuleBean> purchaseItems,
                           List<ModuleBean> produceItems, List<ModuleBean> storageItems
            , List<ModuleBean> salesItems, List<ModuleBean> dailyworkItems, List<ModuleBean> boardItems) {
        this.powerItems = powerItems;
        this.purchaseItems = purchaseItems;
        this.produceItems = produceItems;
        this.storageItems = storageItems;
        this.salesItems = salesItems;
        this.dailyworkItems = dailyworkItems;
        this.boardItems = boardItems;

        // TODO: 2017/3/14 暂时屏蔽测试用
//        showDetailModule();
        ModuleList.addAll(purchaseItems);
        ModuleList.addAll(produceItems);
        ModuleList.addAll(storageItems);
        ModuleList.addAll(salesItems);
        ModuleList.addAll(dailyworkItems);
        ModuleList.addAll(boardItems);
    }

    /**
     * 判断权限小模块
     */
    private void showDetailModule() {
        List<ModuleBean> tempPurchaseItems = new ArrayList<ModuleBean>();
        for (int i = 0; i < purchaseItems.size(); i++) {
            for (int j = 0; j < powerItems.size(); j++) {
                if (purchaseItems.get(i).getId().equals(powerItems.get(j))) {
                    tempPurchaseItems.add(purchaseItems.get(i));
                }
            }
        }
        purchaseItems = tempPurchaseItems;

        List<ModuleBean> tempProduceItems = new ArrayList<ModuleBean>();
        for (int i = 0; i < produceItems.size(); i++) {
            for (int j = 0; j < powerItems.size(); j++) {
                if (produceItems.get(i).getId().equals(powerItems.get(j))) {
                    tempProduceItems.add(produceItems.get(i));
                }
            }
        }
        produceItems = tempProduceItems;

        List<ModuleBean> tempStorageItems = new ArrayList<ModuleBean>();
        for (int i = 0; i < storageItems.size(); i++) {
            for (int j = 0; j < powerItems.size(); j++) {
                if (storageItems.get(i).getId().equals(powerItems.get(j))) {
                    tempStorageItems.add(storageItems.get(i));
                }
            }
        }
        storageItems = tempStorageItems;
        List<ModuleBean> tempSalesItems = new ArrayList<ModuleBean>();
        for (int i = 0; i < salesItems.size(); i++) {
            for (int j = 0; j < powerItems.size(); j++) {
                if (salesItems.get(i).getId().equals(powerItems.get(j))) {
                    tempSalesItems.add(salesItems.get(i));
                }
            }
        }
        salesItems = tempSalesItems;


        List<ModuleBean> tempDailyworkItems = new ArrayList<ModuleBean>();
        for (int i = 0; i < dailyworkItems.size(); i++) {
            for (int j = 0; j < powerItems.size(); j++) {
                if (dailyworkItems.get(i).getId().equals(powerItems.get(j))) {
                    tempDailyworkItems.add(dailyworkItems.get(i));
                }
            }
        }
        dailyworkItems = tempDailyworkItems;

        List<ModuleBean> tempBoardItems = new ArrayList<ModuleBean>();
        for (int i = 0; i < boardItems.size(); i++) {
            for (int j = 0; j < powerItems.size(); j++) {
                if (boardItems.get(i).getId().equals(powerItems.get(j))) {
                    tempBoardItems.add(boardItems.get(i));
                }
            }
        }
        boardItems = tempBoardItems;

    }

    /**
     * 显示标题栏
     */
    public void showTitle(List<TotalMode> totalModes, List<String> titles) {
        try {
            //添加采购管理
            if (purchaseItems.size() > 0 && purchaseItems != null) {
                TotalMode totalMode = new TotalMode(activity.getString(R.string.purchasemanger), purchaseItems);
                totalModes.add(totalMode);
            }
//            //添加生产管理
            if (produceItems.size() > 0 && produceItems != null) {
                TotalMode totalMode = new TotalMode(activity.getString(R.string.producemanager), produceItems);
                totalModes.add(totalMode);
            }
            //添加库存管理
            if (storageItems.size() > 0 && storageItems != null) {
                TotalMode totalMode = new TotalMode(activity.getString(R.string.storagemanager), storageItems);
                totalModes.add(totalMode);
            }
//            //销售管理
            if (salesItems.size() > 0 && salesItems != null) {
                TotalMode totalMode = new TotalMode(activity.getString(R.string.salesmanager), salesItems);
                totalModes.add(totalMode);
            }
//            //报工管理
            if (dailyworkItems.size() > 0 && dailyworkItems != null) {
                TotalMode totalMode = new TotalMode(activity.getString(R.string.dailyworkmanager), dailyworkItems);
                totalModes.add(totalMode);
            }
            //看板管理
            if (boardItems.size() > 0 && boardItems != null) {
                TotalMode totalMode = new TotalMode(activity.getString(R.string.boardmanager), boardItems);
                totalModes.add(totalMode);
            }

            for (TotalMode mode : totalModes) {
                titles.add(mode.name);
            }

        } catch (Exception e) {
            LogUtils.e(TAG, "MainActivity--showTitle-----error" + e);
        }
    }

    public void setCustomTab(Context context, TabLayout tablayout, List<String> titles) {
        //设置tab布局样式
        for (int i = 0; i < tablayout.getTabCount(); i++) {
            TabLayout.Tab tabAt = tablayout.getTabAt(i);
            if (tabAt != null) {
                tabAt.setCustomView(setTabStyle(context, i, tablayout, titles));
            }
        }

        if ((titles.size() + 1) * titleValue >= screenWidth) {
            tablayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        } else {
            tablayout.setTabMode(TabLayout.MODE_FIXED);
        }
        tablayout.setTabGravity(TabLayout.GRAVITY_FILL);
    }

    private View setTabStyle(Context context, int i, TabLayout tablayout, List<String> titles) {
        titleValue = ViewUtils.Dp2Px(context, 75);
        screenWidth = ViewUtils.getScreenWidth(context);
        View view = LayoutInflater.from(context).inflate(R.layout.tablayout_item, tablayout, false);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(titleValue, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(params);
        TextView tv = (TextView) view.findViewById(R.id.tablayout_tv);
//        ImageView iv = (ImageView) view.findViewById(R.id.tablayout_im);
        View iv = (View) view.findViewById(R.id.tablayout_im);
        tv.setText(titles.get(i));
        TypedArray a = context.obtainStyledAttributes(new int[]{
                R.attr.Base_color
        });
        if (i == tablayout.getSelectedTabPosition()) {
            tv.setTextColor(a.getColor(0, context.getResources().getColor(R.color.Base_color)));
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            iv.setVisibility(View.VISIBLE);
        } else {
            tv.setTextColor(context.getResources().getColor(R.color.tab_title_unselected_color));
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            iv.setVisibility(View.INVISIBLE);
        }
        return view;
    }

    public void setPonit(Context context, List<String> titles, List<View> points, LinearLayout linearLayoutPoints) {
        int value = (int) applyDimension(TypedValue.COMPLEX_UNIT_SP, 7, context.getResources().getDisplayMetrics());
        for (int i = 0; i < titles.size(); i++) {
            //小点
            View view = new View(context);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(value, value);
            layoutParams.leftMargin = value;
            if (i == 0) {
                view.setBackgroundResource(R.drawable.point_seletor_yes);
            } else {
                view.setBackgroundResource(R.drawable.point_seletor_no);
            }
            view.setLayoutParams(layoutParams);
            linearLayoutPoints.addView(view);
            points.add(view);
        }
    }

    public void initListener(final Context context, final List<View> points, final ViewPager viewPager, TabLayout tablayout) {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                points.get(position).setBackgroundResource(R.drawable.point_seletor_yes);
                points.get(oldIndex).setBackgroundResource(R.drawable.point_seletor_no);
                oldIndex = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        tablayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                TypedArray a = context.obtainStyledAttributes(new int[]{
                        R.attr.Base_color
                });
                ((TextView) tab.getCustomView().findViewById(R.id.tablayout_tv)).
                        setTextColor(a.getColor(0, context.getResources().getColor(R.color.Base_color)));
                ((TextView) tab.getCustomView().findViewById(R.id.tablayout_tv)).
                        setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                ((View) tab.getCustomView().findViewById(R.id.tablayout_im)).setVisibility(View.VISIBLE);
//                ((ImageView) tab.getCustomView().findViewById(R.id.tablayout_im)).setVisibility(View.VISIBLE);
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                ((TextView) tab.getCustomView().findViewById(R.id.tablayout_tv))
                        .setTextColor(context.getResources().getColor(R.color.tab_title_unselected_color));
                ((TextView) tab.getCustomView().findViewById(R.id.tablayout_tv)).
                        setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                ((View) tab.getCustomView().findViewById(R.id.tablayout_im)).setVisibility(View.INVISIBLE);
//                ((ImageView) tab.getCustomView().findViewById(R.id.tablayout_im)).setVisibility(View.INVISIBLE);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }
}
