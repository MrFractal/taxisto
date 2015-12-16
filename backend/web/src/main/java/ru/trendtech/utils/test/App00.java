package ru.trendtech.utils.test;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.trendtech.utils.DateTimeUtils;

import java.util.Arrays;
import java.util.List;

/**
 * Created by petr on 05.11.2015.
 */
public class App00 {
    private static final Logger LOGGER = LoggerFactory.getLogger(App00.class);



    public static void main(String[] args) {
        int sumHold = 0;
        int sumRefund = 0;

        List<Integer> holdList = Arrays.asList(888);
        holdList.add(300);

        for(Integer sum: holdList){
            sumHold += sum;
        }

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("-----------------"+"\n");
        stringBuilder.append("Сумма всех холдов = " + sumHold+"\n");
        //LOGGER.info("Сумма всех холдов = " + sumHold);

        int priceInFact = 500;

        stringBuilder.append("Стоимость заказа по факту = " + priceInFact+"\n");

        if(sumHold == priceInFact){
            stringBuilder.append("Снято с клиента: " + priceInFact);
        } else if(sumHold > priceInFact){
            boolean isComplete = false;
            for (int i = 0; i < holdList.size() ; i++) {
                int hold = holdList.get(i);
                if(isComplete){
                    sumRefund += hold;
                    stringBuilder.append(" - Сумма к возврату: " + hold + " № холда: " +(i+1)+"\n");
                } else {
                    if(priceInFact > hold){
                        priceInFact = priceInFact - hold;
                        stringBuilder.append("Сумма холда: " + hold + " Комплит суммы холда: " + hold +" № холда = " + (i+1) + " Остаток: " + priceInFact +"\n");
                    } else {
                        sumRefund += (hold - priceInFact);
                        isComplete = true;
                        stringBuilder.append("Сумма холда: " + hold + " Комплит суммы холда: " + priceInFact + " № холда = " + (i + 1) + " Остаток: " + 0+"\n");
                        stringBuilder.append(" - Сумма к возврату: " + sumRefund + " № холда: " + (i+1)+"\n");
                    }
                }
            }
        }  else {
            stringBuilder.append("Сумма холдов не может быть меньше суммы заказа - EXIT ");
        }

            stringBuilder.append("Общая сумма к возврату: " + sumRefund);
            LOGGER.info(stringBuilder.toString());
    }



    /*
                if(priceInFact > 0){
                    residue = priceInFact;
                    sumToComplete = hold;
                } else if(priceInFact < 0){
                    residue = 0;
                    sumRefund = Math.abs(priceInFact);
                }
     */

}
