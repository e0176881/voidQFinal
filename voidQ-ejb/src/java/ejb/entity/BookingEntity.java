/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import util.enumeration.BookingStatus;

/**
 *
 * @author mingxuan
 */
@Entity
public class BookingEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingId;
    
    @Column(nullable = false)
    @NotNull
    @Enumerated(EnumType.STRING)
    private BookingStatus status;
    
    @Column(nullable = false, length = 32)
    @NotNull
    @Size(max = 32)
    private String visitReason;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    @NotNull
    private Date transactionDateTime;

    @OneToOne
    private TransactionEntity transactionEntity;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private ClinicEntity clinicEntity;

    @ManyToOne(optional = true)
    @JoinColumn(nullable = true)
    private DoctorEntity doctorEntity;
    
    @ManyToOne(optional = true)
    @JoinColumn(nullable = true)
    private NurseEntity nurseEntity;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private PatientEntity patientEntity;

    public BookingEntity() {
    }

    public BookingEntity(BookingStatus status, Date transactionDateTime, ClinicEntity clinicEntity, PatientEntity patientEntity, String visitReason) {
        this.status = status;
        this.transactionDateTime = transactionDateTime;
        this.clinicEntity = clinicEntity;
        this.patientEntity = patientEntity;
        this.visitReason = visitReason;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (bookingId != null ? bookingId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the bookingId fields are not set
        if (!(object instanceof BookingEntity)) {
            return false;
        }
        BookingEntity other = (BookingEntity) object;
        if ((this.bookingId == null && other.bookingId != null) || (this.bookingId != null && !this.bookingId.equals(other.bookingId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ejb.entity.BookingEntity[ id=" + bookingId + " ]";
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public Date getTransactionDateTime() {
        return transactionDateTime;
    }

    public void setTransactionDateTime(Date transactionDateTime) {
        this.transactionDateTime = transactionDateTime;
    }

    public TransactionEntity getTransactionEntity() {
        return transactionEntity;
    }

    public void setTransactionEntity(TransactionEntity transactionEntity) {
        this.transactionEntity = transactionEntity;
    }

    public ClinicEntity getClinicEntity() {
        return clinicEntity;
    }

    public void setClinicEntity(ClinicEntity clinicEntity) {
        this.clinicEntity = clinicEntity;
    }

    public DoctorEntity getDoctorEntity() {
        return doctorEntity;
    }

    public void setDoctorEntity(DoctorEntity doctorEntity) {
        this.doctorEntity = doctorEntity;
    }

    public NurseEntity getNurseEntity() {
        return nurseEntity;
    }

    public void setNurseEntity(NurseEntity nurseEntity) {
        this.nurseEntity = nurseEntity;
    }

    public PatientEntity getPatientEntity() {
        return patientEntity;
    }

    public void setPatientEntity(PatientEntity patientEntity) {
        this.patientEntity = patientEntity;
    }

    public String getVisitReason() {
        return visitReason;
    }

    public void setVisitReason(String visitReason) {
        this.visitReason = visitReason;
    }

}
