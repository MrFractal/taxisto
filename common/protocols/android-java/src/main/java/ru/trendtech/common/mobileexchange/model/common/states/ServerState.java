package ru.trendtech.common.mobileexchange.model.common.states;

/**
 * File created by max on 21/05/2014 14:15.
 */


public enum ServerState {
    NOT_LOGGED_IN(ServerStateType.NOT_LOGGED_IN),
    TRIP_PAUSE(ServerStateType.TRIP_PAUSE),
    DEFAULT(ServerStateType.CLIENT_DEFAULT),
    SEARCH_DRIVER(ServerStateType.SEARCH_DRIVER),
    ASSIGNED_DRIVER(ServerStateType.ASSIGNED_DRIVER),
    ARRIVED_DRIVER(ServerStateType.ARRIVED_DRIVER),
    IN_TRIP(ServerStateType.IN_TRIP),
    TRIP_FINISHED(ServerStateType.TRIP_FINISHED),
    DRIVER_AVAILABLE(ServerStateType.DRIVER_AVAILABLE),
    AUTO_SEARCH_DRIVER(ServerStateType.AUTO_SEARCH_DRIVER),
    DRIVER_BUSY(ServerStateType.DRIVER_BUSY),
    ;
    private long stateId;

    ServerState(long stateId) {
        this.stateId = stateId;
    }

    public long getStateId() {
        return stateId;
    }

    public static ServerState getByValue(long value) {
        ServerState result = NOT_LOGGED_IN;
        for (ServerState item : values()) {
            if (item.getStateId() == value) {
                result = item;
                break;
            }
        }
        return result;
    }

}
