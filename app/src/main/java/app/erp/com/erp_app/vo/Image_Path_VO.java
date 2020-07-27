package app.erp.com.erp_app.vo;

public class Image_Path_VO {

    public Image_Path_VO() {
    }

    private String prj_name;
    private String upload_date;
    private String transp_bizr_id;
    private String bus_id;
    private String item_seq;
    private String image_path;

    @Override
    public String toString() {
        return "Image_Path_VO{" +
                "prj_name='" + prj_name + '\'' +
                ", upload_date='" + upload_date + '\'' +
                ", transp_bizr_id='" + transp_bizr_id + '\'' +
                ", bus_id='" + bus_id + '\'' +
                ", item_seq='" + item_seq + '\'' +
                ", image_path='" + image_path + '\'' +
                '}';
    }

    public String getPrj_name() {
        return prj_name;
    }

    public void setPrj_name(String prj_name) {
        this.prj_name = prj_name;
    }

    public String getUpload_date() {
        return upload_date;
    }

    public void setUpload_date(String upload_date) {
        this.upload_date = upload_date;
    }

    public String getTransp_bizr_id() {
        return transp_bizr_id;
    }

    public void setTransp_bizr_id(String transp_bizr_id) {
        this.transp_bizr_id = transp_bizr_id;
    }

    public String getBus_id() {
        return bus_id;
    }

    public void setBus_id(String bus_id) {
        this.bus_id = bus_id;
    }

    public String getItem_seq() {
        return item_seq;
    }

    public void setItem_seq(String item_seq) {
        this.item_seq = item_seq;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }
}
