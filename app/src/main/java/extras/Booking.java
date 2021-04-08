package extras;

import java.io.Serializable;

public class Booking implements Serializable {

    private String serviceName;
    private String serviceDescripcion;
    private String serviceDateTime;

    public String getIdBooking() {
        return idBooking;
    }

    public void setIdBooking(String idBooking) {
        this.idBooking = idBooking;
    }

    private String idBooking;

    public String getIdService() {
        return idService;
    }

    public void setIdService(String idService) {
        this.idService = idService;
    }

    private String idService;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceDescripcion() {
        return serviceDescripcion;
    }

    public void setServiceDescripcion(String serviceDescripcion) {
        this.serviceDescripcion = serviceDescripcion;
    }

    public String getServiceDateTime() {
        return serviceDateTime;
    }

    public void setServiceDateTime(String serviceDateTime) {
        this.serviceDateTime = serviceDateTime;
    }



    public Booking(String serviceName, String serviceDescripcion, String serviceDateTime, String idService, String idBooking) {
        this.serviceName        = serviceName;
        this.serviceDescripcion = serviceDescripcion;
        this.serviceDateTime    = serviceDateTime;
        this.idService          = idService;
        this.idBooking          = idBooking;
    }
}
