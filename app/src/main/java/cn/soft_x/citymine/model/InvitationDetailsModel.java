package cn.soft_x.citymine.model;

import java.util.List;

/**
 * Created by Administrator on 2016-12-09.
 */
public class InvitationDetailsModel extends BaseModel {


    /**
     * list : [{"id":"de43f3325a014bfd9af5c671153c33ee","dhsDanjia":100,"danjia":20.59,"jsms":1122,"cgid":"1a5faae2459b4ab9bbc8a3ed6ad7301a","pzname":"电视机","cgbm":"CG161216145","ggname":"20寸"}]
     */

    private List<ListBean> list;

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public List<ListBean> getList() {
        return list;
    }

    public static class ListBean {
        private boolean isChecked = false;

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }

        /**
         * id : de43f3325a014bfd9af5c671153c33ee
         * dhsDanjia : 100
         * danjia : 20.59
         * jsms : 1122
         * cgid : 1a5faae2459b4ab9bbc8a3ed6ad7301a
         * pzname : 电视机
         * cgbm : CG161216145
         * ggname : 20寸
         */

        private String id;
        private int dhsDanjia;
        private double danjia;
        private int jsms;
        private String cgid;
        private String pzname;
        private String cgbm;
        private String ggname;

        public void setId(String id) {
            this.id = id;
        }

        public void setDhsDanjia(int dhsDanjia) {
            this.dhsDanjia = dhsDanjia;
        }

        public void setDanjia(double danjia) {
            this.danjia = danjia;
        }

        public void setJsms(int jsms) {
            this.jsms = jsms;
        }

        public void setCgid(String cgid) {
            this.cgid = cgid;
        }

        public void setPzname(String pzname) {
            this.pzname = pzname;
        }

        public void setCgbm(String cgbm) {
            this.cgbm = cgbm;
        }

        public void setGgname(String ggname) {
            this.ggname = ggname;
        }

        public String getId() {
            return id;
        }

        public int getDhsDanjia() {
            return dhsDanjia;
        }

        public double getDanjia() {
            return danjia;
        }

        public int getJsms() {
            return jsms;
        }

        public String getCgid() {
            return cgid;
        }

        public String getPzname() {
            return pzname;
        }

        public String getCgbm() {
            return cgbm;
        }

        public String getGgname() {
            return ggname;
        }
    }
}
