package cn.soft_x.citymine.model;

/**
 * Created by Administrator on 2016-11-24.
 */
public class LoginModel extends BaseModel{

    /**
     * YHID : 049ee74567e5488abfda6422785dac42
     */

    private String YHID;
    private String YHNC;

    private String longitude;
    private String latitude;

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getYHNC() {
        return YHNC;
    }

    public void setYHNC(String YHNC) {
        this.YHNC = YHNC;
    }

    public void setYHID(String YHID) {
        this.YHID = YHID;
    }

    public String getYHID() {
        return YHID;
    }

}
