package digiwin.smartdepott100.module.bean.dailywork;

import java.io.Serializable;

/**
 * @author 赵浩然
 * @module 获取员工姓名实体类
 * @date 2017/3/15
 */

public class WorkerPerson implements Serializable {

    /**
     * 姓名
     */
    private String employee_name;
    /**
     * 编号
     */
    private String employee_no;

    /**
     * app展示
     */
    private String showing;


    public String getEmployee_name() {
        return employee_name;
    }

    public void setEmployee_name(String employee_name) {
        this.employee_name = employee_name;
    }

    public String getEmployee_no() {
        return employee_no;
    }

    public void setEmployee_no(String employee_no) {
        this.employee_no = employee_no;
    }

    public String getShowing() {
        return showing;
    }

    public void setShowing(String showing) {
        this.showing = showing;
    }
}
