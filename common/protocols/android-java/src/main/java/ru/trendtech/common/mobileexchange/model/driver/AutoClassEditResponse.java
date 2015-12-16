package ru.trendtech.common.mobileexchange.model.driver;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by petr on 29.08.14.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AutoClassEditResponse {
      private boolean edit=false;

    public boolean isEdit() {
        return edit;
    }

    public void setEdit(boolean edit) {
        this.edit = edit;
    }
}
