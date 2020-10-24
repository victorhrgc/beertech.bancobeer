package beertech.becks.api.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class PaymentSlip implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "CODE")
    private String code;

    @Column(name = "DUE_DATE")
    private LocalDate dueDate;

    @Column(name = "VALUE")
    private BigDecimal value;

    @Column(name = "FK_USER_ID")
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_USER_ID", insertable = false, updatable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private User user;

    @Column(name = "FK_BANK_CODE")
    private String bankCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_BANK_CODE", insertable = false, updatable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Bank bank;

    @Column(name = "DESTINATION_ACCOUNT_CODE")
    private String destinationAccountCode;

    @Column(name = "DESTINATION_BANK_CODE")
    private String destinationBankCode;

}
