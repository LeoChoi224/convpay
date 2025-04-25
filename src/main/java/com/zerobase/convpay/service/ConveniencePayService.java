package com.zerobase.convpay.service;

import com.zerobase.convpay.dto.PayCancelRequest;
import com.zerobase.convpay.dto.PayCancelResponse;
import com.zerobase.convpay.dto.PayRequest;
import com.zerobase.convpay.dto.PayResponse;
import com.zerobase.convpay.type.*;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class ConveniencePayService { // 편결이
    private final Map<PayMethodType, PaymentInterface> paymentInterfaceMap =
            new HashMap<>();
    private final DiscountInterface discountInterface;

    public ConveniencePayService(Set<PaymentInterface> paymentInterfaceSet,
                                 DiscountInterface discountByConvenience) {
        paymentInterfaceSet.forEach(
                paymentInterface -> paymentInterfaceMap.put(
                        paymentInterface.getPayMethodType(),
                        paymentInterface
                )
        );

        this.discountInterface = discountByConvenience;
    }

    public PayResponse pay(PayRequest payRequest) {
        PaymentInterface paymentInterface =
                paymentInterfaceMap.get(payRequest.getPayMethodType());

        Integer discountedAmount = discountInterface.getDiscountedAmount(payRequest);
        PaymentResult payment = paymentInterface.payment(discountedAmount);
        // 위처럼 작성하면 됨
//        CardUseResult cardUseResult;
//        MoneyUseResult moneyUseResult;
//
//        if (payRequest.getPayMethod() == PayMethodType.CARD) {
//            cardAdapter.authorization();
//            cardAdapter.approval();
//            cardUseResult =
//                    cardAdapter.capture(payRequest.getPayAmount());
//        } else {
//            moneyUseResult =
//                    moneyAdapter.use(payRequest.getPayAmount());
//        }
        // ---------------------------------------------------------
        // fail fast

        // Method()

        // Exception case5
        // Exception case4
        // Exception case1
        // Exception case2
        // Exception case3

        // Success Case(Only one)

//        // 좋지 못한 방법
//        if (moneyUseResult == MoneyUseResult.USE_SUCCESS) {
//            return new PayResponse(PayResult.SUCCESS, 100);
//        } else {
//            return new PayResponse(PayResult.FAIL, 100);
//        }

        // fail case 먼저 보내고 마지막에 success Case에 쓰는 걸 추천!
        if (payment == PaymentResult.PAYMENT_FAIL) {
            return new PayResponse(PayResult.FAIL, 0);
        }

        // Success Case
        return new PayResponse(PayResult.SUCCESS, discountedAmount);
    }

    public PayCancelResponse payCancel(PayCancelRequest payCancelRequest) {
        PaymentInterface paymentInterface =
                paymentInterfaceMap.get(payCancelRequest.getPayMethodType());

        CancelPaymentResult cancelPaymentResult =
                paymentInterface.cancelPayment(payCancelRequest.getPayCancelAmount());

        if (cancelPaymentResult == cancelPaymentResult.CANCEL_PAYMENT_FAIL) {
            return new PayCancelResponse(PayCancelResult.PAY_CANCEL_FAIL, 0);
        }

        return new PayCancelResponse(PayCancelResult.PAY_CANCEL_SUCCESS,
                payCancelRequest.getPayCancelAmount());
    }
}
