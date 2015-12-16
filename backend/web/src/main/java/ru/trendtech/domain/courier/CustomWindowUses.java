package ru.trendtech.domain.courier;

import ru.trendtech.domain.Client;

import javax.persistence.*;

/**
 * Created by petr on 03.09.2015.
 */
@Entity
@Table(name = "c_custom_window_uses")
public class CustomWindowUses {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @OneToOne
    @JoinColumn(name = "c_custom_window_id", nullable = false)
    private CustomWindow customWindow;

    @Column(name = "action_type", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private ActionType actionType;

    @Column(name = "is_showed" , nullable = false, columnDefinition = "BIT DEFAULT 0", length = 1)
    private boolean isShowed;

    public boolean isShowed() {
        return isShowed;
    }

    public void setIsShowed(boolean isShowed) {
        this.isShowed = isShowed;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public CustomWindow getCustomWindow() {
        return customWindow;
    }

    public void setCustomWindow(CustomWindow customWindow) {
        this.customWindow = customWindow;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }

    public enum ActionType {
        NONE,
        NOT_SHOWN, // окно не было показано юзеру
        SHOWN_AND_APPROVE, // показано клиенту и нажата кнопка
        SHOWN_AND_CLOSED // показано и закрыто юзером - значит покажем еще раз
    }
}
