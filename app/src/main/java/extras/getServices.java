package extras;

import java.io.Serializable;

public class getServices implements Serializable {
    private String idService;
    private String serviceName;
    private String serviceHour;
    private String idGym;
    private String serviceDate;
    private String nameGym;


    public String getNameGym() {
        return nameGym;
    }

    public void setNameGym(String nameGym) {
        this.nameGym = nameGym;
    }

    public getServices(String idService, String serviceName, String idGym, String nameGym) {
        this.idService = idService;
        this.serviceName = serviceName;
        this.serviceHour = serviceHour;
        this.serviceDate = serviceDate;
        this.idGym = idGym;
        this.nameGym = nameGym;

    }

    public String getIdService() {
        return idService;
    }

    public void setIdService(String idService) {
        this.idService = idService;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceHour() {
        return serviceHour;
    }

    public void setServiceHour(String serviceHour) {
        this.serviceHour = serviceHour;
    }

    public String getIdGym() {
        return idGym;
    }

    public void setIdGym(String idGym) {
        this.idGym = idGym;
    }

    public String getServiceDate() {
        return serviceDate;
    }

    public void setServiceDate(String serviceDate) {
        this.serviceDate = serviceDate;
    }
};

