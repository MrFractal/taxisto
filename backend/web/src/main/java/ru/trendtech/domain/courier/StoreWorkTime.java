package ru.trendtech.domain.courier;

import javax.persistence.*;

/**
 * Created by petr on 20.08.2015.
 */
@Entity
@Table(name = "c_store_work_time")
public class StoreWorkTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "c_store_id", nullable = false)
    private Store store;

    @Column(name = "day_of_week", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private DayOfWeek dayOfWeek;

    @Column(name = "start_hours", nullable = false)
    private int startHours;

    @Column(name = "start_minutes", nullable = false)
    private int startMinutes;

    @Column(name = "end_hours", nullable = false)
    private int endHours;

    @Column(name = "end_minutes", nullable = false)
    private int endMinutes;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public int getStartHours() {
        return startHours;
    }

    public void setStartHours(int startHours) {
        this.startHours = startHours;
    }

    public int getStartMinutes() {
        return startMinutes;
    }

    public void setStartMinutes(int startMinutes) {
        this.startMinutes = startMinutes;
    }

    public int getEndHours() {
        return endHours;
    }

    public void setEndHours(int endHours) {
        this.endHours = endHours;
    }

    public int getEndMinutes() {
        return endMinutes;
    }

    public void setEndMinutes(int endMinutes) {
        this.endMinutes = endMinutes;
    }

    public enum DayOfWeek {
        UNKNOWN(0),
        MONDAY(1),
        TUESDAY(2),
        WEDNESDAY(3),
        THURSDAY(4),
        FRIDAY(5),
        SATURDAY(6),
        SUNDAY(7);

        private final int value;

        DayOfWeek(int value) {
            this.value = value;
        }

        public static DayOfWeek getByValue(int value) {
            DayOfWeek result = UNKNOWN;
            for (DayOfWeek item : values()) {
                if (item.getValue() == value) {
                    result = item;
                    break;
                }
            }
            return result;
        }

        public int getValue() {
            return value;
        }
    }
}
