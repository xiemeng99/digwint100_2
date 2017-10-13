package digiwin.smartdepott100.main.bean;

import java.util.List;
import java.util.Map;

/**
 * Created by ChangquanSun
 * 2017/2/22
 * 大模块对象
 */

public class TotalMode {
    /**
     * 名称
     */
    public String name = null;
    /**
     * 集合
     */
    public List<ModuleBean> list = null;

    /**
     * 集合
     */
    public Map<String,List<ModuleBean>> maps = null;

    public TotalMode(String name, List<ModuleBean> purchaseItems) {
        this.name = name;
        this.list = purchaseItems;
    }

    public TotalMode(String name, List<ModuleBean> purchaseItems, Map<String,List<ModuleBean>> maps) {
        this.name = name;
        this.list = purchaseItems;
        this.maps=maps;
    }
}
