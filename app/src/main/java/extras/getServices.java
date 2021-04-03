package extras;

import java.io.Serializable;

public class getServices implements Serializable {
    private String idService;
    private String serviceName;
    private String idGym;
    private String nameGym;
    private String serviceDes;


    public String getNameGym() {
        return nameGym;
    }

    public void setNameGym(String nameGym) {
        this.nameGym = nameGym;
    }

    public String getServiceDes() {
        return serviceDes;
    }

    public void setServiceDes(String serviceDes) {
        this.serviceDes = serviceDes;
    }

    public getServices(String idService, String serviceName, String idGym, String nameGym, String serviceDes) {
        this.idService = idService;
        this.serviceName = serviceName;
        this.idGym = idGym;
        this.nameGym = nameGym;
        this.serviceDes = serviceDes;

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

    public String getIdGym() {
        return idGym;
    }

    public void setIdGym(String idGym) {
        this.idGym = idGym;
    }


};

