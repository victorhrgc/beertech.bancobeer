package beertech.becks.api.converter;

import beertech.becks.api.entities.Bank;
import beertech.becks.api.entities.PaymentSlip;
import beertech.becks.api.service.AccountService;
import beertech.becks.api.service.BankService;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.xml.bind.DatatypeConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PaymentSlipConverter {

  @Autowired
  AccountService accountService;
  @Autowired
  BankService bankService;

  public PaymentSlip codeToPaymentSlip(String code)
      throws Exception {
    String[] splitedPaymentSlip = decodeSlip(code).split("-");
    String[] originBank = splitedPaymentSlip[2].split("/");
    String[] destinationBank = splitedPaymentSlip[3].split("/");

    Bank bank = bankService.findByCode(originBank[0]);

    if(bank.getName().equalsIgnoreCase("BECKS")){
      return PaymentSlip.builder()
          .code(code)
          .dueDate(LocalDate.parse(splitedPaymentSlip[0], DateTimeFormatter.BASIC_ISO_DATE))
          .value(new BigDecimal(splitedPaymentSlip[1]).divide(BigDecimal.valueOf(100)))
          .user(accountService.getAccountByCode(originBank[1]).getUser())
          .originAccountCode(originBank[1])
          .destinationBankCode(destinationBank[0])
          .destinationAccountCode(destinationBank[1])
          .build();
    }else{
      throw new Exception();
    }
  }

  private static String decodeSlip(String text) throws UnsupportedEncodingException {
    byte[] myBytes = DatatypeConverter.parseHexBinary(text);
    return new String(myBytes,"UTF-8");
  }
}
