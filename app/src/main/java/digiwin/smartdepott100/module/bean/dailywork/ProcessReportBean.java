package digiwin.smartdepott100.module.bean.dailywork;

import java.io.Serializable;
import java.util.List;

/**
 * @author 赵浩然
 * @module 工序报工实体类
 * @date 2017/3/15
 */

public class ProcessReportBean implements Serializable {


    private String wo_no;

    /**
     * app展示
     */
    private String showing;

    /**
     * #料号
     */
    private String item_no;
    /**
     * #工艺
     */
    private String op_no;
    /**
     * #品名
     */
    private String item_name;
    /**
     * #规格
     */
    private String item_spec;

    /**
     * #作业编号
     */
    private String subop_no;
    /**
     * #作业名称
     */
    private String subop_name;
    /**
     * #作业序
     */
    private String op_seq;
    /**
     * #单位
     */
    private String unit_no;
    /**
     * #在制量
     */
    private String apply_qty;

    /**
     * #良品数量
     */
    private String undefect_qty;
    /**
     * #不良品数量
     */
    private String defect_qty;
    /**
     * 工时
     */
    private String report_min;

    /**
     * 人员
     */
    private List<WorkerPerson> people;

    public String getWo_no() {
        return wo_no;
    }

    public void setWo_no(String wo_no) {
        this.wo_no = wo_no;
    }

    public String getShowing() {
        return showing;
    }

    public void setShowing(String showing) {
        this.showing = showing;
    }

    public String getItem_no() {
        return item_no;
    }

    public void setItem_no(String item_no) {
        this.item_no = item_no;
    }

    public String getOp_no() {
        return op_no;
    }

    public void setOp_no(String op_no) {
        this.op_no = op_no;
    }

    public String getUnit_no() {
        return unit_no;
    }

    public void setUnit_no(String unit_no) {
        this.unit_no = unit_no;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_spec() {
        return item_spec;
    }

    public void setItem_spec(String item_spec) {
        this.item_spec = item_spec;
    }

    public String getSubop_no() {
        return subop_no;
    }

    public void setSubop_no(String subop_no) {
        this.subop_no = subop_no;
    }

    public String getSubop_name() {
        return subop_name;
    }

    public void setSubop_name(String subop_name) {
        this.subop_name = subop_name;
    }

    public String getOp_seq() {
        return op_seq;
    }

    public void setOp_seq(String op_seq) {
        this.op_seq = op_seq;
    }

    public String getApply_qty() {
        return apply_qty;
    }

    public void setApply_qty(String apply_qty) {
        this.apply_qty = apply_qty;
    }

    public String getUndefect_qty() {
        return undefect_qty;
    }

    public void setUndefect_qty(String undefect_qty) {
        this.undefect_qty = undefect_qty;
    }

    public String getDefect_qty() {
        return defect_qty;
    }

    public void setDefect_qty(String defect_qty) {
        this.defect_qty = defect_qty;
    }

    public String getReport_min() {
        return report_min;
    }

    public void setReport_min(String report_min) {
        this.report_min = report_min;
    }

    public List<WorkerPerson> getPeople() {
        return people;
    }

    public void setPeople(List<WorkerPerson> people) {
        this.people = people;
    }
}
