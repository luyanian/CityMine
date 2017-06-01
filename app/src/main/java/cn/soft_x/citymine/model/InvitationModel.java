package cn.soft_x.citymine.model;

import java.util.List;

/**
 * Created by Administrator on 2016-12-09.
 */
public class InvitationModel extends BaseModel {


    /**
     * fblist : [{"FBZID":"ec6c7847143445cfba84df79d4a2c812","GHBT":"111","FBRQ":"2016-12-16","GHID":"e231eda155114a9aa1df21ef5826b5bd"},{"FBZID":"ec6c7847143445cfba84df79d4a2c812","GHBT":"我的商品","FBRQ":"2016-12-16","GHID":"1adb25f4438d455c99bfba982697fc19"},{"FBZID":"ec6c7847143445cfba84df79d4a2c812","GHBT":"33","FBRQ":"2016-12-16","GHID":"777d3f5f21694be2ba93c10b8135dd46"},{"FBZID":"ec6c7847143445cfba84df79d4a2c812","GHBT":"商品五","FBRQ":"2016-12-16","GHID":"d055844be4bf4a1faca087f4939005c3"},{"FBZID":"ec6c7847143445cfba84df79d4a2c812","GHBT":"商品一开类","FBRQ":"2016-12-16","GHID":"46c154f58483493390025d4d659f6ba1"},{"FBZID":"ec6c7847143445cfba84df79d4a2c812","GHBT":"1","FBRQ":"2016-12-16","GHID":"3ee7cc545e084395aefa86b56f11e130"},{"FBZID":"ec6c7847143445cfba84df79d4a2c812","GHBT":"66","FBRQ":"2016-12-16","GHID":"996fde7fb7c548d8ae423f84883459f6"},{"FBZID":"ec6c7847143445cfba84df79d4a2c812","GHBT":"414","FBRQ":"2016-12-16","GHID":"c23c8fa1fd1845aeaf922d918358d746"},{"FBZID":"ec6c7847143445cfba84df79d4a2c812","GHBT":"商品四","FBRQ":"2016-12-16","GHID":"7efe08fd254f4f238c250c41ffaecdbb"},{"FBZID":"ec6c7847143445cfba84df79d4a2c812","GHBT":"商品","FBRQ":"2016-12-16","GHID":"088428ba93a94f1eb1370c430980c4fc"}]
     */

    private List<FblistBean> fblist;

    public void setFblist(List<FblistBean> fblist) {
        this.fblist = fblist;
    }

    public List<FblistBean> getFblist() {
        return fblist;
    }

    public static class FblistBean {
        /**
         * FBZID : ec6c7847143445cfba84df79d4a2c812
         * GHBT : 111
         * FBRQ : 2016-12-16
         * GHID : e231eda155114a9aa1df21ef5826b5bd
         */
        private String FBZID;
        private String GHBT;
        private String FBRQ;
        private String GHID;
        private String SL;
        private String GG;
        private String PZ;
        private String FBZTX;

        public String getFBZTX() {
            return FBZTX;
        }

        public void setFBZTX(String FBZTX) {
            this.FBZTX = FBZTX;
        }

        public String getSL() {
            return SL;
        }

        public void setSL(String SL) {
            this.SL = SL;
        }

        public String getPZ() {
            return PZ;
        }

        public void setPZ(String PZ) {
            this.PZ = PZ;
        }

        public String getGG() {
            return GG;
        }

        public void setGG(String GG) {
            this.GG = GG;
        }

        public void setFBZID(String FBZID) {
            this.FBZID = FBZID;
        }

        public void setGHBT(String GHBT) {
            this.GHBT = GHBT;
        }

        public void setFBRQ(String FBRQ) {
            this.FBRQ = FBRQ;
        }

        public void setGHID(String GHID) {
            this.GHID = GHID;
        }

        public String getFBZID() {
            return FBZID;
        }

        public String getGHBT() {
            return GHBT;
        }

        public String getFBRQ() {
            return FBRQ;
        }

        public String getGHID() {
            return GHID;
        }
    }
}
