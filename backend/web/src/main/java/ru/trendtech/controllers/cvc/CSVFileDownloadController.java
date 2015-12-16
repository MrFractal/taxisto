package ru.trendtech.controllers.cvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;
import ru.trendtech.common.mobileexchange.model.web.GlobalDriverStatsInfo;
import ru.trendtech.common.mobileexchange.model.web.GlobalDriverStatsResponse;
import ru.trendtech.services.administration.AdministrationService;
import ru.trendtech.services.report.ReportService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by petr on 17.02.2015.
 */

@Controller
@RequestMapping("/export")
public class CSVFileDownloadController {

    @Autowired
    private ReportService reportService;


    @RequestMapping(value = "/csv")
    public void downloadCSV(HttpServletResponse response) throws IOException {
        String csvFileName = "c:\\books.csv";

        response.setContentType("text/csv");

        // creates mock data
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"", csvFileName);
        response.setHeader(headerKey, headerValue);
        response.setCharacterEncoding("UTF-8");


        //List<Book> listBooks = Arrays.asList(book1, book2, book3, book4);

        // uses the Super CSV API to generate CSV data from the model data
        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);

        String[] header = { "driverId", "autoModel", "autoYear", "autoClass",
                "login", "firstName", "lastName", "phone" , "taxopark", "assistant", "registrationDate",
                "balance", "ownDriver", "rating", "percentRatingOnCountOrder", "ratingAVG", "percentCommentOnCountOrder",
                "blockState", "dateOfBlock", "reasonOfBlock", "dateOfFirstOrder", "dateOfLastOrder", "countMission",
                "allSummMissionMoney", "averageCheckMoney", "averageCountMissionMonth", "countCanceledMissionByClient", "countCanceledMissionByDriver", "averageTimeArriving",
                "averageTimeArrivingInFact", "percentPushLateDriver", "countRepeatOrderWithClient", "countOrderWithPromo"};
/*
private long driverId;
    private String autoModel;
    private int autoYear;
    private String autoClass;
    private String login;
    private String firstName;
    private String lastName;
    private String phone;
    private String taxopark;
    private String assistant;
    private String registrationDate;
    private int balance;
    private boolean ownDriver;
    private int rating; // (10)
    private double percentRatingOnCountOrder; // процент оценок от кол-ва заказов
    private int ratingAVG; // средний бал оценки - ???
    private double percentCommentOnCountOrder; // процент комментов от кол-ва заказов
    private String blockState; //
    private String dateOfBlock;
    private String reasonOfBlock;
    private String dateOfFirstOrder;
    private String dateOfLastOrder;
    private int countMission; // общее количество выполненных заказов
    private double allSummMissionMoney; // Сумма по общему количеству выполненных заказов
    private double averageCheckMoney; // средний чек
    private double averageCountMissionMonth; // среднее кол-во заказов в месяц
    private int countCanceledMissionByClient;
    private int countCanceledMissionByDriver;

    private int averageTimeArriving; //Среднее назначенное времчя подачи
    private int averageTimeArrivingInFact; // Среднее фактическое время подачи

    private double percentPushLateDriver; // % нажатий "опаздываю" на планшете от кол-ва заказов

    private int countRepeatOrderWithClient; //кол-во повторных поездок с клиентом
    private int countOrderWithPromo;
 */

        csvWriter.writeHeader(header);

        GlobalDriverStatsResponse resp = reportService.getDriverGlobalStats(0, 0, 0, 0);
        List<GlobalDriverStatsInfo> listStat = resp.getGlobalDriverStatsInfos();

        for (GlobalDriverStatsInfo global : listStat) {
            //System.out.println("global="+global);
            csvWriter.write(global, header);
        }

        csvWriter.close();
    }
}
