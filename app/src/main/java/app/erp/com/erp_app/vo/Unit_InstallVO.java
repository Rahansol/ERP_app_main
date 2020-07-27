package app.erp.com.erp_app.vo;

import java.io.Serializable;

public class Unit_InstallVO implements Serializable {

    public Unit_InstallVO() {
    }

    private String area_code;
    private String sub_area_code;
    private String doc_seq;
    private String item_seq;
    private String item_name;
    private String item_type;
    private String item_value;
    private String position_x;
    private String position_y;
    private String continue_x;
    private String continue_y;

    private String item_group_seq;
    private String item_gruop_name;
    private String item_each_seq;
    private String item_each_name;

    @Override
    public String toString() {
        return "Unit_InstallVO{" +
                "area_code='" + area_code + '\'' +
                ", sub_area_code='" + sub_area_code + '\'' +
                ", doc_seq='" + doc_seq + '\'' +
                ", item_seq='" + item_seq + '\'' +
                ", item_name='" + item_name + '\'' +
                ", item_type='" + item_type + '\'' +
                ", item_value='" + item_value + '\'' +
                ", position_x='" + position_x + '\'' +
                ", position_y='" + position_y + '\'' +
                ", continue_x='" + continue_x + '\'' +
                ", continue_y='" + continue_y + '\'' +
                ", item_group_seq='" + item_group_seq + '\'' +
                ", item_gruop_name='" + item_gruop_name + '\'' +
                ", item_each_seq='" + item_each_seq + '\'' +
                ", item_each_name='" + item_each_name + '\'' +
                '}';
    }

    public String getArea_code() {
        return area_code;
    }

    public void setArea_code(String area_code) {
        this.area_code = area_code;
    }

    public String getSub_area_code() {
        return sub_area_code;
    }

    public void setSub_area_code(String sub_area_code) {
        this.sub_area_code = sub_area_code;
    }

    public String getDoc_seq() {
        return doc_seq;
    }

    public void setDoc_seq(String doc_seq) {
        this.doc_seq = doc_seq;
    }

    public String getItem_seq() {
        return item_seq;
    }

    public void setItem_seq(String item_seq) {
        this.item_seq = item_seq;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_type() {
        return item_type;
    }

    public void setItem_type(String item_type) {
        this.item_type = item_type;
    }

    public String getItem_value() {
        return item_value;
    }

    public void setItem_value(String item_value) {
        this.item_value = item_value;
    }

    public String getPosition_x() {
        return position_x;
    }

    public void setPosition_x(String position_x) {
        this.position_x = position_x;
    }

    public String getPosition_y() {
        return position_y;
    }

    public void setPosition_y(String position_y) {
        this.position_y = position_y;
    }

    public String getContinue_x() {
        return continue_x;
    }

    public void setContinue_x(String continue_x) {
        this.continue_x = continue_x;
    }

    public String getContinue_y() {
        return continue_y;
    }

    public void setContinue_y(String continue_y) {
        this.continue_y = continue_y;
    }

    public String getItem_group_seq() {
        return item_group_seq;
    }

    public void setItem_group_seq(String item_group_seq) {
        this.item_group_seq = item_group_seq;
    }

    public String getItem_gruop_name() {
        return item_gruop_name;
    }

    public void setItem_gruop_name(String item_gruop_name) {
        this.item_gruop_name = item_gruop_name;
    }

    public String getItem_each_seq() {
        return item_each_seq;
    }

    public void setItem_each_seq(String item_each_seq) {
        this.item_each_seq = item_each_seq;
    }

    public String getItem_each_name() {
        return item_each_name;
    }

    public void setItem_each_name(String item_each_name) {
        this.item_each_name = item_each_name;
    }
}
